//package dev.langchain4j.model.anthropic;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.containing;
//import static com.github.tomakehurst.wiremock.client.WireMock.post;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
//import dev.langchain4j.exception.AuthenticationException;
//import dev.langchain4j.exception.HttpException;
//import dev.langchain4j.exception.InternalServerException;
//import dev.langchain4j.exception.InvalidRequestException;
//import dev.langchain4j.exception.LangChain4jException;
//import dev.langchain4j.exception.ModelNotFoundException;
//import dev.langchain4j.exception.RateLimitException;
//import dev.langchain4j.model.chat.ChatModel;
//import java.time.Duration;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.parallel.Execution;
//import org.junit.jupiter.api.parallel.ExecutionMode;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.ValueSource;
//
//@Execution(ExecutionMode.CONCURRENT)
//class AnthropicChatModelErrorsTest {
//
//    private static final WireMockServer wireMock = new WireMockServer(
//            WireMockConfiguration.options().dynamicPort());
//
//    private static final Duration TIMEOUT = Duration.ofSeconds(2);
//    private static final Duration ERROR_MODEL_TIMEOUT = Duration.ofSeconds(30);
//
//    private static final ChatModel model = AnthropicChatModel.builder()
//            .apiKey("dummy-key")
//            .baseUrl("http://localhost:" + getPort() + "/v1")
//            .modelName("does not matter")
//            .maxTokens(20)
//            .timeout(ERROR_MODEL_TIMEOUT)
//            .maxRetries(0)
//            .logRequests(true)
//            .logResponses(true)
//            .build();
//
//    private static int getPort() {
//        if (!wireMock.isRunning()) {
//            wireMock.start();
//        }
//        return wireMock.port();
//    }
//
//    private double seed;
//
//    @BeforeEach
//    void setUp() {
//        if (!wireMock.isRunning()) {
//            wireMock.start();
//        }
//        seed = ThreadLocalRandom.current().nextDouble();
//    }
//
//    public static Stream<Arguments> errors() {
//        return Stream.of(
//                Arguments.of(400, InvalidRequestException.class),
//                Arguments.of(401, AuthenticationException.class),
//                Arguments.of(403, AuthenticationException.class),
//                Arguments.of(404, ModelNotFoundException.class),
//                Arguments.of(413, InvalidRequestException.class),
//                Arguments.of(429, RateLimitException.class),
//                Arguments.of(500, InternalServerException.class),
//                Arguments.of(503, InternalServerException.class));
//    }
//
//    @ParameterizedTest
//    @MethodSource("errors")
//    void should_handle_error_responses(int httpStatusCode, Class<LangChain4jException> exception) {
//
//        final String question = "What is the number: " + seed;
//        final String message = "Error with seed: " + seed;
//
//        stubFor(post(urlEqualTo("/v1/messages"))
//                .withRequestBody(containing(question))
//                .willReturn(aResponse()
//                        .withStatus(httpStatusCode)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody(String.format("{\"type\":\"error\",\"error\":{\"type\":\"does not matter\",\"message\":\"%s\"}}",
//                                message))));
//
//        assertThatThrownBy(() -> model.chat(question))
//                .isExactlyInstanceOf(exception)
//                .satisfies(ex -> assertThat(((HttpException) ex.getCause()).statusCode())
//                        .as("statusCode")
//                        .isEqualTo(httpStatusCode));
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 100})
//    void should_handle_timeout(int millis) {
//
//        Duration timeout = Duration.ofMillis(millis);
//
//        ChatModel model = AnthropicChatModel.builder()
//                .apiKey("dummy-key")
//                .baseUrl("http://localhost:" + wireMock.port() + "/v1")
//                .modelName("does not matter")
//                .maxTokens(20)
//                .timeout(timeout)
//                .logRequests(true)
//                .logResponses(true)
//                .build();
//
//        final String question = "Simulate timeout " + System.currentTimeMillis();
//        stubFor(post(urlEqualTo("/v1/messages"))
//                .withRequestBody(containing(question))
//                .willReturn(aResponse()
//                        .withStatus(204)
//                        .withFixedDelay((int) TIMEOUT.plusMillis(250).toMillis())));
//
//        assertThatThrownBy(() -> model.chat(question))
//                .isExactlyInstanceOf(dev.langchain4j.exception.TimeoutException.class);
//    }
//
//    @AfterEach
//    void afterEach() {
////        assertThat(findAllUnmatchedRequests()).isEmpty();
//    }
//}
