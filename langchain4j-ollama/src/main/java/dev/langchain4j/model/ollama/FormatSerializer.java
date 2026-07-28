/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonGenerator
 *  com.fasterxml.jackson.databind.JsonSerializer
 *  com.fasterxml.jackson.databind.SerializerProvider
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

class FormatSerializer
extends JsonSerializer<String> {
    private static final String JSON = "json";

    FormatSerializer() {
    }

    public void serialize(String format, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (JSON.equals(format)) {
            jsonGenerator.writeString(JSON);
        } else {
            jsonGenerator.writeRawValue(format);
        }
    }
}

