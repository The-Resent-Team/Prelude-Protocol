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

package prelude.protocol.world;

import prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class PreludeCompactCoordinate implements WriteableObject {
    public final PreludeChunkType chunkType;
    public final PreludeChunkCoordinate chunkCoordinate;
    public final PreludeRelativeCoordinate relativeCoordinate;

    public PreludeCompactCoordinate(PreludeChunkType chunkType, PreludeChunkCoordinate chunkCoordinate, PreludeRelativeCoordinate relativeCoordinate) {
        this.chunkType = chunkType;
        this.chunkCoordinate = chunkCoordinate;
        this.relativeCoordinate = relativeCoordinate;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        chunkType.write(out);
        chunkCoordinate.write(out);
        relativeCoordinate.write(out);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PreludeCompactCoordinate)) return false;
        PreludeCompactCoordinate that = (PreludeCompactCoordinate) object;
        return Objects.equals(chunkCoordinate, that.chunkCoordinate) && chunkType == that.chunkType && Objects.equals(relativeCoordinate, that.relativeCoordinate);
    }

    public static PreludeCompactCoordinate deserialize(InputStream is) throws IOException {
        PreludeChunkType chunkType = PreludeChunkType.deserialize(is);
        PreludeChunkCoordinate chunkCoordinate = PreludeChunkCoordinate.deserialize(is);
        PreludeRelativeCoordinate relativeCoordinate = PreludeRelativeCoordinate.deserialize(is);

        return new PreludeCompactCoordinate(chunkType, chunkCoordinate, relativeCoordinate);
    }
}
