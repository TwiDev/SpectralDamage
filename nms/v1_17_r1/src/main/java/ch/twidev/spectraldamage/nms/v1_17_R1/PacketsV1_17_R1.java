package ch.twidev.spectraldamage.nms.v1_17_R1;

import ch.twidev.spectraldamage.nms.common.IPackets;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketsV1_17_R1 implements IPackets {
    @Override
    public int spawnHologram(Player player, Location location, double damage, String format, Plugin plugin) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        World mcWorld = ((CraftWorld) player.getWorld()).getHandle();

        EntityArmorStand armorStand = new EntityArmorStand(mcWorld, location.getX(), location.getY(), location.getZ());
        armorStand.setSmall(true);
        armorStand.setNoGravity(true);
        armorStand.setInvisible(true);
        armorStand.setCustomName(new ChatMessage(format));
        armorStand.setCustomNameVisible(true);

        int armorStandID = armorStand.getId();

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStandID, armorStand.getDataWatcher(), true);
        connection.sendPacket(packet);
        connection.sendPacket(metadata);

        return armorStandID;
    }

    @Override
    public void relEntityMove(Player player, int entityId, byte x, byte y, byte z, boolean b3) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId, x, y, z, b3
        );
        ((CraftPlayer) player).getHandle().b.sendPacket(packetPlayOutRelEntityMove);

    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        ((CraftPlayer) player).getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public String getVersionName() {
        return "V1.8_R3";
    }
}
