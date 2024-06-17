package prelude.protocol.s2c;

import prelude.protocol.S2CPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerHandshakePacket extends S2CPacket {
    public final byte preludeMajorVersion;
    public final byte preludeMinorVersion;
    public final byte preludePatchVersion;

    public final byte serverMajorVersion;
    public final byte serverMinorVersion;
    public final byte serverPatchVersion;

    public ServerHandshakePacket(byte preludeMajorVersion, byte preludeMinorVersion, byte preludePatchVersion, byte serverMajorVersion, byte serverMinorVersion, byte serverPatchVersion) {
        this.preludeMajorVersion = preludeMajorVersion;
        this.preludeMinorVersion = preludeMinorVersion;
        this.preludePatchVersion = preludePatchVersion;
        this.serverMajorVersion = serverMajorVersion;
        this.serverMinorVersion = serverMinorVersion;
        this.serverPatchVersion = serverPatchVersion;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        bao.write(S2CPacket.SERVER_HANDSHAKE_ID);
        bao.write(preludeMajorVersion);
        bao.write(preludeMinorVersion);
        bao.write(preludePatchVersion);
        bao.write(serverMajorVersion);
        bao.write(serverMinorVersion);
        bao.write(serverPatchVersion);

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerHandshakePacket that)) return false;
        return preludeMajorVersion == that.preludeMajorVersion && preludeMinorVersion == that.preludeMinorVersion && preludePatchVersion == that.preludePatchVersion && serverMajorVersion == that.serverMajorVersion && serverMinorVersion == that.serverMinorVersion && serverPatchVersion == that.serverPatchVersion;
    }

    public static ServerHandshakePacket from(InputStream is) {
        try {
            if (is.read() != S2CPacket.SERVER_HANDSHAKE_ID)
                return null;

            byte preludeMajorVersion = (byte) is.read();
            byte preludeMinorVersion = (byte) is.read();
            byte preludePatchVersion = (byte) is.read();
            byte serverMajorVersion = (byte) is.read();
            byte serverMinorVersion = (byte) is.read();
            byte serverPatchVersion = (byte) is.read();

            return builder()
                    .preludeMajorVersion(preludeMajorVersion)
                    .preludeMinorVersion(preludeMinorVersion)
                    .preludePatchVersion(preludePatchVersion)
                    .serverMajorVersion(serverMajorVersion)
                    .serverMinorVersion(serverMinorVersion)
                    .serverPatchVersion(serverPatchVersion)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse server handshake packet!");
            e.printStackTrace();
            return null;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private byte preludeMajorVersion;
        private byte preludeMinorVersion;
        private byte preludePatchVersion;

        private byte serverMajorVersion;
        private byte serverMinorVersion;
        private byte serverPatchVersion;

        private Builder() {}

        public Builder preludeMajorVersion(int preludeMajorVersion) {
            this.preludeMajorVersion = (byte) preludeMajorVersion;
            return this;
        }

        public Builder preludeMinorVersion(int preludeMinorVersion) {
            this.preludeMinorVersion = (byte) preludeMinorVersion;
            return this;
        }

        public Builder preludePatchVersion(int preludePatchVersion) {
            this.preludePatchVersion = (byte) preludePatchVersion;
            return this;
        }

        public Builder serverMajorVersion(int serverMajorVersion) {
            this.serverMajorVersion = (byte) serverMajorVersion;
            return this;
        }

        public Builder serverMinorVersion(int serverMinorVersion) {
            this.serverMinorVersion = (byte) serverMinorVersion;
            return this;
        }

        public Builder serverPatchVersion(int serverPatchVersion) {
            this.serverPatchVersion = (byte) serverPatchVersion;
            return this;
        }

        public ServerHandshakePacket build() {
            return new ServerHandshakePacket(preludeMajorVersion, preludeMinorVersion, preludePatchVersion, serverMajorVersion, serverMinorVersion, serverPatchVersion);
        }
    }
}
