package dev.langchain4j.internal;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatRequestValidationUtilsTest {

    @Test
    void validateMessages_shouldAllowTextContent() {
        UserMessage message = new UserMessage(Arrays.asList(new TextContent("hello")));
        assertThatCode(() -> ChatRequestValidationUtils.validateMessages(Arrays.asList(message)))
                .doesNotThrowAnyException();
    }

    @Test
    void validateMessages_shouldRejectNonTextContent() {
        UserMessage message = new UserMessage(Arrays.asList(new ImageContent("http://image.com")));
        assertThatThrownBy(() -> ChatRequestValidationUtils.validateMessages(Arrays.asList(message)))
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("Content of type image is not supported");
    }

    @Test
    void validateTools_shouldRejectNonEmptyList() {
        ToolSpecification tool = ToolSpecification.builder().name("myTool").build();
        assertThatThrownBy(() -> ChatRequestValidationUtils.validate(Arrays.asList(tool)))
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("tools are not supported yet");
    }

    @Test
    void validateTools_shouldAllowEmptyList() {
        assertThatCode(() -> ChatRequestValidationUtils.validate(Arrays.asList()))
                .doesNotThrowAnyException();
    }

    @Test
    void validateToolChoice_shouldRejectNonAuto() {
        assertThatThrownBy(() -> ChatRequestValidationUtils.validate(ToolChoice.REQUIRED))
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("ToolChoice.REQUIRED is not supported");
    }

    @Test
    void validateToolChoice_shouldAllowAuto() {
        assertThatCode(() -> ChatRequestValidationUtils.validate(ToolChoice.AUTO))
                .doesNotThrowAnyException();
    }

    @Test
    void validateResponseFormat_shouldRejectJson() {
        assertThatThrownBy(() -> ChatRequestValidationUtils.validate(ResponseFormat.JSON))
                .isInstanceOf(UnsupportedFeatureException.class)
                .hasMessageContaining("JSON response format");
    }

    @Test
    void validateResponseFormat_shouldAllowOtherFormats() {
        assertThatCode(() -> ChatRequestValidationUtils.validate(ResponseFormat.TEXT))
                .doesNotThrowAnyException();
    }
}