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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.packets.c2s.EquipOffhandC2SPacket;
import prelude.protocol.world.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestChunkDataModernS2CPacket {
    @Test
    public void testChunkDataModernS2CPacket() throws IOException {
        PreludeBlockType[][][] blocks = new PreludeBlockType[64][16][16];
        for (int i = 0; i < 64; i++)
            for (int j = 0; j < 16; j++)
                for (int k = 0; k < 16; k++)
                    blocks[i][j][k] = PreludeBlockTypes.RESPAWN_ANCHOR;

        PreludeBiome[][] biomes = new PreludeBiome[16][16];
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
                biomes[i][j] = PreludeBiomes.DEEP_DARK; // fun

        ChunkDataModernS2CPacket packet = ChunkDataModernS2CPacket.builder()
                .chunk(new PreludeChunk(blocks, biomes, new PreludeChunkCoordinate(16, 13), false))
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ChunkDataModernS2CPacket)
                Assertions.assertEquals(ChunkDataModernS2CPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ChunkDataModernS2CPacket deserialized = (ChunkDataModernS2CPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            EquipOffhandC2SPacket invalidPacket = new EquipOffhandC2SPacket();
            try {
                invalidPacket.loadData(new ByteArrayInputStream(bytes));
                Assertions.fail("Somehow parsed invalid packet!");
            } catch (Exception e) {
                Assertions.assertInstanceOf(InvalidPacketException.class, e);
            }
        } catch (Exception e) {
            // erm what the
            Assertions.fail(e);
        }
    }
}
