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
import java.net.URL;

public class ClassPathSource
implements DocumentSource {
    private final URL url;
    private final ClassLoader classLoader;
    private final Metadata metadata = new Metadata();

    protected ClassPathSource(URL url, ClassLoader classLoader) {
        this.url = (URL)ValidationUtils.ensureNotNull((Object)url, (String)"url");
        this.classLoader = (ClassLoader)ValidationUtils.ensureNotNull((Object)classLoader, (String)"classLoader");
        String file = this.url.getFile();
        this.metadata.put("url", file);
        this.metadata.put("file_name", file.substring(file.lastIndexOf(47) + 1));
    }

    public static ClassPathSource from(String classPathResource) {
        return ClassPathSource.from(classPathResource, Thread.currentThread().getContextClassLoader());
    }

    public static ClassPathSource from(String classPathResource, ClassLoader classLoader) {
        String resource = ValidationUtils.ensureNotBlank((String)classPathResource, (String)"classPathResource");
        ClassLoader cl = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
        URL url = (URL)ValidationUtils.ensureNotNull((Object)cl.getResource(resource), (String)"'%s' was not found as a classpath resource", (Object[])new Object[]{classPathResource});
        return new ClassPathSource(url, classLoader);
    }

    public URL url() {
        return this.url;
    }

    public ClassLoader classLoader() {
        return this.classLoader;
    }

    public boolean isInsideArchive() {
        return "jar".equalsIgnoreCase(this.url.getProtocol());
    }

    public InputStream inputStream() throws IOException {
        return this.url.openStream();
    }

    public Metadata metadata() {
        return this.metadata;
    }
}

