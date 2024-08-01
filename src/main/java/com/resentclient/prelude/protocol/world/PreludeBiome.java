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
import com.resentclient.prelude.protocol.Version;
import com.resentclient.prelude.protocol.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PreludeBiome implements WriteableObject {
    public final Version version;
    public final String biomeName;
    public final int biomeId;

    public static PreludeBiome ofModern(Version version, String biomeName) {
        return new PreludeBiome(version, biomeName, -999);
    }

    public static PreludeBiome ofLegacy(Version version, int biomeId) {
        return new PreludeBiome(version, "", biomeId);
    }

    private PreludeBiome(Version version, String biomeName, int biomeId) {
        this.version = version;
        this.biomeName = biomeName;
        this.biomeId = biomeId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PreludeBiome)) return false;
        PreludeBiome that = (PreludeBiome) object;
        if (version.usesStringIds != that.version.usesStringIds) return false;
        if (version.usesStringIds)
            return Objects.equals(biomeName, that.biomeName);
        else return biomeId == that.biomeId;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        version.write(out);

        if (version.usesStringIds) {
            out.write(biomeName.length());
            out.write(biomeName.getBytes(StandardCharsets.US_ASCII));
        } else {
            out.write(biomeId);
        }
    }

    public static PreludeBiome deserialize(InputStream is) throws IOException {
        Version version = Version.deserialize(is);

        if (version.usesStringIds) {
            String biomeName = StreamUtils.readASCII(is.read(), is);
            return ofModern(version, biomeName);
        } else {
            return ofLegacy(version, is.read());
        }
    }
}
