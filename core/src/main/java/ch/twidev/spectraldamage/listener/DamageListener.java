package ch.twidev.spectraldamage.listener;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.api.SpectralDamage;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.damage.DamageTypeEnum;
import ch.twidev.spectraldamage.tasks.TaskType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class DamageListener implements Listener {

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (checkEvent(event)) return;

        Entity entity = event.getEntity();
        if(entity instanceof ArmorStand) return;
        if(!(entity instanceof LivingEntity)) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) return;

        DamageTypeEnum damageType = DamageTypeEnum.checkDamage(null, event.getCause(), true);
        if(!damageType.isNatural()) return;
        if (damageType.getDetectConfig() != null && !ConfigManager.CONFIG_VALUES.get(damageType.getDetectConfig()).asBoolean())
            return;
        if (TaskType.check() == TaskType.WORLD) {
            this.spawnToPlayer(null, entity.getLocation(), event.getFinalDamage(), damageType);
        } else if (entity instanceof Player) {
            this.spawnToPlayer(((Player) entity).getPlayer(), entity.getLocation(), event.getFinalDamage(), damageType);
        }
    }

    @EventHandler
    public void onArmorStandInteract(PlayerInteractAtEntityEvent e) {
        Entity target = e.getRightClicked();

        if (target == null) return;

        if (target instanceof ArmorStand) {
            if(target.hasMetadata("_spectraldamage")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof ArmorStand) return;

        if (checkEvent(event)) return;

        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        DamageTypeEnum damageType = DamageTypeEnum.checkDamage(damager, event.getCause(), false);
        if (damageType.isNatural()) return;
        Player playerDamager = null;
        if(damager instanceof Player) {
            playerDamager = (Player) damager;
        }else {
            if (damageType.isProjectile()) {
                Projectile projectile = null;

                if (damageType == DamageTypeEnum.MAGIC && damager instanceof ThrownPotion) {
                    ThrownPotion thrownPotion = (ThrownPotion) damager;
                    if(thrownPotion.getEffects().stream().anyMatch(potionEffect -> potionEffect.getType() == PotionEffectType.HARM)) {
                        projectile = thrownPotion;
                    }
                }else if(damageType == DamageTypeEnum.PROJECTILE && damager.getType().equals(EntityType.ARROW)) {
                    projectile = (Projectile) damager;
                }

                if(projectile != null && projectile.getShooter() instanceof Player) {
                    playerDamager = (Player) projectile.getShooter();
                }
            }
        }

        if (damageType.getDetectConfig() != null && !ConfigManager.CONFIG_VALUES.get(damageType.getDetectConfig()).asBoolean())
            return;

        if(playerDamager != null) {
            this.spawnToPlayer(playerDamager, target.getLocation(), event.getFinalDamage(), damageType);
        }
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent e) {
        if(checkEvent(e)) return;

        if(e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
            if(ConfigManager.CONFIG_VALUES.get(ConfigVars.DETECT_HEALTH_REGEN).asBoolean()) {
                this.spawnToPlayer(e.getEntity(), e.getEntity().getLocation(), e.getAmount(), DamageTypeEnum.HEALTH_REGEN);
            }
        }else{
            if(ConfigManager.CONFIG_VALUES.get(ConfigVars.DETECT_HEALTH_GAIN).asBoolean()) {
                this.spawnToPlayer(e.getEntity(), e.getEntity().getLocation(), e.getAmount(), DamageTypeEnum.HEALTH_GAIN);
            }
        }
    }

    private boolean checkEvent(EntityEvent e) {
        if(e.getEntity() instanceof ItemFrame) return true;
        if(e.getEntity() instanceof ArmorStand) return true;

        if(e.getEntity() instanceof Player && !ConfigManager.CONFIG_VALUES.get(ConfigVars.PLAYER_AFFECTED).asBoolean()) return true;
        if(e.getEntity() instanceof Creature && !ConfigManager.CONFIG_VALUES.get(ConfigVars.ENTITIES_AFFECTED).asBoolean()) return true;

        return false;
    }

    // Create armor stand NMS entity
    private void spawnToPlayer(Entity damager, Location location, double damage, DamageTypeEnum damageType) {
        if(damage < ConfigManager.CONFIG_VALUES.get(ConfigVars.DETECT_MINIMUM_DAMAGE).asInt()) return;

        if(ConfigManager.CONFIG_VALUES.get(ConfigVars.RESTRICT_BY_PERMISSION).asBoolean() && !damager.hasPermission("spectraldamage.show")) return;

        double offsetX = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_X).asDouble();
        double offsetY = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Y).asDouble();
        double offsetZ = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_OFFSET_Z).asDouble();

        Location targetLocation = location.add(2*offsetX*RANDOM.nextDouble() - offsetX, offsetY - 0.2d, 2*offsetZ*RANDOM.nextDouble() - offsetZ);

        boolean fallingAnimation = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_FALLING_ANIMATION).asBoolean();

        double scale = Math.pow(10, ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_DAMAGE_DECIMAL).asInt());
        double scaledDamage = Math.ceil(damage * scale) / scale;

        if(TaskType.check() == TaskType.WORLD){
            SpectralDamage.getInstance().spawnDamageIndicator(targetLocation, damageType, scaledDamage, false);
            return;
        }

        if(damager instanceof Player) {
            Player player = (Player) damager;

            if(SpectralDamagePlugin.PLAYER_VISIBILITY.containsKey(player) && SpectralDamagePlugin.PLAYER_VISIBILITY.get(player)) return;
            if(!ConfigManager.CONFIG_VALUES.get(ConfigVars.ENABLE_BY_DEFAULT).asBoolean() && (!SpectralDamagePlugin.PLAYER_VISIBILITY.containsKey(player) || SpectralDamagePlugin.PLAYER_VISIBILITY.get(player))) return;
            SpectralDamage.getInstance().spawnDamageIndicator(player, targetLocation, damageType, scaledDamage, fallingAnimation);
        }

    }
}
