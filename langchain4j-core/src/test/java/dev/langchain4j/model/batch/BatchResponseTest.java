package dev.langchain4j.model.batch;

import static dev.langchain4j.model.batch.BatchState.PENDING;
import static dev.langchain4j.model.batch.BatchState.SUCCEEDED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class BatchResponseTest {
    private static final String BATCH_NAME = "batches/test-batch";

    private static BatchResponse<String> responseWithState(BatchState state) {
        return BatchResponse.<String>builder().batchId(BATCH_NAME).state(state).build();
    }

    @Test
    void responsesAndErrors_shouldBeDerivedFromResults() {
        BatchError error = new BatchError(400, "Bad request", null);
        BatchResponse<String> response = BatchResponse.<String>builder()
                .batchId(BATCH_NAME)
                .state(SUCCEEDED)
                .results(Arrays.asList(
                        BatchItemResult.success("response1"),
                        BatchItemResult.success("response2"),
                        BatchItemResult.failure(error)))
                .build();

        assertThat(response.responses()).containsExactly("response1", "response2");
        assertThat(response.errors()).containsExactly(error);
        assertThat(response.errors().get(0).code()).isEqualTo(400);
        assertThat(response.errors().get(0).message()).isEqualTo("Bad request");
    }

    @Test
    void results_shouldPreserveOrderAndCorrelateOutcomesWithRequests() {
        BatchError error = new BatchError(429, "Rate limited", null);
        BatchResponse<String> response = BatchResponse.<String>builder()
                .batchId(BATCH_NAME)
                .state(SUCCEEDED)
                .results(Arrays.asList(
                        BatchItemResult.success("first"),
                        BatchItemResult.failure(error),
                        BatchItemResult.success("third")))
                .build();

        List<BatchItemResult<String>> results = response.results();
        assertThat(results).hasSize(3);

        assertThat(results.get(0).isSuccess()).isTrue();
        assertThat(results.get(0).response()).isEqualTo("first");
        assertThat(results.get(0).error()).isNull();

        assertThat(results.get(1).isSuccess()).isFalse();
        assertThat(results.get(1).response()).isNull();
        assertThat(results.get(1).error()).isEqualTo(error);

        assertThat(results.get(2).isSuccess()).isTrue();
        assertThat(results.get(2).response()).isEqualTo("third");
    }

    @Test
    void shouldDefaultNullResultsToEmptyList() {
        BatchResponse<String> response = BatchResponse.<String>builder()
                .batchId(BATCH_NAME)
                .state(SUCCEEDED)
                .results(null)
                .build();

        assertThat(response.results()).isEmpty();
        assertThat(response.responses()).isEmpty();
        assertThat(response.errors()).isEmpty();
    }

    @Test
    void builder_shouldBuildEquivalentResponse() {
        List<BatchItemResult<String>> results =
                Arrays.asList(BatchItemResult.success("response1"), BatchItemResult.success("response2"));

        BatchResponse<String> built = BatchResponse.<String>builder()
                .batchId(BATCH_NAME)
                .state(SUCCEEDED)
                .results(results)
                .build();

        BatchResponse<String> expected = BatchResponse.<String>builder()
                .batchId(BATCH_NAME)
                .state(SUCCEEDED)
                .results(results)
                .build();

        assertThat(built).isEqualTo(expected);
        assertThat(built.batchId()).isEqualTo(BATCH_NAME);
        assertThat(built.state()).isEqualTo(SUCCEEDED);
        assertThat(built.responses()).containsExactly("response1", "response2");
    }

    @Test
    void builder_shouldDefaultMissingResultsToEmpty() {
        BatchResponse<String> built = BatchResponse.<String>builder()
                .batchId(BATCH_NAME)
                .state(PENDING)
                .build();

        assertThat(built.results()).isEmpty();
        assertThat(built.responses()).isEmpty();
        assertThat(built.errors()).isEmpty();
        assertThat(built.state().isTerminal()).isFalse();
    }
}
