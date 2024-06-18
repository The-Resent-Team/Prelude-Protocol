package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.*;
import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.ModStatusPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Testable
public class TestModStatusPacket {
    @Test
    public void testModStatusPacket() throws IOException {
        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        ModStatusPacket packet = ModStatusPacket.builder()
                .modIdentifier("offhand")
                .modStatus(ModStatusPacket.ModStatus.SUPPORTED)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (optional.isEmpty())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ModStatusPacket)
                Assertions.assertEquals(ModStatusPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ModStatusPacket deserialized = (ModStatusPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            ClientHandshakePacket invalidPacket = new ClientHandshakePacket();
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
