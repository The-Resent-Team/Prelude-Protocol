The reason for no "deny" packet on the server side is quite simple
In Resent's implementation, Resent will NOT try to predict the results
Since making it predict results would break compatibility for a lot of
plugins, especially anticheats. Therefore, Resent waits for the server
to send an update packet.