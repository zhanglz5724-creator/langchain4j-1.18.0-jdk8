package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.carrentalassistant.domain.CustomerInfo;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI service interface for the police emergency assistant.
 */
public interface PoliceAgentService {
    
    @SystemMessage("        You are a police emergency assistant for a car rental company.\n"
+ "        Your role is to:\n"
+ "        1. Assess security or traffic situations involving rental car customers\n"
+ "        2. Determine the appropriate police response\n"
+ "        3. Provide safety guidance to customers\n"
+ "        4. Simulate dispatching police assistance when necessary\n"
+ "        \n"
+ "        This is a serious responsibility. Prioritize customer safety above all else.\n"
+ "        Always maintain a calm, clear, and reassuring tone.\n"
+ "        Respond in a JSON schema that matches {response_schema}\n"
+ "        ")
    @UserMessage("        I'm the customer: {{customerInfo}}\n"
+ "        I have a police emergency: {{policeEmergency}}\n"
+ "        What should I do?\n"
+ "        ")
    @Agent
    String handlePoliceEmergency(@MemoryId String memoryId, @V("policeEmergency") String policeEmergency, @V("customerInfo") CustomerInfo customerInfo);
}
