/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  org.bson.Document
 */
package dev.langchain4j.store.embedding.mongodb;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.mongodb.IndexMapping;
import dev.langchain4j.store.embedding.mongodb.MongoDbDocument;
import dev.langchain4j.store.embedding.mongodb.MongoDbMatchedDocument;
import java.util.ArrayList;
import java.util.Set;
import org.bson.Document;

class MappingUtils {
    private MappingUtils() throws InstantiationException {
        throw new InstantiationException("Can't instantiate this class");
    }

    static MongoDbDocument toMongoDbDocument(String id, Embedding embedding, TextSegment textSegment) {
        boolean hasTextSegment = textSegment != null;
        return MongoDbDocument.builder().id(id).embedding(embedding.vectorAsList()).text(hasTextSegment ? textSegment.text() : null).metadata(hasTextSegment ? textSegment.metadata().toMap() : null).build();
    }

    static EmbeddingMatch<TextSegment> toEmbeddingMatch(MongoDbMatchedDocument matchedDocument) {
        TextSegment textSegment = null;
        if (matchedDocument.getText() != null) {
            textSegment = matchedDocument.getMetadata() == null ? TextSegment.from((String)matchedDocument.getText()) : TextSegment.from((String)matchedDocument.getText(), (Metadata)Metadata.from(matchedDocument.getMetadata()));
        }
        return new EmbeddingMatch(matchedDocument.getScore(), matchedDocument.getId(), Embedding.from(matchedDocument.getEmbedding()), (Object)textSegment);
    }

    static Document fromIndexMapping(IndexMapping indexMapping) {
        ArrayList<Document> list = new ArrayList<Document>();
        list.add(new Document().append("type", (Object)"vector").append("path", (Object)"embedding").append("numDimensions", (Object)indexMapping.getDimension()).append("similarity", (Object)"cosine"));
        Set<String> metadataFields = indexMapping.getMetadataFieldNames();
        if (metadataFields != null && !metadataFields.isEmpty()) {
            metadataFields.forEach(field -> list.add(new Document().append("type", (Object)"filter").append("path", (Object)("metadata." + field))));
        }
        return new Document("fields", list);
    }
}

