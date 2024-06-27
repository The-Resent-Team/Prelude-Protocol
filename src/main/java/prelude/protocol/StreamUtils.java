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

public class StreamUtils {
    public static void writeShort(int i, OutputStream os) throws IOException {
        os.write(i >> 8 & 255);
        os.write(i & 255);
    }

    // ---------------- BEGIN COPY PASTING VARINT AND VARLONG ----------------

    /*
    * Copied from https://wiki.vg/index.php?title=Protocol#VarInt_and_VarLong
    * */
    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static void writeVarInt(int value, OutputStream os) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                os.write(value);
                return;
            }

            os.write((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
        }
    }

    public static void writeVarLong(long value, OutputStream os) throws IOException {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                os.write((int) value);
                return;
            }

            os.write((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));

            value >>>= 7;
        }
    }

    public static int readVarInt(InputStream is) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = (byte) is.read();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static long readVarLong(InputStream is) throws IOException {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = (byte) is.read();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }

        return value;
    }

    // ---------------- END COPY PASTE VARINT AND VARLONG ----------------

    public static int readShort(InputStream is) throws IOException {
        return (is.read() << 8) | is.read();
    }

    public static String readASCII(int len, InputStream bytesIn) throws IOException {
        char[] charIn = new char[len];
        for(int i = 0; i < len; ++i) {
            charIn[i] = (char)(bytesIn.read() & 0xFF);
        }
        return new String(charIn);
    }
}
