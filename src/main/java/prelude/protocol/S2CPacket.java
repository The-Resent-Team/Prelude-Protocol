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
    public static S2CPacketHandler handler = null;

    private static final Map<Integer, Class<? extends S2CPacket>> S2C_ID_TO_PACKET = new HashMap<>();
    private static final Map<Class<? extends S2CPacket>, Integer> S2C_PACKET_TO_ID = new HashMap<>();

    static {
        registerS2CPacket(ServerHandshakeS2CPacket.class);
        registerS2CPacket(ModStatusS2CPacket.class);
        registerS2CPacket(ServerTpsS2CPacket.class);
        registerS2CPacket(UpdateOffhandS2CPacket.class);
        registerS2CPacket(TotemUsedS2CPacket.class);
        registerS2CPacket(SetWaypointsS2CPacket.class);
        registerS2CPacket(ServerSyncRequestS2CPacket.class);
    }

    protected S2CPacket() {
        this.packetId = S2C_PACKET_TO_ID.get(this.getClass());
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
        Class<? extends S2CPacket> clazz = S2C_ID_TO_PACKET.get(id & 0xFF);
        if (clazz == null)
            throw new InvalidPacketException("Invalid packet id for S2CPacket ids!");

        // it's deprecated however it's the only way it works with teavm iirc
        @SuppressWarnings("deprecation")
        S2CPacket packet = clazz.newInstance();

        packet.loadData(new ByteArrayInputStream(bytes));
        return Optional.of(packet);
    }

    static void registerS2CPacket(Class<? extends S2CPacket> clazz) {
        S2C_ID_TO_PACKET.put(S2C_ID_TO_PACKET.size(), clazz);
        S2C_PACKET_TO_ID.put(clazz, S2C_PACKET_TO_ID.size());
    }
}
