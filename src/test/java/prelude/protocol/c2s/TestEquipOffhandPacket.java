package prelude.protocol.c2s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestEquipOffhandPacket {
    @Test
    public void testEquipOffhandPacket() throws IOException {
        EquipOffhandPacket packet = EquipOffhandPacket.builder()
                .slot((short) 25)
                .build();

        byte[] bytes = packet.toBytes();
        EquipOffhandPacket deserializedPacket =
                EquipOffhandPacket.from(new ByteArrayInputStream(bytes));

        Assertions.assertEquals(packet, deserializedPacket);

        ClientHandshakePacket handshakePacket = ClientHandshakePacket.from(new ByteArrayInputStream(bytes));
        Assertions.assertNull(handshakePacket);
    }
}
