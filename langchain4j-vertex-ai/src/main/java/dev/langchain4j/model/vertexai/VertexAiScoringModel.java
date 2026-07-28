/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.discoveryengine.v1beta.RankRequest
 *  com.google.cloud.discoveryengine.v1beta.RankRequest$Builder
 *  com.google.cloud.discoveryengine.v1beta.RankResponse
 *  com.google.cloud.discoveryengine.v1beta.RankServiceClient
 *  com.google.cloud.discoveryengine.v1beta.RankServiceSettings
 *  com.google.cloud.discoveryengine.v1beta.RankingConfigName
 *  com.google.cloud.discoveryengine.v1beta.RankingRecord
 *  com.google.cloud.discoveryengine.v1beta.RankingRecord$Builder
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.scoring.ScoringModel
 */
package dev.langchain4j.model.vertexai;

import com.google.cloud.discoveryengine.v1beta.RankRequest;
import com.google.cloud.discoveryengine.v1beta.RankResponse;
import com.google.cloud.discoveryengine.v1beta.RankServiceClient;
import com.google.cloud.discoveryengine.v1beta.RankServiceSettings;
import com.google.cloud.discoveryengine.v1beta.RankingConfigName;
import com.google.cloud.discoveryengine.v1beta.RankingRecord;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.scoring.ScoringModel;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VertexAiScoringModel
implements ScoringModel {
    private final String model;
    private final String projectId;
    private final String projectNumber;
    private final String location;
    private final String titleMetadataKey;

    public VertexAiScoringModel(String projectId, String projectNumber, String location, String model, String titleMetadataKey) {
        this.projectId = ValidationUtils.ensureNotBlank((String)projectId, (String)"projectId");
        this.projectNumber = ValidationUtils.ensureNotBlank((String)projectNumber, (String)"projectNumber");
        this.location = ValidationUtils.ensureNotBlank((String)location, (String)"location");
        this.model = ValidationUtils.ensureNotBlank((String)model, (String)"model");
        this.titleMetadataKey = titleMetadataKey != null ? titleMetadataKey : "title";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        AtomicInteger counter = new AtomicInteger();
        try (RankServiceClient rankServiceClient = RankServiceClient.create((RankServiceSettings)RankServiceSettings.newBuilder().build());){
            RankRequest.Builder rankingRequestBuilder = RankRequest.newBuilder();
            if (this.model != null && !this.model.isEmpty()) {
                rankingRequestBuilder.setModel(this.model);
            }
            rankingRequestBuilder.setRankingConfig(RankingConfigName.newBuilder().setProject(this.projectId).setLocation(this.location).setRankingConfig(String.format("projects/%s/locations/%s/rankingConfigs/default_ranking_config.", this.projectNumber, this.location)).build().getRankingConfig()).setQuery(query).setIgnoreRecordDetailsInResponse(true).addAllRecords((Iterable)segments.stream().map(segment -> {
                RankingRecord.Builder rankingBuilder = RankingRecord.newBuilder().setContent(segment.text());
                if (segment.metadata().getString(this.titleMetadataKey) != null) {
                    rankingBuilder.setTitle(segment.metadata().getString(this.titleMetadataKey));
                }
                rankingBuilder.setId(String.valueOf(counter.getAndIncrement()));
                return rankingBuilder.build();
            }).collect(Collectors.toList()));
            RankResponse rankResponse = rankServiceClient.rank(rankingRequestBuilder.build());
            Response response = Response.from(rankResponse.getRecordsList().stream().sorted(Comparator.comparing(rr -> Double.valueOf(rr.getId()))).map(RankingRecord::getScore).map(Double::valueOf).collect(Collectors.toList()));
            return response;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String model;
        private String projectId;
        private String projectNumber;
        private String location;
        private String titleMetadataKey;

        public Builder model(String model) {
            this.model = ValidationUtils.ensureNotBlank((String)model, (String)"model");
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder projectNumber(String projectNumber) {
            this.projectNumber = projectNumber;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder titleMetadataKey(String titleMetadataKey) {
            this.titleMetadataKey = ValidationUtils.ensureNotBlank((String)titleMetadataKey, (String)"titleMetadataKey");
            return this;
        }

        public VertexAiScoringModel build() {
            return new VertexAiScoringModel(this.projectId, this.projectNumber, this.location, this.model, this.titleMetadataKey);
        }
    }
}

