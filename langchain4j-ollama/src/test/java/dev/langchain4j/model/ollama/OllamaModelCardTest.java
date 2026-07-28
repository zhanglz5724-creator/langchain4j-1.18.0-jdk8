package dev.langchain4j.model.ollama;

import static dev.langchain4j.model.ollama.OllamaJsonUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;

class OllamaModelCardTest {

    @Test
    void should_deserialize_show_model_information_response() {
        String json = "                {\n"
+ "                  \"license\": \"Apache-2.0\",\n"
+ "                  \"modelfile\": \"FROM llama3.2\",\n"
+ "                  \"parameters\": \"temperature 0.8\",\n"
+ "                  \"template\": \"{{ .Prompt }}\",\n"
+ "                  \"system\": \"You are helpful\",\n"
+ "                  \"details\": {\n"
+ "                    \"parent_model\": \"llama3.2\",\n"
+ "                    \"format\": \"gguf\",\n"
+ "                    \"family\": \"llama\",\n"
+ "                    \"families\": [\n"
+ "                      \"llama\"\n"
+ "                    ],\n"
+ "                    \"parameter_size\": \"3.2B\",\n"
+ "                    \"quantization_level\": \"Q4_K_M\"\n"
+ "                  },\n"
+ "                  \"messages\": [\n"
+ "                    {\n"
+ "                      \"role\": \"SYSTEM\",\n"
+ "                      \"content\": \"Use short answers\",\n"
+ "                      \"thinking\": \"Internal reasoning\",\n"
+ "                      \"images\": [\n"
+ "                        \"base64-image\"\n"
+ "                      ],\n"
+ "                      \"tool_calls\": [\n"
+ "                        {\n"
+ "                          \"function\": {\n"
+ "                            \"index\": 0,\n"
+ "                            \"name\": \"get_weather\",\n"
+ "                            \"arguments\": {\n"
+ "                              \"city\": \"Paris\"\n"
+ "                            }\n"
+ "                          }\n"
+ "                        },\n"
+ "                        {\n"
+ "                          \"function\": {\n"
+ "                            \"name\": \"get_time\",\n"
+ "                            \"arguments\": {\n"
+ "                              \"city\": \"Paris\"\n"
+ "                            }\n"
+ "                          }\n"
+ "                        }\n"
+ "                      ],\n"
+ "                      \"tool_name\": \"get_weather\"\n"
+ "                    }\n"
+ "                  ],\n"
+ "                  \"model_info\": {\n"
+ "                    \"general.architecture\": \"llama\"\n"
+ "                  },\n"
+ "                  \"projector_info\": {\n"
+ "                    \"clip.has_text_encoder\": true\n"
+ "                  },\n"
+ "                  \"tensors\": [\n"
+ "                    {\n"
+ "                      \"name\": \"token_embd.weight\",\n"
+ "                      \"type\": \"F16\",\n"
+ "                      \"shape\": [\n"
+ "                        32000,\n"
+ "                        4096\n"
+ "                      ]\n"
+ "                    }\n"
+ "                  ],\n"
+ "                  \"capabilities\": [\n"
+ "                    \"completion\",\n"
+ "                    \"tools\"\n"
+ "                  ],\n"
+ "                  \"modified_at\": \"2025-01-02T03:04:05Z\"\n"
+ "                }\n"
+ "                ";

        OllamaModelCard modelCard = fromJson(json, OllamaModelCard.class);

        assertThat(modelCard.getLicense()).isEqualTo("Apache-2.0");
        assertThat(modelCard.getModelfile()).isEqualTo("FROM llama3.2");
        assertThat(modelCard.getParameters()).isEqualTo("temperature 0.8");
        assertThat(modelCard.getTemplate()).isEqualTo("{{ .Prompt }}");
        assertThat(modelCard.getSystem()).isEqualTo("You are helpful");
        assertThat(modelCard.getDetails().getParentModel()).isEqualTo("llama3.2");
        assertThat(modelCard.getModelInfo()).containsEntry("general.architecture", "llama");
        assertThat(modelCard.getProjectorInfo()).containsEntry("clip.has_text_encoder", true);
        assertThat(modelCard.getCapabilities()).containsExactly("completion", "tools");
        assertThat(modelCard.getModifiedAt()).isEqualTo(OffsetDateTime.parse("2025-01-02T03:04:05Z"));

        assertThat(modelCard.getMessages()).singleElement().satisfies(message -> {
            assertThat(message.getRole()).isEqualTo("system");
            assertThat(message.getContent()).isEqualTo("Use short answers");
            assertThat(message.getThinking()).isEqualTo("Internal reasoning");
            assertThat(message.getImages()).containsExactly("base64-image");
            assertThat(message.getToolName()).isEqualTo("get_weather");
            assertThat(message.getToolCalls())
                    .hasSize(2)
                    .satisfiesExactly(
                            toolCall -> {
                                assertThat(toolCall.getFunction().getIndex()).isEqualTo(0);
                                assertThat(toolCall.getFunction().getName()).isEqualTo("get_weather");
                                assertThat(toolCall.getFunction().getArguments())
                                        .containsEntry("city", "Paris");
                            },
                            toolCall -> {
                                assertThat(toolCall.getFunction().getIndex()).isNull();
                                assertThat(toolCall.getFunction().getName()).isEqualTo("get_time");
                                assertThat(toolCall.getFunction().getArguments())
                                        .containsEntry("city", "Paris");
                            });
        });
        assertThat(modelCard.getTensors()).singleElement().satisfies(tensor -> {
            assertThat(tensor.getName()).isEqualTo("token_embd.weight");
            assertThat(tensor.getType()).isEqualTo("F16");
            assertThat(tensor.getShape()).containsExactly(32000L, 4096L);
        });
    }

