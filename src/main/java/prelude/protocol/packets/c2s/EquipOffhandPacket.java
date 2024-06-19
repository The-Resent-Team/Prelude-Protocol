package prelude.protocol.packets.c2s;

import prelude.protocol.C2SPacket;
import prelude.protocol.C2SPacketHandler;
import prelude.protocol.InvalidPacketException;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EquipOffhandPacket extends C2SPacket {
    private short slot;
    private UUID recipient;

    public EquipOffhandPacket() {}

    private EquipOffhandPacket(short slot, UUID recipient) {
        this.slot = slot;
        this.recipient = recipient;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(EQUIP_OFFHAND_ID);
        StreamUtils.writeShort(slot, bao);

        return bao.toByteArray();
    }

    /*
    * Note: This is the only packet that could be abused
    * to try to cause damage to the server running prelude
    * we must sanitize all input to this to prevent damage
    * */
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if ((byte) is.read() != EQUIP_OFFHAND_ID)
                throw new InvalidPacketException("Packet ID doesn't match with EQUIP_OFFHAND_ID (%id%)!"
                        .replace("%id%", EQUIP_OFFHAND_ID + ""));

            short slot = (short) StreamUtils.readShort(is);

            // There is no reliable way to determine the max
            // slot id from here without hard coding it
            // we must let the bukkit implementation check this
            if (slot < 0)
                throw new InvalidPacketException("Constructed EQUIP_OFFHAND_PACKET has a negative slot!");

            this.slot = slot;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse EQUIP_OFFHAND_PACKET!", e);
        }
    }

    @Override
    public void processSelf(C2SPacketHandler handler) {
        handler.handleEquipOffhand(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipOffhandPacket)) return false;
        EquipOffhandPacket that = (EquipOffhandPacket) o;
        return slot == that.slot;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private short slot = -1;

        private Builder() {}

        public Builder slot(short slot) {
            this.slot = slot;
            return this;
        }

        public EquipOffhandPacket build() {
            if (slot == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new EquipOffhandPacket(slot);
        }
    }

    public short getSlot() {
        return slot;
    }

    @Override
    public UUID getRecipient() {
        return recipient;
    }
}
