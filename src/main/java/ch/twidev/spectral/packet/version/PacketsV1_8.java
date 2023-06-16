package ch.twidev.spectral.packet.version;

import ch.twidev.spectral.packet.IPackets;
import ch.twidev.spectral.utils.StringUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketsV1_8 implements IPackets {
    @Override
    public int spawnHologram(Player player, Location location, double damage, Plugin plugin) {

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();

        EntityArmorStand armorStand = new EntityArmorStand(mcWorld) {{
            setLocation(
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch());
            setInvisible(false);
            setSmall(true);
            setGravity(false);
            setCustomName(StringUtils.getDamageFormat(damage));
            setCustomNameVisible(true);
        }};

        int armorStandID = armorStand.getId();

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        connection.sendPacket(packet);
        connection.sendPacket(metadata);

        return armorStandID;

    }

}
