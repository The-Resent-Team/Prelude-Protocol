package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import prelude.protocol.packets.s2c.ModStatusPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestModStatusPacket {
    @Test
    public void testModStatusPacket() throws IOException {
        ModStatusPacket packet = ModStatusPacket.builder()
                .modIdentifier("offhand_support")
                .modStatus(ModStatusPacket.ModStatus.SUPPORTED)
                .build();

        byte[] bytes = packet.toBytes();
        ModStatusPacket deserializedPacket = ModStatusPacket.from(new ByteArrayInputStream(bytes));

        Assertions.assertEquals(packet, deserializedPacket);
    }
}
