/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.data.document.splitter;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.HierarchicalDocumentSplitter;
import dev.langchain4j.model.TokenCountEstimator;

public class DocumentByCharacterSplitter
extends HierarchicalDocumentSplitter {
    public DocumentByCharacterSplitter(int maxSegmentSizeInChars, int maxOverlapSizeInChars) {
        super(maxSegmentSizeInChars, maxOverlapSizeInChars, null, null);
    }

    public DocumentByCharacterSplitter(int maxSegmentSizeInChars, int maxOverlapSizeInChars, DocumentSplitter subSplitter) {
        super(maxSegmentSizeInChars, maxOverlapSizeInChars, null, subSplitter);
    }

    public DocumentByCharacterSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, null);
    }

    public DocumentByCharacterSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator, DocumentSplitter subSplitter) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, subSplitter);
    }

    @Override
    public String[] split(String text) {
        return text.split("");
    }

    @Override
    public String joinDelimiter() {
        return "";
    }

    @Override
    protected DocumentSplitter defaultSubSplitter() {
        return null;
    }
}

