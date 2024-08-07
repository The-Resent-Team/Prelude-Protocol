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

import java.io.IOException;
import java.io.InputStream;

public abstract class PreludePacket<E> {
    protected int packetId;

    public abstract byte[] toBytes() throws IOException;
    public abstract boolean equals(Object packet);
    public abstract void loadData(InputStream is) throws InvalidPreludePacketException;
    public abstract void processSelf(E handler);

    public int getPacketId() {
        return packetId;
    }

    protected void validateOrThrow(String packetIdAsString, InputStream is) throws IOException, InvalidPreludePacketException {
        if (is.read() != packetId)
            throw new InvalidPreludePacketException("Packet ID doesn't match with %packet_id_as_string% (%id%)!"
                    .replace("%id%", packetId + "")
                    .replace("%packet_id_as_string%", packetIdAsString));
    }
}
