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

import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.PreludeS2CPacketHandler;
import com.resentclient.prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerSyncRequestS2CPacket extends PreludeS2CPacket {
    private int syncId;

    public ServerSyncRequestS2CPacket() {

    }

    private ServerSyncRequestS2CPacket(int syncId) {
        this.syncId = syncId;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);
        StreamUtils.writeVarInt(syncId, bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSyncRequestS2CPacket)) return false;
        ServerSyncRequestS2CPacket that = (ServerSyncRequestS2CPacket) o;
        return syncId == that.syncId;
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("SERVER_SYNC_REQUEST_ID", is);

            this.syncId = StreamUtils.readVarInt(is);
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse SERVER_SYNC_REQUEST_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleSyncRequest(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int syncId;

        public Builder syncId(int syncId) {
            this.syncId = syncId;
            return this;
        }

        public ServerSyncRequestS2CPacket build() {
            return new ServerSyncRequestS2CPacket(syncId);
        }
    }

    public int getSyncId() {
        return syncId;
    }
}
