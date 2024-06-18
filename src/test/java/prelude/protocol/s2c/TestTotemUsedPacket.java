package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.TestS2CPacketHandler;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.ServerTpsPacket;
import prelude.protocol.packets.s2c.TotemUsedPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static prelude.protocol.S2CPacket.TOTEM_USED_ID;

@Testable
public class TestTotemUsedPacket {
    @Test
    public void testTotemUsedPacket() throws IOException {
        Assertions.assertEquals(TOTEM_USED_ID, new TotemUsedPacket().toBytes()[0]);

        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        byte[] bytes = { TOTEM_USED_ID };
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof TotemUsedPacket)
                Assertions.assertEquals(TotemUsedPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            TotemUsedPacket deserialized = (TotemUsedPacket) optional.get();
            Assertions.assertArrayEquals(bytes, deserialized.toBytes());

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
