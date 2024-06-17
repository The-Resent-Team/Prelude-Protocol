package prelude.protocol;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Testable
public class TestStreamUtils {
    @Test
    public void testASCII() throws IOException {
        {
            String expected = "1234567890abc".repeat(20);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            StreamUtils.writeShort(expected.length(), bao);
            bao.write(expected.getBytes(StandardCharsets.US_ASCII));

            ByteArrayInputStream is = new ByteArrayInputStream(bao.toByteArray());
            String message = StreamUtils.readASCII(StreamUtils.readShort(is), is);

            Assertions.assertEquals(expected, message);
        }

        {
            String expected = "1234567890abc".repeat(2);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bao.write(expected.length());
            bao.write(expected.getBytes(StandardCharsets.US_ASCII));

            ByteArrayInputStream is = new ByteArrayInputStream(bao.toByteArray());
            String message = StreamUtils.readASCII(is.read(), is);
            Assertions.assertEquals(expected, message);
        }
    }
}
