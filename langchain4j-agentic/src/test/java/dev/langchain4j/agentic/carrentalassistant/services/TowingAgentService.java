package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.carrentalassistant.domain.CustomerInfo;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI service interface for the towing assistant.
 */
public interface TowingAgentService {
    
    @SystemMessage("        You are a towing assistant for a car rental company.\n"
+ "        Your role is to:\n"
+ "        1. Determine if a customer required or needs towing services otherwise simply respond with \"No towing required\"\n"
+ "        2. Collect necessary information about the vehicle and its condition\n"
+ "        3. Determine the type of towing required (flatbed or standard)\n"
+ "        4. Assess the safety of the towing location\n"
+ "        5. Simulate dispatching a tow truck to the customer's location\n"
+ "        \n"
+ "        Always maintain a professional, helpful tone.\n"
+ "        Respond in a JSON schema that matches {response_schema}\n"
+ "        ")
    @UserMessage("       I'm the customer: {{customerInfo}}\n"
+ "       Customer message is: {{message}}\n"
+ "       ")
    @Agent
    String processTowingRequest(@MemoryId String memoryId, @V("message") String message, @V("customerInfo") CustomerInfo customerInfo);
}
