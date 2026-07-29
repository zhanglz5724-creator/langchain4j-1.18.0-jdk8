/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 *  opennlp.tools.sentdetect.SentenceDetectorME
 *  opennlp.tools.sentdetect.SentenceModel
 */
package dev.langchain4j.data.document.splitter;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByWordSplitter;
import dev.langchain4j.data.document.splitter.HierarchicalDocumentSplitter;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;
import java.io.InputStream;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class DocumentBySentenceSplitter
extends HierarchicalDocumentSplitter {
    private final SentenceModel sentenceModel;

    public DocumentBySentenceSplitter(int maxSegmentSizeInChars, int maxOverlapSizeInChars) {
        super(maxSegmentSizeInChars, maxOverlapSizeInChars, null, null);
        this.sentenceModel = this.createSentenceModel();
    }

    public DocumentBySentenceSplitter(int maxSegmentSizeInChars, int maxOverlapSizeInChars, DocumentSplitter subSplitter) {
        super(maxSegmentSizeInChars, maxOverlapSizeInChars, null, subSplitter);
        this.sentenceModel = this.createSentenceModel();
    }

    public DocumentBySentenceSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, null);
        this.sentenceModel = this.createSentenceModel();
    }

    public DocumentBySentenceSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator, DocumentSplitter subSplitter) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, subSplitter);
        this.sentenceModel = this.createSentenceModel();
    }

    public DocumentBySentenceSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator, DocumentSplitter subSplitter, SentenceModel sentenceModel) {
        super(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, subSplitter);
        this.sentenceModel = (SentenceModel)ValidationUtils.ensureNotNull((Object)sentenceModel, (String)"sentenceModel");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private SentenceModel createSentenceModel() {
        String sentenceModelFilePath = "/opennlp/en-sent.bin";
        try (InputStream is = this.getClass().getResourceAsStream(sentenceModelFilePath);){
            SentenceModel sentenceModel = new SentenceModel(is);
            return sentenceModel;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] split(String text) {
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(this.sentenceModel);
        return sentenceDetector.sentDetect(text);
    }

    @Override
    public String joinDelimiter() {
        return " ";
    }

    @Override
    protected DocumentSplitter defaultSubSplitter() {
        return new DocumentByWordSplitter(this.maxSegmentSize, this.maxOverlapSize, this.tokenCountEstimator);
    }
}

