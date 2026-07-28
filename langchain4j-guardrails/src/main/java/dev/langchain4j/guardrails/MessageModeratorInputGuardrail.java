/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.InputGuardrailResult
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.moderation.Moderation
 *  dev.langchain4j.model.moderation.ModerationModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.service.ModerationException
 */
package dev.langchain4j.guardrails;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.ModerationException;

public class MessageModeratorInputGuardrail
implements InputGuardrail {
    private final ModerationModel moderationModel;

    public MessageModeratorInputGuardrail(ModerationModel moderationModel) {
        this.moderationModel = (ModerationModel)ValidationUtils.ensureNotNull((Object)moderationModel, (String)"moderationModel");
    }

    public InputGuardrailResult validate(UserMessage userMessage) {
        Response response = this.moderationModel.moderate((ChatMessage)userMessage);
        if (((Moderation)response.content()).flagged()) {
            return this.fatal("User message has been flagged", (Throwable)new ModerationException("User message has been flagged", (Moderation)response.content()));
        }
        return this.success();
    }
}

