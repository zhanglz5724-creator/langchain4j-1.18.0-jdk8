/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.classification;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class ScoredLabel<L> {
    private final L label;
    private final double score;

    public ScoredLabel(L label, double score) {
        this.label = ValidationUtils.ensureNotNull(label, (String)"label");
        this.score = score;
    }

    public L label() {
        return this.label;
    }

    public double score() {
        return this.score;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ScoredLabel)) {
            return false;
        }
        ScoredLabel that = (ScoredLabel)obj;
        return Objects.equals(this.label, that.label) && Double.doubleToLongBits(this.score) == Double.doubleToLongBits(that.score);
    }

    public int hashCode() {
        return Objects.hash(this.label, this.score);
    }

    public String toString() {
        return "ScoredLabel { label = " + Utils.quoted(this.label) + ", score = " + this.score + " }";
    }
}

