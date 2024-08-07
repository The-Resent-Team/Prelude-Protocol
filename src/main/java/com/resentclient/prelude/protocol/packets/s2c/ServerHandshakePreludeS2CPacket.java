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

package com.resentclient.prelude.protocol.packets.s2c;

import com.resentclient.prelude.protocol.InvalidPreludePacketException;
import com.resentclient.prelude.protocol.PreludeS2CPacket;
import com.resentclient.prelude.protocol.PreludeS2CPacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerHandshakePreludeS2CPacket extends PreludeS2CPacket {
    private int preludeMajorVersion;
    private int preludeMinorVersion;
    private int preludePatchVersion;

    private int serverMajorVersion;
    private int serverMinorVersion;
    private int serverPatchVersion;

    public ServerHandshakePreludeS2CPacket() {}

    private ServerHandshakePreludeS2CPacket(int preludeMajorVersion, int preludeMinorVersion, int preludePatchVersion, int serverMajorVersion, int serverMinorVersion, int serverPatchVersion) {
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

        bao.write(packetId);

        bao.write(preludeMajorVersion);
        bao.write(preludeMinorVersion);
        bao.write(preludePatchVersion);
        bao.write(serverMajorVersion);
        bao.write(serverMinorVersion);
        bao.write(serverPatchVersion);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPreludePacketException {
        try {
            this.validateOrThrow("SERVER_HANDSHAKE_ID", is);

            int preludeMajorVersion = is.read();
            int preludeMinorVersion = is.read();
            int preludePatchVersion = is.read();
            int serverMajorVersion = is.read();
            int serverMinorVersion = is.read();
            int serverPatchVersion = is.read();

            if (preludeMajorVersion < 0 || preludeMinorVersion < 0 || preludePatchVersion < 0 || serverMajorVersion < 0 || serverMinorVersion < 0 || serverPatchVersion < 0)
                throw new InvalidPreludePacketException("Constructed SERVER_HANDSHAKE_PACKET is invalid!");

            this.preludeMajorVersion = preludeMajorVersion;
            this.preludeMinorVersion = preludeMinorVersion;
            this.preludePatchVersion = preludePatchVersion;
            this.serverMajorVersion = serverMajorVersion;
            this.serverMinorVersion = serverMinorVersion;
            this.serverPatchVersion = serverPatchVersion;
        } catch (InvalidPreludePacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPreludePacketException("Failed to parse SERVER_HANDSHAKE_PACKET!", e);
        }
    }

    @Override
    public void processSelf(PreludeS2CPacketHandler handler) {
        handler.handleServerHandshake(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerHandshakePreludeS2CPacket)) return false;
        ServerHandshakePreludeS2CPacket that = (ServerHandshakePreludeS2CPacket) o;
        return preludeMajorVersion == that.preludeMajorVersion && preludeMinorVersion == that.preludeMinorVersion && preludePatchVersion == that.preludePatchVersion && serverMajorVersion == that.serverMajorVersion && serverMinorVersion == that.serverMinorVersion && serverPatchVersion == that.serverPatchVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int preludeMajorVersion = -1;
        private int preludeMinorVersion = -1;
        private int preludePatchVersion = -1;

        private int serverMajorVersion = -1;
        private int serverMinorVersion = -1;
        private int serverPatchVersion = -1;

        private Builder() {}

        public Builder preludeMajorVersion(int preludeMajorVersion) {
            this.preludeMajorVersion = (int) preludeMajorVersion;
            return this;
        }

        public Builder preludeMinorVersion(int preludeMinorVersion) {
            this.preludeMinorVersion = (int) preludeMinorVersion;
            return this;
        }

        public Builder preludePatchVersion(int preludePatchVersion) {
            this.preludePatchVersion = (int) preludePatchVersion;
            return this;
        }

        public Builder serverMajorVersion(int serverMajorVersion) {
            this.serverMajorVersion = (int) serverMajorVersion;
            return this;
        }

        public Builder serverMinorVersion(int serverMinorVersion) {
            this.serverMinorVersion = (int) serverMinorVersion;
            return this;
        }

        public Builder serverPatchVersion(int serverPatchVersion) {
            this.serverPatchVersion = (int) serverPatchVersion;
            return this;
        }

        public ServerHandshakePreludeS2CPacket build() {
            if (preludeMajorVersion == -1 || preludeMinorVersion == -1 || preludePatchVersion == -1 || serverMajorVersion == -1 || serverMinorVersion == -1 || serverPatchVersion == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new ServerHandshakePreludeS2CPacket(preludeMajorVersion, preludeMinorVersion, preludePatchVersion, serverMajorVersion, serverMinorVersion, serverPatchVersion);
        }
    }

    public int getPreludeMajorVersion() {
        return preludeMajorVersion;
    }

    public int getPreludeMinorVersion() {
        return preludeMinorVersion;
    }

    public int getPreludePatchVersion() {
        return preludePatchVersion;
    }

    public int getServerMajorVersion() {
        return serverMajorVersion;
    }

    public int getServerMinorVersion() {
        return serverMinorVersion;
    }

    public int getServerPatchVersion() {
        return serverPatchVersion;
    }
}
