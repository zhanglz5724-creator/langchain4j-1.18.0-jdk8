package dev.langchain4j.model.mistralai.common;

import static dev.langchain4j.model.mistralai.common.MistralAiChatModelIT.MISTRAL_CHAT_MODEL;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.common.AbstractAiServiceIT;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.List;
import java.util.Arrays;

@EnabledIfEnvironmentVariable(named = "MISTRAL_AI_API_KEY", matches = ".+")
class MistralAiAiServiceIT extends AbstractAiServiceIT {

    @Override
    protected List<ChatModel> models() {
        return Arrays.asList(MISTRAL_CHAT_MODEL);
    }
}
