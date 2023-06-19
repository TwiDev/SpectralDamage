package ch.twidev.spectraldamage.nms.v1_20_R1;

import ch.twidev.spectraldamage.nms.common.IPackets;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class PacketsV1_20_R1 implements IPackets {
    @Override
    public Entity spawnHologram(Player player, Location location, double damage, String format, Plugin plugin) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().c;
        World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

        EntityArmorStand armorStand = this.createEntity(location, format);

        int armorStandID = armorStand.af();

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStandID, armorStand.aj().b());

        connection.a(packet);
        connection.a(metadata);

        return armorStand.getBukkitEntity();
    }

    @Override
    public void relEntityMove(Player player, int entityId, double y, double dy, boolean b3) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId, (short) 0, (short) (((y + dy) * 32 - y * 32) * 128), (short) 0, b3
        );
        ((CraftPlayer) player).getHandle().c.a(packetPlayOutRelEntityMove);

    }

    @Override
    public org.bukkit.entity.Entity spawnHologram(Location location, double damage, String format, Plugin plugin) {
        EntityArmorStand armorStand = this.createEntity(location, format);
        Class<?> nmsStandClass = net.minecraft.world.entity.Entity.class;
        try {
            Field noClip = nmsStandClass.getDeclaredField("ae");
            noClip.setAccessible(true);
            noClip.setBoolean(armorStand, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        armorStand.dI().addFreshEntity(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return armorStand.getBukkitEntity();
    }

    @Override
    public void destroyEntity(Entity entity) {

    }

    public EntityArmorStand createEntity(Location location, String format) {
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ());
        armorStand.a(true);
        //armorStand.setNoGravity(false);
        armorStand.j(true);
        armorStand.b(IChatBaseComponent.a(format));
        armorStand.n(true);

        return armorStand;
    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        ((CraftPlayer) player).getHandle().c.a(new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public String getVersionName() {
        return "V1.20_R1";
    }
}
