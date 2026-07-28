package dev.langchain4j.agentic.patterns.debate.codereview;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.patterns.debate.ConvergenceStrategy;
import dev.langchain4j.agentic.patterns.debate.DebatePlanner;
import dev.langchain4j.agentic.patterns.debate.codereview.CodeReviewAgents.BugHunter;
import dev.langchain4j.agentic.patterns.debate.codereview.CodeReviewAgents.CodeReviewPanel;
import dev.langchain4j.agentic.patterns.debate.codereview.CodeReviewAgents.DesignCritic;
import dev.langchain4j.agentic.patterns.debate.codereview.CodeReviewAgents.ReviewSummarizer;
import dev.langchain4j.agentic.patterns.debate.codereview.CodeReviewAgents.SecurityReviewer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static dev.langchain4j.agentic.patterns.Models.baseModel;
import static org.assertj.core.api.Assertions.assertThat;

@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class CodeReviewDebateIT {

    private static final String CONTROVERSIAL_CODE = "            public class UserService {\n"
+ "\n"
+ "                private Map<String, String> cache = new HashMap<>();\n"
+ "\n"
+ "                public String getUserRole(HttpServletRequest request) {\n"
+ "                    String userId = request.getParameter(\"userId\");\n"
+ "                    if (cache.containsKey(userId)) {\n"
+ "                        return cache.get(userId);\n"
+ "                    }\n"
+ "                    String query = \"SELECT role FROM users WHERE id = '\" + userId + \"'\";\n"
+ "                    String role = db.executeQuery(query).getString(\"role\");\n"
+ "                    cache.put(userId, role);\n"
+ "                    return role;\n"
+ "                }\n"
+ "\n"
+ "                public void deleteUser(String userId) {\n"
+ "                    db.executeUpdate(\"DELETE FROM users WHERE id = \" + userId);\n"
+ "                    cache.remove(userId);\n"
+ "                }\n"
+ "            }\n"
+ "            ";

    @Test
    void code_review_debate_on_controversial_snippet() {
        BugHunter bugHunter = AgenticServices.agentBuilder(BugHunter.class)
                .chatModel(baseModel())
                .outputKey("bugs")
                .build();

        SecurityReviewer securityReviewer = AgenticServices.agentBuilder(SecurityReviewer.class)
                .chatModel(baseModel())
                .outputKey("security")
                .build();

        DesignCritic designCritic = AgenticServices.agentBuilder(DesignCritic.class)
                .chatModel(baseModel())
                .outputKey("design")
                .build();

        ReviewSummarizer summarizer = AgenticServices.agentBuilder(ReviewSummarizer.class)
                .chatModel(baseModel())
                .outputKey("summary")
                .build();

        CodeReviewPanel panel = AgenticServices.plannerBuilder(CodeReviewPanel.class)
                .subAgents(bugHunter, securityReviewer, designCritic, summarizer)
                .outputKey("summary")
                .planner(() -> new DebatePlanner(3, ConvergenceStrategy.unanimousLastWord()))
                .build();

        String result = panel.review(CONTROVERSIAL_CODE);

        assertThat(result).isNotBlank();
        System.out.println("Code Review Summary:\n" + result);
    }
}
