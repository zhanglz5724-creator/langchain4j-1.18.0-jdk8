/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.openaiofficial;

import dev.langchain4j.model.output.TokenUsage;
import java.util.Objects;

public class OpenAiOfficialTokenUsage
extends TokenUsage {
    private final InputTokensDetails inputTokensDetails;
    private final OutputTokensDetails outputTokensDetails;

    private OpenAiOfficialTokenUsage(Builder builder) {
        super(builder.inputTokenCount, builder.outputTokenCount, builder.totalTokenCount);
        this.inputTokensDetails = builder.inputTokensDetails;
        this.outputTokensDetails = builder.outputTokensDetails;
    }

    public InputTokensDetails inputTokensDetails() {
        return this.inputTokensDetails;
    }

    public OutputTokensDetails outputTokensDetails() {
        return this.outputTokensDetails;
    }

    public OpenAiOfficialTokenUsage add(TokenUsage that) {
        if (that == null) {
            return this;
        }
        return OpenAiOfficialTokenUsage.builder().inputTokenCount(OpenAiOfficialTokenUsage.sum((Integer)this.inputTokenCount(), (Integer)that.inputTokenCount())).inputTokensDetails(this.addInputTokenDetails(that)).outputTokenCount(OpenAiOfficialTokenUsage.sum((Integer)this.outputTokenCount(), (Integer)that.outputTokenCount())).outputTokensDetails(this.addOutputTokensDetails(that)).totalTokenCount(OpenAiOfficialTokenUsage.sum((Integer)this.totalTokenCount(), (Integer)that.totalTokenCount())).build();
    }

    private InputTokensDetails addInputTokenDetails(TokenUsage that) {
        if (that instanceof OpenAiOfficialTokenUsage) {
            OpenAiOfficialTokenUsage thatOpenAiTokenUsage = (OpenAiOfficialTokenUsage)that;
            if (this.inputTokensDetails == null) {
                return thatOpenAiTokenUsage.inputTokensDetails;
            }
            if (thatOpenAiTokenUsage.inputTokensDetails == null) {
                return this.inputTokensDetails;
            }
            return InputTokensDetails.builder().cachedTokens(OpenAiOfficialTokenUsage.sum((Integer)this.inputTokensDetails.cachedTokens, (Integer)thatOpenAiTokenUsage.inputTokensDetails.cachedTokens)).build();
        }
        return this.inputTokensDetails;
    }

    private OutputTokensDetails addOutputTokensDetails(TokenUsage that) {
        if (that instanceof OpenAiOfficialTokenUsage) {
            OpenAiOfficialTokenUsage thatOpenAiTokenUsage = (OpenAiOfficialTokenUsage)that;
            if (this.outputTokensDetails == null) {
                return thatOpenAiTokenUsage.outputTokensDetails;
            }
            if (thatOpenAiTokenUsage.outputTokensDetails == null) {
                return this.outputTokensDetails;
            }
            return OutputTokensDetails.builder().reasoningTokens(OpenAiOfficialTokenUsage.sum((Integer)this.outputTokensDetails.reasoningTokens, (Integer)thatOpenAiTokenUsage.outputTokensDetails.reasoningTokens)).build();
        }
        return this.outputTokensDetails;
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
        OpenAiOfficialTokenUsage that = (OpenAiOfficialTokenUsage)((Object)o);
        return Objects.equals(this.inputTokensDetails, that.inputTokensDetails) && Objects.equals(this.outputTokensDetails, that.outputTokensDetails);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.inputTokensDetails, this.outputTokensDetails);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class OutputTokensDetails {
        private final Integer reasoningTokens;

        public OutputTokensDetails(Builder builder) {
            this.reasoningTokens = builder.reasoningTokens;
        }

        public Integer reasoningTokens() {
            return this.reasoningTokens;
        }

        public static Builder builder() {
            return new Builder();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            OutputTokensDetails that = (OutputTokensDetails)obj;
            return Objects.equals(this.reasoningTokens, that.reasoningTokens);
        }

        public int hashCode() {
            return Objects.hash(this.reasoningTokens);
        }

        public String toString() {
            return "OpenAiOfficialTokenUsage.OutputTokensDetails { reasoningTokens = " + this.reasoningTokens + " }";
        }

        public static class Builder {
            private Integer reasoningTokens;

            public Builder reasoningTokens(Integer reasoningTokens) {
                this.reasoningTokens = reasoningTokens;
                return this;
            }

            public Builder reasoningTokens(Long reasoningTokens) {
                if (reasoningTokens != null) {
                    this.reasoningTokens = reasoningTokens.intValue();
                }
                return this;
            }

            public OutputTokensDetails build() {
                return new OutputTokensDetails(this);
            }
        }
    }

    public static class InputTokensDetails {
        private final Integer cachedTokens;

        public InputTokensDetails(Builder builder) {
            this.cachedTokens = builder.cachedTokens;
        }

        public Integer cachedTokens() {
            return this.cachedTokens;
        }

        public static Builder builder() {
            return new Builder();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            InputTokensDetails that = (InputTokensDetails)obj;
            return Objects.equals(this.cachedTokens, that.cachedTokens);
        }

        public int hashCode() {
            return Objects.hash(this.cachedTokens);
        }

        public String toString() {
            return "OpenAiOfficialTokenUsage.InputTokensDetails { cachedTokens = " + this.cachedTokens + " }";
        }

        public static class Builder {
            private Integer cachedTokens;

            public Builder cachedTokens(Integer cachedTokens) {
                this.cachedTokens = cachedTokens;
                return this;
            }

            public Builder cachedTokens(Long cachedTokens) {
                if (cachedTokens != null) {
                    this.cachedTokens = cachedTokens.intValue();
                }
                return this;
            }

            public InputTokensDetails build() {
                return new InputTokensDetails(this);
            }
        }
    }

    public static class Builder {
        private Integer inputTokenCount;
        private InputTokensDetails inputTokensDetails;
        private Integer outputTokenCount;
        private OutputTokensDetails outputTokensDetails;
        private Integer totalTokenCount;

        public Builder inputTokenCount(Integer inputTokenCount) {
            this.inputTokenCount = inputTokenCount;
            return this;
        }

        public Builder inputTokenCount(Long inputTokenCount) {
            if (inputTokenCount != null) {
                this.inputTokenCount = inputTokenCount.intValue();
            }
            return this;
        }

        public Builder inputTokensDetails(InputTokensDetails inputTokensDetails) {
            this.inputTokensDetails = inputTokensDetails;
            return this;
        }

        public Builder outputTokenCount(Integer outputTokenCount) {
            this.outputTokenCount = outputTokenCount;
            return this;
        }

        public Builder outputTokenCount(Long outputTokenCount) {
            if (outputTokenCount != null) {
                this.outputTokenCount = outputTokenCount.intValue();
            }
            return this;
        }

        public Builder outputTokensDetails(OutputTokensDetails outputTokensDetails) {
            this.outputTokensDetails = outputTokensDetails;
            return this;
        }

        public Builder totalTokenCount(Integer totalTokenCount) {
            this.totalTokenCount = totalTokenCount;
            return this;
        }

        public Builder totalTokenCount(Long totalTokenCount) {
            if (totalTokenCount != null) {
                this.totalTokenCount = totalTokenCount.intValue();
            }
            return this;
        }

        public OpenAiOfficialTokenUsage build() {
            return new OpenAiOfficialTokenUsage(this);
        }
    }
}

