/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
 *  ai.djl.util.PairList
 *  ai.onnxruntime.OrtEnvironment
 *  ai.onnxruntime.OrtException
 *  ai.onnxruntime.OrtSession
 *  ai.onnxruntime.OrtSession$Result
 *  ai.onnxruntime.OrtSession$SessionOptions
 */
package dev.langchain4j.model.scoring.onnx;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.util.PairList;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class OnnxScoringBertCrossEncoder
implements AutoCloseable {
    private final OrtEnvironment environment;
    private final OrtSession session;
    private final Set<String> expectedInputs;
    private final HuggingFaceTokenizer tokenizer;
    private final boolean normalize;
    private boolean closed;

    public OnnxScoringBertCrossEncoder(String modelPath, OrtSession.SessionOptions options, String pathToTokenizer, final int modelMaxLength, boolean normalize) {
        try (OrtSession.SessionOptions localOptions = options;){
            this.environment = OrtEnvironment.getEnvironment();
            this.session = this.environment.createSession(modelPath, options);
            this.expectedInputs = this.session.getInputNames();
            HashMap<String, String> tokenizerOptions = new HashMap<String, String>(){
                {
                    this.put("padding", "true");
                    this.put("truncation", "LONGEST_FIRST");
                    this.put("modelMaxLength", String.valueOf(modelMaxLength - 2));
                }
            };
            this.normalize = normalize;
            this.tokenizer = HuggingFaceTokenizer.newInstance((Path)Paths.get(pathToTokenizer, new String[0]), (Map)tokenizerOptions);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        Exception firstException = null;
        try {
            if (this.session != null) {
                this.session.close();
            }
        }
        catch (Exception e) {
            firstException = e;
        }
        try {
            if (this.tokenizer != null) {
                this.tokenizer.close();
            }
        }
        catch (Exception e) {
            if (firstException != null) {
                firstException.addSuppressed(e);
            }
            firstException = e;
        }
        this.closed = true;
        if (firstException != null) {
            throw new RuntimeException(firstException);
        }
    }

    ScoringAndTokenCount scoreAll(String query, List<String> documents) {
        List<Double> scores;
        int tokenCount = 0;
        int queryTokenCount = this.tokenizer.tokenize(query).size() - 2;
        PairList pairs = new PairList();
        for (String document : documents) {
            pairs.add((Object)query, (Object)document);
            tokenCount += queryTokenCount + this.tokenizer.tokenize(document).size() - 2;
        }
        try (OrtSession.Result result = this.encode((PairList<String, String>)pairs);){
            scores = this.toScore(result);
        }
        catch (OrtException e) {
            throw new RuntimeException(e);
        }
        return new ScoringAndTokenCount(scores, tokenCount);
    }

    /*
     * Exception decompiling
     */
    private OrtSession.Result encode(PairList<String, String> pairs) throws OrtException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 4 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private List<Double> toScore(OrtSession.Result result) throws OrtException {
        float[][] output = (float[][])result.get(0).getValue();
        ArrayList<Double> scores = new ArrayList<Double>();
        for (float[] floats : output) {
            if (this.normalize) {
                scores.add(this.sigmoid(floats[0]));
                continue;
            }
            scores.add(Double.valueOf(floats[0]));
        }
        return scores;
    }

    private double sigmoid(float x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    static class ScoringAndTokenCount {
        List<Double> scores;
        int tokenCount;

        ScoringAndTokenCount(List<Double> scores, int tokenCount) {
            this.scores = scores;
            this.tokenCount = tokenCount;
        }
    }
}

