/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import java.util.Optional;

@Internal
interface OutputParser<T> {
    public T parse(String var1);

    default public Optional<JsonSchema> jsonSchema() {
        return Optional.empty();
    }

    public String formatInstructions();
}

