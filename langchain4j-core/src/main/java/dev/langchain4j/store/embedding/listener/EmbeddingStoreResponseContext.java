/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.listener.EmbeddingStoreRequestContext;
import java.util.List;
import java.util.Map;

@Experimental
public abstract class EmbeddingStoreResponseContext<Embedded> {
    private final EmbeddingStoreRequestContext<Embedded> requestContext;
    private final Map<Object, Object> attributes;

    protected EmbeddingStoreResponseContext(EmbeddingStoreRequestContext<Embedded> requestContext, Map<Object, Object> attributes) {
        this.requestContext = ValidationUtils.ensureNotNull(requestContext, "requestContext");
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public EmbeddingStore<Embedded> embeddingStore() {
        return this.requestContext.embeddingStore();
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }

    public EmbeddingStoreRequestContext<Embedded> requestContext() {
        return this.requestContext;
    }

    @Experimental
    public static final class RemoveAll<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        public RemoveAll(EmbeddingStoreRequestContext.RemoveAll<Embedded> requestContext, Map<Object, Object> attributes) {
            super(requestContext, attributes);
        }
    }

    @Experimental
    public static final class RemoveAllFilter<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        public RemoveAllFilter(EmbeddingStoreRequestContext.RemoveAllFilter<Embedded> requestContext, Map<Object, Object> attributes) {
            super(requestContext, attributes);
        }
    }

    @Experimental
    public static final class RemoveAllIds<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        public RemoveAllIds(EmbeddingStoreRequestContext.RemoveAllIds<Embedded> requestContext, Map<Object, Object> attributes) {
            super(requestContext, attributes);
        }
    }

    @Experimental
    public static final class Remove<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        public Remove(EmbeddingStoreRequestContext.Remove<Embedded> requestContext, Map<Object, Object> attributes) {
            super(requestContext, attributes);
        }
    }

    @Experimental
    public static final class Search<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        private final EmbeddingSearchResult<Embedded> searchResult;

        public Search(EmbeddingStoreRequestContext<Embedded> requestContext, Map<Object, Object> attributes, EmbeddingSearchResult<Embedded> searchResult) {
            super(requestContext, attributes);
            this.searchResult = searchResult;
        }

        public EmbeddingSearchResult<Embedded> searchResult() {
            return this.searchResult;
        }
    }

    @Experimental
    public static final class AddAll<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        private final List<String> returnedIds;

        public AddAll(EmbeddingStoreRequestContext<Embedded> requestContext, Map<Object, Object> attributes, List<String> returnedIds) {
            super(requestContext, attributes);
            this.returnedIds = Utils.copy(returnedIds);
        }

        public List<String> returnedIds() {
            return this.returnedIds;
        }
    }

    @Experimental
    public static final class Add<Embedded>
    extends EmbeddingStoreResponseContext<Embedded> {
        private final String returnedId;

        public Add(EmbeddingStoreRequestContext<Embedded> requestContext, Map<Object, Object> attributes, String returnedId) {
            super(requestContext, attributes);
            this.returnedId = returnedId;
        }

        public String returnedId() {
            return this.returnedId;
        }
    }
}

