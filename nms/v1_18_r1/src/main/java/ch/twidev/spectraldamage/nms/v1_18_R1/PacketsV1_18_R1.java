package ch.twidev.spectraldamage.nms.v1_18_R1;

import ch.twidev.spectraldamage.nms.common.IPackets;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketsV1_18_R1 implements IPackets {
    @Override
    public int spawnHologram(Player player, Location location, double damage, String format, Plugin plugin) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

        EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ());
        armorStand.a(true);
        armorStand.j(true);
        armorStand.a(new ChatMessage(format));
        armorStand.n(true);

        int armorStandID = armorStand.ae();

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        /*PacketPlayOutEntityMetadata metadata = null;
        try {
            Field field = armorStand.getClass().getField("bg");
            field.setAccessible(true);
            metadata = new PacketPlayOutEntityMetadata(armorStandID, (DataWatcher) field.get(armorStand), true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        connection.a(packet);
        /*if(metadata != null)
            connection.a(metadata);*/

        return armorStandID;
    }

    @Override
    public void relEntityMove(Player player, int entityId, double y, double dy, boolean b3) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId, (short) 0, (short) (((y + dy) * 32 - y * 32) * 128), (short) 0, b3
        );
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutRelEntityMove);

    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        ((CraftPlayer) player).getHandle().b.a(new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public String getVersionName() {
        return "V1.18_R2";
    }
}
