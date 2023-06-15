package ch.twidev.spectral;

import ch.twidev.spectral.command.SpectralDamageCommand;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigValue;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.listener.DamageListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TwiDev
 */
public class SpectralDamage extends JavaPlugin {

    public static final List<Integer> TASKS_ID = new ArrayList<>();

    public static final Logger LOGGER = Logger.getLogger("SpectralDamage");

    private static SpectralDamage INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        log("#=============[SPECTRAL DAMAGE IS ENABLED]=============#");
        log("# Spectral Damage is now loading. Please read          #");
        log("# carefully all outputs coming from it.                #");
        log("# A plugin by TwiDev (https://github.com/twidev)       #");
        log("#======================================================#");

        saveDefaultConfig();

        // Load config file values
        ConfigManager.load();

        // Register listeners
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);

        // Register commands
        getCommand("spectraldamage").setExecutor(new SpectralDamageCommand());

    }

    @Override
    public void onDisable() {
        // Stop all active scheduler

        TASKS_ID.forEach(id -> {
            Bukkit.getScheduler().cancelTask(id);
        });
        TASKS_ID.clear();
    }

    public void stop() {
        this.setEnabled(false);
    }

    public static SpectralDamage get() {
        return INSTANCE;
    }

    public static List<Integer> getTasksId() {
        return TASKS_ID;
    }

    public static void log(String message) {
        LOGGER.log(Level.INFO, message);
    }
}
