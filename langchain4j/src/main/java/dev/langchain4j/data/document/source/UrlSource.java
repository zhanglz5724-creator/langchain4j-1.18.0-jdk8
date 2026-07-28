/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.DocumentSource
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.data.document.source;

import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.ValidationUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class UrlSource
implements DocumentSource {
    private final URL url;

    public UrlSource(URL url) {
        this.url = (URL)ValidationUtils.ensureNotNull((Object)url, (String)"url");
    }

    public InputStream inputStream() throws IOException {
        URLConnection connection = this.url.openConnection();
        return connection.getInputStream();
    }

    public Metadata metadata() {
        return Metadata.from((String)"url", (String)this.url.toString());
    }

    public static UrlSource from(String url) {
        try {
            return new UrlSource(new URL(url));
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UrlSource from(URL url) {
        return new UrlSource(url);
    }

    public static UrlSource from(URI uri) {
        try {
            return new UrlSource(uri.toURL());
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

