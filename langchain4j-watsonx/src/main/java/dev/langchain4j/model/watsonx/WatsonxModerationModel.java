import java.util.Arrays;

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.detection.DetectionService
 *  com.ibm.watsonx.ai.detection.DetectionService$Builder
 *  com.ibm.watsonx.ai.detection.DetectionTextRequest
 *  com.ibm.watsonx.ai.detection.DetectionTextResponse
 *  com.ibm.watsonx.ai.detection.detector.BaseDetector
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.moderation.Moderation
 *  dev.langchain4j.model.moderation.ModerationModel
 *  dev.langchain4j.model.moderation.ModerationRequest
 *  dev.langchain4j.model.moderation.ModerationResponse
 *  dev.langchain4j.model.moderation.listener.ModerationModelListener
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.detection.DetectionService;
import com.ibm.watsonx.ai.detection.DetectionTextRequest;
import com.ibm.watsonx.ai.detection.DetectionTextResponse;
import com.ibm.watsonx.ai.detection.detector.BaseDetector;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.moderation.ModerationRequest;
import dev.langchain4j.model.moderation.ModerationResponse;
import dev.langchain4j.model.moderation.listener.ModerationModelListener;
import dev.langchain4j.model.watsonx.WatsonxBuilder;
import dev.langchain4j.model.watsonx.WatsonxExceptionMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WatsonxModerationModel
implements ModerationModel {
    private final List<BaseDetector> detectors;
    private final DetectionService detectionService;
    private final List<ModerationModelListener> listeners;

    public WatsonxModerationModel(Builder builder) {
        if (Objects.isNull(builder.detectors) || builder.detectors.isEmpty()) {
            throw new IllegalArgumentException("At least one detector must be provided");
        }
        this.detectors = builder.detectors;
        DetectionService.Builder detectionServiceBuilder = Objects.nonNull(builder.authenticator) ? (DetectionService.Builder)DetectionService.builder().authenticator(builder.authenticator) : (DetectionService.Builder)DetectionService.builder().apiKey(builder.apiKey);
        this.detectionService = ((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)((DetectionService.Builder)detectionServiceBuilder.baseUrl(builder.baseUrl)).version(builder.version)).projectId(builder.projectId)).spaceId(builder.spaceId)).timeout(builder.timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
        this.listeners = Utils.copy(builder.listeners);
    }

    public List<ModerationModelListener> listeners() {
        return this.listeners;
    }

    public ModelProvider provider() {
        return ModelProvider.WATSONX;
    }

    public ModerationResponse doModerate(ModerationRequest moderationRequest) {
        if (!this.modelName().equals(moderationRequest.modelName())) {
            throw new UnsupportedFeatureException("can't change model name dynamically");
        }
        List futures = moderationRequest.texts().stream().map(input -> CompletableFuture.supplyAsync(() -> this.moderateSingleInput((String)input), DefaultExecutorProvider.getDefaultExecutorService())).toList();
        try {
            return futures.stream().map(CompletableFuture::join).filter(response -> response.moderation().flagged()).findFirst().orElse(ModerationResponse.builder().moderation(Moderation.notFlagged()).build());
        }
        catch (CompletionException e) {
            RuntimeException runtimeException;
            Throwable cause = e.getCause();
            if (cause instanceof LangChain4jException) {
                LangChain4jException langchainException = (LangChain4jException)cause;
                runtimeException = langchainException;
            } else {
                runtimeException = new RuntimeException(cause);
            }
            throw runtimeException;
        }
    }

    private ModerationResponse moderateSingleInput(String input) {
        DetectionTextRequest request = DetectionTextRequest.builder().input(input).detectors(this.detectors).build();
        return (ModerationResponse)WatsonxExceptionMapper.INSTANCE.withExceptionMapper(() -> this.detectionService.detect(request).detections().stream().findFirst().map(this::createModerationResponse).orElse(ModerationResponse.builder().moderation(Moderation.notFlagged()).build()));
    }

    public static Builder builder() {
        return new Builder();
    }

    private ModerationResponse createModerationResponse(DetectionTextResponse detectionTextResponse) {
        Moderation moderation = Moderation.flagged((String)detectionTextResponse.text());
        Map metadata = Map.of((Object)"detection", (Object)detectionTextResponse.detection(), (Object)"detection_type", (Object)detectionTextResponse.detectionType(), (Object)"start", (Object)detectionTextResponse.start(), (Object)"end", (Object)detectionTextResponse.end(), (Object)"score", (Object)detectionTextResponse.score());
        return ModerationResponse.builder().moderation(moderation).metadata(metadata).build();
    }

    public static class Builder
    extends WatsonxBuilder<Builder> {
        private List<BaseDetector> detectors;
        private List<ModerationModelListener> listeners;

        private Builder() {
        }

        public Builder detectors(List<BaseDetector> detectors) {
            this.detectors = detectors;
            return this;
        }

        public Builder detectors(BaseDetector ... detectors) {
            return this.detectors(Arrays.asList((Object[])detectors));
        }

        public Builder listeners(List<ModerationModelListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public WatsonxModerationModel build() {
            return new WatsonxModerationModel(this);
        }
    }
}

