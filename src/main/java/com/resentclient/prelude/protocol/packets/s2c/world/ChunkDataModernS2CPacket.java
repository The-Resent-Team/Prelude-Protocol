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

package com.resentclient.prelude.protocol.packets.s2c.world;

import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.PreludeS2CPacketHandler;
import com.resentclient.prelude.protocol.world.PreludeChunk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/*
* Also used to update chunks when more than 64 blocks are changed
* */
public class ChunkDataModernS2CPacket extends PreludeS2CPacket {
    private PreludeChunk chunk;

    public ChunkDataModernS2CPacket() {}

    private ChunkDataModernS2CPacket(PreludeChunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);
        chunk.write(bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ChunkDataModernS2CPacket)) return false;
        ChunkDataModernS2CPacket that = (ChunkDataModernS2CPacket) object;
        return Objects.equals(chunk, that.chunk);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("CHUNK_DATA_MODERN_ID", is);

            this.chunk = PreludeChunk.deserialize(is);
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse CHUNK_DATA_MODERN_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleChunkDataModern(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PreludeChunk chunk;

        public Builder chunk(PreludeChunk chunk) {
            this.chunk = chunk;
            return this;
        }

        public ChunkDataModernS2CPacket build() {
            return new ChunkDataModernS2CPacket(chunk);
        }
    }

    public PreludeChunk getChunk() {
        return chunk;
    }
}
