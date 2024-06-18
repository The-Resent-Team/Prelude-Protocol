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

public class ModStatusPacket extends S2CPacket {
    private String modIdentifier;
    private ModStatus modStatus;

    public ModStatusPacket() {}

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
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if (is.read() != MOD_STATUS_ID)
                throw new InvalidPacketException("Packet ID doesn't match with MOD_STATUS_ID (%id%)!"
                        .replace("%id%", MOD_STATUS_ID + ""));

            String modId = StreamUtils.readASCII(is.read(), is);
            ModStatus modStatus = ModStatus.from((byte) is.read());

            if (modStatus == null)
                throw new InvalidPacketException("Constructed MOD_STATUS_PACKET has an illegal ModStatus!");

            this.modIdentifier = modId;
            this.modStatus = modStatus;
        } catch (Exception e) {
            if (e instanceof InvalidPacketException)
                throw (InvalidPacketException) e;
            throw new InvalidPacketException("Failed to parse MOD_STATUS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleModStatus(this);
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

    public String getModIdentifier() {
        return modIdentifier;
    }

    public ModStatus getModStatus() {
        return modStatus;
    }
}
