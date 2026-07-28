/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.StringCollectionOutputParser;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

@Internal
class StringSetOutputParser
extends StringCollectionOutputParser<Set<String>> {
    StringSetOutputParser() {
    }

    @Override
    Supplier<Set<String>> emptyCollectionSupplier() {
        return LinkedHashSet::new;
    }

    @Override
    Class<?> collectionType() {
        return Set.class;
    }
}

