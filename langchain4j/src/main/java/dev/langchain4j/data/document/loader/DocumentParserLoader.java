/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.document.DocumentParser
 *  dev.langchain4j.spi.ServiceHelper
 *  dev.langchain4j.spi.data.document.parser.DocumentParserFactory
 */
package dev.langchain4j.data.document.loader;

import dev.langchain4j.Internal;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.data.document.parser.DocumentParserFactory;
import java.util.Collection;
import java.util.Iterator;

@Internal
class DocumentParserLoader {
    DocumentParserLoader() {
    }

    static DocumentParser loadDocumentParser() {
        Collection factories = ServiceHelper.loadFactories(DocumentParserFactory.class);
        if (factories.size() > 1) {
            throw new RuntimeException("Conflict: multiple document parsers have been found in the classpath. Please explicitly specify the one you wish to use.");
        }
        Iterator iterator = factories.iterator();
        if (iterator.hasNext()) {
            DocumentParserFactory factory = (DocumentParserFactory)iterator.next();
            return factory.create();
        }
        return null;
    }
}

