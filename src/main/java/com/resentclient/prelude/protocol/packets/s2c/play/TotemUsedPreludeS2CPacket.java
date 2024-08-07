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

package com.resentclient.prelude.protocol.packets.s2c.play;

import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.PreludeS2CPacketHandler;

import java.io.IOException;
import java.io.InputStream;

public class TotemUsedPreludeS2CPacket extends PreludeS2CPacket {
    public TotemUsedPreludeS2CPacket() {}

    @Override
    public byte[] toBytes() throws IOException {
        return new byte[] {(byte) packetId};
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("TOTEM_USED_ID", is);

        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse TOTEM_USED_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleTotemUsed(this);
    }

    @Override
    public boolean equals(Object packet) {
        return packet instanceof TotemUsedPreludeS2CPacket;
    }
}
