/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.bedrock;

import java.util.Objects;

public class GuardrailAssessment {
    private final Action action;
    private final Policy policy;
    private final String name;

    public GuardrailAssessment(Builder<?> builder) {
        this.action = ((Builder)builder).action;
        this.name = ((Builder)builder).name;
        this.policy = ((Builder)builder).policy;
    }

    public Action action() {
        return this.action;
    }

    public Policy policy() {
        return this.policy;
    }

    public String name() {
        return this.name;
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        GuardrailAssessment that = (GuardrailAssessment)o;
        return this.action == that.action && this.policy == that.policy && Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.action, this.policy, this.name});
    }

    public String toString() {
        return "GuardrailAssessment{action=" + (Object)((Object)this.action) + ", policy=" + (Object)((Object)this.policy) + ", name='" + this.name + '\'' + '}';
    }

    public static class Builder<T extends Builder<T>> {
        private Policy policy;
        private String name;
        private Action action = Action.UNKNOWN;

        public T policy(Policy policy) {
            this.policy = policy;
            return (T)this;
        }

        public T name(String name) {
            this.name = name;
            return (T)this;
        }

        public T action(Action action) {
            this.action = action;
            return (T)this;
        }

        public T action(String action) {
            if (action != null) {
                try {
                    this.action = Enum.valueOf(Action.class, action);
                }
                catch (IllegalArgumentException ignored) {
                    this.action = Action.UNKNOWN;
                }
            }
            return (T)this;
        }

        public GuardrailAssessment build() {
            return new GuardrailAssessment(this);
        }
    }

    public static enum Action {
        ANONYMIZED,
        BLOCKED,
        NONE,
        UNKNOWN;

    }

    public static enum Policy {
        TOPIC,
        CONTENT,
        WORD,
        SENSITIVE,
        CONTEXT;

    }
}

