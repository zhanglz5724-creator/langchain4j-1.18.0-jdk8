/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.ollama.OllamaDateDeserializer;
import dev.langchain4j.model.ollama.OllamaModelDetails;
import dev.langchain4j.model.ollama.OllamaModelMessage;
import dev.langchain4j.model.ollama.OllamaModelTensor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaModelCard {
    private String license;
    private String modelfile;
    private String parameters;
    private String template;
    private String system;
    private OllamaModelDetails details;
    private List<OllamaModelMessage> messages;
    private Map<String, Object> modelInfo;
    private Map<String, Object> projectorInfo;
    private List<OllamaModelTensor> tensors;
    @JsonDeserialize(using=OllamaDateDeserializer.class)
    private OffsetDateTime modifiedAt;
    private List<String> capabilities;

    OllamaModelCard() {
    }

    public OllamaModelCard(String modelfile, String parameters, String template, OllamaModelDetails details) {
        this.modelfile = modelfile;
        this.parameters = parameters;
        this.template = template;
        this.details = details;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getLicense() {
        return this.license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getModelfile() {
        return this.modelfile;
    }

    public void setModelfile(String modelfile) {
        this.modelfile = modelfile;
    }

    public String getParameters() {
        return this.parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public OllamaModelDetails getDetails() {
        return this.details;
    }

    public void setDetails(OllamaModelDetails details) {
        this.details = details;
    }

    public List<OllamaModelMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(List<OllamaModelMessage> messages) {
        this.messages = messages;
    }

    public Map<String, Object> getModelInfo() {
        return this.modelInfo;
    }

    public void setModelInfo(Map<String, Object> modelInfo) {
        this.modelInfo = modelInfo;
    }

    public Map<String, Object> getProjectorInfo() {
        return this.projectorInfo;
    }

    public void setProjectorInfo(Map<String, Object> projectorInfo) {
        this.projectorInfo = projectorInfo;
    }

    public List<OllamaModelTensor> getTensors() {
        return this.tensors;
    }

    public void setTensors(List<OllamaModelTensor> tensors) {
        this.tensors = tensors;
    }

    public OffsetDateTime getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(OffsetDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public List<String> getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public static class Builder {
        private String license;
        private String modelfile;
        private String parameters;
        private String template;
        private String system;
        private OllamaModelDetails details;
        private List<OllamaModelMessage> messages;
        private Map<String, Object> modelInfo;
        private Map<String, Object> projectorInfo;
        private List<OllamaModelTensor> tensors;
        private OffsetDateTime modifiedAt;
        private List<String> capabilities;

        public Builder license(String license) {
            this.license = license;
            return this;
        }

        public Builder modelfile(String modelfile) {
            this.modelfile = modelfile;
            return this;
        }

        public Builder parameters(String parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder template(String template) {
            this.template = template;
            return this;
        }

        public Builder system(String system) {
            this.system = system;
            return this;
        }

        public Builder details(OllamaModelDetails details) {
            this.details = details;
            return this;
        }

        public Builder messages(List<OllamaModelMessage> messages) {
            this.messages = messages;
            return this;
        }

        public Builder modelInfo(Map<String, Object> modelInfo) {
            this.modelInfo = modelInfo;
            return this;
        }

        public Builder projectorInfo(Map<String, Object> projectorInfo) {
            this.projectorInfo = projectorInfo;
            return this;
        }

        public Builder tensors(List<OllamaModelTensor> tensors) {
            this.tensors = tensors;
            return this;
        }

        public Builder capabilities(List<String> capabilities) {
            this.capabilities = capabilities;
            return this;
        }

        public Builder modifiedAt(OffsetDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public OllamaModelCard build() {
            OllamaModelCard modelCard = new OllamaModelCard(this.modelfile, this.parameters, this.template, this.details);
            modelCard.setLicense(this.license);
            modelCard.setSystem(this.system);
            modelCard.setMessages(this.messages);
            modelCard.setModelInfo(this.modelInfo);
            modelCard.setProjectorInfo(this.projectorInfo);
            modelCard.setTensors(this.tensors);
            modelCard.setCapabilities(this.capabilities);
            modelCard.setModifiedAt(this.modifiedAt);
            return modelCard;
        }
    }
}

