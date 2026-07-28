package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface EmergencyResponseService {

    @SystemMessage("        You are an agent that integrates responses from fire, medical, and police emergency services.\n"
+ "        Your role is to collect and integrate responses from different emergency services into a single coherent message.\n"
+ "        ")
    @UserMessage("        Fire emergency: {{fireResponse}}\n"
+ "        \n"
+ "        Medical emergency: {{medicalResponse}}\n"
+ "        \n"
+ "        Police emergency: {{policeResponse}}\n"
+ "        ")
    @Agent
    String summarizeEmergencies(@V("fireResponse") String fireResponse, @V("medicalResponse") String medicalResponse, @V("policeResponse") String policeResponse);
}
