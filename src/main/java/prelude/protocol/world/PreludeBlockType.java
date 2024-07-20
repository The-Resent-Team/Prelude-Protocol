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
import prelude.protocol.Version;
import prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PreludeBlockType implements WriteableObject {
    public final Version version;
    public final String blockName;
    public final int blockId;
    public final PreludeBlockStatePreFlattening state;

    public static PreludeBlockType ofModern(Version version, String blockName) {
        return new PreludeBlockType(version, blockName, -999, null);
    }

    public static PreludeBlockType ofLegacy(Version version, int blockId, PreludeBlockStatePreFlattening state) {
        return new PreludeBlockType(version, "", blockId, state);
    }

    private PreludeBlockType(Version version, String blockName, int blockId, PreludeBlockStatePreFlattening state) {
        this.version = version;
        this.blockName = blockName;
        this.blockId = blockId;
        this.state = state;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        version.write(out);

        if (version.usesStringIds) {
            // we can use new formatting
            out.write(blockName.length());
            out.write(blockName.getBytes(StandardCharsets.US_ASCII));
        } else {
            // we must use old formatting, client supports parsing both types
            out.write(blockId);
            state.write(out);
        }
    }

    public static PreludeBlockType deserialize(InputStream is) throws IOException {
        Version ver = Version.deserialize(is);
        if (ver.usesStringIds) {
            return new PreludeBlockType(ver, StreamUtils.readASCII(is.read(), is), -999, null);
        } else {
            return new PreludeBlockType(ver, null, is.read(), PreludeBlockStatePreFlattening.deserialize(is));
        }
    }
}
