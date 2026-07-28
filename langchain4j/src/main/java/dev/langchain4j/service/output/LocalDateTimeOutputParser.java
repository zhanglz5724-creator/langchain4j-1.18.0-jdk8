/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.OutputParser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Internal
class LocalDateTimeOutputParser
implements OutputParser<LocalDateTime> {
    LocalDateTimeOutputParser() {
    }

    @Override
    public LocalDateTime parse(String string) {
        return LocalDateTime.parse(string.trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String formatInstructions() {
        return "yyyy-MM-ddTHH:mm:ss";
    }
}

