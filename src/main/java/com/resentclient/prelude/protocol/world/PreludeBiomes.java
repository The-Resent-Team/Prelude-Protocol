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

package com.resentclient.prelude.protocol.world;

import static com.resentclient.prelude.protocol.Version.*;

public class PreludeBiomes {
    // ----------------------------- NORMAL BIOMES -----------------------------
    public static final PreludeBiome OCEAN = PreludeBiome.ofLegacy(V1_8, 0x00);
    public static final PreludeBiome PLAINS = PreludeBiome.ofLegacy(V1_8, 0x01);
    public static final PreludeBiome DESERT = PreludeBiome.ofLegacy(V1_8, 0x02);
    public static final PreludeBiome EXTREME_HILLS = PreludeBiome.ofLegacy(V1_8, 0x03);
    public static final PreludeBiome FOREST = PreludeBiome.ofLegacy(V1_8, 0x04);
    public static final PreludeBiome TAIGA = PreludeBiome.ofLegacy(V1_8, 0x05);
    public static final PreludeBiome SWAMPLAND = PreludeBiome.ofLegacy(V1_8, 0x06);
    public static final PreludeBiome RIVER = PreludeBiome.ofLegacy(V1_8, 0x07);

    /**+
     * The Nether
     * */
    public static final PreludeBiome HELL = PreludeBiome.ofLegacy(V1_8, 0x08);

    /**+
     * The End
     * */
    public static final PreludeBiome SKY = PreludeBiome.ofLegacy(V1_8, 0x09);
    public static final PreludeBiome FROZEN_OCEAN = PreludeBiome.ofLegacy(V1_8, 0x0A);
    public static final PreludeBiome FROZEN_RIVER = PreludeBiome.ofLegacy(V1_8, 0x0B);

    /**+
     * Ice Plains
     * */
    public static final PreludeBiome ICE_FLATS = PreludeBiome.ofLegacy(V1_8, 0x0C);
    public static final PreludeBiome ICE_MOUNTAINS = PreludeBiome.ofLegacy(V1_8, 0x0D);
    public static final PreludeBiome MUSHROOM_ISLAND = PreludeBiome.ofLegacy(V1_8, 0x0E);
    public static final PreludeBiome MUSHROOM_ISLAND_SHORE = PreludeBiome.ofLegacy(V1_8, 0x0F);

    /**+
     * Beach
     * */
    public static final PreludeBiome BEACHES = PreludeBiome.ofLegacy(V1_8, 0x10);
    public static final PreludeBiome DESERT_HILLS = PreludeBiome.ofLegacy(V1_8, 0x11);
    public static final PreludeBiome FOREST_HILLS = PreludeBiome.ofLegacy(V1_8, 0x12);
    public static final PreludeBiome TAIGA_HILLS = PreludeBiome.ofLegacy(V1_8, 0x13);
    public static final PreludeBiome SMALLER_EXTREME_HILLS = PreludeBiome.ofLegacy(V1_8, 0x14);
    public static final PreludeBiome JUNGLE = PreludeBiome.ofLegacy(V1_8, 0x15);
    public static final PreludeBiome JUNGLE_HILLS = PreludeBiome.ofLegacy(V1_8, 0x16);
    public static final PreludeBiome JUNGLE_EDGE = PreludeBiome.ofLegacy(V1_8, 0x17);
    public static final PreludeBiome DEEP_OCEAN = PreludeBiome.ofLegacy(V1_8, 0x18);
    public static final PreludeBiome STONE_BEACH = PreludeBiome.ofLegacy(V1_8, 0x19);
    public static final PreludeBiome COLD_BEACH = PreludeBiome.ofLegacy(V1_8, 0x1A);
    public static final PreludeBiome BIRCH_FOREST = PreludeBiome.ofLegacy(V1_8, 0x1B);
    public static final PreludeBiome BIRCH_FOREST_HILLS = PreludeBiome.ofLegacy(V1_8, 0x1C);
    public static final PreludeBiome ROOFED_FOREST = PreludeBiome.ofLegacy(V1_8, 0x1D);

    /**+
     * Cold Taiga
     * */
    public static final PreludeBiome TAIGA_COLD = PreludeBiome.ofLegacy(V1_8, 0x1E);

    /**+
     * Cold Taiga Hills
     * */
    public static final PreludeBiome TAIGA_COLD_HILLS = PreludeBiome.ofLegacy(V1_8, 0x1F);

    /**+
     * Mega Taiga
     * */
    public static final PreludeBiome REDWOOD_TAIGA = PreludeBiome.ofLegacy(V1_8, 0x20);

