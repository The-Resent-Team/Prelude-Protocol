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

import static prelude.protocol.packets.s2c.world.impl.PreludeBiome.ofLegacy;
import static prelude.protocol.packets.s2c.world.impl.PreludeBiome.ofModern;

import static prelude.protocol.Version.*;

public class PreludeBiomes {
    // ----------------------------- NORMAL BIOMES -----------------------------
    public static final PreludeBiome OCEAN = ofLegacy(V1_8, 0x00);
    public static final PreludeBiome PLAINS = ofLegacy(V1_8, 0x01);
    public static final PreludeBiome DESERT = ofLegacy(V1_8, 0x02);
    public static final PreludeBiome EXTREME_HILLS = ofLegacy(V1_8, 0x03);
    public static final PreludeBiome FOREST = ofLegacy(V1_8, 0x04);
    public static final PreludeBiome TAIGA = ofLegacy(V1_8, 0x05);
    public static final PreludeBiome SWAMPLAND = ofLegacy(V1_8, 0x06);
    public static final PreludeBiome RIVER = ofLegacy(V1_8, 0x07);

    /**+
     * The Nether
     * */
    public static final PreludeBiome HELL = ofLegacy(V1_8, 0x08);

    /**+
     * The End
     * */
    public static final PreludeBiome SKY = ofLegacy(V1_8, 0x09);
    public static final PreludeBiome FROZEN_OCEAN = ofLegacy(V1_8, 0x0A);
    public static final PreludeBiome FROZEN_RIVER = ofLegacy(V1_8, 0x0B);

    /**+
     * Ice Plains
     * */
    public static final PreludeBiome ICE_FLATS = ofLegacy(V1_8, 0x0C);
    public static final PreludeBiome ICE_MOUNTAINS = ofLegacy(V1_8, 0x0D);
    public static final PreludeBiome MUSHROOM_ISLAND = ofLegacy(V1_8, 0x0E);
    public static final PreludeBiome MUSHROOM_ISLAND_SHORE = ofLegacy(V1_8, 0x0F);

    /**+
     * Beach
     * */
    public static final PreludeBiome BEACHES = ofLegacy(V1_8, 0x10);
    public static final PreludeBiome DESERT_HILLS = ofLegacy(V1_8, 0x11);
    public static final PreludeBiome FOREST_HILLS = ofLegacy(V1_8, 0x12);
    public static final PreludeBiome TAIGA_HILLS = ofLegacy(V1_8, 0x13);
    public static final PreludeBiome SMALLER_EXTREME_HILLS = ofLegacy(V1_8, 0x14);
    public static final PreludeBiome JUNGLE = ofLegacy(V1_8, 0x15);
    public static final PreludeBiome JUNGLE_HILLS = ofLegacy(V1_8, 0x16);
    public static final PreludeBiome JUNGLE_EDGE = ofLegacy(V1_8, 0x17);
    public static final PreludeBiome DEEP_OCEAN = ofLegacy(V1_8, 0x18);
    public static final PreludeBiome STONE_BEACH = ofLegacy(V1_8, 0x19);
    public static final PreludeBiome COLD_BEACH = ofLegacy(V1_8, 0x1A);
    public static final PreludeBiome BIRCH_FOREST = ofLegacy(V1_8, 0x1B);
    public static final PreludeBiome BIRCH_FOREST_HILLS = ofLegacy(V1_8, 0x1C);
    public static final PreludeBiome ROOFED_FOREST = ofLegacy(V1_8, 0x1D);

    /**+
     * Cold Taiga
     * */
    public static final PreludeBiome TAIGA_COLD = ofLegacy(V1_8, 0x1E);

    /**+
     * Cold Taiga Hills
     * */
    public static final PreludeBiome TAIGA_COLD_HILLS = ofLegacy(V1_8, 0x1F);

    /**+
     * Mega Taiga
     * */
    public static final PreludeBiome REDWOOD_TAIGA = ofLegacy(V1_8, 0x20);

    /**+
     * Mega Taiga Hills
     * */
    public static final PreludeBiome REDWOOD_TAIGA_HILLS = ofLegacy(V1_8, 0x21);

    /**+
     * Extreme Hills Plus
     * */
    public static final PreludeBiome EXTREME_HILLS_WITH_TREES = ofLegacy(V1_8, 0x22);
    public static final PreludeBiome SAVANNA = ofLegacy(V1_8, 0x23);
    public static final PreludeBiome SAVANNA_PLATEAU = ofLegacy(V1_8, 0x24);
    public static final PreludeBiome MESA = ofLegacy(V1_8, 0x25);

