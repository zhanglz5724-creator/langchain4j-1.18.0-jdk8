/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.workersai.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.workersai.client.ApiResponse;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WorkersAiTextCompletionResponse
extends ApiResponse<TextResponse> {

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Usage {
        @JsonProperty(value="prompt_tokens")
        private Integer promptTokens;
        @JsonProperty(value="completion_tokens")
        private Integer completionTokens;
        @JsonProperty(value="total_tokens")
        private Integer totalTokens;

        public Integer getPromptTokens() {
            return this.promptTokens;
        }

        public void setPromptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
        }

        public Integer getCompletionTokens() {
            return this.completionTokens;
        }

        public void setCompletionTokens(Integer completionTokens) {
            this.completionTokens = completionTokens;
        }

        public Integer getTotalTokens() {
            return this.totalTokens;
        }

        public void setTotalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Usage)) {
                return false;
            }
            Usage other = (Usage)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Integer this$promptTokens = this.getPromptTokens();
            Integer other$promptTokens = other.getPromptTokens();
            if (this$promptTokens == null ? other$promptTokens != null : !((Object)this$promptTokens).equals(other$promptTokens)) {
                return false;
            }
            Integer this$completionTokens = this.getCompletionTokens();
            Integer other$completionTokens = other.getCompletionTokens();
            if (this$completionTokens == null ? other$completionTokens != null : !((Object)this$completionTokens).equals(other$completionTokens)) {
                return false;
            }
            Integer this$totalTokens = this.getTotalTokens();
            Integer other$totalTokens = other.getTotalTokens();
            return !(this$totalTokens == null ? other$totalTokens != null : !((Object)this$totalTokens).equals(other$totalTokens));
        }

        protected boolean canEqual(Object other) {
            return other instanceof Usage;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Integer $promptTokens = this.getPromptTokens();
            result = result * 59 + ($promptTokens == null ? 43 : ((Object)$promptTokens).hashCode());
            Integer $completionTokens = this.getCompletionTokens();
            result = result * 59 + ($completionTokens == null ? 43 : ((Object)$completionTokens).hashCode());
            Integer $totalTokens = this.getTotalTokens();
            result = result * 59 + ($totalTokens == null ? 43 : ((Object)$totalTokens).hashCode());
            return result;
        }

        public String toString() {
            return "WorkersAiTextCompletionResponse.Usage(promptTokens=" + this.getPromptTokens() + ", completionTokens=" + this.getCompletionTokens() + ", totalTokens=" + this.getTotalTokens() + ")";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class TextResponse {
        @JsonProperty(value="response")
        private String response;
        @JsonProperty(value="usage")
        private Usage usage;
        @JsonProperty(value="finish_reason")
        private String finishReason;

        public String getResponse() {
            return this.response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public Usage getUsage() {
            return this.usage;
        }

        public void setUsage(Usage usage) {
            this.usage = usage;
        }

        public String getFinishReason() {
            return this.finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TextResponse)) {
                return false;
            }
            TextResponse other = (TextResponse)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$response = this.getResponse();
            String other$response = other.getResponse();
            if (this$response == null ? other$response != null : !this$response.equals(other$response)) {
                return false;
            }
            Usage this$usage = this.getUsage();
            Usage other$usage = other.getUsage();
            if (this$usage == null ? other$usage != null : !((Object)this$usage).equals(other$usage)) {
                return false;
            }
            String this$finishReason = this.getFinishReason();
            String other$finishReason = other.getFinishReason();
            return !(this$finishReason == null ? other$finishReason != null : !this$finishReason.equals(other$finishReason));
        }

        protected boolean canEqual(Object other) {
            return other instanceof TextResponse;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $response = this.getResponse();
            result = result * 59 + ($response == null ? 43 : $response.hashCode());
            Usage $usage = this.getUsage();
            result = result * 59 + ($usage == null ? 43 : ((Object)$usage).hashCode());
            String $finishReason = this.getFinishReason();
            result = result * 59 + ($finishReason == null ? 43 : $finishReason.hashCode());
            return result;
        }

        public String toString() {
            return "WorkersAiTextCompletionResponse.TextResponse(response=" + this.getResponse() + ", usage=" + this.getUsage() + ", finishReason=" + this.getFinishReason() + ")";
        }
    }
}

