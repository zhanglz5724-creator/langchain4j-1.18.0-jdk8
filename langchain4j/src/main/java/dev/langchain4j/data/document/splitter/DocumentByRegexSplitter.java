/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.data.document.splitter;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.HierarchicalDocumentSplitter;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;

public class DocumentByRegexSplitter
extends HierarchicalDocumentSplitter {
    private final String regex;
    private final String joinDelimiter;

    public DocumentByRegexSplitter(String regex, String joinDelimiter, int maxSegmentSizeInChars, int maxOverlapSizeInChars) {
        super(maxSegmentSizeInChars, maxOverlapSizeInChars, null, null);
        this.regex = (String)ValidationUtils.ensureNotNull((Object)regex, (String)"regex");
        this.joinDelimiter = (String)ValidationUtils.ensureNotNull((Object)joinDelimiter, (String)"joinDelimiter");
    }

    public DocumentByRegexSplitter(String regex, String joinDelimiter, int maxSegmentSizeInChars, int maxOverlapSizeInChars, DocumentSplitter subSplitter) {
        super(maxSegmentSizeInChars, maxOverlapSizeInChars, null, subSplitter);
        this.regex = (String)ValidationUtils.ensureNotNull((Object)regex, (String)"regex");
        this.joinDelimiter = (String)ValidationUtils.ensureNotNull((Object)joinDelimiter, (String)"joinDelimiter");
    }

    public DocumentByRegexSplitter(String regex, String joinDelimiter, int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, null);
        this.regex = (String)ValidationUtils.ensureNotNull((Object)regex, (String)"regex");
        this.joinDelimiter = (String)ValidationUtils.ensureNotNull((Object)joinDelimiter, (String)"joinDelimiter");
    }

    public DocumentByRegexSplitter(String regex, String joinDelimiter, int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator, DocumentSplitter subSplitter) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, subSplitter);
        this.regex = (String)ValidationUtils.ensureNotNull((Object)regex, (String)"regex");
        this.joinDelimiter = (String)ValidationUtils.ensureNotNull((Object)joinDelimiter, (String)"joinDelimiter");
    }

    @Override
    public String[] split(String text) {
        return text.split(this.regex);
    }

    @Override
    public String joinDelimiter() {
        return this.joinDelimiter;
    }

    @Override
    protected DocumentSplitter defaultSubSplitter() {
        return null;
    }
}

