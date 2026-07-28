/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.mariadb.MariaDbValidator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataColumDefinition {
    private final String fullDefinition;
    private final String escapedName;
    private final String name;
    private final String type;
    private static final Pattern litteralPattern = Pattern.compile("^(([a-zA-Z0-9_]+)|(`((``)|[^`])+`))", 32);

    public MetadataColumDefinition(String fullDefinition, String escapedName, String name, String type) {
        this.fullDefinition = fullDefinition;
        this.escapedName = escapedName;
        this.name = name;
        this.type = type;
    }

    public String fullDefinition() {
        return this.fullDefinition;
    }

    public String escapedName() {
        return this.escapedName;
    }

    public String name() {
        return this.name;
    }

    public String type() {
        return this.type;
    }

    public static MetadataColumDefinition from(String sqlDefinition, List<String> sqlKeywords) {
        String fullDefinition = ((String)ValidationUtils.ensureNotNull((Object)sqlDefinition, (String)"Metadata column definition")).trim();
        Matcher matcher = litteralPattern.matcher(sqlDefinition);
        if (matcher.find()) {
            String fieldName = matcher.group(0);
            String remainingDefinition = fullDefinition.substring(fieldName.length()).trim();
            if (remainingDefinition.isEmpty()) {
                throw new IllegalArgumentException("Definition format should be: <column name> <type>  [ NULL | NOT NULL ] [ UNIQUE ] [ DEFAULT value ]");
            }
            String escapedName = fieldName;
            String unescapedName = fieldName.startsWith("`") ? fieldName.substring(1, fieldName.length() - 1) : fieldName;
            String type = fullDefinition.substring(fieldName.length()).trim().split(" ")[0].toLowerCase();
            if (!fieldName.startsWith("`") && sqlKeywords.contains(unescapedName.toLowerCase(Locale.ROOT))) {
                escapedName = MariaDbValidator.validateAndEnquoteIdentifier(unescapedName, true);
                fullDefinition = escapedName + fullDefinition.substring(fieldName.length());
            }
            return new MetadataColumDefinition(fullDefinition, escapedName, unescapedName, type);
        }
        throw new IllegalArgumentException("Wrong definition format should be: <column name> <type>  [ NULL | NOT NULL ] [ UNIQUE ] [ DEFAULT value ]");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetadataColumDefinition)) {
            return false;
        }
        MetadataColumDefinition that = (MetadataColumDefinition)o;
        return Objects.equals(this.fullDefinition, that.fullDefinition) && Objects.equals(this.escapedName, that.escapedName) && Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type);
    }

    public int hashCode() {
        return Objects.hash(this.fullDefinition, this.escapedName, this.name, this.type);
    }

    public String toString() {
        return "MetadataColumDefinition[fullDefinition=" + this.fullDefinition + ", escapedName=" + this.escapedName + ", name=" + this.name + ", type=" + this.type + "]";
    }
}

