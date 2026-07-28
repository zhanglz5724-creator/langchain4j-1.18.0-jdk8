/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.store.embedding.oracle;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.EmbeddingTable;
import dev.langchain4j.store.embedding.oracle.Index;
import dev.langchain4j.store.embedding.oracle.IndexBuilder;

public class IVFIndexBuilder
extends IndexBuilder<IVFIndexBuilder> {
    private int targetAccuracy = -1;
    private int degreeOfParallelism = -1;
    private int neighborPartitions = -1;
    private int samplePerPartition = -1;
    private int minVectorsPerPartition = -1;

    IVFIndexBuilder() {
    }

    public IVFIndexBuilder targetAccuracy(int targetAccuracy) throws IllegalArgumentException {
        ValidationUtils.ensureBetween((Integer)targetAccuracy, (int)0, (int)100, (String)"targetAccuracy");
        this.targetAccuracy = targetAccuracy;
        return this;
    }

    public IVFIndexBuilder degreeOfParallelism(int degreeOfParallelism) {
        ValidationUtils.ensureGreaterThanZero((Integer)degreeOfParallelism, (String)"degreeOfParallelism");
        this.degreeOfParallelism = degreeOfParallelism;
        return this;
    }

    public IVFIndexBuilder neighborPartitions(int neighborPartitions) throws IllegalArgumentException {
        ValidationUtils.ensureBetween((Integer)neighborPartitions, (int)1, (int)10000000, (String)"neighborPartitions");
        this.neighborPartitions = neighborPartitions;
        return this;
    }

    public IVFIndexBuilder samplePerPartition(int samplePerPartition) throws IllegalArgumentException {
        ValidationUtils.ensureBetween((Integer)samplePerPartition, (int)1, (int)Integer.MAX_VALUE, (String)"samplePerPartition");
        this.samplePerPartition = samplePerPartition;
        return this;
    }

    public IVFIndexBuilder minVectorsPerPartition(int minVectorsPerPartition) throws IllegalArgumentException {
        ValidationUtils.ensureGreaterThanZero((Integer)minVectorsPerPartition, (String)"minVectorsPerPartition");
        this.minVectorsPerPartition = minVectorsPerPartition;
        return this;
    }

    @Override
    public Index build() {
        return new Index(this);
    }

    @Override
    String getCreateIndexStatement(EmbeddingTable embeddingTable) {
        return "CREATE VECTOR INDEX " + (this.createOption == CreateOption.CREATE_IF_NOT_EXISTS ? "IF NOT EXISTS " : "") + this.getIndexName(embeddingTable) + " ON " + embeddingTable.name() + "( " + embeddingTable.embeddingColumn() + " )  ORGANIZATION NEIGHBOR PARTITIONS  WITH DISTANCE COSINE " + (String)(this.targetAccuracy > 0 ? " WITH TARGET ACCURACY " + this.targetAccuracy + " " : "") + (String)(this.degreeOfParallelism >= 0 ? " PARALLEL " + this.degreeOfParallelism : "") + this.getIndexParameters();
    }

    @Override
    String getIndexName(EmbeddingTable embeddingTable) {
        if (this.indexName == null) {
            this.indexName = this.buildIndexName(embeddingTable.name(), "_VECTOR_INDEX");
        }
        return this.indexName;
    }

    String getIndexParameters() {
        if (this.neighborPartitions == -1 && this.samplePerPartition == -1 && this.minVectorsPerPartition == -1) {
            return " ";
        }
        return "PARAMETERS ( TYPE IVF" + (String)(this.neighborPartitions != -1 ? ", NEIGHBOR PARTITIONS " + this.neighborPartitions + " " : "") + (String)(this.samplePerPartition != -1 ? ", SAMPLES_PER_PARTITION " + this.samplePerPartition + " " : "") + (String)(this.minVectorsPerPartition != -1 ? ", MIN_VECTORS_PER_PARTITION " + this.minVectorsPerPartition + " " : "") + ")";
    }
}

