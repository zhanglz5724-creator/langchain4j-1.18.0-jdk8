/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.BlankDocumentException
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.document.DocumentParser
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.data.document.parser;

import dev.langchain4j.data.document.BlankDocumentException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.internal.ValidationUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TextDocumentParser
implements DocumentParser {
    private final Charset charset;

    public TextDocumentParser() {
        this(StandardCharsets.UTF_8);
    }

    public TextDocumentParser(Charset charset) {
        this.charset = (Charset)ValidationUtils.ensureNotNull((Object)charset, (String)"charset");
    }

    public Document parse(InputStream inputStream) {
        try {
            String text = new String(TextDocumentParser.readAllBytes(inputStream), this.charset);
            if (text.trim().isEmpty()) {
                throw new BlankDocumentException();
            }
            return Document.from((String)text);
        }
        catch (BlankDocumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws Exception {
        int n;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        while ((n = inputStream.read(data)) != -1) {
            buffer.write(data, 0, n);
        }
        return buffer.toByteArray();
    }
}

