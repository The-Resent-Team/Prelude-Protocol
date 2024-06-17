package prelude.protocol.packets.c2s;

import prelude.protocol.packets.C2SPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

import static prelude.protocol.packets.OutputStreamUtils.writeShort;

public final class ClientHandshakePacket extends C2SPacket {
    public final String username;

    // give them between 0 and 255
    public final byte resentMajorVersion;
    public final byte resentMinorVersion;

    public final ClientType clientType;

    // if the config option is on, servers will attempt to resync this value
    public final boolean clientClaimsSelfIsRankedPlayer;
    public final String[] enabledMods;

    private ClientHandshakePacket(String username, byte resentMajorVersion, byte resentMinorVersion, ClientType clientType,
                                 boolean clientClaimsSelfIsRankedPlayer, String[] enabledMods) {
        this.username = username;
        this.resentMajorVersion = resentMajorVersion;
        this.resentMinorVersion = resentMinorVersion;
        this.clientType = clientType;
        this.clientClaimsSelfIsRankedPlayer = clientClaimsSelfIsRankedPlayer;
        this.enabledMods = enabledMods;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        // packet id for parsing
        bao.write(getPacketId());

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
        StringJoiner sb = new StringJoiner(String.valueOf(0x00));
        for (String mod : enabledMods)
            sb.add(mod);

        writeShort(sb.length(), bao);
        bao.write(sb.toString().getBytes(StandardCharsets.US_ASCII));
        return bao.toByteArray();
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
    }

    public static class Builder {
        private String username;
        private byte resentMajorVersion;
        private byte resentMinorVersion;
        private ClientType clientType;
        private boolean clientClaimsSelfIsRankedPlayer;
        private String[] enabledMods;

        private Builder() {}

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder resentMajorVersion(byte resentMajorVersion) {
            this.resentMajorVersion = resentMajorVersion;
            return this;
        }

        public Builder resentMinorVersion(byte resentMinorVersion) {
            this.resentMinorVersion = resentMinorVersion;
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

            return new ClientHandshakePacket(username, resentMajorVersion, resentMinorVersion, clientType, clientClaimsSelfIsRankedPlayer, enabledMods);
        }
    }
}
