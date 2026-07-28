/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 */
package dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore.AzureCosmosDbMongoVCoreDocument;
import dev.langchain4j.store.embedding.azure.cosmos.mongo.vcore.AzureCosmosDbMongoVCoreMatchedDocument;
import java.util.Map;

class MappingUtils {
    private MappingUtils() throws InstantiationException {
        throw new InstantiationException("can't instantiate this class");
    }

    static AzureCosmosDbMongoVCoreDocument toMongoDbDocument(String id, Embedding embedding, TextSegment textSegment) {
        if (textSegment == null) {
            return new AzureCosmosDbMongoVCoreDocument(id, embedding.vectorAsList(), null, null);
        }
        return new AzureCosmosDbMongoVCoreDocument(id, embedding.vectorAsList(), textSegment.text(), Utils.toStringValueMap((Map)textSegment.metadata().toMap()));
    }

    static EmbeddingMatch<TextSegment> toEmbeddingMatch(AzureCosmosDbMongoVCoreMatchedDocument matchedDocument) {
        TextSegment textSegment = null;
        if (matchedDocument.getText() != null) {
            textSegment = TextSegment.from((String)matchedDocument.getText(), (Metadata)Metadata.from(matchedDocument.getMetadata()));
        }
        return new EmbeddingMatch(matchedDocument.getScore(), matchedDocument.getId(), Embedding.from(matchedDocument.getEmbedding()), (Object)textSegment);
    }
}

