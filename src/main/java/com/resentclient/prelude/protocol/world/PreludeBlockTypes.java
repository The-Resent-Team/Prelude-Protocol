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

import com.resentclient.prelude.protocol.Version;

public class PreludeBlockTypes {
    public static final PreludeBlockType VOID_AIR = PreludeBlockType.ofModern(Version.V1_13, "void_air");
    public static final PreludeBlockType RESPAWN_ANCHOR = PreludeBlockType.ofModern(Version.V1_13, "respawn_anchor");
}
