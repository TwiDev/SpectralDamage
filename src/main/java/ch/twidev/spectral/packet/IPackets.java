package ch.twidev.spectral.packet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IPackets {

    int spawnHologram(Player player, Location location, double damage, Plugin plugin);

    void relEntityMove(Player player, int entityId, byte x, byte y, byte z, boolean b3);

    void destroyEntity(Player player, int entityId);

    String getVersionName();

}
