package prelude.protocol.packets.s2c;

import prelude.protocol.InvalidPacketException;
import prelude.protocol.S2CPacket;
import prelude.protocol.S2CPacketHandler;
import prelude.protocol.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
* The Server TPS is stored as double in the actual
* bukkit server code, and in this packet we assume
* that the characteristic of the TPS does not
* exceed 127 (it really shouldn't).
* We also make the assumption that the mantissa of
* the tps does not need more than 16 bits / 2 bytes
* to encode. These assumptions are made because
* 1) they are unlikely to be broken in actual prod
* 2) they cut down on the payload size (encoding a
* raw 8 byte double is far worse than encoding 3 bytes)
* 3) these assumptions make it easier for the client
* */
public class ServerTpsPacket extends S2CPacket {
    // we will just assume that the characteristic does reach over 127 (max value of a java byte)
    private byte characteristic;
    private short mantissa;

    public ServerTpsPacket() {}

    private ServerTpsPacket(byte characteristic, short mantissa) {
        this.characteristic = characteristic;
        this.mantissa = mantissa;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bao.write(SERVER_TPS_ID);

        bao.write(characteristic);
        StreamUtils.writeShort(mantissa, bao);

        return bao.toByteArray();
    }

    @Override
    public void loadData(InputStream is) throws InvalidPacketException {
        try {
            if (is.read() != SERVER_TPS_ID)
                throw new InvalidPacketException("Packet ID doesn't match with SERVER_TPS_ID (%id%)!"
                        .replace("%id%", SERVER_TPS_ID + ""));

            byte characteristic = (byte) is.read();
            short mantissa = (short) StreamUtils.readShort(is);

            // how the actual hell would you get negative tps
            if (characteristic < 0 || mantissa < 0)
                throw new InvalidPacketException("Constructed SERVER_TPS_PACKET is invalid!");

            this.characteristic = characteristic;
            this.mantissa = mantissa;
        } catch (InvalidPacketException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to parse SERVER_TPS_PACKET!", e);
        }
    }

    @Override
    public void processSelf(S2CPacketHandler handler) {
        handler.handleServerTps(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerTpsPacket that)) return false;
        return characteristic == that.characteristic && mantissa == that.mantissa;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private byte characteristic = -1;
        private short mantissa = -1;

        public Builder characteristic(int characteristic) {
            this.characteristic = (byte) characteristic;
            return this;
        }

        public Builder mantissa(int mantissa) {
            this.mantissa = (short) mantissa;
            return this;
        }

        public ServerTpsPacket build() {
            if (characteristic == -1 || mantissa == -1)
                throw new IllegalStateException("Not all required fields are set!");

            return new ServerTpsPacket(characteristic, mantissa);
        }
    }

    public byte getCharacteristic() {
        return characteristic;
    }

    public short getMantissa() {
        return mantissa;
    }
}
