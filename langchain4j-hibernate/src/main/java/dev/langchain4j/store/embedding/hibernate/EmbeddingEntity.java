/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Id
 *  org.hibernate.annotations.Array
 */
package dev.langchain4j.store.embedding.hibernate;

import dev.langchain4j.store.embedding.hibernate.EmbeddedText;
import dev.langchain4j.store.embedding.hibernate.EmbeddingVector;
import dev.langchain4j.store.embedding.hibernate.UnmappedMetadata;
import jakarta.persistence.Id;
import java.util.UUID;
import org.hibernate.annotations.Array;

public class EmbeddingEntity {
    @Id
    private UUID id;
    @EmbeddedText
    private String text;
    @EmbeddingVector
    @Array(length=0)
    private float[] embedding;
    @UnmappedMetadata
    private String metadata;
}

