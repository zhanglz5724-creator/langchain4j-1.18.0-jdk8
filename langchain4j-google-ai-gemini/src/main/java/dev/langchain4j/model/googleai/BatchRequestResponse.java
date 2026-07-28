/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.model.batch.BatchError
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class BatchRequestResponse {
    private BatchRequestResponse() {
    }

    public static final class BatchFileRequest<REQ> {
        private final String key;
        private final REQ request;

        @JsonCreator
        public BatchFileRequest(@JsonProperty(value="key") String key, @JsonProperty(value="request") REQ request) {
            this.key = key;
            this.request = request;
        }

        public String key() {
            return this.key;
        }

        public REQ request() {
            return this.request;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchFileRequest)) {
                return false;
            }
            BatchFileRequest that = (BatchFileRequest)o;
            return Objects.equals(this.key, that.key) && Objects.equals(this.request, that.request);
        }

        public int hashCode() {
            return Objects.hash(this.key, this.request);
        }

        public String toString() {
            return "BatchFileRequest[key=" + this.key + ", request=" + this.request + "]";
        }
    }

    public static final class ListOperationsResponse<RESP> {
        private final @Nullable List<Operation<RESP>> operations;
        private final @Nullable String nextPageToken;

        @JsonCreator
        public ListOperationsResponse(@JsonProperty(value="operations") @Nullable List<Operation<RESP>> operations, @JsonProperty(value="nextPageToken") @Nullable String nextPageToken) {
            this.operations = operations;
            this.nextPageToken = nextPageToken;
        }

        public @Nullable List<Operation<RESP>> operations() {
            return this.operations;
        }

        public @Nullable String nextPageToken() {
            return this.nextPageToken;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ListOperationsResponse)) {
                return false;
            }
            ListOperationsResponse that = (ListOperationsResponse)o;
            return Objects.equals(this.operations, that.operations) && Objects.equals(this.nextPageToken, that.nextPageToken);
        }

        public int hashCode() {
            return Objects.hash(this.operations, this.nextPageToken);
        }

        public String toString() {
            return "ListOperationsResponse[operations=" + this.operations + ", nextPageToken=" + this.nextPageToken + "]";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class Operation<RESP> {
        private final String name;
        private final Map<String, Object> metadata;
        private final boolean done;
        private final Status error;
        private final BatchCreateResponse<RESP> response;

        @JsonCreator
        public Operation(@JsonProperty(value="name") String name, @JsonProperty(value="metadata") Map<String, Object> metadata, @JsonProperty(value="done") boolean done, @JsonProperty(value="error") Status error, @JsonProperty(value="response") BatchCreateResponse<RESP> response) {
            this.name = name;
            this.metadata = metadata;
            this.done = done;
            this.error = error;
            this.response = response;
        }

        public String name() {
            return this.name;
        }

        public Map<String, Object> metadata() {
            return this.metadata;
        }

        public boolean done() {
            return this.done;
        }

        public Status error() {
            return this.error;
        }

        public BatchCreateResponse<RESP> response() {
            return this.response;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Operation)) {
                return false;
            }
            Operation that = (Operation)o;
            return this.done == that.done && Objects.equals(this.name, that.name) && Objects.equals(this.metadata, that.metadata) && Objects.equals(this.error, that.error) && Objects.equals(this.response, that.response);
        }

        public int hashCode() {
            return Objects.hash(this.name, this.metadata, this.done, this.error, this.response);
        }

        public String toString() {
            return "Operation[name=" + this.name + ", metadata=" + this.metadata + ", done=" + this.done + ", error=" + this.error + ", response=" + this.response + "]";
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class Status {
            private final int code;
            private final String message;
            private final @Nullable List<Map<String, Object>> details;

            @JsonCreator
            public Status(@JsonProperty(value="code") int code, @JsonProperty(value="message") String message, @JsonProperty(value="details") @Nullable List<Map<String, Object>> details) {
                this.code = code;
                this.message = message;
                this.details = details;
            }

            public int code() {
                return this.code;
            }

            public String message() {
                return this.message;
            }

            public @Nullable List<Map<String, Object>> details() {
                return this.details;
            }

            public dev.langchain4j.model.batch.BatchError toGenericStatus() {
                return new dev.langchain4j.model.batch.BatchError(this.code, this.message, this.details);
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Status)) {
                    return false;
                }
                Status that = (Status)o;
                return this.code == that.code && Objects.equals(this.message, that.message) && Objects.equals(this.details, that.details);
            }

            public int hashCode() {
                return Objects.hash(this.code, this.message, this.details);
            }

            public String toString() {
                return "Status[code=" + this.code + ", message=" + this.message + ", details=" + this.details + "]";
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    static final class BatchCreateResponse<RESP> {
        private final String type;
        private final InlinedResponses<RESP> inlinedResponses;

        @JsonCreator
        BatchCreateResponse(@JsonProperty(value="@type") String type, @JsonProperty(value="inlinedResponses") InlinedResponses<RESP> inlinedResponses) {
            this.type = type;
            this.inlinedResponses = inlinedResponses;
        }

        String type() {
            return this.type;
        }

        InlinedResponses<RESP> inlinedResponses() {
            return this.inlinedResponses;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchCreateResponse)) {
                return false;
            }
            BatchCreateResponse that = (BatchCreateResponse)o;
            return Objects.equals(this.type, that.type) && Objects.equals(this.inlinedResponses, that.inlinedResponses);
        }

        public int hashCode() {
            return Objects.hash(this.type, this.inlinedResponses);
        }

        public String toString() {
            return "BatchCreateResponse[type=" + this.type + ", inlinedResponses=" + this.inlinedResponses + "]";
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class InlinedResponseWrapper<RESP> {
            private final RESP response;
            private final Operation.Status error;

            @JsonCreator
            InlinedResponseWrapper(@JsonProperty(value="response") RESP response, @JsonProperty(value="error") Operation.Status error) {
                this.response = response;
                this.error = error;
            }

            RESP response() {
                return this.response;
            }

            Operation.Status error() {
                return this.error;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof InlinedResponseWrapper)) {
                    return false;
                }
                InlinedResponseWrapper that = (InlinedResponseWrapper)o;
                return Objects.equals(this.response, that.response) && Objects.equals(this.error, that.error);
            }

            public int hashCode() {
                return Objects.hash(this.response, this.error);
            }

            public String toString() {
                return "InlinedResponseWrapper[response=" + this.response + ", error=" + this.error + "]";
            }
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static final class InlinedResponses<RESP> {
            private final List<InlinedResponseWrapper<RESP>> inlinedResponses;

            @JsonCreator
            InlinedResponses(@JsonProperty(value="inlinedResponses") List<InlinedResponseWrapper<RESP>> inlinedResponses) {
                this.inlinedResponses = inlinedResponses;
            }

            List<InlinedResponseWrapper<RESP>> inlinedResponses() {
                return this.inlinedResponses;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof InlinedResponses)) {
                    return false;
                }
                InlinedResponses that = (InlinedResponses)o;
                return Objects.equals(this.inlinedResponses, that.inlinedResponses);
            }

            public int hashCode() {
                return Objects.hash(this.inlinedResponses);
            }

            public String toString() {
                return "InlinedResponses[inlinedResponses=" + this.inlinedResponses + "]";
            }
        }
    }

    static final class BatchCreateFileRequest {
        private final FileBatch batch;

        @JsonCreator
        BatchCreateFileRequest(@JsonProperty(value="batch") FileBatch batch) {
            this.batch = batch;
        }

        FileBatch batch() {
            return this.batch;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchCreateFileRequest)) {
                return false;
            }
            BatchCreateFileRequest that = (BatchCreateFileRequest)o;
            return Objects.equals(this.batch, that.batch);
        }

        public int hashCode() {
            return Objects.hash(this.batch);
        }

        public String toString() {
            return "BatchCreateFileRequest[batch=" + this.batch + "]";
        }

        static final class FileInputConfig {
            private final String fileName;

            @JsonCreator
            FileInputConfig(@JsonProperty(value="file_name") String fileName) {
                this.fileName = fileName;
            }

            String fileName() {
                return this.fileName;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof FileInputConfig)) {
                    return false;
                }
                FileInputConfig that = (FileInputConfig)o;
                return Objects.equals(this.fileName, that.fileName);
            }

            public int hashCode() {
                return Objects.hash(this.fileName);
            }

            public String toString() {
                return "FileInputConfig[fileName=" + this.fileName + "]";
            }
        }

        static final class FileBatch {
            private final String displayName;
            private final FileInputConfig inputConfig;

            @JsonCreator
            FileBatch(@JsonProperty(value="display_name") String displayName, @JsonProperty(value="input_config") FileInputConfig inputConfig) {
                this.displayName = displayName;
                this.inputConfig = inputConfig;
            }

            String displayName() {
                return this.displayName;
            }

            FileInputConfig inputConfig() {
                return this.inputConfig;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof FileBatch)) {
                    return false;
                }
                FileBatch that = (FileBatch)o;
                return Objects.equals(this.displayName, that.displayName) && Objects.equals(this.inputConfig, that.inputConfig);
            }

            public int hashCode() {
                return Objects.hash(this.displayName, this.inputConfig);
            }

            public String toString() {
                return "FileBatch[displayName=" + this.displayName + ", inputConfig=" + this.inputConfig + "]";
            }
        }
    }

    static final class BatchCreateRequest<REQ> {
        private final Batch<REQ> batch;

        @JsonCreator
        BatchCreateRequest(@JsonProperty(value="batch") Batch<REQ> batch) {
            this.batch = batch;
        }

        Batch<REQ> batch() {
            return this.batch;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchCreateRequest)) {
                return false;
            }
            BatchCreateRequest that = (BatchCreateRequest)o;
            return Objects.equals(this.batch, that.batch);
        }

        public int hashCode() {
            return Objects.hash(this.batch);
        }

        public String toString() {
            return "BatchCreateRequest[batch=" + this.batch + "]";
        }

        static final class InlinedRequest<REQ> {
            private final REQ request;
            private final Map<String, String> metadata;

            @JsonCreator
            InlinedRequest(@JsonProperty(value="request") REQ request, @JsonProperty(value="metadata") Map<String, String> metadata) {
                this.request = request;
                this.metadata = metadata;
            }

            REQ request() {
                return this.request;
            }

            Map<String, String> metadata() {
                return this.metadata;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof InlinedRequest)) {
                    return false;
                }
                InlinedRequest that = (InlinedRequest)o;
                return Objects.equals(this.request, that.request) && Objects.equals(this.metadata, that.metadata);
            }

            public int hashCode() {
                return Objects.hash(this.request, this.metadata);
            }

            public String toString() {
                return "InlinedRequest[request=" + this.request + ", metadata=" + this.metadata + "]";
            }
        }

        static final class Requests<REQ> {
            private final List<InlinedRequest<REQ>> requests;

            @JsonCreator
            Requests(@JsonProperty(value="requests") List<InlinedRequest<REQ>> requests) {
                this.requests = requests;
            }

            List<InlinedRequest<REQ>> requests() {
                return this.requests;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Requests)) {
                    return false;
                }
                Requests that = (Requests)o;
                return Objects.equals(this.requests, that.requests);
            }

            public int hashCode() {
                return Objects.hash(this.requests);
            }

            public String toString() {
                return "Requests[requests=" + this.requests + "]";
            }
        }

        static final class InputConfig<REQ> {
            private final Requests<REQ> requests;

            @JsonCreator
            InputConfig(@JsonProperty(value="requests") Requests<REQ> requests) {
                this.requests = requests;
            }

            Requests<REQ> requests() {
                return this.requests;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof InputConfig)) {
                    return false;
                }
                InputConfig that = (InputConfig)o;
                return Objects.equals(this.requests, that.requests);
            }

            public int hashCode() {
                return Objects.hash(this.requests);
            }

            public String toString() {
                return "InputConfig[requests=" + this.requests + "]";
            }
        }

        static final class Batch<REQ> {
            private final String displayName;
            private final InputConfig<REQ> inputConfig;
            private final long priority;

            @JsonCreator
            Batch(@JsonProperty(value="display_name") String displayName, @JsonProperty(value="input_config") InputConfig<REQ> inputConfig, @JsonProperty(value="priority") long priority) {
                this.displayName = displayName;
                this.inputConfig = inputConfig;
                this.priority = priority;
            }

            String displayName() {
                return this.displayName;
            }

            InputConfig<REQ> inputConfig() {
                return this.inputConfig;
            }

            long priority() {
                return this.priority;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Batch)) {
                    return false;
                }
                Batch that = (Batch)o;
                return this.priority == that.priority && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.inputConfig, that.inputConfig);
            }

            public int hashCode() {
                return Objects.hash(this.displayName, this.inputConfig, this.priority);
            }

            public String toString() {
                return "Batch[displayName=" + this.displayName + ", inputConfig=" + this.inputConfig + ", priority=" + this.priority + "]";
            }
        }
    }

    public static enum BatchJobState {
        BATCH_STATE_PENDING,
        BATCH_STATE_RUNNING,
        BATCH_STATE_SUCCEEDED,
        BATCH_STATE_FAILED,
        BATCH_STATE_CANCELLED,
        BATCH_STATE_EXPIRED,
        UNSPECIFIED;

    }

    public static final class BatchName {
        private final String value;

        @JsonCreator
        public BatchName(@JsonProperty(value="value") String value) {
            BatchName.ensureOperationNameFormat(value);
            this.value = value;
        }

        private static void ensureOperationNameFormat(String operationName) {
            if (!operationName.startsWith("batches/")) {
                throw new IllegalArgumentException("Batch name must start with 'batches/'. This name is returned when creating the batch with #createBatchInline.");
            }
        }

        public String value() {
            return this.value;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchName)) {
                return false;
            }
            BatchName that = (BatchName)o;
            return Objects.equals(this.value, that.value);
        }

        public int hashCode() {
            return Objects.hash(this.value);
        }

        public String toString() {
            return "BatchName[value=" + this.value + "]";
        }
    }

    public static final class BatchError<T>
    implements BatchResponse<T> {
        private final BatchName batchName;
        private final int code;
        private final String message;
        private final BatchJobState state;
        private final List<Map<String, Object>> details;

        @JsonCreator
        public BatchError(@JsonProperty(value="batchName") BatchName batchName, @JsonProperty(value="code") int code, @JsonProperty(value="message") String message, @JsonProperty(value="state") BatchJobState state, @JsonProperty(value="details") List<Map<String, Object>> details) {
            this.batchName = batchName;
            this.code = code;
            this.message = message;
            this.state = state;
            this.details = details;
        }

        public BatchName batchName() {
            return this.batchName;
        }

        public int code() {
            return this.code;
        }

        public String message() {
            return this.message;
        }

        public BatchJobState state() {
            return this.state;
        }

        public List<Map<String, Object>> details() {
            return this.details;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchError)) {
                return false;
            }
            BatchError that = (BatchError)o;
            return this.code == that.code && Objects.equals(this.batchName, that.batchName) && Objects.equals(this.message, that.message) && this.state == that.state && Objects.equals(this.details, that.details);
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.batchName, this.code, this.message, this.state, this.details});
        }

        public String toString() {
            return "BatchError[batchName=" + this.batchName + ", code=" + this.code + ", message=" + this.message + ", state=" + (Object)((Object)this.state) + ", details=" + this.details + "]";
        }
    }

    public static final class BatchSuccess<T>
    implements BatchResponse<T> {
        private final BatchName batchName;
        private final List<T> responses;
        private final @Nullable List<Operation.Status> errors;

        @JsonCreator
        public BatchSuccess(@JsonProperty(value="batchName") BatchName batchName, @JsonProperty(value="responses") List<T> responses, @JsonProperty(value="errors") @Nullable List<Operation.Status> errors) {
            this.batchName = batchName;
            this.responses = responses;
            this.errors = errors;
        }

        public BatchName batchName() {
            return this.batchName;
        }

        public List<T> responses() {
            return this.responses;
        }

        public @Nullable List<Operation.Status> errors() {
            return this.errors;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchSuccess)) {
                return false;
            }
            BatchSuccess that = (BatchSuccess)o;
            return Objects.equals(this.batchName, that.batchName) && Objects.equals(this.responses, that.responses) && Objects.equals(this.errors, that.errors);
        }

        public int hashCode() {
            return Objects.hash(this.batchName, this.responses, this.errors);
        }

        public String toString() {
            return "BatchSuccess[batchName=" + this.batchName + ", responses=" + this.responses + ", errors=" + this.errors + "]";
        }
    }

    public static final class BatchIncomplete<T>
    implements BatchResponse<T> {
        private final BatchName batchName;
        private final BatchJobState state;

        @JsonCreator
        public BatchIncomplete(@JsonProperty(value="batchName") BatchName batchName, @JsonProperty(value="state") BatchJobState state) {
            this.batchName = batchName;
            this.state = state;
        }

        public BatchName batchName() {
            return this.batchName;
        }

        public BatchJobState state() {
            return this.state;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BatchIncomplete)) {
                return false;
            }
            BatchIncomplete that = (BatchIncomplete)o;
            return Objects.equals(this.batchName, that.batchName) && this.state == that.state;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.batchName, this.state});
        }

        public String toString() {
            return "BatchIncomplete[batchName=" + this.batchName + ", state=" + (Object)((Object)this.state) + "]";
        }
    }

    public static interface BatchResponse<T> {
    }
}

