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

package prelude.protocol.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.C2SPacket;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.TestC2SPacketHandler;
import prelude.protocol.packets.c2s.ClientHandshakeC2SPacket;
import prelude.protocol.packets.c2s.EquipOffhandC2SPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestEquipOffhandC2SPacket {
    @Test
    public void testEquipOffhandPacket() throws IOException {
        C2SPacket.trySetHandler(new TestC2SPacketHandler());

        EquipOffhandC2SPacket packet = EquipOffhandC2SPacket.builder()
                .slot((short) 25)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<C2SPacket> optional = C2SPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof EquipOffhandC2SPacket)
                Assertions.assertEquals(EquipOffhandC2SPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            EquipOffhandC2SPacket deserialized = (EquipOffhandC2SPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            ClientHandshakeC2SPacket invalidPacket = new ClientHandshakeC2SPacket();
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

        Assertions.assertEquals(packet.getPacketId(), 1);
    }
}
