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

package prelude.protocol.packets.s2c;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerHandshakePacket extends S2CPacket {
    private byte preludeMajorVersion;
    private byte preludeMinorVersion;
    private byte preludePatchVersion;

    private byte serverMajorVersion;
    private byte serverMinorVersion;
    private byte serverPatchVersion;

    public ServerHandshakePacket() {}

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

        bao.write(SERVER_HANDSHAKE_ID);

        bao.write(preludeMajorVersion);
        bao.write(preludeMinorVersion);
        bao.write(preludePatchVersion);
        bao.write(serverMajorVersion);
        bao.write(serverMinorVersion);
        bao.write(serverPatchVersion);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if (is.read() != SERVER_HANDSHAKE_ID)
                throw new InvalidPacketException("Packet ID doesn't match with SERVER_HANDSHAKE_ID (%id%)!"
                        .replace("%id%", SERVER_HANDSHAKE_ID + ""));

            byte preludeMajorVersion = (byte) is.read();
            byte preludeMinorVersion = (byte) is.read();
            byte preludePatchVersion = (byte) is.read();
            byte serverMajorVersion = (byte) is.read();
            byte serverMinorVersion = (byte) is.read();
            byte serverPatchVersion = (byte) is.read();

            if (preludeMajorVersion < 0 || preludeMinorVersion < 0 || preludePatchVersion < 0 || serverMajorVersion < 0 || serverMinorVersion < 0 || serverPatchVersion < 0)
                throw new InvalidPacketException("Constructed SERVER_HANDSHAKE_PACKET is invalid!");

            this.preludeMajorVersion = preludeMajorVersion;
            this.preludeMinorVersion = preludeMinorVersion;
            this.preludePatchVersion = preludePatchVersion;
            this.serverMajorVersion = serverMajorVersion;
            this.serverMinorVersion = serverMinorVersion;
            this.serverPatchVersion = serverPatchVersion;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse SERVER_HANDSHAKE_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleServerHandshake(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerHandshakePacket)) return false;
        ServerHandshakePacket that = (ServerHandshakePacket) o;
        return preludeMajorVersion == that.preludeMajorVersion && preludeMinorVersion == that.preludeMinorVersion && preludePatchVersion == that.preludePatchVersion && serverMajorVersion == that.serverMajorVersion && serverMinorVersion == that.serverMinorVersion && serverPatchVersion == that.serverPatchVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private byte preludeMajorVersion = -1;
        private byte preludeMinorVersion = -1;
        private byte preludePatchVersion = -1;

        private byte serverMajorVersion = -1;
        private byte serverMinorVersion = -1;
        private byte serverPatchVersion = -1;

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
            if (preludeMajorVersion == -1 || preludeMinorVersion == -1 || preludePatchVersion == -1 || serverMajorVersion == -1 || serverMinorVersion == -1 || serverPatchVersion == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new ServerHandshakePacket(preludeMajorVersion, preludeMinorVersion, preludePatchVersion, serverMajorVersion, serverMinorVersion, serverPatchVersion);
        }
    }

    public byte getPreludeMajorVersion() {
        return preludeMajorVersion;
    }

    public byte getPreludeMinorVersion() {
        return preludeMinorVersion;
    }

    public byte getPreludePatchVersion() {
        return preludePatchVersion;
    }

    public byte getServerMajorVersion() {
        return serverMajorVersion;
    }

    public byte getServerMinorVersion() {
        return serverMinorVersion;
    }

    public byte getServerPatchVersion() {
        return serverPatchVersion;
    }
}
