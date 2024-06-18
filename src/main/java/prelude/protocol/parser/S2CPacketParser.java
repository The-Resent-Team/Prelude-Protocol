package prelude.protocol.parser;

import prelude.protocol.S2CPacket;
import prelude.protocol.packets.s2c.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static prelude.protocol.S2CPacket.*;

public class S2CPacketParser {
    private static final Map<Integer, S2CPacket> ID_TO_PACKET = new HashMap<>();

    static {

    }

    public static S2CPacket parseC2SPacket(byte[] bytes) {
        try {
            if (bytes == null)
                return null;

            if (bytes.length < 1)
                return null;
            byte id = bytes[0];

            return createPacketFromId(id, new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            System.err.println("Error parsing packet: " + e.getMessage());
            return null;
        }
    }

    public static S2CPacket parseC2SPacket(InputStream is) throws IOException {
        try {
            if (is == null)
                return null;

            is.mark(1);
            byte id = (byte) is.read();
            is.reset();

            return createPacketFromId(id, is);
        } catch (Exception e) {
            System.err.println("Error parsing packet: " + e.getMessage());
            return null;
        }
    }
}
