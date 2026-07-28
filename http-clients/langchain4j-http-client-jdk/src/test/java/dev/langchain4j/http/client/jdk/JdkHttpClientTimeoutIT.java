package dev.langchain4j.http.client.jdk;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientTimeoutIT;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdkHttpClientTimeoutIT extends HttpClientTimeoutIT {

    @Override
    protected List<HttpClient> clients(Duration readTimeout) {
        return Collections.<HttpClient>singletonList(
                JdkHttpClient.builder()
                        .readTimeout(readTimeout)
                        .build()
        );
    }

    @Override
    protected void assertCause(Throwable throwable) {
        assertThat(throwable).hasCauseExactlyInstanceOf(SocketTimeoutException.class);
    }

    @Override
    protected Class<? extends Exception> expectedReadTimeoutRootCauseExceptionType() {
        return null;
    }
}
