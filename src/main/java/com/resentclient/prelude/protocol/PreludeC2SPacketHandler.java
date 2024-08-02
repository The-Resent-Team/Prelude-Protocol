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

import com.resentclient.prelude.protocol.packets.c2s.*;
import com.resentclient.prelude.protocol.packets.c2s.interactions.*;

/*
* Extended by BukkitC2SPacketHandler on the plugin
* */
public interface PreludeC2SPacketHandler {
    void handleClientHandshake(ClientHandshakePreludeC2SPacket packet);

    void handleClientAcknowledgeServerHandshake(ClientAcknowledgeServerHandshakePreludeC2SPacket packet);

    void handleEquipOffhand(EquipOffhandPreludeC2SPacket packet);

    void handleClientSyncResponse(ClientSyncResponsePreludeC2SPacket packet);

    void handleInteractWithOffhand(InteractWithOffhandPreludeC2SPacket packet);

    void handleAttemptPlaceInLegacyIllegalSpots(AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket packet);
}
