/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MistralAiAudioBase64Content
extends MistralAiMessageContent {
    public String inputAudio;

    public MistralAiAudioBase64Content(String inputAudio, String mimeType) {
        super("input_audio");
        this.inputAudio = "data:" + mimeType + ";base64," + inputAudio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MistralAiAudioBase64Content that = (MistralAiAudioBase64Content)o;
        return Objects.equals(this.inputAudio, that.inputAudio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.inputAudio);
    }

    public String toString() {
        return "MistralAiAudioBase64Content{inputAudio=" + this.inputAudio + ", type=" + Utils.quoted((Object)this.type) + '}';
    }
}

