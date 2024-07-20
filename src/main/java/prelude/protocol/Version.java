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

package prelude.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public enum Version implements WriteableObject {
    UNKNOWN (false,  false,  false,  false),
    V1_8    (false,  false,  false,  false),
    V1_9    (true,   false,  false,  false),
    V1_10   (true,   false,  false,  false),
    V1_11   (true,   true,   false,  false),
    V1_12   (true,   true,   false,  false),
    V1_13   (true,   true,   true,   false),
    V1_14   (true,   true,   true,   false),
    V1_15   (true,   true,   true,   false),
    V1_16   (true,   true,   true,   false),
    V1_17   (true,   true,   true,   false),
    V1_18   (true,   true,   true,   true),
    V1_19   (true,   true,   true,   true),
    V1_20   (true,   true,   true,   true),
    V1_21   (true,   true,   true,   true);

    public static final int OFFHAND_BITS = 0b0001;
    public static final int OFFHAND_MASK = 0b0001;
    public static final int TOTEM_BITS = 0b0010;
    public static final int TOTEM_MASK = 0b0010;
    public static final int STRING_ID_SHIFT = 0b0100;
    public static final int STRING_ID_MASK = 0b0100;
    public static final int UNDERGROUND_TERRAIN_SHIFT = 0b1000;
    public static final int UNDERGROUND_TERRAIN_MASK = 0b1000;

    public final boolean hasOffhand;
    public final boolean hasTotems;
    public final boolean usesStringIds;
    public final boolean hasUndergroundTerrain;

    public final int serializedForm;

    Version(boolean hasOffhand, boolean hasTotems, boolean usesStringIds, boolean hasUndergroundTerrain) {
        this.hasOffhand = hasOffhand;
        this.hasTotems = hasTotems;
        this.usesStringIds = usesStringIds;
        this.hasUndergroundTerrain = hasUndergroundTerrain;
        this.serializedForm = serialize();
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(serialize());
    }

    public static Version deserialize(InputStream is) throws IOException {
        int serializedForm = is.read();
        for (Version version : Version.values())
            if (serializedForm == version.serializedForm)
                return version;

        return UNKNOWN;
    }

    public int serialize() {
        int output = 0;
        output += hasOffhand ? OFFHAND_BITS : 0;
        output += hasTotems ? TOTEM_BITS : 0;
        output += usesStringIds ? STRING_ID_SHIFT : 0;
        output += hasUndergroundTerrain ? UNDERGROUND_TERRAIN_SHIFT : 0;
        return output;
    }
}
