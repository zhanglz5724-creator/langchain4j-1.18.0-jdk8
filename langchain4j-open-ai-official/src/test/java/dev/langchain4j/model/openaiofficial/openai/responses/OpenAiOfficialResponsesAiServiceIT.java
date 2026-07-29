package dev.langchain4j.model.openaiofficial.openai.responses;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialResponsesChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialTokenUsage;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.common.AbstractAiServiceIT;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.Collections;
import java.util.List;

@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class OpenAiOfficialResponsesAiServiceIT extends AbstractAiServiceIT {

    @Override
    protected List<ChatModel> models() {
        ChatModel model = OpenAiOfficialResponsesChatModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-5.4-mini")
                .build();

        return Collections.singletonList(model);
    }

    @Override
    protected Class<? extends TokenUsage> tokenUsageType(ChatModel model) {
        return OpenAiOfficialTokenUsage.class;
    }
}
