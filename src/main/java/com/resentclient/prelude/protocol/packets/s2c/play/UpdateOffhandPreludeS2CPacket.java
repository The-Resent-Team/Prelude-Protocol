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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UpdateOffhandPreludeS2CPacket extends PreludeS2CPacket {
    private boolean canClientDisregardThis;
    private String serializedItem;

    public UpdateOffhandPreludeS2CPacket() {}

    private UpdateOffhandPreludeS2CPacket(boolean canClientDisregardThis, String serializedItem) {
        this.canClientDisregardThis = canClientDisregardThis;
        this.serializedItem = serializedItem;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        bao.write(canClientDisregardThis ? 1 : 0);

        StreamUtils.writeShort(serializedItem.length(), bao);
        bao.write(serializedItem.getBytes(StandardCharsets.US_ASCII));

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("UPDATE_OFFHAND_ID", is);

            boolean canClientDisregardThis = is.read() != 0;
            String serializedItem = StreamUtils.readASCII(StreamUtils.readShort(is), is);

            if (!serializedItem.startsWith("ItemStack{") || !serializedItem.endsWith("}"))
                throw new InvalidPreludePacketException("Constructed UPDATE_OFFHAND_PACKET has an invalid serialized item!");

            this.canClientDisregardThis = canClientDisregardThis;
            this.serializedItem = serializedItem;
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse UPDATE_OFFHAND_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleOffhandUpdate(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOffhandPreludeS2CPacket)) return false;
        UpdateOffhandPreludeS2CPacket that = (UpdateOffhandPreludeS2CPacket) o;
        return canClientDisregardThis == that.canClientDisregardThis && Objects.equals(serializedItem, that.serializedItem);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean canClientDisregardThis = false;
        private String serializedItem = null;

        private Builder() {}

        public Builder canClientDisregardThis(boolean canClientDisregardThis) {
            this.canClientDisregardThis = canClientDisregardThis;
            return this;
        }

        public Builder serializedItem(String serializedItem) {
            this.serializedItem = serializedItem;
            return this;
        }

        public UpdateOffhandPreludeS2CPacket build() {
            if (serializedItem == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new UpdateOffhandPreludeS2CPacket(canClientDisregardThis, serializedItem);
        }
    }

    public boolean isCanClientDisregardThis() {
        return canClientDisregardThis;
    }

    public String getSerializedItem() {
        return serializedItem;
    }
}
