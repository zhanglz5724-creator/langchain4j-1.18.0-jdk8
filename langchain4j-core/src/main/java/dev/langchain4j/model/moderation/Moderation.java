/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.moderation;

import dev.langchain4j.internal.Utils;
import java.io.Serializable;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public class Moderation
implements Serializable {
    private final boolean flagged;
    private final @Nullable String flaggedText;

    public Moderation() {
        this.flagged = false;
        this.flaggedText = null;
    }

    public Moderation(@Nullable String flaggedText) {
        this.flagged = true;
        this.flaggedText = flaggedText;
    }

    public boolean flagged() {
        return this.flagged;
    }

    public @Nullable String flaggedText() {
        return this.flaggedText;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Moderation that = (Moderation)o;
        return this.flagged == that.flagged && Objects.equals(this.flaggedText, that.flaggedText);
    }

    public int hashCode() {
        return Objects.hash(this.flagged, this.flaggedText);
    }

    public String toString() {
        return "Moderation { flagged = " + this.flagged + ", flaggedText = " + Utils.quoted(this.flaggedText) + " }";
    }

    public static Moderation flagged(String flaggedText) {
        return new Moderation(flaggedText);
    }

    public static Moderation notFlagged() {
        return new Moderation();
    }
}

