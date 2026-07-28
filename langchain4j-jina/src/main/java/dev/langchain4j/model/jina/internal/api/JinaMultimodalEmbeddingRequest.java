/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.jina.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JinaMultimodalEmbeddingRequest {
    public String model;
    public List<JinaMultimodalInput> input;

    public JinaMultimodalEmbeddingRequest(String model, List<JinaMultimodalInput> input) {
        this.model = model;
        this.input = input;
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    public static class JinaMultimodalInput {
        public String text;
        public String image;

        private JinaMultimodalInput(String text, String image) {
            this.text = text;
            this.image = image;
        }

        public static JinaMultimodalInput text(String text) {
            return new JinaMultimodalInput(text, null);
        }

        public static JinaMultimodalInput image(String image) {
            return new JinaMultimodalInput(null, image);
        }
    }
}

