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

import prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

public class PreludeChunk implements WriteableObject {
    private final PreludeBlockType[][][] blocks;
    private final PreludeBiome[][] biomes;
    private final PreludeChunkCoordinate chunkCoordinate;
    private final boolean isPreChunk;

    public PreludeChunk(PreludeChunkCoordinate chunkCoordinate, final boolean isPreChunk) {
        this.blocks = new PreludeBlockType[64][16][16];
        this.biomes = new PreludeBiome[16][16];
        this.chunkCoordinate = chunkCoordinate;
        this.isPreChunk = isPreChunk;
    }

    public PreludeChunk(PreludeBlockType[][][] blocks, PreludeBiome[][] biomes, PreludeChunkCoordinate chunkCoordinate, final boolean isPreChunk) {
        if (blocks.length != 64)
            throw new IllegalArgumentException("A chunk must be exactly 16 blocks long in the Y axis, but this is {} blocks long!"
                    .replace("{}", blocks.length + ""));
        if (blocks[0].length != 16)
            throw new IllegalArgumentException("A chunk must be exactly 16 blocks long in the Z axis, but this is {} blocks long!"
                    .replace("{}", blocks[0].length + ""));
        if (blocks[0][0].length != 16)
            throw new IllegalArgumentException("A chunk must be exactly 16 blocks long in the X axis, but this is {} blocks long!"
                    .replace("{}", blocks[0][0].length + ""));

        this.blocks = blocks;
        this.biomes = biomes;
        this.chunkCoordinate = chunkCoordinate;
        this.isPreChunk = isPreChunk;
    }

    public void setBlock(PreludeRelativeCoordinate coordinate, PreludeBlockType block) {
        blocks[coordinate.relativePosX][coordinate.relativePosY][coordinate.relativePosZ] = block;
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
        chunkCoordinate.write(out);

        for (int y = 0; y < 64; y++)
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    blocks[y][z][x].write(out);
                    biomes[x][z].write(out);
                }
            }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PreludeChunk)) return false;
        PreludeChunk that = (PreludeChunk) object;
        return isPreChunk == that.isPreChunk && Objects.deepEquals(blocks, that.blocks) && Objects.deepEquals(biomes, that.biomes) && Objects.equals(chunkCoordinate, that.chunkCoordinate);
    }

    public static PreludeChunk deserialize(InputStream is) throws IOException {
        boolean isPreChunk = is.read() != 0;
        PreludeChunkCoordinate chunkCoordinate = PreludeChunkCoordinate.deserialize(is);

        PreludeBlockType[][][] blocks = new PreludeBlockType[16][16][64];
        PreludeBiome[][] biomes = new PreludeBiome[16][16];

        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    PreludeBlockType block = PreludeBlockType.deserialize(is);

                    blocks[i][j][k] = block;
                }

                biomes[i][j] = PreludeBiome.deserialize(is);
            }
        }

        return new PreludeChunk(blocks, biomes, chunkCoordinate, isPreChunk);
    }

    public PreludeBlockType[][][] getBlocks() {
        return blocks;
    }

    public PreludeBiome[][] getBiomes() {
        return biomes;
    }

    public PreludeChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public boolean isPreChunk() {
        return isPreChunk;
    }
}
