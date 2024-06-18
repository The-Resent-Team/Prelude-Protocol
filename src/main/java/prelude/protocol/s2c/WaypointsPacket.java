package prelude.protocol.s2c;

import prelude.protocol.S2CPacket;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaypointsPacket extends S2CPacket {
    private static final String NULL_TERMINATOR = "\u0000";

    public final Waypoint[] waypoints;

    private WaypointsPacket(Waypoint[] waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(WAYPOINTS_ID);

        for (int i = 0; i < waypoints.length; i++) {
            Waypoint waypoint = waypoints[i];
            StreamUtils.writeShort(waypoint.name.length(), bao);
            bao.write(waypoint.name.getBytes(StandardCharsets.US_ASCII));
            StreamUtils.writeVarInt(waypoint.x, bao);
            StreamUtils.writeVarInt(waypoint.y, bao);
            StreamUtils.writeVarInt(waypoint.z, bao);

            if (i != waypoints.length - 1)
                bao.write(NULL_TERMINATOR.getBytes(StandardCharsets.US_ASCII));
        }

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaypointsPacket that)) return false;
        return Objects.deepEquals(waypoints, that.waypoints);
    }

    public static WaypointsPacket from(InputStream is) {
        try {
            if (is.read() != WAYPOINTS_ID)
                return null;

            byte[] waypointsData = is.readAllBytes();

        } catch (Exception e) {
            System.err.println("Failed to parse server tps packet!");
            e.printStackTrace();
            return null;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Waypoint> waypoints = new ArrayList<>();
        private Builder() {}

        public Builder addWaypoint(Waypoint waypoint) {
            if (waypoints == null) {
                waypoints = new ArrayList<>();
            }
            waypoints.add(waypoint);
            return this;
        }

        public Builder addWaypoints(List<Waypoint> waypoints) {
            if (this.waypoints == null) {
                this.waypoints = new ArrayList<>();
            }
            this.waypoints.addAll(waypoints);
            return this;
        }

        public WaypointsPacket build() {
            return new WaypointsPacket(waypoints.toArray(new Waypoint[0]));
        }
    }

    public record Waypoint(String name, int x, int y, int z) {
        public String serialize() {

        }
    }
}
