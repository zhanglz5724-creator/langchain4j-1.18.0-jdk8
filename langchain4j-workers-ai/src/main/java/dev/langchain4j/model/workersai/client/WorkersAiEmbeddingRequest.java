/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

import java.util.ArrayList;
import java.util.List;

public class WorkersAiEmbeddingRequest {
    private List<String> text = new ArrayList<String>();

    public List<String> getText() {
        return this.text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorkersAiEmbeddingRequest)) {
            return false;
        }
        WorkersAiEmbeddingRequest other = (WorkersAiEmbeddingRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<String> this$text = this.getText();
        List<String> other$text = other.getText();
        return !(this$text == null ? other$text != null : !((Object)this$text).equals(other$text));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WorkersAiEmbeddingRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<String> $text = this.getText();
        result = result * 59 + ($text == null ? 43 : ((Object)$text).hashCode());
        return result;
    }

    public String toString() {
        return "WorkersAiEmbeddingRequest(text=" + this.getText() + ")";
    }
}

