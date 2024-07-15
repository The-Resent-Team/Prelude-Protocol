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

public class ClientSyncResponseC2SPacket extends C2SPacket {
    private int syncId;

    public ClientSyncResponseC2SPacket() {}

    public ClientSyncResponseC2SPacket(int syncId) {
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
    public boolean equals(Object packet) {
        return false;
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {

    }

    @Override
    public void processSelf(C2SPacketHandler handler) {

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

        public ClientSyncResponseC2SPacket build() {
            return new ClientSyncResponseC2SPacket(syncId);
        }
    }
}
