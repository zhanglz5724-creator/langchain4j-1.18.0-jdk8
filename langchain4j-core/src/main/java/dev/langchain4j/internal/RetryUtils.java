/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.exception.NonRetriableException;
import dev.langchain4j.internal.ExceptionMapper;
import java.util.Random;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
public final class RetryUtils {
    private static final Random RANDOM = new Random();
    private static final Logger log = LoggerFactory.getLogger(RetryUtils.class);
    public static final RetryPolicy DEFAULT_RETRY_POLICY = RetryUtils.retryPolicyBuilder().maxRetries(2).delayMillis(500).jitterScale(0.2).backoffExp(1.5).build();

    private RetryUtils() {
    }

    public static RetryPolicy.Builder retryPolicyBuilder() {
        return new RetryPolicy.Builder();
    }

    public static <T> T withRetry(Callable<T> action) {
        return DEFAULT_RETRY_POLICY.withRetry(action);
    }

    public static <T> T withRetry(Callable<T> action, int maxRetries) {
        return DEFAULT_RETRY_POLICY.withRetry(action, maxRetries);
    }

    public static void withRetry(Runnable action, int maxRetries) {
        DEFAULT_RETRY_POLICY.withRetry(() -> {
            action.run();
            return null;
        }, maxRetries);
    }

    public static <T> T withRetryMappingExceptions(Callable<T> action) {
        return (T)RetryUtils.withRetry(() -> ExceptionMapper.DEFAULT.withExceptionMapper(action));
    }

    public static <T> T withRetryMappingExceptions(Callable<T> action, int maxRetries) {
        return RetryUtils.withRetryMappingExceptions(action, maxRetries, ExceptionMapper.DEFAULT);
    }

    public static <T> T withRetryMappingExceptions(Callable<T> action, int maxRetries, ExceptionMapper exceptionMapper) {
        return (T)RetryUtils.withRetry(() -> exceptionMapper.withExceptionMapper(action), maxRetries);
    }

    public static final class RetryPolicy {
        private final int maxRetries;
        private final int delayMillis;
        private final double jitterScale;
        private final double backoffExp;

        public RetryPolicy(int maxRetries, int delayMillis, double jitterScale, double backoffExp) {
            this.maxRetries = maxRetries;
            this.delayMillis = delayMillis;
            this.jitterScale = jitterScale;
            this.backoffExp = backoffExp;
        }

        public double rawDelayMs(int retry) {
            return (double)this.delayMillis * Math.pow(this.backoffExp, retry);
        }

        public int jitterDelayMillis(int retry) {
            double delay = this.rawDelayMs(retry);
            double jitter = delay * this.jitterScale;
            int jitterBound = (int)jitter;
            if (jitterBound <= 0) {
                return (int)delay;
            }
            return (int)(delay + (double)RANDOM.nextInt(jitterBound));
        }

        public void sleep(int retry) {
            try {
                Thread.sleep(this.jitterDelayMillis(retry));
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while retrying", e);
            }
        }

        public <T> T withRetry(Callable<T> action) {
            return this.withRetry(action, this.maxRetries);
        }

        public <T> T withRetry(Callable<T> action, int maxRetries) {
            int retry = 0;
            while (true) {
                try {
                    return action.call();
                }
                catch (NonRetriableException e) {
                    throw e;
                }
                catch (Exception e) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new RuntimeException("Interrupted during action execution", e);
                    }
                    if (retry >= maxRetries) {
                        throw e instanceof RuntimeException ? (RuntimeException)e : new LangChain4jException(e);
                    }
                    log.warn(String.format("A retriable exception occurred. Remaining retries: %s of %s", maxRetries - retry, maxRetries), (Throwable)e);
                    this.sleep(retry);
                    ++retry;
                    continue;
                }
            }
        }

        public static final class Builder {
            private int maxRetries = 2;
            private int delayMillis = 1000;
            private double jitterScale = 0.2;
            private double backoffExp = 1.5;

            public Builder maxRetries(int maxRetries) {
                this.maxRetries = maxRetries;
                return this;
            }

            public Builder delayMillis(int delayMillis) {
                this.delayMillis = delayMillis;
                return this;
            }

            public Builder jitterScale(double jitterScale) {
                this.jitterScale = jitterScale;
                return this;
            }

            public Builder backoffExp(double backoffExp) {
                this.backoffExp = backoffExp;
                return this;
            }

            public RetryPolicy build() {
                return new RetryPolicy(this.maxRetries, this.delayMillis, this.jitterScale, this.backoffExp);
            }
        }
    }
}

