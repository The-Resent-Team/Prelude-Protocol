package prelude.protocol;

import java.io.InputStream;

public abstract class C2SPacket extends Packet {
    public static final byte CLIENT_HANDSHAKE_ID = 0;
    public static final byte EQUIP_OFFHAND_ID = 1;

    @Override
    public abstract C2SPacket loadData(InputStream is)
}
