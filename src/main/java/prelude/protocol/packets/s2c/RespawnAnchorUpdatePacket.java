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

package prelude.protocol.packets.s2c;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RespawnAnchorUpdatePacket extends S2CPacket {
    private byte charge;
    private int x;
    private int y;
    private int z;

    public RespawnAnchorUpdatePacket() {}

    private RespawnAnchorUpdatePacket(byte charge, int x, int y, int z) {
        this.charge = charge;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(RESPAWN_ANCHOR_UPDATE_ID);

        bao.write(charge);
        StreamUtils.writeVarInt(x, bao);
        StreamUtils.writeVarInt(y, bao);
        StreamUtils.writeVarInt(z, bao);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream in) throws InvalidPacketException {
        try {
            if ((byte) in.read() != RESPAWN_ANCHOR_UPDATE_ID)
                throw new InvalidPacketException("Packet ID doesn't match with RESPAWN_ANCHOR_UPDATE_ID (%id%)!"
                        .replace("%id%", RESPAWN_ANCHOR_UPDATE_ID + ""));

            // charge is guaranteed to be bound within 0-5 in a valid packet
            byte charge = (byte) in.read();
            int x = StreamUtils.readVarInt(in);
            int y = StreamUtils.readVarInt(in);
            int z = StreamUtils.readVarInt(in);

            if (charge > 5 || charge < 0)
                throw new InvalidPacketException("Constructed RESPAWN_ANCHOR_UPDATE_PACKET has an invalid charge (%c%)!"
                        .replace("%c%", charge + ""));

            this.charge = charge;
            this.x = x;
            this.y = y;
            this.z = z;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse RESPAWN_ANCHOR_UPDATE_PACKET packet!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleRespawnAnchorUpdate(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RespawnAnchorUpdatePacket)) return false;
        RespawnAnchorUpdatePacket that = (RespawnAnchorUpdatePacket) o;
        return charge == that.charge && x == that.x && y == that.y && z == that.z;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private byte charge = -1;
        private int x;
        private int y;
        private int z;

        private Builder() {}

        public Builder charge(int charge) {
            this.charge = (byte) charge;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder z(int z) {
            this.z = z;
            return this;
        }

        public RespawnAnchorUpdatePacket build() {
            if (charge == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new RespawnAnchorUpdatePacket(charge, x, y, z);
        }
    }

    public byte getCharge() {
        return charge;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
