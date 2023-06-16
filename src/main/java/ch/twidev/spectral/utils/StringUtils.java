package ch.twidev.spectral.utils;

import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;

public class StringUtils {

    public static String getDamageFormat(double damage){
        return ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_DAMAGE_FORMAT).asString().replaceAll("%damage%", String.valueOf(Math.round(damage)));
    }

}
