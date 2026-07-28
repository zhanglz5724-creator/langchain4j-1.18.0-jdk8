/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.StringCollectionOutputParser;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Internal
class StringListOutputParser
extends StringCollectionOutputParser<List<String>> {
    StringListOutputParser() {
    }

    @Override
    Supplier<List<String>> emptyCollectionSupplier() {
        return ArrayList::new;
    }

    @Override
    Class<?> collectionType() {
        return List.class;
    }
}

