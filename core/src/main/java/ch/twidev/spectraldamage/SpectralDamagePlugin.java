package ch.twidev.spectraldamage;

import ch.twidev.spectraldamage.command.SpectralDamageCommand;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.exception.PluginEnableException;
import ch.twidev.spectraldamage.listener.DamageListener;
import ch.twidev.spectraldamage.nms.NMSManagerFactory;
import ch.twidev.spectraldamage.nms.NMSVersion;
import ch.twidev.spectraldamage.utils.UpdateChecker;
import ch.twidev.spectraldamage.nms.common.IPackets;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TwiDev
 */
public class SpectralDamagePlugin extends JavaPlugin {

    public static final int pluginId = 20119;

    public static final HashMap<Player, Boolean> PLAYER_VISIBILITY = new HashMap<>();

    public static final List<Integer> TASKS_ID = new ArrayList<>();

    public static final Logger LOGGER = Logger.getLogger("SpectralDamage");

    private static SpectralDamagePlugin INSTANCE;

    private IPackets packetManager;

    private Metrics metrics;

    private CoreAPI api;

    @Override
    public void onEnable() {
        INSTANCE = this;

        metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("launch", () -> packetManager.getVersionName()));

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
            throw new PluginEnableException("Spectral Damage only supports Spigot from 1.8 to 1.20.3");
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
            log("[Spectral Damage] Checking for a new update ...");
            new UpdateChecker(this, 110551).getVersion(version -> {
                Bukkit.getScheduler().runTaskLater(SpectralDamagePlugin.get(), () -> {
                    if (this.getDescription().getVersion().equals(version)) {
                        getLogger().info("There is not a new update available.");
                    } else {
                        log("#=====[SPECTRAL DAMAGE A NEW UPDATE IS AVAILABLE (v" + version + ")]==========#");
                        log("# Your spectral damage plugin is out of date !                       #");
                        log("# please install the latest version available on spigot              #");
                        log("# to take advantage of the latest fixes and features                 #");
                        log("# https://bit.ly/spectraldamage                                      #");
                        log("#====================================================================#");
                    }
                }, 5 * 20);
            });
        }

        this.api = new CoreAPI(this);

    }

    public CoreAPI getAPI() {
        return api;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    @NotNull
    public IPackets getPacketManager() {
        return packetManager;
    }

    @Override
    public void onDisable() {
        // Stop all active scheduler


        metrics.shutdown();

        try {
            TASKS_ID.forEach(id -> {
                Bukkit.getScheduler().cancelTask(id);
            });
            TASKS_ID.clear();
        } catch (NullPointerException ignore) {}
    }

    public void stop() {
        this.setEnabled(false);
    }

    public static SpectralDamagePlugin get() {
        return INSTANCE;
    }

    public static List<Integer> getTasksId() {
        return TASKS_ID;
    }

    public static void log(String message) {
        LOGGER.log(Level.INFO, message);
    }
}
