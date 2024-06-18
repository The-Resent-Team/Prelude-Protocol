package prelude.protocol.packets.s2c;

import prelude.protocol.S2CPacket;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ModStatusPacket extends S2CPacket {
    public final String modIdentifier;
    public final ModStatus modStatus;

    private ModStatusPacket(String modIdentifier, ModStatus modStatus) {
        this.modIdentifier = modIdentifier;
        this.modStatus = modStatus;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(MOD_STATUS_ID);

        bao.write(modIdentifier.length());
        bao.write(modIdentifier.getBytes(StandardCharsets.US_ASCII));

        bao.write(modStatus.value);

        return bao.toByteArray();
    }

    @Override
    public ModStatusPacket loadData(InputStream is) {
        try {
            if (is.read() != MOD_STATUS_ID)
                return null;

            String modId = StreamUtils.readASCII(is.read(), is);
            ModStatus modStatus = ModStatus.from((byte) is.read());

            return builder()
                    .modIdentifier(modId)
                    .modStatus(modStatus)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse mod status packet!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModStatusPacket that)) return false;
        return Objects.equals(modIdentifier, that.modIdentifier) && modStatus == that.modStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String modIdentifier = null;
        private ModStatus modStatus = null;

        private Builder() {}

        public Builder modIdentifier(String modIdentifier) {
            this.modIdentifier = modIdentifier;
            return this;
        }

        public Builder modStatus(ModStatus modStatus) {
            this.modStatus = modStatus;
            return this;
        }

        public ModStatusPacket build() {
            if (modIdentifier == null || modStatus == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new ModStatusPacket(modIdentifier, modStatus);
        }
    }

    public enum ModStatus {
        SUPPORTED(0),
        DISABLE(1);

        public final byte value;

        ModStatus(int value) {
            this.value = (byte) value;
        }

        public static ModStatus from(byte value) {
            for (ModStatus modStatus : ModStatus.values()) {
                if (modStatus.value == value)
                    return modStatus;
            }
            return null;
        }
    }
}