    /**+
     * Mega Taiga Hills
     * */
    public static final PreludeBiome REDWOOD_TAIGA_HILLS = PreludeBiome.ofLegacy(V1_8, 0x21);

    /**+
     * Extreme Hills Plus
     * */
    public static final PreludeBiome EXTREME_HILLS_WITH_TREES = PreludeBiome.ofLegacy(V1_8, 0x22);
    public static final PreludeBiome SAVANNA = PreludeBiome.ofLegacy(V1_8, 0x23);
    public static final PreludeBiome SAVANNA_PLATEAU = PreludeBiome.ofLegacy(V1_8, 0x24);
    public static final PreludeBiome MESA = PreludeBiome.ofLegacy(V1_8, 0x25);

    /**+
     * Mesa Plateau F
     * */
    public static final PreludeBiome MESA_ROCK = PreludeBiome.ofLegacy(V1_8, 0x26);

    /**+
     * Mesa Plateau
     * */
    public static final PreludeBiome MESA_CLEAR_ROCK = PreludeBiome.ofLegacy(V1_8, 0x27);


    public static final PreludeBiome WARM_OCEAN = PreludeBiome.ofModern(V1_13, "warm_ocean");
    /**+
     * Removed in 1.18-21w43a (why??)
     * */
    public static final PreludeBiome DEEP_WARM_OCEAN = PreludeBiome.ofModern(V1_13, "deep_warm_ocean");
    public static final PreludeBiome LUKEWARM_OCEAN = PreludeBiome.ofModern(V1_13, "lukewarm_ocean");
    public static final PreludeBiome DEEP_LUKEWARM_OCEAN = PreludeBiome.ofModern(V1_13, "deep_lukewarm_ocean");
    public static final PreludeBiome COLD_OCEAN = PreludeBiome.ofModern(V1_13, "cold_ocean");
    public static final PreludeBiome DEEP_COLD_OCEAN = PreludeBiome.ofModern(V1_13, "deep_cold_ocean");
    public static final PreludeBiome DEEP_FROZEN_OCEAN = PreludeBiome.ofModern(V1_13, "deep_frozen_ocean");

    public static final PreludeBiome DRIPSTONE_CAVES = PreludeBiome.ofModern(V1_17, "dripstone_caves");
    public static final PreludeBiome LUSH_CAVES = PreludeBiome.ofModern(V1_17, "lush_caves");

    public static final PreludeBiome MEADOW = PreludeBiome.ofModern(V1_18, "meadow");
    public static final PreludeBiome GROVE = PreludeBiome.ofModern(V1_18, "grove");
    public static final PreludeBiome SNOWY_SLOPES = PreludeBiome.ofModern(V1_18, "snowy_slopes");
    public static final PreludeBiome JAGGED_PEAKS = PreludeBiome.ofModern(V1_18, "jagged_peaks");
    public static final PreludeBiome FROZEN_PEAKS = PreludeBiome.ofModern(V1_18, "frozen_peaks");
    public static final PreludeBiome STONY_PEAKS = PreludeBiome.ofModern(V1_18, "stony_peaks");

    public static final PreludeBiome DEEP_DARK = PreludeBiome.ofModern(V1_19, "deep_dark");
    public static final PreludeBiome MANGROVE_SWAMP = PreludeBiome.ofModern(V1_19, "mangove_swamp");

    public static final PreludeBiome CHERRY_GROVE = PreludeBiome.ofModern(V1_20, "cherry_grove");

    // ----------------------------- NETHER BIOMES -----------------------------
    public static final PreludeBiome SOUL_SAND_VALLEY = PreludeBiome.ofModern(V1_16, "soulsand_valley");
    public static final PreludeBiome CRIMSON_FOREST = PreludeBiome.ofModern(V1_16, "crimson_forest");
    public static final PreludeBiome WARPED_FOREST = PreludeBiome.ofModern(V1_16, "warped_forest");
    public static final PreludeBiome BASALT_DELTA = PreludeBiome.ofModern(V1_16, "basalt_deltas");

    // ----------------------------- MUTATED BIOMES -----------------------------
    // these biomes are not in the needed biomes to visit list
    // for that one achievement, but exist and (most) are used

    /**+
     * Not the void, but rather this is a biome only accessible with Superflat Presets
     */
    public static final PreludeBiome THE_VOID = PreludeBiome.ofLegacy(V1_9, 0x7F);

    /**+
     * Sunflower Plains
     * */
    public static final PreludeBiome MUTATED_PLAINS = PreludeBiome.ofLegacy(V1_8, 0x81);

