/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.foundationmodel.FoundationModel$Function
 *  com.ibm.watsonx.ai.foundationmodel.FoundationModelParameters
 *  com.ibm.watsonx.ai.foundationmodel.FoundationModelService
 *  com.ibm.watsonx.ai.foundationmodel.FoundationModelService$Builder
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.catalog.ModelCatalog
 *  dev.langchain4j.model.catalog.ModelDescription
 *  dev.langchain4j.model.catalog.ModelDescription$Builder
 *  dev.langchain4j.model.catalog.ModelType
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.foundationmodel.FoundationModel;
import com.ibm.watsonx.ai.foundationmodel.FoundationModelParameters;
import com.ibm.watsonx.ai.foundationmodel.FoundationModelService;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.catalog.ModelCatalog;
import dev.langchain4j.model.catalog.ModelDescription;
import dev.langchain4j.model.catalog.ModelType;
import dev.langchain4j.model.watsonx.WatsonxBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

public class WatsonxModelCatalog
implements ModelCatalog {
    private final FoundationModelService foundationModelService;
    private final FoundationModelParameters parameters;

    public WatsonxModelCatalog(Builder builder) {
        this.foundationModelService = ((FoundationModelService.Builder)((FoundationModelService.Builder)((FoundationModelService.Builder)((FoundationModelService.Builder)((FoundationModelService.Builder)((FoundationModelService.Builder)((FoundationModelService.Builder)FoundationModelService.builder().baseUrl(builder.baseUrl)).version(builder.version)).timeout(builder.timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
        this.parameters = FoundationModelParameters.builder().limit(Integer.valueOf(200)).techPreview(Boolean.valueOf(true)).build();
    }

    public List<ModelDescription> listModels() {
        return this.foundationModelService.getModels(this.parameters).resources().stream().map(model -> {
            ModelDescription.Builder builder = ModelDescription.builder().description(model.longDescription()).displayName(model.label()).name(model.modelId()).owner(model.provider()).provider(ModelProvider.WATSONX).type(this.resolveModelType(model.functions()));
            if (Objects.nonNull(model.lifecycle())) {
                Instant createdAt = model.lifecycle().stream().filter(l -> l.id().equals("available")).findFirst().map(l -> LocalDate.parse(l.startDate()).atStartOfDay().toInstant(ZoneOffset.UTC)).orElse(null);
                builder.createdAt(createdAt);
            }
            if (Objects.nonNull(model.modelLimits())) {
                builder.maxInputTokens(model.modelLimits().maxSequenceLength()).maxOutputTokens(model.modelLimits().maxOutputTokens());
            }
            return builder.build();
        }).toList();
    }

    public ModelProvider provider() {
        return ModelProvider.WATSONX;
    }

    public static Builder builder() {
        return new Builder();
    }

    private ModelType resolveModelType(List<FoundationModel.Function> functions) {
        for (FoundationModel.Function function : functions) {
            switch (function.id()) {
                case "text_chat": 
                case "image_chat": {
                    return ModelType.CHAT;
                }
                case "embedding": {
                    return ModelType.EMBEDDING;
                }
                case "rerank": {
                    return ModelType.SCORING;
                }
            }
        }
        return ModelType.OTHER;
    }

    public static class Builder
    extends WatsonxBuilder<Builder> {
        private Builder() {
        }

        public WatsonxModelCatalog build() {
            return new WatsonxModelCatalog(this);
        }
    }
}

