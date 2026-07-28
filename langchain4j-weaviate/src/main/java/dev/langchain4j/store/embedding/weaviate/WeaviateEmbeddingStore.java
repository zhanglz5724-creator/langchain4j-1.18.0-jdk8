/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  io.weaviate.client.Config
 *  io.weaviate.client.WeaviateAuthClient
 *  io.weaviate.client.WeaviateClient
 *  io.weaviate.client.base.Result
 *  io.weaviate.client.base.WeaviateErrorMessage
 *  io.weaviate.client.v1.auth.exception.AuthException
 *  io.weaviate.client.v1.data.model.WeaviateObject
 *  io.weaviate.client.v1.filters.WhereFilter
 *  io.weaviate.client.v1.graphql.model.GraphQLError
 *  io.weaviate.client.v1.graphql.model.GraphQLResponse
 *  io.weaviate.client.v1.graphql.query.argument.NearVectorArgument
 *  io.weaviate.client.v1.graphql.query.fields.Field
 *  org.apache.commons.lang3.ArrayUtils
 */
package dev.langchain4j.store.embedding.weaviate;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateAuthClient;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.base.WeaviateErrorMessage;
import io.weaviate.client.v1.auth.exception.AuthException;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.filters.WhereFilter;
import io.weaviate.client.v1.graphql.model.GraphQLError;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument;
import io.weaviate.client.v1.graphql.query.fields.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;

