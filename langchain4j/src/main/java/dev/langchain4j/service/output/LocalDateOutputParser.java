/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.OutputParser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Internal
class LocalDateOutputParser
implements OutputParser<LocalDate> {
    LocalDateOutputParser() {
    }

    @Override
    public LocalDate parse(String string) {
        return LocalDate.parse(string.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String formatInstructions() {
        return "yyyy-MM-dd";
    }
}

