/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request;

import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import java.util.Objects;

public class ResponseFormat {
    public static final ResponseFormat TEXT = ResponseFormat.builder().type(ResponseFormatType.TEXT).build();
    public static final ResponseFormat JSON = ResponseFormat.builder().type(ResponseFormatType.JSON).build();
    private final ResponseFormatType type;
    private final JsonSchema jsonSchema;

    private ResponseFormat(Builder builder) {
        this.type = ValidationUtils.ensureNotNull(builder.type, "type");
        this.jsonSchema = builder.jsonSchema;
        if (this.jsonSchema != null && this.type != ResponseFormatType.JSON) {
            throw new IllegalStateException("JsonSchema can be specified only when type=JSON");
        }
    }

    public ResponseFormatType type() {
        return this.type;
    }

    public JsonSchema jsonSchema() {
        return this.jsonSchema;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ResponseFormat that = (ResponseFormat)o;
        return Objects.equals((Object)this.type, (Object)that.type) && Objects.equals(this.jsonSchema, that.jsonSchema);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(new Object[]{this.type, this.jsonSchema});
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ResponseFormat { type = " + (Object)((Object)this.type) + ", jsonSchema = " + this.jsonSchema + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ResponseFormatType type;
        private JsonSchema jsonSchema;

        public Builder type(ResponseFormatType type) {
            this.type = type;
            return this;
        }

        public Builder jsonSchema(JsonSchema jsonSchema) {
            this.jsonSchema = jsonSchema;
            return this;
        }

        public ResponseFormat build() {
            return new ResponseFormat(this);
        }
    }
}

