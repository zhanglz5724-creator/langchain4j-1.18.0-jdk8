package dev.langchain4j.model.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class BatchRequestTest {

    @Test
    void constructor_shouldThrowNullPointerException_whenRequestsIsNull() {
        assertThatThrownBy(() -> new BatchRequest<>(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void requests_shouldReturnCorrectList_whenBatchIsInitialized() {
        List<String> items = Arrays.asList("item1", "item2");
        BatchRequest<String> batch = new BatchRequest<>(items);

        assertThat(batch.requests()).hasSize(2).containsExactly("item1", "item2");
    }

    @Test
    void equals_shouldReturnTrue_whenRequestsAreIdentical() {
        List<Integer> list1 = Arrays.asList(1, 2);
        List<Integer> list2 = Arrays.asList(1, 2);

        BatchRequest<Integer> batch1 = new BatchRequest<>(list1);
        BatchRequest<Integer> batch2 = new BatchRequest<>(list2);

        assertThat(batch1).isEqualTo(batch2);
    }

    @Test
    void equals_shouldReturnFalse_whenRequestsAreDifferent() {
        BatchRequest<Integer> batch1 = new BatchRequest<>(Arrays.asList(1));
        BatchRequest<Integer> batch2 = new BatchRequest<>(Arrays.asList(2));

        assertThat(batch1).isNotEqualTo(batch2);
    }

    @Test
    void hashCode_shouldBeEqual_whenRequestsAreIdentical() {
        List<String> list = Arrays.asList("data");
        BatchRequest<String> batch1 = new BatchRequest<>(list);
        BatchRequest<String> batch2 = new BatchRequest<>(list);

        assertThat(batch1).hasSameHashCodeAs(batch2);
    }

    @Test
    void toString_shouldReturnFormattedString_whenCalled() {
        BatchRequest<String> batch = new BatchRequest<>(Arrays.asList("A"));

        assertThat(batch).hasToString("BatchRequest{requests=[A]}");
    }
}
