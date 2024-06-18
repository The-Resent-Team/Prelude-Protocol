package prelude.protocol.packets.s2c;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UpdateOffhandPacket extends S2CPacket {
    private boolean canClientDisregardThis;
    private String serializedItem;

    public UpdateOffhandPacket() {}

    private UpdateOffhandPacket(boolean canClientDisregardThis, String serializedItem) {
        this.canClientDisregardThis = canClientDisregardThis;
        this.serializedItem = serializedItem;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(UPDATE_OFFHAND_ID);

        bao.write(canClientDisregardThis ? 1 : 0);

        StreamUtils.writeShort(serializedItem.length(), bao);
        bao.write(serializedItem.getBytes(StandardCharsets.US_ASCII));

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if (is.read() != UPDATE_OFFHAND_ID)
                throw new InvalidPacketException("Packet ID doesn't match with TOTEM_USED_ID (%id%)!"
                        .replace("%id%", TOTEM_USED_ID + ""));

            boolean canClientDisregardThis = is.read() != 0;
            String serializedItem = StreamUtils.readASCII(StreamUtils.readShort(is), is);

            if (!serializedItem.startsWith("ItemStack{") || !serializedItem.endsWith("}"))
                throw new InvalidPacketException("Constructed UPDATE_OFFHAND_PACKET has an invalid serialized item!");

            this.canClientDisregardThis = canClientDisregardThis;
            this.serializedItem = serializedItem;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse UPDATE_OFFHAND_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleOffhandUpdate(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOffhandPacket that)) return false;
        return canClientDisregardThis == that.canClientDisregardThis && Objects.equals(serializedItem, that.serializedItem);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean canClientDisregardThis = false;
        private String serializedItem = null;

        private Builder() {}

        public Builder canClientDisregardThis(boolean canClientDisregardThis) {
            this.canClientDisregardThis = canClientDisregardThis;
            return this;
        }

        public Builder serializedItem(String serializedItem) {
            this.serializedItem = serializedItem;
            return this;
        }

        public UpdateOffhandPacket build() {
            if (serializedItem == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new UpdateOffhandPacket(canClientDisregardThis, serializedItem);
        }
    }

    public boolean isCanClientDisregardThis() {
        return canClientDisregardThis;
    }

    public String getSerializedItem() {
        return serializedItem;
    }
}
