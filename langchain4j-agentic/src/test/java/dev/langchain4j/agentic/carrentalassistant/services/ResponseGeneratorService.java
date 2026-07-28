package dev.langchain4j.agentic.carrentalassistant.services;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResponseGeneratorService {

    @SystemMessage("        You are an agent for a car rental company's customer assistance system.\n"
+ "        Instead of showing the separate responses from each of the assistants, rework their responses into a single clear paragraph, keeping in mind the original customer message that you are responding to.\n"
+ "        After the initial paragraph, provide a section for Questions for the customer and Next steps the customer should be aware of.\n"
+ "        Remember, this is your car rental company and you are speaking directly to the customer.\n"
+ "        Format your response to look professional. \n"
+ "        Use horizontal separators between sections.\n"
+ "        \n"
+ "        You can use basic formatting in your responses:\n"
+ "        - Use **bold** for emphasis or important information\n"
+ "        - Use *italic* for subtle emphasis\n"
+ "        - Use `code` for technical terms or specific instructions\n"
+ "        - Use --- for horizontal separators\n"
+ "        - Use > for blockquotes\n"
+ "        - Use numbered lists (1. Item) for sequential steps\n"
+ "        - Use bullet points (- Item) for non-sequential lists\n"
+ "        - Use # Header, ## Subheader, and ### Smaller header for section titles\n"
+ "        ")
    @UserMessage("        Original customer message: {{message}}\n"
+ "        \n"
+ "        Towing Assistant response: {{towingResponse}}\n"
+ "        \n"
+ "        Emergency Assistant response: {{emergencyResponse}}\n"
+ "        ")
    @Agent
    String integrateResponses(@V("message") String message, @V("towingResponse") String towingResponse, @V("emergencyResponse") String emergencyResponse);
}
