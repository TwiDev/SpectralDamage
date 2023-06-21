package ch.twidev.spectraldamage.listener;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.tasks.TaskType;
import ch.twidev.spectraldamage.utils.DamageUtility;
import ch.twidev.spectraldamage.api.SpectralDamage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
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
        if(event.getEntity() instanceof ArmorStand) return;

        if(event.getDamager() instanceof Player) {
            if(event.getEntity() instanceof Player && !ConfigManager.CONFIG_VALUES.get(ConfigVars.PLAYER_AFFECTED).asBoolean()) return;
            if(event.getEntity() instanceof Creature && !ConfigManager.CONFIG_VALUES.get(ConfigVars.ENTITIES_AFFECTED).asBoolean()) return;

            Player damager = (Player) event.getDamager();
            Entity target = event.getEntity();

            double offsetX = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_X).asDouble();
            double offsetY = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Y).asDouble();
            double offsetZ = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Z).asDouble();

            Location targetLocation = target.getLocation().add(2*offsetX*RANDOM.nextDouble() - offsetX, offsetY - 0.2d, 2*offsetZ*RANDOM.nextDouble() - offsetZ);

            boolean isCritical = DamageUtility.isCritical(damager);
            // Create armor stand NMS entity
            boolean fallingAnimation = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean();

            if(TaskType.check() == TaskType.WORLD){
                SpectralDamage.getInstance().spawnDamageIndicator(targetLocation, isCritical, Math.toIntExact(Math.round(event.getDamage())), fallingAnimation);
                return;
            }

            if(SpectralDamagePlugin.PLAYER_VISIBILITY.contains(damager)) return;
            SpectralDamage.getInstance().spawnDamageIndicator(damager, targetLocation, isCritical, Math.toIntExact(Math.round(event.getDamage())), fallingAnimation);
        }
    }
}
