package prelude.protocol.parser;

import prelude.protocol.C2SPacket;
import prelude.protocol.c2s.*;
import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static prelude.protocol.C2SPacket.*;

/*
* Only reason this class exists is because TeaVM does not support
* using reflection to invoke a method from a class instance
* */
public class C2SPacketParser {
    public static C2SPacket parseC2SPacket(byte[] bytes) {
        if (bytes == null)
            return null;

        if (bytes.length < 1)
            return null;
        byte id = bytes[0];

        return createPacketFromId(id, new ByteArrayInputStream(bytes));
    }

    public static C2SPacket parseC2SPacket(InputStream is) throws IOException {
        if (is == null)
            return null;

        is.mark(1);
        byte id = (byte) is.read();
        is.reset();

        return createPacketFromId(id, is);
    }

    private static C2SPacket createPacketFromId(byte id, InputStream is) {
        return switch (id) {
            case CLIENT_HANDSHAKE_ID -> ClientHandshakePacket.from(is);
            case EQUIP_OFFHAND_ID -> EquipOffhandPacket.from(is);
            default -> null;
        };
    }
}
