/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

import dev.langchain4j.model.workersai.client.ApiResponse;
import java.util.List;

public class WorkersAiEmbeddingResponse
extends ApiResponse<EmbeddingResult> {

    public static class EmbeddingResult {
        private List<Integer> shape;
        private List<List<Float>> data;

        public List<Integer> getShape() {
            return this.shape;
        }

        public List<List<Float>> getData() {
            return this.data;
        }

        public void setShape(List<Integer> shape) {
            this.shape = shape;
        }

        public void setData(List<List<Float>> data) {
            this.data = data;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof EmbeddingResult)) {
                return false;
            }
            EmbeddingResult other = (EmbeddingResult)o;
            if (!other.canEqual(this)) {
                return false;
            }
            List<Integer> this$shape = this.getShape();
            List<Integer> other$shape = other.getShape();
            if (this$shape == null ? other$shape != null : !((Object)this$shape).equals(other$shape)) {
                return false;
            }
            List<List<Float>> this$data = this.getData();
            List<List<Float>> other$data = other.getData();
            return !(this$data == null ? other$data != null : !((Object)this$data).equals(other$data));
        }

        protected boolean canEqual(Object other) {
            return other instanceof EmbeddingResult;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            List<Integer> $shape = this.getShape();
            result = result * 59 + ($shape == null ? 43 : ((Object)$shape).hashCode());
            List<List<Float>> $data = this.getData();
            result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
            return result;
        }

        public String toString() {
            return "WorkersAiEmbeddingResponse.EmbeddingResult(shape=" + this.getShape() + ", data=" + this.getData() + ")";
        }
    }
}

