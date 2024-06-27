/*
 * Prelude-Protocol is an implementation to abstract communications between the Client and Prelude-API.
 * Copyright (C) 2024 cire3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
