/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.client;

import java.util.List;

public class ApiResponse<T> {
    private T result;
    private boolean success;
    private List<Error> errors;
    private List<String> messages;

    public T getResult() {
        return this.result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public List<Error> getErrors() {
        return this.errors;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApiResponse)) {
            return false;
        }
        ApiResponse other = (ApiResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        T this$result = this.getResult();
        T other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) {
            return false;
        }
        if (this.isSuccess() != other.isSuccess()) {
            return false;
        }
        List<Error> this$errors = this.getErrors();
        List<Error> other$errors = other.getErrors();
        if (this$errors == null ? other$errors != null : !((Object)this$errors).equals(other$errors)) {
            return false;
        }
        List<String> this$messages = this.getMessages();
        List<String> other$messages = other.getMessages();
        return !(this$messages == null ? other$messages != null : !((Object)this$messages).equals(other$messages));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ApiResponse;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        T $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        List<Error> $errors = this.getErrors();
        result = result * 59 + ($errors == null ? 43 : ((Object)$errors).hashCode());
        List<String> $messages = this.getMessages();
        result = result * 59 + ($messages == null ? 43 : ((Object)$messages).hashCode());
        return result;
    }

    public String toString() {
        return "ApiResponse(result=" + this.getResult() + ", success=" + this.isSuccess() + ", errors=" + this.getErrors() + ", messages=" + this.getMessages() + ")";
    }

    public static class Error {
        private String message;
        private int code;

        public String getMessage() {
            return this.message;
        }

        public int getCode() {
            return this.code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Error)) {
                return false;
            }
            Error other = (Error)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$message = this.getMessage();
            String other$message = other.getMessage();
            if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
                return false;
            }
            return this.getCode() == other.getCode();
        }

        protected boolean canEqual(Object other) {
            return other instanceof Error;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $message = this.getMessage();
            result = result * 59 + ($message == null ? 43 : $message.hashCode());
            result = result * 59 + this.getCode();
            return result;
        }

        public String toString() {
            return "ApiResponse.Error(message=" + this.getMessage() + ", code=" + this.getCode() + ")";
        }
    }
}

