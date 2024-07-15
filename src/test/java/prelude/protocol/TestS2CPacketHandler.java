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
    public void handleServerHandshake(ServerHandshakeS2CPacket packet) {
        System.out.println("Handshake received");
    }

    public void handleModStatus(ModStatusS2CPacket packet) {
        System.out.println("Mod status received");
    }

    public void handleServerTps(ServerTpsS2CPacket packet) {
        System.out.println("Server tps received");
    }

    public void handleOffhandUpdate(UpdateOffhandS2CPacket packet) {
        System.out.println("Update offhand received");
    }

    public void handleTotemUsed(TotemUsedS2CPacket packet) {
        System.out.println("Totem used received");
    }

    public void handleWaypoints(SetWaypointsS2CPacket packet) {
        System.out.println("Waypoints received");
    }
}
