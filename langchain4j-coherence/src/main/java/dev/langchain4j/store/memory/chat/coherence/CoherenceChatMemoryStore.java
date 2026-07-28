/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.tangosol.net.Coherence
 *  com.tangosol.net.NamedMap
 *  com.tangosol.net.NamedMap$Option
 *  com.tangosol.net.Session
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageDeserializer
 *  dev.langchain4j.data.message.ChatMessageSerializer
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.memory.chat.ChatMemoryStore
 */
package dev.langchain4j.store.memory.chat.coherence;

import com.tangosol.net.Coherence;
import com.tangosol.net.NamedMap;
import com.tangosol.net.Session;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.util.ArrayList;
import java.util.List;

public class CoherenceChatMemoryStore
implements ChatMemoryStore {
    public static final String DEFAULT_MAP_NAME = "chatMemory";
    protected final NamedMap<Object, String> chatMemory;

    protected CoherenceChatMemoryStore(NamedMap<Object, String> chatMemory) {
        this.chatMemory = chatMemory;
    }

    public List<ChatMessage> getMessages(Object memoryId) {
        this.validateId(memoryId);
        String json = (String)this.chatMemory.get(memoryId);
        return json == null ? new ArrayList() : ChatMessageDeserializer.messagesFromJson((String)json);
    }

    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        this.validateId(memoryId);
        String json = ChatMessageSerializer.messagesToJson((List)((List)ValidationUtils.ensureNotEmpty(messages, (String)"messages")));
        this.chatMemory.put(memoryId, (Object)json);
    }

    public void deleteMessages(Object memoryId) {
        this.validateId(memoryId);
        this.chatMemory.remove(memoryId);
    }

    private void validateId(Object memoryId) {
        ValidationUtils.ensureNotNull((Object)memoryId, (String)"memoryId");
    }

    public static CoherenceChatMemoryStore create() {
        return CoherenceChatMemoryStore.builder().build();
    }

    public static CoherenceChatMemoryStore create(String name) {
        return CoherenceChatMemoryStore.builder().name(name).build();
    }

    public static CoherenceChatMemoryStore create(NamedMap<Object, String> map) {
        return new CoherenceChatMemoryStore(map);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = "chatMemory";
        private String sessionName;
        private Session session;

        protected Builder() {
        }

        public Builder name(String name) {
            this.name = Utils.isNullOrBlank((String)name) ? CoherenceChatMemoryStore.DEFAULT_MAP_NAME : name;
            return this;
        }

        public Builder session(String sessionName) {
            this.sessionName = sessionName;
            this.session = null;
            return this;
        }

        public Builder session(Session session) {
            this.session = session;
            this.sessionName = null;
            return this;
        }

        public CoherenceChatMemoryStore build() {
            Session session = this.session;
            if (session == null) {
                session = this.sessionName != null ? Coherence.getInstance().getSession(this.sessionName) : Coherence.getInstance().getSession();
            }
            NamedMap map = session.getMap(this.name, new NamedMap.Option[0]);
            return new CoherenceChatMemoryStore((NamedMap<Object, String>)map);
        }
    }
}

