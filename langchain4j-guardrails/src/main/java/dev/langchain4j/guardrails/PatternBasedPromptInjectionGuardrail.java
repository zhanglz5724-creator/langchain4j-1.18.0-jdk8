/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.InputGuardrailResult
 *  dev.langchain4j.internal.ValidationUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.internal.ValidationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternBasedPromptInjectionGuardrail
implements InputGuardrail {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatternBasedPromptInjectionGuardrail.class);
    private static final List<Pattern> DEFAULT_PATTERNS = Arrays.asList(Pattern.compile("ignore\\s+(all\\s+|previous\\s+|prior\\s+|the\\s+)*(instructions?|rules?|context|prompt)", 2), Pattern.compile("forget\\s+(everything|all\\s+(rules|instructions)|prior\\s+context)", 2), Pattern.compile("disregard\\s+(all\\s+|your\\s+)?(previous\\s+|prior\\s+)?(instructions?|rules?|context)", 2), Pattern.compile("override\\s+(your\\s+|the\\s+)?(instructions?|rules?|system\\s+prompt)", 2), Pattern.compile("you\\s+are\\s+now\\s+(a|an)\\s+", 2), Pattern.compile("act\\s+as\\s+(a|an)\\s+", 2), Pattern.compile("pretend\\s+(to\\s+be|you\\s+are)", 2), Pattern.compile("roleplay\\s+as\\s+", 2), Pattern.compile("\\bDAN\\b"), Pattern.compile("developer\\s+mode", 2), Pattern.compile("bypass\\s+(all\\s+)?(safety|content|security)\\s+(filters?|restrictions?|checks?)", 2), Pattern.compile("jailbreak", 2), Pattern.compile("(reveal|print|show|tell|repeat|output)\\s+(me\\s+)?(your|the)\\s+(system\\s+|original\\s+|initial\\s+)?(prompt|instructions?)", 2), Pattern.compile("what\\s+(are|were)\\s+your\\s+(original\\s+|initial\\s+)?(instructions?|prompt)", 2), Pattern.compile("```\\s*system", 2), Pattern.compile("<\\s*system\\s*>", 2), Pattern.compile("\\[INST]", 2), Pattern.compile("<<SYS>>", 2), Pattern.compile("base64\\s*:", 2), Pattern.compile("decode\\s+(the\\s+following|this)\\s+and\\s+(execute|run)", 2));
    private static final String DEFAULT_FAILURE_MESSAGE = "Prompt injection attempt detected";
    private final List<Pattern> patterns;

    public PatternBasedPromptInjectionGuardrail() {
        this.patterns = DEFAULT_PATTERNS;
    }

    public PatternBasedPromptInjectionGuardrail(List<Pattern> additionalPatterns) {
        ValidationUtils.ensureNotNull(additionalPatterns, (String)"additionalPatterns");
        ArrayList<Pattern> combined = new ArrayList<Pattern>(DEFAULT_PATTERNS.size() + additionalPatterns.size());
        combined.addAll(DEFAULT_PATTERNS);
        combined.addAll(additionalPatterns);
        this.patterns = Collections.unmodifiableList(combined);
    }

    public InputGuardrailResult validate(UserMessage userMessage) {
        ValidationUtils.ensureNotNull((Object)userMessage, (String)"userMessage");
        String text = userMessage.singleText();
        if (text == null || text.trim().isEmpty()) {
            return this.success();
        }
        for (Pattern pattern : this.patterns) {
            if (!pattern.matcher(text).find()) continue;
            LOGGER.warn("Prompt injection detected. pattern='{}' input='{}'", (Object)pattern.pattern(), (Object)text);
            return this.fatal(this.buildFailureMessage(text, pattern));
        }
        return this.success();
    }

    protected String buildFailureMessage(String input, Pattern matchedPattern) {
        return DEFAULT_FAILURE_MESSAGE;
    }
}

