package prelude.protocol.packets.s2c;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;

import java.io.IOException;
import java.io.InputStream;

public class TotemUsedPacket extends S2CPacket {
    public TotemUsedPacket() {}

    @Override
    public byte[] toBytes() throws IOException {
        return new byte[] { TOTEM_USED_ID };
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if (is.read() != TOTEM_USED_ID)
                throw new InvalidPacketException("Packet ID doesn't match with TOTEM_USED_ID (%id%)!"
                        .replace("%id%", TOTEM_USED_ID + ""));
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse TOTEM_USED_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleTotemUsed(this);
    }

    @Override
    public boolean equals(Object packet) {
        return packet instanceof TotemUsedPacket;
    }
}
