package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.TestS2CPacketHandler;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.ModStatusPacket;
import prelude.protocol.packets.s2c.RespawnAnchorUpdatePacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestRespawnAnchorUpdatePacket {
    @Test
    public void testRespawnAnchorUpdatePacket() throws IOException {
        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        RespawnAnchorUpdatePacket packet = RespawnAnchorUpdatePacket.builder()
                .charge(4)
                .x(-203)
                .y(23)
                .z(2100291722)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (optional.isEmpty())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof RespawnAnchorUpdatePacket)
                Assertions.assertEquals(RespawnAnchorUpdatePacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            RespawnAnchorUpdatePacket deserialized = (RespawnAnchorUpdatePacket) optional.get();
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
