package dev.langchain4j.service.output;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PojoOutputParserTest {

    enum MyEnumWithToString {
        A,
        B,
        C;

        @Override
        public String toString() {
            return "[" + name() + "]";
        }
    }

    @Test
    void should_create_schema_for_enum_with_custom_toString() {

        // given
        assertThat(MyEnumWithToString.A.toString()).isEqualTo("[A]");

        class PojoWithEnum {
            private MyEnumWithToString myEnumWithToString;
        }

        PojoOutputParser<PojoWithEnum> parser = new PojoOutputParser<>(PojoWithEnum.class);

        // when
        String formatInstructions = parser.formatInstructions();

        // then
        assertThat(formatInstructions).contains("enum, must be one of [A, B, C]");
    }

    enum Status {
        OPEN,
        CLOSED
    }

    @Test
    void should_create_schema_for_list_of_enums() {

        // given

        class PojoWithEnumList {
            private List<Status> statuses;
        }

        PojoOutputParser<PojoWithEnumList> parser = new PojoOutputParser<>(PojoWithEnumList.class);

        // when
        String formatInstructions = parser.formatInstructions();

        // then
        assertThat(formatInstructions).contains("array of enum, must be one of [OPEN, CLOSED]");
    }
}
