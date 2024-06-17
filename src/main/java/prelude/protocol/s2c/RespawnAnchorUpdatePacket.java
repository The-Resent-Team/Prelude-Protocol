package prelude.protocol.s2c;

import prelude.protocol.S2CPacket;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RespawnAnchorUpdatePacket extends S2CPacket {
    public final byte charge;
    public final int x;
    public final int y;
    public final int z;

    private RespawnAnchorUpdatePacket(byte charge, int x, int y, int z) {
        this.charge = charge;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(S2CPacket.RESPAWN_ANCHOR_UPDATE_ID);

        bao.write(charge);
        StreamUtils.writeInt(x, bao);
        StreamUtils.writeInt(y, bao);
        StreamUtils.writeInt(z, bao);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RespawnAnchorUpdatePacket that)) return false;
        return charge == that.charge && x == that.x && y == that.y && z == that.z;
    }

    public static RespawnAnchorUpdatePacket from(InputStream in) {
        try {
            if ((byte) in.read() != S2CPacket.RESPAWN_ANCHOR_UPDATE_ID)
                return null;

            byte charge = (byte) in.read();
            int x = StreamUtils.readInt(in);
            int y = StreamUtils.readInt(in);
            int z = StreamUtils.readInt(in);

            return builder()
                    .charge(charge)
                    .x(x)
                    .y(y)
                    .z(z)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse respawn anchor update packet!");
            e.printStackTrace();
            return null;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private byte charge = -1;
        private int x;
        private int y;
        private int z;

        private Builder() {}

        public Builder charge(int charge) {
            this.charge = (byte) charge;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder z(int z) {
            this.z = z;
            return this;
        }

        public RespawnAnchorUpdatePacket build() {
            if (charge == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new RespawnAnchorUpdatePacket(charge, x, y, z);
        }
    }
}
