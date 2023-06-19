package ch.twidev.spectral;

import ch.twidev.spectral.command.SpectralDamageCommand;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigValue;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.exception.PluginEnableException;
import ch.twidev.spectral.listener.DamageListener;
import ch.twidev.spectral.nms.NMSManagerFactory;
import ch.twidev.spectral.nms.NMSVersion;
import ch.twidev.spectral.utils.UpdateChecker;
import ch.twidev.spectraldamage.nms.common.IPackets;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TwiDev
 */
public class SpectralDamage extends JavaPlugin {

    public static final List<Player> PLAYER_VISIBILITY = new ArrayList<>();

    public static final List<Integer> TASKS_ID = new ArrayList<>();

    public static final Logger LOGGER = Logger.getLogger("SpectralDamage");

    private static SpectralDamage INSTANCE;

    private IPackets packetManager;

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

        // Init NMS Version manager
        try {
            this.packetManager = NMSVersion.getCurrentVersion().getManagerFactory().create();
        } catch (NMSManagerFactory.UnknownVersionException e) {
            throw new PluginEnableException("Spectral Damage only supports Spigot from 1.8 to 1.20.");
        } catch (NMSManagerFactory.OutdatedVersionException e) {
            throw new PluginEnableException("Spectral Damage doesn't support this version please use " + e.getMinimumSupportedVersion());
        }

        log("[Spectral Damage] is now running in version " + NMSVersion.getCurrentVersion());

        // Register listeners
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);

        // Register commands
        getCommand("spectraldamage").setExecutor(new SpectralDamageCommand());

        if(ConfigManager.CONFIG_VALUES.get(ConfigVars.UPDATE_CHECKER).asBoolean()) {
            log("[SPECTRAL DAMAGE] Checking for a new update ...");
            new UpdateChecker(this, 110551).getVersion(version -> {
                Bukkit.getScheduler().runTaskLater(SpectralDamage.get(), () -> {
                    if (this.getDescription().getVersion().equals(version)) {
                        getLogger().info("There is not a new update available.");
                    } else {
                        log("#=====[SPECTRAL DAMAGE A NEW UPDATE IS AVAILABLE (v" + version + ")]=====#");
                        log("# Your spectral damage plugin is out of date !                       #");
                        log("# please install the latest version available on spigot              #");
                        log("# to take advantage of the latest fixes and features                 #");
                        log("# https://bit.ly/spectraldamage                                      #");
                        log("#====================================================================#");
                    }
                }, 5 * 20);
            });
        }

    }

    @NotNull
    public IPackets getPacketManager() {
        return packetManager;
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
