/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.segment.TextSegmentTransformer;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.data.document.splitter.DocumentSplitterFactory;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.IngestionResult;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddingStoreIngestor {
    private static final Logger log = LoggerFactory.getLogger(EmbeddingStoreIngestor.class);
    private final DocumentTransformer documentTransformer;
    private final DocumentSplitter documentSplitter;
    private final TextSegmentTransformer textSegmentTransformer;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingInputType embeddingInputType;

    public EmbeddingStoreIngestor(DocumentTransformer documentTransformer, DocumentSplitter documentSplitter, TextSegmentTransformer textSegmentTransformer, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this(documentTransformer, documentSplitter, textSegmentTransformer, embeddingModel, embeddingStore, null);
    }

    private EmbeddingStoreIngestor(DocumentTransformer documentTransformer, DocumentSplitter documentSplitter, TextSegmentTransformer textSegmentTransformer, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, EmbeddingInputType embeddingInputType) {
        this.documentTransformer = documentTransformer;
        this.documentSplitter = Utils.getOrDefault(documentSplitter, EmbeddingStoreIngestor::loadDocumentSplitter);
        this.textSegmentTransformer = textSegmentTransformer;
        this.embeddingModel = ValidationUtils.ensureNotNull(Utils.getOrDefault(embeddingModel, EmbeddingStoreIngestor::loadEmbeddingModel), "embeddingModel");
        this.embeddingStore = ValidationUtils.ensureNotNull(embeddingStore, "embeddingStore");
        this.embeddingInputType = embeddingInputType;
    }

    private static DocumentSplitter loadDocumentSplitter() {
        Collection<DocumentSplitterFactory> factories = ServiceHelper.loadFactories(DocumentSplitterFactory.class);
        if (factories.size() > 1) {
            throw new RuntimeException("Conflict: multiple document splitters have been found in the classpath. Please explicitly specify the one you wish to use.");
        }
        Iterator<DocumentSplitterFactory> iterator = factories.iterator();
        if (iterator.hasNext()) {
            DocumentSplitterFactory factory = iterator.next();
            DocumentSplitter documentSplitter = factory.create();
            log.debug("Loaded the following document splitter through SPI: {}", (Object)documentSplitter);
            return documentSplitter;
        }
        return null;
    }

    private static EmbeddingModel loadEmbeddingModel() {
        Collection<EmbeddingModelFactory> factories = ServiceHelper.loadFactories(EmbeddingModelFactory.class);
        if (factories.size() > 1) {
            throw new RuntimeException("Conflict: multiple embedding models have been found in the classpath. Please explicitly specify the one you wish to use.");
        }
        Iterator<EmbeddingModelFactory> iterator = factories.iterator();
        if (iterator.hasNext()) {
            EmbeddingModelFactory factory = iterator.next();
            EmbeddingModel embeddingModel = factory.create();
            log.debug("Loaded the following embedding model through SPI: {}", (Object)embeddingModel);
            return embeddingModel;
        }
        return null;
    }

    public static IngestionResult ingest(Document document, EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreIngestor.builder().embeddingStore(embeddingStore).build().ingest(document);
    }

    public static IngestionResult ingest(List<Document> documents, EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreIngestor.builder().embeddingStore(embeddingStore).build().ingest(documents);
    }

    public IngestionResult ingest(Document document) {
        return this.ingest(Collections.singletonList(document));
    }

    public IngestionResult ingest(Document ... documents) {
        return this.ingest(Arrays.asList(documents));
    }

    public IngestionResult ingest(List<Document> documents) {
        List<TextSegment> segments;
        log.debug("Starting to ingest {} documents", (Object)documents.size());
        if (this.documentTransformer != null) {
            documents = this.documentTransformer.transformAll(documents);
            log.debug("Documents were transformed into {} documents", (Object)documents.size());
        }
        if (this.documentSplitter != null) {
            segments = this.documentSplitter.splitAll(documents);
            log.debug("Documents were split into {} text segments", (Object)segments.size());
        } else {
            segments = documents.stream().map(Document::toTextSegment).collect(Collectors.toList());
        }
        if (this.textSegmentTransformer != null) {
            segments = this.textSegmentTransformer.transformAll(segments);
            log.debug("{} documents were transformed into {} text segments", (Object)documents.size(), (Object)segments.size());
        }
        log.debug("Starting to embed {} text segments", (Object)segments.size());
        Response<List<Embedding>> embeddingsResponse = this.embedSegments(segments);
        log.debug("Finished embedding {} text segments", (Object)segments.size());
        log.debug("Starting to store {} text segments into the embedding store", (Object)segments.size());
        this.embeddingStore.addAll(embeddingsResponse.content(), segments);
        log.debug("Finished storing {} text segments into the embedding store", (Object)segments.size());
        return new IngestionResult(embeddingsResponse.tokenUsage());
    }

    private Response<List<Embedding>> embedSegments(List<TextSegment> segments) {
        if (this.embeddingInputType == null) {
            return this.embeddingModel.embedAll(segments);
        }
        EmbeddingResponse response = this.embeddingModel.embed(EmbeddingRequest.builder().textSegments(segments).inputType(this.embeddingInputType).build());
        return Response.from(response.embeddings(), response.metadata().tokenUsage());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DocumentTransformer documentTransformer;
        private DocumentSplitter documentSplitter;
        private TextSegmentTransformer textSegmentTransformer;
        private EmbeddingModel embeddingModel;
        private EmbeddingStore<TextSegment> embeddingStore;
        private EmbeddingInputType embeddingInputType;

        public Builder embeddingInputType(EmbeddingInputType embeddingInputType) {
            this.embeddingInputType = embeddingInputType;
            return this;
        }

        public Builder documentTransformer(DocumentTransformer documentTransformer) {
            this.documentTransformer = documentTransformer;
            return this;
        }

        public Builder documentSplitter(DocumentSplitter documentSplitter) {
            this.documentSplitter = documentSplitter;
            return this;
        }

        public Builder textSegmentTransformer(TextSegmentTransformer textSegmentTransformer) {
            this.textSegmentTransformer = textSegmentTransformer;
            return this;
        }

        public Builder embeddingModel(EmbeddingModel embeddingModel) {
            this.embeddingModel = embeddingModel;
            return this;
        }

        public Builder embeddingStore(EmbeddingStore<TextSegment> embeddingStore) {
            this.embeddingStore = embeddingStore;
            return this;
        }

        public EmbeddingStoreIngestor build() {
            return new EmbeddingStoreIngestor(this.documentTransformer, this.documentSplitter, this.textSegmentTransformer, this.embeddingModel, this.embeddingStore, this.embeddingInputType);
        }
    }
}

