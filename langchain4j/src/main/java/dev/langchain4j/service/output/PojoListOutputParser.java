/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.PojoCollectionOutputParser;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Internal
class PojoListOutputParser<T>
extends PojoCollectionOutputParser<T, List<T>> {
    PojoListOutputParser(Class<T> type) {
        super(type);
    }

    @Override
    Supplier<List<T>> emptyCollectionSupplier() {
        return ArrayList::new;
    }

    @Override
    Class<?> collectionType() {
        return List.class;
    }
}

