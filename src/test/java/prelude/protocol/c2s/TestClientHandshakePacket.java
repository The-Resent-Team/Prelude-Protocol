package prelude.protocol.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.packets.c2s.ClientHandshakePacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestClientHandshakePacket {
    @Test
    public void testClientHandshakePacket() throws IOException {
        ClientHandshakePacket packet = ClientHandshakePacket.builder()
                .username("cire3")
                .resentMajorVersion(5)
                .resentMinorVersion(0)
                .clientType(ClientHandshakePacket.ClientType.DEV)
                .clientClaimsSelfIsRankedPlayer(true)
                .enabledMods(new String[]{"fullbright", "tps"})
                .build();

        byte[] bytes = packet.toBytes();
        ClientHandshakePacket deserializedPacket =
                ClientHandshakePacket.from(new ByteArrayInputStream(bytes));

        Assertions.assertEquals(packet, deserializedPacket);
    }
}
