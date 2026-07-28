/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.store.embedding.CosineSimilarity
 *  dev.langchain4j.store.embedding.RelevanceScore
 */
package dev.langchain4j.classification;

import dev.langchain4j.classification.ClassificationResult;
import dev.langchain4j.classification.ScoredLabel;
import dev.langchain4j.classification.TextClassifier;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.RelevanceScore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmbeddingModelTextClassifier<L>
implements TextClassifier<L> {
    private final EmbeddingModel embeddingModel;
    private final Map<L, List<Embedding>> exampleEmbeddingsByLabel;
    private final int maxResults;
    private final double minScore;
    private final double meanToMaxScoreRatio;

    public EmbeddingModelTextClassifier(EmbeddingModel embeddingModel, Map<L, ? extends Collection<String>> examplesByLabel) {
        this(embeddingModel, examplesByLabel, 1, 0.0, 0.5);
    }

    public EmbeddingModelTextClassifier(EmbeddingModel embeddingModel, Map<L, ? extends Collection<String>> examplesByLabel, int maxResults, double minScore, double meanToMaxScoreRatio) {
        this.embeddingModel = (EmbeddingModel)ValidationUtils.ensureNotNull((Object)embeddingModel, (String)"embeddingModel");
        ValidationUtils.ensureNotEmpty(examplesByLabel, (String)"examplesByLabel");
        this.maxResults = ValidationUtils.ensureGreaterThanZero((Integer)maxResults, (String)"maxResults");
        this.minScore = ValidationUtils.ensureBetween((Double)minScore, (double)0.0, (double)1.0, (String)"minScore");
        this.meanToMaxScoreRatio = ValidationUtils.ensureBetween((Double)meanToMaxScoreRatio, (double)0.0, (double)1.0, (String)"meanToMaxScoreRatio");
        this.exampleEmbeddingsByLabel = new HashMap<L, List<Embedding>>();
        examplesByLabel.forEach((label, examples) -> this.exampleEmbeddingsByLabel.put(label, (List<Embedding>)embeddingModel.embedAll(examples.stream().map(TextSegment::from).collect(Collectors.toList())).content()));
    }

    @Override
    public ClassificationResult<L> classifyWithScores(String text) {
        ValidationUtils.ensureNotBlank((String)text, (String)"text");
        Embedding textEmbedding = (Embedding)this.embeddingModel.embed(text).content();
        ArrayList scoredLabels = new ArrayList();
        this.exampleEmbeddingsByLabel.forEach((label, exampleEmbeddings) -> {
            double meanScore = 0.0;
            double maxScore = 0.0;
            for (Embedding exampleEmbedding : exampleEmbeddings) {
                double cosineSimilarity = CosineSimilarity.between((Embedding)textEmbedding, (Embedding)exampleEmbedding);
                double score = RelevanceScore.fromCosineSimilarity((double)cosineSimilarity);
                meanScore += score;
                maxScore = Math.max(score, maxScore);
            }
            double aggregateScore = this.aggregatedScore(meanScore /= (double)exampleEmbeddings.size(), maxScore);
            if (aggregateScore >= this.minScore) {
                scoredLabels.add(new ScoredLabel<Object>(label, aggregateScore));
            }
        });
        return new ClassificationResult(scoredLabels.stream().sorted(Comparator.comparingDouble(classificationResult -> 1.0 - classificationResult.score())).limit(this.maxResults).collect(Collectors.toList()));
    }

    private double aggregatedScore(double meanScore, double maxScore) {
        return this.meanToMaxScoreRatio * meanScore + (1.0 - this.meanToMaxScoreRatio) * maxScore;
    }
}

