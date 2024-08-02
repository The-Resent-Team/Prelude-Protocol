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

package com.resentclient.prelude.protocol;

import com.resentclient.prelude.protocol.packets.s2c.*;
import com.resentclient.prelude.protocol.packets.s2c.play.*;
import com.resentclient.prelude.protocol.packets.s2c.world.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class PreludeS2CPacket extends PreludePacket<PreludeS2CPacketHandler> {
    private static final Map<Integer, Class<? extends PreludeS2CPacket>> S2C_ID_TO_PACKET = new HashMap<>();
    private static final Map<Class<? extends PreludeS2CPacket>, Integer> S2C_PACKET_TO_ID = new HashMap<>();

    static {
        registerS2CPacket(ServerHandshakePreludeS2CPacket.class);
        registerS2CPacket(ModStatusPreludeS2CPacket.class);
        registerS2CPacket(ServerTpsPreludeS2CPacket.class);
        registerS2CPacket(UpdateOffhandPreludeS2CPacket.class);
        registerS2CPacket(TotemUsedPreludeS2CPacket.class);
        registerS2CPacket(SetWaypointsPreludeS2CPacket.class);
        registerS2CPacket(ServerSyncRequestPreludeS2CPacket.class);
        registerS2CPacket(ChunkDataModernPreludeS2CPacket.class);
        registerS2CPacket(MultiBlockChangeModernPreludeS2CPacket.class);
        registerS2CPacket(BlockChangeModernPreludeS2CPacket.class);
        registerS2CPacket(SetItemTypePreludeS2CPacket.class);
    }

    protected PreludeS2CPacket() {
        this.packetId = S2C_PACKET_TO_ID.get(this.getClass());
    }

    @Override
    public abstract void loadData(InputStream is) throws InvalidPreludePacketException;

    public static Optional<PreludeS2CPacket> parsePacket(byte[] bytes) throws Exception {
        if (bytes == null)
            return Optional.empty();

        if (bytes.length < 1)
            return Optional.empty();

        byte id = bytes[0];
        Class<? extends PreludeS2CPacket> clazz = S2C_ID_TO_PACKET.get(id & 0xFF);
        if (clazz == null)
            throw new InvalidPreludePacketException("Invalid packet id for S2CPacket ids!");

        // it's deprecated however it's the only way it works with teavm iirc
        @SuppressWarnings("deprecation")
        PreludeS2CPacket packet = clazz.newInstance();

        packet.loadData(new ByteArrayInputStream(bytes));
        return Optional.of(packet);
    }

    static void registerS2CPacket(Class<? extends PreludeS2CPacket> clazz) {
        S2C_ID_TO_PACKET.put(S2C_ID_TO_PACKET.size(), clazz);
        S2C_PACKET_TO_ID.put(clazz, S2C_PACKET_TO_ID.size());
    }
}
