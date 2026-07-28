package dev.langchain4j.agentic.patterns.p2p.researcher;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ValidationAgent {

    @SystemMessage("Validate the provided hypothesis on the given topic based on the critique provided.")
    @UserMessage("            You are a validation agent.\n"
+ "            Your task is to validate the hypothesis provided by the user in relation to the specified topic based on the critique provided.\n"
+ "            Validate the provided hypothesis, either confirming it or reformulating a different hypothesis based on the critique.\n"
+ "            The topic is: {{topic}}\n"
+ "            The hypothesis is: {{hypothesis}}\n"
+ "            The critique is: {{critique}}\n"
+ "            ")
    @Agent("Validate a hypothesis based on a given topic and critique")
    String validateHypothesis(@V("topic") String topic, @V("hypothesis") String hypothesis, @V("critique") String critique);
}
