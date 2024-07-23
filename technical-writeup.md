# Prelude-Protocol Technical Stuff

## Getting some stuff out of the way
To not be confused when I say packet, let packet mean a plugin message sent on the `resent:prelude` channel with the payload being a `Lprelude/protocol/Packet#toBytes()[b`.
When we need to talk about Minecraft Protocol's packets, I will explictly say `Minecraft Protocol's packets`

## Client-Server Handshake
On joining a server, the client will listen for a `minecraft:register` or `REGISTER` plugin message that contains `resent:prelude` in the payload.
On receiving that packet, the client will send over a packet of type ClientHandshakeC2SPacket, and the server will store the info for later. 
The server will (probably since they registered the channel) respond by sending back a ServerHandshakeS2CPacket which the client stores the info to enable/disable certain features.
After the client stores the info without any errors, it will send to the server a ClientAcknowledgeServerHandshakeC2SPacket, and then the server will begin relaying needed information.

## Y < 0 && Y > 255
The reason Y > 255 needs specific handling is because if I just let the client send Minecraft Protocol's C08PacketPlayerBlockPlacement with a Y value greater than 255 (1.8 build limit)
many anticheats would not be prepared for this since it isnt in vanilla, and incompatibilies are to be avoided at all costs. Therefore, I use my own packet, AttemptPlaceInLegacyIllegalSpotsC2SPacket.
The prelude bukkit plugin will manually verify that the place is legal, and fire the bukkit event for a block being placed. It will then send a BlockChangeModernS2CPacket to update the block on the client
as the client does not try to predict results of placing in illegal spots (incompatabilies once again). The plugin will also send the Minecraft Protocol's update block packet, in order for anticheats 
to update the state (p sure viaver will drop the packet but better to be safe than sorry) and not false flag the player. Rendering on the client side is handled via an extended chunk system.

## World
// TODO i'm lazy + this is a bit mroe complicated to explain

## Offhanding
The client will send a EquipOffhandC2SPacket when the client attempts to offhand an item. The client will try to predict the results of this, and the plugin will verify that if the #getSlot is in an inventory, the player is in an inventory, and if it's in hotbar, it will verify that the current slot is the hotbar. It will fire the appropriate bukkit event, in order to not cause incompatibilies.
The plugin can also send an UpdateOffhandS2CPacket to update offhand, or send said packet to deny offhanding.

## QoL
TotemUsedS2CPacket is used to render the proper totem popping animation.

ServerTpsS2CPacket is used to accurately show the TPS.

ServerSyncRequestS2CPacket is used to sync a lot of things, and ClientSyncResponseC2SPacket is the client counterpart

SetWaypointsS2CPacket is useful for servers to set the waypoints on the client

ModStatusS2CPacket is used to inform if a mod is supported or is disabled on the server

## the end
idk if theres anything else
