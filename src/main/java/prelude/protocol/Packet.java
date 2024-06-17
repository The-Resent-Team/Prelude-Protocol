package prelude.protocol;

import java.io.IOException;

public abstract class Packet {
    public abstract byte[] toBytes() throws IOException;
    public abstract boolean equals(Object packet);
}
