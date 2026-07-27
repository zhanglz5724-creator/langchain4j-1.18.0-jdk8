/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.catalog;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.catalog.ModelDescription;
import java.util.List;

@Experimental
public interface ModelCatalog {
    public List<ModelDescription> listModels();

    public ModelProvider provider();
}

