/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ai.onnxruntime.OrtSession$SessionOptions
 */
package dev.langchain4j.model.scoring.onnx;

import ai.onnxruntime.OrtSession;
import dev.langchain4j.model.scoring.onnx.AbstractInProcessScoringModel;
import dev.langchain4j.model.scoring.onnx.OnnxScoringBertCrossEncoder;

public class OnnxScoringModel
extends AbstractInProcessScoringModel {
    private static final int DEFAULT_MODEL_MAX_LENGTH = 510;
    private static final boolean DEFAULT_NORMALIZE = false;
    private final OnnxScoringBertCrossEncoder onnxBertBiEncoder;

    public OnnxScoringModel(String pathToModel, String pathToTokenizer) {
        this.onnxBertBiEncoder = OnnxScoringModel.loadFromFileSystem(pathToModel, OnnxScoringModel.newDefaultSessionOptions(), pathToTokenizer, 510, false);
    }

    public OnnxScoringModel(String pathToModel, OrtSession.SessionOptions options, String pathToTokenizer) {
        this.onnxBertBiEncoder = OnnxScoringModel.loadFromFileSystem(pathToModel, options, pathToTokenizer, 510, false);
    }

    public OnnxScoringModel(String pathToModel, String pathToTokenizer, int modelMaxLength) {
        this.onnxBertBiEncoder = OnnxScoringModel.loadFromFileSystem(pathToModel, OnnxScoringModel.newDefaultSessionOptions(), pathToTokenizer, modelMaxLength, false);
    }

    public OnnxScoringModel(String pathToModel, OrtSession.SessionOptions options, String pathToTokenizer, int modelMaxLength, boolean normalize) {
        this.onnxBertBiEncoder = OnnxScoringModel.loadFromFileSystem(pathToModel, options, pathToTokenizer, modelMaxLength, normalize);
    }

    @Override
    protected OnnxScoringBertCrossEncoder model() {
        return this.onnxBertBiEncoder;
    }

    private static OrtSession.SessionOptions newDefaultSessionOptions() {
        try {
            return new OrtSession.SessionOptions();
        }
        catch (ExceptionInInitializerError | NoClassDefFoundError | UnsatisfiedLinkError e) {
            throw OnnxScoringModel.wrapNativeLibraryLoadFailure(e);
        }
    }

    static RuntimeException wrapNativeLibraryLoadFailure(Throwable cause) {
        return new RuntimeException("Failed to initialize ONNX Runtime native library. On Windows, install the latest Microsoft Visual C++ Redistributable for Visual Studio 2015-2022 (see https://learn.microsoft.com/cpp/windows/latest-supported-vc-redist). Also ensure your JVM architecture (x64/ARM64) matches the ONNX Runtime native binary, and that no security software is blocking DLL loading from the temp directory. See https://onnxruntime.ai/docs/install/ for the full list of requirements.", cause);
    }
}

