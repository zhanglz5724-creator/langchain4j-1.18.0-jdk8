/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.credential.TokenCredential
 *  com.azure.identity.AuthenticationUtil
 *  com.azure.identity.DefaultAzureCredentialBuilder
 *  com.openai.credential.BearerTokenCredential
 *  com.openai.credential.Credential
 */
package dev.langchain4j.model.openaiofficial.setup;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.AuthenticationUtil;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.openai.credential.BearerTokenCredential;
import com.openai.credential.Credential;
import java.util.function.Supplier;

class AzureInternalOpenAiOfficialHelper {
    AzureInternalOpenAiOfficialHelper() {
    }

    static Credential getAzureCredential() {
        return BearerTokenCredential.create((Supplier)AuthenticationUtil.getBearerTokenSupplier((TokenCredential)new DefaultAzureCredentialBuilder().build(), (String[])new String[]{"https://cognitiveservices.azure.com/.default"}));
    }
}

