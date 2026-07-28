package dev.langchain4j.http.client.apache;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.langchain4j.http.client.FormDataFile;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.junit.jupiter.api.Test;

class MultipartBodyPublisherTest {

    private static final String BOUNDARY = "------LangChain4j";

    @Test
    void should_build_body_with_single_form_field() throws UnsupportedEncodingException {
        MultipartBodyPublisher publisher = new MultipartBodyPublisher();

        publisher.addField("field1", "value1");
        publisher.build();

        String body = bodyAsString(publisher.parts());

        String expected = BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"field1\"\n"
                + "\n"
                + "value1\n"
                + BOUNDARY + "--\n";

        assertEquals(normalize(expected), body);
    }

    @Test
    void should_build_body_with_file() throws UnsupportedEncodingException {
        MultipartBodyPublisher publisher = new MultipartBodyPublisher();

        FormDataFile file = new FormDataFile("test.txt", "text/plain", "hello".getBytes(UTF_8));

        publisher.addFile("file", file);
        publisher.build();

        String body = bodyAsString(publisher.parts());

        String expected = BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\n"
                + "Content-Type: text/plain\n"
                + "\n"
                + "hello\n"
                + BOUNDARY + "--\n";

        assertEquals(normalize(expected), body);
    }

    @Test
    void should_build_body_with_field_then_file() throws UnsupportedEncodingException {
        MultipartBodyPublisher publisher = new MultipartBodyPublisher();

        FormDataFile file = new FormDataFile("test.txt", "text/plain", "hello".getBytes(UTF_8));

        publisher.addField("field1", "value1");
        publisher.addFile("file", file);
        publisher.build();

        String body = bodyAsString(publisher.parts());

        String expected = BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"field1\"\n"
                + "\n"
                + "value1\n"
                + BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\n"
                + "Content-Type: text/plain\n"
                + "\n"
                + "hello\n"
                + BOUNDARY + "--\n";

        assertEquals(normalize(expected), body);
    }

    @Test
    void should_omit_content_type_header_when_content_type_is_null() throws UnsupportedEncodingException {
        MultipartBodyPublisher publisher = new MultipartBodyPublisher();

        FormDataFile file = new FormDataFile("audio.wav", null, "hello".getBytes(UTF_8));

        publisher.addFile("file", file);
        publisher.build();

        String body = bodyAsString(publisher.parts());

        assertThat(body).doesNotContain("Content-Type:");
        assertThat(body).doesNotContain("Content-Type: null");

        String expected = BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"audio.wav\"\n"
                + "\n"
                + "hello\n"
                + BOUNDARY + "--\n";

        assertEquals(normalize(expected), body);
    }

    @Test
    void should_omit_content_type_header_when_content_type_is_blank() throws UnsupportedEncodingException {
        MultipartBodyPublisher publisher = new MultipartBodyPublisher();

        FormDataFile file = new FormDataFile("audio.wav", "", "hello".getBytes(UTF_8));

        publisher.addFile("file", file);
        publisher.build();

        String body = bodyAsString(publisher.parts());

        assertThat(body).doesNotContain("Content-Type:");

        String expected = BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"audio.wav\"\n"
                + "\n"
                + "hello\n"
                + BOUNDARY + "--\n";

        assertEquals(normalize(expected), body);
    }

    @Test
    void should_keep_content_type_header_when_content_type_is_present() throws UnsupportedEncodingException {
        MultipartBodyPublisher publisher = new MultipartBodyPublisher();

        FormDataFile file = new FormDataFile("audio.wav", "audio/wav", "hello".getBytes(UTF_8));

        publisher.addFile("file", file);
        publisher.build();

        String body = bodyAsString(publisher.parts());

        assertThat(body).contains("Content-Type: audio/wav");

        String expected = BOUNDARY + "\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"audio.wav\"\n"
                + "Content-Type: audio/wav\n"
                + "\n"
                + "hello\n"
                + BOUNDARY + "--\n";

        assertEquals(normalize(expected), body);
    }

    private static String bodyAsString(List<byte[]> parts) throws UnsupportedEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] part : parts) {
            out.write(part, 0, part.length);
        }
        return out.toString("UTF-8");
    }

    private static String normalize(String s) {
        return s.replace("\n", "\r\n");
    }
}
