package dev.langchain4j.model.openai;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;
import static dev.langchain4j.internal.JsonSchemaElementUtils.jsonSchemaElementFrom;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class OpenAiChatModelWithJsonSchemaIT {

    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({@JsonSubTypes.Type(Circle.class), @JsonSubTypes.Type(Rectangle.class)})
    interface Shape {}

    static class Circle implements Shape {
        public double radius;

        public Circle() {}

        public Circle(double radius) { this.radius = radius; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Circle)) return false;
            Circle circle = (Circle) o;
            return Double.compare(circle.radius, radius) == 0;
        }

        @Override
        public int hashCode() { return Objects.hash(radius); }
    }

    static class Rectangle implements Shape {
        public double width;
        public double height;

        public Rectangle() {}

        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Rectangle)) return false;
            Rectangle rectangle = (Rectangle) o;
            return Double.compare(rectangle.width, width) == 0
                    && Double.compare(rectangle.height, height) == 0;
        }

        @Override
        public int hashCode() { return Objects.hash(width, height); }
    }

    static class Shapes {
        public List<Shape> shapes;

        public Shapes() {}

        public List<Shape> shapes() { return shapes; }
    }

    // TODO move to common tests
    @Test
    void should_generate_valid_json_with_anyof() throws JsonProcessingException {

        // given
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
        JsonSchemaElement circleSchema = jsonSchemaElementFrom(Circle.class);
        JsonSchemaElement rectangleSchema = jsonSchemaElementFrom(Rectangle.class);

        JsonSchema jsonSchema = JsonSchema.builder()
                .name("Shapes")
                .rootElement(JsonObjectSchema.builder()
                        .addProperty(
                                "shapes",
                                JsonArraySchema.builder()
                                        .items(JsonAnyOfSchema.builder()
                                                .anyOf(circleSchema, rectangleSchema)
                                                .build())
                                        .build())
                        .required(Collections.singletonList("shapes"))
                        .build())
                .build();

        ResponseFormat responseFormat =
                ResponseFormat.builder().type(JSON).jsonSchema(jsonSchema).build();

        UserMessage userMessage = UserMessage.from(
                "Extract information from the following text:\n"
                + "1. A circle with a radius of 5\n"
                + "2. A rectangle with a width of 10 and a height of 20");

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .responseFormat(responseFormat)
                .build();

        // when
        ChatResponse chatResponse = model.chat(chatRequest);

        // then
        Shapes shapes = new ObjectMapper().readValue(chatResponse.aiMessage().text(), Shapes.class);
        assertThat(shapes).isNotNull();
        assertThat(shapes.shapes()).isNotNull().containsExactlyInAnyOrder(new Circle(5), new Rectangle(10, 20));
    }

    @Test
    void should_support_json_schema_in_model() throws JsonProcessingException {

        // given
        JsonSchemaElement circleSchema = jsonSchemaElementFrom(Circle.class);
        JsonSchemaElement rectangleSchema = jsonSchemaElementFrom(Rectangle.class);

        JsonSchema jsonSchema = JsonSchema.builder()
                .name("Shapes")
                .rootElement(JsonObjectSchema.builder()
                        .addProperty(
                                "shapes",
                                JsonArraySchema.builder()
                                        .items(JsonAnyOfSchema.builder()
                                                .anyOf(circleSchema, rectangleSchema)
                                                .build())
                                        .build())
                        .required(Collections.singletonList("shapes"))
                        .build())
                .build();

        ResponseFormat responseFormat =
                ResponseFormat.builder().type(JSON).jsonSchema(jsonSchema).build();
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .responseFormat(responseFormat)
                .logRequests(true)
                .logResponses(true)
                .build();

        UserMessage userMessage = UserMessage.from(
                "Extract information from the following text:\n"
                + "1. A circle with a radius of 5\n"
                + "2. A rectangle with a width of 10 and a height of 20");

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .build();

        // when
        ChatResponse chatResponse = model.chat(chatRequest);

        // then
        Shapes shapes = new ObjectMapper().readValue(chatResponse.aiMessage().text(), Shapes.class);
        assertThat(shapes).isNotNull();
        assertThat(shapes.shapes()).isNotNull().containsExactlyInAnyOrder(new Circle(5), new Rectangle(10, 20));
    }
}
