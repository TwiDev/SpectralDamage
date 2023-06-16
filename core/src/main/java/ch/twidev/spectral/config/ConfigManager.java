package ch.twidev.spectral.config;

import ch.twidev.spectral.SpectralDamage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;

public class ConfigManager {

    public static final HashMap<ConfigVars, ConfigValue> CONFIG_VALUES = new HashMap<>();

    public static void load() {
        CONFIG_VALUES.clear();
        SpectralDamage.get().saveConfig();
        FileConfiguration fileConfiguration = SpectralDamage.get().getConfig();

        try {
            for (ConfigVars var : ConfigVars.values()) {
                String path = var.getConfigName();

                if (fileConfiguration.contains(path)) {
                    ConfigManager.CONFIG_VALUES.put(var, new ConfigValue(fileConfiguration.get(path)));
                } else {
                    ConfigManager.CONFIG_VALUES.put(var, new ConfigValue(var.getDefaultValue()));
                    SpectralDamage.LOGGER.warning("The configurable value "+var.getConfigName()+" doesn't seem to exist, let's use the default value");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            SpectralDamage.LOGGER.severe("The configuration file did not load correctly!");
            SpectralDamage.get().stop();
        }

    }

}
