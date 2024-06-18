package prelude.protocol;

import prelude.protocol.packets.s2c.*;

/*
* Extended by ResentS2CPacketHandler on the client
* */
public abstract class S2CPacketHandler {
    public abstract void handleServerHandshake(ServerHandshakePacket packet);

    public abstract void handleModStatus(ModStatusPacket packet);

    public abstract void handleRespawnAnchorUpdate(RespawnAnchorUpdatePacket packet);

    public abstract void handleServerTps(ServerTpsPacket packet);

    public abstract void handleOffhandUpdate(UpdateOffhandPacket packet);

    public abstract void handleTotemUsed(TotemUsedPacket packet);

    public abstract void handleWaypoints(WaypointsPacket packet);
}
