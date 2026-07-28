/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.classification;

import dev.langchain4j.classification.ScoredLabel;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;

public class ClassificationResult<L> {
    private final List<ScoredLabel<L>> scoredLabels;

    public ClassificationResult(List<ScoredLabel<L>> scoredLabels) {
        this.scoredLabels = (List)ValidationUtils.ensureNotNull(scoredLabels, (String)"scoredLabels");
    }

    public List<ScoredLabel<L>> scoredLabels() {
        return this.scoredLabels;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ClassificationResult)) {
            return false;
        }
        ClassificationResult that = (ClassificationResult)obj;
        return Objects.equals(this.scoredLabels, that.scoredLabels);
    }

    public int hashCode() {
        return Objects.hash(this.scoredLabels);
    }

    public String toString() {
        return "ClassificationResult { scoredLabels = " + this.scoredLabels + " }";
    }
}

