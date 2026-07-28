/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ai.onnxruntime.OrtSession$SessionOptions
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.model.scoring.ScoringModel
 */
package dev.langchain4j.model.scoring.onnx;

import ai.onnxruntime.OrtSession;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.model.scoring.onnx.OnnxScoringBertCrossEncoder;
import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractInProcessScoringModel
implements ScoringModel,
AutoCloseable {
    static OnnxScoringBertCrossEncoder loadFromFileSystem(String pathToModel, OrtSession.SessionOptions options, String pathToTokenizer, int modelMaxLength, boolean normalize) {
        try {
            return new OnnxScoringBertCrossEncoder(pathToModel, options, pathToTokenizer, modelMaxLength, normalize);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract OnnxScoringBertCrossEncoder model();

    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        OnnxScoringBertCrossEncoder.ScoringAndTokenCount scoresAndTokenCount = this.model().scoreAll(query, segments.stream().map(TextSegment::text).collect(Collectors.toList()));
        return Response.from(scoresAndTokenCount.scores, (TokenUsage)new TokenUsage(Integer.valueOf(scoresAndTokenCount.tokenCount)));
    }

    @Override
    public void close() {
        OnnxScoringBertCrossEncoder m = this.model();
        if (m != null) {
            m.close();
        }
    }
}

