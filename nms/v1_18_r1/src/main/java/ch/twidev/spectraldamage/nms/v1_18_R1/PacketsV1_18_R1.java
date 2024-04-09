package ch.twidev.spectraldamage.nms.v1_18_R1;

import ch.twidev.spectraldamage.nms.common.IPackets;
import ch.twidev.spectraldamage.nms.rgb.ColorUtils;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class PacketsV1_18_R1 implements IPackets {
    @Override
    public Entity spawnHologram(Player player, Location location, double damage, String format, Plugin plugin) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

        EntityArmorStand armorStand = createEntity(location, format,false);

        int armorStandID = armorStand.ae();

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStandID, armorStand.ai(), true);

        connection.a(packet);
        connection.a(metadata);

        return armorStand.getBukkitEntity();
    }

    @Override
    public void relEntityMove(Player player, int entityId, double y, double dy, boolean b3) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId, (short) 0, (short) (((y + dy) * 32 - y * 32) * 128), (short) 0, b3
        );
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutRelEntityMove);

    }

    @Override
    public org.bukkit.entity.Entity spawnHologram(Location location, double damage, String format, Plugin plugin, boolean gravity) {
        EntityArmorStand armorStand = this.createEntity(location, format, gravity);
        Class<?> nmsStandClass = net.minecraft.world.entity.Entity.class;
        try {
            Field noClip = nmsStandClass.getDeclaredField("Q");
            noClip.setAccessible(true);
            noClip.setBoolean(armorStand, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        armorStand.W().addFreshEntity(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return armorStand.getBukkitEntity();
    }

    @Override
    public void destroyEntity(Entity entity) {

    }

    public EntityArmorStand createEntity(Location location, String format, boolean gravity) {
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ());
        armorStand.a(true);
        armorStand.j(true);
        armorStand.a(new ChatMessage(format));
        armorStand.n(true);
        armorStand.e(!gravity);
        armorStand.t(true);

        return armorStand;
    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        ((CraftPlayer) player).getHandle().b.a(new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public String getVersionName() {
        return "V1.18_R2";
    }

    @Override
    public String getColoredString(String s) {
        return ColorUtils.colorize(s);
    }
}
