package ch.twidev.spectraldamage.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class DamageUtility {

    /**
     * Determine if the direct hit was a critical hit
     *
     * @param damager - The damaging player
     */
    @SuppressWarnings("deprecation")
    public static boolean isCritical(Entity damager) {
        if(!(damager instanceof Player)) return false;
        Player player = (Player) damager;
        
        return /*damager.getAttackCooldown() > 0.9F &&*/ damager.getFallDistance() > 0.0F
                && !damager.isOnGround() && !damager.getLocation().getBlock().isLiquid() && player.getActivePotionEffects().stream()
                .noneMatch(o -> o.getType().equals(PotionEffectType.BLINDNESS))
                && damager.getVehicle() == null && !player.isSprinting();
    }

}
