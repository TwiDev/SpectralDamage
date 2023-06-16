package ch.twidev.spectral.packet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IPackets {

    int spawnHologram(Player player, Location location, double damage, Plugin plugin);

}
