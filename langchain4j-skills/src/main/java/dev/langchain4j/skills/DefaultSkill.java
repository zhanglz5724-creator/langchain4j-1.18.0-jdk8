/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.skills.AbstractSkill;

@Experimental
public class DefaultSkill
extends AbstractSkill {
    public DefaultSkill(Builder builder) {
        super(builder);
    }

    public Builder toBuilder() {
        return (Builder)DefaultSkill.builder().copyFrom(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends AbstractSkill.BaseBuilder<Builder> {
        public DefaultSkill build() {
            return new DefaultSkill(this);
        }
    }
}

