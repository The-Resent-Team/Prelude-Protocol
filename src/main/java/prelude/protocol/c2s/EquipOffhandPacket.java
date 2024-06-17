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

        bao.write(C2SPacket.EQUIP_OFFHAND_ID);
        StreamUtils.writeShort(slot, bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipOffhandPacket that)) return false;
        return slot == that.slot;
    }

    public static EquipOffhandPacket toPacket(InputStream is) throws IOException {
        try {
            if ((byte) is.read() != C2SPacket.EQUIP_OFFHAND_ID)
                return null;

            short slot = (short) StreamUtils.readShort(is);

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
        private short slot;

        private Builder() {}

        public Builder slot(short slot) {
            this.slot = slot;
            return this;
        }

        public EquipOffhandPacket build() {
            return new EquipOffhandPacket(slot);
        }
    }
}
