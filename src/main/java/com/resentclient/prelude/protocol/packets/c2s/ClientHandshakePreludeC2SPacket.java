/*
 * Prelude-Protocol is an implementation to abstract communications between the Client and Prelude-API.
 * Copyright (C) 2024 cire3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.resentclient.prelude.protocol.packets.c2s;

import com.resentclient.common.WriteableObject;
import com.resentclient.prelude.protocol.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public final class ClientHandshakePreludeC2SPacket extends PreludeC2SPacket {
    private static final String NULL_TERMINATOR = "\u0000";

    private String username;

    private int resentMajorVersion;
    private int resentMinorVersion;

    // this is the integer in the signed build
    private int resentBuildInteger;

    private ClientType clientType;

    // if the config option is on, servers will attempt to resync this value
    private boolean clientClaimsSelfIsRankedPlayer;
    private String[] enabledMods;

    public ClientHandshakePreludeC2SPacket() {}

    private ClientHandshakePreludeC2SPacket(String username, byte resentMajorVersion, byte resentMinorVersion, int resentBuildInteger, ClientType clientType,
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
        bao.write(packetId);

        // write username
        // we can write it as a byte since we know username cant exceed 16 chars, which is well below the limit of a byte
        bao.write(username.length());
        bao.write(username.getBytes(StandardCharsets.US_ASCII));

        // write resent ver so prelude doesnt send packets which this ver cant parse
        bao.write(resentMajorVersion);
        bao.write(resentMinorVersion);

        // write client type
        clientType.write(bao);

        // write boolean
        bao.write(clientClaimsSelfIsRankedPlayer ? 1 : 0);

        // write all enabled mods
        StringBuilder sb = new StringBuilder();
        for (String mod : enabledMods)
            sb.append(mod).append(NULL_TERMINATOR);

        StreamUtils.writeShort(sb.length(), bao);
        bao.write(sb.toString().getBytes(StandardCharsets.US_ASCII));

        return bao.toByteArray();
    }

    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("CLIENT_HANDSHAKE_ID", is);

            String username = StreamUtils.readASCII(is.read(), is);
            int major = is.read();
            int minor = is.read();
            ClientType type = ClientType.deserialize(is);
            boolean clientClaimsSelfIsRankedPlayer = is.read() == 1;
            String enabledMods = StreamUtils.readASCII(StreamUtils.readShort(is), is);
            String[] mods = enabledMods.split(NULL_TERMINATOR);

            if (username.length() < 3 || username.length() > 16 || minor < 0 || major < 0 || type == null)
                throw new InvalidPreludePacketException("Constructed CLIENT_HANDSHAKE_PACKET is invalid!");

            this.username = username;
            this.resentMajorVersion = major;
            this.resentMinorVersion = minor;
            this.clientType = type;
            this.clientClaimsSelfIsRankedPlayer = clientClaimsSelfIsRankedPlayer;
            this.enabledMods = mods;
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse CLIENT_HANDSHAKE_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeC2SPacketHandler handler) {
        handler.handleClientHandshake(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientHandshakePreludeC2SPacket)) return false;
        ClientHandshakePreludeC2SPacket that = (ClientHandshakePreludeC2SPacket) o;
        return resentMajorVersion == that.resentMajorVersion && resentMinorVersion == that.resentMinorVersion && clientClaimsSelfIsRankedPlayer == that.clientClaimsSelfIsRankedPlayer && Objects.equals(username, that.username) && clientType == that.clientType && Objects.deepEquals(enabledMods, that.enabledMods);
    }

    public static Builder builder() {
        return new Builder();
    }

    public enum ClientType implements WriteableObject {
        STABLE(0),
        BETA(1),
        DEV(2);

        final int value;
        ClientType(int value) {
            this.value = value;
        }

        private static ClientType of(int b) {
            for (ClientType type : ClientType.values()) {
                if (type.value == b)
                    return type;
            }

            return null;
        }

        public static ClientType deserialize(InputStream is) throws IOException {
            return of(is.read());
        }

        @Override
        public void write(OutputStream out) throws IOException {
            out.write(value);
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

        public ClientHandshakePreludeC2SPacket build() {
            if (username == null || username.length() > 16 || username.length() < 3)
                throw new RuntimeException("Username must be between 3 and 16 characters");

            if (resentMajorVersion == -1 || resentMinorVersion == -1 || resentBuildInteger == -1 || clientType == null || enabledMods == null)
                throw new IllegalStateException("Not all required fields are set!");

            return new ClientHandshakePreludeC2SPacket(username, resentMajorVersion, resentMinorVersion, resentBuildInteger, clientType, clientClaimsSelfIsRankedPlayer, enabledMods);
        }
    }

    public String getUsername() {
        return username;
    }

    public int getResentMajorVersion() {
        return resentMajorVersion;
    }

    public int getResentMinorVersion() {
        return resentMinorVersion;
    }

    public int getResentBuildInteger() {
        return resentBuildInteger;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public boolean doesClientClaimSelfIsRankedPlayer() {
        return clientClaimsSelfIsRankedPlayer;
    }

    public String[] getEnabledMods() {
        return Arrays.copyOf(enabledMods, enabledMods.length);
    }
}
