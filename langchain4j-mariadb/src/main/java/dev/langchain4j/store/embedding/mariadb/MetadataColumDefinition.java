package dev.langchain4j.store.embedding.mariadb;

import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MetadataColumDefinition used to define column definition from sql String
 */
public class MetadataColumDefinition {
    private final String fullDefinition;
    private final String escapedName;
    private final String name;
    private final String type;

    public MetadataColumDefinition(String fullDefinition, String escapedName, String name, String type) {
        this.fullDefinition = fullDefinition;
        this.escapedName = escapedName;
        this.name = name;
        this.type = type;
    }

    public String getFullDefinition() {
        return fullDefinition;
    }

    public String getEscapedName() {
        return escapedName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetadataColumDefinition that = (MetadataColumDefinition) o;
        return java.util.Objects.equals(this.fullDefinition, that.fullDefinition) && java.util.Objects.equals(this.escapedName, that.escapedName) && java.util.Objects.equals(this.name, that.name) && java.util.Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fullDefinition, escapedName, name, type);
    }

    @Override
    public String toString() {
        return "MetadataColumDefinition{"fullDefinition=" + fullDefinition + , "escapedName=" + escapedName + , "name=" + name + , "type=" + type + "}"";
    }

    private static final Pattern litteralPattern =
            Pattern.compile("^(([a-zA-Z0-9_]+)|(`((``)|[^`])+`))", Pattern.DOTALL);

    /**
     * transform sql string to MetadataColumDefinition
     * @param sqlDefinition sql definition string
     * @param sqlKeywords sql reserved keywords
     * @return MetadataColumDefinition
     */
    public static MetadataColumDefinition from(String sqlDefinition, List<String> sqlKeywords) {
        String fullDefinition = ValidationUtils.ensureNotNull(sqlDefinition, "Metadata column definition")
                .trim();
        Matcher matcher = litteralPattern.matcher(sqlDefinition);
        if (matcher.find()) {
            String fieldName = matcher.group(0);
            String remainingDefinition =
                    fullDefinition.substring(fieldName.length()).trim();
            if (remainingDefinition.isEmpty()) {
                throw new IllegalArgumentException("Definition format should be: <column name> <type> "
                        + " [ NULL | NOT NULL ] [ UNIQUE ] [ DEFAULT value ]");
            }
            String escapedName = fieldName;
            String unescapedName =
                    (fieldName.startsWith("`")) ? fieldName.substring(1, fieldName.length() - 1) : fieldName;
            String type = fullDefinition
                    .substring(fieldName.length())
                    .trim()
                    .split(" ")[0]
                    .toLowerCase();

            if (!fieldName.startsWith("`") && sqlKeywords.contains(unescapedName.toLowerCase(Locale.ROOT))) {
                // if field name is a reserved keywords, force quote
                escapedName = MariaDbValidator.validateAndEnquoteIdentifier(unescapedName, true);
                fullDefinition = escapedName + fullDefinition.substring(fieldName.length());
            }
            return new MetadataColumDefinition(fullDefinition, escapedName, unescapedName, type);
        } else
            throw new IllegalArgumentException("Wrong definition format should be: <column name> <type> "
                    + " [ NULL | NOT NULL ] [ UNIQUE ] [ DEFAULT value ]");
    }
}
