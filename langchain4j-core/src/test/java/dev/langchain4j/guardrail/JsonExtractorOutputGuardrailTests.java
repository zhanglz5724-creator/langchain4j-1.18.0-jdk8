package dev.langchain4j.guardrail;

import static dev.langchain4j.test.guardrail.GuardrailAssertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.stream.Stream;
import java.util.Collections;
import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.data.message.AiMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Deprecated()
class JsonExtractorOutputGuardrailTests {
    private static final String JSON = "{\n" +
            "    \"name\": \"MyObject\",\n" +
            "    \"description\": \"Description of MyObject\"\n" +
            "}";

    private static final JsonExtractorOutputGuardrail<MyObject> MY_OBJECT_JSON_OUTPUT_GUARDRAIL =
            new JsonExtractorOutputGuardrail<>(MyObject.class);
    private static final JsonExtractorOutputGuardrail<Map<String, MyObject>> MAP_OF_MY_OBJECT_JSON_OUTPUT_GUARDRAIL =
            new JsonExtractorOutputGuardrail<>(new TypeReference<Map<String, MyObject>>() {});

    @ParameterizedTest
    @MethodSource("guardrails")
    void successfulValidation(String json, JsonExtractorOutputGuardrail<?> guardrail, Object expectedResult) {
        JsonExtractorOutputGuardrail<?> guardrailSpy = spy(guardrail);
        OutputGuardrailResult result = guardrailSpy.validate(AiMessage.from(json));

        assertThat(result)
                .isNotNull()
                .extracting(
                        OutputGuardrailResult::result,
                        OutputGuardrailResult::successfulText,
                        OutputGuardrailResult::successfulResult)
                .containsExactly(GuardrailResult.Result.SUCCESS_WITH_RESULT, json, expectedResult);

        verify(guardrailSpy).deserialize(json);
    }

    @ParameterizedTest
    @MethodSource("guardrails")
    void successfulValidationAfterTrimming(
            String json, JsonExtractorOutputGuardrail<?> guardrail, Object expectedResult) {
        String input = "abc" + json;
        parseJsonRequiringTrimming(json, guardrail, expectedResult, input);
    }

    @ParameterizedTest
    @MethodSource("guardrails")
    void successfulValidationAfterTrimmingWithInvalidJson(
            String json, JsonExtractorOutputGuardrail<?> guardrail, Object expectedResult) {
        String input = "abc [test] {\"key\":\"value\"} " + json + " [another] xyz";
        parseJsonRequiringTrimming(json, guardrail, expectedResult, input);
    }

    private void parseJsonRequiringTrimming(
            String json, JsonExtractorOutputGuardrail<?> guardrail, Object expectedResult, String input) {
        JsonExtractorOutputGuardrail<?> guardrailSpy = spy(guardrail);
        OutputGuardrailResult result = guardrailSpy.validate(AiMessage.from(input));

        assertThat(result)
                .isNotNull()
                .extracting(
                        OutputGuardrailResult::result,
                        OutputGuardrailResult::successfulText,
                        OutputGuardrailResult::successfulResult)
                .containsExactly(GuardrailResult.Result.SUCCESS_WITH_RESULT, json, expectedResult);

        verify(guardrailSpy).deserialize(input);
    }

    @Test
    void invalidJson() {
        JsonExtractorOutputGuardrail<?> guardrail = spy(MY_OBJECT_JSON_OUTPUT_GUARDRAIL);
        String input = "{{" + JSON;
        OutputGuardrailResult result = guardrail.validate(AiMessage.from(input));

        assertThat(result)
                .hasSingleFailureWithMessageAndReprompt(
                        JsonExtractorOutputGuardrail.DEFAULT_REPROMPT_MESSAGE,
                        JsonExtractorOutputGuardrail.DEFAULT_REPROMPT_PROMPT);

        verify(guardrail).deserialize(input);
        verify(guardrail).invokeInvalidJson(any(AiMessage.class), eq(input));
    }

    static Stream<Arguments> guardrails() {
        MyObject result = new MyObject("MyObject", "Description of MyObject");

        return Stream.of(
                Arguments.of(JSON, MY_OBJECT_JSON_OUTPUT_GUARDRAIL, result),
                Arguments.of(
                        String.format("{ \"myObject\": %s}", JSON),
                        MAP_OF_MY_OBJECT_JSON_OUTPUT_GUARDRAIL,
                        Collections.singletonMap("myObject", result)));
    }

    static class MyObject {
        private String name;
        private String description;

        public MyObject() {}

        public MyObject(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String name() { return name; }
        public String description() { return description; }
    }
}
