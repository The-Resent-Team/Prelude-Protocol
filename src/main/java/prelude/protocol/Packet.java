package prelude.protocol;

import java.io.IOException;
import java.io.InputStream;

public abstract class Packet<E> {
    public abstract byte[] toBytes() throws IOException;
    public abstract boolean equals(Object packet);
    public abstract void loadData(InputStream is) throws InvalidPacketException;
    public abstract void processSelf(E handler);
    public abstract UUID getRecipientUUID();
}
