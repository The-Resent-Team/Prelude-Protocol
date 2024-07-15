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

import prelude.protocol.packets.c2s.ClientHandshakeC2SPacket;
import prelude.protocol.packets.c2s.ClientSyncResponseC2SPacket;
import prelude.protocol.packets.c2s.EquipOffhandC2SPacket;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class C2SPacket extends Packet<C2SPacketHandler> {
    public static C2SPacketHandler handler = null;

    private static final Map<Integer, Class<? extends C2SPacket>> C2S_ID_TO_PACKET = new HashMap<>();
    private static final Map<Class<? extends C2SPacket>, Integer> C2S_PACKET_TO_ID = new HashMap<>();

    static {
        int curId = 0;
        C2S_ID_TO_PACKET.put(curId++, ClientHandshakeC2SPacket.class);
        C2S_PACKET_TO_ID.put(ClientHandshakeC2SPacket.class, curId);

        C2S_ID_TO_PACKET.put(curId++, EquipOffhandC2SPacket.class);
        C2S_PACKET_TO_ID.put(EquipOffhandC2SPacket.class, curId);

        C2S_ID_TO_PACKET.put(curId++, ClientSyncResponseC2SPacket.class);
        C2S_PACKET_TO_ID.put(ClientSyncResponseC2SPacket.class, curId);
    }

    protected C2SPacket() {
        this.packetId = C2S_PACKET_TO_ID.get(this.getClass());
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
        Class<? extends C2SPacket> clazz = C2S_ID_TO_PACKET.get(id & 0xFF);
        if (clazz == null)
            throw new InvalidPacketException("Invalid packet id for C2SPacket ids!");

        // it's deprecated however it's the only way it works with teavm iirc
        @SuppressWarnings("deprecation")
        C2SPacket packet = clazz.newInstance();

        packet.loadData(new ByteArrayInputStream(bytes));
        return Optional.of(packet);
    }
}
