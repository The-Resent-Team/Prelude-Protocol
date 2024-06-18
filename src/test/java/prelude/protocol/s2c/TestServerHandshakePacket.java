package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.TestS2CPacketHandler;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.RespawnAnchorUpdatePacket;
import prelude.protocol.packets.s2c.ServerHandshakePacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestServerHandshakePacket {
    @Test
    public void testServerHandshakePacket() throws IOException {
        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        ServerHandshakePacket packet = ServerHandshakePacket.builder()
                .preludeMajorVersion(1)
                .preludeMinorVersion(0)
                .preludePatchVersion(0)
                .serverMajorVersion(1)
                .serverMinorVersion(8)
                .serverPatchVersion(8)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ServerHandshakePacket)
                Assertions.assertEquals(ServerHandshakePacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ServerHandshakePacket deserialized = (ServerHandshakePacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            EquipOffhandPacket invalidPacket = new EquipOffhandPacket();
            try {
                invalidPacket.loadData(new ByteArrayInputStream(bytes));
                Assertions.fail("Somehow parsed invalid packet!");
            } catch (Exception e) {
                Assertions.assertInstanceOf(InvalidPacketException.class, e);
            }
        } catch (Exception e) {
            // erm what the
            Assertions.fail(e);
        }
    }
}
