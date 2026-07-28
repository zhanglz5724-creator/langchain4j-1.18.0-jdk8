package dev.langchain4j.mcp.client;

import static dev.langchain4j.mcp.client.McpToolMetadataKeys.DESTRUCTIVE_HINT;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.ICONS;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.IDEMPOTENT_HINT;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.OPEN_WORLD_HINT;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.OUTPUT_SCHEMA;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.READ_ONLY_HINT;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.TITLE;
import static dev.langchain4j.mcp.client.McpToolMetadataKeys.TITLE_ANNOTATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNullSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonReferenceSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ToolSpecificationHelperTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void toolWithSimpleParams() throws JsonProcessingException {
        String text =
                "[ {\n" +
                "      \"name\" : \"operation\",\n" +
                "      \"description\" : \"Super operation\",\n" +
                "      \"inputSchema\" : {\n" +
                "        \"type\" : \"object\",\n" +
                "        \"properties\" : {\n" +
                "          \"stringParameter\" : {\n" +
                "            \"type\" : \"string\",\n" +
                "            \"description\" : \"Message to echo\"\n" +
                "          },\n" +
                "          \"enumParameter\" : {\n" +
                "            \"type\" : \"string\",\n" +
                "            \"description\" : \"The protocol to use\",\n" +
                "            \"enum\": [\n" +
                "                \"http\",\n" +
                "                \"https\"\n" +
                "            ]\n" +
                "          },\n" +
                "          \"numberParameter\": {\n" +
                "            \"type\": \"number\",\n" +
                "            \"description\": \"A number\"\n" +
                "          },\n" +
                "          \"integerParameter\": {\n" +
                "            \"type\": \"integer\",\n" +
                "            \"description\": \"An integer\"\n" +
                "          },\n" +
                "          \"booleanParameter\": {\n" +
                "            \"type\": \"boolean\",\n" +
                "            \"description\": \"A boolean\"\n" +
                "          },\n" +
                "          \"arrayParameter\": {\n" +
                "              \"type\": \"array\",\n" +
                "              \"description\": \"An array of strings\",\n" +
                "              \"items\": {\n" +
                "                \"type\": \"string\"\n" +
                "              }\n" +
                "          }\n" +
                "        },\n" +
                "        \"required\" : [ \"stringParameter\" ],\n" +
                "        \"additionalProperties\" : false,\n" +
                "        \"$schema\" : \"http://json-schema.org/draft-07/schema#\"\n" +
                "      }\n" +
                "    } ]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        assertThat(toolSpecification.name()).isEqualTo("operation");
        assertThat(toolSpecification.description()).isEqualTo("Super operation");

        // validate parameters
        JsonObjectSchema parameters = toolSpecification.parameters();
        assertThat(parameters.properties()).hasSize(6);
        assertThat(parameters.required()).hasSize(1);
        assertThat(parameters.required().get(0)).isEqualTo("stringParameter");

        JsonStringSchema messageParameter =
                (JsonStringSchema) parameters.properties().get("stringParameter");
        assertThat(messageParameter.description()).isEqualTo("Message to echo");

        JsonEnumSchema enumParameter = (JsonEnumSchema) parameters.properties().get("enumParameter");
        assertThat(enumParameter.description()).isEqualTo("The protocol to use");
        assertThat(enumParameter.enumValues()).containsExactly("http", "https");

        JsonNumberSchema numberParameter =
                (JsonNumberSchema) parameters.properties().get("numberParameter");
        assertThat(numberParameter.description()).isEqualTo("A number");

        JsonIntegerSchema integerParameter =
                (JsonIntegerSchema) parameters.properties().get("integerParameter");
        assertThat(integerParameter.description()).isEqualTo("An integer");

        JsonBooleanSchema booleanParameter =
                (JsonBooleanSchema) parameters.properties().get("booleanParameter");
        assertThat(booleanParameter.description()).isEqualTo("A boolean");

        JsonArraySchema arrayParameter =
                (JsonArraySchema) parameters.properties().get("arrayParameter");
        assertThat(arrayParameter.description()).isEqualTo("An array of strings");
    }

    @Test
    void toolWithObjectParam() throws JsonProcessingException {
        String text =
                "[\n" +
                "  {\n" +
                "    \"name\": \"operation\",\n" +
                "    \"description\": \"Super operation\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"complexParameter\": {\n" +
                "          \"type\": \"object\",\n" +
                "          \"description\": \"A complex parameter\",\n" +
                "          \"properties\": {\n" +
                "            \"nestedString\": {\n" +
                "              \"type\": \"string\",\n" +
                "              \"description\": \"A nested string\"\n" +
                "            },\n" +
                "            \"nestedNumber\": {\n" +
                "              \"type\": \"number\",\n" +
                "              \"description\": \"A nested number\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false\n" +
                "    }\n" +
                "  }\n" +
                "]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        assertThat(toolSpecification.name()).isEqualTo("operation");
        assertThat(toolSpecification.description()).isEqualTo("Super operation");

        // validate parameters
        JsonObjectSchema parameters = toolSpecification.parameters();
        assertThat(parameters.properties()).hasSize(1);

        JsonObjectSchema complexParameter =
                (JsonObjectSchema) parameters.properties().get("complexParameter");
        assertThat(complexParameter.description()).isEqualTo("A complex parameter");
        assertThat(complexParameter.properties()).hasSize(2);

        JsonStringSchema nestedStringParameter =
                (JsonStringSchema) complexParameter.properties().get("nestedString");
        assertThat(nestedStringParameter.description()).isEqualTo("A nested string");

        JsonNumberSchema nestedNumberParameter =
                (JsonNumberSchema) complexParameter.properties().get("nestedNumber");
        assertThat(nestedNumberParameter.description()).isEqualTo("A nested number");
    }

    @Test
    void toolWithNoParams() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\" : \"getTinyImage\",\n" +
                "    \"description\" : \"Returns the MCP_TINY_IMAGE\",\n" +
                "    \"inputSchema\" : {\n" +
                "        \"type\" : \"object\",\n" +
                "        \"properties\" : { },\n" +
                "        \"additionalProperties\" : false,\n" +
                "        \"$schema\" : \"http://json-schema.org/draft-07/schema#\"\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        assertThat(toolSpecification.name()).isEqualTo("getTinyImage");
        assertThat(toolSpecification.description()).isEqualTo("Returns the MCP_TINY_IMAGE");
        JsonObjectSchema parameters = toolSpecification.parameters();
        assertThat(parameters.properties()).isEmpty();
    }

    @Test
    void arrayWithMultipleAllowedTypes() throws JsonProcessingException {
        String text =
                        "[{\n" +
                        "  \"name\": \"query\",\n" +
                        "  \"description\": \"Execute a SELECT query\",\n" +
                        "  \"inputSchema\": {\n" +
                        "    \"type\": \"object\",\n" +
                        "    \"properties\": {\n" +
                        "      \"sql\": {\n" +
                        "        \"type\": \"string\",\n" +
                        "        \"description\": \"SQL SELECT query\"\n" +
                        "      },\n" +
                        "      \"params\": {\n" +
                        "        \"type\": \"array\",\n" +
                        "        \"items\": {\n" +
                        "          \"type\": [\n" +
                        "            \"string\",\n" +
                        "            \"number\",\n" +
                        "            \"boolean\",\n" +
                        "            \"null\",\n" +
                        "            \"integer\"\n" +
                        "          ]\n" +
                        "        },\n" +
                        "        \"description\": \"Query parameters (optional)\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"required\": [\n" +
                        "      \"sql\"\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        JsonObjectSchema parameters = toolSpecification.parameters();
        JsonArraySchema params = (JsonArraySchema) parameters.properties().get("params");
        assertThat(params.description()).isEqualTo("Query parameters (optional)");
        assertThat(params.items()).isInstanceOf(JsonAnyOfSchema.class);
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) params.items();
        assertThat(anyOf.anyOf().get(0)).isInstanceOf(JsonStringSchema.class);
        assertThat(anyOf.anyOf().get(1)).isInstanceOf(JsonNumberSchema.class);
        assertThat(anyOf.anyOf().get(2)).isInstanceOf(JsonBooleanSchema.class);
        assertThat(anyOf.anyOf().get(3)).isInstanceOf(JsonNullSchema.class);
        assertThat(anyOf.anyOf().get(4)).isInstanceOf(JsonIntegerSchema.class);
    }

    @Test
    void arrayWithAnyOf() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"create_pull_request_review\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"comments\": {\n" +
                "          \"type\": \"array\",\n" +
                "          \"items\": {\n" +
                "            \"anyOf\": [\n" +
                "              {\n" +
                "                \"type\": \"object\",\n" +
                "                \"properties\": {\n" +
                "                  \"path\": {\n" +
                "                    \"type\": \"string\",\n" +
                "                    \"description\": \"The relative path to the file being commented on\"\n" +
                "                  },\n" +
                "                  \"position\": {\n" +
                "                    \"type\": \"number\",\n" +
                "                    \"description\": \"The position in the diff where you want to add a review comment\"\n" +
                "                  },\n" +
                "                  \"body\": {\n" +
                "                    \"type\": \"string\",\n" +
                "                    \"description\": \"Text of the review comment\"\n" +
                "                  }\n" +
                "                },\n" +
                "                \"required\": [\n" +
                "                  \"path\",\n" +
                "                  \"position\",\n" +
                "                  \"body\"\n" +
                "                ],\n" +
                "                \"additionalProperties\": false\n" +
                "              },\n" +
                "              {\n" +
                "                \"type\": \"object\",\n" +
                "                \"properties\": {\n" +
                "                  \"path\": {\n" +
                "                    \"type\": \"string\",\n" +
                "                    \"description\": \"The relative path to the file being commented on\"\n" +
                "                  },\n" +
                "                  \"line\": {\n" +
                "                    \"type\": \"number\",\n" +
                "                    \"description\": \"The line number in the file where you want to add a review comment\"\n" +
                "                  },\n" +
                "                  \"body\": {\n" +
                "                    \"type\": \"string\",\n" +
                "                    \"description\": \"Text of the review comment\"\n" +
                "                  }\n" +
                "                },\n" +
                "                \"required\": [\n" +
                "                  \"path\",\n" +
                "                  \"line\",\n" +
                "                  \"body\"\n" +
                "                ],\n" +
                "                \"additionalProperties\": false\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          \"description\": \"Comments to post as part of the review (specify either position or line, not both)\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false,\n" +
                "      \"$schema\": \"http://json-schema.org/draft-07/schema#\"\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);

        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        JsonObjectSchema parameters = toolSpecification.parameters();
        JsonArraySchema comments = (JsonArraySchema) parameters.properties().get("comments");
        assertThat(comments.items()).isInstanceOf(JsonAnyOfSchema.class);
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) comments.items();
        assertThat(anyOf.anyOf()).hasSize(2);

        JsonSchemaElement option1 = anyOf.anyOf().get(0);
        assertThat(option1).isInstanceOf(JsonObjectSchema.class);
        assertThat(((JsonObjectSchema) option1).properties()).containsOnlyKeys("path", "position", "body");

        JsonSchemaElement option2 = anyOf.anyOf().get(1);
        assertThat(option2).isInstanceOf(JsonObjectSchema.class);
        assertThat(((JsonObjectSchema) option2).properties()).containsOnlyKeys("path", "line", "body");
    }

    @Test
    void toolWithAnyOfAlongsideObjectTypeAtRoot() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"fetch_artifacts\",\n" +
                "    \"description\": \"Fetch build artifacts\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"version\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"Version identifier. Provide this, run_url, or both.\"\n" +
                "        },\n" +
                "        \"run_url\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"Execution run URL. Provide this, version, or both.\"\n" +
                "        },\n" +
                "        \"filter\": {\n" +
                "          \"type\": \"string\",\n" +
                "          \"description\": \"Comma-separated list of names to fetch.\"\n" +
                "        },\n" +
                "        \"signed\": {\n" +
                "          \"type\": \"boolean\",\n" +
                "          \"description\": \"If true, returns signed URLs.\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"anyOf\": [\n" +
                "        { \"required\": [\"version\"] },\n" +
                "        { \"required\": [\"run_url\"] }\n" +
                "      ]\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);

        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        assertThat(toolSpecification.name()).isEqualTo("fetch_artifacts");
        JsonObjectSchema parameters = toolSpecification.parameters();
        assertThat(parameters.properties()).containsOnlyKeys("version", "run_url", "filter", "signed");
        assertThat(parameters.properties().get("version")).isInstanceOf(JsonStringSchema.class);
        assertThat(parameters.properties().get("signed")).isInstanceOf(JsonBooleanSchema.class);
    }

    @Test
    void toolWithAnyOfAlongsideObjectTypeAndPropertiesInBranches() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"fetch_by_key\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"anyOf\": [\n" +
                "        {\n" +
                "          \"properties\": { \"version\": { \"type\": \"string\" } },\n" +
                "          \"required\": [\"version\"]\n" +
                "        },\n" +
                "        {\n" +
                "          \"properties\": { \"run_url\": { \"type\": \"string\" } },\n" +
                "          \"required\": [\"run_url\"]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);

        assertThat(toolSpecifications).hasSize(1);
        assertThat(toolSpecifications.get(0).parameters()).isInstanceOf(JsonObjectSchema.class);
    }

    @Test
    void nullTypeName() throws JsonProcessingException {
        String text =
                "[{\n" +
                "   \"name\": \"set_config_value\",\n" +
                "   \"description\": \"Set a specific configuration value by key\",\n" +
                "   \"inputSchema\": {\n" +
                "     \"type\": \"object\",\n" +
                "     \"properties\": {\n" +
                "       \"key\": {\n" +
                "         \"type\": \"string\"\n" +
                "       },\n" +
                "       \"value\": {}\n" +
                "     },\n" +
                "     \"required\": [\n" +
                "       \"key\"\n" +
                "     ],\n" +
                "     \"additionalProperties\": false,\n" +
                "     \"$schema\": \"http://json-schema.org/draft-07/schema#\"\n" +
                "   }\n" +
                " }]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications.get(0).parameters().properties().get("value"))
                .isInstanceOf(JsonObjectSchema.class);
    }

    @Test
    void nullType() throws JsonProcessingException {
        String text =
                "[{\n" +
                "  \"name\": \"createCompassCustomFieldDefinition\",\n" +
                "  \"description\": \"Create a new Compass custom field definition\",\n" +
                "  \"inputSchema\": {\n" +
                "    \"type\": \"object\",\n" +
                "    \"properties\": {\n" +
                "      \"cloudId\": {\n" +
                "        \"type\": \"string\",\n" +
                "        \"description\": \"Unique identifier for an Atlassian Cloud instance in the form of a UUID. Can also be a site URL. If not working, use the \\\"getAccessibleAtlassianResources\\\" tool to find accessible Cloud IDs.\"\n" +
                "      },\n" +
                "      \"fieldSelections\": {\n" +
                "        \"anyOf\": [\n" +
                "          {\n" +
                "            \"type\": \"object\",\n" +
                "            \"additionalProperties\": {\n" +
                "              \"type\": \"null\"\n" +
                "            },\n" +
                "            \"description\": \"An list of options for the custom field definition, expressed as maps. The keys should be the options, and the values should all be null. Only used for SingleSelect and MultiSelect custom field definitions.\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"null\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"description\": \"An list of options for the custom field definition, expressed as maps. The keys should be the options, and the values should all be null. Only used for SingleSelect and MultiSelect custom field definitions.\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"required\": [\n" +
                "      \"cloudId\"\n" +
                "    ],\n" +
                "    \"additionalProperties\": false,\n" +
                "    \"$schema\": \"http://json-schema.org/draft-07/schema#\"\n" +
                "  }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications.get(0).parameters().properties().get("fieldSelections"))
                .isInstanceOf(JsonAnyOfSchema.class);
        JsonAnyOfSchema jsAny = (JsonAnyOfSchema)
                toolSpecifications.get(0).parameters().properties().get("fieldSelections");
        assertThat(jsAny.description())
                .isEqualTo(
                        "An list of options for the custom field definition, expressed as maps. "
                                + "The keys should be the options, and the values should all be null. Only used for SingleSelect and MultiSelect custom field definitions.");
        assertThat(jsAny.anyOf()).hasSize(2);
        assertThat(jsAny.anyOf().get(0)).isInstanceOf(JsonObjectSchema.class);
        assertThat(jsAny.anyOf().get(1)).isInstanceOf(JsonNullSchema.class);
    }

    @Test
    void arrayWithoutSpecifiedType() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"something\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"arrayparam\": {\n" +
                "          \"type\": \"array\",\n" +
                "          \"description\": \"An array of whatever you like\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false,\n" +
                "      \"$schema\": \"http://json-schema.org/draft-07/schema#\"\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);

        assertThat(toolSpecifications).hasSize(1);
        JsonSchemaElement parameter =
                toolSpecifications.get(0).parameters().properties().get("arrayparam");
        assertThat(parameter).isInstanceOf(JsonArraySchema.class);
        assertThat(((JsonArraySchema) parameter).items()).isNull();
    }

    @Test
    void toolWithAnnotations() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"something\",\n" +
                "    \"inputSchema\": {\n" +
                "    },\n" +
                "    \"annotations\": {\n" +
                "      \"destructiveHint\": true,\n" +
                "      \"idempotentHint\": false,\n" +
                "      \"openWorldHint\": true,\n" +
                "      \"readOnlyHint\": false,\n" +
                "      \"title\": \"A tool with annotations\"\n" +
                "    },\n" +
                "    \"title\": \"A title in the root tool object\"\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        Map<String, Object> metadata = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json)
                .get(0)
                .metadata();
        assertThat(metadata.get(TITLE_ANNOTATION)).isEqualTo("A tool with annotations");
        assertThat(metadata.get(TITLE)).isEqualTo("A title in the root tool object");
        assertThat(metadata.get(READ_ONLY_HINT)).isEqualTo(false);
        assertThat(metadata.get(DESTRUCTIVE_HINT)).isEqualTo(true);
        assertThat(metadata.get(IDEMPOTENT_HINT)).isEqualTo(false);
        assertThat(metadata.get(OPEN_WORLD_HINT)).isEqualTo(true);
    }

    @Test
    void toolWithOutputSchema() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"get_weather\",\n" +
                "    \"description\": \"Get the weather for a location\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"location\": { \"type\": \"string\" }\n" +
                "      },\n" +
                "      \"required\": [\"location\"]\n" +
                "    },\n" +
                "    \"outputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"temperature\": {\n" +
                "          \"type\": \"number\",\n" +
                "          \"description\": \"Temperature in Celsius\"\n" +
                "        },\n" +
                "        \"conditions\": {\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"required\": [\"temperature\", \"conditions\"]\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        Map<String, Object> metadata = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json)
                .get(0)
                .metadata();

        assertThat(metadata.get(OUTPUT_SCHEMA)).isInstanceOf(Map.class);
        Map<String, Object> outputSchema = (Map<String, Object>) metadata.get(OUTPUT_SCHEMA);
        assertThat(outputSchema.get("type")).isEqualTo("object");
        assertThat(outputSchema.get("required")).isEqualTo(Arrays.asList("temperature", "conditions"));
        assertThat(outputSchema.get("properties")).isInstanceOf(Map.class);
        Map<String, Object> properties = (Map<String, Object>) outputSchema.get("properties");
        assertThat(properties).containsOnlyKeys("temperature", "conditions");
    }

    @Test
    void toolWithIcons() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"get_weather\",\n" +
                "    \"inputSchema\": {\n" +
                "    },\n" +
                "    \"icons\": [\n" +
                "      {\n" +
                "        \"src\": \"https://example.org/weather.png\",\n" +
                "        \"mimeType\": \"image/png\",\n" +
                "        \"sizes\": [\"64x64\"],\n" +
                "        \"theme\": \"dark\"\n" +
                "      }\n" +
                "    ]\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        Map<String, Object> metadata = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json)
                .get(0)
                .metadata();

        assertThat(metadata.get(ICONS))
                .isEqualTo(Arrays.asList(new McpIcon(
                        "image/png", Arrays.asList("64x64"), "https://example.org/weather.png", McpIconTheme.DARK)));
    }

    @Test
    void toolWithoutOutputSchema() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"noop\",\n" +
                "    \"inputSchema\": {}\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        Map<String, Object> metadata = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json)
                .get(0)
                .metadata();

        assertThat(metadata).doesNotContainKey(OUTPUT_SCHEMA);
    }

    @Test
    void toolWithMetadata() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"something\",\n" +
                "    \"inputSchema\": {\n" +
                "    },\n" +
                "    \"_meta\": {\n" +
                "      \"example.org/array\": [1, 2, 3],\n" +
                "      \"example.org/string\": \"hello\",\n" +
                "      \"complex\": {\n" +
                "        \"a\": true\n" +
                "      }\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        Map<String, Object> metadata = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json)
                .get(0)
                .metadata();

        assertThat(metadata.get("example.org/array")).isEqualTo(Arrays.asList(1, 2, 3));
        assertThat(metadata.get("example.org/string")).isEqualTo("hello");
        assertThat(metadata.get("complex")).isInstanceOf(Map.class);
        Map<String, Object> complex = (Map<String, Object>) metadata.get("complex");
        assertThat(complex.get("a")).isEqualTo(true);
    }

    @Test
    void toolWithRefAndDefs() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"create_node\",\n" +
                "    \"description\": \"Creates a tree node\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"node\": {\n" +
                "          \"$ref\": \"#/$defs/TreeNode\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"required\": [\"node\"],\n" +
                "      \"$defs\": {\n" +
                "        \"TreeNode\": {\n" +
                "          \"type\": \"object\",\n" +
                "          \"properties\": {\n" +
                "            \"value\": {\n" +
                "              \"type\": \"string\",\n" +
                "              \"description\": \"Node value\"\n" +
                "            },\n" +
                "            \"children\": {\n" +
                "              \"type\": \"array\",\n" +
                "              \"items\": {\n" +
                "                \"$ref\": \"#/$defs/TreeNode\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"required\": [\"value\"]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);
        ToolSpecification toolSpecification = toolSpecifications.get(0);
        assertThat(toolSpecification.name()).isEqualTo("create_node");

        JsonObjectSchema parameters = toolSpecification.parameters();

        // the "node" property should be a reference
        JsonSchemaElement nodeParam = parameters.properties().get("node");
        assertThat(nodeParam).isInstanceOf(JsonReferenceSchema.class);
        assertThat(((JsonReferenceSchema) nodeParam).reference()).isEqualTo("TreeNode");

        // $defs should be parsed into definitions
        assertThat(parameters.definitions()).isNotNull();
        assertThat(parameters.definitions()).containsKey("TreeNode");
        JsonObjectSchema treeNodeDef =
                (JsonObjectSchema) parameters.definitions().get("TreeNode");
        assertThat(treeNodeDef.properties()).containsOnlyKeys("value", "children");

        // the "children" items should also be a reference
        JsonArraySchema children = (JsonArraySchema) treeNodeDef.properties().get("children");
        assertThat(children.items()).isInstanceOf(JsonReferenceSchema.class);
        assertThat(((JsonReferenceSchema) children.items()).reference()).isEqualTo("TreeNode");
    }

    @Test
    void toolWithDraft07Definitions() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"send_message\",\n" +
                "    \"description\": \"Sends a message\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"recipient\": {\n" +
                "          \"$ref\": \"#/definitions/Contact\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"definitions\": {\n" +
                "        \"Contact\": {\n" +
                "          \"type\": \"object\",\n" +
                "          \"properties\": {\n" +
                "            \"name\": {\n" +
                "              \"type\": \"string\"\n" +
                "            },\n" +
                "            \"email\": {\n" +
                "              \"type\": \"string\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"required\": [\"name\", \"email\"]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);

        JsonObjectSchema parameters = toolSpecifications.get(0).parameters();

        // the "recipient" property should be a reference
        JsonSchemaElement recipientParam = parameters.properties().get("recipient");
        assertThat(recipientParam).isInstanceOf(JsonReferenceSchema.class);
        assertThat(((JsonReferenceSchema) recipientParam).reference()).isEqualTo("Contact");

        // definitions should be parsed
        assertThat(parameters.definitions()).containsKey("Contact");
        JsonObjectSchema contactDef =
                (JsonObjectSchema) parameters.definitions().get("Contact");
        assertThat(contactDef.properties()).containsOnlyKeys("name", "email");
        assertThat(contactDef.required()).containsExactly("name", "email");
    }

    @Test
    void toolWithRefInAnyOf() throws JsonProcessingException {
        String text =
                "[{\n" +
                "    \"name\": \"process\",\n" +
                "    \"inputSchema\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"input\": {\n" +
                "          \"anyOf\": [\n" +
                "            { \"$ref\": \"#/$defs/TextInput\" },\n" +
                "            { \"type\": \"string\" }\n" +
                "          ],\n" +
                "          \"description\": \"The input to process\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"$defs\": {\n" +
                "        \"TextInput\": {\n" +
                "          \"type\": \"object\",\n" +
                "          \"properties\": {\n" +
                "            \"text\": { \"type\": \"string\" },\n" +
                "            \"language\": { \"type\": \"string\" }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "}]";
        ArrayNode json = OBJECT_MAPPER.readValue(text, ArrayNode.class);
        List<ToolSpecification> toolSpecifications = ToolSpecificationHelper.toolSpecificationListFromMcpResponse(json);
        assertThat(toolSpecifications).hasSize(1);

        JsonObjectSchema parameters = toolSpecifications.get(0).parameters();

        // the "input" property should be anyOf with a $ref and a string
        JsonSchemaElement inputParam = parameters.properties().get("input");
        assertThat(inputParam).isInstanceOf(JsonAnyOfSchema.class);
        JsonAnyOfSchema anyOf = (JsonAnyOfSchema) inputParam;
        assertThat(anyOf.anyOf()).hasSize(2);
        assertThat(anyOf.anyOf().get(0)).isInstanceOf(JsonReferenceSchema.class);
        assertThat(((JsonReferenceSchema) anyOf.anyOf().get(0)).reference()).isEqualTo("TextInput");
        assertThat(anyOf.anyOf().get(1)).isInstanceOf(JsonStringSchema.class);

        // $defs should be parsed into definitions
        assertThat(parameters.definitions()).containsKey("TextInput");
        JsonObjectSchema textInputDef =
                (JsonObjectSchema) parameters.definitions().get("TextInput");
        assertThat(textInputDef.properties()).containsOnlyKeys("text", "language");
    }
}
