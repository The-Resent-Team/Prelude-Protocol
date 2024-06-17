package prelude.protocol.packets;

public abstract class S2CPacket {
    private static int NEXT_ID = 0;

    private int packetId;

    public S2CPacket() {
        packetId = NEXT_ID++;
    }

    public int getPacketId() {
        return packetId;
    }
}
