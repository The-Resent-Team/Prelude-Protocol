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

package com.resentclient.prelude.protocol.packets.s2c.play;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.packets.c2s.ClientAcknowledgeServerHandshakePreludeC2SPacket;
import com.resentclient.prelude.protocol.world.PreludeItems;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestSetItemTypePreludeS2CPacket {
    @Test
    public void testSetItemTypePacket() throws IOException {
        SetItemTypePreludeS2CPacket packet = SetItemTypePreludeS2CPacket.builder().slot(19).item(PreludeItems.TOTEM_OF_UNDYING).build();

        byte[] bytes = packet.toBytes();
        try {
            Optional<PreludeS2CPacket> optional = PreludeS2CPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof SetItemTypePreludeS2CPacket)
                Assertions.assertEquals(SetItemTypePreludeS2CPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            SetItemTypePreludeS2CPacket deserialized = (SetItemTypePreludeS2CPacket) optional.get();
            Assertions.assertEquals(packet, deserialized);

            ClientAcknowledgeServerHandshakePreludeC2SPacket invalidPacket = new ClientAcknowledgeServerHandshakePreludeC2SPacket();
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
