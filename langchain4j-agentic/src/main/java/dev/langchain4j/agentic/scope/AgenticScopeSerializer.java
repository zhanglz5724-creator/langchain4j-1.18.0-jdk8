/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.scope;

import dev.langchain4j.agentic.scope.AgenticScopeJsonCodec;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.agentic.scope.JacksonAgenticScopeJsonCodec;
import java.util.Iterator;
import java.util.ServiceLoader;

public class AgenticScopeSerializer {
    static final AgenticScopeJsonCodec CODEC = AgenticScopeSerializer.loadCodec();

    private AgenticScopeSerializer() {
    }

    private static AgenticScopeJsonCodec loadCodec() {
        Iterator<AgenticScopeJsonCodec> iterator = ServiceLoader.load(AgenticScopeJsonCodec.class).iterator();
        if (iterator.hasNext()) {
            AgenticScopeJsonCodec codec = iterator.next();
            return codec;
        }
        return new JacksonAgenticScopeJsonCodec();
    }

    public static String toJson(DefaultAgenticScope agenticScope) {
        return CODEC.toJson(agenticScope);
    }

    public static DefaultAgenticScope fromJson(String json) {
        return CODEC.fromJson(json);
    }
}

