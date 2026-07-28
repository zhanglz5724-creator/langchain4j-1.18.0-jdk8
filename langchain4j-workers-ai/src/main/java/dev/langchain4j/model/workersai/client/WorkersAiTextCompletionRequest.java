/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

public class WorkersAiTextCompletionRequest {
    String prompt;

    public WorkersAiTextCompletionRequest() {
    }

    public WorkersAiTextCompletionRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorkersAiTextCompletionRequest)) {
            return false;
        }
        WorkersAiTextCompletionRequest other = (WorkersAiTextCompletionRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$prompt = this.getPrompt();
        String other$prompt = other.getPrompt();
        return !(this$prompt == null ? other$prompt != null : !this$prompt.equals(other$prompt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WorkersAiTextCompletionRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $prompt = this.getPrompt();
        result = result * 59 + ($prompt == null ? 43 : $prompt.hashCode());
        return result;
    }

    public String toString() {
        return "WorkersAiTextCompletionRequest(prompt=" + this.getPrompt() + ")";
    }
}

