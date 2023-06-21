package ch.twidev.spectral.listener;

import ch.twidev.spectral.SpectralDamagePlugin;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.tasks.AsyncDestroyTask;
import ch.twidev.spectral.tasks.AsyncHologramTask;
import ch.twidev.spectral.tasks.TaskType;
import ch.twidev.spectral.utils.DamageUtility;
import ch.twidev.spectral.utils.StringUtils;
import ch.twidev.spectraldamage.api.SpectralDamage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

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

            Location targetLocation = target.getLocation().add(2*offsetX*RANDOM.nextDouble() - offsetX, offsetY - 0.2d, 2*offsetZ*RANDOM.nextDouble() - offsetZ);

            boolean isCritical = DamageUtility.isCritical(damager);
            // Create armor stand NMS entity
            boolean fallingAnimation = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean();

            if(TaskType.check() == TaskType.WORLD){
                /*Entity armorStand = SpectralDamagePlugin.get().getPacketManager().spawnHologram(targetLocation.add(2*offsetX*RANDOM.nextDouble() - 1, offsetY, 2*offsetZ*RANDOM.nextDouble() - 1), event.getDamage(), StringUtils.getDamageFormat(event.getDamage(), isCritical), SpectralDamagePlugin.get(), fallingAnimation);
                ArmorStand entityArmorStand = (ArmorStand) armorStand;
                entityArmorStand.setGravity(fallingAnimation);

                if(fallingAnimation) armorStand.setVelocity(new Vector(0, ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_INITIAL_SPEED).asDouble()/1.8d, 0));

                AsyncDestroyTask.createDestroyingTask(damager, armorStand);*/
                SpectralDamage.getInstance().spawnDamageIndicator(targetLocation, isCritical, Math.toIntExact(Math.round(event.getDamage())), fallingAnimation);
                return;
            }

            if(SpectralDamagePlugin.PLAYER_VISIBILITY.contains(damager)) return;
            SpectralDamage.getInstance().spawnDamageIndicator(damager, targetLocation, isCritical, Math.toIntExact(Math.round(event.getDamage())), fallingAnimation);

           /* Entity armorStand = SpectralDamagePlugin.get().getPacketManager().spawnHologram(damager, targetLocation.add(2*offsetX*RANDOM.nextDouble() - 1, offsetY, 2*offsetZ*RANDOM.nextDouble() - 1), event.getDamage(), StringUtils.getDamageFormat(event.getDamage(), isCritical), SpectralDamagePlugin.get());

            if(ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean()) {
                AsyncHologramTask.createHologramTask(damager, armorStand, targetLocation);
            }else{
                AsyncDestroyTask.createDestroyingTask(damager, armorStand);
            }*/



        }
    }

}
