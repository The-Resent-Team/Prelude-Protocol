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

import prelude.protocol.*;
import prelude.protocol.world.PreludeBlockType;
import prelude.protocol.world.PreludeChunkType;

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
public class MultiBlockChangeModernS2CPacket extends S2CPacket {
    private PreludeChunkType chunkType;
    private int chunkX;
    private int chunkZ;
    private Set<PreludeBlock> blocksToChange;

    public MultiBlockChangeModernS2CPacket() {}

    private MultiBlockChangeModernS2CPacket(PreludeChunkType chunkType, int chunkX, int chunkZ, Set<PreludeBlock> blocksToChange) {
        this.chunkType = chunkType;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        if (blocksToChange.size() > 64 || blocksToChange.size() < 2)
            throw new IllegalArgumentException("This packet can only update between 2 and 64 blocks!");

        this.blocksToChange = blocksToChange;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(packetId);

        StreamUtils.writeVarInt(chunkX, bao);
        StreamUtils.writeVarInt(chunkZ, bao);
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
        return chunkX == that.chunkX && chunkZ == that.chunkZ && chunkType == that.chunkType && Objects.equals(blocksToChange, that.blocksToChange);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("MULTI_BLOCK_CHANGE_MODERN_ID", is);

            int chunkX = StreamUtils.readVarInt(is);
            int chunkZ = StreamUtils.readVarInt(is);
            PreludeChunkType chunkType = PreludeChunkType.deserialize(is);

            int amt = is.read();
            Set<PreludeBlock> blocks = new HashSet<>(amt);

            for (int i = 0; i < amt; i++)
                blocks.add(PreludeBlock.deserialize(is));

            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.chunkType = chunkType;
            this.blocksToChange = blocks;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse MULTI_BLOCK_CHANGE_MODERN_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
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
            return new MultiBlockChangeModernS2CPacket(chunkType, chunkX, chunkZ, blocksToChange);
        }
    }

    public static class PreludeBlock implements WriteableObject {
        private int relativeX;
        private int relativeY;
        private int relativeZ;
        private PreludeBlockType type;

        public PreludeBlock(int relativeX, int relativeY, int relativeZ, PreludeBlockType type) {
            this.relativeX = relativeX;
            this.relativeY = relativeY;
            this.relativeZ = relativeZ;
            this.type = type;
        }

        @Override
        public void write(OutputStream out) throws IOException {
            out.write(relativeX);
            out.write(relativeY);
            out.write(relativeZ);
            type.write(out);
        }

        public static PreludeBlock deserialize(InputStream in) throws IOException {
            int relativeX = in.read();
            int relativeY = in.read();
            int relativeZ = in.read();
            PreludeBlockType type = PreludeBlockType.deserialize(in);

            return new PreludeBlock(relativeX, relativeY, relativeZ, type);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof PreludeBlock)) return false;
            PreludeBlock that = (PreludeBlock) object;
            return relativeX == that.relativeX && relativeY == that.relativeY && relativeZ == that.relativeZ && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(relativeX, relativeY, relativeZ, type);
        }

        public int getRelativeX() {
            return relativeX;
        }

        public int getRelativeY() {
            return relativeY;
        }

        public int getRelativeZ() {
            return relativeZ;
        }

        public PreludeBlockType getType() {
            return type;
        }
    }
}
