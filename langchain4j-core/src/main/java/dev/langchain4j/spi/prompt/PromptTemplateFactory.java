/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.prompt;

import dev.langchain4j.Internal;
import java.util.Map;

@Internal
public interface PromptTemplateFactory {
    public Template create(Input var1);

    @Internal
    public static interface Template {
        public String render(Map<String, Object> var1);
    }

    @Internal
    public static interface Input {
        public String getTemplate();

        default public String getName() {
            return "template";
        }
    }
}

