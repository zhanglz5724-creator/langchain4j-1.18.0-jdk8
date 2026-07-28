/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.document.Metadata
 *  org.jsoup.Jsoup
 *  org.jsoup.nodes.Document
 *  org.jsoup.nodes.Element
 *  org.jsoup.select.Elements
 */
package dev.langchain4j.data.document.loader.oracle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.loader.oracle.DirectoryPreference;
import dev.langchain4j.data.document.loader.oracle.FilePreference;
import dev.langchain4j.data.document.loader.oracle.TablePreference;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OracleDocumentLoader {
    private final Connection conn;
    private final String COLUMN_ROWID = "rowid";
    private final String COLUMN_TEXT = "text";
    private final String COLUMN_METADATA = "metadata";
    private static final String META_TAG = "meta";
    private static final String META_NAME_ATTR = "name";
    private static final String META_CONTENT_ATTR = "content";

    public OracleDocumentLoader(Connection conn) {
        this.conn = conn;
    }

    public List<Document> loadDocuments(String pref) throws IOException, SQLException {
        ArrayList<Document> documents = new ArrayList<Document>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(pref);
        if (rootNode.has("file")) {
            FilePreference filePref;
            try {
                filePref = (FilePreference)mapper.readValue(pref, FilePreference.class);
            }
            catch (UnrecognizedPropertyException ex) {
                throw new InvalidParameterException("Invalid file preference: unknown property specified");
            }
            String filename = filePref.getFilename();
            Document doc = this.loadDocument(filename, pref);
            if (doc != null) {
                documents.add(doc);
            }
        } else if (rootNode.has("dir")) {
            DirectoryPreference dirPref;
            try {
                dirPref = (DirectoryPreference)mapper.readValue(pref, DirectoryPreference.class);
            }
            catch (UnrecognizedPropertyException ex) {
                throw new InvalidParameterException("Invalid directory preference: unknown property specified");
            }
            String dir = dirPref.getDirectory();
            Path root = Paths.get(dir, new String[0]);
            Files.walk(root, new FileVisitOption[0]).forEach(path -> {
                if (path.toFile().isFile()) {
                    Document doc = null;
                    try {
                        doc = this.loadDocument(path.toFile().toString(), pref);
                        if (doc != null) {
                            documents.add(doc);
                        }
                    }
                    catch (IOException | SQLException ex) {
                        throw new RuntimeException("cannot load document", ex);
                    }
                }
            });
        } else if (rootNode.has("tablename")) {
            TablePreference tablePref;
            try {
                tablePref = (TablePreference)mapper.readValue(pref, TablePreference.class);
            }
            catch (UnrecognizedPropertyException ex) {
                throw new InvalidParameterException("Invalid table preference: unknown property specified");
            }
            if (!tablePref.isValid()) {
                throw new InvalidParameterException("Invalid table preference: missing owner, table, or column name");
            }
            String owner = tablePref.getOwner();
            String table = tablePref.getTableName();
            String column = tablePref.getColumnName();
            documents.addAll(this.loadDocuments(owner, table, column, pref));
        } else {
            throw new InvalidParameterException("Invalid preference: missing filename, directory, or table");
        }
        return documents;
    }

    private Document loadDocument(String filename, String pref) throws IOException, SQLException {
        Document document = null;
        byte[] bytes = Files.readAllBytes(Paths.get(filename, new String[0]));
        String query = "select dbms_vector_chain.utl_to_text(?, json(?)) text, dbms_vector_chain.utl_to_text(?, json('{\"plaintext\": \"false\"}')) metadata from dual";
        try (PreparedStatement stmt = this.conn.prepareStatement(query);){
            Blob blob = this.conn.createBlob();
            blob.setBytes(1L, bytes);
            stmt.setBlob(1, blob);
            stmt.setObject(2, pref);
            stmt.setBlob(3, blob);
            try (ResultSet rs = stmt.executeQuery();){
                while (rs.next()) {
                    String text = rs.getString("text");
                    String html = rs.getString("metadata");
                    Metadata metadata = OracleDocumentLoader.getMetadata(html);
                    Path path = Paths.get(filename, new String[0]);
                    metadata.put("file_name", path.getFileName().toString());
                    metadata.put("absolute_directory_path", path.getParent().toString());
                    document = Document.from((String)text, (Metadata)metadata);
                }
            }
        }
        return document;
    }

    private List<Document> loadDocuments(String owner, String table, String column, String pref) throws SQLException {
        ArrayList<Document> documents = new ArrayList<Document>();
        String query = String.format("select rowid, dbms_vector_chain.utl_to_text(t.%s, json(?)) text, dbms_vector_chain.utl_to_text(t.%s, json('{\"plaintext\": \"false\"}')) metadata from %s.%s t", column, column, owner, table);
        try (PreparedStatement stmt = this.conn.prepareStatement(query);){
            stmt.setObject(1, pref);
            try (ResultSet rs = stmt.executeQuery();){
                while (rs.next()) {
                    String rowid = rs.getString("rowid");
                    String text = rs.getString("text");
                    String html = rs.getString("metadata");
                    Metadata metadata = OracleDocumentLoader.getMetadata(html);
                    metadata.put("table", table);
                    metadata.put("column", column);
                    metadata.put("rowid", rowid);
                    Document doc = Document.from((String)text, (Metadata)metadata);
                    documents.add(doc);
                }
            }
        }
        return documents;
    }

    private static Metadata getMetadata(String html) {
        Metadata metadata = new Metadata();
        org.jsoup.nodes.Document doc = Jsoup.parse((String)html);
        Elements metaTags = doc.getElementsByTag(META_TAG);
        for (Element metaTag : metaTags) {
            String name = metaTag.attr(META_NAME_ATTR);
            if (name.isEmpty()) continue;
            String content = metaTag.attr(META_CONTENT_ATTR);
            metadata.put(name, content);
        }
        return metadata;
    }
}

