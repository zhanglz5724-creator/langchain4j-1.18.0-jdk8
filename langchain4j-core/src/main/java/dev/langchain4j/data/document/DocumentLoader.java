/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.BlankDocumentException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSource;
import java.io.InputStream;

public class DocumentLoader {
    private DocumentLoader() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Document load(DocumentSource source, DocumentParser parser) {
        try (InputStream inputStream = source.inputStream();){
            Document document = parser.parse(inputStream);
            document.metadata().putAll(source.metadata().toMap());
            Document document2 = document;
            return document2;
        }
        catch (BlankDocumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load document", e);
        }
    }
}

