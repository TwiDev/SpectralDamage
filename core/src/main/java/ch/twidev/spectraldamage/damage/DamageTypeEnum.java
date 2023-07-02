package ch.twidev.spectraldamage.damage;

import ch.twidev.spectraldamage.api.DamageTypeFactory;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.utils.BooleanCallback;
import ch.twidev.spectraldamage.utils.DamageUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public enum DamageTypeEnum implements DamageTypeFactory {

    NORMAL(null, false, ConfigVars.HOLOGRAM_DAMAGE_FORMAT, null),
    FIRE((player, cause) ->
               cause == DamageCause.FIRE
            || cause == DamageCause.FIRE_TICK
            || cause == DamageCause.LAVA
    , true, ConfigVars.HOLOGRAM_FIRE_DAMAGE_FORMAT, ConfigVars.DETECT_FIRE_DAMAGE),
    POISON((player, cause) -> cause == DamageCause.POISON,true, ConfigVars.HOLOGRAM_POISON_DAMAGE_FORMAT, ConfigVars.DETECT_POISON_DAMAGE),
    CRITICAL((player, event) -> player != null && DamageUtility.isCritical(player), false, ConfigVars.HOLOGRAM_CRITICAL_DAMAGE_FORMAT, ConfigVars.DETECT_CRITICAL_DAMAGE);

    private final BooleanCallback<Player, DamageCause> checker;

    private final boolean isNatural;

    private final ConfigVars configFormat, detectConfig;

    DamageTypeEnum(BooleanCallback<Player, DamageCause> checker, boolean isNatural, ConfigVars configFormat, ConfigVars detectConfig) {
        this.checker = checker;
        this.isNatural = isNatural;
        this.configFormat = configFormat;
        this.detectConfig = detectConfig;
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

    public BooleanCallback<Player, DamageCause> getChecker() {
        return checker;
    }

    public static DamageTypeEnum checkDamage(Player damager, DamageCause damageCause, boolean natural) {
        DamageTypeEnum damageType = DamageTypeEnum.NORMAL;
        
        for (DamageTypeEnum value : values()) {
            if(value == CRITICAL || value.isNatural() != natural) continue;

            if(value.getChecker().run(damager, damageCause)) {
                damageType = value;
                break;
            }
        }

        if(damageType == NORMAL && DamageTypeEnum.CRITICAL.getChecker().run(damager, damageCause)) {
            damageType = CRITICAL;
        }

        return damageType;
    }
}
