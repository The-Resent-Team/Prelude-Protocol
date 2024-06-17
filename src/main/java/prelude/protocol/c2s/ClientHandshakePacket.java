package prelude.protocol.c2s;

import prelude.protocol.StreamUtils;
import prelude.protocol.C2SPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;

public final class ClientHandshakePacket extends C2SPacket {
    private static final String NULL_TERMINATOR = "\u0000";

    public final String username;

    // give them between 0 and 255
    public final byte resentMajorVersion;
    public final byte resentMinorVersion;

    // this is the integer in the signed build
    public final int resentBuildInteger;

    public final ClientType clientType;

    // if the config option is on, servers will attempt to resync this value
    public final boolean clientClaimsSelfIsRankedPlayer;
    public final String[] enabledMods;

    private ClientHandshakePacket(String username, byte resentMajorVersion, byte resentMinorVersion, int resentBuildInteger, ClientType clientType,
                                 boolean clientClaimsSelfIsRankedPlayer, String[] enabledMods) {
        this.username = username;
        this.resentMajorVersion = resentMajorVersion;
        this.resentMinorVersion = resentMinorVersion;
        this.resentBuildInteger = resentBuildInteger;
        this.clientType = clientType;
        this.clientClaimsSelfIsRankedPlayer = clientClaimsSelfIsRankedPlayer;
        this.enabledMods = enabledMods;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        // packet id for parsing
        bao.write(C2SPacket.CLIENT_HANDSHAKE_ID);

        // write username
        // we can write it as a byte since we know username cant exceed 16 chars, which is well below the limit of a byte
        bao.write(username.length());
        bao.write(username.getBytes(StandardCharsets.US_ASCII));

        // write resent ver so prelude doesnt send packets which this ver cant parse
        bao.write(resentMajorVersion);
        bao.write(resentMinorVersion);

        // write client type
        bao.write(clientType.value);

        // write boolean
        bao.write(clientClaimsSelfIsRankedPlayer ? 1 : 0);

        // write all enabled mods
        StringJoiner sb = new StringJoiner(NULL_TERMINATOR);
        for (String mod : enabledMods)
            sb.add(mod);

        StreamUtils.writeShort(sb.length(), bao);
        bao.write(sb.toString().getBytes(StandardCharsets.US_ASCII));

        return bao.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientHandshakePacket that)) return false;
        return resentMajorVersion == that.resentMajorVersion && resentMinorVersion == that.resentMinorVersion && clientClaimsSelfIsRankedPlayer == that.clientClaimsSelfIsRankedPlayer && Objects.equals(username, that.username) && clientType == that.clientType && Objects.deepEquals(enabledMods, that.enabledMods);
    }

    /**+
     * Attempts to parse a byte array into a packet
     * @return null if failed to parse, or a new instance of this packet
     * */
    public static ClientHandshakePacket from(InputStream is) {
        try {
            if ((byte) is.read() != C2SPacket.CLIENT_HANDSHAKE_ID)
                return null;

            String username = StreamUtils.readASCII(is.read(), is);
            byte major = (byte) is.read();
            byte minor = (byte) is.read();
            ClientType type = ClientType.getFromByte((byte) is.read());
            boolean clientClaimsSelfIsRankedPlayer = (byte) is.read() == 1;
            String enabledMods = StreamUtils.readASCII(StreamUtils.readShort(is), is);
            String[] mods = enabledMods.split(NULL_TERMINATOR);

            if (username.length() < 3 || username.length() > 16 || minor < 0 || major < 0 || type == null)
                return null;

            return builder()
                    .username(username)
                    .resentMajorVersion(major)
                    .resentMinorVersion(minor)
                    .clientType(type)
                    .clientClaimsSelfIsRankedPlayer(clientClaimsSelfIsRankedPlayer)
                    .enabledMods(mods)
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse client handshake packet!");
            e.printStackTrace();
            return null;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public enum ClientType {
        STABLE(0),
        BETA(1),
        DEV(2);

        final byte value;
        ClientType(int value) {
            this.value = (byte) value;
        }

        public static ClientType getFromByte(byte b) {
            for (ClientType type : ClientType.values()) {
                if (type.value == b)
                    return type;
            }

            return null;
        }
    }

    public static class Builder {
        private String username;
        private byte resentMajorVersion = -1;
        private byte resentMinorVersion = -1;
        private int resentBuildInteger = -1;
        private ClientType clientType = null;
        private boolean clientClaimsSelfIsRankedPlayer;
        private String[] enabledMods = null;

        private Builder() {}

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder resentMajorVersion(int resentMajorVersion) {
            this.resentMajorVersion = (byte) resentMajorVersion;
            return this;
        }

        public Builder resentMinorVersion(int resentMinorVersion) {
            this.resentMinorVersion = (byte) resentMinorVersion;
            return this;
        }

        public Builder resentBuildInteger(int resentBuildInteger) {
            this.resentBuildInteger = resentBuildInteger;
            return this;
        }

        public Builder clientType(ClientType clientType) {
            this.clientType = clientType;
            return this;
        }

        public Builder clientClaimsSelfIsRankedPlayer(boolean clientClaimsSelfIsRankedPlayer) {
            this.clientClaimsSelfIsRankedPlayer = clientClaimsSelfIsRankedPlayer;
            return this;
        }

        public Builder enabledMods(String[] enabledMods) {
            this.enabledMods = enabledMods;
            return this;
        }

        public ClientHandshakePacket build() {
            if (username == null || username.length() > 16 || username.length() < 3)
                throw new RuntimeException("Username must be between 3 and 16 characters");

            if (resentMajorVersion == -1 || resentMinorVersion == -1 || resentBuildInteger == -1 || clientType == null || enabledMods == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new ClientHandshakePacket(username, resentMajorVersion, resentMinorVersion, resentBuildInteger, clientType, clientClaimsSelfIsRankedPlayer, enabledMods);
        }
    }
}
