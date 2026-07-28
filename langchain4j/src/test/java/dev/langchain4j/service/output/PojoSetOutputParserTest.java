package dev.langchain4j.service.output;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PojoSetOutputParserTest {

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

    static final class Config {
        private final String key;
        private final String value;

        public Config(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String key() { return key; }
        public String value() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Config)) return false;
            Config config = (Config) o;
            return java.util.Objects.equals(key, config.key) && java.util.Objects.equals(value, config.value);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(key, value); }

        @Override
        public String toString() { return "Config[key=" + key + ", value=" + value + "]"; }
    }

    static final class Item {
        private final String name;
        private final Config config;

        public Item(String name, Config config) {
            this.name = name;
            this.config = config;
        }

        public String name() { return name; }
        public Config config() { return config; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;
            Item item = (Item) o;
            return java.util.Objects.equals(name, item.name) && java.util.Objects.equals(config, item.config);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, config); }

        @Override
        public String toString() { return "Item[name=" + name + ", config=" + config + "]"; }
    }

    @ParameterizedTest
    @MethodSource
    void should_parse_set_of_pojo(String json, Set<Person> expected) {

        // when
        Set<Person> people = new PojoSetOutputParser<>(Person.class).parse(json);

        // then
        assertThat(people).isEqualTo(expected);
    }


    static Stream<Arguments> should_parse_set_of_pojo() {
        HashSet<Person> people = new HashSet<>();
        people.add(new Person("Klaus"));
        HashSet<Person> franny = new HashSet<>();
        franny.add(new Person("Franny"));
        HashSet<Person> k = new HashSet<>();
        k.add(new Person("Klaus"));
        return Stream.of(
                Arguments.of("{\"values\":[{\"name\":\"Klaus\"}]}", k),
                Arguments.of(
                        "{\"values\":[{\"name\":\"Klaus\"}, {\"name\":\"Franny\"}]}",
                        people, franny),
                Arguments.of("", new HashSet<>()),
                Arguments.of(" ", new HashSet<>()),
                Arguments.of("{\"values\":[]}", new HashSet<>()),
                Arguments.of(" {\"values\":[{\"name\":\"Klaus\"}]} ", k));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"{}", "{\"values\": null}"})
    void should_fail_to_parse_empty_input(String input) {

        assertThatThrownBy(() -> new PojoSetOutputParser<>(Person.class).parse(input))
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

        // when-then
        assertThatThrownBy(() -> new PojoSetOutputParser<>(Person.class).parse(text))
                .isExactlyInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse")
                .hasMessageContaining("Person");
    }

    @Test
    void should_handle_duplicate_values_in_set() {
        // Given JSON with duplicate entries
        String json = "{\"values\":[{\"name\":\"Value1\"}, {\"name\":\"Value1\"}, {\"name\":\"Value2\"}]}";

        // When parsing
        Set<Person> people = new PojoSetOutputParser<>(Person.class).parse(json);

        // Then duplicates are removed (Set behavior)
        assertThat(people).hasSize(2);
        assertThat(people).containsExactlyInAnyOrder(new Person("Value1"), new Person("Value2"));
    }

    @Test
    void should_handle_nested_objects() {

        String json = "{\"values\":[{\"name\":\"Item1\",\"config\":{\"key\":\"Key1\",\"value\":\"Value1\"}}]}";

        Set<Item> items = new PojoSetOutputParser<>(Item.class).parse(json);

        assertThat(items).hasSize(1);
        assertThat(items.iterator().next().config().value()).isEqualTo("Value1");
    }

    @Test
    void should_handle_null_field_values() {
        String json = "{\"values\":[{\"name\":null}]}";

        Set<Person> people = new PojoSetOutputParser<>(Person.class).parse(json);

        assertThat(people).hasSize(1);
        assertThat(people.iterator().next().name()).isNull();
    }

    @Test
    void should_handle_missing_required_fields() {
        // JSON missing the 'name' field
        String json = "{\"values\":[{}]}";

        Set<Person> people = new PojoSetOutputParser<>(Person.class).parse(json);

        assertThat(people).hasSize(1);
        assertThat(people.iterator().next().name()).isNull();
    }

    @Test
    void should_handle_escaped_characters_in_json() {
        String json = "{\"values\":[{\"name\":\"Value:\\\"test\\\"\"}]}";

        Set<Person> people = new PojoSetOutputParser<>(Person.class).parse(json);

        assertThat(people).hasSize(1);
        assertThat(people.iterator().next().name()).isEqualTo("Value:\"test\"");
    }

    @Test
    void should_preserve_order_independence_of_set() {
        String json1 = "{\"values\":[{\"name\":\"Value1\"},{\"name\":\"Value2\"}]}";
        String json2 = "{\"values\":[{\"name\":\"Value2\"},{\"name\":\"Value1\"}]}";

        Set<Person> set1 = new PojoSetOutputParser<>(Person.class).parse(json1);
        Set<Person> set2 = new PojoSetOutputParser<>(Person.class).parse(json2);

        assertThat(set1).isEqualTo(set2);
    }
}
