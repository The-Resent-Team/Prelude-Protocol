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

package prelude.protocol.packets.c2s;

import prelude.protocol.C2SPacket;
import prelude.protocol.C2SPacketHandler;
import prelude.protocol.InvalidPacketException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InteractWithOffhandC2SPacket extends C2SPacket {
    private InteractType interactType;

    public InteractWithOffhandC2SPacket() {}

    private InteractWithOffhandC2SPacket(InteractType interactType) {
        this.interactType = interactType;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);
        bao.write(interactType.value);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof InteractWithOffhandC2SPacket)) return false;
        InteractWithOffhandC2SPacket that = (InteractWithOffhandC2SPacket) object;
        return interactType == that.interactType;
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("INTERACT_WITH_OFFHAND_ID", is);

            this.interactType = InteractType.of(is.read());
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse CLIENT_SYNC_RESPONSE_PACKET!", e);
        }
    }

    @Override
    public void processSelf(C2SPacketHandler handler) {

    }

    public enum InteractType {
        HOLD_INTERACT(0),
        BEGIN_INTERACT(1),
        END_INTERACT(2);

        public final int value;
        InteractType(int value) {
            this.value = value;
        }

        public static InteractType of(int value) {
            for (InteractType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }
}
