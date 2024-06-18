package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.packets.s2c.WaypointsPacket;

import java.io.IOException;

@Testable
public class TestWaypointsPacket {
    @Test
    public void testWaypointsPacket() throws IOException {
        WaypointsPacket packet = WaypointsPacket.builder()
                .addWaypoint(new WaypointsPacket.Waypoint("spawn", 0, 75, 0))
                .build();

        Assertions.assertEquals(packet.waypoints.length, 1);
        Assertions.assertEquals(packet.waypoints[0].name(), "spawn");

        byte[] bytes = packet.toBytes();
        WaypointsPacket deserializedPacket = WaypointsPacket.from()
    }
}
