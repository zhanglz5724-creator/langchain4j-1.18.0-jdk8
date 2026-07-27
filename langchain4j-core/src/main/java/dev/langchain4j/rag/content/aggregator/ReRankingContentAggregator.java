/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.aggregator;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReciprocalRankFuser;
import dev.langchain4j.rag.query.Query;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReRankingContentAggregator
implements ContentAggregator {
    public static final Function<Map<Query, Collection<List<Content>>>, Query> DEFAULT_QUERY_SELECTOR = queryToContents -> {
        if (queryToContents.size() > 1) {
            throw Exceptions.illegalArgument("The 'queryToContents' contains %s queries, making the re-ranking ambiguous. Because there are multiple queries, it is unclear which one should be used for re-ranking. Please provide a 'querySelector' in the constructor/builder.", queryToContents.size());
        }
        return (Query)queryToContents.keySet().iterator().next();
    };
    private final ScoringModel scoringModel;
    private final Function<Map<Query, Collection<List<Content>>>, Query> querySelector;
    private final Double minScore;
    private final Integer maxResults;

    public ReRankingContentAggregator(ScoringModel scoringModel) {
        this(scoringModel, DEFAULT_QUERY_SELECTOR, null);
    }

    public ReRankingContentAggregator(ScoringModel scoringModel, Function<Map<Query, Collection<List<Content>>>, Query> querySelector, Double minScore) {
        this(scoringModel, querySelector, minScore, null);
    }

    public ReRankingContentAggregator(ScoringModel scoringModel, Function<Map<Query, Collection<List<Content>>>, Query> querySelector, Double minScore, Integer maxResults) {
        this.scoringModel = ValidationUtils.ensureNotNull(scoringModel, "scoringModel");
        this.querySelector = Utils.getOrDefault(querySelector, DEFAULT_QUERY_SELECTOR);
        this.minScore = minScore;
        this.maxResults = Utils.getOrDefault(maxResults, Integer.MAX_VALUE);
    }

    public static ReRankingContentAggregatorBuilder builder() {
        return new ReRankingContentAggregatorBuilder();
    }

    @Override
    public List<Content> aggregate(Map<Query, Collection<List<Content>>> queryToContents) {
        if (queryToContents.isEmpty()) {
            return Collections.emptyList();
        }
        Query query = this.querySelector.apply(queryToContents);
        Map<Query, List<Content>> queryToFusedContents = this.fuse(queryToContents);
        List<Content> fusedContents = ReciprocalRankFuser.fuse(queryToFusedContents.values());
        if (fusedContents.isEmpty()) {
            return fusedContents;
        }
        return this.reRankAndFilter(fusedContents, query);
    }

    protected Map<Query, List<Content>> fuse(Map<Query, Collection<List<Content>>> queryToContents) {
        LinkedHashMap<Query, List<Content>> fused = new LinkedHashMap<Query, List<Content>>();
        for (Query query : queryToContents.keySet()) {
            Collection<List<Content>> contents = queryToContents.get(query);
            fused.put(query, ReciprocalRankFuser.fuse(contents));
        }
        return fused;
    }

    protected List<Content> reRankAndFilter(List<Content> contents, Query query) {
        List<TextSegment> segments = contents.stream().map(Content::textSegment).collect(Collectors.toList());
        List<Double> scores = this.scoringModel.scoreAll(segments, query.text()).content();
        HashMap<TextSegment, Double> segmentToScore = new HashMap<TextSegment, Double>();
        for (int i = 0; i < segments.size(); ++i) {
            segmentToScore.put(segments.get(i), scores.get(i));
        }
        return segmentToScore.entrySet().stream().filter(entry -> this.minScore == null || (Double)entry.getValue() >= this.minScore).sorted(Map.Entry.<TextSegment, Double>comparingByValue().reversed()).map(entry -> Content.from((TextSegment)entry.getKey(), Collections.singletonMap(ContentMetadata.RERANKED_SCORE, entry.getValue()))).limit(this.maxResults.intValue()).collect(Collectors.toList());
    }

    public static class ReRankingContentAggregatorBuilder {
        private ScoringModel scoringModel;
        private Function<Map<Query, Collection<List<Content>>>, Query> querySelector;
        private Double minScore;
        private Integer maxResults;

        ReRankingContentAggregatorBuilder() {
        }

        public ReRankingContentAggregatorBuilder scoringModel(ScoringModel scoringModel) {
            this.scoringModel = scoringModel;
            return this;
        }

        public ReRankingContentAggregatorBuilder querySelector(Function<Map<Query, Collection<List<Content>>>, Query> querySelector) {
            this.querySelector = querySelector;
            return this;
        }

        public ReRankingContentAggregatorBuilder minScore(Double minScore) {
            this.minScore = minScore;
            return this;
        }

        public ReRankingContentAggregatorBuilder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public ReRankingContentAggregator build() {
            return new ReRankingContentAggregator(this.scoringModel, this.querySelector, this.minScore, this.maxResults);
        }
    }
}

