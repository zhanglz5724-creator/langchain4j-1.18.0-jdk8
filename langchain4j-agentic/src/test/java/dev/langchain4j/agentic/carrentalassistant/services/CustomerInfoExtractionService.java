package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.carrentalassistant.domain.CustomerInfo;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * AI service interface for extracting customer information from messages.
 */
public interface CustomerInfoExtractionService {
    
    @SystemMessage("        You are a customer information extraction assistant for a car rental company.\n"
+ "        Your role is to analyze the history of customer messages and extract relevant information.\n"
+ "        \n"
+ "        Extract the following information when present:\n"
+ "        - Customer name\n"
+ "        - Customer ID\n"
+ "        - Booking reference number\n"
+ "        - Car make (brand)\n"
+ "        - Car model\n"
+ "        - Car year\n"
+ "        - Current location\n"
+ "        \n"
+ "        Only extract information that is explicitly mentioned in the message.\n"
+ "        Do not make assumptions or infer information that isn't clearly stated.\n"
+ "        If a piece of information is not present, leave that field null.\n"
+ "        ")
    @UserMessage("        Extract customer information from this message:\n"
+ "        {{message}}\n"
+ "        and update the existing customer information:\n"
+ "        {{customerInfo}}\n"
+ "        ")
    @Agent("Extract customer information from user message")
    CustomerInfo extractCustomerInfo(@MemoryId String memoryId, @V("message") String message, @V("customerInfo") CustomerInfo customerInfo);
}
