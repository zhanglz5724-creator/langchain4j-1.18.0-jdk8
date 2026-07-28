/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ai.vespa.client.dsl.A
 *  ai.vespa.client.dsl.NearestNeighbor
 *  ai.vespa.client.dsl.Q
 *  ai.vespa.client.dsl.QueryChain
 *  ai.vespa.feed.client.DocumentId
 *  ai.vespa.feed.client.FeedClient
 *  ai.vespa.feed.client.FeedClientBuilder
 *  ai.vespa.feed.client.FeedException
 *  ai.vespa.feed.client.JsonFeeder
 *  ai.vespa.feed.client.JsonFeeder$ResultCallback
 *  ai.vespa.feed.client.Result
 *  ai.vespa.feed.client.Result$Type
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  okhttp3.ResponseBody
 *  retrofit2.Response
 */
package dev.langchain4j.store.embedding.vespa;

import ai.vespa.client.dsl.A;
import ai.vespa.client.dsl.NearestNeighbor;
import ai.vespa.client.dsl.Q;
import ai.vespa.client.dsl.QueryChain;
import ai.vespa.feed.client.DocumentId;
import ai.vespa.feed.client.FeedClient;
import ai.vespa.feed.client.FeedClientBuilder;
import ai.vespa.feed.client.FeedException;
import ai.vespa.feed.client.JsonFeeder;
import ai.vespa.feed.client.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.vespa.QueryResponse;
import dev.langchain4j.store.embedding.vespa.Record;
import dev.langchain4j.store.embedding.vespa.VespaApi;
import dev.langchain4j.store.embedding.vespa.VespaClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class VespaEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5L);
    static final String DEFAULT_NAMESPACE = "namespace";
    static final String DEFAULT_DOCUMENT_TYPE = "langchain4j";
    private static final String DEFAULT_CLUSTER_NAME = "langchain4j";
    private static final boolean DEFAULT_AVOID_DUPS = true;
    private static final String FIELD_NAME_TEXT_SEGMENT = "text_segment";
    private static final String FIELD_NAME_VECTOR = "vector";
    private static final String FIELD_NAME_DOCUMENT_ID = "documentid";
    private static final String DEFAULT_RANK_PROFILE = "langchain4j_relevance_score";
    private static final int DEFAULT_TARGET_HITS = 10;
    private final String url;
    private final Path keyPath;
    private final Path certPath;
    private final Duration timeout;
    private final String namespace;
    private final String documentType;
    private final String clusterName;
    private final String rankProfile;
    private final int targetHits;
    private final boolean avoidDups;
    private final boolean logRequests;
    private final boolean logResponses;
    private VespaApi api;

    public VespaEmbeddingStore(String url, String keyPath, String certPath, Duration timeout, String namespace, String documentType, String clusterName, String rankProfile, Integer targetHits, Boolean avoidDups, Boolean logRequests, Boolean logResponses) {
        ValidationUtils.ensureNotNull((Object)url, (String)"url");
        this.url = url;
        this.keyPath = keyPath != null ? Paths.get(keyPath, new String[0]) : null;
        this.certPath = certPath != null ? Paths.get(certPath, new String[0]) : null;
        this.timeout = (Duration)Utils.getOrDefault((Object)timeout, (Object)DEFAULT_TIMEOUT);
        this.namespace = (String)Utils.getOrDefault((Object)namespace, (Object)DEFAULT_NAMESPACE);
        this.documentType = (String)Utils.getOrDefault((Object)documentType, (Object)"langchain4j");
        this.clusterName = (String)Utils.getOrDefault((Object)clusterName, (Object)"langchain4j");
        this.rankProfile = (String)Utils.getOrDefault((Object)rankProfile, (Object)DEFAULT_RANK_PROFILE);
        this.targetHits = (Integer)Utils.getOrDefault((Object)targetHits, (Object)10);
        this.avoidDups = (Boolean)Utils.getOrDefault((Object)avoidDups, (Object)true);
        this.logRequests = (Boolean)Utils.getOrDefault((Object)logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)logResponses, (Object)false);
    }

    private static EmbeddingMatch<TextSegment> toEmbeddingMatch(Record in) {
        return new EmbeddingMatch(in.relevance(), DocumentId.of((String)in.fields().documentid()).userSpecific(), Embedding.from(in.fields().vector().values()), in.fields().textSegment() != null ? TextSegment.from((String)in.fields().textSegment()) : null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String add(Embedding embedding) {
        return this.add(null, embedding, null);
    }

    public void add(String id, Embedding embedding) {
        this.add(id, embedding, null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        return this.add(null, embedding, textSegment);
    }

    public List<String> addAll(List<Embedding> embeddings) {
        return this.addAll(embeddings, null);
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            return;
        }
        if (ids.size() != embeddings.size()) {
            throw new IllegalArgumentException("The list of ids and embeddings must have the same size");
        }
        if (embedded != null && embeddings.size() != embedded.size()) {
            throw new IllegalArgumentException("The list of embeddings and embedded must have the same size");
        }
        try (JsonFeeder jsonFeeder = this.feeder();){
            ArrayList<Record> records = new ArrayList<Record>();
            for (int i = 0; i < embeddings.size(); ++i) {
                records.add(this.buildRecord(ids.get(i), embeddings.get(i), embedded != null ? embedded.get(i) : null));
            }
            jsonFeeder.feedMany((InputStream)new ByteArrayInputStream(OBJECT_MAPPER.writeValueAsString(records).getBytes()), new JsonFeeder.ResultCallback(){

                public void onNextResult(Result result, FeedException error) {
                    if (error != null) {
                        throw new RuntimeException(error.getMessage());
                    }
                }

                public void onError(FeedException error) {
                    throw new RuntimeException(error.getMessage());
                }
            });
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        try {
            String searchQuery = Q.select((String)FIELD_NAME_DOCUMENT_ID, (String[])new String[]{FIELD_NAME_TEXT_SEGMENT, FIELD_NAME_VECTOR}).from(this.documentType).where((QueryChain)this.buildNearestNeighbor()).fix().hits(request.maxResults()).ranking(this.rankProfile).param("input.query(q)", OBJECT_MAPPER.writeValueAsString((Object)request.queryEmbedding().vectorAsList())).param("input.query(threshold)", String.valueOf(request.minScore())).build();
            Response response = this.api().search(searchQuery).execute();
            if (response.isSuccessful()) {
                QueryResponse parsedResponse = (QueryResponse)((Object)response.body());
                List<Record> children = parsedResponse.root().children();
                return new EmbeddingSearchResult(Utils.isNullOrEmpty(children) ? new ArrayList() : children.stream().map(VespaEmbeddingStore::toEmbeddingMatch).toList());
            }
            throw VespaEmbeddingStore.toException(response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll() {
        try {
            Response response = this.api().deleteAll(this.namespace, this.documentType, this.clusterName).execute();
            if (!response.isSuccessful()) {
                throw VespaEmbeddingStore.toException(response);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String add(String id, Embedding embedding, TextSegment textSegment) {
        AtomicReference resId = new AtomicReference();
        try (JsonFeeder jsonFeeder = this.feeder();){
            jsonFeeder.feedSingle(OBJECT_MAPPER.writeValueAsString((Object)this.buildRecord(id, embedding, textSegment))).whenComplete((result, throwable) -> {
                if (throwable != null) {
                    throw new RuntimeException((Throwable)throwable);
                }
                if (Result.Type.success.equals((Object)result.type())) {
                    resId.set(result.documentId().userSpecific());
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (String)resId.get();
    }

    private JsonFeeder feeder() {
        FeedClientBuilder fcBuilder = FeedClientBuilder.create((URI)URI.create(this.url));
        if (this.certPath != null && this.keyPath != null) {
            fcBuilder.setCertificate(this.certPath, this.keyPath);
        }
        return JsonFeeder.builder((FeedClient)fcBuilder.build()).withTimeout(this.timeout).build();
    }

    private VespaApi api() {
        if (this.api == null) {
            this.api = VespaClient.createInstance(this.url, this.certPath, this.keyPath, this.logRequests, this.logResponses);
        }
        return this.api;
    }

    private Record buildRecord(String id, Embedding embedding, TextSegment textSegment) {
        String recordId = id != null ? id : (this.avoidDups && textSegment != null ? Utils.generateUUIDFrom((String)textSegment.text()) : Utils.randomUUID());
        DocumentId documentId = DocumentId.of((String)this.namespace, (String)this.documentType, (String)recordId);
        String text = textSegment != null ? textSegment.text() : null;
        return new Record(documentId.toString(), null, new Record.Fields(null, text, new Record.Fields.Vector(embedding.vectorAsList())));
    }

    private NearestNeighbor buildNearestNeighbor() {
        NearestNeighbor nb = Q.nearestNeighbor((String)FIELD_NAME_VECTOR, (String)"q");
        nb.annotate(A.a((String)"targetHits", (Object)this.targetHits));
        return nb;
    }

    private static RuntimeException toException(Response<?> response) throws IOException {
        try (ResponseBody responseBody = response.errorBody();){
            int code = response.code();
            if (responseBody != null) {
                String body = responseBody.string();
                String errorMessage = String.format("status code: %s; body: %s", code, body);
                RuntimeException runtimeException = new RuntimeException(errorMessage);
                return runtimeException;
            }
            RuntimeException runtimeException = new RuntimeException(String.format("status code: %s;", code));
            return runtimeException;
        }
    }

    public static class Builder {
        private String url;
        private String keyPath;
        private String certPath;
        private Duration timeout;
        private String namespace;
        private String documentType;
        private String clusterName;
        private String rankProfile;
        private Integer targetHits;
        private Boolean avoidDups;
        private Boolean logRequests;
        private Boolean logResponses;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder keyPath(String keyPath) {
            this.keyPath = keyPath;
            return this;
        }

        public Builder certPath(String certPath) {
            this.certPath = certPath;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder clusterName(String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public Builder rankProfile(String rankProfile) {
            this.rankProfile = rankProfile;
            return this;
        }

        public Builder targetHits(Integer targetHits) {
            this.targetHits = targetHits;
            return this;
        }

        public Builder avoidDups(Boolean avoidDups) {
            this.avoidDups = avoidDups;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public VespaEmbeddingStore build() {
            return new VespaEmbeddingStore(this.url, this.keyPath, this.certPath, this.timeout, this.namespace, this.documentType, this.clusterName, this.rankProfile, this.targetHits, this.avoidDups, this.logRequests, this.logResponses);
        }
    }
}

