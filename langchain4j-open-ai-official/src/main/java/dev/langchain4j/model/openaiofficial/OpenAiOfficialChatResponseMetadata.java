/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.openaiofficial;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialTokenUsage;
import java.util.Objects;

@Experimental
public class OpenAiOfficialChatResponseMetadata
extends ChatResponseMetadata {
    private final Long created;
    private final String serviceTier;
    private final String systemFingerprint;

    private OpenAiOfficialChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.created = builder.created;
        this.serviceTier = builder.serviceTier;
        this.systemFingerprint = builder.systemFingerprint;
    }

    public OpenAiOfficialTokenUsage tokenUsage() {
        return (OpenAiOfficialTokenUsage)super.tokenUsage();
    }

    public Long created() {
        return this.created;
    }

    public String serviceTier() {
        return this.serviceTier;
    }

    public String systemFingerprint() {
        return this.systemFingerprint;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)OpenAiOfficialChatResponseMetadata.builder())).created(this.created).serviceTier(this.serviceTier).systemFingerprint(this.systemFingerprint);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OpenAiOfficialChatResponseMetadata that = (OpenAiOfficialChatResponseMetadata)((Object)o);
        return Objects.equals(this.created, that.created) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.systemFingerprint, that.systemFingerprint);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.created, this.serviceTier, this.systemFingerprint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private Long created;
        private String serviceTier;
        private String systemFingerprint;

        public Builder created(Long created) {
            this.created = created;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder systemFingerprint(String systemFingerprint) {
            this.systemFingerprint = systemFingerprint;
            return this;
        }

        public OpenAiOfficialChatResponseMetadata build() {
            return new OpenAiOfficialChatResponseMetadata(this);
        }
    }
}

