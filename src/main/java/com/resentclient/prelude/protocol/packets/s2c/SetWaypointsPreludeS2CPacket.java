/*
 * Prelude-Protocol is an implementation to abstract communications between the Client and Prelude-API.
 * Copyright (C) 2024 cire3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.resentclient.prelude.protocol.packets.s2c;

import com.resentclient.prelude.protocol.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SetWaypointsPreludeS2CPacket extends PreludeS2CPacket {
    private static final String SPLIT = "\u0000\u8301\u9281";

    private Waypoint[] waypoints;

    public SetWaypointsPreludeS2CPacket() {}

    private SetWaypointsPreludeS2CPacket(Waypoint[] waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        for (int i = 0; i < waypoints.length; i++) {
            Waypoint waypoint = waypoints[i];

            waypoint.write(bao);

            if (i != waypoints.length - 1)
                bao.write(SPLIT.getBytes(StandardCharsets.US_ASCII));
        }

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("SET_WAYPOINTS_ID", is);

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
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse WAYPOINTS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleWaypoints(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SetWaypointsPreludeS2CPacket)) return false;
        SetWaypointsPreludeS2CPacket that = (SetWaypointsPreludeS2CPacket) o;
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

        public SetWaypointsPreludeS2CPacket build() {
            return new SetWaypointsPreludeS2CPacket(waypoints.toArray(new Waypoint[0]));
        }
    }

    public static class Waypoint implements WriteableObject {
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

        @Override
        public void write(OutputStream out) throws IOException {
            StreamUtils.writeShort(name.length(), out);
            out.write(name.getBytes(StandardCharsets.US_ASCII));

            StreamUtils.writeVarInt(x, out);
            StreamUtils.writeVarInt(y, out);
            StreamUtils.writeVarInt(z, out);
        }
    }

    public Waypoint[] getWaypoints() {
        return waypoints;
    }
}
