/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.data.document.splitter;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.DocumentByWordSplitter;
import dev.langchain4j.model.TokenCountEstimator;

public class DocumentSplitters {
    public static DocumentSplitter recursive(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator) {
        return new DocumentByParagraphSplitter(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, new DocumentByLineSplitter(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, new DocumentBySentenceSplitter(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, new DocumentByWordSplitter(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator))));
    }

    public static DocumentSplitter recursive(int maxSegmentSizeInChars, int maxOverlapSizeInChars) {
        return DocumentSplitters.recursive(maxSegmentSizeInChars, maxOverlapSizeInChars, null);
    }
}

