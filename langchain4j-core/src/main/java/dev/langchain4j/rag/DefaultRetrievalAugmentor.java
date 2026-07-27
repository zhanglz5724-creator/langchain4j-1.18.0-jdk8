package dev.langchain4j.rag;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.DefaultQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultRetrievalAugmentor implements RetrievalAugmentor {
    private final QueryTransformer queryTransformer;
    private final QueryRouter queryRouter;
    private final ContentAggregator contentAggregator;
    private final ContentInjector contentInjector;
    private final Executor executor;

    public DefaultRetrievalAugmentor(QueryTransformer queryTransformer, QueryRouter queryRouter,
            ContentAggregator contentAggregator, ContentInjector contentInjector, Executor executor) {
        this.queryTransformer = (QueryTransformer) Utils.getOrDefault(queryTransformer, DefaultQueryTransformer::new);
        this.queryRouter = (QueryRouter) ValidationUtils.ensureNotNull(queryRouter, "queryRouter");
        this.contentAggregator = (ContentAggregator) Utils.getOrDefault(contentAggregator,
                DefaultContentAggregator::new);
        this.contentInjector = (ContentInjector) Utils.getOrDefault(contentInjector, DefaultContentInjector::new);
        this.executor = (Executor) Utils.getOrDefault(executor, DefaultRetrievalAugmentor::createDefaultExecutor);
    }

    private static ExecutorService createDefaultExecutor() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    @Override
    public AugmentationResult augment(AugmentationRequest augmentationRequest) {
        ChatMessage chatMessage = augmentationRequest.chatMessage();
        if (!(chatMessage instanceof UserMessage)) {
            throw new IllegalArgumentException("Unsupported message type: " + chatMessage.type());
        }
        UserMessage userMessage = (UserMessage) chatMessage;
        String queryText = userMessage.singleText();
        Query originalQuery = Query.from(queryText, augmentationRequest.metadata());
        Collection<Query> queries = (Collection<Query>) this.queryTransformer.transform(originalQuery);
        Map<Query, Collection<List<Content>>> queryToContents = this.process(queries);
        List<Content> contents = (List<Content>) this.contentAggregator.aggregate(queryToContents);
        ChatMessage augmentedChatMessage = this.contentInjector.inject(contents, chatMessage);
        return AugmentationResult.builder().chatMessage(augmentedChatMessage).contents(contents).build();
    }

    private Map<Query, Collection<List<Content>>> process(Collection<Query> queries) {
        if (queries.size() == 1) {
            Query query2 = queries.iterator().next();
            Collection<ContentRetriever> retrievers = (Collection<ContentRetriever>) this.queryRouter.route(query2);
            if (retrievers.size() == 1) {
                ContentRetriever contentRetriever = (ContentRetriever) retrievers.iterator().next();
                List<Content> contents = (List<Content>) contentRetriever.retrieve(query2);
                return Collections.singletonMap(query2, Collections.singletonList(contents));
            }
            if (retrievers.size() > 1) {
                Collection<List<Content>> contents = this.retrieveFromAll(retrievers, query2).join();
                return Collections.singletonMap(query2, contents);
            }
            return Collections.emptyMap();
        }
        if (queries.size() > 1) {
            ConcurrentHashMap<Query, CompletableFuture<Collection<List<Content>>>> queryToFutureContents =
                    new ConcurrentHashMap<Query, CompletableFuture<Collection<List<Content>>>>();
            queries.forEach(query -> {
                CompletionStage futureContents = CompletableFuture
                        .supplyAsync(() -> this.queryRouter.route(query), this.executor)
                        .thenCompose(retrievers -> this.retrieveFromAll(
                                (Collection<ContentRetriever>) retrievers, query));
                queryToFutureContents.put(query,
                        (CompletableFuture<Collection<List<Content>>>) futureContents);
            });
            return DefaultRetrievalAugmentor.join(queryToFutureContents);
        }
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    private CompletableFuture<Collection<List<Content>>> retrieveFromAll(Collection<ContentRetriever> retrievers,
            Query query) {
        List<CompletableFuture<List<Content>>> futureContents = retrievers.stream()
                .map(retriever -> CompletableFuture.supplyAsync(() -> retriever.retrieve(query), this.executor))
                .collect(Collectors.toList());
        return CompletableFuture.allOf(futureContents.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> futureContents.stream().map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map<Query, Collection<List<Content>>> join(
            Map<Query, CompletableFuture<Collection<List<Content>>>> queryToFutureContents) {
        return (Map) ((CompletableFuture) CompletableFuture
                .allOf(queryToFutureContents.values().toArray(new CompletableFuture[0]))
                .thenApply(ignored -> queryToFutureContents.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                entry -> (Collection) ((CompletableFuture) entry.getValue()).join()))))
                .join();
    }

    public static DefaultRetrievalAugmentorBuilder builder() {
        return new DefaultRetrievalAugmentorBuilder();
    }

    public static class DefaultRetrievalAugmentorBuilder {
        private QueryTransformer queryTransformer;
        private QueryRouter queryRouter;
        private ContentAggregator contentAggregator;
        private ContentInjector contentInjector;
        private Executor executor;

        DefaultRetrievalAugmentorBuilder() {
        }

        public DefaultRetrievalAugmentorBuilder contentRetriever(ContentRetriever contentRetriever) {
            this.queryRouter = new DefaultQueryRouter(ValidationUtils.ensureNotNull(contentRetriever, "contentRetriever"));
            return this;
        }

        public DefaultRetrievalAugmentorBuilder queryTransformer(QueryTransformer queryTransformer) {
            this.queryTransformer = queryTransformer;
            return this;
        }

        public DefaultRetrievalAugmentorBuilder queryRouter(QueryRouter queryRouter) {
            this.queryRouter = queryRouter;
            return this;
        }

        public DefaultRetrievalAugmentorBuilder contentAggregator(ContentAggregator contentAggregator) {
            this.contentAggregator = contentAggregator;
            return this;
        }

        public DefaultRetrievalAugmentorBuilder contentInjector(ContentInjector contentInjector) {
            this.contentInjector = contentInjector;
            return this;
        }

        public DefaultRetrievalAugmentorBuilder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public DefaultRetrievalAugmentor build() {
            return new DefaultRetrievalAugmentor(this.queryTransformer, this.queryRouter, this.contentAggregator, this.contentInjector, this.executor);
        }
    }
}

