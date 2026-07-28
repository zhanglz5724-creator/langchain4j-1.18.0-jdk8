/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.milvus;

class FieldDefinition {
    String idFieldName;
    String textFieldName;
    String metadataFieldName;
    String vectorFieldName;

    public FieldDefinition(String idFieldName, String textFieldName, String metadataFieldName, String vectorFieldName) {
        this.idFieldName = idFieldName;
        this.textFieldName = textFieldName;
        this.metadataFieldName = metadataFieldName;
        this.vectorFieldName = vectorFieldName;
    }

    public String getIdFieldName() {
        return this.idFieldName;
    }

    public String getTextFieldName() {
        return this.textFieldName;
    }

    public String getMetadataFieldName() {
        return this.metadataFieldName;
    }

    public String getVectorFieldName() {
        return this.vectorFieldName;
    }
}

