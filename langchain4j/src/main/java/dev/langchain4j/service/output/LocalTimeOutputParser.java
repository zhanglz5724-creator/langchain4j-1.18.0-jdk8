/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.OutputParser;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Internal
class LocalTimeOutputParser
implements OutputParser<LocalTime> {
    LocalTimeOutputParser() {
    }

    @Override
    public LocalTime parse(String string) {
        return LocalTime.parse(string.trim(), DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public String formatInstructions() {
        return "HH:mm:ss";
    }
}

