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

import com.resentclient.prelude.protocol.world.*;
import com.resentclient.prelude.protocol.InvalidPacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.PreludeS2CPacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/*
* Used to update ONE y < 0 or y > 255 block (specified by #getChunkType)
* This is also used to replace viaversion replacements
* */
public class BlockChangeModernS2CPacket extends PreludeS2CPacket {
    private PreludeCompactCoordinate compactCoordinate;
    private PreludeBlockType newBlock;

    public BlockChangeModernS2CPacket() {}

    private BlockChangeModernS2CPacket(PreludeCompactCoordinate compactCoordinate, PreludeBlockType newBlock) {
        this.compactCoordinate = compactCoordinate;
        this.newBlock = newBlock;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        compactCoordinate.write(bao);
        newBlock.write(bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BlockChangeModernS2CPacket)) return false;
        BlockChangeModernS2CPacket that = (BlockChangeModernS2CPacket) object;
        return Objects.equals(newBlock, that.newBlock) && Objects.equals(compactCoordinate, that.compactCoordinate);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("BLOCK_CHANGE_MODERN_ID", is);

            PreludeCompactCoordinate compactCoordinate = PreludeCompactCoordinate.deserialize(is);
            PreludeBlockType newBlock = PreludeBlockType.deserialize(is);

            this.compactCoordinate = compactCoordinate;
            this.newBlock = newBlock;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse BLOCK_CHANGE_MODERN_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
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
            return new BlockChangeModernS2CPacket(new PreludeCompactCoordinate(chunkType, new PreludeChunkCoordinate(chunkX, chunkZ), new PreludeRelativeCoordinate(relativePosX, relativePosY, relativePosZ)), newBlock);
        }
    }

    public PreludeCompactCoordinate getCompactCoordinate() {
        return compactCoordinate;
    }

    public PreludeBlockType getNewBlock() {
        return newBlock;
    }
}
