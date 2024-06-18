package prelude.protocol;

import prelude.protocol.packets.c2s.ClientHandshakePacket;
import prelude.protocol.packets.c2s.EquipOffhandPacket;

/*
* Extended by BukkitC2SPacketHandler on the plugin
* */
public abstract class C2SPacketHandler extends PacketHandler {
    public void handleClientHandshake(ClientHandshakePacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleEquipOffhand(EquipOffhandPacket packet) {
        throw new UnsupportedOperationException();
    }
}
