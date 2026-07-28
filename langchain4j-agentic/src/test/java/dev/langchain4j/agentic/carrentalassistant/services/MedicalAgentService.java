package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.carrentalassistant.domain.CustomerInfo;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI service interface for the medical emergency assistant.
 */
public interface MedicalAgentService {
    
    @SystemMessage("        You are a medical emergency assistant for a car rental company.\n"
+ "        Your role is to:\n"
+ "        1. Assess medical emergencies involving rental car customers\n"
+ "        2. Determine the appropriate medical response\n"
+ "        3. Provide first aid guidance when appropriate\n"
+ "        4. Simulate dispatching medical assistance when necessary\n"
+ "        \n"
+ "        This is a serious responsibility. Prioritize customer safety above all else.\n"
+ "        Always maintain a calm, clear, and reassuring tone.\n"
+ "        Respond in a JSON schema that matches {response_schema}\n"
+ "        ")
    @UserMessage("        I'm the customer: {{customerInfo}}\n"
+ "        I have a medical emergency: {{medicalEmergency}}\n"
+ "        What should I do?\n"
+ "        ")
    @Agent
    String handleMedicalEmergency(@MemoryId String memoryId, @V("medicalEmergency") String medicalEmergency, @V("customerInfo") CustomerInfo customerInfo);
}
