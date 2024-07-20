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

package prelude.protocol;

import prelude.protocol.packets.c2s.ClientHandshakeC2SPacket;
import prelude.protocol.packets.c2s.ClientSyncResponseC2SPacket;
import prelude.protocol.packets.c2s.EquipOffhandC2SPacket;
import prelude.protocol.packets.c2s.interactions.AttemptPlaceInLegacyIllegalSpotsC2SPacket;
import prelude.protocol.packets.c2s.interactions.InteractWithOffhandC2SPacket;

public class TestC2SPacketHandler implements C2SPacketHandler {
    @Override
    public void handleClientHandshake(ClientHandshakeC2SPacket packet) {
        System.out.println("Client handshake received");
    }

    @Override
    public void handleEquipOffhand(EquipOffhandC2SPacket packet) {
        System.out.println("Equip offhand received");
    }

    @Override
    public void handleClientSyncResponse(ClientSyncResponseC2SPacket packet) {
        System.out.println("Client sync response received");
    }

    @Override
    public void handleInteractWithOffhand(InteractWithOffhandC2SPacket packet) {
        System.out.println("Interact with offhand received");
    }

    @Override
    public void handleAttemptPlaceInLegacyIllegalSpots(AttemptPlaceInLegacyIllegalSpotsC2SPacket packet) {
        System.out.println("Attempt place in legacy illegal spots received");
    }
}
