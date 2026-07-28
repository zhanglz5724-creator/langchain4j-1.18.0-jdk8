/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.voyageai;

import java.util.List;

class RerankRequest {
    private String query;
    private List<String> documents;
    private String model;
    private Integer topK;
    private Boolean returnDocuments;
    private Boolean truncation;

    RerankRequest() {
    }

    RerankRequest(String query, List<String> documents, String model, Integer topK, Boolean returnDocuments, Boolean truncation) {
        this.query = query;
        this.documents = documents;
        this.model = model;
        this.topK = topK;
        this.returnDocuments = returnDocuments;
        this.truncation = truncation;
    }

    public String getQuery() {
        return this.query;
    }

    public List<String> getDocuments() {
        return this.documents;
    }

    public String getModel() {
        return this.model;
    }

    public Integer getTopK() {
        return this.topK;
    }

    public Boolean getReturnDocuments() {
        return this.returnDocuments;
    }

    public Boolean getTruncation() {
        return this.truncation;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String query;
        private List<String> documents;
        private String model;
        private Integer topK;
        private Boolean returnDocuments;
        private Boolean truncation;

        Builder() {
        }

        Builder query(String query) {
            this.query = query;
            return this;
        }

        Builder documents(List<String> documents) {
            this.documents = documents;
            return this;
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        Builder returnDocuments(Boolean returnDocuments) {
            this.returnDocuments = returnDocuments;
            return this;
        }

        Builder truncation(Boolean truncation) {
            this.truncation = truncation;
            return this;
        }

        RerankRequest build() {
            return new RerankRequest(this.query, this.documents, this.model, this.topK, this.returnDocuments, this.truncation);
        }
    }
}

