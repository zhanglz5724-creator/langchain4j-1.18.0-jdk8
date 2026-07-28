/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.models.responses.Response
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.models.responses.Response;
import dev.langchain4j.Experimental;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialTokenUsage;
import java.util.Objects;

@Experimental
public class OpenAiOfficialResponsesChatResponseMetadata
extends ChatResponseMetadata {
    private final Long createdAt;
    private final Long completedAt;
    private final String serviceTier;
    private final Response rawResponse;

    private OpenAiOfficialResponsesChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.createdAt = builder.createdAt;
        this.completedAt = builder.completedAt;
        this.serviceTier = builder.serviceTier;
        this.rawResponse = builder.rawResponse;
    }

    public OpenAiOfficialTokenUsage tokenUsage() {
        return (OpenAiOfficialTokenUsage)super.tokenUsage();
    }

    public Long createdAt() {
        return this.createdAt;
    }

    public Long completedAt() {
        return this.completedAt;
    }

    public String serviceTier() {
        return this.serviceTier;
    }

    public Response rawResponse() {
        return this.rawResponse;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)OpenAiOfficialResponsesChatResponseMetadata.builder())).createdAt(this.createdAt).completedAt(this.completedAt).serviceTier(this.serviceTier).rawResponse(this.rawResponse);
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
        OpenAiOfficialResponsesChatResponseMetadata that = (OpenAiOfficialResponsesChatResponseMetadata)((Object)o);
        return Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.completedAt, that.completedAt) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.rawResponse, that.rawResponse);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.createdAt, this.completedAt, this.serviceTier, this.rawResponse);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private Long createdAt;
        private Long completedAt;
        private String serviceTier;
        private Response rawResponse;

        public Builder createdAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder completedAt(Long completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder rawResponse(Response rawResponse) {
            this.rawResponse = rawResponse;
            return this;
        }

        public OpenAiOfficialResponsesChatResponseMetadata build() {
            return new OpenAiOfficialResponsesChatResponseMetadata(this);
        }
    }
}

