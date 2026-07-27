/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Marker
 *  org.slf4j.MarkerFactory
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Internal
public class Markers {
    public static final Marker SENSITIVE = MarkerFactory.getMarker((String)"SENSITIVE");

    private Markers() {
    }
}

