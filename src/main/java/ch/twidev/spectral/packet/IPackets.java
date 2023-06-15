package ch.twidev.spectral.packet;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public interface IPackets {

    void spawnPacket(int entityID, Location location, Plugin plugin);

}
