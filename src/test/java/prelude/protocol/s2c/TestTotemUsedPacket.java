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
import prelude.protocol.packets.s2c.ServerTpsPacket;
import prelude.protocol.packets.s2c.TotemUsedPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static prelude.protocol.S2CPacket.TOTEM_USED_ID;

@Testable
public class TestTotemUsedPacket {
    @Test
    public void testTotemUsedPacket() throws IOException {
        Assertions.assertEquals(TOTEM_USED_ID, new TotemUsedPacket().toBytes()[0]);

        S2CPacket.trySetHandler(new TestS2CPacketHandler());

        byte[] bytes = { TOTEM_USED_ID };
        try {
            Optional<S2CPacket> optional = S2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof TotemUsedPacket)
                Assertions.assertEquals(TotemUsedPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            TotemUsedPacket deserialized = (TotemUsedPacket) optional.get();
            Assertions.assertArrayEquals(bytes, deserialized.toBytes());

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
