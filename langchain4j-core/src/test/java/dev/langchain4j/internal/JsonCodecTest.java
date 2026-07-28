package dev.langchain4j.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class JsonCodecTest {

    private static final String PERSON_JSON =
            "{\n" +
                    "    \"name\": \"Klaus\",\n" +
                    "    \"age\": 42\n" +
                    "}\n";

    static List<Json.JsonCodec> codecs() {
        return Arrays.asList(new JacksonJsonCodec());
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record(Json.JsonCodec codec) {
        // when
        Person person = codec.fromJson(PERSON_JSON, Person.class);

        // then
        assertThat(person.name()).isEqualTo("Klaus");
        assertThat(person.age()).isEqualTo(42);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_missing_fields(Json.JsonCodec codec) {
        // given
        String json = "{}";

        // when
        Person pojo = codec.fromJson(json, Person.class);

        // then
        assertThat(pojo.name()).isNull();
        assertThat(pojo.age()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_different_field_order(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"age\": 42,\n" +
                        "    \"name\": \"Klaus\"\n" +
                        "}\n";

        // when
        Person pojo = codec.fromJson(json, Person.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEqualTo(42);
    }

    /**
     * To prevent issues caused by LLM hallucinations,
     * the default behavior is to fail when a hallucination is detected.
     * If LLM generates structured output or a tool call containing unknown fields or properties,
     * we fail by default.
     */
    @ParameterizedTest
    @MethodSource("codecs")
    void should_fail_on_unknown_fields_by_default(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"age\": 42,\n" +
                        "    \"married\": false\n" +
                        "}\n";

        // when-then
        assertThatThrownBy(() -> codec.fromJson(json, Person.class))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasCauseExactlyInstanceOf(UnrecognizedPropertyException.class)
                .hasMessageContaining("married");

        // if required, user can override the default behaviour and ignore unknown properties
        LenientPersonRecord lenientPerson = codec.fromJson(json, LenientPersonRecord.class);
        assertThat(lenientPerson).isEqualTo(new LenientPersonRecord("Klaus", 42));
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_null_value(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"age\": null\n" +
                        "}\n";

        // when
        Person pojo = codec.fromJson(json, Person.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_wrong_type(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"age\": \"42\"\n" +
                        "}\n";

        // when
        Person pojo = codec.fromJson(json, Person.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEqualTo(42);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_wrong_type_2(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"age\": 42.0\n" +
                        "}\n";

        // when
        Person pojo = codec.fromJson(json, Person.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEqualTo(42);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_nested_record(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"address\": {\n" +
                        "        \"city\": \"Langley Falls\"\n" +
                        "    }\n" +
                        "}\n";

        // when
        PersonRecordWithNestedRecord pojo = codec.fromJson(json, PersonRecordWithNestedRecord.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.address().city()).isEqualTo("Langley Falls");
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_missing_collections(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\"\n" +
                        "}\n";

        // when
        PersonRecordWithCollections pojo = codec.fromJson(json, PersonRecordWithCollections.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.collection()).isNull();
        assertThat(pojo.list()).isNull();
        assertThat(pojo.set()).isNull();
        assertThat(pojo.array()).isNull();
        assertThat(pojo.map()).isNull();
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_empty_collections(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"collection\": [],\n" +
                        "    \"list\": [],\n" +
                        "    \"set\": [],\n" +
                        "    \"array\": [],\n" +
                        "    \"map\": {}\n" +
                        "}\n";

        // when
        PersonRecordWithCollections pojo = codec.fromJson(json, PersonRecordWithCollections.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.collection()).isEmpty();
        assertThat(pojo.list()).isEmpty();
        assertThat(pojo.set()).isEmpty();
        assertThat(pojo.array()).isEmpty();
        assertThat(pojo.map()).isEmpty();
    }

    @Disabled("optional fields are currently not supported")
    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_optional_present(Json.JsonCodec codec) {
        // when
        PersonRecordWithOptional pojo = codec.fromJson(PERSON_JSON, PersonRecordWithOptional.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).hasValue(42);
    }

    @Disabled("optional fields are currently not supported")
    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_optional_absent(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\"\n" +
                        "}\n";

        // when
        PersonRecordWithOptional pojo = codec.fromJson(json, PersonRecordWithOptional.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEmpty();
    }

    @Disabled("optional fields are currently not supported")
    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_optional_null(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"age\": null\n" +
                        "}\n";

        // when
        PersonRecordWithOptional pojo = codec.fromJson(json, PersonRecordWithOptional.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void inner_record(Json.JsonCodec codec) {
        // when
        PersonInnerRecord pojo = codec.fromJson(PERSON_JSON, PersonInnerRecord.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEqualTo(42);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_validation(Json.JsonCodec codec) {
        // given
        String json =
                "{\n" +
                        "    \"name\": \"Klaus\",\n" +
                        "    \"age\": -1\n" +
                        "}\n";

        // when-then
        assertThatThrownBy(() -> codec.fromJson(json, PersonRecordWithValidation.class))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasCauseExactlyInstanceOf(ValueInstantiationException.class)
                .hasRootCauseExactlyInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("age must be positive");
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void record_with_custom_ctor(Json.JsonCodec codec) {
        // when
        PersonRecordCustomCtor pojo = codec.fromJson(
                "{\n" +
                        "    \"name\": \"Klaus\"\n" +
                        "}\n",
                PersonRecordCustomCtor.class);

        // then
        assertThat(pojo.name()).isEqualTo("Klaus");
        assertThat(pojo.age()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("codecs")
    void static_nested_class(Json.JsonCodec codec) {
        // when
        PersonStaticNestedClass pojo = codec.fromJson(PERSON_JSON, PersonStaticNestedClass.class);

        // then
        assertThat(pojo.name).isEqualTo("Klaus");
        assertThat(pojo.age).isEqualTo(42);
    }

    // =========================================================================
    // Static Nested Classes (Replacements for Java 14+ 'record' types)
    // =========================================================================

    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String name() { return name; }
        public int age() { return age; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LenientPersonRecord {
        private final String name;
        private final int age;

        public LenientPersonRecord(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String name() { return name; }
        public int age() { return age; }
    }

    static class Address {
        private final String city;

        public Address(String city) {
            this.city = city;
        }

        public String city() { return city; }
    }

    static class PersonRecordWithNestedRecord {
        private final String name;
        private final Address address;

        public PersonRecordWithNestedRecord(String name, Address address) {
            this.name = name;
            this.address = address;
        }

        public String name() { return name; }
        public Address address() { return address; }
    }

    static class PersonRecordWithCollections {
        private final String name;
        private final Collection<String> collection;
        private final List<String> list;
        private final Set<Object> set;
        private final String[] array;
        private final Map<Object, Object> map;

        public PersonRecordWithCollections(String name, Collection<String> collection, List<String> list, Set<Object> set, String[] array, Map<Object, Object> map) {
            this.name = name;
            this.collection = collection;
            this.list = list;
            this.set = set;
            this.array = array;
            this.map = map;
        }

        public String name() { return name; }
        public Collection<String> collection() { return collection; }
        public List<String> list() { return list; }
        public Set<Object> set() { return set; }
        public String[] array() { return array; }
        public Map<Object, Object> map() { return map; }
    }

    static class PersonRecordWithOptional {
        private final String name;
        private final Optional<Integer> age;

        public PersonRecordWithOptional(String name, Optional<Integer> age) {
            this.name = name;
            this.age = age;
        }

        public String name() { return name; }
        public Optional<Integer> age() { return age; }
    }

    static class PersonInnerRecord {
        private final String name;
        private final int age;

        public PersonInnerRecord(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String name() { return name; }
        public int age() { return age; }
    }

    static class PersonRecordWithValidation {
        private final String name;
        private final int age;

        public PersonRecordWithValidation(String name, int age) {
            if (age < 0) {
                throw new IllegalArgumentException("age must be positive");
            }
            this.name = name;
            this.age = age;
        }

        public String name() { return name; }
        public int age() { return age; }
    }

    static class PersonRecordCustomCtor {
        private final String name;
        private final int age;

        // Single-parameter constructor to match the test expectation
        // where 'age' defaults to 0 when not provided in the JSON payload.
        public PersonRecordCustomCtor(String name) {
            this.name = name;
            this.age = 0;
        }

        public String name() { return name; }
        public int age() { return age; }
    }

    static class PersonStaticNestedClass {
        private String name;
        private int age;

        // Default constructor needed for Jackson deserialization of non-final fields
        public PersonStaticNestedClass() {}
    }
}
