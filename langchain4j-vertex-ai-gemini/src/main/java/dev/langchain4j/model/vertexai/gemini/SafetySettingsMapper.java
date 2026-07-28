/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.HarmCategory
 *  com.google.cloud.vertexai.api.SafetySetting
 *  com.google.cloud.vertexai.api.SafetySetting$Builder
 *  com.google.cloud.vertexai.api.SafetySetting$HarmBlockThreshold
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.SafetySetting;
import dev.langchain4j.model.vertexai.gemini.HarmCategory;
import dev.langchain4j.model.vertexai.gemini.SafetyThreshold;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class SafetySettingsMapper {
    SafetySettingsMapper() {
    }

    static List<SafetySetting> mapSafetySettings(Map<HarmCategory, SafetyThreshold> safetySettingsMap) {
        return safetySettingsMap.entrySet().stream().map(entry -> {
            SafetySetting.Builder safetySettingBuilder = SafetySetting.newBuilder();
            safetySettingBuilder.setCategory(SafetySettingsMapper.map((HarmCategory)((Object)((Object)entry.getKey()))));
            safetySettingBuilder.setThreshold(SafetySettingsMapper.map((SafetyThreshold)((Object)((Object)entry.getValue()))));
            return safetySettingBuilder.build();
        }).collect(Collectors.toList());
    }

    private static com.google.cloud.vertexai.api.HarmCategory map(HarmCategory harmCategory) {
        return com.google.cloud.vertexai.api.HarmCategory.valueOf((String)harmCategory.name());
    }

    private static SafetySetting.HarmBlockThreshold map(SafetyThreshold safetyThreshold) {
        return SafetySetting.HarmBlockThreshold.valueOf((String)safetyThreshold.name());
    }
}