    /**+
     * Unsure, known as "Desert M" pre-1.13
     * */
    public static final PreludeBiome MUTATED_DESERT = PreludeBiome.ofLegacy(V1_8, 0x82);

    /**+
     * Extreme Hills M
     * */
    public static final PreludeBiome MUTATED_EXTREME_HILLS = PreludeBiome.ofLegacy(V1_8, 0x83);

    /**+
     * Flower Forest
     * */
    public static final PreludeBiome MUTATED_FOREST = PreludeBiome.ofLegacy(V1_8, 0x84);

    /**+
     * Taiga M
     * */
    public static final PreludeBiome MUTATED_TAIGA = PreludeBiome.ofLegacy(V1_8, 0x85);

    /**+
     * Swampland M
     * */
    public static final PreludeBiome MUTATED_SWAMPLAND = PreludeBiome.ofLegacy(V1_8, 0x86);

    /**+
     * Ice Plains Spikes
     * */
    public static final PreludeBiome MUTATED_ICE_FLATS = PreludeBiome.ofLegacy(V1_8, 0x8C);

    /**+
     * Jungle M
     * */
    public static final PreludeBiome MUTATED_JUNGLE = PreludeBiome.ofLegacy(V1_8, 0x95);

    /**+
     * Jungle Edge M
     * */
    public static final PreludeBiome MUTATED_JUNGLE_EDGE = PreludeBiome.ofLegacy(V1_8, 0x97);

    /**+
     * Birch Forest M
     * */
    public static final PreludeBiome MUTATED_BIRCH_FOREST = PreludeBiome.ofLegacy(V1_8, 0x9B);

    /**+
     * Birch Forest Hills M
     * */
    public static final PreludeBiome MUTATED_BIRCH_FOREST_HILLS = PreludeBiome.ofLegacy(V1_8, 0x9C);

    /**+
     * Roofed Forest M
     */
    public static final PreludeBiome MUTATED_ROOFED_FOREST = PreludeBiome.ofLegacy(V1_8, 0x9D);

    /**+
     * Cold Taiga M
     * */
    public static final PreludeBiome MUTATED_TAIGA_COLD = PreludeBiome.ofLegacy(V1_8, 0x9E);

    /**+
     * Mega Spruce Taiga
     */
    public static final PreludeBiome MUTATED_REDWOOD_TAIGA = PreludeBiome.ofLegacy(V1_8, 0xA0);

    /**+
     * Redwood Taiga Hills M
     * */
    public static final PreludeBiome MUTATED_REDWOOD_TAIGA_HILLS = PreludeBiome.ofLegacy(V1_8, 0xA1);

    /**+
     * Extreme Hills Plus M
     * */
    public static final PreludeBiome MUTATED_EXTREME_HILLS_WITH_TREES = PreludeBiome.ofLegacy(V1_8, 0xA2);

    /**+
     * Savanna M
     * */
    public static final PreludeBiome MUTATED_SAVANNA = PreludeBiome.ofLegacy(V1_8, 0xA3);

    /**+
     * Savanna Plateau M
     * */
    public static final PreludeBiome MUTATED_SAVANNA_ROCK = PreludeBiome.ofLegacy(V1_8, 0xA4);

    /**+
     * Mesa (Bryce)
     * */
    public static final PreludeBiome MUTATED_MESA = PreludeBiome.ofLegacy(V1_8, 0xA5);

    /**+
     * Mesa Plateau F M
     * */
    public static final PreludeBiome MUTATED_MESA_ROCK = PreludeBiome.ofLegacy(V1_8, 0xA6);

    /**+
     * Mesa Plateau M
     * */
    public static final PreludeBiome MUTATED_MESA_CLEAR_ROCK = PreludeBiome.ofLegacy(V1_8, 0xA6);

    // ----------------------------- OTHER BIOMES -----------------------------
    /**+
     * The main End island (the big one in the middle uk?)
     * */
    public static final PreludeBiome THE_END = PreludeBiome.ofModern(V1_13, "the_end");

    /**+
     * Small End Islands
     * */
    public static final PreludeBiome THE_END_FLOATING_ISLANDS = PreludeBiome.ofModern(V1_13, "small_end_islands");

    /**+
     * End Highlands
     * */
    public static final PreludeBiome THE_END_HIGHLANDS = PreludeBiome.ofModern(V1_13, "end_highlands");

    /**+
     * End Midlands
     * */
    public static final PreludeBiome THE_END_MIDLANDS = PreludeBiome.ofModern(V1_13, "end_midlands");

    /**+
     * End Barrens
     * */
    public static final PreludeBiome THE_END_BARRENS = PreludeBiome.ofModern(V1_13, "end_barrens");
}