    /**+
     * Mesa Plateau F
     * */
    public static final PreludeBiome MESA_ROCK = ofLegacy(V1_8, 0x26);

    /**+
     * Mesa Plateau
     * */
    public static final PreludeBiome MESA_CLEAR_ROCK = ofLegacy(V1_8, 0x27);

    // ----------------------------- MUTATED BIOMES -----------------------------
    // these biomes are not in the needed biomes to visit list
    // for that one achievement, but exist and (most) are used

    /**+
     * Not the void, but rather this is a biome only accessible with Superflat Presets
     */
    public static final PreludeBiome THE_VOID = ofLegacy(V1_9, 0x7F);

    /**+
     * Sunflower Plains
     * */
    public static final PreludeBiome MUTATED_PLAINS = ofLegacy(V1_8, 0x81);

    /**+
     * Unsure, known as "Desert M" pre-1.13
     * */
    public static final PreludeBiome MUTATED_DESERT = ofLegacy(V1_8, 0x82);

    /**+
     * Extreme Hills M
     * */
    public static final PreludeBiome MUTATED_EXTREME_HILLS = ofLegacy(V1_8, 0x83);

    /**+
     * Flower Forest
     * */
    public static final PreludeBiome MUTATED_FOREST = ofLegacy(V1_8, 0x84);

    /**+
     * Taiga M
     * */
    public static final PreludeBiome MUTATED_TAIGA = ofLegacy(V1_8, 0x85);

    /**+
     * Swampland M
     * */
    public static final PreludeBiome MUTATED_SWAMPLAND = ofLegacy(V1_8, 0x86);

    /**+
     * Ice Plains Spikes
     * */
    public static final PreludeBiome MUTATED_ICE_FLATS = ofLegacy(V1_8, 0x8C);

    /**+
     * Jungle M
     * */
    public static final PreludeBiome MUTATED_JUNGLE = ofLegacy(V1_8, 0x95);

    /**+
     * Jungle Edge M
     * */
    public static final PreludeBiome MUTATED_JUNGLE_EDGE = ofLegacy(V1_8, 0x97);

    /**+
     * Birch Forest M
     * */
    public static final PreludeBiome MUTATED_BIRCH_FOREST = ofLegacy(V1_8, 0x9B);

    /**+
     * Birch Forest Hills M
     * */
    public static final PreludeBiome MUTATED_BIRCH_FOREST_HILLS = ofLegacy(V1_8, 0x9C);

    /**+
     * Roofed Forest M
     */
    public static final PreludeBiome MUTATED_ROOFED_FOREST = ofLegacy(V1_8, 0x9D);

    /**+
     * Cold Taiga M
     * */
    public static final PreludeBiome MUTATED_TAIGA_COLD = ofLegacy(V1_8, 0x9E);

    /**+
     * Mega Spruce Taiga
     */
    public static final PreludeBiome MUTATED_REDWOOD_TAIGA = ofLegacy(V1_8, 0xA0);

    /**+
     * Redwood Taiga Hills M
     * */
    public static final PreludeBiome MUTATED_REDWOOD_TAIGA_HILLS = ofLegacy(V1_8, 0xA1);

    /**+
     * Extreme Hills Plus M
     * */
    public static final PreludeBiome MUTATED_EXTREME_HILLS_WITH_TREES = ofLegacy(V1_8, 0xA2);

    /**+
     * Savanna M
     * */
    public static final PreludeBiome MUTATED_SAVANNA = ofLegacy(V1_8, 0xA3);

    /**+
     * Savanna Plateau M
     * */
    public static final PreludeBiome MUTATED_SAVANNA_ROCK = ofLegacy(V1_8, 0xA4);

    /**+
     * Mesa (Bryce)
     * */
    public static final PreludeBiome MUTATED_MESA = ofLegacy(V1_8, 0xA5);

    /**+
     * Mesa Plateau F M
     * */
    public static final PreludeBiome MUTATED_MESA_ROCK = ofLegacy(V1_8, 0xA6);

    /**+
     * Mesa Plateau M
     * */
    public static final PreludeBiome MUTATED_MESA_CLEAR_ROCK = ofLegacy(V1_8, 0xA6);
}
