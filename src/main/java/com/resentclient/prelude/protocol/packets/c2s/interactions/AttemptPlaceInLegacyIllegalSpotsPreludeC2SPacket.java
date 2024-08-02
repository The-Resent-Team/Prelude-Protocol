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

package com.resentclient.prelude.protocol.packets.c2s.interactions;

import com.resentclient.prelude.protocol.PreludeC2SPacket;
import com.resentclient.prelude.protocol.PreludeC2SPacketHandler;
import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.world.PreludeChunkCoordinate;
import com.resentclient.prelude.protocol.world.PreludeChunkType;
import com.resentclient.prelude.protocol.world.PreludeCompactCoordinate;
import com.resentclient.prelude.protocol.world.PreludeRelativeCoordinate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket extends PreludeC2SPacket {
    private PreludeCompactCoordinate compactCoordinate;

    public AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket() {}

    private AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket(PreludeCompactCoordinate compactCoordinate) {
        this.compactCoordinate = compactCoordinate;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(packetId);
        compactCoordinate.write(bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket)) return false;
        AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket that = (AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket) object;
        return Objects.equals(compactCoordinate, that.compactCoordinate);
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("ATTEMPT_PLACE_IN_LEGACY_ILLEGAL_SPOTS_ID", is);

            this.compactCoordinate = PreludeCompactCoordinate.deserialize(is);
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse ATTEMPT_PLACE_IN_LEGACY_ILLEGAL_SPOTS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeC2SPacketHandler handler) {
        handler.handleAttemptPlaceInLegacyIllegalSpots(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PreludeChunkType chunkType;
        private int chunkX;
        private int chunkZ;
        private int attemptedXPosRelativeToChunk;
        private int attemptedYPosRelativeToChunkToLegacyBuildLimit;
        private int attemptedZPosRelativeToChunk;

        public Builder chunkType(PreludeChunkType chunkType) {
            this.chunkType = chunkType;
            return this;
        }

        public Builder chunkX(int chunkX) {
            this.chunkX = chunkX;
            return this;
        }

        public Builder chunkZ(int chunkZ) {
            this.chunkZ = chunkZ;
            return this;
        }

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

        public AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket build() {
            return new AttemptPlaceInLegacyIllegalSpotsPreludeC2SPacket(new PreludeCompactCoordinate(chunkType, new PreludeChunkCoordinate(chunkX, chunkZ), new PreludeRelativeCoordinate(attemptedXPosRelativeToChunk, attemptedYPosRelativeToChunkToLegacyBuildLimit, attemptedZPosRelativeToChunk)));
        }
    }

    public PreludeCompactCoordinate getCompactCoordinate() {
        return compactCoordinate;
    }
}