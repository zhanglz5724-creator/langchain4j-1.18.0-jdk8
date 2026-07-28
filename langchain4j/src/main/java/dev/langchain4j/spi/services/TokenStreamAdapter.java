/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.spi.services;

import dev.langchain4j.Internal;
import dev.langchain4j.service.TokenStream;
import java.lang.reflect.Type;

@Internal
public interface TokenStreamAdapter {
    public boolean canAdaptTokenStreamTo(Type var1);

    public Object adapt(TokenStream var1);
}

