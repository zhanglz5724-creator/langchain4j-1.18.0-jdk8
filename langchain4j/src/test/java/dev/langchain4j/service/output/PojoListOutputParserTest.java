package dev.langchain4j.service.output;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PojoListOutputParserTest {

    static final class Person {
        private final String name;

        public Person(String name) { this.name = name; }

        public String name() { return name; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person person = (Person) o;
            return java.util.Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name); }

        @Override
        public String toString() { return "Person[name=" + name + "]"; }
    }

    @ParameterizedTest
    @MethodSource
    void should_parse_list_of_pojo(String json, List<Person> expected) {

        // when
        List<Person> people = new PojoListOutputParser<>(Person.class).parse(json);

        // then
        assertThat(people).isEqualTo(expected);
    }

    static Stream<Arguments> should_parse_list_of_pojo() {
        return Stream.of(
                Arguments.of("{\"values\":[{\"name\":\"Klaus\"}]}", Arrays.asList(new Person("Klaus"))),
                Arguments.of(
                        "{\"values\":[{\"name\":\"Klaus\"}, {\"name\":\"Franny\"}]}",
                        Arrays.asList(new Person("Klaus"), new Person("Franny"))),
                Arguments.of("", Collections.emptyList()),
                Arguments.of(" ", Collections.emptyList()),
                Arguments.of("{\"values\":[]}", Collections.emptyList()),
                Arguments.of(" {\"values\":[{\"name\":\"Klaus\"}]} ", Arrays.asList(new Person("Klaus"))));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"{}", "{\"values\": null}"})
    void should_fail_to_parse_empty_input(String input) {

        assertThatThrownBy(() -> new PojoListOutputParser<>(Person.class).parse(input))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse")
                .hasMessageContaining("Person");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "banana",
                "{\"values\": \"\"}",
                "{\"values\":\"banana\"}",
                "{\"values\":[\"banana\"]}",
                "{\"values\":{\"name\":\"Klaus\"}}",
                "{\"banana\":[{\"name\":\"Klaus\"}]}",
            })
    void should_fail_to_parse_invalid_input(String text) {

        assertThatThrownBy(() -> new PojoListOutputParser<>(Person.class).parse(text))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse")
                .hasMessageContaining("Person");
    }

    @ParameterizedTest
    @MethodSource
    void should_parse_person_with_null_name(String json, List<Person> expected) {
        // when
        List<Person> people = new PojoListOutputParser<>(Person.class).parse(json);

        // then
        assertThat(people).isEqualTo(expected);
    }

    static Stream<Arguments> should_parse_person_with_null_name() {
        return Stream.of(
                Arguments.of("{\"values\":[{\"name\":null}]}", Arrays.asList(new Person(null))),
                Arguments.of(
                        "{\"values\":[{\"name\":\"Alice\"},{\"name\":null},{\"name\":\"Bob\"}]}",
                        Arrays.asList(new Person("Alice"), new Person(null), new Person("Bob"))));
    }

    @ParameterizedTest
    @MethodSource
    void should_parse_person_with_missing_name(String json, List<Person> expected) {
        // when
        List<Person> people = new PojoListOutputParser<>(Person.class).parse(json);

        // then
        assertThat(people).isEqualTo(expected);
    }

    static Stream<Arguments> should_parse_person_with_missing_name() {
        return Stream.of(
                Arguments.of("{\"values\":[{}]}", Arrays.asList(new Person(null))),
                Arguments.of(
                        "{\"values\":[{},{\"name\":\"Alice\"},{}]}",
                        Arrays.asList(new Person(null), new Person("Alice"), new Person(null))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "{\"values\":{\"name\":\"Alice\"}}",
                "[{\"name\":\"Alice\"}]",
                "{\"values\":[\"Alice\"]}",
            })
    void should_fail_to_parse_malformed_json(String malformedJson) {
        assertThatThrownBy(() -> new PojoListOutputParser<>(Person.class).parse(malformedJson))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "{\"values\":[{\"name\":[\"array\"]}]}",
                "{\"values\":[{\"name\":{\"object\":\"value\"}}]}",
            })
    void should_fail_to_parse_wrong_type_for_name(String json) {
        assertThatThrownBy(() -> new PojoListOutputParser<>(Person.class).parse(json))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse");
    }

    @ParameterizedTest
    @MethodSource
    void should_handle_whitespace_in_json(String json, List<Person> expected) {
        // when
        List<Person> people = new PojoListOutputParser<>(Person.class).parse(json);

        // then
        assertThat(people).isEqualTo(expected);
    }

    static Stream<Arguments> should_handle_whitespace_in_json() {
        return Stream.of(
                Arguments.of("  {  \"values\"  :  [  ]  }  ", Collections.emptyList()),
                Arguments.of("\n{\n\"values\"\n:\n[\n]\n}\n", Collections.emptyList()),
                Arguments.of("\t{\t\"values\"\t:\t[\t]\t}\t", Collections.emptyList()),
                Arguments.of(
                        "   {   \"values\"   :   [   {   \"name\"   :   \"Alice\"   }   ]   }   ",
                        Arrays.asList(new Person("Alice"))));
    }

    @ParameterizedTest
    @MethodSource
    void should_parse_escaped_characters(String json, List<Person> expected) {
        // when
        List<Person> people = new PojoListOutputParser<>(Person.class).parse(json);

        // then
        assertThat(people).isEqualTo(expected);
    }

    static Stream<Arguments> should_parse_escaped_characters() {
        return Stream.of(
                Arguments.of("{\"values\":[{\"name\":\"Alice\\\"Bob\"}]}", Arrays.asList(new Person("Alice\"Bob"))),
                Arguments.of("{\"values\":[{\"name\":\"Line\\nBreak\"}]}", Arrays.asList(new Person("Line\nBreak"))),
                Arguments.of("{\"values\":[{\"name\":\"Tab\\tCharacter\"}]}", Arrays.asList(new Person("Tab\tCharacter"))),
                Arguments.of("{\"values\":[{\"name\":\"Back\\\\slash\"}]}", Arrays.asList(new Person("Back\\slash"))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "{\"values\":[{\"name\":\"Alice\",\"name\":\"Bob\"}]}",
                "{\"values\":[{\"name\":\"Alice\"}],\"values\":[{\"name\":\"Bob\"}]}",
            })
    void should_handle_or_fail_duplicate_keys(String json) {
        try {
            List<Person> people = new PojoListOutputParser<>(Person.class).parse(json);
            assertThat(people).isNotNull();
        } catch (OutputParsingException e) {
            assertThat(e.getMessage()).contains("Failed to parse");
        }
    }
}
