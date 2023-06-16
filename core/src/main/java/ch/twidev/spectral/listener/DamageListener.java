package ch.twidev.spectral.listener;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.packet.PacketFactory;
import ch.twidev.spectral.tasks.AsyncDestroyTask;
import ch.twidev.spectral.tasks.AsyncHologramTask;
import ch.twidev.spectral.utils.StringUtils;
import org.bukkit.Location;
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

            Player damager = (Player) event.getDamager();
            Entity target = event.getEntity();

            double offsetX = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_X).asDouble();
            double offsetY = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Y).asDouble();
            double offsetZ = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Z).asDouble();

            Location targetLocation = target.getLocation().add(2*offsetX*RANDOM.nextDouble() - offsetX, offsetY, 2*offsetZ*RANDOM.nextDouble() - offsetZ);

            // Create armor stand NMS entity

            int armorStandId = PacketFactory.get().spawnHologram(damager, targetLocation.add(2*offsetX*RANDOM.nextDouble() - 1, offsetY, 2*offsetZ*RANDOM.nextDouble() - 1), event.getDamage(), StringUtils.getDamageFormat(event.getDamage()), SpectralDamage.get());

            if(ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean()) {
                AsyncHologramTask.createHologramTask(damager, armorStandId, targetLocation);
            }else{
                AsyncDestroyTask.createDestroyingTask(damager, armorStandId);
            }


        }
    }

}
