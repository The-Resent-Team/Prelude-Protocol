package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestUpdateOffhandPacket {
    @Test
    public void testUpdateOffhandPacket() throws IOException {
        UpdateOffhandPacket packet = UpdateOffhandPacket.builder()
                .canClientDisregardThis(false)
                .serializedItem("ItemStack{DISPENSER x 35}")
                .build();

        byte[] bytes = packet.toBytes();
        UpdateOffhandPacket deserializedPacket = UpdateOffhandPacket.from(new ByteArrayInputStream(bytes));

        Assertions.assertEquals(packet, deserializedPacket);
    }
}
