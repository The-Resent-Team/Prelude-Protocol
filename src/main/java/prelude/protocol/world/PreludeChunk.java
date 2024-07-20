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

package prelude.protocol.world;

import prelude.protocol.StreamUtils;
import prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PreludeChunk implements WriteableObject {
    private final PreludeBlockType[][][] blocks;
    private final PreludeBiome[][] biomes;
    private final int chunkX;
    private final int chunkZ;
    private final boolean isPreChunk;
    private final PreludeChunkType chunkType;

    public PreludeChunk(final int chunkX, final int chunkZ, final boolean isPreChunk, final PreludeChunkType chunkType) {
        this.blocks = new PreludeBlockType[16][16][64];
        this.biomes = new PreludeBiome[16][16];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.isPreChunk = isPreChunk;
        this.chunkType = chunkType;
    }

    public void setBlock(final int xPosRelative, final int yPosRelative, final int zPosRelative, final PreludeBlockType block) {
        blocks[xPosRelative][yPosRelative][zPosRelative] = block;
    }

    public void setBiome(final int xPosRelative, final int zPosRelative, final PreludeBiome biome) {
        biomes[xPosRelative][zPosRelative] = biome;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(isPreChunk ? 1 : 0);
        chunkType.write(out);
        StreamUtils.writeVarInt(chunkX, out);
        StreamUtils.writeVarInt(chunkZ, out);

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 64; y++) {
                    blocks[x][z][y].write(out);
                }

                biomes[x][z].write(out);
            }
    }

    public static PreludeChunk deserialize(InputStream is) throws IOException {
        boolean isPreChunk = is.read() != 0;
        PreludeChunkType chunkType = PreludeChunkType.deserialize(is);
        int chunkX = StreamUtils.readVarInt(is);
        int chunkZ = StreamUtils.readVarInt(is);

        PreludeBlockType[][][] blocks = new PreludeBlockType[16][16][64];
        PreludeBiome[][] biomes = new PreludeBiome[16][16];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 64; k++) {
                    PreludeBlockType block = PreludeBlockType.deserialize(is);

                    blocks[i][j][k] = block;
                }

                biomes[i][j] = PreludeBiome.deserialize(is);
            }
        }

        return new PreludeChunk(chunkX, chunkZ, isPreChunk, chunkType);
    }

    public PreludeBlockType[][][] getBlocks() {
        return blocks;
    }

    public PreludeBiome[][] getBiomes() {
        return biomes;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public boolean isPreChunk() {
        return isPreChunk;
    }

    public PreludeChunkType getChunkType() {
        return chunkType;
    }
}
