package dev.langchain4j.code.graalvm;

import dev.langchain4j.code.CodeExecutionEngine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GraalVmJavaScriptExecutionEngineTest {

    CodeExecutionEngine engine = new GraalVmJavaScriptExecutionEngine();

    @Test
    void should_execute_code() {

        String code = "function fibonacci(n) {\n" +
                "    if (n <= 1) return n;\n" +
                "    return fibonacci(n - 1) + fibonacci(n - 2);\n" +
                "}\n" +
                "                \n" +
                "fibonacci(10)\n";

        String result = engine.execute(code);

        assertThat(result).isEqualTo("55");
    }
}