public class WeaviateEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final String ADDITIONALS = "_additional";
    private static final String NULL_VALUE = "<null>";
    private final WeaviateClient client;
    private final String objectClass;
    private final boolean avoidDups;
    private final String consistencyLevel;
    private final String metadataFieldName;
    private final Collection<String> metadataKeys;
    private final String textFieldName;

    public WeaviateEmbeddingStore(String apiKey, String scheme, String host, Integer port, Boolean useGrpcForInserts, Boolean securedGrpc, Integer grpcPort, String objectClass, Boolean avoidDups, String consistencyLevel, Collection<String> metadataKeys, String textFieldName, String metadataFieldName) {
        try {
            Config config = new Config(ValidationUtils.ensureNotBlank((String)scheme, (String)"scheme"), WeaviateEmbeddingStore.concatenate(ValidationUtils.ensureNotBlank((String)host, (String)"host"), port));
            if (((Boolean)Utils.getOrDefault((Object)useGrpcForInserts, (Object)Boolean.FALSE)).booleanValue()) {
                config.setGRPCSecured(((Boolean)Utils.getOrDefault((Object)securedGrpc, (Object)Boolean.FALSE)).booleanValue());
                config.setGRPCHost(host + ":" + Utils.getOrDefault((Object)grpcPort, (Object)50051));
            }
            this.client = Utils.isNullOrBlank((String)apiKey) ? new WeaviateClient(config) : WeaviateAuthClient.apiKey((Config)config, (String)apiKey);
        }
        catch (AuthException e) {
            throw new IllegalArgumentException(e);
        }
        this.objectClass = (String)Utils.getOrDefault((Object)objectClass, (Object)"Default");
        this.avoidDups = (Boolean)Utils.getOrDefault((Object)avoidDups, (Object)true);
        this.consistencyLevel = (String)Utils.getOrDefault((Object)consistencyLevel, (Object)"QUORUM");
        this.metadataFieldName = (String)Utils.getOrDefault((Object)metadataFieldName, (Object)"_metadata");
        this.metadataKeys = (Collection)Utils.getOrDefault(metadataKeys, Collections.emptyList());
        this.textFieldName = (String)Utils.getOrDefault((Object)textFieldName, (Object)"text");
    }

    private static String concatenate(String host, Integer port) {
        if (port == null) {
            return host;
        }
        return host + ":" + port;
    }

    public static WeaviateEmbeddingStoreBuilder builder() {
        return new WeaviateEmbeddingStoreBuilder();
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        return this.addAll(Collections.singletonList(embedding), Collections.singletonList(textSegment)).stream().findFirst().orElse(null);
    }

    public List<String> addAll(List<Embedding> embeddings) {
        return this.addAll(embeddings, null);
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.client.batch().objectsBatchDeleter().withClassName(this.objectClass).withWhere(WhereFilter.builder().path(new String[]{"id"}).operator("ContainsAny").valueText(ids.toArray(new String[0])).build()).run();
    }

    public void removeAll() {
        List objects = (List)this.client.data().objectsGetter().withClassName(this.objectClass).run().getResult();
        if (objects == null || objects.isEmpty()) {
            return;
        }
        List<String> allIds = objects.stream().map(WeaviateObject::getId).collect(Collectors.toList());
        this.removeAll(allIds);
    }

    public void remove(String id) {
        ValidationUtils.ensureNotBlank((String)id, (String)"id");
        this.client.data().deleter().withClassName(this.objectClass).withID(id).run();
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        List objects = (List)this.client.data().objectsGetter().withClassName(this.objectClass).run().getResult();
        if (objects == null || objects.isEmpty()) {
            return;
        }
        List<String> idsToDelete = objects.stream().filter(obj -> obj.getProperties() != null && WeaviateEmbeddingStore.matchesFilter(obj.getProperties(), this.metadataFieldName, this.textFieldName, filter)).map(WeaviateObject::getId).collect(Collectors.toList());
        if (!idsToDelete.isEmpty()) {
            this.removeAll(idsToDelete);
        }
    }

    static boolean matchesFilter(Map<String, Object> properties, String metadataFieldName, String textFieldName, Filter filter) {
        Map<String, Object> metadata;
        if (metadataFieldName.isEmpty()) {
            metadata = new HashMap<String, Object>(properties);
            metadata.remove(textFieldName);
            metadata.remove(ADDITIONALS);
            metadata.remove("indexFilterable");
            metadata.remove("indexSearchable");
            metadata.values().removeIf(NULL_VALUE::equals);
        } else {
            Object metadataObj = properties.get(metadataFieldName);
            if (!(metadataObj instanceof Map)) {
                return false;
            }
            Map nested = (Map)metadataObj;
            metadata = nested;
        }
        return filter.test((Object)new Metadata(metadata));
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        Result result;
        ArrayList<Object> fields = new ArrayList<Object>();
        fields.add(Field.builder().name(this.textFieldName).build());
        fields.add(Field.builder().name(ADDITIONALS).fields(new Field[]{Field.builder().name("id").build(), Field.builder().name("certainty").build(), Field.builder().name("vector").build()}).build());
        if (!this.metadataKeys.isEmpty()) {
            ArrayList<Field> metadataFields = new ArrayList<Field>();
            for (String property : this.metadataKeys) {
                metadataFields.add(Field.builder().name(property).build());
            }
            if (!this.metadataFieldName.isEmpty()) {
                fields.add(Field.builder().name(this.metadataFieldName).fields(metadataFields.toArray(new Field[0])).build());
            } else {
                fields.addAll(metadataFields);
            }
        }
        if ((result = this.client.graphQL().get().withClassName(this.objectClass).withFields(fields.toArray(new Field[0])).withNearVector(NearVectorArgument.builder().vector(request.queryEmbedding().vectorAsList().toArray(new Float[0])).certainty(Float.valueOf((float)request.minScore())).build()).withLimit(Integer.valueOf(request.maxResults())).run()).hasErrors()) {
            throw new IllegalArgumentException(result.getError().getMessages().stream().map(WeaviateErrorMessage::getMessage).collect(Collectors.joining("\n")));
        }
        GraphQLError[] errors = ((GraphQLResponse)result.getResult()).getErrors();
        if (errors != null && errors.length > 0) {
            throw new IllegalArgumentException(Arrays.stream(errors).map(GraphQLError::getMessage).collect(Collectors.joining("\n")));
        }
        Optional resGetPart = ((Map)((GraphQLResponse)result.getResult()).getData()).entrySet().stream().findFirst();
        if (!resGetPart.isPresent()) {
            return new EmbeddingSearchResult(Collections.emptyList());
        }
        Optional resItemsPart = ((Map)((Map.Entry)resGetPart.get()).getValue()).entrySet().stream().findFirst();
        if (!resItemsPart.isPresent()) {
            return new EmbeddingSearchResult(Collections.emptyList());
        }
        List resItems = (List)((Map.Entry)resItemsPart.get()).getValue();
        List matches = resItems.stream().map(item -> this.toEmbeddingMatch((Map<String, ?>)item)).collect(Collectors.toList());
        return new EmbeddingSearchResult(matches);
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (embedded != null && embeddings.size() != embedded.size()) {
            throw new IllegalArgumentException("The list of embeddings and embedded must have the same size");
        }
        ArrayList<String> resIds = new ArrayList<String>();
        ArrayList<WeaviateObject> objects = new ArrayList<WeaviateObject>();
        for (int i = 0; i < embeddings.size(); ++i) {
            String id = ids != null ? ids.get(i) : (this.avoidDups && embedded != null ? Utils.generateUUIDFrom((String)embedded.get(i).text()) : Utils.randomUUID());
            resIds.add(id);
            objects.add(this.buildObject(id, embeddings.get(i), embedded != null ? embedded.get(i) : null));
        }
        this.client.batch().objectsBatcher().withObjects(objects.toArray(new WeaviateObject[0])).withConsistencyLevel(this.consistencyLevel).run();
    }

    private WeaviateObject buildObject(String id, Embedding embedding, TextSegment segment) {
        HashMap<String, Object> props = new HashMap<String, Object>();
        Map<String, Object> metadata = this.prefillMetadata();
        if (segment != null) {
            props.put(this.textFieldName, segment.text());
            Map metadataMap = segment.metadata().toMap();
            for (String metadataKey : this.metadataKeys) {
                if (!metadataMap.containsKey(metadataKey)) continue;
                Object metadataValue = metadataMap.get(metadataKey);
                metadata.put(metadataKey, Objects.toString(metadataValue, null));
            }
            this.setMetadata(props, metadata);
        } else {
            props.put(this.textFieldName, "");
            this.setMetadata(props, metadata);
        }
        props.put("indexFilterable", true);
        props.put("indexSearchable", true);
        return WeaviateObject.builder().className(this.objectClass).id(id).vector(embedding.vectorAsList().toArray(ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY)).properties(props).build();
    }

    private void setMetadata(Map<String, Object> props, Map<String, Object> metadata) {
        if (metadata != null && !metadata.isEmpty()) {
            if (!this.metadataFieldName.isEmpty()) {
                props.put(this.metadataFieldName, metadata);
            } else {
                props.putAll(metadata);
            }
        }
    }

    private Map<String, Object> prefillMetadata() {
        HashMap<String, Object> metadata = new HashMap<String, Object>(this.metadataKeys.size());
        for (String property : this.metadataKeys) {
            metadata.put(property, NULL_VALUE);
        }
        return metadata;
    }

    private EmbeddingMatch<TextSegment> toEmbeddingMatch(Map<String, ?> item) {
        Map additional = (Map)item.get(ADDITIONALS);
        Double score = (Double)additional.get("certainty");
        String embeddingId = (String)additional.get("id");
        Embedding embedding = WeaviateEmbeddingStore.toEmbedding(additional);
        String text = (String)item.get(this.textFieldName);
        Metadata metadata = this.toMetadata(item);
        TextSegment textSegment = Utils.isNullOrBlank((String)text) ? null : TextSegment.from((String)text, (Metadata)metadata);
        return new EmbeddingMatch(score, embeddingId, embedding, (Object)textSegment);
    }

    private Metadata toMetadata(Map<String, ?> item) {
        Map<Object, Object> metadataMap = new HashMap();
        if (this.metadataFieldName.isEmpty()) {
            metadataMap = new HashMap(item);
            metadataMap.remove(this.textFieldName);
            metadataMap.remove(ADDITIONALS);
        } else if (item.get(this.metadataFieldName) instanceof Map) {
            metadataMap = (Map)item.get(this.metadataFieldName);
        }
        if (!this.metadataKeys.isEmpty()) {
            metadataMap.keySet().retainAll(this.metadataKeys);
        }
        metadataMap = metadataMap.entrySet().stream().filter(entry -> entry.getValue() != null).filter(entry -> !NULL_VALUE.equals(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Metadata(metadataMap);
    }

    private static Embedding toEmbedding(Map<String, ?> additional) {
        List vector = ((List)additional.get("vector")).stream().map(Double::floatValue).collect(Collectors.toList());
        return Embedding.from(vector);
    }

    public static class WeaviateEmbeddingStoreBuilder {
        private String apiKey;
        private String scheme;
        private String host;
        private Integer port;
        private Boolean useGrpcForInserts;
        private Boolean securedGrpc;
        private Integer grpcPort;
        private String objectClass;
        private Boolean avoidDups;
        private String consistencyLevel;
        private Collection<String> metadataKeys;
        private String textFieldName;
        private String metadataFieldName;

        WeaviateEmbeddingStoreBuilder() {
        }

        public WeaviateEmbeddingStoreBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder scheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder host(String host) {
            this.host = host;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder useGrpcForInserts(Boolean useGrpcForInserts) {
            this.useGrpcForInserts = useGrpcForInserts;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder securedGrpc(Boolean securedGrpc) {
            this.securedGrpc = securedGrpc;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder grpcPort(Integer grpcPort) {
            this.grpcPort = grpcPort;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder objectClass(String objectClass) {
            this.objectClass = objectClass;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder avoidDups(Boolean avoidDups) {
            this.avoidDups = avoidDups;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder consistencyLevel(String consistencyLevel) {
            this.consistencyLevel = consistencyLevel;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder metadataKeys(Collection<String> metadataKeys) {
            this.metadataKeys = metadataKeys;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder textFieldName(String textFieldName) {
            this.textFieldName = textFieldName;
            return this;
        }

        public WeaviateEmbeddingStoreBuilder metadataFieldName(String metadataFieldName) {
            this.metadataFieldName = metadataFieldName;
            return this;
        }

        public WeaviateEmbeddingStore build() {
            return new WeaviateEmbeddingStore(this.apiKey, this.scheme, this.host, this.port, this.useGrpcForInserts, this.securedGrpc, this.grpcPort, this.objectClass, this.avoidDups, this.consistencyLevel, this.metadataKeys, this.textFieldName, this.metadataFieldName);
        }

        public String toString() {
            return "WeaviateEmbeddingStore.WeaviateEmbeddingStoreBuilder(apiKey=" + (this.apiKey == null ? null : "********") + ", scheme=" + this.scheme + ", host=" + this.host + ", port=" + this.port + ", useGrpcForInserts=" + this.useGrpcForInserts + ", securedGrpc=" + this.securedGrpc + ", grpcPort=" + this.grpcPort + ", objectClass=" + this.objectClass + ", avoidDups=" + this.avoidDups + ", consistencyLevel=" + this.consistencyLevel + ", metadataKeys=" + this.metadataKeys + ", textFieldName=" + this.textFieldName + ", metadataFieldName=" + this.metadataFieldName + ")";
        }
    }
}

