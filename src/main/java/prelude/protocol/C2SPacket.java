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

import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class C2SPacket extends Packet<C2SPacketHandler> {
    public static final Map<Byte, Class<? extends C2SPacket>> packets = new HashMap<>();
    public static C2SPacketHandler handler = null;

    public static final byte CLIENT_HANDSHAKE_ID = 0;
    public static final byte EQUIP_OFFHAND_ID = 1;

    static {
        packets.put(CLIENT_HANDSHAKE_ID, ClientHandshakePacket.class);
        packets.put(EQUIP_OFFHAND_ID, EquipOffhandPacket.class);
    }

    public static void setHandler(C2SPacketHandler newHandler) {
        if (handler != null)
            throw new IllegalStateException("Handler already set!");

        handler = newHandler;
    }

    public static void trySetHandler(C2SPacketHandler newHandler) {
        if (handler == null)
            handler = newHandler;
    }

    @Override
    public abstract void loadData(InputStream is) throws InvalidPacketException;

    public static Optional<C2SPacket> parsePacket(byte[] bytes) throws Exception {
        if (handler == null)
            throw new IllegalStateException("Handler not set!");

        if (bytes == null)
            return Optional.empty();

        if (bytes.length < 1)
            return Optional.empty();

        byte id = bytes[0];
        Class<? extends C2SPacket> clazz = packets.get(id);
        if (clazz == null)
            throw new InvalidPacketException("Invalid packet id for S2CPacket ids!");

        // it's deprecated however it's the only way it works with teavm iirc
        @SuppressWarnings("deprecation")
        C2SPacket packet = clazz.newInstance();

        packet.loadData(new ByteArrayInputStream(bytes));
        return Optional.of(packet);
    }
}
