package prelude.protocol.s2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Testable
public class TestRespawnAnchorUpdatePacket {
    @Test
    public void testRespawnAnchorUpdatePacket() throws IOException {
        RespawnAnchorUpdatePacket packet = RespawnAnchorUpdatePacket.builder()
                .charge(4)
                .x(-203)
                .y(23)
                .z(2100291722)
                .build();

        byte[] bytes = packet.toBytes();
        RespawnAnchorUpdatePacket deserializedPacket =
                RespawnAnchorUpdatePacket.fromBytes(new ByteArrayInputStream(bytes));

        Assertions.assertEquals(packet, deserializedPacket);
    }
}
