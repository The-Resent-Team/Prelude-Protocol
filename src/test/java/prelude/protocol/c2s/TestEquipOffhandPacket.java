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
import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestEquipOffhandPacket {
    @Test
    public void testEquipOffhandPacket() throws IOException {
        C2SPacket.trySetHandler(new TestC2SPacketHandler());

        EquipOffhandPacket packet = EquipOffhandPacket.builder()
                .slot((short) 25)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<C2SPacket> optional = C2SPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof EquipOffhandPacket)
                Assertions.assertEquals(EquipOffhandPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            EquipOffhandPacket deserialized = (EquipOffhandPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            ClientHandshakePacket invalidPacket = new ClientHandshakePacket();
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
