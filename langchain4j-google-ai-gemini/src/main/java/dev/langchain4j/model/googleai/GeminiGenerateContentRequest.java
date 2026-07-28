/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiFunctionCallingConfig;
import dev.langchain4j.model.googleai.GeminiFunctionDeclaration;
import dev.langchain4j.model.googleai.GeminiGenerationConfig;
import dev.langchain4j.model.googleai.GeminiSafetySetting;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiGenerateContentRequest {
    private final String model;
    private final List<GeminiContent> contents;
    private final List<GeminiTool> tools;
    private final GeminiToolConfig toolConfig;
    private final List<GeminiSafetySetting> safetySettings;
    private final GeminiContent systemInstruction;
    private final GeminiGenerationConfig generationConfig;
    private final String cachedContent;

    @JsonCreator
    GeminiGenerateContentRequest(@JsonProperty(value="model") String model, @JsonProperty(value="contents") List<GeminiContent> contents, @JsonProperty(value="tools") List<GeminiTool> tools, @JsonProperty(value="toolConfig") GeminiToolConfig toolConfig, @JsonProperty(value="safetySettings") List<GeminiSafetySetting> safetySettings, @JsonProperty(value="systemInstruction") GeminiContent systemInstruction, @JsonProperty(value="generationConfig") GeminiGenerationConfig generationConfig, @JsonProperty(value="cachedContent") String cachedContent) {
        this.model = model;
        this.contents = contents;
        this.tools = tools;
        this.toolConfig = toolConfig;
        this.safetySettings = safetySettings;
        this.systemInstruction = systemInstruction;
        this.generationConfig = generationConfig;
        this.cachedContent = cachedContent;
    }

    String model() {
        return this.model;
    }

    List<GeminiContent> contents() {
        return this.contents;
    }

    List<GeminiTool> tools() {
        return this.tools;
    }

    GeminiToolConfig toolConfig() {
        return this.toolConfig;
    }

    List<GeminiSafetySetting> safetySettings() {
        return this.safetySettings;
    }

    GeminiContent systemInstruction() {
        return this.systemInstruction;
    }

    GeminiGenerationConfig generationConfig() {
        return this.generationConfig;
    }

    String cachedContent() {
        return this.cachedContent;
    }

    static GeminiGenerateContentRequestBuilder builder() {
        return new GeminiGenerateContentRequestBuilder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiGenerateContentRequest)) {
            return false;
        }
        GeminiGenerateContentRequest that = (GeminiGenerateContentRequest)o;
        return Objects.equals(this.model, that.model) && Objects.equals(this.contents, that.contents) && Objects.equals(this.tools, that.tools) && Objects.equals(this.toolConfig, that.toolConfig) && Objects.equals(this.safetySettings, that.safetySettings) && Objects.equals(this.systemInstruction, that.systemInstruction) && Objects.equals(this.generationConfig, that.generationConfig) && Objects.equals(this.cachedContent, that.cachedContent);
    }

    public int hashCode() {
        return Objects.hash(this.model, this.contents, this.tools, this.toolConfig, this.safetySettings, this.systemInstruction, this.generationConfig, this.cachedContent);
    }

    public String toString() {
        return "GeminiGenerateContentRequest[model=" + this.model + ", contents=" + this.contents + ", tools=" + this.tools + ", toolConfig=" + this.toolConfig + ", safetySettings=" + this.safetySettings + ", systemInstruction=" + this.systemInstruction + ", generationConfig=" + this.generationConfig + ", cachedContent=" + this.cachedContent + "]";
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiToolConfig {
        private final GeminiFunctionCallingConfig functionCallingConfig;

        @JsonCreator
        GeminiToolConfig(@JsonProperty(value="functionCallingConfig") GeminiFunctionCallingConfig functionCallingConfig) {
            this.functionCallingConfig = functionCallingConfig;
        }

        GeminiFunctionCallingConfig functionCallingConfig() {
            return this.functionCallingConfig;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiToolConfig)) {
                return false;
            }
            GeminiToolConfig that = (GeminiToolConfig)o;
            return Objects.equals(this.functionCallingConfig, that.functionCallingConfig);
        }

        public int hashCode() {
            return Objects.hash(this.functionCallingConfig);
        }

        public String toString() {
            return "GeminiToolConfig[functionCallingConfig=" + this.functionCallingConfig + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class GeminiTool {
        private final List<GeminiFunctionDeclaration> functionDeclarations;
        private final GeminiCodeExecution codeExecution;
        private final GeminiGoogleSearchRetrieval googleSearch;
        private final GeminiUrlContext urlContext;
        private final GeminiGoogleMaps googleMaps;

        @JsonCreator
        GeminiTool(@JsonProperty(value="functionDeclarations") List<GeminiFunctionDeclaration> functionDeclarations, @JsonProperty(value="codeExecution") GeminiCodeExecution codeExecution, @JsonProperty(value="google_search") GeminiGoogleSearchRetrieval googleSearch, @JsonProperty(value="urlContext") GeminiUrlContext urlContext, @JsonProperty(value="googleMaps") GeminiGoogleMaps googleMaps) {
            this.functionDeclarations = functionDeclarations;
            this.codeExecution = codeExecution;
            this.googleSearch = googleSearch;
            this.urlContext = urlContext;
            this.googleMaps = googleMaps;
        }

        List<GeminiFunctionDeclaration> functionDeclarations() {
            return this.functionDeclarations;
        }

        GeminiCodeExecution codeExecution() {
            return this.codeExecution;
        }

        GeminiGoogleSearchRetrieval googleSearch() {
            return this.googleSearch;
        }

        GeminiUrlContext urlContext() {
            return this.urlContext;
        }

        GeminiGoogleMaps googleMaps() {
            return this.googleMaps;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GeminiTool)) {
                return false;
            }
            GeminiTool that = (GeminiTool)o;
            return Objects.equals(this.functionDeclarations, that.functionDeclarations) && Objects.equals(this.codeExecution, that.codeExecution) && Objects.equals(this.googleSearch, that.googleSearch) && Objects.equals(this.urlContext, that.urlContext) && Objects.equals(this.googleMaps, that.googleMaps);
        }

        public int hashCode() {
            return Objects.hash(this.functionDeclarations, this.codeExecution, this.googleSearch, this.urlContext, this.googleMaps);
        }

        public String toString() {
            return "GeminiTool[functionDeclarations=" + this.functionDeclarations + ", codeExecution=" + this.codeExecution + ", googleSearch=" + this.googleSearch + ", urlContext=" + this.urlContext + ", googleMaps=" + this.googleMaps + "]";
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiGoogleMaps {
            private final Boolean enableWidget;

            @JsonCreator
            GeminiGoogleMaps(@JsonProperty(value="enableWidget") Boolean enableWidget) {
                this.enableWidget = enableWidget;
            }

            Boolean enableWidget() {
                return this.enableWidget;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof GeminiGoogleMaps)) {
                    return false;
                }
                GeminiGoogleMaps that = (GeminiGoogleMaps)o;
                return Objects.equals(this.enableWidget, that.enableWidget);
            }

            public int hashCode() {
                return Objects.hash(this.enableWidget);
            }

            public String toString() {
                return "GeminiGoogleMaps[enableWidget=" + this.enableWidget + "]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiGoogleSearchRetrieval {
            @JsonCreator
            GeminiGoogleSearchRetrieval() {
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                return o instanceof GeminiGoogleSearchRetrieval;
            }

            public int hashCode() {
                return 0;
            }

            public String toString() {
                return "GeminiGoogleSearchRetrieval[]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiUrlContext {
            @JsonCreator
            GeminiUrlContext() {
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                return o instanceof GeminiUrlContext;
            }

            public int hashCode() {
                return 0;
            }

            public String toString() {
                return "GeminiUrlContext[]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class GeminiCodeExecution {
            @JsonCreator
            GeminiCodeExecution() {
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                return o instanceof GeminiCodeExecution;
            }

            public int hashCode() {
                return 0;
            }

            public String toString() {
                return "GeminiCodeExecution[]";
            }
        }
    }

    static class GeminiGenerateContentRequestBuilder {
        private String model;
        private List<GeminiContent> contents;
        private List<GeminiTool> tools;
        private GeminiToolConfig toolConfig;
        private List<GeminiSafetySetting> safetySettings;
        private GeminiContent systemInstruction;
        private GeminiGenerationConfig generationConfig;
        private String cachedContent;

        GeminiGenerateContentRequestBuilder() {
        }

        GeminiGenerateContentRequestBuilder model(String model) {
            this.model = model;
            return this;
        }

        GeminiGenerateContentRequestBuilder contents(List<GeminiContent> contents) {
            this.contents = contents;
            return this;
        }

        GeminiGenerateContentRequestBuilder tools(List<GeminiTool> tools) {
            this.tools = tools;
            return this;
        }

        GeminiGenerateContentRequestBuilder toolConfig(GeminiToolConfig toolConfig) {
            this.toolConfig = toolConfig;
            return this;
        }

        GeminiGenerateContentRequestBuilder safetySettings(List<GeminiSafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        GeminiGenerateContentRequestBuilder systemInstruction(GeminiContent systemInstruction) {
            this.systemInstruction = systemInstruction;
            return this;
        }

        GeminiGenerateContentRequestBuilder generationConfig(GeminiGenerationConfig generationConfig) {
            this.generationConfig = generationConfig;
            return this;
        }

        GeminiGenerateContentRequestBuilder cachedContent(String cachedContent) {
            this.cachedContent = cachedContent;
            return this;
        }

        public GeminiGenerateContentRequest build() {
            return new GeminiGenerateContentRequest(this.model, this.contents, this.tools, this.toolConfig, this.safetySettings, this.systemInstruction, this.generationConfig, this.cachedContent);
        }
    }
}

