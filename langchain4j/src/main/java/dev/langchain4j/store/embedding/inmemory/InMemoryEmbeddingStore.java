/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.spi.ServiceHelper
 *  dev.langchain4j.store.embedding.CosineSimilarity
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.RelevanceScore
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.store.embedding.inmemory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodecFactory;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.RelevanceScore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodec;
import dev.langchain4j.store.embedding.inmemory.JacksonInMemoryEmbeddingStoreJsonCodec;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryEmbeddingStore<Embedded>
implements EmbeddingStore<Embedded> {
    final CopyOnWriteArrayList<Entry<Embedded>> entries;

    public InMemoryEmbeddingStore() {
        this.entries = new CopyOnWriteArrayList();
    }

    private InMemoryEmbeddingStore(Collection<Entry<Embedded>> entries) {
        this.entries = new CopyOnWriteArrayList<Entry<Embedded>>(entries);
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.add(id, embedding, null);
    }

    public String add(Embedding embedding, Embedded embedded) {
        String id = Utils.randomUUID();
        this.add(id, embedding, embedded);
        return id;
    }

    public void add(String id, Embedding embedding, Embedded embedded) {
        this.entries.add(new Entry<Embedded>(id, embedding, embedded));
    }

    public List<String> addAll(List<Embedding> embeddings) {
        List<Entry<Embedded>> newEntries = embeddings.stream().map(embedding -> new Entry<Embedded>(Utils.randomUUID(), embedding)).collect(Collectors.toList());
        return this.add(newEntries);
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<Embedded> embedded) {
        if (ids.size() != embeddings.size() || embeddings.size() != embedded.size()) {
            throw new IllegalArgumentException("The list of ids and embeddings and embedded must have the same size");
        }
        ArrayList<Entry<Embedded>> newEntries = new ArrayList<Entry<Embedded>>(ids.size());
        for (int i = 0; i < ids.size(); ++i) {
            newEntries.add(new Entry<Embedded>(ids.get(i), embeddings.get(i), embedded.get(i)));
        }
        this.add(newEntries);
    }

    private List<String> add(List<Entry<Embedded>> newEntries) {
        this.entries.addAll(newEntries);
        return newEntries.stream().map(entry -> entry.id).collect(Collectors.toList());
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        Set<String> idSet = ids instanceof Set ? (Set<String>) ids : new HashSet<String>(ids);
        this.entries.removeIf(entry -> idSet.contains(entry.id));
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        this.entries.removeIf(entry -> InMemoryEmbeddingStore.matchesFilter(entry.embedded, filter));
    }

    public void removeAll() {
        this.entries.clear();
    }

    public EmbeddingSearchResult<Embedded> search(EmbeddingSearchRequest embeddingSearchRequest) {
        Comparator<EmbeddingMatch> comparator = Comparator.comparingDouble(EmbeddingMatch::score);
        PriorityQueue<EmbeddingMatch> matches = new PriorityQueue<EmbeddingMatch>(comparator);
        Filter filter = embeddingSearchRequest.filter();
        for (Entry<Embedded> entry : this.entries) {
            double cosineSimilarity;
            double score;
            if (!InMemoryEmbeddingStore.matchesFilter(entry.embedded, filter) || !((score = RelevanceScore.fromCosineSimilarity((double)(cosineSimilarity = CosineSimilarity.between((Embedding)entry.embedding, (Embedding)embeddingSearchRequest.queryEmbedding())))) >= embeddingSearchRequest.minScore())) continue;
            matches.add(new EmbeddingMatch(Double.valueOf(score), entry.id, entry.embedding, entry.embedded));
            if (matches.size() <= embeddingSearchRequest.maxResults()) continue;
            matches.poll();
        }
        ArrayList<EmbeddingMatch> result = new ArrayList<EmbeddingMatch>(matches);
        result.sort(comparator);
        Collections.reverse(result);
        return new EmbeddingSearchResult(result);
    }

    public String serializeToJson() {
        return InMemoryEmbeddingStore.loadCodec().toJson(this);
    }

    public void serializeToFile(Path filePath) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));){
            InMemoryEmbeddingStore.loadCodec().toJson(outputStream, this);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void serializeToFile(String filePath) {
        this.serializeToFile(Paths.get(filePath, new String[0]));
    }

    public static InMemoryEmbeddingStore<TextSegment> fromJson(String json) {
        return InMemoryEmbeddingStore.loadCodec().fromJson(json);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static InMemoryEmbeddingStore<TextSegment> fromFile(Path filePath) {
        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(filePath, new OpenOption[0]));){
            InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore = InMemoryEmbeddingStore.loadCodec().fromJson(inputStream);
            return inMemoryEmbeddingStore;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InMemoryEmbeddingStore<TextSegment> fromFile(String filePath) {
        return InMemoryEmbeddingStore.fromFile(Paths.get(filePath, new String[0]));
    }

    public static <Embedded> InMemoryEmbeddingStore<Embedded> merge(Collection<InMemoryEmbeddingStore<Embedded>> stores) {
        ValidationUtils.ensureNotNull(stores, (String)"stores");
        ArrayList<Entry<Embedded>> entries = new ArrayList<Entry<Embedded>>();
        for (InMemoryEmbeddingStore<Embedded> store : stores) {
            entries.addAll(store.entries);
        }
        return new InMemoryEmbeddingStore<Embedded>(entries);
    }

    public static <Embedded> InMemoryEmbeddingStore<Embedded> merge(InMemoryEmbeddingStore<Embedded> first, InMemoryEmbeddingStore<Embedded> second) {
        return InMemoryEmbeddingStore.merge(Arrays.asList(first, second));
    }

    private static boolean matchesFilter(Object embedded, Filter filter) {
        if (filter == null) {
            return true;
        }
        if (embedded instanceof TextSegment) {
            return filter.test((Object)((TextSegment)embedded).metadata());
        }
        return false;
    }

    private static InMemoryEmbeddingStoreJsonCodec loadCodec() {
        Iterator iterator = ServiceHelper.loadFactories(InMemoryEmbeddingStoreJsonCodecFactory.class).iterator();
        if (iterator.hasNext()) {
            InMemoryEmbeddingStoreJsonCodecFactory factory = (InMemoryEmbeddingStoreJsonCodecFactory)iterator.next();
            return factory.create();
        }
        return new JacksonInMemoryEmbeddingStoreJsonCodec();
    }

    @JsonIgnore
    public int size() {
        return this.entries.size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    static class Entry<Embedded> {
        String id;
        Embedding embedding;
        Embedded embedded;

        Entry(String id, Embedding embedding) {
            this(id, embedding, null);
        }

        Entry(String id, Embedding embedding, Embedded embedded) {
            this.id = ValidationUtils.ensureNotBlank((String)id, (String)"id");
            this.embedding = (Embedding)ValidationUtils.ensureNotNull((Object)embedding, (String)"embedding");
            this.embedded = embedded;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Entry that = (Entry)o;
            return Objects.equals(this.id, that.id) && Objects.equals(this.embedding, that.embedding) && Objects.equals(this.embedded, that.embedded);
        }

        public int hashCode() {
            return Objects.hash(this.id, this.embedding, this.embedded);
        }
    }
}

