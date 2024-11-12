package ch.twidev.spectraldamage.nms.v1_21_R2;

import ch.twidev.spectraldamage.nms.common.IPackets;
import ch.twidev.spectraldamage.nms.rgb.ColorUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

public class PacketsV1_21_R2 implements IPackets {

    @Override
    public Entity spawnHologram(Player player, Location location, double damage, String format, Plugin plugin) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().f;
        World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

        EntityArmorStand armorStand = this.createEntity(location, format, false);

        int armorStandID = armorStand.ar();

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand, 0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStandID, armorStand.au().b());

        connection.b(packet);
        connection.b(metadata);

        return armorStand.getBukkitEntity();
    }

    @Override
    public void relEntityMove(Player player, int entityId, double y, double dy, boolean b3) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId, (short) 0, (short) (((y + dy) * 32 - y * 32) * 128), (short) 0, b3
        );
        ((CraftPlayer) player).getHandle().f.b(packetPlayOutRelEntityMove);

    }

    @Override
    public org.bukkit.entity.Entity spawnHologram(Location location, double damage, String format, Plugin plugin, boolean gravity) {
        EntityArmorStand armorStand = this.createEntity(location.add(0,1,0), format, gravity);
        Class<?> nmsStandClass = net.minecraft.world.entity.Entity.class;
      /*  try {
            Field noClip = nmsStandClass.getDeclaredField("ae");
            noClip.setAccessible(true);
            noClip.setBoolean(armorStand, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        armorStand.cU().addFreshEntity(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return armorStand.getBukkitEntity();
    }

    @Override
    public void destroyEntity(Entity entity) {

    }

    public EntityArmorStand createEntity(Location location, String format, boolean gravity) {
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ());
        armorStand.t(true);
        //armorStand.setNoGravity(!gravity);
        armorStand.f(true);
        armorStand.b(IChatBaseComponent.a(format));
        armorStand.k(true);
        armorStand.p(true);
        armorStand.e(!gravity);
        armorStand.o(true);

        return armorStand;
    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        ((CraftPlayer) player).getHandle().f.b(new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public String getVersionName() {
        return "V1.21_R2";
    }

    @Override
    public String getColoredString(String s) {
        return ColorUtils.colorize(s);
    }

}
