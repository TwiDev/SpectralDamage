package ch.twidev.spectraldamage.utils;

import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;

public class StringUtils {

    public static String getDamageFormat(double damage, boolean isCritical){
        return getRawDamageFormat(isCritical).replaceAll("&","§").replaceAll("%damage%", String.valueOf(Math.round(damage)));
    }

    public static String getRawDamageFormat(boolean isCritical) {
        return (isCritical && ConfigManager.CONFIG_VALUES.get(ConfigVars.DETECT_CRITICAL_DAMAGE).asBoolean()) ? ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_CRITICAL_DAMAGE_FORMAT).asString() : ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_DAMAGE_FORMAT).asString();
    }

}
