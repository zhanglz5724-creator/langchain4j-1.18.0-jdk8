package dev.langchain4j.mcp.client;

import static dev.langchain4j.mcp.client.DefaultMcpClient.OBJECT_MAPPER;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.ICONS;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class McpMetadataParsingTest {

    @Test
    void resourceWithMetadataAndIcons() throws Exception {
        JsonNode json = OBJECT_MAPPER.readTree(
                "{\n" +
                "  \"result\": {\n" +
                "    \"resources\": [\n" +
                "      {\n" +
                "        \"uri\": \"file:///project/weather.md\",\n" +
                "        \"name\": \"weather\",\n" +
                "        \"description\": \"Weather docs\",\n" +
                "        \"mimeType\": \"text/markdown\",\n" +
                "        \"_meta\": {\n" +
                "          \"example.org/status\": \"stable\"\n" +
                "        },\n" +
                "        \"icons\": [\n" +
                "          {\n" +
                "            \"src\": \"https://example.org/resource.png\",\n" +
                "            \"mimeType\": \"image/png\",\n" +
                "            \"sizes\": [\"64x64\"],\n" +
                "            \"theme\": \"dark\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        McpResource resource = ResourcesHelper.parseResourceRefs(json).get(0);

        assertThat(resource.metadata().get("example.org/status")).isEqualTo("stable");
        assertThat(resource.metadata()).doesNotContainKey(ICONS);
        assertThat(resource.icons())
                .containsExactly(new McpIcon(
                        "image/png", Arrays.asList("64x64"), "https://example.org/resource.png", McpIconTheme.DARK));
    }

    @Test
    void resourceTemplateWithMetadataAndIcons() throws Exception {
        JsonNode json = OBJECT_MAPPER.readTree(
                "{\n" +
                "  \"result\": {\n" +
                "    \"resourceTemplates\": [\n" +
                "      {\n" +
                "        \"uriTemplate\": \"file:///project/{name}.md\",\n" +
                "        \"name\": \"docs\",\n" +
                "        \"description\": \"Project docs\",\n" +
                "        \"mimeType\": \"text/markdown\",\n" +
                "        \"_meta\": {\n" +
                "          \"example.org/status\": \"stable\"\n" +
                "        },\n" +
                "        \"icons\": [\n" +
                "          {\n" +
                "            \"src\": \"https://example.org/template.png\",\n" +
                "            \"mimeType\": \"image/png\",\n" +
                "            \"sizes\": [\"32x32\"],\n" +
                "            \"theme\": \"light\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        McpResourceTemplate resourceTemplate =
                ResourcesHelper.parseResourceTemplateRefs(json).get(0);

        assertThat(resourceTemplate.metadata().get("example.org/status")).isEqualTo("stable");
        assertThat(resourceTemplate.metadata()).doesNotContainKey(ICONS);
        assertThat(resourceTemplate.icons())
                .containsExactly(new McpIcon(
                        "image/png", Arrays.asList("32x32"), "https://example.org/template.png", McpIconTheme.LIGHT));
    }

    @Test
    void promptWithMetadataAndIcons() throws Exception {
        JsonNode json = OBJECT_MAPPER.readTree(
                "{\n" +
                "  \"result\": {\n" +
                "    \"prompts\": [\n" +
                "      {\n" +
                "        \"name\": \"summarize\",\n" +
                "        \"description\": \"Summarize a document\",\n" +
                "        \"arguments\": [\n" +
                "          {\n" +
                "            \"name\": \"document\",\n" +
                "            \"description\": \"Document text\",\n" +
                "            \"required\": true\n" +
                "          }\n" +
                "        ],\n" +
                "        \"_meta\": {\n" +
                "          \"example.org/status\": \"stable\"\n" +
                "        },\n" +
                "        \"icons\": [\n" +
                "          {\n" +
                "            \"src\": \"https://example.org/prompt.png\",\n" +
                "            \"mimeType\": \"image/png\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        McpPrompt prompt = PromptsHelper.parsePromptRefs(json).get(0);

        assertThat(prompt.metadata().get("example.org/status")).isEqualTo("stable");
        assertThat(prompt.metadata()).doesNotContainKey(ICONS);
        assertThat(prompt.icons())
                .containsExactly(new McpIcon("image/png", Collections.emptyList(), "https://example.org/prompt.png", null));
    }

    @Test
    void metadataIsEmptyWhenMetaAndIconsAreAbsent() throws Exception {
        JsonNode resourcesJson = OBJECT_MAPPER.readTree(
                "{\n" +
                "  \"result\": {\n" +
                "    \"resources\": [\n" +
                "      {\n" +
                "        \"uri\": \"file:///project/weather.md\",\n" +
                "        \"name\": \"weather\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        JsonNode resourceTemplatesJson = OBJECT_MAPPER.readTree(
                "{\n" +
                "  \"result\": {\n" +
                "    \"resourceTemplates\": [\n" +
                "      {\n" +
                "        \"uriTemplate\": \"file:///project/{name}.md\",\n" +
                "        \"name\": \"docs\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        JsonNode promptsJson = OBJECT_MAPPER.readTree(
                "{\n" +
                "  \"result\": {\n" +
                "    \"prompts\": [\n" +
                "      {\n" +
                "        \"name\": \"summarize\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        McpResource resource = ResourcesHelper.parseResourceRefs(resourcesJson).get(0);
        McpResourceTemplate resourceTemplate =
                ResourcesHelper.parseResourceTemplateRefs(resourceTemplatesJson).get(0);
        McpPrompt prompt = PromptsHelper.parsePromptRefs(promptsJson).get(0);

        assertThat(resource.metadata()).isEmpty();
        assertThat(resource.icons()).isEmpty();
        assertThat(resourceTemplate.metadata()).isEmpty();
        assertThat(resourceTemplate.icons()).isEmpty();
        assertThat(prompt.metadata()).isEmpty();
        assertThat(prompt.icons()).isEmpty();
    }
}
