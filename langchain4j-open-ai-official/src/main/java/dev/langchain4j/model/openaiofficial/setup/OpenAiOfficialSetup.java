/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.azure.AzureOpenAIServiceVersion
 *  com.openai.client.OpenAIClient
 *  com.openai.client.OpenAIClientAsync
 *  com.openai.client.okhttp.OpenAIOkHttpClient
 *  com.openai.client.okhttp.OpenAIOkHttpClient$Builder
 *  com.openai.client.okhttp.OpenAIOkHttpClientAsync
 *  com.openai.client.okhttp.OpenAIOkHttpClientAsync$Builder
 *  com.openai.credential.Credential
 *  dev.langchain4j.model.ModelProvider
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.openaiofficial.setup;

import com.openai.azure.AzureOpenAIServiceVersion;
import com.openai.client.OpenAIClient;
import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.credential.Credential;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.openaiofficial.setup.AzureInternalOpenAiOfficialHelper;
import java.net.Proxy;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAiOfficialSetup {
    static final String OPENAI_URL = "https://api.openai.com/v1";
    static final String OPENAI_API_KEY = "OPENAI_API_KEY";
    static final String MICROSOFT_FOUNDRY_KEY = "MICROSOFT_FOUNDRY_KEY";
    static final String AZURE_OPENAI_KEY = "AZURE_OPENAI_KEY";
    static final String GITHUB_MODELS_URL = "https://models.github.ai/inference";
    static final String GITHUB_TOKEN = "GITHUB_TOKEN";
    static final String DEFAULT_USER_AGENT = "langchain4j-openai-official";
    private static final Logger logger = LoggerFactory.getLogger(OpenAiOfficialSetup.class);
    private static final Duration DEFAULT_DURATION = Duration.ofSeconds(60L);
    private static final int DEFAULT_MAX_RETRIES = 3;

    public static OpenAIClient setupSyncClient(String baseUrl, String apiKey, Credential credential, String microsoftFoundryDeploymentName, AzureOpenAIServiceVersion azureOpenAiServiceVersion, String organizationId, boolean isMicrosoftFoundry, boolean isGitHubModels, String modelName, Duration timeout, Integer maxRetries, Proxy proxy, Map<String, String> customHeaders) {
        String calculatedApiKey;
        baseUrl = OpenAiOfficialSetup.detectBaseUrlFromEnv(baseUrl);
        ModelProvider modelProvider = OpenAiOfficialSetup.detectModelProvider(isMicrosoftFoundry, isGitHubModels, baseUrl, microsoftFoundryDeploymentName, azureOpenAiServiceVersion);
        if (timeout == null) {
            timeout = DEFAULT_DURATION;
        }
        if (maxRetries == null) {
            maxRetries = 3;
        }
        OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder();
        builder.baseUrl(OpenAiOfficialSetup.calculateBaseUrl(baseUrl, modelProvider, modelName, microsoftFoundryDeploymentName));
        String string = calculatedApiKey = apiKey != null ? apiKey : OpenAiOfficialSetup.detectApiKey(modelProvider);
        if ((modelProvider == ModelProvider.MICROSOFT_FOUNDRY || modelProvider == ModelProvider.AZURE_OPEN_AI) && credential != null) {
            builder.credential(credential);
        } else if (calculatedApiKey != null) {
            builder.apiKey(calculatedApiKey);
        } else if (credential != null) {
            builder.credential(credential);
        } else if (modelProvider == ModelProvider.MICROSOFT_FOUNDRY || modelProvider == ModelProvider.AZURE_OPEN_AI) {
            builder.credential(OpenAiOfficialSetup.azureAuthentication());
        }
        builder.organization(organizationId);
        if (azureOpenAiServiceVersion != null) {
            builder.azureServiceVersion(azureOpenAiServiceVersion);
        }
        if (proxy != null) {
            builder.proxy(proxy);
        }
        builder.putHeader("User-Agent", DEFAULT_USER_AGENT);
        if (customHeaders != null) {
            builder.putAllHeaders(customHeaders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> Collections.singletonList(entry.getValue()))));
        }
        builder.timeout(timeout);
        builder.maxRetries(maxRetries.intValue());
        return builder.build();
    }

    public static OpenAIClientAsync setupAsyncClient(String baseUrl, String apiKey, Credential credential, String microsoftFoundryDeploymentName, AzureOpenAIServiceVersion azureOpenAiServiceVersion, String organizationId, boolean isMicrosoftFoundry, boolean isGitHubModels, String modelName, Duration timeout, Integer maxRetries, Proxy proxy, Map<String, String> customHeaders) {
        String calculatedApiKey;
        baseUrl = OpenAiOfficialSetup.detectBaseUrlFromEnv(baseUrl);
        ModelProvider modelProvider = OpenAiOfficialSetup.detectModelProvider(isMicrosoftFoundry, isGitHubModels, baseUrl, microsoftFoundryDeploymentName, azureOpenAiServiceVersion);
        if (timeout == null) {
            timeout = DEFAULT_DURATION;
        }
        if (maxRetries == null) {
            maxRetries = 3;
        }
        OpenAIOkHttpClientAsync.Builder builder = OpenAIOkHttpClientAsync.builder();
        builder.baseUrl(OpenAiOfficialSetup.calculateBaseUrl(baseUrl, modelProvider, modelName, microsoftFoundryDeploymentName));
        String string = calculatedApiKey = apiKey != null ? apiKey : OpenAiOfficialSetup.detectApiKey(modelProvider);
        if ((modelProvider == ModelProvider.MICROSOFT_FOUNDRY || modelProvider == ModelProvider.AZURE_OPEN_AI) && credential != null) {
            builder.credential(credential);
        } else if (calculatedApiKey != null) {
            builder.apiKey(calculatedApiKey);
        } else if (credential != null) {
            builder.credential(credential);
        } else if (modelProvider == ModelProvider.MICROSOFT_FOUNDRY || modelProvider == ModelProvider.AZURE_OPEN_AI) {
            builder.credential(OpenAiOfficialSetup.azureAuthentication());
        }
        builder.organization(organizationId);
        if (azureOpenAiServiceVersion != null) {
            builder.azureServiceVersion(azureOpenAiServiceVersion);
        }
        if (proxy != null) {
            builder.proxy(proxy);
        }
        builder.putHeader("User-Agent", DEFAULT_USER_AGENT);
        if (customHeaders != null) {
            builder.putAllHeaders(customHeaders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> Collections.singletonList(entry.getValue()))));
        }
        builder.timeout(timeout);
        builder.maxRetries(maxRetries.intValue());
        return builder.build();
    }

    static String detectBaseUrlFromEnv(String baseUrl) {
        if (baseUrl == null) {
            String azureOpenAiBaseUrl;
            String openAiBaseUrl = System.getenv("OPENAI_BASE_URL");
            if (openAiBaseUrl != null) {
                baseUrl = openAiBaseUrl;
                logger.debug("OpenAI Base URL detected from environment variable OPENAI_BASE_URL.");
            }
            if ((azureOpenAiBaseUrl = System.getenv("AZURE_OPENAI_BASE_URL")) != null) {
                baseUrl = azureOpenAiBaseUrl;
                logger.debug("Microsoft Foundry Base URL detected from environment variable AZURE_OPENAI_BASE_URL.");
            }
        }
        return baseUrl;
    }

    public static ModelProvider detectModelProvider(boolean isMicrosoftFoundry, boolean isGitHubModels, String baseUrl, String microsoftFoundryDeploymentName, AzureOpenAIServiceVersion azureOpenAIServiceVersion) {
        if (isMicrosoftFoundry) {
            return ModelProvider.MICROSOFT_FOUNDRY;
        }
        if (isGitHubModels) {
            return ModelProvider.GITHUB_MODELS;
        }
        if (baseUrl != null) {
            if (baseUrl.endsWith("openai.azure.com") || baseUrl.endsWith("openai.azure.com/") || baseUrl.endsWith("cognitiveservices.azure.com") || baseUrl.endsWith("cognitiveservices.azure.com/") || baseUrl.endsWith("ai.azure.com") || baseUrl.endsWith("ai.azure.com/")) {
                return ModelProvider.MICROSOFT_FOUNDRY;
            }
            if (baseUrl.startsWith(GITHUB_MODELS_URL)) {
                return ModelProvider.GITHUB_MODELS;
            }
        }
        if (microsoftFoundryDeploymentName != null || azureOpenAIServiceVersion != null) {
            return ModelProvider.MICROSOFT_FOUNDRY;
        }
        return ModelProvider.OPEN_AI;
    }

    static String calculateBaseUrl(String baseUrl, ModelProvider modelProvider, String modelName, String microsoftFoundryDeploymentName) {
        if (modelProvider == ModelProvider.OPEN_AI) {
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                return OPENAI_URL;
            }
            return baseUrl;
        }
        if (modelProvider == ModelProvider.GITHUB_MODELS) {
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                return GITHUB_MODELS_URL;
            }
            if (baseUrl.startsWith(GITHUB_MODELS_URL)) {
                return baseUrl;
            }
            return GITHUB_MODELS_URL;
        }
        if (modelProvider == ModelProvider.MICROSOFT_FOUNDRY) {
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("Base URL must be provided for Microsoft Foundry.");
            }
            String tmpUrl = baseUrl;
            if (baseUrl.endsWith("/") || baseUrl.endsWith("?")) {
                tmpUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            if (microsoftFoundryDeploymentName != null && !microsoftFoundryDeploymentName.equals(modelName)) {
                tmpUrl = tmpUrl + "/openai/deployments/" + microsoftFoundryDeploymentName;
            }
            return tmpUrl;
        }
        throw new IllegalArgumentException("Unknown model provider: " + modelProvider);
    }

    static Credential azureAuthentication() {
        try {
            return AzureInternalOpenAiOfficialHelper.getAzureCredential();
        }
        catch (NoClassDefFoundError e) {
            throw new IllegalArgumentException("Microsoft Foundry was detected, but no credential was provided. If you want to use passwordless authentication, you need to add the Azure Identity library (groupId=`com.azure`, artifactId=`azure-identity`) to your classpath.");
        }
    }

    static String detectApiKey(ModelProvider modelProvider) {
        if (modelProvider == ModelProvider.OPEN_AI && System.getenv(OPENAI_API_KEY) != null) {
            return System.getenv(OPENAI_API_KEY);
        }
        if (modelProvider == ModelProvider.MICROSOFT_FOUNDRY && System.getenv(MICROSOFT_FOUNDRY_KEY) != null) {
            return System.getenv(MICROSOFT_FOUNDRY_KEY);
        }
        if (modelProvider == ModelProvider.MICROSOFT_FOUNDRY && System.getenv(AZURE_OPENAI_KEY) != null) {
            return System.getenv(AZURE_OPENAI_KEY);
        }
        if (modelProvider == ModelProvider.MICROSOFT_FOUNDRY && System.getenv(OPENAI_API_KEY) != null) {
            return System.getenv(OPENAI_API_KEY);
        }
        if (modelProvider == ModelProvider.GITHUB_MODELS && System.getenv(GITHUB_TOKEN) != null) {
            return System.getenv(GITHUB_TOKEN);
        }
        return null;
    }
}

