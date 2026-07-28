package dev.langchain4j.mcp.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class StructuredContentParsingTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testComplexObject() throws JsonProcessingException {
        // JSON
        String response = "{\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"id\": 2,\n" +
                "  \"result\": {\n" +
                "    \"isError\": false,\n" +
                "    \"structuredContent\": {\n" +
                "      \"integer\": 1,\n" +
                "      \"string\": \"hello\",\n" +
                "      \"boolean\": true,\n" +
                "      \"innerObject\": {\n" +
                "        \"double\": 1.0,\n" +
                "        \"null\": null\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JsonNode responseNode = objectMapper.readTree(response);
        McpToolResultExtractor extractor = mock(McpToolResultExtractor.class);
        ToolExecutionResult toolExecutionResult = ToolExecutionHelper.extractResult(responseNode, false, extractor);
        assertThat(toolExecutionResult.result()).isInstanceOf(Map.class);
        Map<String, Object> map = (Map<String, Object>) toolExecutionResult.result();
        assertThat(map).hasSize(4);
        assertThat(map.get("integer")).isEqualTo(1);
        assertThat(map.get("string")).isEqualTo("hello");
        assertThat(map.get("boolean")).isEqualTo(true);
        assertThat(map.get("innerObject")).isInstanceOf(Map.class);
        Map<String, Object> innerMap = (Map<String, Object>) map.get("innerObject");
        assertThat(innerMap).hasSize(2);
        assertThat(innerMap.get("double")).isEqualTo(1.0);
        assertThat(innerMap.containsKey("null")).isTrue();
        assertThat(innerMap.get("null")).isNull();
        verifyNoInteractions(extractor);
    }

    @Test
    public void testStructuredContentWithArrays() throws JsonProcessingException {
        String response = "{\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"id\": 3,\n" +
                "  \"result\": {\n" +
                "    \"structuredContent\": {\n" +
                "      \"items\": [1, 2, 3],\n" +
                "      \"nested\": {\n" +
                "        \"labels\": [\"a\", \"b\"]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JsonNode responseNode = objectMapper.readTree(response);
        McpToolResultExtractor extractor = mock(McpToolResultExtractor.class);

        ToolExecutionResult toolExecutionResult = ToolExecutionHelper.extractResult(responseNode, false, extractor);

        assertThat(toolExecutionResult.result()).isInstanceOf(Map.class);
        Map<String, Object> map = (Map<String, Object>) toolExecutionResult.result();
        assertThat(map.get("items")).isEqualTo(Arrays.asList(1, 2, 3));
        assertThat(map.get("nested")).isInstanceOf(Map.class);
        assertThat(((Map<String, Object>) map.get("nested")).get("labels")).isEqualTo(Arrays.asList("a", "b"));
        verifyNoInteractions(extractor);
    }

    @Test
    public void should_preserve_integer_larger_than_long_max_value() throws JsonProcessingException {
        // 9223372036854775808 = Long.MAX_VALUE + 1, parsed by Jackson as a BigIntegerNode.
        // Converting it via asLong() silently truncates to Long.MIN_VALUE, so the value must be kept as BigInteger.
        String response = "{\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"id\": 5,\n" +
                "  \"result\": {\n" +
                "    \"structuredContent\": {\n" +
                "      \"bigValue\": 9223372036854775808\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JsonNode responseNode = objectMapper.readTree(response);
        McpToolResultExtractor extractor = mock(McpToolResultExtractor.class);

        ToolExecutionResult toolExecutionResult = ToolExecutionHelper.extractResult(responseNode, false, extractor);

        assertThat(toolExecutionResult.result()).isInstanceOf(Map.class);
        Map<String, Object> map = (Map<String, Object>) toolExecutionResult.result();
        assertThat(map.get("bigValue")).isEqualTo(new BigInteger("9223372036854775808"));
        verifyNoInteractions(extractor);
    }

    @Test
    public void should_preserve_long_max_value_as_long() throws JsonProcessingException {
        // Long.MAX_VALUE must still be parsed as a Long (no regression for INT/LONG number types).
        String response = "{\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"id\": 6,\n" +
                "  \"result\": {\n" +
                "    \"structuredContent\": {\n" +
                "      \"longValue\": 9223372036854775807\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JsonNode responseNode = objectMapper.readTree(response);
        McpToolResultExtractor extractor = mock(McpToolResultExtractor.class);

        ToolExecutionResult toolExecutionResult = ToolExecutionHelper.extractResult(responseNode, false, extractor);

        assertThat(toolExecutionResult.result()).isInstanceOf(Map.class);
        Map<String, Object> map = (Map<String, Object>) toolExecutionResult.result();
        assertThat(map.get("longValue")).isEqualTo(Long.MAX_VALUE);
        verifyNoInteractions(extractor);
    }

    @Test
    public void should_prefer_structured_content_over_content_array() throws JsonProcessingException {
        String response = "{\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"id\": 4,\n" +
                "  \"result\": {\n" +
                "    \"structuredContent\": {\n" +
                "      \"source\": \"structured\"\n" +
                "    },\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"type\": \"text\",\n" +
                "        \"text\": \"{\\\"source\\\":\\\"text\\\"}\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JsonNode responseNode = objectMapper.readTree(response);
        McpToolResultExtractor extractor = mock(McpToolResultExtractor.class);

        ToolExecutionResult toolExecutionResult = ToolExecutionHelper.extractResult(responseNode, false, extractor);

        assertThat(toolExecutionResult.result()).isEqualTo(Collections.singletonMap("source", "structured"));
        assertThat(toolExecutionResult.resultText()).isEqualTo("{\"source\":\"structured\"}");
        verifyNoInteractions(extractor);
    }
}
