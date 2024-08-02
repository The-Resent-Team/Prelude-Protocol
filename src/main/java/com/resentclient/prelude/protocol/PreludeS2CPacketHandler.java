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

package com.resentclient.prelude.protocol;

import com.resentclient.prelude.protocol.packets.s2c.*;
import com.resentclient.prelude.protocol.packets.s2c.play.*;
import com.resentclient.prelude.protocol.packets.s2c.world.*;

/*
* Extended by ResentS2CPacketHandler on the client
* */
public interface PreludeS2CPacketHandler {
    void handleServerHandshake(ServerHandshakePreludeS2CPacket packet);

    void handleModStatus(ModStatusPreludeS2CPacket packet);

    void handleServerTps(ServerTpsPreludeS2CPacket packet);

    void handleOffhandUpdate(UpdateOffhandPreludeS2CPacket packet);

    void handleTotemUsed(TotemUsedPreludeS2CPacket packet);

    void handleWaypoints(SetWaypointsPreludeS2CPacket packet);

    void handleSyncRequest(ServerSyncRequestPreludeS2CPacket packet);

    void handleChunkDataModern(ChunkDataModernPreludeS2CPacket packet);

    void handleBlockChangeModern(BlockChangeModernPreludeS2CPacket packet);

    void handleMultiBlockChangeModern(MultiBlockChangeModernPreludeS2CPacket packet);

    void handleSetItemType(SetItemTypePreludeS2CPacket packet);
}
