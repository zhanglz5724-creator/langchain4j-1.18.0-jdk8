/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai;

import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;

class VertexAiEmbeddingInstance {
    private String content;
    private String title;
    private VertexAiEmbeddingModel.TaskType task_type;

    VertexAiEmbeddingInstance(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskType(VertexAiEmbeddingModel.TaskType taskType) {
        this.task_type = taskType;
    }
}

