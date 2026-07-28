/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.googleai.GeminiMediaResolution;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiContent {
    private final List<GeminiPart> parts;
    private final String role;

    @JsonCreator
    GeminiContent(@JsonProperty(value="parts") List<GeminiPart> parts, @JsonProperty(value="role") String role) {
        this.parts = Utils.mutableCopy(parts);
        this.role = role;
    }

    List<GeminiPart> parts() {
        return this.parts;
    }

    String role() {
        return this.role;
    }

    void addPart(GeminiPart part) {
        this.parts.add(part);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiContent)) {
            return false;
        }
        GeminiContent that = (GeminiContent)o;
        return Objects.equals(this.parts, that.parts) && Objects.equals(this.role, that.role);
    }

    public int hashCode() {
        return Objects.hash(this.parts, this.role);
    }

    public String toString() {
        return "GeminiContent[parts=" + this.parts + ", role=" + this.role + "]";
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiPart {
        private final String text;
        private final GeminiBlob inlineData;
        private final GeminiFunctionCall functionCall;
        private final GeminiFunctionResponse functionResponse;
        private final GeminiFileData fileData;
        private final GeminiExecutableCode executableCode;
        private final GeminiCodeExecutionResult codeExecutionResult;
        private final Boolean thought;
        private final String thoughtSignature;
        private final GeminiMediaResolution mediaResolution;

        @JsonCreator
        GeminiPart(@JsonProperty(value="text") String text, @JsonProperty(value="inlineData") GeminiBlob inlineData, @JsonProperty(value="functionCall") GeminiFunctionCall functionCall, @JsonProperty(value="functionResponse") GeminiFunctionResponse functionResponse, @JsonProperty(value="fileData") GeminiFileData fileData, @JsonProperty(value="executableCode") GeminiExecutableCode executableCode, @JsonProperty(value="codeExecutionResult") GeminiCodeExecutionResult codeExecutionResult, @JsonProperty(value="thought") Boolean thought, @JsonProperty(value="thoughtSignature") String thoughtSignature, @JsonProperty(value="mediaResolution") GeminiMediaResolution mediaResolution) {
            this.text = text;
            this.inlineData = inlineData;
            this.functionCall = functionCall;
            this.functionResponse = functionResponse;
            this.fileData = fileData;
            this.executableCode = executableCode;
            this.codeExecutionResult = codeExecutionResult;
            this.thought = thought;
            this.thoughtSignature = thoughtSignature;
            this.mediaResolution = mediaResolution;
        }

        String text() {
            return this.text;
        }

        GeminiBlob inlineData() {
            return this.inlineData;
        }

        GeminiFunctionCall functionCall() {
            return this.functionCall;
        }

        GeminiFunctionResponse functionResponse() {
            return this.functionResponse;
        }

        GeminiFileData fileData() {
            return this.fileData;
        }

        GeminiExecutableCode executableCode() {
            return this.executableCode;
        }

        GeminiCodeExecutionResult codeExecutionResult() {
            return this.codeExecutionResult;
        }

        Boolean thought() {
            return this.thought;
        }

        String thoughtSignature() {
            return this.thoughtSignature;
        }

        GeminiMediaResolution mediaResolution() {
            return this.mediaResolution;
        }

        static GeminiPart ofText(String text) {
            return GeminiPart.builder().text(text).build();
        }

        static Builder builder() {
            return new Builder();
        }

        Boolean isThought() {
            return this.thought;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiPart)) {
                return false;
            }
            GeminiPart that = (GeminiPart)o;
            return Objects.equals(this.text, that.text) && Objects.equals(this.inlineData, that.inlineData) && Objects.equals(this.functionCall, that.functionCall) && Objects.equals(this.functionResponse, that.functionResponse) && Objects.equals(this.fileData, that.fileData) && Objects.equals(this.executableCode, that.executableCode) && Objects.equals(this.codeExecutionResult, that.codeExecutionResult) && Objects.equals(this.thought, that.thought) && Objects.equals(this.thoughtSignature, that.thoughtSignature) && Objects.equals(this.mediaResolution, that.mediaResolution);
        }

        public int hashCode() {
            return Objects.hash(this.text, this.inlineData, this.functionCall, this.functionResponse, this.fileData, this.executableCode, this.codeExecutionResult, this.thought, this.thoughtSignature, this.mediaResolution);
        }

        public String toString() {
            return "GeminiPart[text=" + this.text + ", inlineData=" + this.inlineData + ", functionCall=" + this.functionCall + ", functionResponse=" + this.functionResponse + ", fileData=" + this.fileData + ", executableCode=" + this.executableCode + ", codeExecutionResult=" + this.codeExecutionResult + ", thought=" + this.thought + ", thoughtSignature=" + this.thoughtSignature + ", mediaResolution=" + this.mediaResolution + "]";
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiCodeExecutionResult {
            private final GeminiOutcome outcome;
            private final String output;

            @JsonCreator
            GeminiCodeExecutionResult(@JsonProperty(value="outcome") GeminiOutcome outcome, @JsonProperty(value="output") String output) {
                this.outcome = outcome;
                this.output = output;
            }

            GeminiOutcome outcome() {
                return this.outcome;
            }

            String output() {
                return this.output;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiCodeExecutionResult)) {
                    return false;
                }
                GeminiCodeExecutionResult that = (GeminiCodeExecutionResult)o;
                return this.outcome == that.outcome && Objects.equals(this.output, that.output);
            }

            public int hashCode() {
                return Objects.hash(new Object[]{this.outcome, this.output});
            }

            public String toString() {
                return "GeminiCodeExecutionResult[outcome=" + (Object)((Object)this.outcome) + ", output=" + this.output + "]";
            }

            static enum GeminiOutcome {
                OUTCOME_UNSPECIFIED,
                OUTCOME_OK,
                OUTCOME_FAILED,
                OUTCOME_DEADLINE_EXCEEDED;


                public String toString() {
                    return this.name().toLowerCase();
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiExecutableCode {
            private final GeminiLanguage programmingLanguage;
            private final String code;

            @JsonCreator
            GeminiExecutableCode(@JsonProperty(value="programmingLanguage") GeminiLanguage programmingLanguage, @JsonProperty(value="code") String code) {
                this.programmingLanguage = programmingLanguage == null ? GeminiLanguage.PYTHON : programmingLanguage;
                this.code = code;
            }

            GeminiLanguage programmingLanguage() {
                return this.programmingLanguage;
            }

            String code() {
                return this.code;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiExecutableCode)) {
                    return false;
                }
                GeminiExecutableCode that = (GeminiExecutableCode)o;
                return this.programmingLanguage == that.programmingLanguage && Objects.equals(this.code, that.code);
            }

            public int hashCode() {
                return Objects.hash(new Object[]{this.programmingLanguage, this.code});
            }

            public String toString() {
                return "GeminiExecutableCode[programmingLanguage=" + (Object)((Object)this.programmingLanguage) + ", code=" + this.code + "]";
            }

            static enum GeminiLanguage {
                PYTHON,
                LANGUAGE_UNSPECIFIED;


                public String toString() {
                    return this.name().toLowerCase();
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiFileData {
            private final String mimeType;
            private final String fileUri;

            @JsonCreator
            GeminiFileData(@JsonProperty(value="mimeType") String mimeType, @JsonProperty(value="fileUri") String fileUri) {
                this.mimeType = mimeType;
                this.fileUri = fileUri;
            }

            String mimeType() {
                return this.mimeType;
            }

            String fileUri() {
                return this.fileUri;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiFileData)) {
                    return false;
                }
                GeminiFileData that = (GeminiFileData)o;
                return Objects.equals(this.mimeType, that.mimeType) && Objects.equals(this.fileUri, that.fileUri);
            }

            public int hashCode() {
                return Objects.hash(this.mimeType, this.fileUri);
            }

            public String toString() {
                return "GeminiFileData[mimeType=" + this.mimeType + ", fileUri=" + this.fileUri + "]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiFunctionResponse {
            private final String id;
            private final String name;
            private final Map<String, String> response;

            @JsonCreator
            GeminiFunctionResponse(@JsonProperty(value="id") String id, @JsonProperty(value="name") String name, @JsonProperty(value="response") Map<String, String> response) {
                this.id = id;
                this.name = name;
                this.response = response;
            }

            String id() {
                return this.id;
            }

            String name() {
                return this.name;
            }

            Map<String, String> response() {
                return this.response;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiFunctionResponse)) {
                    return false;
                }
                GeminiFunctionResponse that = (GeminiFunctionResponse)o;
                return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name) && Objects.equals(this.response, that.response);
            }

            public int hashCode() {
                return Objects.hash(this.id, this.name, this.response);
            }

            public String toString() {
                return "GeminiFunctionResponse[id=" + this.id + ", name=" + this.name + ", response=" + this.response + "]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiFunctionCall {
            private final String id;
            private final String name;
            private final Map<String, Object> args;

            @JsonCreator
            GeminiFunctionCall(@JsonProperty(value="id") String id, @JsonProperty(value="name") String name, @JsonProperty(value="args") Map<String, Object> args) {
                this.id = id;
                this.name = name;
                this.args = args;
            }

            String id() {
                return this.id;
            }

            String name() {
                return this.name;
            }

            Map<String, Object> args() {
                return this.args;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiFunctionCall)) {
                    return false;
                }
                GeminiFunctionCall that = (GeminiFunctionCall)o;
                return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name) && Objects.equals(this.args, that.args);
            }

            public int hashCode() {
                return Objects.hash(this.id, this.name, this.args);
            }

            public String toString() {
                return "GeminiFunctionCall[id=" + this.id + ", name=" + this.name + ", args=" + this.args + "]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiBlob {
            private final String mimeType;
            private final String data;

            @JsonCreator
            GeminiBlob(@JsonProperty(value="mimeType") String mimeType, @JsonProperty(value="data") String data) {
                this.mimeType = mimeType;
                this.data = data;
            }

            String mimeType() {
                return this.mimeType;
            }

            String data() {
                return this.data;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiBlob)) {
                    return false;
                }
                GeminiBlob that = (GeminiBlob)o;
                return Objects.equals(this.mimeType, that.mimeType) && Objects.equals(this.data, that.data);
            }

            public int hashCode() {
                return Objects.hash(this.mimeType, this.data);
            }

            public String toString() {
                return "GeminiBlob[mimeType=" + this.mimeType + ", data=" + this.data + "]";
            }
        }

        static class Builder {
            private String text;
            private GeminiBlob inlineData;
            private GeminiFunctionCall functionCall;
            private GeminiFunctionResponse functionResponse;
            private GeminiFileData fileData;
            private GeminiExecutableCode executableCode;
            private GeminiCodeExecutionResult codeExecutionResult;
            private Boolean thought;
            private String thoughtSignature;
            private GeminiMediaResolution mediaResolution;

            private Builder() {
            }

            Builder text(String text) {
                this.text = text;
                return this;
            }

            Builder inlineData(GeminiBlob inlineData) {
                this.inlineData = inlineData;
                return this;
            }

            Builder functionCall(GeminiFunctionCall functionCall) {
                this.functionCall = functionCall;
                return this;
            }

            Builder functionResponse(GeminiFunctionResponse functionResponse) {
                this.functionResponse = functionResponse;
                return this;
            }

            Builder fileData(GeminiFileData fileData) {
                this.fileData = fileData;
                return this;
            }

            Builder executableCode(GeminiExecutableCode executableCode) {
                this.executableCode = executableCode;
                return this;
            }

            Builder codeExecutionResult(GeminiCodeExecutionResult codeExecutionResult) {
                this.codeExecutionResult = codeExecutionResult;
                return this;
            }

            Builder thought(Boolean thought) {
                this.thought = thought;
                return this;
            }

            Builder thoughtSignature(String thoughtSignature) {
                this.thoughtSignature = thoughtSignature;
                return this;
            }

            Builder mediaResolution(GeminiMediaResolution mediaResolution) {
                this.mediaResolution = mediaResolution;
                return this;
            }

            GeminiPart build() {
                return new GeminiPart(this.text, this.inlineData, this.functionCall, this.functionResponse, this.fileData, this.executableCode, this.codeExecutionResult, this.thought, this.thoughtSignature, this.mediaResolution);
            }
        }
    }
}

