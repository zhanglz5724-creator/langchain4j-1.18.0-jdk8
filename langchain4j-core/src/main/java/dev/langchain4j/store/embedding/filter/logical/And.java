/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter.logical;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Objects;

public class And
implements Filter {
    private final Filter left;
    private final Filter right;

    public And(Filter left, Filter right) {
        this.left = ValidationUtils.ensureNotNull(left, "left");
        this.right = ValidationUtils.ensureNotNull(right, "right");
    }

    public Filter left() {
        return this.left;
    }

    public Filter right() {
        return this.right;
    }

    @Override
    public boolean test(Object object) {
        return this.left().test(object) && this.right().test(object);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof And)) {
            return false;
        }
        And other = (And)o;
        return Objects.equals(this.left, other.left) && Objects.equals(this.right, other.right);
    }

    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }

    public String toString() {
        return "And(left=" + this.left + ", right=" + this.right + ")";
    }
}

