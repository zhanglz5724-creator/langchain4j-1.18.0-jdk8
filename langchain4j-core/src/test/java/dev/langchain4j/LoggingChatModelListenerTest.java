package dev.langchain4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingChatModelListenerTest {

    @Test
    void formatArguments() {
        assertThat(LoggingChatModelListener.formatArguments("{\n" +
                "    \"arg0\": 0\n" +
                "}")).isEqualTo("0");

        assertThat(LoggingChatModelListener.formatArguments("{\n" +
                "    \"arg0\": 0,\n" +
                "    \"arg1\": 1\n" +
                "}")).isEqualTo("0, 1");

        assertThat(LoggingChatModelListener.formatArguments("{\n" +
                "    \"a\": 0\n" +
                "}")).isEqualTo("0");

        assertThat(LoggingChatModelListener.formatArguments("{\n" +
                "    \"a\": 0,\n" +
                "    \"b\": 1\n" +
                "}")).isEqualTo("{\"a\":0,\"b\":1}");
    }
}