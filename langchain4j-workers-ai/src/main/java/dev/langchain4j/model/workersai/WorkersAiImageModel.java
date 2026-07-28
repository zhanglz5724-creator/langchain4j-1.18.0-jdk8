/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.workersai;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.workersai.client.AbstractWorkersAIModel;
import dev.langchain4j.model.workersai.client.WorkersAiImageGenerationRequest;
import dev.langchain4j.model.workersai.spi.WorkersAiImageModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import javax.imageio.ImageIO;

public class WorkersAiImageModel
extends AbstractWorkersAIModel
implements ImageModel {
    private static final String MIME_TYPE = "image/png";

    public WorkersAiImageModel(Builder builder) {
        super(builder.accountId, builder.modelName, builder.apiToken, builder.httpClientBuilder);
    }

    public WorkersAiImageModel(String accountId, String modelName, String apiToken) {
        super(accountId, modelName, apiToken);
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(WorkersAiImageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            WorkersAiImageModelBuilderFactory factory = (WorkersAiImageModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public Response<Image> generate(String prompt) {
        ValidationUtils.ensureNotBlank((String)prompt, (String)"Prompt");
        return new Response((Object)this.convertAsImage(this.executeQuery(prompt, null, null)), null, FinishReason.STOP);
    }

    public Response<Image> edit(Image image, String prompt) {
        ValidationUtils.ensureNotBlank((String)prompt, (String)"Prompt");
        ValidationUtils.ensureNotNull((Object)image, (String)"Image");
        return new Response((Object)this.convertAsImage(this.executeQuery(prompt, null, image)), null, FinishReason.STOP);
    }

    public Response<Image> edit(Image image, Image mask, String prompt) {
        ValidationUtils.ensureNotBlank((String)prompt, (String)"Prompt");
        ValidationUtils.ensureNotNull((Object)image, (String)"Image");
        ValidationUtils.ensureNotNull((Object)mask, (String)"Mask");
        return new Response((Object)this.convertAsImage(this.executeQuery(prompt, mask, image)), null, FinishReason.STOP);
    }

    public Response<File> generate(String prompt, String destinationFile) {
        ValidationUtils.ensureNotBlank((String)prompt, (String)"Prompt");
        ValidationUtils.ensureNotBlank((String)destinationFile, (String)"Destination file");
        try {
            byte[] image = this.executeQuery(prompt, null, null);
            try (FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);){
                fileOutputStream.write(image);
            }
            return new Response((Object)new File(destinationFile), null, FinishReason.STOP);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] executeQuery(String prompt, Image image, Image mask) {
        try {
            WorkersAiImageGenerationRequest imgReq = new WorkersAiImageGenerationRequest();
            imgReq.setPrompt(prompt);
            if (image != null && image.url() != null) {
                imgReq.setImage(this.getPixels(image.url().toURL()));
            }
            if (mask != null && mask.url() != null) {
                imgReq.setMask(this.getPixels(mask.url().toURL()));
            }
            return this.client.generateImage(imgReq, this.accountId, this.modelName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getPixels(URL imageUrl) throws Exception {
        BufferedImage image = ImageIO.read(imageUrl);
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelData = new int[width * height];
        int index = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int pixel = image.getRGB(x, y);
                int alpha = pixel >> 24 & 0xFF;
                int red = pixel >> 16 & 0xFF;
                int green = pixel >> 8 & 0xFF;
                int blue = pixel & 0xFF;
                int color = alpha << 24 | red << 16 | green << 8 | blue;
                pixelData[index++] = color;
            }
        }
        return pixelData;
    }

    public Image convertAsImage(byte[] data) {
        return Image.builder().base64Data(Base64.getEncoder().encodeToString(data)).mimeType(MIME_TYPE).build();
    }

    public static class Builder {
        public String accountId;
        public String apiToken;
        public String modelName;
        public HttpClientBuilder httpClientBuilder;

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public WorkersAiImageModel build() {
            return new WorkersAiImageModel(this);
        }
    }
}

