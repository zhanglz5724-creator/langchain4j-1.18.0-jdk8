/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.batch.BatchError;
import org.jspecify.annotations.Nullable;

@Experimental
public interface BatchItemResult<T> {
    public boolean isSuccess();

    public @Nullable T response();

    public @Nullable BatchError error();

    public static <T> BatchItemResult<T> success(T response) {
        return new Success<T>(response);
    }

    public static <T> BatchItemResult<T> failure(BatchError error) {
        return new Failure(error);
    }

    public static final class Failure<T>
    implements BatchItemResult<T> {
        private final BatchError error;

        public Failure(BatchError error) {
            ValidationUtils.ensureNotNull(error, "error");
            this.error = error;
        }

        @Override
        public BatchError error() {
            return this.error;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public @Nullable T response() {
            return null;
        }
    }

    public static final class Success<T>
    implements BatchItemResult<T> {
        private final T response;

        public Success(T response) {
            ValidationUtils.ensureNotNull(response, "response");
            this.response = response;
        }

        @Override
        public T response() {
            return this.response;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public @Nullable BatchError error() {
            return null;
        }
    }
}

