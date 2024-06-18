package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.TestS2CPacketHandler;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.ServerHandshakePacket;
import prelude.protocol.packets.s2c.ServerTpsPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestServerTpsPacket {
    @Test
    public void testServerTpsPacket() throws IOException {
        ServerTpsPacket notPossiblePacket = ServerTpsPacket.builder().characteristic(124).mantissa(Short.MAX_VALUE).build();
        ServerTpsPacket packet = ServerTpsPacket.builder().characteristic(19).mantissa(19372).build();
        Assertions.assertNotEquals(notPossiblePacket, packet);

        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ServerTpsPacket)
                Assertions.assertEquals(ServerTpsPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ServerTpsPacket deserialized = (ServerTpsPacket) optional.get();
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
