/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonGenerator$Feature
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package dev.langchain4j.model.googleai.jsonl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.googleai.jsonl.JsonLinesWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

final class StreamingJsonLinesWriter
implements JsonLinesWriter {
    private final BufferedWriter writer;
    private final ObjectMapper objectMapper;

    StreamingJsonLinesWriter(Path path) throws IOException {
        this(Files.newBufferedWriter(path, StandardCharsets.UTF_8, new OpenOption[0]), new ObjectMapper());
    }

    StreamingJsonLinesWriter(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), new ObjectMapper());
    }

    StreamingJsonLinesWriter(Writer writer, ObjectMapper objectMapper) {
        this.writer = writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer);
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(Object object) throws IOException {
        this.objectMapper.writer().without(JsonGenerator.Feature.AUTO_CLOSE_TARGET).writeValue((Writer)this.writer, object);
        this.writer.newLine();
    }

    @Override
    public void write(Iterable<?> objects) throws IOException {
        for (Object object : objects) {
            this.write(object);
        }
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }
}

