package dev.langchain4j.agentic.patterns.voting.critic;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.observability.MonitoredAgent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class CriticAgents {

    public record CritiqueResult(double score, String suggestions) {}

    public record ScoredStory(String story, double score, String suggestions) {}

    public interface CreativeWriter {

        @UserMessage("                You are a creative writer.\n"
+ "                Generate a short story of no more than 3 sentences around the given topic.\n"
+ "                Return only the story and nothing else.\n"
+ "                The topic is: {{topic}}\n"
+ "                ")
        @Agent(value = "Generate a short story based on the given topic", outputKey = "story")
        String generateStory(@V("topic") String topic);
    }

    public interface StyleCritic {

        @UserMessage("                You are a literary style critic.\n"
+ "                Evaluate the writing style of the following story.\n"
+ "                Consider prose quality, word choice, and narrative flow.\n"
+ "                Return a JSON object with two fields:\n"
+ "                - \"score\": a numeric value from 0.0 to 10.0\n"
+ "                - \"suggestions\": one or two very short suggestions to improve style\n"
+ "                The story is: \"{{story}}\"\n"
+ "                ")
        @Agent(value = "Evaluate the writing style of a story", outputKey = "styleCritique")
        CritiqueResult critique(@V("story") String story);
    }

    public interface OriginalityCritic {

        @UserMessage("                You are an originality critic.\n"
+ "                Evaluate how creative and original the following story is.\n"
+ "                Consider uniqueness of the concept, unexpected twists, and imaginative elements.\n"
+ "                Return a JSON object with two fields:\n"
+ "                - \"score\": a numeric value from 0.0 to 10.0\n"
+ "                - \"suggestions\": one or two very short suggestions to improve originality\n"
+ "                The story is: \"{{story}}\"\n"
+ "                ")
        @Agent(value = "Evaluate the originality of a story", outputKey = "originalityCritique")
        CritiqueResult critique(@V("story") String story);
    }

    public interface EngagementCritic {

        @UserMessage("                You are a reader engagement critic.\n"
+ "                Evaluate how engaging and captivating the following story is.\n"
+ "                Consider whether it hooks the reader, creates tension, and has a satisfying arc.\n"
+ "                Return a JSON object with two fields:\n"
+ "                - \"score\": a numeric value from 0.0 to 10.0\n"
+ "                - \"suggestions\": one or two very short suggestions to improve engagement\n"
+ "                The story is: \"{{story}}\"\n"
+ "                ")
        @Agent(value = "Evaluate how engaging a story is", outputKey = "engagementCritique")
        CritiqueResult critique(@V("story") String story);
    }

    public interface StoryEditor {

        @UserMessage("                You are a professional story editor.\n"
+ "                Rewrite and improve the following story based on the provided critique.\n"
+ "                Keep the story to no more than 3 sentences.\n"
+ "                Return only the improved story and nothing else.\n"
+ "                The story is: \"{{story}}\"\n"
+ "                The critique is: {{critique}}\n"
+ "                ")
        @Agent(value = "Improve a story based on critique suggestions", outputKey = "story")
        String edit(@V("story") String story, @V("critique") CritiqueResult critique);
    }

    public interface StoryEvaluator extends MonitoredAgent {

        @Agent
        ScoredStory evaluate(@V("topic") String topic);
    }
}
