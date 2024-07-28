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
import java.util.Objects;

public class PreludeItem implements WriteableObject {
    public final Version version;
    public final String id;
    public final int preFlatteningId;

    public PreludeItem(Version version, String id, int preFlatteningId) {
        this.version = version;
        this.id = id;
        this.preFlatteningId = preFlatteningId;
    }

    public static PreludeItem ofLegacy(Version version, int id) {
        return new PreludeItem(version, "", id);
    }

    public static PreludeItem ofModern(Version version, String id) {
        return new PreludeItem(version, id, -999);
    }

    @Override
    public void write(OutputStream out) throws IOException {
        version.write(out);

        if (version.usesStringIds) {
            // we can use new formatting
            out.write(id.length());
            out.write(id.getBytes(StandardCharsets.US_ASCII));
        } else {
            StreamUtils.writeShort(preFlatteningId, out);
        }
    }

    public static PreludeItem deserialize(InputStream is) throws IOException {
        Version ver = Version.deserialize(is);
        if (ver.usesStringIds) {
            String id = StreamUtils.readASCII(StreamUtils.readShort(is), is);
            return ofModern(ver, id);
        } else {
            int id = StreamUtils.readShort(is);
            return ofLegacy(ver, id);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PreludeItem)) return false;
        PreludeItem that = (PreludeItem) object;
        return preFlatteningId == that.preFlatteningId && version == that.version && Objects.equals(id, that.id);
    }
}
