package prelude.protocol.packets;

public abstract class C2SPacket {
    private static byte NEXT_ID = 0;

    private byte packetId;

    public C2SPacket() {
        if (NEXT_ID == Byte.MAX_VALUE)
            throw new RuntimeException("How did this happen! You've reached the max amount of packets!");
        packetId = NEXT_ID++;
    }

    public byte getPacketId() {
        return packetId;
    }
}
