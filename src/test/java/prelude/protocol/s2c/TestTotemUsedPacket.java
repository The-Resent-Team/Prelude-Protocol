package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.IOException;

import static prelude.protocol.S2CPacket.TOTEM_USED_ID;

@Testable
public class TestTotemUsedPacket {
    @Test
    public void testTotemUsedPacket() throws IOException {
        Assertions.assertEquals(TOTEM_USED_ID, new TotemUsedPacket().toBytes()[0]);
    }
}
