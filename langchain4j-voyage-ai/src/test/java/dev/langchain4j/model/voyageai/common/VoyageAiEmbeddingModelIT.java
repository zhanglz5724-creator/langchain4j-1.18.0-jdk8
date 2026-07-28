package dev.langchain4j.model.voyageai.common;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.common.AbstractEmbeddingModelIT;
import dev.langchain4j.model.embedding.listener.EmbeddingModelListener;
import dev.langchain4j.model.voyageai.VoyageAiEmbeddingModel;
import java.util.List;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.util.Arrays;

@EnabledIfEnvironmentVariable(named = "VOYAGE_API_KEY", matches = ".+")
class VoyageAiEmbeddingModelIT extends AbstractEmbeddingModelIT {

    private static final String API_KEY = System.getenv("VOYAGE_API_KEY");
    // multimodal is auto-detected from the model name
    private static final String MODEL_NAME = "voyage-multimodal-3.5";

    @Override
    protected List<EmbeddingModel> models() {
        return Arrays.asList(VoyageAiEmbeddingModel.builder()
                .apiKey(API_KEY)
                .modelName(MODEL_NAME)
                .build());
    }

    @Override
    protected EmbeddingModel modelWith(EmbeddingModelListener listener) {
        return VoyageAiEmbeddingModel.builder()
                .apiKey(API_KEY)
                .modelName(MODEL_NAME)
                .listeners(Arrays.asList(listener))
                .build();
    }

    @Override
    protected EmbeddingModel failingModelWith(EmbeddingModelListener listener) {
        return VoyageAiEmbeddingModel.builder()
                .apiKey("banana")
                .modelName(MODEL_NAME)
                .maxRetries(0)
                .listeners(Arrays.asList(listener))
                .build();
    }

    // voyage-multimodal-3.5 is multimodal (text + image, fused) and maps input_type to query/document, but does
    // not support a per-call dimensions parameter.
    @Override
    protected boolean supportsDimensionsParameter() {
        return false;
    }
}
