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
            StringBuilder expected = new StringBuilder();
            for (int i = 0; i < 20; i++)
                expected.append("1234567890abc");

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            StreamUtils.writeShort(expected.length(), bao);
            bao.write(expected.toString().getBytes(StandardCharsets.US_ASCII));

            ByteArrayInputStream is = new ByteArrayInputStream(bao.toByteArray());
            String message = StreamUtils.readASCII(StreamUtils.readShort(is), is);

            Assertions.assertEquals(expected.toString(), message);
        }

        {
            StringBuilder expected = new StringBuilder();
            for (int i = 0; i < 2; i++)
                expected.append("1234567890abc");
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bao.write(expected.length());
            bao.write(expected.toString().getBytes(StandardCharsets.US_ASCII));

            ByteArrayInputStream is = new ByteArrayInputStream(bao.toByteArray());
            String message = StreamUtils.readASCII(is.read(), is);
            Assertions.assertEquals(expected.toString(), message);
        }
    }
}
