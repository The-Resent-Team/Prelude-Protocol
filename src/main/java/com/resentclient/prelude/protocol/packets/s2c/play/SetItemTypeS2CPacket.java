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

import com.resentclient.prelude.protocol.InvalidPacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.PreludeS2CPacketHandler;
import com.resentclient.prelude.protocol.StreamUtils;
import com.resentclient.prelude.protocol.world.PreludeItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/*
* Used to correct viaversion
* */
public class SetItemTypeS2CPacket extends PreludeS2CPacket {
    private int slot;
    private PreludeItem item; // i cba to deal with enchantments, so we only take the item type for rendering

    public SetItemTypeS2CPacket() {}

    private SetItemTypeS2CPacket(int slot, PreludeItem item) {
        this.slot = slot;
        this.item = item;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        
        bao.write(packetId);
        StreamUtils.writeShort(slot, bao);
        item.write(bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SetItemTypeS2CPacket)) return false;
        SetItemTypeS2CPacket that = (SetItemTypeS2CPacket) object;
        return slot == that.slot && Objects.equals(item, that.item);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("SET_ITEM_TYPE_ID", is);
            int slot = StreamUtils.readShort(is);
            PreludeItem item = PreludeItem.deserialize(is);

            this.slot = slot;
            this.item = item;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse SET_ITEM_TYPE_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleSetItemType(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int slot = -1;
        private PreludeItem item;

        public Builder slot(int slot) {
            this.slot = slot;
            return this;
        }

        public Builder item(PreludeItem item) {
            this.item = item;
            return this;
        }

        public SetItemTypeS2CPacket build() {
            return new SetItemTypeS2CPacket(slot, item);
        }
    }

    public int getSlot() {
        return slot;
    }

    public PreludeItem getItem() {
        return item;
    }
}
