/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.workersai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.workersai.client.AbstractWorkersAIModel;
import dev.langchain4j.model.workersai.client.WorkersAiEmbeddingRequest;
import dev.langchain4j.model.workersai.client.WorkersAiEmbeddingResponse;
import dev.langchain4j.model.workersai.spi.WorkersAiEmbeddingModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkersAiEmbeddingModel
extends AbstractWorkersAIModel
implements EmbeddingModel {
    private static final Logger log = LoggerFactory.getLogger(WorkersAiEmbeddingModel.class);

    public WorkersAiEmbeddingModel(Builder builder) {
        super(builder.accountId, builder.modelName, builder.apiToken, builder.httpClientBuilder);
    }

    public WorkersAiEmbeddingModel(String accountId, String modelName, String apiToken) {
        super(accountId, modelName, apiToken);
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(WorkersAiEmbeddingModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            WorkersAiEmbeddingModelBuilderFactory factory = (WorkersAiEmbeddingModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public Response<Embedding> embed(String text) {
        WorkersAiEmbeddingRequest req = new WorkersAiEmbeddingRequest();
        req.getText().add(text);
        WorkersAiEmbeddingResponse response = this.client.embed(req, this.accountId, this.modelName);
        if (response == null || response.getResult() == null) {
            throw new RuntimeException("Unexpected response: " + response);
        }
        WorkersAiEmbeddingResponse.EmbeddingResult res = (WorkersAiEmbeddingResponse.EmbeddingResult)response.getResult();
        if (res.getShape().get(0) != 1) {
            throw new RuntimeException("Unexpected shape: " + res.getShape());
        }
        List<Float> embeddings = res.getData().get(0);
        float[] floatArray = new float[embeddings.size()];
        for (int i = 0; i < embeddings.size(); ++i) {
            floatArray[i] = embeddings.get(i).floatValue();
        }
        return new Response((Object)new Embedding(floatArray), null, FinishReason.STOP);
    }

    public Response<Embedding> embed(TextSegment textSegment) {
        return this.embed(textSegment.text());
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        ArrayList<Future<List>> futures = new ArrayList<Future<List>>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            int chunkSize = 100;
            for (int i = 0; i < textSegments.size(); i += 100) {
                List<TextSegment> chunk = textSegments.subList(i, Math.min(textSegments.size(), i + 100));
                Future<List> future = executor.submit(() -> this.processChunk(chunk, this.accountId, this.modelName));
                futures.add(future);
            }
            ArrayList result = new ArrayList();
            for (Future<List> future : futures) {
                result.addAll(future.get());
            }
            Response response = new Response(result);
            return response;
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800L, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            }
            catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }

    public String modelName() {
        return this.modelName;
    }

    private List<Embedding> processChunk(List<TextSegment> chunk, String accountIdentifier, String modelName) {
        WorkersAiEmbeddingRequest req = new WorkersAiEmbeddingRequest();
        for (TextSegment textSegment : chunk) {
            req.getText().add(textSegment.text());
        }
        WorkersAiEmbeddingResponse response = this.client.embed(req, accountIdentifier, modelName);
        if (response == null || response.getResult() == null) {
            throw new RuntimeException("Unexpected response: " + response);
        }
        WorkersAiEmbeddingResponse.EmbeddingResult res = (WorkersAiEmbeddingResponse.EmbeddingResult)response.getResult();
        List<List<Float>> embeddings = res.getData();
        ArrayList<Embedding> embeddingsList = new ArrayList<Embedding>();
        for (List<Float> embedding : embeddings) {
            float[] floatArray = new float[embedding.size()];
            for (int i = 0; i < embedding.size(); ++i) {
                floatArray[i] = embedding.get(i).floatValue();
            }
            embeddingsList.add(new Embedding(floatArray));
        }
        return embeddingsList;
    }

    public static class Builder {
        public String accountId;
        public String apiToken;
        public String modelName;
        public HttpClientBuilder httpClientBuilder;

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public WorkersAiEmbeddingModel build() {
            return new WorkersAiEmbeddingModel(this);
        }
    }
}

