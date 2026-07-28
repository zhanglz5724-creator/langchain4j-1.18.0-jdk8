/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.infinispan.protostream.MessageMarshaller
 *  org.infinispan.protostream.MessageMarshaller$ProtoStreamReader
 *  org.infinispan.protostream.MessageMarshaller$ProtoStreamWriter
 */
package dev.langchain4j.store.embedding.infinispan;

import dev.langchain4j.store.embedding.infinispan.LangChainInfinispanItem;
import dev.langchain4j.store.embedding.infinispan.LangChainMetadata;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.infinispan.protostream.MessageMarshaller;

public class LangChainItemMarshaller
implements MessageMarshaller<LangChainInfinispanItem> {
    private final String typeName;

    public LangChainItemMarshaller(String typeName) {
        this.typeName = typeName;
    }

    public LangChainInfinispanItem readFrom(MessageMarshaller.ProtoStreamReader reader) throws IOException {
        String id = reader.readString("id");
        String text = reader.readString("text");
        float[] embedding = reader.readFloats("embedding");
        Set metadata = (Set)reader.readCollection("metadata", new HashSet(), LangChainMetadata.class);
        HashMap<String, Object> metadataMap = new HashMap<String, Object>();
        if (metadata != null) {
            for (LangChainMetadata meta : metadata) {
                metadataMap.put(meta.name(), meta.value());
            }
        }
        return new LangChainInfinispanItem(id, embedding, text, metadata, metadataMap);
    }

    public void writeTo(MessageMarshaller.ProtoStreamWriter writer, LangChainInfinispanItem item) throws IOException {
        writer.writeString("id", item.id());
        writer.writeString("text", item.text());
        writer.writeFloats("embedding", item.embedding());
        writer.writeCollection("metadata", item.metadata(), LangChainMetadata.class);
    }

    public Class<? extends LangChainInfinispanItem> getJavaClass() {
        return LangChainInfinispanItem.class;
    }

    public String getTypeName() {
        return this.typeName;
    }
}

