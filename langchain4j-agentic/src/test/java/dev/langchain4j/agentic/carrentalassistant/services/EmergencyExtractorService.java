package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.carrentalassistant.domain.CustomerInfo;
import dev.langchain4j.agentic.carrentalassistant.domain.Emergencies;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI service interface for the fire emergency assistant.
 */
public interface EmergencyExtractorService {
    
    @SystemMessage("        You are an emergencies handler.\n"
+ "        Your role is to:\n"
+ "        1. Analyze customer messages to identify emergencies\n"
+ "        2. Determine the type of emergency among police, medical and fire. There could be multiple emergencies in one message.\n"
+ "        3. Extract relevant emergency information and put them into the corresponding field in the Emergencies object.\n"
+ "        4. If no emergency is detected for a specific emergency type leave the corresponding field blank.\n"
+ "        ")
    @UserMessage("        I'm the customer: {{customerInfo}}\n"
+ "        My message is: {{message}}\n"
+ "        ")
    @Agent
    Emergencies extractEmergencies(@V("message") String message, @V("customerInfo") CustomerInfo customerInfo);
}
