/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.Content
 *  com.google.cloud.vertexai.api.Part
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.Part;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.vertexai.gemini.PartsMapper;
import dev.langchain4j.model.vertexai.gemini.RoleMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ContentsMapper {
    ContentsMapper() {
    }

    static InstructionAndContent splitInstructionAndContent(List<ChatMessage> messages) {
        InstructionAndContent instructionAndContent = new InstructionAndContent();
        ArrayList<Part> sysInstructionParts = new ArrayList<Part>();
        ArrayList<Object> executionResultMessages = new ArrayList<ToolExecutionResultMessage>();
        for (int msgIdx = 0; msgIdx < messages.size(); ++msgIdx) {
            boolean isLastMessage;
            ChatMessage message = messages.get(msgIdx);
            boolean bl = isLastMessage = msgIdx == messages.size() - 1;
            if (message instanceof ToolExecutionResultMessage) {
                ToolExecutionResultMessage toolResult = (ToolExecutionResultMessage)message;
                if (isLastMessage) {
                    if (executionResultMessages.isEmpty()) {
                        instructionAndContent.contents.add(ContentsMapper.createContent(message));
                        continue;
                    }
                    executionResultMessages.add(toolResult);
                    instructionAndContent.contents.add(ContentsMapper.createToolExecutionResultContent(executionResultMessages));
                    continue;
                }
                executionResultMessages.add(toolResult);
                continue;
            }
            if (!executionResultMessages.isEmpty()) {
                instructionAndContent.contents.add(ContentsMapper.createToolExecutionResultContent(executionResultMessages));
                executionResultMessages = new ArrayList();
            }
            if (message instanceof UserMessage || message instanceof AiMessage) {
                instructionAndContent.contents.add(ContentsMapper.createContent(message));
                continue;
            }
            if (!(message instanceof SystemMessage)) continue;
            sysInstructionParts.addAll(PartsMapper.map(message));
        }
        if (!sysInstructionParts.isEmpty()) {
            instructionAndContent.systemInstruction = Content.newBuilder().setRole("system").addAllParts(sysInstructionParts).build();
        }
        return instructionAndContent;
    }

    private static Content createContent(ChatMessage message) {
        return Content.newBuilder().setRole(RoleMapper.map(message.type())).addAllParts(PartsMapper.map(message)).build();
    }

    private static Content createToolExecutionResultContent(List<ToolExecutionResultMessage> executionResultMessages) {
        return Content.newBuilder().setRole(RoleMapper.map(ChatMessageType.TOOL_EXECUTION_RESULT)).addAllParts((Iterable)executionResultMessages.stream().map(PartsMapper::map).flatMap(Collection::stream).collect(Collectors.toList())).build();
    }

    static class InstructionAndContent {
        public Content systemInstruction = null;
        public List<Content> contents = new ArrayList<Content>();

        InstructionAndContent() {
        }

        public String toString() {
            return "InstructionAndContent {\n systemInstruction = " + this.systemInstruction + ",\n contents = " + this.contents + "\n}";
        }
    }
}

