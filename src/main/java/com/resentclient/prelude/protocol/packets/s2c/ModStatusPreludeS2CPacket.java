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

package com.resentclient.prelude.protocol.packets.s2c;

import com.resentclient.common.WriteableObject;
import com.resentclient.prelude.protocol.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ModStatusPreludeS2CPacket extends PreludeS2CPacket {
    private String modIdentifier;
    private ModStatus modStatus;

    public ModStatusPreludeS2CPacket() {}

    private ModStatusPreludeS2CPacket(String modIdentifier, ModStatus modStatus) {
        this.modIdentifier = modIdentifier;
        this.modStatus = modStatus;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);

        bao.write(modIdentifier.length());
        bao.write(modIdentifier.getBytes(StandardCharsets.US_ASCII));

        modStatus.write(bao);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("MOD_STATUS_ID", is);

            String modId = StreamUtils.readASCII(is.read(), is);
            ModStatus modStatus = ModStatus.from((byte) is.read());

            if (modStatus == null)
                throw new InvalidPreludePacketException("Constructed MOD_STATUS_PACKET has an illegal ModStatus!");

            this.modIdentifier = modId;
            this.modStatus = modStatus;
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse MOD_STATUS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleModStatus(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModStatusPreludeS2CPacket)) return false;
        ModStatusPreludeS2CPacket that = (ModStatusPreludeS2CPacket) o;
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

        public ModStatusPreludeS2CPacket build() {
            if (modIdentifier == null || modStatus == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new ModStatusPreludeS2CPacket(modIdentifier, modStatus);
        }
    }

    public enum ModStatus implements WriteableObject {
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

        @Override
        public void write(OutputStream out) throws IOException {
            out.write(value);
        }
    }

    public String getModIdentifier() {
        return modIdentifier;
    }

    public ModStatus getModStatus() {
        return modStatus;
    }
}
