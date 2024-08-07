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
import com.resentclient.prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
* The Server TPS is stored as double in the actual
* bukkit server code, and in this packet we assume
* that the characteristic of the TPS does not
* exceed 255 (it really shouldn't).
* We also make the assumption that the mantissa of
* the tps does not need more than 16 bits / 2 bytes
* to encode. These assumptions are made because
* 1) they are unlikely to be broken in actual prod
* 2) they cut down on the payload size (encoding a
* raw 8 byte double is far worse than encoding 3 bytes)
* 3) these assumptions make it easier for the client
* */
public class ServerTpsPreludeS2CPacket extends PreludeS2CPacket {
    // we will just assume that the characteristic does reach over 255
    private int characteristic;
    private short mantissa;

    public ServerTpsPreludeS2CPacket() {}

    private ServerTpsPreludeS2CPacket(int characteristic, short mantissa) {
        this.characteristic = characteristic;
        this.mantissa = mantissa;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        bao.write(characteristic);
        StreamUtils.writeShort(mantissa, bao);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("SERVER_TPS_ID", is);

            int characteristic = (int) is.read();
            short mantissa = (short) StreamUtils.readShort(is);

            // how the actual hell would you get negative tps
            if (characteristic < 0 || mantissa < 0)
                throw new InvalidPreludePacketException("Constructed SERVER_TPS_PACKET is invalid!");

            this.characteristic = characteristic;
            this.mantissa = mantissa;
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse SERVER_TPS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleServerTps(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerTpsPreludeS2CPacket)) return false;
        ServerTpsPreludeS2CPacket that = (ServerTpsPreludeS2CPacket) o;
        return characteristic == that.characteristic && mantissa == that.mantissa;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int characteristic = -1;
        private short mantissa = -1;

        public Builder characteristic(int characteristic) {
            this.characteristic = (int) characteristic;
            return this;
        }

        public Builder mantissa(int mantissa) {
            this.mantissa = (short) mantissa;
            return this;
        }

        public ServerTpsPreludeS2CPacket build() {
            if (characteristic == -1 || mantissa == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new ServerTpsPreludeS2CPacket(characteristic, mantissa);
        }
    }

    public int getCharacteristic() {
        return characteristic;
    }

    public short getMantissa() {
        return mantissa;
    }
}
