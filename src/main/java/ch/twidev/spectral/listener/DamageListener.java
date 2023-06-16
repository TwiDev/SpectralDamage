package ch.twidev.spectral.listener;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.packet.PacketFactory;
import ch.twidev.spectral.tasks.HologramTask;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class DamageListener implements Listener {

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(event.getEntity() instanceof Player && !ConfigManager.CONFIG_VALUES.get(ConfigVars.PLAYER_AFFECTED).asBoolean()) return;
            if(event.getEntity() instanceof Creature && !ConfigManager.CONFIG_VALUES.get(ConfigVars.ENTITIES_AFFECTED).asBoolean()) return;

            System.out.println("1");
            Player damager = (Player) event.getDamager();
            Entity target = event.getEntity();
            Location targetLocation = target.getLocation();

            double offsetX = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_X).asDouble();
            double offsetY = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Y).asDouble();
            double offsetZ = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Z).asDouble();

            // Create armor stand NMS entity

            PacketFactory.get().spawnHologram(damager, targetLocation, event.getDamage(), SpectralDamage.get());

        /*    EntityArmorStand armorStand = new EntityArmorStand(mcWorld) {{
                setLocation(
                        targetLocation.getX() + (2*offsetX*RANDOM.nextDouble() - 1),
                        targetLocation.getY() + offsetY,
                        targetLocation.getZ() + (2*offsetZ*RANDOM.nextDouble() - 1),
                        targetLocation.getYaw(),
                        targetLocation.getPitch());
                setInvisible(false);
                setSmall(true);
                setGravity(false);
                setCustomName(ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_DAMAGE_FORMAT).asString().replaceAll("%damage%", String.valueOf(Math.round(event.getDamage()))));
                setCustomNameVisible(true);
            }};*/

            System.out.println("2");
            // Send hologram to the damager
            /*int armorStandId = armorStand.getBukkitEntity().getEntityId();
            connection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
            connection.sendPacket(new PacketPlayOutEntityMetadata(armorStandId, armorStand.getDataWatcher(), true));*/
            System.out.println("3");
           /* if(ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean()) {
                HologramTask.createHologramTask(damager, armorStand, armorStand.getBukkitEntity().getLocation());
                System.out.println("4");
            }else{
                System.out.println("5");
                Bukkit.getScheduler().runTaskLaterAsynchronously(SpectralDamage.get(), () -> {
                    if(damager.isOnline()) {
                        connection.sendPacket(new PacketPlayOutEntityDestroy(armorStand.getBukkitEntity().getEntityId()));
                    }
                }, ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt());
            }*/


        }
    }

}
