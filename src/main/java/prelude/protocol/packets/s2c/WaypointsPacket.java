package prelude.protocol.packets.s2c;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;
import prelude.protocol.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WaypointsPacket extends S2CPacket {
    private static final String SPLIT = "\u0000\u8301\u9281";

    private Waypoint[] waypoints;

    public WaypointsPacket() {}

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
                bao.write(SPLIT.getBytes(StandardCharsets.US_ASCII));
        }

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if (is.read() != WAYPOINTS_ID)
                throw new InvalidPacketException("Packet ID doesn't match with WAY (%id%)!"
                        .replace("%id%", RESPAWN_ANCHOR_UPDATE_ID + ""));

            StringBuilder waypointsString = new StringBuilder();

            int b;
            while ((b = is.read()) != -1) {
                waypointsString.append((char) b);
            }

            String[] waypointsData = waypointsString.toString().split(SPLIT);

            List<Waypoint> waypointList = new ArrayList<>();
            for (String waypointsDatum : waypointsData) {
                ByteArrayInputStream bis = new ByteArrayInputStream(waypointsDatum.getBytes(StandardCharsets.US_ASCII));
                String name = StreamUtils.readASCII(StreamUtils.readShort(bis), bis);
                int x = StreamUtils.readVarInt(bis);
                int y = StreamUtils.readVarInt(bis);
                int z = StreamUtils.readVarInt(bis);
                waypointList.add(new Waypoint(name, x, y, z));
            }

            this.waypoints = waypointList.toArray(new Waypoint[0]);
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse WAYPOINTS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleWaypoints(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaypointsPacket)) return false;
        WaypointsPacket that = (WaypointsPacket) o;
        return Arrays.equals(this.waypoints, that.waypoints);
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

        public Builder addWaypoints(Collection<Waypoint> waypoints) {
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

    public static class Waypoint {
        public final String name;
        public final int x;
        public final int y;
        public final int z;

        public Waypoint(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Waypoint)) return false;
            Waypoint waypoint = (Waypoint) o;
            return x == waypoint.x && y == waypoint.y && z == waypoint.z && Objects.equals(name, waypoint.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, x, y, z);
        }
    }

    public Waypoint[] getWaypoints() {
        return waypoints;
    }
}
