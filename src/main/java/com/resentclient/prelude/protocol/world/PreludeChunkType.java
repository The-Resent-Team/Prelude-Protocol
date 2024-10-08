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

import com.resentclient.common.WriteableObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public enum PreludeChunkType implements WriteableObject {
    BELOW_Y0(0x00),
    ABOVE_Y255(0x01);

    public final int value;

    PreludeChunkType(int value) {
        this.value = value;
    }

    private static PreludeChunkType of(int value) {
        for (PreludeChunkType type : PreludeChunkType.values()) {
            if (type.value == value) {
                return type;
            }
        }

        return null;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(value);
    }

    public static PreludeChunkType deserialize(InputStream in) throws IOException {
        return of(in.read());
    }
}
