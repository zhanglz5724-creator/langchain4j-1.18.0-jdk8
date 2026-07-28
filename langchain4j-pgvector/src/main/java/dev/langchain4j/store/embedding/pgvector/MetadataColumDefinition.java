/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.store.embedding.pgvector;

import dev.langchain4j.internal.ValidationUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MetadataColumDefinition {
    private final String fullDefinition;
    private final String name;
    private final String type;

    private MetadataColumDefinition(String fullDefinition, String name, String type) {
        this.fullDefinition = fullDefinition;
        this.name = name;
        this.type = type;
    }

    public static MetadataColumDefinition from(String sqlDefinition) {
        String fullDefinition = (String)ValidationUtils.ensureNotNull((Object)sqlDefinition, (String)"Metadata column definition");
        List tokens = Arrays.stream(fullDefinition.split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        if (tokens.size() < 2) {
            throw new IllegalArgumentException("Definition format should be: column type [ NULL | NOT NULL ] [ UNIQUE ] [ DEFAULT value ]");
        }
        String name = (String)tokens.get(0);
        String type = ((String)tokens.get(1)).toLowerCase();
        return new MetadataColumDefinition(fullDefinition, name, type);
    }

    public String getFullDefinition() {
        return this.fullDefinition;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }
}

