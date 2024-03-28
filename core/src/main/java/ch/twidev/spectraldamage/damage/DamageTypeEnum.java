package ch.twidev.spectraldamage.damage;

import ch.twidev.spectraldamage.api.DamageTypeFactory;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.utils.BooleanCallback;
import ch.twidev.spectraldamage.utils.DamageUtility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public enum DamageTypeEnum implements DamageTypeFactory {

    NORMAL((player,cause) -> cause == DamageCause.ENTITY_ATTACK, false, ConfigVars.HOLOGRAM_DAMAGE_FORMAT, null),
    FIRE((player, cause) ->
               cause == DamageCause.FIRE
            || cause == DamageCause.FIRE_TICK
            || cause == DamageCause.LAVA
    , true, ConfigVars.HOLOGRAM_FIRE_DAMAGE_FORMAT, ConfigVars.DETECT_FIRE_DAMAGE),
    POISON((player, cause) -> cause == DamageCause.POISON,true, ConfigVars.HOLOGRAM_POISON_DAMAGE_FORMAT, ConfigVars.DETECT_POISON_DAMAGE),
    MAGIC((player,cause) -> cause == DamageCause.MAGIC,false, true, ConfigVars.HOLOGRAM_MAGIC_DAMAGE_FORMAT,ConfigVars.DETECT_POTION_DAMAGE),
    PROJECTILE((player,cause) -> cause == DamageCause.PROJECTILE,false, true, ConfigVars.HOLOGRAM_PROJECTILE_DAMAGE_FORMAT,ConfigVars.DETECT_PROJECTILE_DAMAGE),
    FALL(((player, cause) ->
               cause == DamageCause.FALL
            || cause == DamageCause.FALLING_BLOCK), true, ConfigVars.HOLOGRAM_FALL_DAMAGE_FORMAT, ConfigVars.DETECT_POISON_DAMAGE),
    CRITICAL((player, event) -> DamageUtility.isCritical(player), false, ConfigVars.HOLOGRAM_CRITICAL_DAMAGE_FORMAT, ConfigVars.DETECT_CRITICAL_DAMAGE);

    private final BooleanCallback<Entity, DamageCause> checker;

    private final boolean isNatural;
    private boolean isProjectile = false;

    private final ConfigVars configFormat, detectConfig;

    DamageTypeEnum(BooleanCallback<Entity, DamageCause> checker, boolean isNatural, ConfigVars configFormat, ConfigVars detectConfig) {
        this.checker = checker;
        this.isNatural = isNatural;
        this.configFormat = configFormat;
        this.detectConfig = detectConfig;
    }

    DamageTypeEnum(BooleanCallback<Entity, DamageCause> checker, boolean isNatural, boolean isProjectile, ConfigVars configFormat, ConfigVars detectConfig) {
        this.checker = checker;
        this.isNatural = isNatural;
        this.isProjectile = isProjectile;
        this.configFormat = configFormat;
        this.detectConfig = detectConfig;
    }

    public boolean isProjectile() {
        return isProjectile;
    }

    public ConfigVars getDetectConfig() {
        return detectConfig;
    }

    public boolean isNatural() {
        return isNatural;
    }

    public ConfigVars getConfigFormat() {
        return configFormat;
    }

    @Override
    public String getFormat(double damage) {
        return ConfigManager.CONFIG_VALUES.get(configFormat).asString().replaceAll("&","§").replaceAll("%damage%", String.valueOf(Math.round(damage)));
    }

    public boolean check(Entity player, DamageCause damageCause) {
        return checker == null || checker.run(player, damageCause);
    }

    public static DamageTypeEnum checkDamage(Entity damager, DamageCause damageCause, boolean natural) {
        DamageTypeEnum damageType = DamageTypeEnum.NORMAL;
        
        for (DamageTypeEnum value : values()) {
            if(value == CRITICAL || value.isNatural() != natural) continue;

            if(value.check(damager, damageCause)) {
                damageType = value;
                break;
            }
        }

        if(damageType == NORMAL && DamageTypeEnum.CRITICAL.check(damager, damageCause)) {
            damageType = CRITICAL;
        }

        return damageType;
    }
}
