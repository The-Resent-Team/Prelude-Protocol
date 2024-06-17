package prelude.protocol;

public abstract class S2CPacket extends Packet {
    public static final byte SERVER_HANDSHAKE_ID = 0;
    public static final byte MOD_STATUS_ID = 1;
    public static final byte RESPAWN_ANCHOR_UPDATE_ID = 2;
    public static final byte SERVER_TPS_ID = 3;
    public static final byte UPDATE_OFFHAND_ID = 4;
    public static final byte TOTEM_USED_ID = 5;
    public static final byte WAYPOINTS_ID = 6;

    @SuppressWarnings("unchecked")
    public <E extends S2CPacket> E cast(Class<E> clazz) throws ClassCastException {
        return (E) this;
    }
}
