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
import prelude.protocol.packets.s2c.play.ServerTpsS2CPacket;
import prelude.protocol.packets.s2c.play.TotemUsedS2CPacket;
import prelude.protocol.packets.s2c.play.UpdateOffhandS2CPacket;
import prelude.protocol.packets.s2c.world.ChunkDataUnderYZeroS2CPacket;

/*
* Extended by ResentS2CPacketHandler on the client
* */
public interface S2CPacketHandler {
    void handleServerHandshake(ServerHandshakeS2CPacket packet);

    void handleModStatus(ModStatusS2CPacket packet);

    void handleServerTps(ServerTpsS2CPacket packet);

    void handleOffhandUpdate(UpdateOffhandS2CPacket packet);

    void handleTotemUsed(TotemUsedS2CPacket packet);

    void handleWaypoints(SetWaypointsS2CPacket packet);

    void handleSyncRequest(ServerSyncRequestS2CPacket packet);

    void handleChunkDataUnderYZero(ChunkDataUnderYZeroS2CPacket packet);
}
