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
import prelude.protocol.world.PreludeBlockType;
import prelude.protocol.world.PreludeChunkType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/*
* Used to update y < 0 or y > 255 blocks
* */
public class BlockChangeModernS2CPacket extends S2CPacket {
    private PreludeChunkType chunkType;
    private int chunkX;
    private int chunkZ;
    private PreludeBlockType newBlock;
    private int relativePosX;
    private int relativePosY;
    private int relativePosZ;

    public BlockChangeModernS2CPacket() {}

    private BlockChangeModernS2CPacket(PreludeChunkType chunkType, int chunkX, int chunkZ, PreludeBlockType newBlock, int relativePosX, int relativePosY, int relativePosZ) {
        this.chunkType = chunkType;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.newBlock = newBlock;
        this.relativePosX = relativePosX;
        this.relativePosY = relativePosY;
        this.relativePosZ = relativePosZ;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        chunkType.write(bao);
        StreamUtils.writeVarInt(chunkX, bao);
        StreamUtils.writeVarInt(chunkZ, bao);
        newBlock.write(bao);
        bao.write(relativePosX);
        bao.write(relativePosY);
        bao.write(relativePosZ);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BlockChangeModernS2CPacket)) return false;
        BlockChangeModernS2CPacket that = (BlockChangeModernS2CPacket) object;
        return chunkX == that.chunkX && chunkZ == that.chunkZ && relativePosX == that.relativePosX && relativePosY == that.relativePosY && relativePosZ == that.relativePosZ && chunkType == that.chunkType && Objects.equals(newBlock, that.newBlock);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("BLOCK_CHANGE_MODERN_ID", is);

            PreludeChunkType chunkType = PreludeChunkType.deserialize(is);
            int chunkX = StreamUtils.readVarInt(is);
            int chunkZ = StreamUtils.readVarInt(is);
            PreludeBlockType newBlock = PreludeBlockType.deserialize(is);
            int relativePosX = is.read();
            int relativePosY = is.read();
            int relativePosZ = is.read();

            this.chunkType = chunkType;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.newBlock = newBlock;
            this.relativePosX = relativePosX;
            this.relativePosY = relativePosY;
            this.relativePosZ = relativePosZ;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse BLOCK_CHANGE_MODERN_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleBlockChangeModern(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PreludeChunkType chunkType;
        private int chunkX;
        private int chunkZ;
        private PreludeBlockType newBlock;
        private int relativePosX;
        private int relativePosY;
        private int relativePosZ;

        public Builder chunkType(PreludeChunkType chunkType) {
            this.chunkType = chunkType;
            return this;
        }

        public Builder chunkX(int chunkX) {
            this.chunkX = chunkX;
            return this;
        }

        public Builder chunkZ(int chunkZ) {
            this.chunkZ = chunkZ;
            return this;
        }

        public Builder newBlock(PreludeBlockType newBlock) {
            this.newBlock = newBlock;
            return this;
        }

        public Builder relativePosX(int relativePosX) {
            this.relativePosX = relativePosX;
            return this;
        }

        public Builder relativePosY(int relativePosY) {
            this.relativePosY = relativePosY;
            return this;
        }

        public Builder relativePosZ(int relativePosZ) {
            this.relativePosZ = relativePosZ;
            return this;
        }

        public BlockChangeModernS2CPacket build() {
            return new BlockChangeModernS2CPacket(chunkType, chunkX, chunkZ, newBlock, relativePosX, relativePosY, relativePosZ);
        }
    }

    public PreludeChunkType getChunkType() {
        return chunkType;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public PreludeBlockType getNewBlock() {
        return newBlock;
    }

    public int getRelativePosX() {
        return relativePosX;
    }

    public int getRelativePosY() {
        return relativePosY;
    }

    public int getRelativePosZ() {
        return relativePosZ;
    }
}
