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

package prelude.protocol.packets.s2c.world;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;
import prelude.protocol.StreamUtils;
import prelude.protocol.world.PreludeChunk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkDataModernS2CPacket extends S2CPacket {
    private int chunkCoordX;
    private int chunkCoordZ;
    private PreludeChunk chunk;

    public ChunkDataModernS2CPacket() {}

    private ChunkDataModernS2CPacket(int chunkCoordX, int chunkCoordZ, PreludeChunk chunk) {
        this.chunkCoordX = chunkCoordX;
        this.chunkCoordZ = chunkCoordZ;
        this.chunk = chunk;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);

        StreamUtils.writeVarInt(chunkCoordX, bao);
        StreamUtils.writeVarInt(chunkCoordZ, bao);
        chunk.write(bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object packet) {
        return false;
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("CHUNK_DATA_MODERN_ID", is);

            this.chunkCoordX = StreamUtils.readVarInt(is);
            this.chunkCoordZ = StreamUtils.readVarInt(is);
            this.chunk = PreludeChunk.deserialize(is);
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse CHUNK_DATA_MODERN_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleChunkDataModern(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int chunkCoordX;
        private int chunkCoordZ;
        private PreludeChunk chunk;

        public Builder chunkCoordX(int chunkCoordX) {
            this.chunkCoordX = chunkCoordX;
            return this;
        }

        public Builder chunkCoordZ(int chunkCoordZ) {
            this.chunkCoordZ = chunkCoordZ;
            return this;
        }

        public Builder chunk(PreludeChunk chunk) {
            this.chunk = chunk;
            return this;
        }

        public ChunkDataModernS2CPacket build() {
            return new ChunkDataModernS2CPacket(chunkCoordX, chunkCoordZ, chunk);
        }
    }

    public int getChunkCoordX() {
        return chunkCoordX;
    }

    public int getChunkCoordZ() {
        return chunkCoordZ;
    }

    public PreludeChunk getChunk() {
        return chunk;
    }
}
