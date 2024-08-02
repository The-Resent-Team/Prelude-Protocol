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

package com.resentclient.prelude.protocol.packets.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import com.resentclient.prelude.protocol.PreludeC2SPacket;
import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.packets.s2c.ServerSyncRequestPreludeS2CPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Testable
public class TestClientAcknowledgeServerHandshakePreludeC2SPacket {
    @Test
    public void testClientAcknowledgeServerHandshakePacket() throws IOException {
        Assertions.assertEquals(1, new ClientAcknowledgeServerHandshakePreludeC2SPacket().toBytes()[0]);

        byte[] bytes = { 1 };
        try {
            Optional<PreludeC2SPacket> optional = PreludeC2SPacket.parsePacket(bytes);

            if (!optional.isPresent())
                Assertions.fail("Failed to parse packet");

            if (optional.get() instanceof ClientAcknowledgeServerHandshakePreludeC2SPacket)
                Assertions.assertEquals(ClientAcknowledgeServerHandshakePreludeC2SPacket.class, optional.get().getClass());
            else Assertions.fail("Parsing didn't return correct packet type!");

            ClientAcknowledgeServerHandshakePreludeC2SPacket deserialized = (ClientAcknowledgeServerHandshakePreludeC2SPacket) optional.get();
            Assertions.assertArrayEquals(bytes, deserialized.toBytes());

            ServerSyncRequestPreludeS2CPacket invalidPacket = new ServerSyncRequestPreludeS2CPacket();
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
