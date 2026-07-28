/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.bedrock;

public class BedrockGuardrailConfiguration {
    private final String guardrailIdentifier;
    private final String guardrailVersion;
    private final ProcessingMode streamProcessingMode;

    public BedrockGuardrailConfiguration(String guardrailIdentifier, String guardrailVersion, ProcessingMode streamProcessingMode) {
        this.guardrailIdentifier = guardrailIdentifier;
        this.guardrailVersion = guardrailVersion;
        this.streamProcessingMode = streamProcessingMode;
    }

    public String guardrailIdentifier() {
        return this.guardrailIdentifier;
    }

    public String guardrailVersion() {
        return this.guardrailVersion;
    }

    public ProcessingMode streamProcessingMode() {
        return this.streamProcessingMode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toString() {
        return "BedrockGuardrailConfiguration{guardrailIdentifier='" + this.guardrailIdentifier + '\'' + ", guardrailVersion='" + this.guardrailVersion + '\'' + ", streamProcessingMode=" + (Object)((Object)this.streamProcessingMode) + '}';
    }

    public static class Builder {
        private String guardrailIdentifier;
        private String guardrailVersion;
        private ProcessingMode streamProcessingMode;

        public Builder guardrailIdentifier(String guardrailIdentifier) {
            this.guardrailIdentifier = guardrailIdentifier;
            return this;
        }

        public Builder guardrailVersion(String guardrailVersion) {
            this.guardrailVersion = guardrailVersion;
            return this;
        }

        public Builder streamProcessingMode(ProcessingMode streamProcessingMode) {
            this.streamProcessingMode = streamProcessingMode;
            return this;
        }

        public BedrockGuardrailConfiguration build() {
            return new BedrockGuardrailConfiguration(this.guardrailIdentifier, this.guardrailVersion, this.streamProcessingMode);
        }
    }

    public static enum ProcessingMode {
        SYNC,
        ASYNC;

    }
}

