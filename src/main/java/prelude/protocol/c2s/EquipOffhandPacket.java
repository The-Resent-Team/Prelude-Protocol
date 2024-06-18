package prelude.protocol.c2s;

import prelude.protocol.C2SPacket;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EquipOffhandPacket extends C2SPacket {
    public final short slot;

    private EquipOffhandPacket(short slot) {
        this.slot = slot;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(EQUIP_OFFHAND_ID);
        StreamUtils.writeShort(slot, bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipOffhandPacket that)) return false;
        return slot == that.slot;
    }

    /*
    * Note: This is the only packet that could be abused
    * to try to cause damage to the server running prelude
    * we must sanitize all input to this to prevent damage
    * */
    public static EquipOffhandPacket from(InputStream is) {
        try {
            if ((byte) is.read() != EQUIP_OFFHAND_ID)
                return null;

            short slot = (short) StreamUtils.readShort(is);

            // There is no reliable way to determine the max
            // slot id from here without hard coding it
            // we must let the bukkit implementation check this
            if (slot < 0)
                return null;

            return builder()
                    .slot(slot)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse equip offhand packet!");
            e.printStackTrace();
            return null;
        }
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
}
