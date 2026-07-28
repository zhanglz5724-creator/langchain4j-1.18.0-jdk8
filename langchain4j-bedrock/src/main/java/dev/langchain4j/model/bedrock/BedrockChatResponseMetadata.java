/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.model.bedrock.GuardrailAssessmentSummary;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import java.util.Objects;

public class BedrockChatResponseMetadata
extends ChatResponseMetadata {
    private final GuardrailAssessmentSummary guardrailAssessmentSummary;

    protected BedrockChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.guardrailAssessmentSummary = builder.guardrailAssessmentSummary;
    }

    public GuardrailAssessmentSummary guardrailAssessmentSummary() {
        return this.guardrailAssessmentSummary;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)BedrockChatResponseMetadata.builder())).guardrailAssessmentSummary(this.guardrailAssessmentSummary);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BedrockChatResponseMetadata that = (BedrockChatResponseMetadata)((Object)o);
        return Objects.equals(this.guardrailAssessmentSummary, that.guardrailAssessmentSummary);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.guardrailAssessmentSummary);
    }

    public String toString() {
        return "BedrockChatResponseMetadata{guardrailAssessmentSummary=" + this.guardrailAssessmentSummary + "} " + super.toString();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private GuardrailAssessmentSummary guardrailAssessmentSummary;

        public Builder guardrailAssessmentSummary(GuardrailAssessmentSummary guardrailAssessmentSummary) {
            this.guardrailAssessmentSummary = guardrailAssessmentSummary;
            return this;
        }

        public BedrockChatResponseMetadata build() {
            return new BedrockChatResponseMetadata(this);
        }
    }
}

