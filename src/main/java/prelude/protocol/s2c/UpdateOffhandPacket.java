package prelude.protocol.s2c;

import prelude.protocol.S2CPacket;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UpdateOffhandPacket extends S2CPacket {
    public final boolean canClientDisregardThis;
    public final String serializedItem;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOffhandPacket that)) return false;
        return canClientDisregardThis == that.canClientDisregardThis && Objects.equals(serializedItem, that.serializedItem);
    }

    public static UpdateOffhandPacket from(InputStream is) {
        try {
            if (is.read() != UPDATE_OFFHAND_ID)
                return null;

            boolean canClientDisregardThis = is.read() != 0;
            String serializedItem = StreamUtils.readASCII(StreamUtils.readShort(is), is);

            return builder()
                    .serializedItem(serializedItem)
                    .canClientDisregardThis(canClientDisregardThis)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse update offhand packet!");
            e.printStackTrace();
            return null;
        }
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
}
