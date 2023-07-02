package ch.twidev.spectraldamage.listener;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.damage.DamageTypeEnum;
import ch.twidev.spectraldamage.tasks.TaskType;
import ch.twidev.spectraldamage.api.SpectralDamage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class DamageListener implements Listener {

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (checkEvent(event)) return;

        Entity entity = event.getEntity();
        DamageTypeEnum damageType = DamageTypeEnum.checkDamage(null, event.getCause(), true);
        if(!damageType.isNatural()) return;
        if (damageType.getDetectConfig() != null && !ConfigManager.CONFIG_VALUES.get(damageType.getDetectConfig()).asBoolean())
            return;
        if (TaskType.check() == TaskType.WORLD) {
            this.spawnToPlayer(null, entity.getLocation(), event.getDamage(), damageType);
        } else if (entity instanceof Player) {
            this.spawnToPlayer(((Player) entity).getPlayer(), entity.getLocation(), event.getDamage(), damageType);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof ArmorStand) return;

        if(event.getDamager() instanceof Player) {
            if(checkEvent(event)) return;

            Player damager = (Player) event.getDamager();
            Entity target = event.getEntity();

            DamageTypeEnum damageType = DamageTypeEnum.checkDamage(damager, event.getCause(), false);
            if(damageType.isNatural()) return;
            if(damageType.getDetectConfig() != null && !ConfigManager.CONFIG_VALUES.get(damageType.getDetectConfig()).asBoolean()) return;
            this.spawnToPlayer(damager, target.getLocation(), event.getDamage(), damageType);
        }
    }

    private boolean checkEvent(EntityDamageEvent e) {
        if(e.getEntity() instanceof ArmorStand) return true;

        if(e.getEntity() instanceof Player && !ConfigManager.CONFIG_VALUES.get(ConfigVars.PLAYER_AFFECTED).asBoolean()) return true;
        if(e.getEntity() instanceof Creature && !ConfigManager.CONFIG_VALUES.get(ConfigVars.ENTITIES_AFFECTED).asBoolean()) return true;

        return false;
    }

    // Create armor stand NMS entity
    private void spawnToPlayer(Player damager, Location location, double damage, DamageTypeEnum damageType) {
        double offsetX = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_X).asDouble();
        double offsetY = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Y).asDouble();
        double offsetZ = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Z).asDouble();

        Location targetLocation = location.add(2*offsetX*RANDOM.nextDouble() - offsetX, offsetY - 0.2d, 2*offsetZ*RANDOM.nextDouble() - offsetZ);

        boolean fallingAnimation = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean();

        if(TaskType.check() == TaskType.WORLD){
            SpectralDamage.getInstance().spawnDamageIndicator(targetLocation, damageType, Math.toIntExact(Math.round(damage)), fallingAnimation);
            return;
        }

        if(SpectralDamagePlugin.PLAYER_VISIBILITY.contains(damager)) return;
        SpectralDamage.getInstance().spawnDamageIndicator(damager, targetLocation, damageType, Math.toIntExact(Math.round(damage)), fallingAnimation);
    }
}
