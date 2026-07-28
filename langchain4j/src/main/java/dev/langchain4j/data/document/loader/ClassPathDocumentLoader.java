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
import dev.langchain4j.data.document.source.ClassPathSource;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassPathDocumentLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathDocumentLoader.class);
    private static final DocumentParser DEFAULT_DOCUMENT_PARSER = (DocumentParser)Utils.getOrDefault((Object)DocumentParserLoader.loadDocumentParser(), TextDocumentParser::new);

    private ClassPathDocumentLoader() {
    }

    public static Document loadDocument(String pathOnClasspath) {
        return ClassPathDocumentLoader.loadDocument(pathOnClasspath, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static Document loadDocument(String pathOnClasspath, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocument(pathOnClasspath, DEFAULT_DOCUMENT_PARSER, classLoader);
    }

    public static Document loadDocument(String pathOnClasspath, DocumentParser documentParser) {
        return ClassPathDocumentLoader.loadDocument(pathOnClasspath, documentParser, ClassPathDocumentLoader.getDefaultClassloader());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Document loadDocument(String pathOnClasspath, DocumentParser documentParser, ClassLoader classLoader) {
        ClassPathSource classPathSource = ClassPathSource.from(pathOnClasspath, classLoader);
        try {
            URI uri = classPathSource.url().toURI();
            if (!classPathSource.isInsideArchive()) return ClassPathDocumentLoader.loadDocument(classPathSource, Paths.get(uri), documentParser);
            try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.singletonMap("create", "true"));){
                Document document = ClassPathDocumentLoader.loadDocument(classPathSource, fs.getPath(pathOnClasspath, new String[0]), documentParser);
                return document;
            }
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document loadDocument(ClassPathSource classPathSource, Path path, DocumentParser documentParser) {
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a file", (Object[])new Object[]{path});
        }
        return DocumentLoader.load((DocumentSource)classPathSource, (DocumentParser)documentParser);
    }

    public static List<Document> loadDocuments(String directoryOnClasspath) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, DEFAULT_DOCUMENT_PARSER, classLoader);
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, DocumentParser documentParser) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, documentParser, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, DocumentParser documentParser, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, path -> true, documentParser, classLoader);
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, PathMatcher pathMatcher) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, pathMatcher, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, PathMatcher pathMatcher, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, pathMatcher, DEFAULT_DOCUMENT_PARSER, classLoader);
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, PathMatcher pathMatcher, DocumentParser documentParser) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, pathMatcher, documentParser, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocuments(String directoryOnClasspath, PathMatcher pathMatcher, DocumentParser documentParser, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, pathMatcher, documentParser, classLoader, ClassPathDocumentLoader::getFilesInDirectory);
    }

    private static ClassLoader getDefaultClassloader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<Document> loadDocuments(String directoryOnClasspath, PathMatcher pathMatcher, DocumentParser documentParser, ClassLoader classLoader, Function<Path, Stream<Path>> pathStreamFunction) {
        ClassPathSource classPathSource2 = ClassPathSource.from(directoryOnClasspath, classLoader);
        try {
            URI uri2 = classPathSource2.url().toURI();
            if (!classPathSource2.isInsideArchive()) return ClassPathDocumentLoader.loadDocuments(classPathSource2, directoryOnClasspath, Paths.get(uri2), pathMatcher, documentParser, pathStreamFunction);
            try (FileSystem fs2 = FileSystems.newFileSystem(uri2, Collections.singletonMap("create", "true"));){
                List<Document> list = ClassPathDocumentLoader.loadDocuments(classPathSource2, directoryOnClasspath, fs2.getPath(directoryOnClasspath, new String[0]), pathMatcher, documentParser, pathStreamFunction);
                return list;
            }
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Document> loadDocuments(ClassPathSource rootDirectoryClassPathSource, String directoryOnClasspath, Path path, PathMatcher pathMatcher, DocumentParser documentParser, Function<Path, Stream<Path>> pathStreamFunction) {
        if (!Files.isDirectory(path, new LinkOption[0])) {
            throw Exceptions.illegalArgument((String)"'%s' is not a directory", (Object[])new Object[]{path});
        }
        try (Stream<Path> pathStream = pathStreamFunction.apply(path);){
            List<Document> list = ClassPathDocumentLoader.loadDocuments(pathStream, rootDirectoryClassPathSource, directoryOnClasspath, path, pathMatcher, documentParser);
            return list;
        }
    }

    private static List<Document> loadDocuments(Stream<Path> pathStream, ClassPathSource rootDirectoryClassPathSource, String directoryOnClasspath, Path pathMatcherRoot, PathMatcher pathMatcher, DocumentParser documentParser) {
        return pathStream.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(p -> pathMatcher.matches(Paths.get(pathMatcherRoot.relativize((Path)p).toString().replace('/', File.separatorChar), new String[0]))).map(p -> {
            try {
                String relativePath = ClassPathDocumentLoader.getRelativePath(directoryOnClasspath, rootDirectoryClassPathSource, p);
                return ClassPathDocumentLoader.loadDocument(ClassPathSource.from(relativePath, rootDirectoryClassPathSource.classLoader()), p, documentParser);
            }
            catch (BlankDocumentException ignored) {
                return null;
            }
            catch (Exception e) {
                String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                LOG.warn("Failed to load '{}': {}", p, (Object)message);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static String getRelativePath(String directoryOnClasspath, ClassPathSource rootDirectoryClassPathSource, Path subPath) {
        if (rootDirectoryClassPathSource.isInsideArchive()) {
            return subPath.toString();
        }
        try {
            boolean isClasspathRoot;
            Path rootClasspathPath = Paths.get(rootDirectoryClassPathSource.url().toURI());
            boolean bl = isClasspathRoot = ".".equals(directoryOnClasspath) || "/".equals(directoryOnClasspath);
            if (!isClasspathRoot) {
                String withoutLeadingAndTrailingSpaces = directoryOnClasspath.trim().replaceAll("^/+", "").replaceAll("/+$", "");
                long numDirs = withoutLeadingAndTrailingSpaces.chars().filter(c -> c == 47).count() + 1L;
                rootClasspathPath = IntStream.range(0, (int)numDirs).mapToObj(index -> "..").reduce(rootClasspathPath, Path::resolve, (a, b) -> b).normalize();
            }
            Path relativeClasspathPath = rootClasspathPath.relativize(subPath);
            return relativeClasspathPath.toString().replace(File.separatorChar, '/');
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, DEFAULT_DOCUMENT_PARSER, classLoader);
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, DocumentParser documentParser) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, documentParser, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, DocumentParser documentParser, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, path -> true, documentParser, classLoader);
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, PathMatcher pathMatcher) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, pathMatcher, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, PathMatcher pathMatcher, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, pathMatcher, DEFAULT_DOCUMENT_PARSER, classLoader);
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, PathMatcher pathMatcher, DocumentParser documentParser) {
        return ClassPathDocumentLoader.loadDocumentsRecursively(directoryOnClasspath, pathMatcher, documentParser, ClassPathDocumentLoader.getDefaultClassloader());
    }

    public static List<Document> loadDocumentsRecursively(String directoryOnClasspath, PathMatcher pathMatcher, DocumentParser documentParser, ClassLoader classLoader) {
        return ClassPathDocumentLoader.loadDocuments(directoryOnClasspath, pathMatcher, documentParser, classLoader, ClassPathDocumentLoader::getFilesInDirectoryRecursively);
    }

    private static Stream<Path> getFilesInDirectory(Path directoryPath) {
        try {
            return Files.list(directoryPath);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Path> getFilesInDirectoryRecursively(Path directoryPath) {
        try {
            return Files.walk(directoryPath, new FileVisitOption[0]);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