    @Test
    void should_deserialize_show_model_information_response_without_optional_fields() {
        String json = "                {\n"
+ "                  \"modelfile\": \"FROM llama3.2\",\n"
+ "                  \"template\": \"{{ .Prompt }}\",\n"
+ "                  \"details\": {\n"
+ "                    \"format\": \"gguf\"\n"
+ "                  }\n"
+ "                }\n"
+ "                ";

        OllamaModelCard modelCard = fromJson(json, OllamaModelCard.class);

        assertThat(modelCard.getModelfile()).isEqualTo("FROM llama3.2");
        assertThat(modelCard.getTemplate()).isEqualTo("{{ .Prompt }}");
        assertThat(modelCard.getSystem()).isNull();
        assertThat(modelCard.getMessages()).isNull();
        assertThat(modelCard.getProjectorInfo()).isNull();
        assertThat(modelCard.getTensors()).isNull();
        assertThat(modelCard.getDetails().getParentModel()).isNull();
    }

    @Test
    void should_build_with_all_model_card_fields() {
        OffsetDateTime modifiedAt = OffsetDateTime.parse("2025-01-02T03:04:05Z");
        OllamaModelDetails details = OllamaModelDetails.builder()
                .parentModel("llama3.2")
                .format("gguf")
                .build();
        List<OllamaModelMessage> messages = Arrays.asList(OllamaModelMessage.builder()
                .role("SYSTEM")
                .content("Use short answers")
                .thinking("Internal reasoning")
                .images(Arrays.asList("base64-image"))
                .toolCalls(Arrays.asList(OllamaModelToolCall.builder()
                        .function(OllamaModelToolCallFunction.builder()
                                .index(0)
                                .name("get_weather")
                                .arguments(Collections.singletonMap("city", "Paris"))
                                .build())
                        .build()))
                .toolName("get_weather")
                .build());
        Map<String, Object> modelInfo = Collections.singletonMap("general.architecture", "llama");
        Map<String, Object> projectorInfo = Collections.singletonMap("clip.has_text_encoder", true);
        List<OllamaModelTensor> tensors = Arrays.asList(OllamaModelTensor.builder()
                .name("token_embd.weight")
                .type("F16")
                .shape(Arrays.asList(32000L, 4096L))
                .build());

        OllamaModelCard modelCard = OllamaModelCard.builder()
                .license("Apache-2.0")
                .modelfile("FROM llama3.2")
                .parameters("temperature 0.8")
                .template("{{ .Prompt }}")
                .system("You are helpful")
                .details(details)
                .messages(messages)
                .modelInfo(modelInfo)
                .projectorInfo(projectorInfo)
                .tensors(tensors)
                .capabilities(Arrays.asList("completion", "tools"))
                .modifiedAt(modifiedAt)
                .build();

        assertThat(modelCard.getLicense()).isEqualTo("Apache-2.0");
        assertThat(modelCard.getModelfile()).isEqualTo("FROM llama3.2");
        assertThat(modelCard.getParameters()).isEqualTo("temperature 0.8");
        assertThat(modelCard.getTemplate()).isEqualTo("{{ .Prompt }}");
        assertThat(modelCard.getSystem()).isEqualTo("You are helpful");
        assertThat(modelCard.getDetails()).isSameAs(details);
        assertThat(modelCard.getMessages()).singleElement().satisfies(message -> {
            assertThat(message.getRole()).isEqualTo("system");
            assertThat(message.getToolCalls()).singleElement().satisfies(toolCall -> {
                assertThat(toolCall.getFunction().getIndex()).isEqualTo(0);
                assertThat(toolCall.getFunction().getName()).isEqualTo("get_weather");
            });
        });
        assertThat(modelCard.getModelInfo()).isSameAs(modelInfo);
        assertThat(modelCard.getProjectorInfo()).isSameAs(projectorInfo);
        assertThat(modelCard.getTensors()).isSameAs(tensors);
        assertThat(modelCard.getCapabilities()).containsExactly("completion", "tools");
        assertThat(modelCard.getModifiedAt()).isEqualTo(modifiedAt);
    }
}
