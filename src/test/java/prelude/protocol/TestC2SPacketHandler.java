package prelude.protocol;

import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;

public class TestC2SPacketHandler extends C2SPacketHandler {
    public void handleClientHandshake(ClientHandshakePacket packet) {
        System.out.println("Client handshake received");
    }

    public void handleEquipOffhand(EquipOffhandPacket packet) {
        System.out.println("Equip offhand received");
    }
}
