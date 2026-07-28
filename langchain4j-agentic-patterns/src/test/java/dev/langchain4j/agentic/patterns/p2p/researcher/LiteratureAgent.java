package dev.langchain4j.agentic.patterns.p2p.researcher;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface LiteratureAgent {

    @SystemMessage("Search for scientific literature on the given topic and return a summary of the findings.")
    @UserMessage("            You are a scientific literature search agent.\n"
+ "            Your task is to find relevant scientific papers on the topic provided by the user and summarize them.\n"
+ "            Use the provided tool to search for scientific papers and return a summary of your findings.\n"
+ "            The topic is: {{topic}}\n"
+ "            ")
    @Agent("Search for scientific literature on a given topic")
    String searchLiterature(@V("topic") String topic);
}
