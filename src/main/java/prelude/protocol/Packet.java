package prelude.protocol;

import java.io.IOException;
import java.io.InputStream;

public abstract class Packet {
    public abstract byte[] toBytes() throws IOException;
    public abstract boolean equals(Object packet);
    public abstract Packet loadData(InputStream is);
}
