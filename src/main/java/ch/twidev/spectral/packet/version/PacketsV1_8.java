package ch.twidev.spectral.packet.version;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.packet.IPackets;
import ch.twidev.spectral.utils.StringUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

public class PacketsV1_8 implements IPackets {
    @Override
    public int spawnHologram(Player player, Location location, double damage, Plugin plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(SpectralDamage.get(), () -> {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

            EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ()) {{
                setCustomName(StringUtils.getDamageFormat(damage));
                setCustomNameVisible(true);
            }};

            int armorStandID = armorStand.getId();

            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
            PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStandID, armorStand.getDataWatcher(), true);
            connection.sendPacket(packet);
            connection.sendPacket(metadata);
        });

        return -1;

    }

    @Override
    public String getVersionName() {
        return "V1.8_R3";
    }
}
