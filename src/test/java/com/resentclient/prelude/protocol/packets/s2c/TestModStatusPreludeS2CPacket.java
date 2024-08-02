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

package com.resentclient.prelude.protocol.packets.s2c;

import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import com.resentclient.prelude.protocol.packets.c2s.ClientHandshakePreludeC2SPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestModStatusPreludeS2CPacket {
    @Test
    public void testModStatusPacket() throws IOException {
        ModStatusPreludeS2CPacket packet = ModStatusPreludeS2CPacket.builder()
                .modIdentifier("offhand")
                .modStatus(ModStatusPreludeS2CPacket.ModStatus.SUPPORTED)
                .build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<PreludeS2CPacket> optional = PreludeS2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ModStatusPreludeS2CPacket)
                Assertions.assertEquals(ModStatusPreludeS2CPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ModStatusPreludeS2CPacket deserialized = (ModStatusPreludeS2CPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            ClientHandshakePreludeC2SPacket invalidPacket = new ClientHandshakePreludeC2SPacket();
            try {
                invalidPacket.loadData(new ByteArrayInputStream(bytes));
                Assertions.fail("Somehow parsed invalid packet!");
            } catch (Exception e) {
                Assertions.assertInstanceOf(InvalidPreludePacketException.class, e);
            }
        } catch (Exception e) {
            // erm what the
            Assertions.fail(e);
        }
    }
}
