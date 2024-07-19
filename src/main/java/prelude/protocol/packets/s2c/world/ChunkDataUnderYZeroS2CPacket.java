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
import prelude.protocol.world.PreludeUndergroundChunk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkDataUnderYZeroS2CPacket extends S2CPacket {
    private int chunkCoordX;
    private int chunkCoordZ;
    private boolean isChunkPreChunk;
    private PreludeUndergroundChunk chunk;

    public ChunkDataUnderYZeroS2CPacket() {

    }

    private ChunkDataUnderYZeroS2CPacket(int chunkCoordX, int chunkCoordZ, PreludeUndergroundChunk chunk, boolean isChunkPreChunk) {
        this.chunkCoordX = chunkCoordX;
        this.chunkCoordZ = chunkCoordZ;
        this.chunk = chunk;
        this.isChunkPreChunk = isChunkPreChunk;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);

        StreamUtils.writeVarInt(chunkCoordX, bao);
        StreamUtils.writeVarInt(chunkCoordZ, bao);

        bao.write(isChunkPreChunk ? 1 : 0);
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
            this.validateOrThrow("CHUNK_DATA_UNDER_Y_ZERO_ID", is);

            this.chunkCoordX = StreamUtils.readVarInt(is);
            this.chunkCoordZ = StreamUtils.readVarInt(is);
            this.isChunkPreChunk = is.read() != 0;
            this.chunk = PreludeUndergroundChunk.deserialize(is);
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse CHUNK_DATA_UNDER_Y_ZERO_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleChunkDataUnderYZero(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int chunkCoordX;
        private int chunkCoordZ;
        private boolean isChunkPreChunk;
        private PreludeUndergroundChunk chunk;

        public Builder chunkCoordX(int chunkCoordX) {
            this.chunkCoordX = chunkCoordX;
            return this;
        }

        public Builder chunkCoordZ(int chunkCoordZ) {
            this.chunkCoordZ = chunkCoordZ;
            return this;
        }

        public Builder isChunkPreChunk(boolean isChunkPreChunk) {
            this.isChunkPreChunk = isChunkPreChunk;
            return this;
        }

        public Builder chunk(PreludeUndergroundChunk chunk) {
            this.chunk = chunk;
            return this;
        }

        public ChunkDataUnderYZeroS2CPacket build() {
            return new ChunkDataUnderYZeroS2CPacket(chunkCoordX, chunkCoordZ, chunk, isChunkPreChunk);
        }
    }
}
