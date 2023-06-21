package ch.twidev.spectraldamage.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpectralDamage {

    private static SpectralDamage instance;

    private final JavaPlugin plugin;

    public SpectralDamage(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public abstract void spawnDamageIndicator(Player player, Location location,boolean critical, int damage, boolean falling);

    public abstract void spawnDamageIndicator(Player player, Location location, boolean critical, int damage);

    public abstract void spawnDamageIndicator(Location location, boolean critical, int damage, boolean falling);

    public abstract void spawnDamageIndicator(Location location, boolean critical, int damage);

    public void spawnDamageIndicator(Player player, Location location, int damage, boolean falling) {
        this.spawnDamageIndicator(player, location, false, damage, falling);
    }

    public void spawnDamageIndicator(Player player, Location location, int damage) {
        this.spawnDamageIndicator(player, location, false, damage);
    }

    public void spawnDamageIndicator(Location location, int damage, boolean falling) {
        this.spawnDamageIndicator(location, false, damage, falling);
    }

    public void spawnDamageIndicator(Location location, int damage) {
        this.spawnDamageIndicator(location, false, damage);
    }

    public abstract boolean enableDamageIndicators(Player player);

    public abstract boolean disableDamageIndicators(Player player);

    public static SpectralDamage getInstance() {
        return instance;
    }

}
