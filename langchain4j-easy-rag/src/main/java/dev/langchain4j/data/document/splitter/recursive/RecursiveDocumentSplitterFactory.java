/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.data.document.splitter.DocumentSplitters
 *  dev.langchain4j.model.TokenCountEstimator
 *  dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator
 *  dev.langchain4j.spi.data.document.splitter.DocumentSplitterFactory
 */
package dev.langchain4j.data.document.splitter.recursive;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.spi.data.document.splitter.DocumentSplitterFactory;

public class RecursiveDocumentSplitterFactory
implements DocumentSplitterFactory {
    public DocumentSplitter create() {
        HuggingFaceTokenCountEstimator tokenCountEstimator = new HuggingFaceTokenCountEstimator();
        return DocumentSplitters.recursive((int)300, (int)30, (TokenCountEstimator)tokenCountEstimator);
    }
}

