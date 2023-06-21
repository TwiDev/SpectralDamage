package ch.twidev.spectraldamage.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpectralDamage {

    private static SpectralDamage instance;

    private final JavaPlugin plugin;

    public SpectralDamage(JavaPlugin plugin) {
        instance = this;

        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public abstract Entity spawnDamageIndicator(Player player, Location location, boolean critical, int damage, boolean falling);

    public abstract Entity spawnDamageIndicator(Player player, Location location, boolean critical, int damage);

    public abstract Entity spawnDamageIndicator(Location location, boolean critical, int damage, boolean falling);

    public abstract Entity spawnDamageIndicator(Location location, boolean critical, int damage);

    public Entity spawnDamageIndicator(Player player, Location location, int damage, boolean falling) {
        return this.spawnDamageIndicator(player, location, false, damage, falling);
    }

    public Entity spawnDamageIndicator(Player player, Location location, int damage) {
        return this.spawnDamageIndicator(player, location, false, damage);
    }

    public Entity spawnDamageIndicator(Location location, int damage, boolean falling) {
        return this.spawnDamageIndicator(location, false, damage, falling);
    }

    public Entity spawnDamageIndicator(Location location, int damage) {
        return this.spawnDamageIndicator(location, false, damage);
    }

    public abstract boolean enableDamageIndicators(Player player);

    public abstract boolean disableDamageIndicators(Player player);

    public static SpectralDamage getInstance() {
        return instance;
    }

}
