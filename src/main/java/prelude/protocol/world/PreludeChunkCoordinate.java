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

import prelude.protocol.StreamUtils;
import prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PreludeChunkCoordinate implements WriteableObject {
    public final int chunkX;
    public final int chunkZ;

    public PreludeChunkCoordinate(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        StreamUtils.writeVarInt(chunkX, out);
        StreamUtils.writeVarInt(chunkZ, out);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PreludeChunkCoordinate)) return false;
        PreludeChunkCoordinate that = (PreludeChunkCoordinate) object;
        return chunkX == that.chunkX && chunkZ == that.chunkZ;
    }

    public static PreludeChunkCoordinate deserialize(InputStream in) throws IOException {
        int chunkX = StreamUtils.readVarInt(in);
        int chunkZ = StreamUtils.readVarInt(in);

        return new PreludeChunkCoordinate(chunkX, chunkZ);
    }
}
