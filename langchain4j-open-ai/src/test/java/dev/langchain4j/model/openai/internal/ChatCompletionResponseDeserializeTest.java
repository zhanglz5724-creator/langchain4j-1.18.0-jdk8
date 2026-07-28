package dev.langchain4j.model.openai.internal;

import static org.assertj.core.api.Assertions.assertThat;

import dev.langchain4j.model.openai.internal.chat.ChatCompletionChoice;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionResponse;
import dev.langchain4j.model.openai.internal.chat.ToolCall;
import org.junit.jupiter.api.Test;

class ChatCompletionResponseDeserializeTest {

    @Test
    void should_deserialize_chat_response_without_tool_type() {

        // given
        String json = "{\n"
                + "    \"id\": \"0195a749b17b5668b9753240788da6f8\",\n"
                + "    \"object\": \"chat.completion.chunk\",\n"
                + "    \"created\": 1742268380,\n"
                + "    \"model\": \"deepseek-ai/DeepSeek-V3\",\n"
                + "    \"choices\": [\n"
                + "        {\n"
                + "            \"index\": 0,\n"
                + "            \"delta\": {\n"
                + "                \"content\": null,\n"
                + "                \"reasoning_content\": null,\n"
                + "                \"tool_calls\": [\n"
                + "                    {\n"
                + "                        \"index\": 0,\n"
                + "                        \"id\": \"\",\n"
                + "                        \"type\": \"\",\n"
                + "                        \"function\": {\n"
                + "                            \"arguments\": \"{\\\"\"\n"
                + "                        }\n"
                + "                    }\n"
                + "                ]\n"
                + "            },\n"
                + "            \"finish_reason\": null\n"
                + "        }\n"
                + "    ],\n"
                + "    \"system_fingerprint\": \"\",\n"
                + "    \"usage\": {\n"
                + "        \"prompt_tokens\": 83,\n"
                + "        \"completion_tokens\": 2,\n"
                + "        \"total_tokens\": 85\n"
                + "    }\n"
                + "}";

        // when
        ChatCompletionResponse response = Json.fromJson(json, ChatCompletionResponse.class);

        // then
        ChatCompletionChoice chatCompletionChoice = response.choices().get(0);
        ToolCall toolCall = chatCompletionChoice.delta().toolCalls().get(0);
        assertThat(toolCall.function().arguments()).isEqualTo("{\"");
    }
}
