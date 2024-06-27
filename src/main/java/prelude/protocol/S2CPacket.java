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

package prelude.protocol;

import prelude.protocol.packets.s2c.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class S2CPacket extends Packet<S2CPacketHandler> {
    public static final Map<Byte, Class<? extends S2CPacket>> packets = new HashMap<>();
    public static S2CPacketHandler handler = null;

    public static final byte SERVER_HANDSHAKE_ID = 0;
    public static final byte MOD_STATUS_ID = 1;
    public static final byte RESPAWN_ANCHOR_UPDATE_ID = 2;
    public static final byte SERVER_TPS_ID = 3;
    public static final byte UPDATE_OFFHAND_ID = 4;
    public static final byte TOTEM_USED_ID = 5;
    public static final byte WAYPOINTS_ID = 6;

    static {
        packets.put(SERVER_HANDSHAKE_ID, ServerHandshakePacket.class);
        packets.put(MOD_STATUS_ID, ModStatusPacket.class);
        packets.put(RESPAWN_ANCHOR_UPDATE_ID, RespawnAnchorUpdatePacket.class);
        packets.put(SERVER_TPS_ID, ServerTpsPacket.class);
        packets.put(UPDATE_OFFHAND_ID, UpdateOffhandPacket.class);
        packets.put(TOTEM_USED_ID, TotemUsedPacket.class);
        packets.put(WAYPOINTS_ID, WaypointsPacket.class);
    }

    public static void setHandler(S2CPacketHandler newHandler) {
        if (handler != null)
            throw new IllegalStateException("Handler already set!");

        handler = newHandler;
    }

    public static void trySetHandler(S2CPacketHandler newHandler) {
        if (handler == null)
            handler = newHandler;
    }

    @Override
    public abstract void loadData(InputStream is) throws InvalidPacketException;

    public static Optional<S2CPacket> parsePacket(byte[] bytes) throws Exception {
        if (handler == null)
            throw new IllegalStateException("Handler not set!");

        if (bytes == null)
            return Optional.empty();

        if (bytes.length < 1)
            return Optional.empty();

        byte id = bytes[0];
        Class<? extends S2CPacket> clazz = packets.get(id);
        if (clazz == null)
            throw new InvalidPacketException("Invalid packet id for S2CPacket ids!");

        // it's deprecated however it's the only way it works with teavm iirc
        @SuppressWarnings("deprecation")
        S2CPacket packet = clazz.newInstance();

        packet.loadData(new ByteArrayInputStream(bytes));
        return Optional.of(packet);
    }
}
