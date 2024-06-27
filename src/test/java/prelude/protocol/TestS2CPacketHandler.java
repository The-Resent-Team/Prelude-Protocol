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

import prelude.protocol.packets.s2c.*;

public class TestS2CPacketHandler extends S2CPacketHandler {
    public void handleServerHandshake(ServerHandshakePacket packet) {
        System.out.println("Handshake received");
    }

    public void handleModStatus(ModStatusPacket packet) {
        System.out.println("Mod status received");
    }

    public void handleRespawnAnchorUpdate(RespawnAnchorUpdatePacket packet) {
        System.out.println("Respawn anchor update received");
    }

    public void handleServerTps(ServerTpsPacket packet) {
        System.out.println("Server tps received");
    }

    public void handleOffhandUpdate(UpdateOffhandPacket packet) {
        System.out.println("Update offhand received");
    }

    public void handleTotemUsed(TotemUsedPacket packet) {
        System.out.println("Totem used received");
    }

    public void handleWaypoints(WaypointsPacket packet) {
        System.out.println("Waypoints received");
    }
}
