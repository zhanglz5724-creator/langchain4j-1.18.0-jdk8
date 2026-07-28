/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.model.bedrock.GuardrailAssessment;
import java.util.List;
import java.util.Objects;

public class GuardrailAssessmentSummary {
    private final List<GuardrailAssessment> inputAssessments;
    private final List<GuardrailAssessment> outputAssessments;

    public GuardrailAssessmentSummary(Builder builder) {
        this.inputAssessments = builder.inputAssessments;
        this.outputAssessments = builder.outputAssessments;
    }

    public List<GuardrailAssessment> inputAssessments() {
        return this.inputAssessments;
    }

    public List<GuardrailAssessment> outputAssessments() {
        return this.outputAssessments;
    }

    @Deprecated
    public List<GuardrailAssessment> ouputAssessments() {
        return this.outputAssessments();
    }

    public boolean hasAssessments() {
        return this.inputAssessments != null && !this.inputAssessments.isEmpty() || this.outputAssessments != null && !this.outputAssessments.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        GuardrailAssessmentSummary that = (GuardrailAssessmentSummary)o;
        return Objects.equals(this.inputAssessments, that.inputAssessments) && Objects.equals(this.outputAssessments, that.outputAssessments);
    }

    public int hashCode() {
        return Objects.hash(this.inputAssessments, this.outputAssessments);
    }

    public String toString() {
        return "GuardrailAssessmentSummary{inputAssessments=" + this.inputAssessments + ", outputAssessments=" + this.outputAssessments + '}';
    }

    public static class Builder {
        private List<GuardrailAssessment> inputAssessments;
        private List<GuardrailAssessment> outputAssessments;

        public Builder inputAssessments(List<GuardrailAssessment> inputAssessments) {
            this.inputAssessments = inputAssessments;
            return this;
        }

        public Builder outputAssessments(List<GuardrailAssessment> outputAssessments) {
            this.outputAssessments = outputAssessments;
            return this;
        }

        @Deprecated
        public Builder ouputAssessments(List<GuardrailAssessment> ouputAssessments) {
            return this.outputAssessments(ouputAssessments);
        }

        public GuardrailAssessmentSummary build() {
            return new GuardrailAssessmentSummary(this);
        }
    }
}

