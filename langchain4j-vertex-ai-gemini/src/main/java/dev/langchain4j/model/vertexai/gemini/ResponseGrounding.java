/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.GoogleSearchRetrieval
 *  com.google.cloud.vertexai.api.Retrieval
 *  com.google.cloud.vertexai.api.Tool
 *  com.google.cloud.vertexai.api.Tool$GoogleSearch
 *  com.google.cloud.vertexai.api.VertexAISearch
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.GoogleSearchRetrieval;
import com.google.cloud.vertexai.api.Retrieval;
import com.google.cloud.vertexai.api.Tool;
import com.google.cloud.vertexai.api.VertexAISearch;

class ResponseGrounding {
    ResponseGrounding() {
    }

    static Tool googleSearchTool(String modelName) {
        if (modelName.startsWith("gemini-1")) {
            return Tool.newBuilder().setGoogleSearchRetrieval(GoogleSearchRetrieval.newBuilder().build()).build();
        }
        return Tool.newBuilder().setGoogleSearch(Tool.GoogleSearch.newBuilder().build()).build();
    }

    static Tool vertexAiSearch(String datastore) {
        return Tool.newBuilder().setRetrieval(Retrieval.newBuilder().setVertexAiSearch(VertexAISearch.newBuilder().setDatastore(datastore)).setDisableAttribution(false)).build();
    }
}

