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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ModStatusS2CPacket extends S2CPacket {
    private String modIdentifier;
    private ModStatus modStatus;

    public ModStatusS2CPacket() {}

    private ModStatusS2CPacket(String modIdentifier, ModStatus modStatus) {
        this.modIdentifier = modIdentifier;
        this.modStatus = modStatus;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);

        bao.write(modIdentifier.length());
        bao.write(modIdentifier.getBytes(StandardCharsets.US_ASCII));

        bao.write(modStatus.value);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("MOD_STATUS_ID", is);

            String modId = StreamUtils.readASCII(is.read(), is);
            ModStatus modStatus = ModStatus.from((byte) is.read());

            if (modStatus == null)
                throw new InvalidPacketException("Constructed MOD_STATUS_PACKET has an illegal ModStatus!");

            this.modIdentifier = modId;
            this.modStatus = modStatus;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse MOD_STATUS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleModStatus(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModStatusS2CPacket)) return false;
        ModStatusS2CPacket that = (ModStatusS2CPacket) o;
        return Objects.equals(modIdentifier, that.modIdentifier) && modStatus == that.modStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String modIdentifier = null;
        private ModStatus modStatus = null;

        private Builder() {}

        public Builder modIdentifier(String modIdentifier) {
            this.modIdentifier = modIdentifier;
            return this;
        }

        public Builder modStatus(ModStatus modStatus) {
            this.modStatus = modStatus;
            return this;
        }

        public ModStatusS2CPacket build() {
            if (modIdentifier == null || modStatus == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new ModStatusS2CPacket(modIdentifier, modStatus);
        }
    }

    public enum ModStatus {
        SUPPORTED(0),
        DISABLE(1);

        public final int value;

        ModStatus(int value) {
            this.value = (int) value;
        }

        public static ModStatus from(byte value) {
            for (ModStatus modStatus : ModStatus.values()) {
                if (modStatus.value == value)
                    return modStatus;
            }
            return null;
        }
    }

    public String getModIdentifier() {
        return modIdentifier;
    }

    public ModStatus getModStatus() {
        return modStatus;
    }
}
