/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.List;
import java.util.Map;

@Experimental
public abstract class EmbeddingStoreRequestContext<Embedded> {
    private final EmbeddingStore<Embedded> embeddingStore;
    private final Map<Object, Object> attributes;

    protected EmbeddingStoreRequestContext(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes) {
        this.embeddingStore = ValidationUtils.ensureNotNull(embeddingStore, "embeddingStore");
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public EmbeddingStore<Embedded> embeddingStore() {
        return this.embeddingStore;
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }

    @Experimental
    public static final class RemoveAll<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        public RemoveAll(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes) {
            super(embeddingStore, attributes);
        }
    }

    @Experimental
    public static final class RemoveAllFilter<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        private final Filter filter;

        public RemoveAllFilter(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, Filter filter) {
            super(embeddingStore, attributes);
            this.filter = filter;
        }

        public Filter filter() {
            return this.filter;
        }
    }

    @Experimental
    public static final class RemoveAllIds<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        private final List<String> ids;

        public RemoveAllIds(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, List<String> ids) {
            super(embeddingStore, attributes);
            this.ids = Utils.copy(ids);
        }

        public List<String> ids() {
            return this.ids;
        }
    }

    @Experimental
    public static final class Remove<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        private final String id;

        public Remove(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, String id) {
            super(embeddingStore, attributes);
            this.id = id;
        }

        public String id() {
            return this.id;
        }
    }

    @Experimental
    public static final class Search<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        private final EmbeddingSearchRequest searchRequest;

        public Search(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, EmbeddingSearchRequest searchRequest) {
            super(embeddingStore, attributes);
            this.searchRequest = searchRequest;
        }

        public EmbeddingSearchRequest searchRequest() {
            return this.searchRequest;
        }
    }

    @Experimental
    public static final class AddAll<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        private final List<String> ids;
        private final List<Embedding> embeddings;
        private final List<Embedded> embeddedList;

        public AddAll(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, List<String> ids, List<Embedding> embeddings, List<Embedded> embeddedList) {
            super(embeddingStore, attributes);
            this.ids = Utils.copy(ids);
            this.embeddings = Utils.copy(embeddings);
            this.embeddedList = Utils.copy(embeddedList);
        }

        public List<String> ids() {
            return this.ids;
        }

        public List<Embedding> embeddings() {
            return this.embeddings;
        }

        public List<Embedded> embeddedList() {
            return this.embeddedList;
        }
    }

    @Experimental
    public static final class Add<Embedded>
    extends EmbeddingStoreRequestContext<Embedded> {
        private final String id;
        private final Embedding embedding;
        private final Embedded embedded;

        public Add(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, Embedding embedding) {
            this(embeddingStore, attributes, null, embedding, null);
        }

        public Add(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, String id, Embedding embedding) {
            this(embeddingStore, attributes, id, embedding, null);
        }

        public Add(EmbeddingStore<Embedded> embeddingStore, Map<Object, Object> attributes, String id, Embedding embedding, Embedded embedded) {
            super(embeddingStore, attributes);
            this.id = id;
            this.embedding = embedding;
            this.embedded = embedded;
        }

        public String id() {
            return this.id;
        }

        public Embedding embedding() {
            return this.embedding;
        }

        public Embedded embedded() {
            return this.embedded;
        }
    }
}

