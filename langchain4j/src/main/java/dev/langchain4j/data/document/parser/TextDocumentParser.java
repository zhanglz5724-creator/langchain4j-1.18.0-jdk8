package dev.langchain4j.data.document.parser;

import dev.langchain4j.data.document.BlankDocumentException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static dev.langchain4j.internal.ValidationUtils.ensureNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

public class TextDocumentParser implements DocumentParser {

    private final Charset charset;

    public TextDocumentParser() {
        this(UTF_8);
    }

    public TextDocumentParser(Charset charset) {
        this.charset = ensureNotNull(charset, "charset");
    }

    @Override
    public Document parse(InputStream inputStream) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int n;
            byte[] data = new byte[4096];
            while ((n = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, n);
            }
            String text = new String(buffer.toByteArray(), charset);
            if (text.trim().isEmpty()) {
                throw new BlankDocumentException();
            }
            return Document.from(text);
        } catch (BlankDocumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
