package prelude.protocol.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.C2SPacket;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.TestC2SPacketHandler;
import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestClientHandshakePacket {
    @Test
    public void testClientHandshakePacket() throws IOException {
        C2SPacket.trySetHandler(new TestC2SPacketHandler());

        ClientHandshakePacket packet = ClientHandshakePacket.builder()
                .username("cire3")
                .resentMajorVersion(5)
                .resentMinorVersion(0)
                .resentBuildInteger(500)
                .clientType(ClientHandshakePacket.ClientType.DEV)
                .clientClaimsSelfIsRankedPlayer(true)
                .enabledMods(new String[]{"preva1l_is_cool", "freelook", "tps"})
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<C2SPacket> optional = C2SPacket.parsePacket(bytes);

            if (optional.isEmpty())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ClientHandshakePacket)
                Assertions.assertEquals(ClientHandshakePacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ClientHandshakePacket deserialized = (ClientHandshakePacket) optional.get();
            Assertions.assertEquals(packet, deserialized);
            Assertions.assertArrayEquals(packet .getEnabledMods(), deserialized.getEnabledMods());

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
