package prelude.protocol;

import prelude.protocol.packets.s2c.*;

/*
* Extended by ResentS2CPacketHandler on the client
* */
public abstract class S2CPacketHandler extends PacketHandler {
    public void handleServerHandshake(ServerHandshakePacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleModStatus(ModStatusPacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleRespawnAnchorUpdate(RespawnAnchorUpdatePacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleServerTps(ServerTpsPacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleOffhandUpdate(UpdateOffhandPacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleTotemUsed(TotemUsedPacket packet) {
        throw new UnsupportedOperationException();
    }

    public void handleWaypoints(WaypointsPacket packet) {
        throw new UnsupportedOperationException();
    }
}
