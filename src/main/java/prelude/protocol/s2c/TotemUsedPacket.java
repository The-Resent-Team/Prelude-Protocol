package prelude.protocol.s2c;

import prelude.protocol.S2CPacket;

import java.io.IOException;
import java.io.InputStream;

public class TotemUsedPacket extends S2CPacket {
    @Override
    public byte[] toBytes() throws IOException {
        return new byte[] { TOTEM_USED_ID };
    }

    @Override
    public boolean equals(Object packet) {
        return packet instanceof TotemUsedPacket;
    }

    public static TotemUsedPacket from(InputStream is) {
        try {
            if (is.read() != TOTEM_USED_ID)
                return null;

            return new TotemUsedPacket();
        } catch (Exception e) {
            System.err.println("Failed to parse totem used packet!");
            e.printStackTrace();
            return null;
        }
    }
}
