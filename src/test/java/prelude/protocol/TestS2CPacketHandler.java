package prelude.protocol;

import prelude.protocol.packets.s2c.*;

public class TestS2CPacketHandler extends S2CPacketHandler {
    public void handleServerHandshake(ServerHandshakePacket packet) {
        System.out.println("Handshake received");
    }

    public void handleModStatus(ModStatusPacket packet) {
        System.out.println("Mod status received");
    }

    public void handleRespawnAnchorUpdate(RespawnAnchorUpdatePacket packet) {
        System.out.println("Respawn anchor update received");
    }

    public void handleServerTps(ServerTpsPacket packet) {
        System.out.println("Server tps received");
    }

    public void handleOffhandUpdate(UpdateOffhandPacket packet) {
        System.out.println("Update offhand received");
    }

    public void handleTotemUsed(TotemUsedPacket packet) {
        System.out.println("Totem used received");
    }

    public void handleWaypoints(WaypointsPacket packet) {
        System.out.println("Waypoints received");
    }
}
