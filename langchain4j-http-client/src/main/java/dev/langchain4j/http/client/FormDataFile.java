/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.http.client;

import dev.langchain4j.Experimental;
import java.util.Arrays;
import java.util.Objects;

@Experimental
public class FormDataFile {
    private final String fileName;
    private final String contentType;
    private final byte[] content;

    public FormDataFile(String fileName, String contentType, byte[] content) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }

    public String fileName() {
        return this.fileName;
    }

    public String contentType() {
        return this.contentType;
    }

    public byte[] content() {
        return this.content;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        FormDataFile that = (FormDataFile)o;
        return Objects.equals(this.fileName, that.fileName) && Objects.equals(this.contentType, that.contentType) && Objects.deepEquals(this.content, that.content);
    }

    public int hashCode() {
        return Objects.hash(this.fileName, this.contentType, Arrays.hashCode(this.content));
    }
}

