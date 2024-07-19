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

package prelude.protocol.packets.s2c.play;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;

import java.io.IOException;
import java.io.InputStream;

public class TotemUsedS2CPacket extends S2CPacket {
    public TotemUsedS2CPacket() {}

    @Override
    public byte[] toBytes() throws IOException {
        return new byte[] {(byte) packetId};
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("TOTEM_USED_ID", is);

        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse TOTEM_USED_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleTotemUsed(this);
    }

    @Override
    public boolean equals(Object packet) {
        return packet instanceof TotemUsedS2CPacket;
    }
}
