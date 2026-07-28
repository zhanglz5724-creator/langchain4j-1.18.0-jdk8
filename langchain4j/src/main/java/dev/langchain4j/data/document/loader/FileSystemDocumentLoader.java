/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.BlankDocumentException
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.document.DocumentLoader
 *  dev.langchain4j.data.document.DocumentParser
 *  dev.langchain4j.data.document.DocumentSource
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.data.document.loader;

import dev.langchain4j.data.document.BlankDocumentException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentLoader;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.loader.DocumentParserLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.source.FileSystemSource;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemDocumentLoader {
    private static final Logger log = LoggerFactory.getLogger(FileSystemDocumentLoader.class);
    private static final DocumentParser DEFAULT_DOCUMENT_PARSER = (DocumentParser)Utils.getOrDefault((Object)DocumentParserLoader.loadDocumentParser(), TextDocumentParser::new);

    private FileSystemDocumentLoader() {
    }

    public static Document loadDocument(Path filePath, DocumentParser documentParser) {
        if (!Files.isRegularFile(filePath, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a file", (Object[])new Object[]{filePath});
        }
        return DocumentLoader.load((DocumentSource)FileSystemSource.from(filePath), (DocumentParser)documentParser);
    }

    public static Document loadDocument(Path filePath) {
        return FileSystemDocumentLoader.loadDocument(filePath, DEFAULT_DOCUMENT_PARSER);
    }

    public static Document loadDocument(String filePath, DocumentParser documentParser) {
        return FileSystemDocumentLoader.loadDocument(Paths.get(filePath, new String[0]), documentParser);
    }

    public static Document loadDocument(String filePath) {
        return FileSystemDocumentLoader.loadDocument(filePath, DEFAULT_DOCUMENT_PARSER);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<Document> loadDocuments(Path directoryPath, DocumentParser documentParser) {
        if (!Files.isDirectory(directoryPath, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a directory", (Object[])new Object[]{directoryPath});
        }
        try (Stream<Path> pathStream = Files.list(directoryPath);){
            List<Document> list = FileSystemDocumentLoader.loadDocuments(pathStream, path -> true, directoryPath, documentParser);
            return list;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Document> loadDocuments(Path directoryPath) {
        return FileSystemDocumentLoader.loadDocuments(directoryPath, DEFAULT_DOCUMENT_PARSER);
    }

    public static List<Document> loadDocuments(String directoryPath, DocumentParser documentParser) {
        return FileSystemDocumentLoader.loadDocuments(Paths.get(directoryPath, new String[0]), documentParser);
    }

    public static List<Document> loadDocuments(String directoryPath) {
        return FileSystemDocumentLoader.loadDocuments(directoryPath, DEFAULT_DOCUMENT_PARSER);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<Document> loadDocuments(Path directoryPath, PathMatcher pathMatcher, DocumentParser documentParser) {
        if (!Files.isDirectory(directoryPath, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a directory", (Object[])new Object[]{directoryPath});
        }
        try (Stream<Path> pathStream = Files.list(directoryPath);){
            List<Document> list = FileSystemDocumentLoader.loadDocuments(pathStream, pathMatcher, directoryPath, documentParser);
            return list;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Document> loadDocuments(Path directoryPath, PathMatcher pathMatcher) {
        return FileSystemDocumentLoader.loadDocuments(directoryPath, pathMatcher, DEFAULT_DOCUMENT_PARSER);
    }

    public static List<Document> loadDocuments(String directoryPath, PathMatcher pathMatcher, DocumentParser documentParser) {
        return FileSystemDocumentLoader.loadDocuments(Paths.get(directoryPath, new String[0]), pathMatcher, documentParser);
    }

    public static List<Document> loadDocuments(String directoryPath, PathMatcher pathMatcher) {
        return FileSystemDocumentLoader.loadDocuments(directoryPath, pathMatcher, DEFAULT_DOCUMENT_PARSER);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<Document> loadDocumentsRecursively(Path directoryPath, DocumentParser documentParser) {
        if (!Files.isDirectory(directoryPath, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a directory", (Object[])new Object[]{directoryPath});
        }
        try (Stream<Path> pathStream = Files.walk(directoryPath, new FileVisitOption[0]);){
            List<Document> list = FileSystemDocumentLoader.loadDocuments(pathStream, path -> true, directoryPath, documentParser);
            return list;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Document> loadDocumentsRecursively(Path directoryPath) {
        return FileSystemDocumentLoader.loadDocumentsRecursively(directoryPath, DEFAULT_DOCUMENT_PARSER);
    }

    public static List<Document> loadDocumentsRecursively(String directoryPath, DocumentParser documentParser) {
        return FileSystemDocumentLoader.loadDocumentsRecursively(Paths.get(directoryPath, new String[0]), documentParser);
    }

    public static List<Document> loadDocumentsRecursively(String directoryPath) {
        return FileSystemDocumentLoader.loadDocumentsRecursively(directoryPath, DEFAULT_DOCUMENT_PARSER);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<Document> loadDocumentsRecursively(Path directoryPath, PathMatcher pathMatcher, DocumentParser documentParser) {
        if (!Files.isDirectory(directoryPath, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a directory", (Object[])new Object[]{directoryPath});
        }
        try (Stream<Path> pathStream = Files.walk(directoryPath, new FileVisitOption[0]);){
            List<Document> list = FileSystemDocumentLoader.loadDocuments(pathStream, pathMatcher, directoryPath, documentParser);
            return list;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Document> loadDocumentsRecursively(Path directoryPath, PathMatcher pathMatcher) {
        return FileSystemDocumentLoader.loadDocumentsRecursively(directoryPath, pathMatcher, DEFAULT_DOCUMENT_PARSER);
    }

    public static List<Document> loadDocumentsRecursively(String directoryPath, PathMatcher pathMatcher, DocumentParser documentParser) {
        return FileSystemDocumentLoader.loadDocumentsRecursively(Paths.get(directoryPath, new String[0]), pathMatcher, documentParser);
    }

    public static List<Document> loadDocumentsRecursively(String directoryPath, PathMatcher pathMatcher) {
        return FileSystemDocumentLoader.loadDocumentsRecursively(directoryPath, pathMatcher, DEFAULT_DOCUMENT_PARSER);
    }

    private static List<Document> loadDocuments(Stream<Path> pathStream, PathMatcher pathMatcher, Path pathMatcherRoot, DocumentParser documentParser) {
        ArrayList<Document> documents = new ArrayList<Document>();
        pathStream.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).map(pathMatcherRoot::relativize).filter(pathMatcher::matches).map(pathMatcherRoot::resolve).forEach(file -> {
            try {
                Document document = FileSystemDocumentLoader.loadDocument(file, documentParser);
                documents.add(document);
            }
            catch (BlankDocumentException document) {
            }
            catch (Exception e) {
                String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                log.warn("Failed to load '{}': {}", file, (Object)message);
            }
        });
        return documents;
    }
}

