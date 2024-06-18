package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.TestS2CPacketHandler;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.UpdateOffhandPacket;
import prelude.protocol.packets.s2c.WaypointsPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Testable
public class TestWaypointsPacket {
    @Test
    public void testWaypointsPacket() throws IOException {
        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        WaypointsPacket packet = WaypointsPacket.builder()
                .addWaypoint(new WaypointsPacket.Waypoint("spawn", 0, 75, 0))
                .build();

        Assertions.assertEquals(packet.getWaypoints().length, 1);
        Assertions.assertEquals(packet.getWaypoints()[0].name(), "spawn");

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (optional.isEmpty())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof WaypointsPacket)
                Assertions.assertEquals(WaypointsPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            WaypointsPacket deserialized = (WaypointsPacket) optional.get();
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
