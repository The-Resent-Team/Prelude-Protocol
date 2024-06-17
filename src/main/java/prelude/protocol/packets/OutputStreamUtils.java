package prelude.protocol.packets;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamUtils {
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
        System.out.println((int)(i >> 56 & 255L));
        System.out.println((int)(i >> 48 & 255L));
        System.out.println((int)(i >> 40 & 255L));
        System.out.println((int)(i >> 32 & 255L));
        System.out.println((int)(i >> 24 & 255L));
        System.out.println((int)(i >> 16 & 255L));
        System.out.println((int)(i >> 8 & 255L));
        System.out.println((int)(i & 255L));

        os.write((int)(i >> 56 & 255L));
        os.write((int)(i >> 48 & 255L));
        os.write((int)(i >> 40 & 255L));
        os.write((int)(i >> 32 & 255L));
        os.write((int)(i >> 24 & 255L));
        os.write((int)(i >> 16 & 255L));
        os.write((int)(i >> 8 & 255L));
        os.write((int)(i & 255L));
    }
}
