package prelude.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
    public static void writeShort(int i, OutputStream os) throws IOException {
        os.write(i >> 8 & 255);
        os.write(i & 255);
    }

    public static void writeInt(int i, OutputStream os) throws IOException {
        os.write(i >> 24 & 255);
        os.write(i >> 16 & 255);
        os.write(i >> 8 & 255);
        os.write(i & 255);
    }

    public static void writeLong(long i, OutputStream os) throws IOException {
        os.write((int)(i >> 56 & 255L));
        os.write((int)(i >> 48 & 255L));
        os.write((int)(i >> 40 & 255L));
        os.write((int)(i >> 32 & 255L));
        os.write((int)(i >> 24 & 255L));
        os.write((int)(i >> 16 & 255L));
        os.write((int)(i >> 8 & 255L));
        os.write((int)(i & 255L));
    }

    public static int readShort(InputStream is) throws IOException {
        return (is.read() << 8) | is.read();
    }

    public static int readInt(InputStream is) throws IOException {
        return (is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read();
    }

    public static String readASCII(int len, InputStream bytesIn) throws IOException {
        char[] charIn = new char[len];
        for(int i = 0; i < len; ++i) {
            charIn[i] = (char)(bytesIn.read() & 0xFF);
        }
        return new String(charIn);
    }
}
