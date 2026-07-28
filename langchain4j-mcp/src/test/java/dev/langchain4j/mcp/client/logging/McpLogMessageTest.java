package dev.langchain4j.mcp.client.logging;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class McpLogMessageTest {

    @Test
    void logMessageWithoutLogger() {
        String json =
                "{\n" +
                "  \"method\" : \"notifications/message\",\n" +
                "  \"params\" : {\n" +
                "    \"level\" : \"info\",\n" +
                "    \"data\" : \"Searching DuckDuckGo for: length of pont des arts in meters\"\n" +
                "  },\n" +
                "  \"jsonrpc\" : \"2.0\"\n" +
                "}";

        JsonNode jsonNode = toJsonNode(json);

        McpLogMessage message = McpLogMessage.fromJson(jsonNode.get("params"));
        assertThat(message.level()).isEqualTo(McpLogLevel.from("info"));
        assertThat(message.data()).isEqualTo(jsonNode.get("params").get("data"));
        assertThat(message.logger()).isNull();
    }

    private static JsonNode toJsonNode(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
