package dev.langchain4j.model.watsonx;

import static dev.langchain4j.model.ModelProvider.WATSONX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.ibm.watsonx.ai.CloudRegion;
import com.ibm.watsonx.ai.core.auth.ibmcloud.IBMCloudAuthenticator;
import com.ibm.watsonx.ai.core.provider.HttpClientProvider;
import com.ibm.watsonx.ai.foundationmodel.FoundationModelService;
import dev.langchain4j.model.catalog.ModelType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("unchecked")
public class WatsonxModelCatalogTest {

    @Mock
    FoundationModelService mockFoundationModelService;

    @Mock
    FoundationModelService.Builder mockFoundationModelServiceBuilder;

    @BeforeEach
    void setUp() {
        when(mockFoundationModelServiceBuilder.baseUrl(any(URI.class))).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.timeout(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.version(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.logRequests(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.logResponses(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.authenticator(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.apiKey(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.httpClient(any())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.verifySsl(anyBoolean())).thenReturn(mockFoundationModelServiceBuilder);
        when(mockFoundationModelServiceBuilder.build()).thenReturn(mockFoundationModelService);
    }

    @Test
    void should_list_models() throws Exception {

        var mockHttpClient = mock(HttpClient.class);
        var mockHttpResponse = mock(HttpResponse.class);
        var mockAuthenticatorProvider = mock(IBMCloudAuthenticator.class);
        var mockHttpRequest = ArgumentCaptor.forClass(HttpRequest.class);

        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body())
                .thenReturn(
                        "                    {\n"
+ "                        \"total_count\": 28,\n"
+ "                        \"limit\": 100,\n"
+ "                        \"first\": {\n"
+ "                            \"href\": \"https://us-south.ml.cloud.ibm.com/ml/v1/foundation_model_specs?version=2025-12-05\"\n"
+ "                        },\n"
+ "                        \"resources\": [\n"
+ "                            {\n"
+ "                                \"model_id\": \"cross-encoder/ms-marco-minilm-l-12-v2\",\n"
+ "                                \"label\": \"ms-marco-minilm-l-12-v2\",\n"
+ "                                \"provider\": \"cross-encoder\",\n"
+ "                                \"source\": \"cross-encoder\",\n"
+ "                                \"indemnity\": \"NON_IBM\",\n"
+ "                                \"functions\": [\n"
+ "                                    {\n"
+ "                                        \"id\": \"rerank\"\n"
+ "                                    }\n"
+ "                                ],\n"
+ "                                \"short_description\": \"Used for Information Retrieval: Encode and sort a query will all possible passages.\",\n"
+ "                                \"long_description\": \"The model can be used for Information Retrieval: Given a query, encode the query will all possible passages (e.g. retrieved with ElasticSearch). Then sort the passages in a decreasing order.\",\n"
+ "                                \"input_tier\": \"class_11\",\n"
+ "                                \"output_tier\": \"class_11\",\n"
+ "                                \"number_params\": \"33.4m\",\n"
+ "                                \"model_limits\": {\n"
+ "                                    \"max_sequence_length\": 512,\n"
+ "                                    \"max_output_tokens\": 1024\n"
+ "                                },\n"
+ "                                \"limits\": {\n"
+ "                                    \"19e27818-79cf-4137-ae58-da279f9e9d16\": {\n"
+ "                                        \"call_time\": \"10m0s\",\n"
+ "                                        \"max_output_tokens\": 1024\n"
+ "                                    },\n"
+ "                                    \"3f6acf43-ede8-413a-ac69-f8af3bb0cbfe\": {\n"
+ "                                        \"call_time\": \"5m0s\",\n"
+ "                                        \"max_output_tokens\": 1024\n"
+ "                                    },\n"
+ "                                    \"a3d2f92f-06f9-48d0-b2e6-a7ba2b4e0577\": {\n"
+ "                                        \"call_time\": \"10m0s\",\n"
+ "                                        \"max_output_tokens\": 1024\n"
+ "                                    },\n"
+ "                                    \"d18d88b9-be7a-46ec-be1e-aff14904f1e9\": {\n"
+ "                                        \"call_time\": \"10m0s\",\n"
+ "                                        \"max_output_tokens\": 1024\n"
+ "                                    }\n"
+ "                                },\n"
+ "                                \"lifecycle\": [\n"
+ "                                    {\n"
+ "                                        \"id\": \"available\",\n"
+ "                                        \"start_date\": \"2024-09-17\"\n"
+ "                                    }\n"
+ "                                ]\n"
+ "                            }\n"
+ "                        ]\n"
+ "                    }");

        when(mockHttpClient.send(mockHttpRequest.capture(), any(BodyHandler.class)))
                .thenReturn(mockHttpResponse);

        try (MockedStatic<HttpClientProvider> httpClientProvider = mockStatic(HttpClientProvider.class)) {
            httpClientProvider.when(() -> HttpClientProvider.httpClient(true)).thenReturn(mockHttpClient);

            var modelCatalog = WatsonxModelCatalog.builder()
                    .baseUrl(CloudRegion.FRANKFURT)
                    .authenticator(mockAuthenticatorProvider)
                    .build();

            var models = modelCatalog.listModels();
            assertEquals(1, models.size());
            assertEquals("2024-09-17T00:00:00Z", models.get(0).createdAt().toString());
            assertEquals(
                    "The model can be used for Information Retrieval: Given a query, encode the query will all possible passages (e.g. retrieved with ElasticSearch). Then sort the passages in a decreasing order.",
                    models.get(0).description());
            assertEquals("ms-marco-minilm-l-12-v2", models.get(0).displayName());
            assertEquals(512, models.get(0).maxInputTokens());
            assertEquals(1024, models.get(0).maxOutputTokens());
            assertEquals("cross-encoder/ms-marco-minilm-l-12-v2", models.get(0).name());
            assertEquals(WATSONX, models.get(0).provider());
            assertEquals(ModelType.SCORING, models.get(0).type());
        }
    }
}
