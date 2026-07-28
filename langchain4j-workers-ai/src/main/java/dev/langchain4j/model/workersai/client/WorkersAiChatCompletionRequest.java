/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

import java.util.ArrayList;
import java.util.List;

public class WorkersAiChatCompletionRequest {
    private List<Message> messages = new ArrayList<Message>();

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorkersAiChatCompletionRequest)) {
            return false;
        }
        WorkersAiChatCompletionRequest other = (WorkersAiChatCompletionRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<Message> this$messages = this.getMessages();
        List<Message> other$messages = other.getMessages();
        return !(this$messages == null ? other$messages != null : !((Object)this$messages).equals(other$messages));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WorkersAiChatCompletionRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<Message> $messages = this.getMessages();
        result = result * 59 + ($messages == null ? 43 : ((Object)$messages).hashCode());
        return result;
    }

    public String toString() {
        return "WorkersAiChatCompletionRequest(messages=" + this.getMessages() + ")";
    }

    public WorkersAiChatCompletionRequest() {
    }

    public WorkersAiChatCompletionRequest(MessageRole role, String content) {
        this();
        this.addMessage(role, content);
    }

    public void addMessage(MessageRole role, String content) {
        Message message = new Message(role, content);
        this.messages.add(message);
    }

    public static enum MessageRole {
        system,
        ai,
        user;

    }

    public static class Message {
        private MessageRole role;
        private String content;

        public Message() {
        }

        public Message(MessageRole role, String content) {
            this.role = role;
            this.content = content;
        }

        public MessageRole getRole() {
            return this.role;
        }

        public String getContent() {
            return this.content;
        }

        public void setRole(MessageRole role) {
            this.role = role;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Message)) {
                return false;
            }
            Message other = (Message)o;
            if (!other.canEqual(this)) {
                return false;
            }
            MessageRole this$role = this.getRole();
            MessageRole other$role = other.getRole();
            if (this$role == null ? other$role != null : !((Object)((Object)this$role)).equals((Object)other$role)) {
                return false;
            }
            String this$content = this.getContent();
            String other$content = other.getContent();
            return !(this$content == null ? other$content != null : !this$content.equals(other$content));
        }

        protected boolean canEqual(Object other) {
            return other instanceof Message;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            MessageRole $role = this.getRole();
            result = result * 59 + ($role == null ? 43 : ((Object)((Object)$role)).hashCode());
            String $content = this.getContent();
            result = result * 59 + ($content == null ? 43 : $content.hashCode());
            return result;
        }

        public String toString() {
            return "WorkersAiChatCompletionRequest.Message(role=" + (Object)((Object)this.getRole()) + ", content=" + this.getContent() + ")";
        }
    }
}

