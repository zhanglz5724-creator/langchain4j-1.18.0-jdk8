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
package dev.langchain4j.store.embedding.azure.cosmos.nosql;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDbNoSqlDocument;
import dev.langchain4j.store.embedding.azure.cosmos.nosql.AzureCosmosDbNoSqlMatchedDocument;
import java.util.Map;

class MappingUtils {
    private MappingUtils() throws InstantiationException {
        throw new InstantiationException("can't instantiate this class");
    }

    static AzureCosmosDbNoSqlDocument toNoSqlDbDocument(String id, Embedding embedding, TextSegment textSegment) {
        if (textSegment == null) {
            return new AzureCosmosDbNoSqlDocument(id, embedding.vectorAsList(), null, null);
        }
        if (embedding == null) {
            return new AzureCosmosDbNoSqlDocument(id, null, textSegment.text(), Utils.toStringValueMap((Map)textSegment.metadata().toMap()));
        }
        return new AzureCosmosDbNoSqlDocument(id, embedding.vectorAsList(), textSegment.text(), Utils.toStringValueMap((Map)textSegment.metadata().toMap()));
    }

    static EmbeddingMatch<TextSegment> toEmbeddingMatch(AzureCosmosDbNoSqlMatchedDocument matchedDocument) {
        TextSegment textSegment = null;
        if (matchedDocument.getText() != null) {
            textSegment = TextSegment.from((String)matchedDocument.getText(), (Metadata)Metadata.from(matchedDocument.getMetadata()));
        }
        if (matchedDocument.getScore() == null) {
            return new EmbeddingMatch(Double.valueOf(0.0), matchedDocument.getId(), null, (Object)textSegment);
        }
        return new EmbeddingMatch(matchedDocument.getScore(), matchedDocument.getId(), Embedding.from(matchedDocument.getEmbedding()), (Object)textSegment);
    }
}

