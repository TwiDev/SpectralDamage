package ch.twidev.spectraldamage.nms.v1_16_R3;

import ch.twidev.spectraldamage.nms.common.IPackets;
import ch.twidev.spectraldamage.nms.rgb.ColorUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import net.minecraft.server.v1_16_R3.*;

import java.lang.reflect.Field;

public class PacketsV1_16_R3 implements IPackets {
    @Override
    public Entity spawnHologram(Player player, Location location, double damage, String format, Plugin plugin) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

        EntityArmorStand armorStand = this.createEntity(location,format,false);

        int armorStandID = armorStand.getId();

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStandID, armorStand.getDataWatcher(), true);
        connection.sendPacket(packet);
        connection.sendPacket(metadata);

        return armorStand.getBukkitEntity();
    }

    @Override
    public void relEntityMove(Player player, int entityId, double y, double dy, boolean b3) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId, (short) 0, (short) (((y + dy) * 32 - y * 32) * 128), (short) 0, b3
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutRelEntityMove);

    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public org.bukkit.entity.Entity spawnHologram(Location location, double damage, String format, Plugin plugin, boolean gravity) {
        EntityArmorStand armorStand = this.createEntity(location, format, gravity);
        Class<?> nmsStandClass = net.minecraft.server.v1_16_R3.Entity.class;
        try {
            Field noClip = nmsStandClass.getDeclaredField("noclip");
            noClip.setAccessible(true);
            noClip.setBoolean(armorStand, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        armorStand.world.addEntity(armorStand);
        return armorStand.getBukkitEntity();
    }

    @Override
    public void destroyEntity(Entity entity) {
        ((CraftWorld) entity.getWorld()).getHandle().removeEntity(((CraftEntity) entity).getHandle());
    }

    public EntityArmorStand createEntity(Location location, String format, boolean gravity) {
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ());
        armorStand.setSmall(true);
        armorStand.setNoGravity(!gravity);
        armorStand.setInvisible(true);
        armorStand.setCustomName(new ChatMessage(format));
        armorStand.setCustomNameVisible(true);
        armorStand.setMarker(true);

        return armorStand;
    }

    @Override
    public String getVersionName() {
        return "V1.16_R3";
    }

    @Override
    public String getColoredString(String s) {
        return ColorUtils.colorize(s);
    }
}
