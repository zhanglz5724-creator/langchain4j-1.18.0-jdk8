package dev.langchain4j.service.common;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.internal.Utils.copy;
import static dev.langchain4j.internal.Utils.generateUUIDFrom;
import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;
import static dev.langchain4j.service.common.AbstractAiServiceWithJsonSchemaIT.PersonExtractor3.MaritalStatus.SINGLE;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonReferenceSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(PER_CLASS)
public abstract class AbstractAiServiceWithJsonSchemaIT {
    // TODO test the same for streaming models

    protected abstract List<ChatModel> models();

    interface PersonExtractor1 {

        class Person {

            String name;
            int age;
            Double height;
            boolean married;
        }

        Person extractPersonFrom(String text);
    }

    interface MissingDataPersonExtractor {

        class Address {
            private final String street;
            public Address(String street) { this.street = street; }
            public String street() { return street; }
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Address address = (Address) o;
                return Objects.equals(street, address.street);
            }
            @Override
            public int hashCode() { return Objects.hash(street); }
            @Override
            public String toString() { return "Address[street=" + street + "]"; }
        }

        class Person {

            String name;
            int age;
            Double height;
            boolean married;
            Map<String, Object> map;
            List<String> list;
            String[] array;
            Address address;
            MaritalStatus2 maritalStatus;
            LocalDate localDate;
        }

