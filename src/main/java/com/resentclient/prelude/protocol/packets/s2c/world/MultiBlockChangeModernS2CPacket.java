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
import com.resentclient.prelude.protocol.WriteableObject;
import com.resentclient.prelude.protocol.world.PreludeBlockType;
import com.resentclient.prelude.protocol.world.PreludeChunkCoordinate;
import com.resentclient.prelude.protocol.world.PreludeChunkType;
import com.resentclient.prelude.protocol.world.PreludeRelativeCoordinate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
 * Used to update 2 to 64 y < 0 or y > 255 blocks (specified by #getChunkType)
 * This is also used to replace viaversion replacements
 * */
public class MultiBlockChangeModernS2CPacket extends PreludeS2CPacket {
    private PreludeChunkType chunkType;
    private PreludeChunkCoordinate chunkCoordinate;
    private Set<PreludeBlock> blocksToChange;

    public MultiBlockChangeModernS2CPacket() {}

    private MultiBlockChangeModernS2CPacket(PreludeChunkType chunkType, PreludeChunkCoordinate chunkCoordinate, Set<PreludeBlock> blocksToChange) {
        this.chunkType = chunkType;
        this.chunkCoordinate = chunkCoordinate;

        if (blocksToChange.size() > 64 || blocksToChange.size() < 2)
            throw new IllegalArgumentException("This packet can only update between 2 and 64 blocks!");

        this.blocksToChange = blocksToChange;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        chunkCoordinate.write(bao);
        chunkType.write(bao);
        // only up to 64, we can write as a byte
        bao.write(blocksToChange.size());

        for (PreludeBlock block : blocksToChange)
            block.write(bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof MultiBlockChangeModernS2CPacket)) return false;
        MultiBlockChangeModernS2CPacket that = (MultiBlockChangeModernS2CPacket) object;
        return chunkType == that.chunkType && Objects.equals(chunkCoordinate, that.chunkCoordinate) && Objects.equals(blocksToChange, that.blocksToChange);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("MULTI_BLOCK_CHANGE_MODERN_ID", is);

            PreludeChunkCoordinate chunkCoordinate = PreludeChunkCoordinate.deserialize(is);
            PreludeChunkType chunkType = PreludeChunkType.deserialize(is);

            int amt = is.read();
            Set<PreludeBlock> blocks = new HashSet<>(amt);

            for (int i = 0; i < amt; i++)
                blocks.add(PreludeBlock.deserialize(is));

            this.chunkCoordinate = chunkCoordinate;
            this.chunkType = chunkType;
            this.blocksToChange = blocks;
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse MULTI_BLOCK_CHANGE_MODERN_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleMultiBlockChangeModern(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PreludeChunkType chunkType;
        private int chunkX;
        private int chunkZ;
        private Set<PreludeBlock> blocksToChange = new HashSet<>();

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

        public Builder blocksToChange(Set<PreludeBlock> blocksToChange) {
            this.blocksToChange.addAll(blocksToChange);
            return this;
        }

        public Builder blockToChange(PreludeBlock block) {
            this.blocksToChange.add(block);
            return this;
        }

        public MultiBlockChangeModernS2CPacket build() {
            return new MultiBlockChangeModernS2CPacket(chunkType, new PreludeChunkCoordinate(chunkX, chunkZ), blocksToChange);
        }
    }

    public static class PreludeBlock implements WriteableObject {
        private PreludeRelativeCoordinate relativeCoordinate;
        private PreludeBlockType type;

        public PreludeBlock(PreludeRelativeCoordinate relativeCoordinate, PreludeBlockType type) {
            this.relativeCoordinate = relativeCoordinate;
            this.type = type;
        }

        @Override
        public void write(OutputStream out) throws IOException {
            relativeCoordinate.write(out);
            type.write(out);
        }

        public static PreludeBlock deserialize(InputStream in) throws IOException {
            PreludeRelativeCoordinate coordinate = PreludeRelativeCoordinate.deserialize(in);
            PreludeBlockType type = PreludeBlockType.deserialize(in);

            return new PreludeBlock(coordinate, type);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof PreludeBlock)) return false;
            PreludeBlock that = (PreludeBlock) object;
            return Objects.equals(relativeCoordinate, that.relativeCoordinate) && Objects.equals(type, that.type);
        }

        public PreludeRelativeCoordinate getRelativeCoordinate() {
            return relativeCoordinate;
        }

        public PreludeBlockType getType() {
            return type;
        }
    }

    public PreludeChunkType getChunkType() {
        return chunkType;
    }

    public PreludeChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public Set<PreludeBlock> getBlocksToChange() {
        return blocksToChange;
    }
}
