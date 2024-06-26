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

package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.TestS2CPacketHandler;
import prelude.protocol.packets.c2s.EquipOffhandPacket;
import prelude.protocol.packets.s2c.RespawnAnchorUpdatePacket;
import prelude.protocol.packets.s2c.UpdateOffhandPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestUpdateOffhandPacket {
    @Test
    public void testUpdateOffhandPacket() throws IOException {
        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        UpdateOffhandPacket packet = UpdateOffhandPacket.builder()
                .canClientDisregardThis(false)
                .serializedItem("ItemStack{DISPENSER x 35}")
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof UpdateOffhandPacket)
                Assertions.assertEquals(UpdateOffhandPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            UpdateOffhandPacket deserialized = (UpdateOffhandPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            EquipOffhandPacket invalidPacket = new EquipOffhandPacket();
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