        Person extractPersonFrom(String text);
    }

    interface PojoSetPersonExtractor {
        PojoSetPerson extractPersonFrom(String text);
    }

    interface ResultPersonExtractor {

        class Person {

            String name;
        }

        Result<Person> extractPersonFrom(String text);
    }

    interface RecursionPersonExtractor {
        RecursionPerson extractPersonFrom(String text);
    }

    interface BooleanPrimitiveExtractor {

        @UserMessage("Extract if the person from the following text is a man: {{it}}")
        boolean isPersonAMan(String text);
    }

    interface BooleanBoxedExtractor {

        @UserMessage("Extract if the person from the following text is a man: {{it}}")
        Boolean isPersonAMan(String text);
    }

    interface IntPrimitiveExtractor {

        @UserMessage("Extract number of people mentioned in the following text: {{it}}")
        int extractNumberOfPeople(String text);
    }

    interface IntegerBoxedExtractor {

        @UserMessage("Extract number of people mentioned in the following text: {{it}}")
        Integer extractNumberOfPeople(String text);
    }

    interface LongPrimitiveExtractor {

        @UserMessage("Extract number of people mentioned in the following text: {{it}}")
        long extractNumberOfPeople(String text);
    }

    interface LongBoxedExtractor {

        @UserMessage("Extract number of people mentioned in the following text: {{it}}")
        Long extractNumberOfPeople(String text);
    }

    interface FloatPrimitiveExtractor {

        @UserMessage("Extract temperature in Munich from the following text: {{it}}")
        float extractTemperatureInMunich(String text);
    }

    interface FloatBoxedExtractor {

        @UserMessage("Extract temperature in Munich from the following text: {{it}}")
        Float extractTemperatureInMunich(String text);
    }

    interface DoublePrimitiveExtractor {

        @UserMessage("Extract temperature in Munich from the following text: {{it}}")
        double extractTemperatureInMunich(String text);
    }

    interface DoubleBoxedExtractor {

        @UserMessage("Extract temperature in Munich from the following text: {{it}}")
        Double extractTemperatureInMunich(String text);
    }

    interface PeopleListExtractor {

        class Person {

            String name;
            int age;
            Double height;
            boolean married;
        }

        List<Person> extractPeopleFrom(String text);
    }

    interface PeopleResultListExtractor {

        class Person {

            String name;
        }

        Result<List<Person>> extractPeopleFrom(String text);
    }

    interface ListOfStringsPrimitiveExtractor {

        @UserMessage("Extract names of people from the following text: {{it}}")
        List<String> extractPeopleNames(String text);
    }

    interface SetOfStringsPrimitiveExtractor {

        @UserMessage("Extract names of people from the following text: {{it}}")
        Set<String> extractSetOfPeopleNames(String text);
    }

    interface PojoSetPrimitiveExtractor {

        class Person {

            String name;
            int age;
            Double height;
            boolean married;
        }

        Set<Person> extractSetOfPojoFrom(String text);
    }

    interface EnumPrimitiveExtractor {

        MaritalStatus2 extractEnumFrom(String text);
    }

    interface EnumListPrimitiveExtractor {

        List<MaritalStatus2> extractListOfEnumsFrom(String text);
    }

    interface EnumSetPrimitiveExtractor {

        Set<Weather> extractSetOfEnumsFrom(String text);
    }

    interface AnimalPolymorphicExtractor {
        Animal extractAnimalFrom(String text);
    }

    interface AnimalsPolymorphicExtractor {
        List<Animal> extractAnimalsFrom(String text);
    }

    interface OwnerPolymorphicExtractor {
        PolymorphicOwner extractOwnerFrom(String text);
    }

    interface ExpressionRecursiveExtractor {
        ArithmeticExpression extractFrom(String text);
    }

    static class PojoSetPet {
        private final String name;
        PojoSetPet(String name) { this.name = name; }
        String name() { return name; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PojoSetPet pet = (PojoSetPet) o;
            return Objects.equals(name, pet.name);
        }
        @Override
        public int hashCode() { return Objects.hash(name); }
        @Override
        public String toString() { return "PojoSetPet[name=" + name + "]"; }
    }

    static class PojoSetPerson {
        private final String name;
        private final Set<PojoSetPet> pets;
        PojoSetPerson(String name, Set<PojoSetPet> pets) { this.name = name; this.pets = pets; }
        String name() { return name; }
        Set<PojoSetPet> pets() { return pets; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PojoSetPerson person = (PojoSetPerson) o;
            return Objects.equals(name, person.name) && Objects.equals(pets, person.pets);
        }
        @Override
        public int hashCode() { return Objects.hash(name, pets); }
        @Override
        public String toString() { return "PojoSetPerson[name=" + name + ", pets=" + pets + "]"; }
    }

    static class RecursionPerson {
        private final String name;
        private final List<RecursionPerson> children;

        RecursionPerson(String name, List<RecursionPerson> children) {
            this.name = name;
            this.children = copy(children);
        }

        String name() { return name; }
        List<RecursionPerson> children() { return children; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecursionPerson person = (RecursionPerson) o;
            return Objects.equals(name, person.name) && Objects.equals(children, person.children);
        }

        @Override
        public int hashCode() { return Objects.hash(name, children); }

        @Override
        public String toString() { return "RecursionPerson[name=" + name + ", children=" + children + "]"; }
    }

    static class PolymorphicOwner {
        private final String name;
        private final Animal pet;

        PolymorphicOwner(String name, Animal pet) {
            this.name = name;
            this.pet = pet;
        }

        String name() { return name; }
        Animal pet() { return pet; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PolymorphicOwner owner = (PolymorphicOwner) o;
            return Objects.equals(name, owner.name) && Objects.equals(pet, owner.pet);
        }

        @Override
        public int hashCode() { return Objects.hash(name, pet); }

        @Override
        public String toString() { return "PolymorphicOwner[name=" + name + ", pet=" + pet + "]"; }
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_primitives(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor1 personExtractor = AiServices.create(PersonExtractor1.class, model);

        String text = "Extract the person's information from the following text: "
                + "Klaus is 37 years old, 1.78m height and single";

        // when
        PersonExtractor1.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.age).isEqualTo(37);
        assertThat(person.height).isEqualTo(1.78);
        assertThat(person.married).isFalse();

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addIntegerProperty("age")
                                                .addNumberProperty("height")
                                                .addBooleanProperty("married")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_missing_data(ChatModel model) {

        // given
        ChatModel spyModel = spy(model);

        MissingDataPersonExtractor personExtractor = AiServices.create(MissingDataPersonExtractor.class, spyModel);

        String text = "Extract the person's information from the following text. Do not include missing fields! "
                + "Text: 'Klaus'";

        // when
        MissingDataPersonExtractor.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.age).isEqualTo(0);
        assertThat(person.height).isNull();
        assertThat(person.married).isFalse();
        assertThat(person.map).isNullOrEmpty();
        assertThat(person.list).isNullOrEmpty();
        assertThat(person.array).isNullOrEmpty();
        assertThat(person.address).isNull();
        if (!isStrictJsonSchemaEnabled(model)) {
            // LLMs in strict JSON schema mode return enums for some reason, even if it is optional and no data
            // available
            assertThat(person.maritalStatus).isNull();
        }
        assertThat(person.localDate).isNull();

        verify(spyModel)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addIntegerProperty("age")
                                                .addNumberProperty("height")
                                                .addBooleanProperty("married")
                                                .addProperty(
                                                        "map",
                                                        JsonObjectSchema.builder()
                                                                .build())
                                                .addProperty(
                                                        "list",
                                                        JsonArraySchema.builder()
                                                                .items(new JsonStringSchema())
                                                                .build())
                                                .addProperty(
                                                        "array",
                                                        JsonArraySchema.builder()
                                                                .items(new JsonStringSchema())
                                                                .build())
                                                .addProperty(
                                                        "address",
                                                        JsonObjectSchema.builder()
                                                                .addStringProperty("street")
                                                                .build())
                                                .addEnumProperty("maritalStatus", List.of("SINGLE", "MARRIED"))
                                                .addProperty(
                                                        "localDate",
                                                        JsonObjectSchema.builder()
                                                                .addIntegerProperty("year")
                                                                .addIntegerProperty("month")
                                                                .addIntegerProperty("day")
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(spyModel).supportedCapabilities();
    }

    protected boolean isStrictJsonSchemaEnabled(ChatModel model) {
        return false;
    }

    interface PersonExtractor2 {

        class Person {

            String name;
            Address shippingAddress;
            Address billingAddress;
        }

        class Address {

            String city;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_nested_pojo(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor2 personExtractor = AiServices.create(PersonExtractor2.class, model);

        String text = "Extract the person's information from the following text. "
                + "Fill in all the fields where the information is available! "
                + "Text: 'Klaus wants a delivery to Langley Falls, but billing address should be New York'";

        // when
        PersonExtractor2.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.shippingAddress.city).isEqualTo("Langley Falls");
        assertThat(person.billingAddress.city).isEqualTo("New York");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "shippingAddress",
                                                        JsonObjectSchema.builder()
                                                                .addStringProperty("city")
                                                                .build())
                                                .addProperty(
                                                        "billingAddress",
                                                        JsonObjectSchema.builder()
                                                                .addStringProperty("city")
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor3 {

        class Person {

            String name;
            MaritalStatus maritalStatus;
        }

        enum MaritalStatus {
            SINGLE,
            MARRIED
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_enum(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor3 personExtractor = AiServices.create(PersonExtractor3.class, model);

        String text = "Extract the person's information from the following text: Klaus is single";

        // when
        PersonExtractor3.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.maritalStatus).isEqualTo(SINGLE);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addEnumProperty("maritalStatus", List.of("SINGLE", "MARRIED"))
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor4 {

        class Person {

            String name;
            String[] favouriteColors;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_array_of_primitives(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor4 personExtractor = AiServices.create(PersonExtractor4.class, model);

        String text = "Extract the person's information from the following text: Klaus likes orange and green";

        // when
        PersonExtractor4.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.favouriteColors).containsExactly("orange", "green");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "favouriteColors",
                                                        JsonArraySchema.builder()
                                                                .items(new JsonStringSchema())
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor5 {

        class Person {

            String name;
            List<String> favouriteColors;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_list_of_primitives(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor5 personExtractor = AiServices.create(PersonExtractor5.class, model);

        String text = "Extract the person's information from the following text: Klaus likes orange and green";

        // when
        PersonExtractor5.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.favouriteColors).containsExactly("orange", "green");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "favouriteColors",
                                                        JsonArraySchema.builder()
                                                                .items(new JsonStringSchema())
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor6 {

        class Person {

            String name;
            Set<String> favouriteColors;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_set_of_primitives(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor6 personExtractor = AiServices.create(PersonExtractor6.class, model);

        String text = "Extract the person's information from the following text: Klaus likes orange and green";

        // when
        PersonExtractor6.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.favouriteColors).containsExactly("orange", "green");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "favouriteColors",
                                                        JsonArraySchema.builder()
                                                                .items(new JsonStringSchema())
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor7 {

        class Person {

            String name;
            Pet[] pets;
        }

        class Pet {

            String name;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_array_of_pojos(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor7 personExtractor = AiServices.create(PersonExtractor7.class, model);

        String text = "Extract the person's information from the following text: Klaus has 2 pets: Peanut and Muffin";

        // when
        PersonExtractor7.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.pets).hasSize(2);
        assertThat(person.pets[0].name).isEqualTo("Peanut");
        assertThat(person.pets[1].name).isEqualTo("Muffin");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "pets",
                                                        JsonArraySchema.builder()
                                                                .items(JsonObjectSchema.builder()
                                                                        .addStringProperty("name")
                                                                        .build())
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor8 {

        class Person {

            String name;
            List<Pet> pets;
        }

        class Pet {

            String name;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_list_of_pojos(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor8 personExtractor = AiServices.create(PersonExtractor8.class, model);

        String text = "Extract the person's information from the following text: Klaus has 2 pets: Peanut and Muffin";

        // when
        PersonExtractor8.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.pets).hasSize(2);
        assertThat(person.pets.get(0).name).isEqualTo("Peanut");
        assertThat(person.pets.get(1).name).isEqualTo("Muffin");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "pets",
                                                        JsonArraySchema.builder()
                                                                .items(JsonObjectSchema.builder()
                                                                        .addStringProperty("name")
                                                                        .build())
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_set_of_pojos(ChatModel model) {

        // given
        model = spy(model);

        PojoSetPersonExtractor personExtractor = AiServices.create(PojoSetPersonExtractor.class, model);

        String text = "Extract the person's information from the following text: Klaus has 2 pets: Peanut and Muffin";

        // when
        PojoSetPerson person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person).isEqualTo(new PojoSetPerson("Klaus", Set.of(new PojoSetPet("Peanut"), new PojoSetPet("Muffin"))));

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "pets",
                                                        JsonArraySchema.builder()
                                                                .items(JsonObjectSchema.builder()
                                                                        .addStringProperty("name")
                                                                        .build())
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor10 {

        class Person {

            String name;
            Group[] groups;
        }

        enum Group {
            A,
            B,
            C
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_array_of_enums(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor10 personExtractor = AiServices.create(PersonExtractor10.class, model);

        String text = "Extract the person's information from the following text: Klaus is assigned to groups A and C";

        // when
        PersonExtractor10.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.groups).containsExactlyInAnyOrder(PersonExtractor10.Group.A, PersonExtractor10.Group.C);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperties(new LinkedHashMap<>() {
                                                    {
                                                        put("name", new JsonStringSchema());
                                                        put(
                                                                "groups",
                                                                JsonArraySchema.builder()
                                                                        .items(JsonEnumSchema.builder()
                                                                                .enumValues("A", "B", "C")
                                                                                .build())
                                                                        .build());
                                                    }
                                                })
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor11 {

        class Person {

            String name;
            List<Group> groups;
        }

        enum Group {
            A,
            B,
            C
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_list_of_enums(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor11 personExtractor = AiServices.create(PersonExtractor11.class, model);

        String text = "Extract the person's information from the following text: Klaus is assigned to groups A and C";

        // when
        PersonExtractor11.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.groups).containsExactlyInAnyOrder(PersonExtractor11.Group.A, PersonExtractor11.Group.C);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperties(new LinkedHashMap<>() {
                                                    {
                                                        put("name", new JsonStringSchema());
                                                        put(
                                                                "groups",
                                                                JsonArraySchema.builder()
                                                                        .items(JsonEnumSchema.builder()
                                                                                .enumValues("A", "B", "C")
                                                                                .build())
                                                                        .build());
                                                    }
                                                })
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor12 {

        class Person {

            String name;
            Set<Group> groups;
        }

        enum Group {
            A,
            B,
            C
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_set_of_enums(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor12 personExtractor = AiServices.create(PersonExtractor12.class, model);

        String text = "Extract the person's information from the following text: Klaus is assigned to groups A and C";

        // when
        PersonExtractor12.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.groups).containsExactlyInAnyOrder(PersonExtractor12.Group.A, PersonExtractor12.Group.C);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperties(new LinkedHashMap<>() {
                                                    {
                                                        put("name", new JsonStringSchema());
                                                        put(
                                                                "groups",
                                                                JsonArraySchema.builder()
                                                                        .items(JsonEnumSchema.builder()
                                                                                .enumValues("A", "B", "C")
                                                                                .build())
                                                                        .build());
                                                    }
                                                })
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor13 {

        class Person {

            String name;
            LocalDate birthDate;
            LocalTime birthTime;
            LocalDateTime birthDateTime;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_local_date_time_fields(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor13 personExtractor = AiServices.create(PersonExtractor13.class, model);

        String text = "Extract the person's information from the following text."
                + "Fill in all the fields where the information is available! "
                + "Text: 'Klaus was born at 14:43 on 12th of August 1976'";

        // when
        PersonExtractor13.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.birthDate).isEqualTo(LocalDate.of(1976, 8, 12));
        assertThat(person.birthTime).isEqualTo(LocalTime.of(14, 43));

        assertThat(person.birthDateTime).isEqualTo(LocalDateTime.of(1976, 8, 12, 14, 43));

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(userMessage(text))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperties(new LinkedHashMap<>() {
                                                    {
                                                        put("name", new JsonStringSchema());
                                                        put(
                                                                "birthDate",
                                                                JsonObjectSchema.builder()
                                                                        .addProperties(new LinkedHashMap<>() {
                                                                            {
                                                                                put("year", new JsonIntegerSchema());
                                                                                put("month", new JsonIntegerSchema());
                                                                                put("day", new JsonIntegerSchema());
                                                                            }
                                                                        })
                                                                        .build());
                                                        put(
                                                                "birthTime",
                                                                JsonObjectSchema.builder()
                                                                        .addProperties(new LinkedHashMap<>() {
                                                                            {
                                                                                put("hour", new JsonIntegerSchema());
                                                                                put("minute", new JsonIntegerSchema());
                                                                                put("second", new JsonIntegerSchema());
                                                                                put("nano", new JsonIntegerSchema());
                                                                            }
                                                                        })
                                                                        .build());
                                                        put(
                                                                "birthDateTime",
                                                                JsonObjectSchema.builder()
                                                                        .addProperties(new LinkedHashMap<>() {
                                                                            {
                                                                                put(
                                                                                        "date",
                                                                                        JsonObjectSchema.builder()
                                                                                                .addProperties(
                                                                                                        new LinkedHashMap<>() {
                                                                                                            {
                                                                                                                put(
                                                                                                                        "year",
                                                                                                                        new JsonIntegerSchema());
                                                                                                                put(
                                                                                                                        "month",
                                                                                                                        new JsonIntegerSchema());
                                                                                                                put(
                                                                                                                        "day",
                                                                                                                        new JsonIntegerSchema());
                                                                                                            }
                                                                                                        })
                                                                                                .build());
                                                                                put(
                                                                                        "time",
                                                                                        JsonObjectSchema.builder()
                                                                                                .addProperties(
                                                                                                        new LinkedHashMap<>() {
                                                                                                            {
                                                                                                                put(
                                                                                                                        "hour",
                                                                                                                        new JsonIntegerSchema());
                                                                                                                put(
                                                                                                                        "minute",
                                                                                                                        new JsonIntegerSchema());
                                                                                                                put(
                                                                                                                        "second",
                                                                                                                        new JsonIntegerSchema());
                                                                                                                put(
                                                                                                                        "nano",
                                                                                                                        new JsonIntegerSchema());
                                                                                                            }
                                                                                                        })
                                                                                                .build());
                                                                            }
                                                                        })
                                                                        .build());
                                                    }
                                                })
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_return_result_with_pojo(ChatModel model) {

        // given
        model = spy(model);

        ResultPersonExtractor personExtractor = AiServices.create(ResultPersonExtractor.class, model);

        String text = "Extract the person's information from the following text: Klaus";

        // when
        Result<ResultPersonExtractor.Person> result = personExtractor.extractPersonFrom(text);
        ResultPersonExtractor.Person person = result.content();

        // then
        assertThat(person.name).isEqualTo("Klaus");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperties(new LinkedHashMap<>() {
                                                    {
                                                        put("name", new JsonStringSchema());
                                                    }
                                                })
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    @EnabledIf("supportsRecursion")
    void should_extract_pojo_with_recursion(ChatModel model) {

        // given
        model = spy(model);

        RecursionPersonExtractor personExtractor = AiServices.create(RecursionPersonExtractor.class, model);

        String text = "Extract the person's information from the following text: "
                + "Francine has 2 children: Steve and Hayley";

        // when
        RecursionPerson person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name()).isEqualTo("Francine");
        assertThat(person.children()).contains(new RecursionPerson("Steve", List.of()));
        assertThat(person.children()).contains(new RecursionPerson("Hayley", List.of()));

        String reference = generateUUIDFrom(RecursionPerson.class.getName());

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "children",
                                                        JsonArraySchema.builder()
                                                                .items(JsonReferenceSchema.builder()
                                                                        .reference(reference)
                                                                        .build())
                                                                .build())
                                                .definitions(Map.of(
                                                        reference,
                                                        JsonObjectSchema.builder()
                                                                .addStringProperty("name")
                                                                .addProperty(
                                                                        "children",
                                                                        JsonArraySchema.builder()
                                                                                .items(JsonReferenceSchema.builder()
                                                                                        .reference(reference)
                                                                                        .build())
                                                                                .build())
                                                                .build()))
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface PersonExtractor16 {

        class Person {

            UUID id;
            String name;
        }

        Person extractPersonFrom(String text);
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_uuid(ChatModel model) {

        // given
        model = spy(model);

        PersonExtractor16 personExtractor = AiServices.create(PersonExtractor16.class, model);

        String text = "Klaus can be identified by the following ID: 567b229a-6b0a-4f1e-9006-448cd9dfbfda\n";

        // when
        PersonExtractor16.Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.id).isEqualTo(UUID.fromString("567b229a-6b0a-4f1e-9006-448cd9dfbfda"));

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("id", "String in a UUID format")
                                                .addStringProperty("name")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    // Primitives

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_boolean_primitive(ChatModel model) {

        // given
        model = spy(model);

        BooleanPrimitiveExtractor booleanExtractor = AiServices.create(BooleanPrimitiveExtractor.class, model);

        String text = "Klaus is a 37-year-old man, 1.78 meters tall, and single.";

        // when
        boolean isAMan = booleanExtractor.isPersonAMan(text);

        // then
        assertThat(isAMan).isTrue();

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract if the person from the following text is a man: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("boolean")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addBooleanProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_boolean_boxed(ChatModel model) {

        // given
        model = spy(model);

        BooleanBoxedExtractor booleanExtractor = AiServices.create(BooleanBoxedExtractor.class, model);

        String text = "Klaus is a 37-year-old man, 1.78 meters tall, and single.";

        // when
        Boolean isAMan = booleanExtractor.isPersonAMan(text);

        // then
        assertThat(isAMan).isTrue();

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract if the person from the following text is a man: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("boolean")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addBooleanProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_int_primitive(ChatModel model) {

        // given
        model = spy(model);

        IntPrimitiveExtractor intExtractor = AiServices.create(IntPrimitiveExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        int numberOfPeople = intExtractor.extractNumberOfPeople(text);

        // then
        assertThat(numberOfPeople).isEqualTo(2);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract number of people mentioned in the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("integer")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addIntegerProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_int_boxed(ChatModel model) {

        // given
        model = spy(model);

        IntegerBoxedExtractor intExtractor = AiServices.create(IntegerBoxedExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        Integer numberOfPeople = intExtractor.extractNumberOfPeople(text);

        // then
        assertThat(numberOfPeople).isEqualTo(2);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract number of people mentioned in the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("integer")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addIntegerProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_long_primitive(ChatModel model) {

        // given
        model = spy(model);

        LongPrimitiveExtractor intExtractor = AiServices.create(LongPrimitiveExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        long numberOfPeople = intExtractor.extractNumberOfPeople(text);

        // then
        assertThat(numberOfPeople).isEqualTo(2);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract number of people mentioned in the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("integer")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addIntegerProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_long_boxed(ChatModel model) {

        // given
        model = spy(model);

        LongBoxedExtractor intExtractor = AiServices.create(LongBoxedExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        Long numberOfPeople = intExtractor.extractNumberOfPeople(text);

        // then
        assertThat(numberOfPeople).isEqualTo(2);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract number of people mentioned in the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("integer")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addIntegerProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_float_primitive(ChatModel model) {

        // given
        model = spy(model);

        FloatPrimitiveExtractor doubleExtractor = AiServices.create(FloatPrimitiveExtractor.class, model);

        String text = "The average temperature of the coldest month is of -0.5 °C";

        // when
        float temperatureInMunich = doubleExtractor.extractTemperatureInMunich(text);

        // then
        assertThat(temperatureInMunich).isEqualTo(-0.5f);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract temperature in Munich from the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("number")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addNumberProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_float_boxed(ChatModel model) {

        // given
        model = spy(model);

        FloatBoxedExtractor doubleExtractor = AiServices.create(FloatBoxedExtractor.class, model);

        String text = "The average temperature of the coldest month is of -0.5 °C";

        // when
        Float temperatureInMunich = doubleExtractor.extractTemperatureInMunich(text);

        // then
        assertThat(temperatureInMunich).isEqualTo(-0.5f);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract temperature in Munich from the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("number")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addNumberProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_double_primitive(ChatModel model) {

        // given
        model = spy(model);

        DoublePrimitiveExtractor doubleExtractor = AiServices.create(DoublePrimitiveExtractor.class, model);

        String text = "The average temperature of the coldest month is of -0.5 °C";

        // when
        double temperatureInMunich = doubleExtractor.extractTemperatureInMunich(text);

        // then
        assertThat(temperatureInMunich).isEqualTo(-0.5);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract temperature in Munich from the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("number")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addNumberProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_double_boxed(ChatModel model) {

        // given
        model = spy(model);

        DoubleBoxedExtractor doubleExtractor = AiServices.create(DoubleBoxedExtractor.class, model);

        String text = "The average temperature of the coldest month is of -0.5 °C";

        // when
        Double temperatureInMunich = doubleExtractor.extractTemperatureInMunich(text);

        // then
        assertThat(temperatureInMunich).isEqualTo(-0.5);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(
                                userMessage("Extract temperature in Munich from the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("number")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addNumberProperty("value")
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    // Lists

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_list_of_pojo(ChatModel model) {

        // given
        model = spy(model);

        PeopleListExtractor peopleExtractor = AiServices.create(PeopleListExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        List<PeopleListExtractor.Person> people = peopleExtractor.extractPeopleFrom(text);

        // then
        assertThat(people.get(0).name).isEqualTo("Klaus");
        assertThat(people.get(0).age).isEqualTo(37);
        assertThat(people.get(0).height).isEqualTo(1.78);
        assertThat(people.get(0).married).isFalse();

        assertThat(people.get(1).name).isEqualTo("Franny");
        assertThat(people.get(1).age).isEqualTo(35);
        assertThat(people.get(1).height).isEqualTo(1.65);
        assertThat(people.get(1).married).isTrue();

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("List_of_Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonObjectSchema.builder()
                                                                        .addStringProperty("name")
                                                                        .addIntegerProperty("age")
                                                                        .addNumberProperty("height")
                                                                        .addBooleanProperty("married")
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_return_result_with_list_of_pojo(ChatModel model) {

        // given
        model = spy(model);

        PeopleResultListExtractor personExtractor = AiServices.create(PeopleResultListExtractor.class, model);

        String text = "Extract the person's information from the following text: Klaus and Francine";

        // when
        Result<List<PeopleResultListExtractor.Person>> result = personExtractor.extractPeopleFrom(text);
        List<PeopleResultListExtractor.Person> people = result.content();

        // then
        assertThat(people).hasSize(2);
        assertThat(people.get(0).name).isEqualTo("Klaus");
        assertThat(people.get(1).name).isEqualTo("Francine");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(List.of(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("List_of_Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonObjectSchema.builder()
                                                                        .addStringProperty("name")
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_list_of_strings(ChatModel model) {

        // given
        model = spy(model);

        ListOfStringsPrimitiveExtractor listOfStringsExtractor = AiServices.create(ListOfStringsPrimitiveExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        List<String> names = listOfStringsExtractor.extractPeopleNames(text);

        // then
        assertThat(names).hasSize(2);
        assertThat(names.get(0)).contains("Klaus");
        assertThat(names.get(1)).contains("Franny");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(
                                singletonList(userMessage("Extract names of people from the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("List_of_String")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonStringSchema.builder()
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    // Sets

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_set_of_strings(ChatModel model) {

        // given
        model = spy(model);

        SetOfStringsPrimitiveExtractor setOfStringsExtractor = AiServices.create(SetOfStringsPrimitiveExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        Set<String> names = setOfStringsExtractor.extractSetOfPeopleNames(text);

        // then
        assertThat(names).hasSize(2).contains("Klaus").contains("Franny");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(
                                singletonList(userMessage("Extract names of people from the following text: " + text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Set_of_String")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonStringSchema.builder()
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_set_of_pojo(ChatModel model) {

        // given
        model = spy(model);

        PojoSetPrimitiveExtractor pojoSetExtractor = AiServices.create(PojoSetPrimitiveExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single. "
                + "Franny is 35 years old, 1.65m height and married.";

        // when
        Set<PojoSetPrimitiveExtractor.Person> people = pojoSetExtractor.extractSetOfPojoFrom(text);

        // then
        assertThat(people)
                .hasSize(2)
                .anyMatch(person -> person.name.equals("Klaus")
                        && person.age == 37
                        && person.height.equals(1.78)
                        && !person.married)
                .anyMatch(person -> person.name.equals("Franny")
                        && person.age == 35
                        && person.height.equals(1.65)
                        && person.married);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Set_of_Person")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonObjectSchema.builder()
                                                                        .addStringProperty("name")
                                                                        .addIntegerProperty("age")
                                                                        .addNumberProperty("height")
                                                                        .addBooleanProperty("married")
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    // Enums

    enum MaritalStatus2 {
        SINGLE,
        MARRIED
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_enum(ChatModel model) {

        // given
        model = spy(model);

        EnumPrimitiveExtractor enumExtractor = AiServices.create(EnumPrimitiveExtractor.class, model);

        String text = "Klaus is 37 years old, 1.78m height and single.";

        // when
        MaritalStatus2 maritalStatus = enumExtractor.extractEnumFrom(text);

        // then
        assertThat(maritalStatus).isEqualTo(MaritalStatus2.SINGLE);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("MaritalStatus")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addEnumProperty("value", List.of("SINGLE", "MARRIED"))
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_list_of_enums(ChatModel model) {

        // given

        model = spy(model);

        EnumListPrimitiveExtractor enumListExtractor = AiServices.create(EnumListPrimitiveExtractor.class, model);

        String text =
                "Klaus is 37 years old, 1.78m height and single. " + "Franny is 35 years old, 1.65m height and married."
                        + "Staniel is 33 years old, 1.70m height and married.";

        // when
        List<MaritalStatus2> maritalStatuses = enumListExtractor.extractListOfEnumsFrom(text);

        // then
        assertThat(maritalStatuses).containsExactly(MaritalStatus2.SINGLE, MaritalStatus2.MARRIED, MaritalStatus2.MARRIED);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("List_of_MaritalStatus")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonEnumSchema.builder()
                                                                        .enumValues("SINGLE", "MARRIED")
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    enum Weather {
        SUNNY,
        RAINY,
        CLOUDY,
        WINDY
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_set_of_enums(ChatModel model) {

        // given

        model = spy(model);

        EnumSetPrimitiveExtractor enumSetExtractor = AiServices.create(EnumSetPrimitiveExtractor.class, model);

        String text = "The weather in Berlin was sunny and windy." + " Paris experienced rainy and cloudy weather."
                + " New York had cloudy and windy weather.";

        // when
        Set<Weather> weatherCharacteristics = enumSetExtractor.extractSetOfEnumsFrom(text);

        // then
        assertThat(weatherCharacteristics)
                .containsExactlyInAnyOrder(Weather.SUNNY, Weather.WINDY, Weather.RAINY, Weather.CLOUDY);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Set_of_Weather")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonEnumSchema.builder()
                                                                        .enumValues("SUNNY", "RAINY", "CLOUDY", "WINDY")
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    protected boolean supportsRecursion() {
        return false;
    }

    // Polymorphic types

    interface Animal {}

    static class Dog implements Animal {
        private final String name;
        private final String breed;

        public Dog(String name, String breed) {
            this.name = name;
            this.breed = breed;
        }

        public String name() { return name; }
        public String breed() { return breed; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dog dog = (Dog) o;
            return Objects.equals(name, dog.name) && Objects.equals(breed, dog.breed);
        }

        @Override
        public int hashCode() { return Objects.hash(name, breed); }

        @Override
        public String toString() { return "Dog[name=" + name + ", breed=" + breed + "]"; }
    }

    static class Cat implements Animal {
        private final String name;
        private final boolean indoor;

        public Cat(String name, boolean indoor) {
            this.name = name;
            this.indoor = indoor;
        }

        public String name() { return name; }
        public boolean indoor() { return indoor; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cat cat = (Cat) o;
            return indoor == cat.indoor && Objects.equals(name, cat.name);
        }

        @Override
        public int hashCode() { return Objects.hash(name, indoor); }

        @Override
        public String toString() { return "Cat[name=" + name + ", indoor=" + indoor + "]"; }
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_polymorphic_type(ChatModel model) {

        // given
        model = spy(model);

        AnimalPolymorphicExtractor extractor = AiServices.create(AnimalPolymorphicExtractor.class, model);

        String text = "Rex is a Labrador dog";

        // when
        Animal animal = extractor.extractAnimalFrom(text);

        // then
        assertThat(animal).isInstanceOf(Dog.class);
        Dog dog = (Dog) animal;
        assertThat(dog.name()).isEqualToIgnoringCase("Rex");
        assertThat(dog.breed()).containsIgnoringCase("Labrador");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Animal")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "value",
                                                        JsonAnyOfSchema.builder()
                                                                .description("Animal")
                                                                .anyOf(List.of(
                                                                        JsonObjectSchema.builder()
                                                                                .description("Dog")
                                                                                .addProperty(
                                                                                        "type",
                                                                                        JsonEnumSchema.builder()
                                                                                                .enumValues("Dog")
                                                                                                .build())
                                                                                .addStringProperty("name")
                                                                                .addStringProperty("breed")
                                                                                .required("type")
                                                                                .build(),
                                                                        JsonObjectSchema.builder()
                                                                                .description("Cat")
                                                                                .addProperty(
                                                                                        "type",
                                                                                        JsonEnumSchema.builder()
                                                                                                .enumValues("Cat")
                                                                                                .build())
                                                                                .addStringProperty("name")
                                                                                .addBooleanProperty("indoor")
                                                                                .required("type")
                                                                                .build()))
                                                                .build())
                                                .required("value")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_list_of_polymorphic_types(ChatModel model) {

        // given
        model = spy(model);

        AnimalsPolymorphicExtractor extractor = AiServices.create(AnimalsPolymorphicExtractor.class, model);

        String text = "Rex is a Labrador. Whiskers is an indoor cat.";

        // when
        List<Animal> animals = extractor.extractAnimalsFrom(text);

        // then
        assertThat(animals).hasSize(2);
        assertThat(animals).hasAtLeastOneElementOfType(Dog.class);
        assertThat(animals).hasAtLeastOneElementOfType(Cat.class);

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("List_of_Animal")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addProperty(
                                                        "values",
                                                        JsonArraySchema.builder()
                                                                .items(JsonAnyOfSchema.builder()
                                                                        .description("Animal")
                                                                        .anyOf(List.of(
                                                                                JsonObjectSchema.builder()
                                                                                        .description("Dog")
                                                                                        .addProperty(
                                                                                                "type",
                                                                                                JsonEnumSchema.builder()
                                                                                                        .enumValues(
                                                                                                                "Dog")
                                                                                                        .build())
                                                                                        .addStringProperty("name")
                                                                                        .addStringProperty("breed")
                                                                                        .required("type")
                                                                                        .build(),
                                                                                JsonObjectSchema.builder()
                                                                                        .description("Cat")
                                                                                        .addProperty(
                                                                                                "type",
                                                                                                JsonEnumSchema.builder()
                                                                                                        .enumValues(
                                                                                                                "Cat")
                                                                                                        .build())
                                                                                        .addStringProperty("name")
                                                                                        .addBooleanProperty("indoor")
                                                                                        .required("type")
                                                                                        .build()))
                                                                        .build())
                                                                .build())
                                                .required("values")
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    @ParameterizedTest
    @MethodSource("models")
    protected void should_extract_pojo_with_nested_polymorphic_field(ChatModel model) {

        // given
        model = spy(model);

        OwnerPolymorphicExtractor extractor = AiServices.create(OwnerPolymorphicExtractor.class, model);

        String text = "Alice owns a Labrador dog named Rex.";

        // when
        PolymorphicOwner owner = extractor.extractOwnerFrom(text);

        // then
        assertThat(owner.name()).isEqualToIgnoringCase("Alice");
        assertThat(owner.pet()).isInstanceOf(Dog.class);
        Dog dog = (Dog) owner.pet();
        assertThat(dog.name()).isEqualToIgnoringCase("Rex");
        assertThat(dog.breed()).containsIgnoringCase("Labrador");

        verify(model)
                .chat(ChatRequest.builder()
                        .messages(singletonList(userMessage(text)))
                        .responseFormat(ResponseFormat.builder()
                                .type(JSON)
                                .jsonSchema(JsonSchema.builder()
                                        .name("Owner")
                                        .rootElement(JsonObjectSchema.builder()
                                                .addStringProperty("name")
                                                .addProperty(
                                                        "pet",
                                                        JsonAnyOfSchema.builder()
                                                                .description("Animal")
                                                                .anyOf(List.of(
                                                                        JsonObjectSchema.builder()
                                                                                .description("Dog")
                                                                                .addProperty(
                                                                                        "type",
                                                                                        JsonEnumSchema.builder()
                                                                                                .enumValues("Dog")
                                                                                                .build())
                                                                                .addStringProperty("name")
                                                                                .addStringProperty("breed")
                                                                                .required("type")
                                                                                .build(),
                                                                        JsonObjectSchema.builder()
                                                                                .description("Cat")
                                                                                .addProperty(
                                                                                        "type",
                                                                                        JsonEnumSchema.builder()
                                                                                                .enumValues("Cat")
                                                                                                .build())
                                                                                .addStringProperty("name")
                                                                                .addBooleanProperty("indoor")
                                                                                .required("type")
                                                                                .build()))
                                                                .build())
                                                .build())
                                        .build())
                                .build())
                        .build());
        verify(model).supportedCapabilities();
    }

    interface ArithmeticExpression {}

    static class Constant implements ArithmeticExpression {
        private final int value;

        public Constant(int value) {
            this.value = value;
        }

        public int value() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Constant constant = (Constant) o;
            return value == constant.value;
        }

        @Override
        public int hashCode() { return Objects.hash(value); }

        @Override
        public String toString() { return "Constant[value=" + value + "]"; }
    }

    static class Addition implements ArithmeticExpression {
        private final ArithmeticExpression left;
        private final ArithmeticExpression right;

        public Addition(ArithmeticExpression left, ArithmeticExpression right) {
            this.left = left;
            this.right = right;
        }

        public ArithmeticExpression left() { return left; }
        public ArithmeticExpression right() { return right; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Addition addition = (Addition) o;
            return Objects.equals(left, addition.left) && Objects.equals(right, addition.right);
        }

        @Override
        public int hashCode() { return Objects.hash(left, right); }

        @Override
        public String toString() { return "Addition[left=" + left + ", right=" + right + "]"; }
    }

    @ParameterizedTest
    @MethodSource("models")
    @EnabledIf("supportsRecursion")
    protected void should_extract_recursive_polymorphic_type(ChatModel model) {

        // given
        ExpressionRecursiveExtractor extractor = AiServices.create(ExpressionRecursiveExtractor.class, model);

        // when
        ArithmeticExpression expression = extractor.extractFrom(
                "Represent the literal expression 1+2+3 as a syntax tree. Do NOT simplify or evaluate. "
                        + "Use a left-associative tree: Addition(Addition(Constant(1), Constant(2)), Constant(3)).");

        // then
        assertThat(expression).isInstanceOf(Addition.class);
        List<Integer> leaves = new ArrayList<>();
        collectLeaves(expression, leaves);
        assertThat(leaves).containsExactlyInAnyOrder(1, 2, 3);
    }

    private static void collectLeaves(ArithmeticExpression expr, List<Integer> leaves) {
        if (expr instanceof Constant) {
            Constant c = (Constant) expr;
            leaves.add(c.value());
        } else if (expr instanceof Addition) {
            Addition a = (Addition) expr;
            collectLeaves(a.left(), leaves);
            collectLeaves(a.right(), leaves);
        }
    }
}
