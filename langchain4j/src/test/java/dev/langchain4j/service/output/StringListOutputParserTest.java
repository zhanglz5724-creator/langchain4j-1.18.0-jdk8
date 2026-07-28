package dev.langchain4j.service.output;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringListOutputParserTest {

    @ParameterizedTest
    @MethodSource
    void should_parse_list_of_strings(String text, List<String> expected) {

        // given
        StringListOutputParser parser = new StringListOutputParser();

        // when
        List<String> result = parser.parse(text);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> should_parse_list_of_strings() {
        return Stream.of(

                // Plain text
                Arguments.of("CAT", Arrays.asList("CAT")),
                Arguments.of("CAT\nDOG", Arrays.asList("CAT", "DOG")),
                Arguments.of("", Arrays.asList()),
                Arguments.of(" ", Arrays.asList()),
                Arguments.of("  CAT  ", Arrays.asList("CAT")),
                Arguments.of(" CAT \n DOG ", Arrays.asList("CAT", "DOG")),

                // JSON
                Arguments.of("{\"values\":[\"CAT\"]}", Arrays.asList("CAT")),
                Arguments.of("{\"values\":[\"CAT\",\"DOG\"]}", Arrays.asList("CAT", "DOG")),
                Arguments.of("{\"values\":[]}", Arrays.asList()),
                Arguments.of("  {\"values\":[\"CAT\"]}  ", Arrays.asList("CAT"))
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"{}", "{\"values\": null}", "{\"banana\": []}"})
    void should_fail_to_parse_empty_input(String input) {

        assertThatThrownBy(() -> new StringListOutputParser().parse(input))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse")
                .hasMessageContaining("java.util.List<java.lang.String>");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "{\"values\": \"\"}",
            "{\"values\": false}",
            "{\"values\":\"banana\"}",
            "{\"values\":{\"name\":\"Klaus\"}}",
            "{\"banana\":[{\"name\":\"Klaus\"}]}",
    })
    void should_fail_to_parse_invalid_input(String input) {

        assertThatThrownBy(() -> new StringListOutputParser().parse(input))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse")
                .hasMessageContaining("java.util.List<java.lang.String>");
    }
}
