/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag;

import dev.langchain4j.rag.AugmentationRequest;
import dev.langchain4j.rag.AugmentationResult;

public interface RetrievalAugmentor {
    public AugmentationResult augment(AugmentationRequest var1);
}

