package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestServerTpsPacket {
    @Test
    public void testServerTpsPacket() throws IOException {
        ServerTpsPacket notPossiblePacket = ServerTpsPacket.builder().characteristic(124).mantissa(9999999).build();
        ServerTpsPacket packet = ServerTpsPacket.builder().characteristic(19).mantissa(391823).build();
        Assertions.assertNotEquals(notPossiblePacket, packet);

        byte[] bytes = notPossiblePacket.toBytes();
        ServerTpsPacket deserializedPacket = ServerTpsPacket.from(new ByteArrayInputStream(bytes));

        Assertions.assertEquals(notPossiblePacket, deserializedPacket);
    }
}
