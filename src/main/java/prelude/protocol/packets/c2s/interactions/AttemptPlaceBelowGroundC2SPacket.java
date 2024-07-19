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

package prelude.protocol.packets.c2s.interactions;

import prelude.protocol.C2SPacket;
import prelude.protocol.C2SPacketHandler;
import prelude.protocol.InvalidPacketException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AttemptPlaceBelowGroundC2SPacket extends C2SPacket {
    public int attemptedXPosRelativeToChunk;
    public int attemptedYPosRelativeToChunkToLegacyBuildLimit;
    public int attemptedZPosRelativeToChunk;

    public AttemptPlaceBelowGroundC2SPacket() {}

    private AttemptPlaceBelowGroundC2SPacket(int attemptedXPosRelativeToChunk, int attemptedYPosRelativeToChunkToLegacyBuildLimit, int attemptedZPosRelativeToChunk) {
        this.attemptedXPosRelativeToChunk = attemptedXPosRelativeToChunk;
        this.attemptedYPosRelativeToChunkToLegacyBuildLimit = attemptedYPosRelativeToChunkToLegacyBuildLimit;
        this.attemptedZPosRelativeToChunk = attemptedZPosRelativeToChunk;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);
        bao.write(attemptedXPosRelativeToChunk);
        bao.write(attemptedYPosRelativeToChunkToLegacyBuildLimit);
        bao.write(attemptedZPosRelativeToChunk);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AttemptPlaceAboveGroundC2SPacket)) return false;
        AttemptPlaceAboveGroundC2SPacket that = (AttemptPlaceAboveGroundC2SPacket) object;
        return attemptedXPosRelativeToChunk == that.attemptedXPosRelativeToChunk && attemptedYPosRelativeToChunkToLegacyBuildLimit == that.attemptedYPosRelativeToChunkToLegacyBuildLimit && attemptedZPosRelativeToChunk == that.attemptedZPosRelativeToChunk;
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            this.validateOrThrow("ATTEMPT_PLACE_BELOW_GROUND_ID", is);

            this.attemptedXPosRelativeToChunk = is.read();
            this.attemptedYPosRelativeToChunkToLegacyBuildLimit = is.read();
            this.attemptedZPosRelativeToChunk = is.read();
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse ATTEMPT_PLACE_BELOW_GROUND_PACKET!", e);
        }
    }

    @Override
    public void processSelf(C2SPacketHandler handler) {
        handler.handleAttemptPlaceBelowGround(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int attemptedXPosRelativeToChunk;
        private int attemptedYPosRelativeToChunkToLegacyBuildLimit;
        private int attemptedZPosRelativeToChunk;

        public Builder attemptedXPosRelativeToChunk(int attemptedXPosRelativeToChunk) {
            this.attemptedXPosRelativeToChunk = attemptedXPosRelativeToChunk;
            return this;
        }

        public Builder attemptedYPosRelativeToChunk(int attemptedYPosRelativeToChunkToLegacyBuildLimit) {
            this.attemptedYPosRelativeToChunkToLegacyBuildLimit = attemptedYPosRelativeToChunkToLegacyBuildLimit;
            return this;
        }

        public Builder attemptedZPosRelativeToChunk(int attemptedZPosRelativeToChunk) {
            this.attemptedZPosRelativeToChunk = attemptedZPosRelativeToChunk;
            return this;
        }

        public AttemptPlaceBelowGroundC2SPacket build() {
            return new AttemptPlaceBelowGroundC2SPacket(attemptedXPosRelativeToChunk, attemptedYPosRelativeToChunkToLegacyBuildLimit, attemptedZPosRelativeToChunk);
        }
    }
}
