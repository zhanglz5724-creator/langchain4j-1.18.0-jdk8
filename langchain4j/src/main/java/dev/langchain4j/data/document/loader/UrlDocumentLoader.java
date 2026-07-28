/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.document.DocumentLoader
 *  dev.langchain4j.data.document.DocumentParser
 *  dev.langchain4j.data.document.DocumentSource
 */
package dev.langchain4j.data.document.loader;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentLoader;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.source.UrlSource;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlDocumentLoader {
    public static Document load(URL url, DocumentParser documentParser) {
        return DocumentLoader.load((DocumentSource)UrlSource.from(url), (DocumentParser)documentParser);
    }

    public static Document load(String url, DocumentParser documentParser) {
        return UrlDocumentLoader.load(UrlDocumentLoader.createUrl(url), documentParser);
    }

    static URL createUrl(String url) {
        try {
            return new URL(url);
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

