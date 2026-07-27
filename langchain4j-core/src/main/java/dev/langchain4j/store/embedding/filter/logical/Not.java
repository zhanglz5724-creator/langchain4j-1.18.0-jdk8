/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.filter.logical;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Objects;

public class Not
implements Filter {
    private final Filter expression;

    public Not(Filter expression) {
        this.expression = ValidationUtils.ensureNotNull(expression, "expression");
    }

    public Filter expression() {
        return this.expression;
    }

    @Override
    public boolean test(Object object) {
        if (!(object instanceof Metadata)) {
            return false;
        }
        return !this.expression.test(object);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Not)) {
            return false;
        }
        Not other = (Not)o;
        return Objects.equals(this.expression, other.expression);
    }

    public int hashCode() {
        return Objects.hash(this.expression);
    }

    public String toString() {
        return "Not(expression=" + this.expression + ")";
    }
}

