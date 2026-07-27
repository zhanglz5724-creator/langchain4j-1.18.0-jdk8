/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.JacksonJsonCodec;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.json.JsonCodecFactory;
import java.lang.reflect.Type;
import java.util.Iterator;

@Internal
public class Json {
    private static final JsonCodec CODEC = Json.loadCodec();

    private Json() {
    }

    private static JsonCodec loadCodec() {
        Iterator<JsonCodecFactory> iterator = ServiceHelper.loadFactories(JsonCodecFactory.class).iterator();
        if (iterator.hasNext()) {
            JsonCodecFactory factory = iterator.next();
            return factory.create();
        }
        return new JacksonJsonCodec();
    }

    public static String toJson(Object o) {
        return CODEC.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return CODEC.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return CODEC.fromJson(json, type);
    }

    @Internal
    public static interface JsonCodec {
        public String toJson(Object var1);

        public <T> T fromJson(String var1, Class<T> var2);

        public <T> T fromJson(String var1, Type var2);
    }
}

