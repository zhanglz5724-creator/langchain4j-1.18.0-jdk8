/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.infinispan.protostream.schema.Field$Builder
 *  org.infinispan.protostream.schema.Schema
 *  org.infinispan.protostream.schema.Schema$Builder
 *  org.infinispan.protostream.schema.Type
 *  org.infinispan.protostream.schema.Type$Scalar
 */
package dev.langchain4j.store.embedding.infinispan;

import dev.langchain4j.store.embedding.infinispan.InfinispanStoreConfiguration;
import org.infinispan.protostream.schema.Field;
import org.infinispan.protostream.schema.Schema;
import org.infinispan.protostream.schema.Type;

public final class LangchainSchemaCreator {
    public static Schema buildSchema(InfinispanStoreConfiguration storeConfiguration) {
        Field.Builder builder = new Schema.Builder(storeConfiguration.fileName()).packageName(storeConfiguration.packageItem()).addMessage(storeConfiguration.metadataItemName()).addComment("@Indexed").addField((Type)Type.Scalar.STRING, "name", 1).addComment("@Basic(projectable=true)").addField((Type)Type.Scalar.STRING, "value", 2).addComment("@Basic(projectable=true)").addField((Type)Type.Scalar.INT64, "value_int", 3).addComment("@Basic(projectable=true)").addField((Type)Type.Scalar.DOUBLE, "value_float", 4).addComment("@Basic(projectable=true)").addMessage(storeConfiguration.langchainItemName()).addComment("@Indexed").addField((Type)Type.Scalar.STRING, "id", 1).addComment("@Basic(projectable=true)").addField((Type)Type.Scalar.STRING, "text", 2).addComment("@Basic(projectable=true)").addRepeatedField((Type)Type.Scalar.FLOAT, "embedding", 3).addComment(String.format("@Vector(dimension=%d, similarity=%s)", storeConfiguration.dimension(), storeConfiguration.similarity()));
        builder.addRepeatedField(Type.create((String)storeConfiguration.metadataItemName()), "metadata", 4).addComment("@Embedded");
        return builder.build();
    }
}

