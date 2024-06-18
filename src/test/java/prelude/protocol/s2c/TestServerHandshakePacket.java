package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.packets.s2c.ServerHandshakePacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestServerHandshakePacket {
    @Test
    public void testServerHandshakePacket() throws IOException {
        ServerHandshakePacket packet = ServerHandshakePacket.builder()
                .preludeMajorVersion(1)
                .preludeMinorVersion(0)
                .preludePatchVersion(0)
                .serverMajorVersion(1)
                .serverMinorVersion(8)
                .serverPatchVersion(8)
                .build();

        byte[] bytes = packet.toBytes();
        ServerHandshakePacket deserializedPacket = ServerHandshakePacket.from(new ByteArrayInputStream(bytes));
        Assertions.assertEquals(packet, deserializedPacket);
    }
}
