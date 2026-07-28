/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.UserMessage
 *  dev.langchain4j.service.V
 */
package dev.langchain4j.agentic.patterns.p2p;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import java.util.Collection;
import java.util.Map;

public interface VariablesExtractorAgent {
    @UserMessage(value={"Extract the values of the given list of variables from the provided text,\nreturning a map where the keys are the variable names and the values are the corresponding extracted values.\nIf a variable is not found in the text, it should not be included in the map.\nBe conservative in your extraction, only include values that are clearly present in the text.\nThe text is: {{text}}\nThe names of the variables to extract are: {{variableNames}}\n"})
    public Map<String, String> extractVariables(@V(value="text") String var1, @V(value="variableNames") Collection<String> var2);
}

