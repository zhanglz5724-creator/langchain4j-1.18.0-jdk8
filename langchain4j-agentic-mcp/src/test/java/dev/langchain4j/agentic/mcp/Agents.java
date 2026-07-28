package dev.langchain4j.agentic.mcp;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.ExitCondition;
import dev.langchain4j.agentic.declarative.LoopAgent;
import dev.langchain4j.agentic.declarative.McpClientAgent;
import dev.langchain4j.agentic.declarative.McpClientSupplier;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class Agents {

    public interface CreativeWriter {

        @UserMessage("                You are a creative writer.\n"
+ "                Generate a draft of a story long no more than 3 sentence around the given topic.\n"
+ "                Return only the story and nothing else.\n"
+ "                The topic is {{topic}}.\n"
+ "                ")
        @Agent(description = "Generate a story based on the given topic", outputKey = "story")
        String generateStory(@V("topic") String topic);
    }

    public interface StyleEditor {

        @UserMessage(
                "                You are a professional editor.\n"
+ "                Analyze and rewrite the following story to better fit and be more coherent with the {{style}} style.\n"
+ "                Return only the story and nothing else.\n"
+ "                The story is \"{{story}}\".\n"
+ "                ")
        @Agent(description = "Edit a story to better fit a given style", outputKey = "story")
        String editStory(@V("story") String story, @V("style") String style);
    }

    public interface StyleScorer {

        @UserMessage(
                "                You are a critical reviewer.\n"
+ "                Give a review score between 0.0 and 1.0 for the following story based on how well it aligns with the style '{{style}}'.\n"
+ "                Return only the score and nothing else.\n"
+ "\n"
+ "                The story is: \"{{story}}\"\n"
+ "                ")
        @Agent(description = "Score a story based on how well it aligns with a given style", outputKey = "score")
        double scoreStyle(@V("story") String story, @V("style") String style);
    }

    public interface StyleReviewLoop {

        @Agent("Review the given story to ensure it aligns with the specified style")
        String scoreAndReview(@V("story") String story, @V("style") String style);
    }

    public interface StyledWriter extends AgenticScopeAccess {

        @Agent
        ResultWithAgenticScope<String> writeStoryWithStyle(@V("topic") String topic, @V("style") String style);
    }

    public interface McpToolAgent {

        @Agent
        String execute(@V("input") String input);
    }

    public interface DeclarativeMcpStoryGenerator {

        @McpClientAgent(toolName = "writer", outputKey = "story")
        String generateStory(@V("topic") String topic);

        @McpClientSupplier
        static Object mcpClient() {
            return McpAgentIT.mcpClient;
        }
    }

    public interface StyleReviewLoopAgent {

        @LoopAgent(
                description = "Review and score the given story to ensure it aligns with the specified style",
                outputKey = "story",
                maxIterations = 5,
                subAgents = { StyleScorer.class, StyleEditor.class }
        )
        String reviewAndScore(@V("story") String story);

        @ExitCondition
        static boolean exit(@V("score") double score) {
            return score >= 0.8;
        }
    }

    public interface StoryCreatorWithReview {

        @SequenceAgent(
                outputKey = "story",
                subAgents = { DeclarativeMcpStoryGenerator.class, StyleReviewLoopAgent.class }
        )
        ResultWithAgenticScope<String> write(@V("topic") String topic, @V("style") String style);
    }
}
