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
public class TestEquipOffhandPacket {
    @Test
    public void testEquipOffhandPacket() throws IOException {
        C2SPacket.trySetHandler(new TestC2SPacketHandler());

        EquipOffhandPacket packet = EquipOffhandPacket.builder()
                .slot((short) 25)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<C2SPacket> optional = C2SPacket.parsePacket(bytes);

            if (optional.isEmpty())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof EquipOffhandPacket)
                Assertions.assertEquals(EquipOffhandPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            EquipOffhandPacket deserialized = (EquipOffhandPacket) optional.get();
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
