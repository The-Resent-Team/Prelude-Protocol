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

package com.resentclient.prelude.protocol.world;

import com.resentclient.prelude.protocol.StreamUtils;
import com.resentclient.common.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PreludeRelativeCoordinate implements WriteableObject {
    private static final int X_MASK = 0b11111;
    private static final int Z_MASK = 0b11111;
    private static final int Y_MASK = 0b1111111;
    private static final int X_SHIFT = 0;
    private static final int Z_SHIFT = 5;
    private static final int Y_SHIFT = 10;

    public final int relativePosX;
    public final int relativePosY; // abs value, direction is determined by a ChunkType
    public final int relativePosZ;

    public PreludeRelativeCoordinate(int relativePosX, int relativePosY, int relativePosZ) {
        if (relativePosX < 0 || relativePosX > 16)
            throw new IllegalArgumentException("Relative Pos X is invalid! Must be between 0-16!");
        if (relativePosY < 0 || relativePosY > 64)
            throw new IllegalArgumentException("Relative Pos Y is invalid! Must be between 0-64!");
        if (relativePosZ < 0 || relativePosZ > 16)
            throw new IllegalArgumentException("Relative Pos Z is invalid! Must be between 0-16!");

        this.relativePosX = relativePosX;
        this.relativePosY = relativePosY;
        this.relativePosZ = relativePosZ;
    }

    public static PreludeRelativeCoordinate deserialize(InputStream is) throws IOException {
        int serialized = StreamUtils.readVarInt(is);

        int relativePosX = (serialized >> X_SHIFT) & X_MASK;
        int relativePosY = (serialized >> Y_SHIFT) & Y_MASK;
        int relativePosZ = (serialized >> Z_SHIFT) & Z_MASK;

        return new PreludeRelativeCoordinate(relativePosX, relativePosY, relativePosZ);
    }

    @Override
    public void write(OutputStream out) throws IOException {
        int output = 0;
        output += (relativePosX & X_MASK) << X_SHIFT;
        output += (relativePosY & Y_MASK) << Y_SHIFT;
        output += (relativePosZ & Z_MASK) << Z_SHIFT;

        StreamUtils.writeVarInt(output, out);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PreludeRelativeCoordinate)) return false;
        PreludeRelativeCoordinate that = (PreludeRelativeCoordinate) object;
        return relativePosX == that.relativePosX && relativePosY == that.relativePosY && relativePosZ == that.relativePosZ;
    }
}
