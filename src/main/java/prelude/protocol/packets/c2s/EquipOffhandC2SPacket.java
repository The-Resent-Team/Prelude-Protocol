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

package prelude.protocol.packets.c2s;

import prelude.protocol.C2SPacket;
import prelude.protocol.C2SPacketHandler;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EquipOffhandC2SPacket extends C2SPacket {
    private short slot;

    public EquipOffhandC2SPacket() {}

    private EquipOffhandC2SPacket(short slot) {
        this.slot = slot;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);
        StreamUtils.writeShort(slot, bao);

        return bao.toByteArray();
    }

    /*
    * Note: This is the only packet that could be abused
    * to try to cause damage to the server running prelude
    * we must sanitize all input to this to prevent damage
    * */
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("EQUIP_OFFHAND_ID", is);

            short slot = (short) StreamUtils.readShort(is);

            // There is no reliable way to determine the max
            // slot id from here without hard coding it
            // we must let the bukkit implementation check this
            if (slot < 0)
                throw new InvalidPacketException("Constructed EQUIP_OFFHAND_PACKET has a negative slot!");

            this.slot = slot;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse EQUIP_OFFHAND_PACKET!", e);
        }
    }

    @Override
    public void processSelf(C2SPacketHandler handler) {
        handler.handleEquipOffhand(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipOffhandC2SPacket)) return false;
        EquipOffhandC2SPacket that = (EquipOffhandC2SPacket) o;
        return slot == that.slot;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private short slot = -1;

        private Builder() {}

        public Builder slot(short slot) {
            this.slot = slot;
            return this;
        }

        public EquipOffhandC2SPacket build() {
            if (slot == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new EquipOffhandC2SPacket(slot);
        }
    }

    public short getSlot() {
        return slot;
    }
}
