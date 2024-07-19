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

package prelude.protocol.packets.s2c.world.impl;

import prelude.protocol.StreamUtils;
import prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class PreludeUndergroundChunk implements WriteableObject {
    private static final int X_MASK = 0b00000000000011111;
    private static final int Z_MASK = 0b00000001111100000;
    private static final int Y_MASK = 0b11111110000000000;
    private static final int X_SHIFT = 0;
    private static final int Z_SHIFT = 5;
    private static final int Y_SHIFT = 10;

    private PreludeBlockType[][][] blocks;
    private PreludeBiome[][] biomes;
    private int chunkX;
    private int chunkZ;
    private boolean isPreChunk;

    public PreludeUndergroundChunk(final int chunkX, final int chunkZ, final boolean isPreChunk) {
        this.blocks = new PreludeBlockType[16][16][64];
        this.biomes = new PreludeBiome[16][16];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.isPreChunk = isPreChunk;
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
        StreamUtils.writeVarInt(chunkX, out);
        StreamUtils.writeVarInt(chunkZ, out);

        for (int x = 0; x < 16; x++)
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 64; z++) {
                    StreamUtils.writeVarInt((x << X_SHIFT) + (z << Z_SHIFT) + (y << Y_SHIFT), out);
                    blocks[x][y][z].write(out);
                }

                biomes[x][y].write(out);
            }
    }

    public static PreludeUndergroundChunk deserialize(InputStream is) throws IOException {
        boolean isPreChunk = is.read() != 0;
        int chunkX = StreamUtils.readVarInt(is);
        int chunkZ = StreamUtils.readVarInt(is);

        PreludeBlockType[][][] blocks = new PreludeBlockType[16][16][64];
        PreludeBiome[][] biomes = new PreludeBiome[16][16];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 64; k++) {
                    int serialized = StreamUtils.readVarInt(is);
                    int x = (serialized & X_MASK) >> X_SHIFT;
                    int z = (serialized & Z_MASK) >> Z_SHIFT;
                    int y = (serialized & Y_MASK) >> Y_SHIFT;

                    PreludeBlockType block = PreludeBlockType.deserialize(is);

                    blocks[i][j][k] = block;
                }

                biomes[i][j] = PreludeBiome.deserialize(is);
            }
        }

        return new PreludeUndergroundChunk(chunkX, chunkZ, isPreChunk);
    }
}
