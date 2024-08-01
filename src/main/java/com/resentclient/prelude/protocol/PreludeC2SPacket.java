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

import com.resentclient.prelude.protocol.packets.c2s.*;
import com.resentclient.prelude.protocol.packets.c2s.interactions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class PreludeC2SPacket extends PreludePacket<PreludeC2SPacketHandler> {
    private static final Map<Integer, Class<? extends PreludeC2SPacket>> C2S_ID_TO_PACKET = new HashMap<>();
    private static final Map<Class<? extends PreludeC2SPacket>, Integer> C2S_PACKET_TO_ID = new HashMap<>();

    static {
        registerC2SPacket(ClientHandshakeC2SPacket.class);
        registerC2SPacket(ClientAcknowledgeServerHandshakeC2SPacket.class);
        registerC2SPacket(EquipOffhandC2SPacket.class);
        registerC2SPacket(ClientSyncResponseC2SPacket.class);
        registerC2SPacket(InteractWithOffhandC2SPacket.class);
        registerC2SPacket(AttemptPlaceInLegacyIllegalSpotsC2SPacket.class);
    }

    protected PreludeC2SPacket() {
        this.packetId = C2S_PACKET_TO_ID.get(this.getClass());
    }

    @Override
    public abstract void loadData(InputStream is) throws InvalidPacketException;

    public static Optional<PreludeC2SPacket> parsePacket(byte[] bytes) throws Exception {
        if (bytes == null)
            return Optional.empty();

        if (bytes.length < 1)
            return Optional.empty();

        int id = bytes[0] & 0xFF;
        Class<? extends PreludeC2SPacket> clazz = C2S_ID_TO_PACKET.get(id);
        if (clazz == null)
            throw new InvalidPacketException("Invalid packet id for C2SPacket ids!");

        // it's deprecated however it's the only way it works with teavm iirc
        @SuppressWarnings("deprecation")
        PreludeC2SPacket packet = clazz.newInstance();

        packet.loadData(new ByteArrayInputStream(bytes));
        return Optional.of(packet);
    }

    static void registerC2SPacket(Class<? extends PreludeC2SPacket> clazz) {
        C2S_ID_TO_PACKET.put(C2S_ID_TO_PACKET.size(), clazz);
        C2S_PACKET_TO_ID.put(clazz, C2S_PACKET_TO_ID.size());
    }
}
