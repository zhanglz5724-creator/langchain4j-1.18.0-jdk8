/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.segment.TextSegment
 */
package dev.langchain4j.data.document.splitter.oracle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.oracle.Chunk;
import dev.langchain4j.data.segment.TextSegment;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleDocumentSplitter
implements DocumentSplitter {
    private static final String INDEX = "index";
    private final Connection conn;
    private final String pref;

    public OracleDocumentSplitter(Connection conn, String pref) {
        this.conn = conn;
        this.pref = pref;
    }

    public List<TextSegment> split(Document document) {
        ArrayList<TextSegment> segments = new ArrayList<TextSegment>();
        try {
            String[] parts = this.split(document.text());
            int index = 0;
            for (String part : parts) {
                segments.add(OracleDocumentSplitter.createSegment(part, document, index));
                ++index;
            }
        }
        catch (JsonProcessingException | SQLException ex) {
            throw new RuntimeException("cannot split document", ex);
        }
        return segments;
    }

    public List<TextSegment> splitAll(List<Document> list) {
        return super.splitAll(list);
    }

    public String[] split(String content) throws SQLException, JsonProcessingException {
        ArrayList<String> strArr = new ArrayList<String>();
        String query = "select t.column_value as data from dbms_vector_chain.utl_to_chunks(?, json(?)) t";
        try (PreparedStatement stmt = this.conn.prepareStatement(query);){
            Clob clob = this.conn.createClob();
            clob.setString(1L, content);
            stmt.setObject(1, clob);
            stmt.setObject(2, this.pref);
            try (ResultSet rs = stmt.executeQuery();){
                while (rs.next()) {
                    String text = rs.getString("data");
                    ObjectMapper mapper = new ObjectMapper();
                    Chunk chunk = (Chunk)mapper.readValue(text, Chunk.class);
                    strArr.add(chunk.getData());
                }
            }
        }
        return strArr.toArray(new String[strArr.size()]);
    }

    static TextSegment createSegment(String text, Document document, int index) {
        Metadata metadata = document.metadata().copy().put(INDEX, String.valueOf(index));
        return TextSegment.from((String)text, (Metadata)metadata);
    }
}

