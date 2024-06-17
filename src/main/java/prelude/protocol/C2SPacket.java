package prelude.protocol;

public abstract class C2SPacket extends Packet {
    public static final byte CLIENT_HANDSHAKE_ID = 0;
    public static final byte EQUIP_OFFHAND_ID = 1;

    @SuppressWarnings("unchecked")
    public <E extends C2SPacket> E cast(Class<E> clazz) throws ClassCastException {
        return (E) this;
    }
}
