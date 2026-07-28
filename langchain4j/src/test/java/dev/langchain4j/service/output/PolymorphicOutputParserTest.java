package dev.langchain4j.service.output;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonReferenceSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.output.structured.Description;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PolymorphicOutputParserTest {

    interface Animal {
    }

    static final class Dog implements Animal {
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
            if (!(o instanceof Dog)) return false;
            Dog dog = (Dog) o;
            return java.util.Objects.equals(name, dog.name) && java.util.Objects.equals(breed, dog.breed);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, breed); }

        @Override
        public String toString() { return "Dog[name=" + name + ", breed=" + breed + "]"; }
    }

    static final class Cat implements Animal {
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
            if (!(o instanceof Cat)) return false;
            Cat cat = (Cat) o;
            return indoor == cat.indoor && java.util.Objects.equals(name, cat.name);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, indoor); }

        @Override
        public String toString() { return "Cat[name=" + name + ", indoor=" + indoor + "]"; }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Square.class, name = "square"),
            @JsonSubTypes.Type(value = Circle.class, name = "circle")
    })
    interface Shape {
    }

    static class Square implements Shape {
        double side;

        Square() {
        }

        Square(double side) {
            this.side = side;
        }
    }

    static class Circle implements Shape {
        double radius;

        Circle() {
        }

        Circle(double radius) {
            this.radius = radius;
        }
    }

    @Description("A pet that lives in your home")
    interface Pet {
    }

    interface Vehicle {
    }

    static final class Truck implements Vehicle {
        private final String type;
        private final int wheels;

        public Truck(String type, int wheels) {
            this.type = type;
            this.wheels = wheels;
        }

        public String type() { return type; }
        public int wheels() { return wheels; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Truck)) return false;
            Truck truck = (Truck) o;
            return wheels == truck.wheels && java.util.Objects.equals(type, truck.type);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(type, wheels); }

        @Override
        public String toString() { return "Truck[type=" + type + ", wheels=" + wheels + "]"; }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
    @JsonSubTypes(@JsonSubTypes.Type(Coffee.class))
    interface Beverage {
    }

    static class Coffee implements Beverage {
        String origin;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonSubTypes(@JsonSubTypes.Type(value = Apple.class, name = "apple"))
    interface Fruit {
    }

    static class Apple implements Fruit {
        String variety;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME)
    @JsonSubTypes(@JsonSubTypes.Type(Pizza.class))
    interface Food {
    }

    static class Pizza implements Food {
        String topping;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = Wrench.class)
    @JsonSubTypes({@JsonSubTypes.Type(value = Hammer.class, name = "hammer")})
    interface Tool {
    }

    static class Hammer implements Tool {
        double weightKg;
    }

    static class Wrench implements Tool {
        int sizeMm;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind", visible = true)
    @JsonSubTypes({@JsonSubTypes.Type(value = Sword.class, name = "sword")})
    interface Weapon {
    }

    static class Sword implements Weapon {
        String kind; // intentionally collides; visible=true makes it OK
        int blade_length_cm;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "category")
    @JsonSubTypes({@JsonSubTypes.Type(value = HardCover.class, name = "hard")})
    interface Book {
    }

    static class HardCover implements Book {
        String category; // existing field used by Jackson as the type id
        String title;
    }

    static final class Hamster implements Pet {
        private final String name;
        private final double weightGrams;

        public Hamster(String name, double weightGrams) {
            this.name = name;
            this.weightGrams = weightGrams;
        }

        public String name() { return name; }
        public double weightGrams() { return weightGrams; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Hamster)) return false;
            Hamster hamster = (Hamster) o;
            return Double.compare(hamster.weightGrams, weightGrams) == 0 && java.util.Objects.equals(name, hamster.name);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, weightGrams); }

        @Override
        public String toString() { return "Hamster[name=" + name + ", weightGrams=" + weightGrams + "]"; }
    }

    @Description("A talking bird that can mimic human speech")
    static final class Parrot implements Pet {
        private final String name;
        private final int vocabulary;

        public Parrot(String name, int vocabulary) {
            this.name = name;
            this.vocabulary = vocabulary;
        }

        public String name() { return name; }
        public int vocabulary() { return vocabulary; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Parrot)) return false;
            Parrot parrot = (Parrot) o;
            return vocabulary == parrot.vocabulary && java.util.Objects.equals(name, parrot.name);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, vocabulary); }

        @Override
        public String toString() { return "Parrot[name=" + name + ", vocabulary=" + vocabulary + "]"; }
    }

    static final class Owner {
        private final String name;
        private final Animal pet;

        public Owner(String name, Animal pet) {
            this.name = name;
            this.pet = pet;
        }

        public String name() { return name; }
        public Animal pet() { return pet; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Owner)) return false;
            Owner owner = (Owner) o;
            return java.util.Objects.equals(name, owner.name) && java.util.Objects.equals(pet, owner.pet);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, pet); }

        @Override
        public String toString() { return "Owner[name=" + name + ", pet=" + pet + "]"; }
    }

    interface Bird {
    }

    @JsonTypeName("eagle")
    static final class Eagle implements Bird {
        private final double wingspanMeters;

        public Eagle(double wingspanMeters) { this.wingspanMeters = wingspanMeters; }

        public double wingspanMeters() { return wingspanMeters; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Eagle)) return false;
            Eagle eagle = (Eagle) o;
            return Double.compare(eagle.wingspanMeters, wingspanMeters) == 0;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(wingspanMeters); }

        @Override
        public String toString() { return "Eagle[wingspanMeters=" + wingspanMeters + "]"; }
    }

    @JsonTypeName("sparrow")
    static final class Sparrow implements Bird {
        private final boolean migratory;

        public Sparrow(boolean migratory) { this.migratory = migratory; }

        public boolean migratory() { return migratory; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Sparrow)) return false;
            Sparrow sparrow = (Sparrow) o;
            return migratory == sparrow.migratory;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(migratory); }

        @Override
        public String toString() { return "Sparrow[migratory=" + migratory + "]"; }
    }

    interface Tree {
    }

    // No annotation — should fall back to simpleName "Oak"
    static final class Oak implements Tree {
        private final int rings;

        public Oak(int rings) { this.rings = rings; }

        public int rings() { return rings; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Oak)) return false;
            Oak oak = (Oak) o;
            return rings == oak.rings;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(rings); }

        @Override
        public String toString() { return "Oak[rings=" + rings + "]"; }
    }

    @JsonTypeName("pine")
    static final class Pine implements Tree {
        private final int needles;

        public Pine(int needles) { this.needles = needles; }

        public int needles() { return needles; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pine)) return false;
            Pine pine = (Pine) o;
            return needles == pine.needles;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(needles); }

        @Override
        public String toString() { return "Pine[needles=" + needles + "]"; }
    }

    @JsonSubTypes(@JsonSubTypes.Type(value = ElectricCar.class, name = "tesla"))
    interface Car {
    }

    // @JsonSubTypes.Type(name="tesla") on the base should win over @JsonTypeName here.
    @JsonTypeName("losing-name")
    static final class ElectricCar implements Car {
        private final int range;

        public ElectricCar(int range) { this.range = range; }

        public int range() { return range; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ElectricCar)) return false;
            ElectricCar that = (ElectricCar) o;
            return range == that.range;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(range); }

        @Override
        public String toString() { return "ElectricCar[range=" + range + "]"; }
    }

    abstract static class Plant {

    static final class Rose extends Plant {
        String color;
    }

    static final class Cactus extends Plant {
        int height;
    }
    }

    @JsonSubTypes({
            @JsonSubTypes.Type(value = Email.class, name = "email"),
            @JsonSubTypes.Type(value = SmsMessage.class, name = "sms")
    })
    abstract static class Notification {
    }

    static class Email extends Notification {
        String subject;
    }

    static class SmsMessage extends Notification {
        String phoneNumber;
    }

    @JsonSubTypes({
            @JsonSubTypes.Type(value = Pdf.class, name = "pdf"),
            @JsonSubTypes.Type(value = Html.class, name = "html")
    })
    interface Document {
    }

    static class Pdf implements Document {
        int pages;
    }

    static class Html implements Document {
        String url;
    }

    interface ExpressionNode {
    }

    static final class Literal implements ExpressionNode {
        private final int value;

        public Literal(int value) { this.value = value; }

        public int value() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Literal)) return false;
            Literal literal = (Literal) o;
            return value == literal.value;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(value); }

        @Override
        public String toString() { return "Literal[value=" + value + "]"; }
    }

    static final class BinaryOp implements ExpressionNode {
        private final String operator;
        private final ExpressionNode left;
        private final ExpressionNode right;

        public BinaryOp(String operator, ExpressionNode left, ExpressionNode right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        public String operator() { return operator; }
        public ExpressionNode left() { return left; }
        public ExpressionNode right() { return right; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BinaryOp)) return false;
            BinaryOp binaryOp = (BinaryOp) o;
            return java.util.Objects.equals(operator, binaryOp.operator)
                    && java.util.Objects.equals(left, binaryOp.left)
                    && java.util.Objects.equals(right, binaryOp.right);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(operator, left, right); }

        @Override
        public String toString() { return "BinaryOp[operator=" + operator + ", left=" + left + ", right=" + right + "]"; }
    }

    // Multi-level sealed hierarchy: Transport permits LandTransport, Plane;
    // LandTransport itself is sealed and permits Bus, Train.
    interface Transport {
    }

    interface LandTransport extends Transport {
    }

    static final class Bus implements LandTransport {
        private final int seats;

        public Bus(int seats) { this.seats = seats; }

        public int seats() { return seats; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Bus)) return false;
            Bus bus = (Bus) o;
            return seats == bus.seats;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(seats); }

        @Override
        public String toString() { return "Bus[seats=" + seats + "]"; }
    }

    static final class Train implements LandTransport {
        private final int cars;

        public Train(int cars) { this.cars = cars; }

        public int cars() { return cars; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Train)) return false;
            Train train = (Train) o;
            return cars == train.cars;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(cars); }

        @Override
        public String toString() { return "Train[cars=" + cars + "]"; }
    }

    static final class Plane implements Transport {
        private final int wingspan;

        public Plane(int wingspan) { this.wingspan = wingspan; }

        public int wingspan() { return wingspan; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Plane)) return false;
            Plane plane = (Plane) o;
            return wingspan == plane.wingspan;
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(wingspan); }

        @Override
        public String toString() { return "Plane[wingspan=" + wingspan + "]"; }
    }

    @Test
    void multi_level_sealed_hierarchy_is_flattened_to_concrete_subtypes() {

        // findConcreteSubtypes walks through the intermediate sealed `LandTransport` and returns
        // only the leaf concrete classes.
        List<Class<?>> concrete = dev.langchain4j.internal.PolymorphicTypes.findConcreteSubtypes(Transport.class);
        assertThat(concrete).containsExactly(Bus.class, Train.class, Plane.class);

        // The resulting anyOf is flat — one option per leaf, no nested anyOf.
        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Transport.class).jsonSchema().get().rootElement();
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        assertThat(anyOf.anyOf()).hasSize(3);
        assertThat(anyOf.anyOf()).allMatch(JsonObjectSchema.class::isInstance);
    }

    @Test
    void multi_level_sealed_hierarchy_parses_a_leaf_subtype() {

        Transport transport = new PojoOutputParser<>(Transport.class)
                .parse("{\"value\":{\"type\":\"Train\",\"cars\":12}}");

        assertThat(transport).isInstanceOf(Train.class);
        assertThat(((Train) transport).cars()).isEqualTo(12);
    }

    @Test
    void recursive_polymorphic_schema_is_compact_and_uses_refs() {

        JsonSchema actual = new PojoOutputParser<>(ExpressionNode.class).jsonSchema().get();

        String reference = dev.langchain4j.internal.Utils.generateUUIDFrom(ExpressionNode.class.getName());
        JsonReferenceSchema selfRef = JsonReferenceSchema.builder().reference(reference).build();

        JsonSchema expected = JsonSchema.builder()
                .name("ExpressionNode")
                .rootElement(JsonObjectSchema.builder()
                        .addProperty("value", selfRef)
                        .required("value")
                        .definitions(Collections.singletonMap(
                                reference,
                                JsonAnyOfSchema.builder()
                                        .description("ExpressionNode")
                                        .anyOf(Arrays.asList(
                                                JsonObjectSchema.builder()
                                                        .description("Literal")
                                                        .addProperty(
                                                                "type",
                                                                JsonEnumSchema.builder()
                                                                        .enumValues("Literal")
                                                                        .build())
                                                        .addIntegerProperty("value")
                                                        .required("type")
                                                        .build(),
                                                JsonObjectSchema.builder()
                                                        .description("BinaryOp")
                                                        .addProperty(
                                                                "type",
                                                                JsonEnumSchema.builder()
                                                                        .enumValues("BinaryOp")
                                                                        .build())
                                                        .addStringProperty("operator")
                                                        .addProperty("left", selfRef)
                                                        .addProperty("right", selfRef)
                                                        .required("type")
                                                        .build()))
                                        .build()))
                        .build())
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void recursive_polymorphic_parses_correctly() {

        // (1 + 2) * 3
        ExpressionNode parsed = new PojoOutputParser<>(ExpressionNode.class).parse(
                "{\"value\":{\"type\":\"BinaryOp\",\"operator\":\"*\",\n"
                + "  \"left\": {\"type\":\"BinaryOp\",\"operator\":\"+\",\n"
                + "            \"left\":{\"type\":\"Literal\",\"value\":1},\n"
                + "            \"right\":{\"type\":\"Literal\",\"value\":2}},\n"
                + "  \"right\":{\"type\":\"Literal\",\"value\":3}}}");

        assertThat(parsed).isInstanceOf(BinaryOp.class);
        BinaryOp outer = (BinaryOp) parsed;
        assertThat(outer.operator()).isEqualTo("*");
        assertThat(outer.right()).isEqualTo(new Literal(3));
        assertThat(outer.left()).isInstanceOf(BinaryOp.class);
        BinaryOp inner = (BinaryOp) outer.left();
        assertThat(inner.left()).isEqualTo(new Literal(1));
        assertThat(inner.right()).isEqualTo(new Literal(2));
    }

    static final class Container {
        private final Animal pet;
        private final Tree tree;

        public Container(Animal pet, Tree tree) {
            this.pet = pet;
            this.tree = tree;
        }

        public Animal pet() { return pet; }
        public Tree tree() { return tree; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Container)) return false;
            Container container = (Container) o;
            return java.util.Objects.equals(pet, container.pet) && java.util.Objects.equals(tree, container.tree);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(pet, tree); }

        @Override
        public String toString() { return "Container[pet=" + pet + ", tree=" + tree + "]"; }
    }

    interface SingleVariant {
    }

    static final class OnlyOne implements SingleVariant {
        private final String value;

        public OnlyOne(String value) { this.value = value; }

        public String value() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OnlyOne)) return false;
            OnlyOne onlyOne = (OnlyOne) o;
            return java.util.Objects.equals(value, onlyOne.value);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(value); }

        @Override
        public String toString() { return "OnlyOne[value=" + value + "]"; }
    }

    interface Action {
    }

    static final class Ping implements Action {
        public Ping() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return o instanceof Ping;
        }

        @Override
        public int hashCode() { return 0; }

        @Override
        public String toString() { return "Ping[]"; }
    }

    static final class Print implements Action {
        private final String message;

        public Print(String message) { this.message = message; }

        public String message() { return message; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Print)) return false;
            Print print = (Print) o;
            return java.util.Objects.equals(message, print.message);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(message); }

        @Override
        public String toString() { return "Print[message=" + message + "]"; }
    }

    static final class OwnerWithDescribedPet {
        private final String name;
        private final Animal pet;

        public OwnerWithDescribedPet(String name, Animal pet) {
            this.name = name;
            this.pet = pet;
        }

        public String name() { return name; }
        public Animal pet() { return pet; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OwnerWithDescribedPet)) return false;
            OwnerWithDescribedPet that = (OwnerWithDescribedPet) o;
            return java.util.Objects.equals(name, that.name) && java.util.Objects.equals(pet, that.pet);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, pet); }

        @Override
        public String toString() { return "OwnerWithDescribedPet[name=" + name + ", pet=" + pet + "]"; }
    }

    @Test
    void sealed_with_single_subtype_works() {

        Optional<JsonSchema> schema = new PojoOutputParser<>(SingleVariant.class).jsonSchema();
        assertThat(schema).isPresent();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema)
                ((JsonObjectSchema) schema.get().rootElement()).properties().get("value");
        assertThat(anyOf.anyOf()).hasSize(1);

        SingleVariant parsed = new PojoOutputParser<>(SingleVariant.class)
                .parse("{\"value\":{\"type\":\"OnlyOne\",\"value\":\"hi\"}}");
        assertThat(parsed).isInstanceOf(OnlyOne.class);
        assertThat(((OnlyOne) parsed).value()).isEqualTo("hi");
    }

    @Test
    void field_level_Description_on_polymorphic_field_flows_into_anyOf_description() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(OwnerWithDescribedPet.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema petAnyOf = (JsonAnyOfSchema) root.properties().get("pet");
        assertThat(petAnyOf.description()).isEqualTo("The owner's pet, must be either a dog or a cat");
    }

    static final class OwnerWithDescribedPetAlsoOnBase {
        private final String name;
        private final Pet pet;

        public OwnerWithDescribedPetAlsoOnBase(String name, Pet pet) {
            this.name = name;
            this.pet = pet;
        }

        public String name() { return name; }
        public Pet pet() { return pet; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OwnerWithDescribedPetAlsoOnBase)) return false;
            OwnerWithDescribedPetAlsoOnBase that = (OwnerWithDescribedPetAlsoOnBase) o;
            return java.util.Objects.equals(name, that.name) && java.util.Objects.equals(pet, that.pet);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(name, pet); }

        @Override
        public String toString() { return "OwnerWithDescribedPetAlsoOnBase[name=" + name + ", pet=" + pet + "]"; }
    }

    static final class Adoption {
        private final Owner owner;
        private final List<Animal> petsAdopted;

        public Adoption(Owner owner, List<Animal> petsAdopted) {
            this.owner = owner;
            this.petsAdopted = petsAdopted;
        }

        public Owner owner() { return owner; }
        public List<Animal> petsAdopted() { return petsAdopted; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Adoption)) return false;
            Adoption adoption = (Adoption) o;
            return java.util.Objects.equals(owner, adoption.owner) && java.util.Objects.equals(petsAdopted, adoption.petsAdopted);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(owner, petsAdopted); }

        @Override
        public String toString() { return "Adoption[owner=" + owner + ", petsAdopted=" + petsAdopted + "]"; }
    }

    static final class Reservation {
        private final String customer;
        private final Shape preferredShape;

        public Reservation(String customer, Shape preferredShape) {
            this.customer = customer;
            this.preferredShape = preferredShape;
        }

        public String customer() { return customer; }
        public Shape preferredShape() { return preferredShape; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Reservation)) return false;
            Reservation that = (Reservation) o;
            return java.util.Objects.equals(customer, that.customer) && java.util.Objects.equals(preferredShape, that.preferredShape);
        }

        @Override
        public int hashCode() { return java.util.Objects.hash(customer, preferredShape); }

        @Override
        public String toString() { return "Reservation[customer=" + customer + ", preferredShape=" + preferredShape + "]"; }
    }

    @Test
    void field_level_Description_wins_over_base_type_Description() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(OwnerWithDescribedPetAlsoOnBase.class)
                        .jsonSchema()
                        .get()
                        .rootElement();

        JsonAnyOfSchema petAnyOf = (JsonAnyOfSchema) root.properties().get("pet");
        // Pet has @Description("A pet that lives in your home"), but the field's @Description wins
        assertThat(petAnyOf.description()).isEqualTo("Field-level description wins");
    }

    @Test
    void empty_subtype_works() {

        Optional<JsonSchema> schema = new PojoOutputParser<>(Action.class).jsonSchema();
        assertThat(schema).isPresent();

        Action parsed = new PojoOutputParser<>(Action.class).parse("{\"value\":{\"type\":\"Ping\"}}");
        assertThat(parsed).isInstanceOf(Ping.class);
    }

    @Test
    void sealed_class_is_supported_without_annotations() {

        Optional<JsonSchema> schemaOpt = new PojoOutputParser<>(Plant.class).jsonSchema();
        assertThat(schemaOpt).isPresent();

        JsonObjectSchema root = (JsonObjectSchema) schemaOpt.get().rootElement();
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        assertThat(anyOf.anyOf()).hasSize(2);

        Plant plant = new PojoOutputParser<>(Plant.class)
                .parse("{\"value\":{\"type\":\"Rose\",\"color\":\"red\"}}");
        assertThat(plant).isInstanceOf(Plant.Rose.class);
        assertThat(((Plant.Rose) plant).color).isEqualTo("red");
    }

    @Test
    void abstract_class_with_JsonSubTypes_is_supported() {

        Optional<JsonSchema> schemaOpt = new PojoOutputParser<>(Notification.class).jsonSchema();
        assertThat(schemaOpt).isPresent();

        JsonObjectSchema root = (JsonObjectSchema) schemaOpt.get().rootElement();
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        assertThat(anyOf.anyOf()).hasSize(2);

        Notification notification = new PojoOutputParser<>(Notification.class)
                .parse("{\"value\":{\"type\":\"email\",\"subject\":\"hello\"}}");
        assertThat(notification).isInstanceOf(Email.class);
        assertThat(((Email) notification).subject).isEqualTo("hello");
    }

    interface NotPolymorphicInterface {
    }

    abstract static class NotPolymorphicAbstractClass {
    }

    @Test
    void plain_interface_without_subtypes_fails_with_clear_message() {

        assertThatThrownBy(() -> new PojoOutputParser<>(NotPolymorphicInterface.class).jsonSchema())
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("NotPolymorphicInterface")
                .hasMessageContaining("sealed")
                .hasMessageContaining("@JsonSubTypes");
    }

    @Test
    void plain_abstract_class_without_subtypes_fails_with_clear_message() {

        assertThatThrownBy(() -> new PojoOutputParser<>(NotPolymorphicAbstractClass.class).jsonSchema())
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("NotPolymorphicAbstractClass")
                .hasMessageContaining("sealed")
                .hasMessageContaining("@JsonSubTypes");
    }

    @Test
    void plain_interface_with_JsonSubTypes_is_supported() {

        Optional<JsonSchema> schemaOpt = new PojoOutputParser<>(Document.class).jsonSchema();
        assertThat(schemaOpt).isPresent();

        JsonObjectSchema root = (JsonObjectSchema) schemaOpt.get().rootElement();
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        assertThat(anyOf.anyOf()).hasSize(2);

        Document document = new PojoOutputParser<>(Document.class)
                .parse("{\"value\":{\"type\":\"pdf\",\"pages\":42}}");
        assertThat(document).isInstanceOf(Pdf.class);
        assertThat(((Pdf) document).pages).isEqualTo(42);
    }

    @Test
    void JsonTypeName_on_subtype_is_used_as_discriminator_value() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Bird.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");

        JsonObjectSchema eagleOption = (JsonObjectSchema) anyOf.anyOf().get(0);
        JsonEnumSchema eagleDiscriminator = (JsonEnumSchema) eagleOption.properties().get("type");
        assertThat(eagleDiscriminator.enumValues()).containsExactly("eagle");

        JsonObjectSchema sparrowOption = (JsonObjectSchema) anyOf.anyOf().get(1);
        JsonEnumSchema sparrowDiscriminator = (JsonEnumSchema) sparrowOption.properties().get("type");
        assertThat(sparrowDiscriminator.enumValues()).containsExactly("sparrow");
    }

    @Test
    void JsonTypeName_drives_dispatch_during_parsing() {

        Bird bird = new PojoOutputParser<>(Bird.class)
                .parse("{\"value\":{\"type\":\"eagle\",\"wingspanMeters\":2.5}}");

        assertThat(bird).isInstanceOf(Eagle.class);
        assertThat(((Eagle) bird).wingspanMeters()).isEqualTo(2.5);
    }

    @Test
    void JsonTypeName_falls_back_to_simple_name_for_subtypes_without_the_annotation() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Tree.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");

        JsonObjectSchema oakOption = (JsonObjectSchema) anyOf.anyOf().get(0);
        JsonEnumSchema oakDiscriminator = (JsonEnumSchema) oakOption.properties().get("type");
        // Oak has no @JsonTypeName — falls back to simple name
        assertThat(oakDiscriminator.enumValues()).containsExactly("Oak");

        JsonObjectSchema pineOption = (JsonObjectSchema) anyOf.anyOf().get(1);
        JsonEnumSchema pineDiscriminator = (JsonEnumSchema) pineOption.properties().get("type");
        // Pine has @JsonTypeName("pine") — wins over simple name
        assertThat(pineDiscriminator.enumValues()).containsExactly("pine");
    }

    @Test
    void JsonSubTypes_name_wins_over_JsonTypeName_when_both_are_set() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Car.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        JsonObjectSchema option = (JsonObjectSchema) anyOf.anyOf().get(0);
        JsonEnumSchema discriminator = (JsonEnumSchema) option.properties().get("type");
        // @JsonSubTypes.Type(name = "tesla") wins over @JsonTypeName("losing-name")
        assertThat(discriminator.enumValues()).containsExactly("tesla");
    }

    @Test
    void nested_sealed_field_produces_anyOf_in_outer_schema() {

        Optional<JsonSchema> schemaOpt = new PojoOutputParser<>(Owner.class).jsonSchema();

        assertThat(schemaOpt).isPresent();
        JsonObjectSchema root = (JsonObjectSchema) schemaOpt.get().rootElement();
        // Outer object is non-polymorphic — no `value` envelope
        assertThat(root.properties()).containsOnlyKeys("name", "pet");

        JsonAnyOfSchema petAnyOf = (JsonAnyOfSchema) root.properties().get("pet");
        assertThat(petAnyOf.anyOf()).hasSize(2);

        JsonObjectSchema dogOption = (JsonObjectSchema) petAnyOf.anyOf().get(0);
        JsonEnumSchema discriminator = (JsonEnumSchema) dogOption.properties().get("type");
        assertThat(discriminator.enumValues()).containsExactly("Dog");
    }

    @Test
    void nested_sealed_field_parses_correctly() {

        Owner owner = new PojoOutputParser<>(Owner.class)
                .parse("{\"name\":\"Alice\",\"pet\":{\"type\":\"Dog\",\"name\":\"Rex\",\"breed\":\"Labrador\"}}");

        assertThat(owner.name()).isEqualTo("Alice");
        assertThat(owner.pet()).isInstanceOf(Dog.class);
        assertThat(((Dog) owner.pet()).name()).isEqualTo("Rex");
        assertThat(((Dog) owner.pet()).breed()).isEqualTo("Labrador");
    }

    @Test
    void deeply_nested_sealed_collection_produces_array_of_anyOf() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Adoption.class).jsonSchema().get().rootElement();

        // Polymorphic schema for Animal is generated once and reused via $ref everywhere else
        // (compact schema). Either occurrence (owner.pet or petsAdopted items) carries the
        // anyOf inline; the other points at the same schema via $ref.
        JsonArraySchema petsArray = (JsonArraySchema) root.properties().get("petsAdopted");
        JsonSchemaElement petsItems = petsArray.items();

        JsonObjectSchema ownerObj = (JsonObjectSchema) root.properties().get("owner");
        JsonSchemaElement ownerPet = ownerObj.properties().get("pet");

        assertThat(petsItems).isInstanceOfAny(JsonAnyOfSchema.class, JsonReferenceSchema.class);
        assertThat(ownerPet).isInstanceOfAny(JsonAnyOfSchema.class, JsonReferenceSchema.class);

        // Definitions must hold the actual anyOf used by the $ref(s).
        assertThat(root.definitions()).isNotNull().isNotEmpty();
        assertThat(root.definitions().values()).hasAtLeastOneElementOfType(JsonAnyOfSchema.class);
    }

    @Test
    void deeply_nested_sealed_collection_parses_correctly() {

        Adoption adoption = new PojoOutputParser<>(Adoption.class)
                .parse("{\"owner\":{\"name\":\"Alice\",\"pet\":{\"type\":\"Cat\",\"name\":\"Whiskers\",\"indoor\":true}},"
                        + "\"petsAdopted\":["
                        + "{\"type\":\"Dog\",\"name\":\"Rex\",\"breed\":\"Lab\"},"
                        + "{\"type\":\"Cat\",\"name\":\"Mittens\",\"indoor\":false}"
                        + "]}");

        assertThat(adoption.owner().name()).isEqualTo("Alice");
        assertThat(adoption.owner().pet()).isInstanceOf(Cat.class);
        assertThat(adoption.petsAdopted()).hasSize(2);
        assertThat(adoption.petsAdopted().get(0)).isInstanceOf(Dog.class);
        assertThat(adoption.petsAdopted().get(1)).isInstanceOf(Cat.class);
    }

    @Test
    void nested_jackson_annotated_field_produces_anyOf_with_jackson_discriminator() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Reservation.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema shapeAnyOf = (JsonAnyOfSchema) root.properties().get("preferredShape");
        JsonObjectSchema firstOption = (JsonObjectSchema) shapeAnyOf.anyOf().get(0);
        // Jackson @JsonTypeInfo(property = "kind") is honored at field level too
        assertThat(firstOption.properties()).containsKey("kind");
    }

    @Test
    void nested_jackson_annotated_field_parses_correctly() {

        Reservation reservation = new PojoOutputParser<>(Reservation.class)
                .parse("{\"customer\":\"Bob\",\"preferredShape\":{\"kind\":\"circle\",\"radius\":3.14}}");

        assertThat(reservation.customer()).isEqualTo("Bob");
        assertThat(reservation.preferredShape()).isInstanceOf(Circle.class);
        assertThat(((Circle) reservation.preferredShape()).radius).isEqualTo(3.14);
    }

    @Test
    void schema_generation_fails_for_unsupported_JsonTypeInfo_use() {

        assertThatThrownBy(() -> new PojoOutputParser<>(Beverage.class).jsonSchema())
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("Id.MINIMAL_CLASS")
                .hasMessageContaining("Id.NAME")
                .hasMessageContaining("Beverage");
    }

    @Test
    void Id_SIMPLE_NAME_falls_back_to_simple_class_name_when_no_explicit_name() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Food.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        JsonObjectSchema pizzaOption = (JsonObjectSchema) anyOf.anyOf().get(0);
        // Jackson default property name for Id.SIMPLE_NAME is "@type"
        JsonEnumSchema discriminator = (JsonEnumSchema) pizzaOption.properties().get("@type");
        assertThat(discriminator.enumValues()).containsExactly("Pizza");
    }

    @Test
    void Id_SIMPLE_NAME_parses_simple_name() {

        Food food = new PojoOutputParser<>(Food.class).parse("{\"value\":{\"@type\":\"Pizza\",\"topping\":\"margherita\"}}");

        assertThat(food).isInstanceOf(Pizza.class);
        assertThat(((Pizza) food).topping).isEqualTo("margherita");
    }

    @Test
    void defaultImpl_is_used_when_discriminator_is_missing() {

        Tool tool = new PojoOutputParser<>(Tool.class).parse("{\"value\":{\"sizeMm\":17}}");

        assertThat(tool).isInstanceOf(Wrench.class);
        assertThat(((Wrench) tool).sizeMm).isEqualTo(17);
    }

    @Test
    void defaultImpl_is_used_when_discriminator_is_unknown() {

        // Jackson default property name for Id.NAME is "@type"
        Tool tool = new PojoOutputParser<>(Tool.class).parse("{\"value\":{\"@type\":\"unknown\",\"sizeMm\":17}}");

        assertThat(tool).isInstanceOf(Wrench.class);
        assertThat(((Wrench) tool).sizeMm).isEqualTo(17);
    }

    @Test
    void existing_property_allows_subtype_to_have_discriminator_field() {

        // schema generation succeeds despite the field collision because of As.EXISTING_PROPERTY
        Optional<JsonSchema> schema = new PojoOutputParser<>(Book.class).jsonSchema();
        assertThat(schema).isPresent();

        // and parsing dispatches to the correct subtype
        Book book = new PojoOutputParser<>(Book.class)
                .parse("{\"value\":{\"category\":\"hard\",\"title\":\"Effective Java\"}}");

        assertThat(book).isInstanceOf(HardCover.class);
        assertThat(((HardCover) book).title).isEqualTo("Effective Java");
    }

    @Test
    void visible_true_allows_subtype_to_have_discriminator_field() {

        // schema generation succeeds despite the field collision
        Optional<JsonSchema> schema = new PojoOutputParser<>(Weapon.class).jsonSchema();
        assertThat(schema).isPresent();

        // and parsing populates the field
        Weapon weapon = new PojoOutputParser<>(Weapon.class)
                .parse("{\"value\":{\"kind\":\"sword\",\"blade_length_cm\":85}}");

        assertThat(weapon).isInstanceOf(Sword.class);
        assertThat(((Sword) weapon).kind).isEqualTo("sword");
        assertThat(((Sword) weapon).blade_length_cm).isEqualTo(85);
    }

    @Test
    void schema_generation_fails_for_unsupported_JsonTypeInfo_include() {

        assertThatThrownBy(() -> new PojoOutputParser<>(Fruit.class).jsonSchema())
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("As.WRAPPER_OBJECT")
                .hasMessageContaining("As.PROPERTY")
                .hasMessageContaining("Fruit");
    }

    @Test
    void schema_generation_fails_when_subtype_field_collides_with_discriminator() {

        assertThatThrownBy(() -> new PojoOutputParser<>(Vehicle.class).jsonSchema())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Truck")
                .hasMessageContaining("'type'")
                .hasMessageContaining("@JsonTypeInfo(property")
                .hasMessageContaining("Vehicle");
    }

    @Test
    void base_type_description_uses_Description_annotation_when_present() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Pet.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");

        assertThat(anyOf.description()).isEqualTo("A pet that lives in your home");
    }

    @Test
    void base_type_description_falls_back_to_simple_name_when_no_annotation() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Animal.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");

        assertThat(anyOf.description()).isEqualTo("Animal");
    }

    @Test
    void subtype_description_uses_Description_annotation_when_present() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Pet.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");

        JsonObjectSchema hamsterOption = (JsonObjectSchema) anyOf.anyOf().get(0);
        assertThat(hamsterOption.description()).isEqualTo("A small caged rodent kept as a pet");

        JsonObjectSchema parrotOption = (JsonObjectSchema) anyOf.anyOf().get(1);
        assertThat(parrotOption.description()).isEqualTo("A talking bird that can mimic human speech");
    }

    @Test
    void subtype_description_falls_back_to_simple_name_when_no_annotation() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Animal.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");

        assertThat(((JsonObjectSchema) anyOf.anyOf().get(0)).description()).isEqualTo("Dog");
        assertThat(((JsonObjectSchema) anyOf.anyOf().get(1)).description()).isEqualTo("Cat");
    }

    @Test
    void schema_for_sealed_interface_wraps_anyOf_under_value() {

        Optional<JsonSchema> schemaOpt = new PojoOutputParser<>(Animal.class).jsonSchema();

        assertThat(schemaOpt).isPresent();
        JsonSchema schema = schemaOpt.get();
        assertThat(schema.name()).isEqualTo("Animal");

        JsonObjectSchema root = (JsonObjectSchema) schema.rootElement();
        assertThat(root.required()).containsExactly("value");

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        assertThat(anyOf.anyOf()).hasSize(2);

        // Dog option includes synthesized type discriminator with simple-name enum value
        JsonObjectSchema dogOption = (JsonObjectSchema) anyOf.anyOf().get(0);
        assertThat(dogOption.required()).startsWith("type");
        JsonEnumSchema dogDiscriminator = (JsonEnumSchema) dogOption.properties().get("type");
        assertThat(dogDiscriminator.enumValues()).containsExactly("Dog");
        assertThat(dogOption.properties()).containsKeys("name", "breed");

        JsonObjectSchema catOption = (JsonObjectSchema) anyOf.anyOf().get(1);
        JsonEnumSchema catDiscriminator = (JsonEnumSchema) catOption.properties().get("type");
        assertThat(catDiscriminator.enumValues()).containsExactly("Cat");
        assertThat(catOption.properties()).containsKeys("name", "indoor");
    }

    @Test
    void schema_honors_jackson_discriminator_property_and_subtype_names() {

        JsonObjectSchema root = (JsonObjectSchema)
                new PojoOutputParser<>(Shape.class).jsonSchema().get().rootElement();

        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) root.properties().get("value");
        JsonObjectSchema firstOption = (JsonObjectSchema) anyOf.anyOf().get(0);

        // Discriminator property name comes from @JsonTypeInfo
        assertThat(firstOption.properties()).containsKey("kind");
        // Discriminator value comes from @JsonSubTypes.Type(name=...)
        JsonEnumSchema kind = (JsonEnumSchema) firstOption.properties().get("kind");
        assertThat(kind.enumValues()).containsExactly("square");
    }

    @Test
    void schema_for_collection_of_polymorphic_uses_array_of_anyOf() {

        Optional<JsonSchema> schemaOpt = new PojoListOutputParser<>(Animal.class).jsonSchema();

        assertThat(schemaOpt).isPresent();
        JsonObjectSchema root = (JsonObjectSchema) schemaOpt.get().rootElement();
        assertThat(root.required()).containsExactly("values");

        JsonArraySchema array = (JsonArraySchema) root.properties().get("values");
        JsonSchemaElement items = array.items();
        assertThat(items).isInstanceOf(JsonAnyOfSchema.class);
        assertThat(((JsonAnyOfSchema) items).anyOf()).hasSize(2);
    }

    @Test
    void parse_unwraps_value_envelope_for_single_polymorphic() {

        Animal animal = new PojoOutputParser<>(Animal.class)
                .parse("{\"value\":{\"type\":\"Dog\",\"name\":\"Rex\",\"breed\":\"Labrador\"}}");

        assertThat(animal).isInstanceOf(Dog.class);
        assertThat(((Dog) animal).name()).isEqualTo("Rex");
        assertThat(((Dog) animal).breed()).isEqualTo("Labrador");
    }

    @Test
    void parse_accepts_unwrapped_polymorphic_payload() {

        Animal animal = new PojoOutputParser<>(Animal.class)
                .parse("{\"type\":\"Cat\",\"name\":\"Whiskers\",\"indoor\":true}");

        assertThat(animal).isInstanceOf(Cat.class);
        assertThat(((Cat) animal).name()).isEqualTo("Whiskers");
        assertThat(((Cat) animal).indoor()).isTrue();
    }

    @Test
    void parse_collection_of_polymorphic() {

        List<Animal> animals = new PojoListOutputParser<>(Animal.class)
                .parse("{\"values\":["
                        + "{\"type\":\"Dog\",\"name\":\"Rex\",\"breed\":\"Labrador\"},"
                        + "{\"type\":\"Cat\",\"name\":\"Whiskers\",\"indoor\":true}"
                        + "]}");

        assertThat(animals).hasSize(2);
        assertThat(animals.get(0)).isInstanceOf(Dog.class);
        assertThat(animals.get(1)).isInstanceOf(Cat.class);
    }

    @Test
    void parse_set_of_polymorphic() {

        Set<Animal> animals = new PojoSetOutputParser<>(Animal.class)
                .parse("{\"values\":["
                        + "{\"type\":\"Dog\",\"name\":\"Rex\",\"breed\":\"Labrador\"}"
                        + "]}");

        assertThat(animals).hasSize(1);
        assertThat(animals.iterator().next()).isInstanceOf(Dog.class);
    }

    @Test
    void parse_polymorphic_with_jackson_discriminator() {

        Shape shape = new PojoOutputParser<>(Shape.class)
                .parse("{\"value\":{\"kind\":\"circle\",\"radius\":2.5}}");

        assertThat(shape).isInstanceOf(Circle.class);
        assertThat(((Circle) shape).radius).isEqualTo(2.5);
    }

    @Test
    void parse_fails_for_unknown_discriminator_value() {

        assertThatThrownBy(() -> new PojoOutputParser<>(Animal.class)
                .parse("{\"type\":\"Hamster\",\"name\":\"Nibbles\"}"))
                .isInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse");
    }

    @Test
    void parse_fails_when_discriminator_missing() {

        assertThatThrownBy(() -> new PojoOutputParser<>(Animal.class)
                .parse("{\"value\":{\"name\":\"Rex\",\"breed\":\"Labrador\"}}"))
                .isInstanceOf(OutputParsingException.class)
                .hasMessageContaining("Failed to parse");
    }
}
