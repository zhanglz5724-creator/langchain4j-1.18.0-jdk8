/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.pdf.PdfFile;
import dev.langchain4j.internal.ContentUtil;
import dev.langchain4j.internal.ValidationUtils;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class PdfFileContent
implements Content {
    private final PdfFile pdfFile;

    @Override
    public ContentType type() {
        return ContentType.PDF;
    }

    public PdfFileContent(URI url) {
        this.pdfFile = PdfFile.builder().url(ValidationUtils.ensureNotNull(url, "url")).build();
    }

    public PdfFileContent(String url) {
        this(URI.create(url));
    }

    public PdfFileContent(String base64Data, String mimeType) {
        this.pdfFile = PdfFile.builder().base64Data(ValidationUtils.ensureNotBlank(base64Data, "base64data")).mimeType(mimeType).build();
    }

    public PdfFileContent(PdfFile pdfFile) {
        this.pdfFile = pdfFile;
    }

    public PdfFile pdfFile() {
        return this.pdfFile;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        PdfFileContent that = (PdfFileContent)o;
        return Objects.equals(this.pdfFile, that.pdfFile);
    }

    public int hashCode() {
        return Objects.hash(this.pdfFile);
    }

    public String toString() {
        return "PdfFileContent { pdfFile = " + this.pdfFile + " }";
    }

    public static PdfFileContent from(URI url) {
        return new PdfFileContent(url);
    }

    public static PdfFileContent from(String url) {
        return new PdfFileContent(url);
    }

    public static PdfFileContent from(String base64Data, String mimeType) {
        return new PdfFileContent(base64Data, mimeType);
    }

    public static PdfFileContent from(Path pdfFilePath) {
        return PdfFileContent.from(pdfFilePath, PdfFile.DEFAULT_MIME_TYPE);
    }

    public static PdfFileContent from(Path pdfFilePath, String mimeType) {
        return PdfFileContent.from(ContentUtil.extractBase64Content(pdfFilePath), mimeType);
    }

    public static PdfFileContent from(PdfFile pdfFile) {
        return new PdfFileContent(pdfFile);
    }
}

