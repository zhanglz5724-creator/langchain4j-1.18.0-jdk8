/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat;

import dev.langchain4j.internal.Utils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ChatRequestOptions {
    public static final ChatRequestOptions EMPTY = new ChatRequestOptions(Collections.emptyMap());
    private final Map<Object, Object> listenerAttributes;

    private ChatRequestOptions(Map<Object, Object> listenerAttributes) {
        this.listenerAttributes = Utils.copy(listenerAttributes);
    }

    public Map<Object, Object> listenerAttributes() {
        return this.listenerAttributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRequestOptions)) {
            return false;
        }
        ChatRequestOptions that = (ChatRequestOptions)o;
        return Objects.equals(this.listenerAttributes, that.listenerAttributes);
    }

    public int hashCode() {
        return Objects.hash(this.listenerAttributes);
    }

    public String toString() {
        return "ChatRequestOptions{listenerAttributes=" + this.listenerAttributes + "}";
    }

    public static class Builder {
        private Map<Object, Object> listenerAttributes;

        public Builder listenerAttributes(Map<Object, Object> listenerAttributes) {
            this.listenerAttributes = listenerAttributes;
            return this;
        }

        public Builder addListenerAttribute(Object key, Object value) {
            if (this.listenerAttributes == null) {
                this.listenerAttributes = new LinkedHashMap<Object, Object>();
            }
            this.listenerAttributes.put(key, value);
            return this;
        }

        public ChatRequestOptions build() {
            return new ChatRequestOptions(this.listenerAttributes);
        }
    }
}

