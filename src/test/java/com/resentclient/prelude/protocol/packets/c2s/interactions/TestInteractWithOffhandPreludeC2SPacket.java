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

package com.resentclient.prelude.protocol.packets.c2s.interactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import com.resentclient.prelude.protocol.PreludeC2SPacket;
import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.packets.c2s.EquipOffhandPreludeC2SPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestInteractWithOffhandPreludeC2SPacket {
    @Test
    public void testInteractWithOffhandC2SPacket() throws IOException {
        InteractWithOffhandPreludeC2SPacket packet = InteractWithOffhandPreludeC2SPacket.builder()
                .interactType(InteractWithOffhandPreludeC2SPacket.InteractType.BEGIN_INTERACT)
                .build();

        byte[] bytes = packet.toBytes();

        try {
            Optional<PreludeC2SPacket> optional = PreludeC2SPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof InteractWithOffhandPreludeC2SPacket)
                Assertions.assertEquals(InteractWithOffhandPreludeC2SPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            InteractWithOffhandPreludeC2SPacket deserialized = (InteractWithOffhandPreludeC2SPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            EquipOffhandPreludeC2SPacket invalidPacket = new EquipOffhandPreludeC2SPacket();
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
