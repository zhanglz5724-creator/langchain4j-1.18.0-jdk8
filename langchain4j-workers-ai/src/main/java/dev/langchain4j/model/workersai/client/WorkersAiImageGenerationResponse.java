/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

import dev.langchain4j.model.workersai.client.ApiResponse;
import java.io.InputStream;

public class WorkersAiImageGenerationResponse
extends ApiResponse<ImageGenerationResult> {

    public static class ImageGenerationResult {
        private InputStream image;

        public ImageGenerationResult() {
        }

        public ImageGenerationResult(InputStream image) {
            this.image = image;
        }

        public InputStream getImage() {
            return this.image;
        }

        public void setImage(InputStream image) {
            this.image = image;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ImageGenerationResult)) {
                return false;
            }
            ImageGenerationResult other = (ImageGenerationResult)o;
            if (!other.canEqual(this)) {
                return false;
            }
            InputStream this$image = this.getImage();
            InputStream other$image = other.getImage();
            return !(this$image == null ? other$image != null : !this$image.equals(other$image));
        }

        protected boolean canEqual(Object other) {
            return other instanceof ImageGenerationResult;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            InputStream $image = this.getImage();
            result = result * 59 + ($image == null ? 43 : $image.hashCode());
            return result;
        }

        public String toString() {
            return "WorkersAiImageGenerationResponse.ImageGenerationResult(image=" + this.getImage() + ")";
        }
    }
}

