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

    /**
     * Spawn a damage indicator visible only to a player
     *
     * @param player location of the damager
     * @param location location of the damage indicator
     * @param critical is damage a critical (to format the hologram)
     * @param damage point of damage to show in the hologram
     * @param falling is hologram falling to the ground (animation configurable)
     * @return Damage indicator hologram (ArmorStand entity) (Packet entity)
     */

    public abstract Entity spawnDamageIndicator(Player player, Location location, boolean critical, int damage, boolean falling);

    /**
     * Spawn a damage indicator visible only to a player
     *
     * @param player location of the damager
     * @param location location of the damage indicator
     * @param critical is damage a critical (to format the hologram)
     * @param damage point of damage to show in the hologram
     * @return Damage indicator hologram (ArmorStand entity) (Packet entity)
     */
    public abstract Entity spawnDamageIndicator(Player player, Location location, boolean critical, int damage);

    /**
     * shows a damage indicator visible to all players who have not disabled damage indicators
     *
     * @param location location of the damage indicator
     * @param critical is damage a critical (to format the hologram)
     * @param damage point of damage to show in the hologram
     * @param falling is hologram falling to the ground (animation configurable)
     * @return Damage indicator hologram (ArmorStand entity)
     */
    public abstract Entity spawnDamageIndicator(Location location, boolean critical, int damage, boolean falling);

    /**
     * shows a damage indicator visible to all players who have not disabled damage indicators
     *
     * @param location location of the damage indicator
     * @param critical is damage a critical (to format the hologram)
     * @param damage point of damage to show in the hologram
     * @return Damage indicator hologram (ArmorStand entity)
     */

    public abstract Entity spawnDamageIndicator(Location location, boolean critical, int damage);


    /**
     * Spawn a damage indicator visible only to a player
     *
     * @param player location of the damager
     * @param location location of the damage indicator
     * @param damage point of damage to show in the hologram
     * @param falling is hologram falling to the ground (animation configurable)
     * @return Damage indicator hologram (ArmorStand entity) (Packet entity)
     */

    public Entity spawnDamageIndicator(Player player, Location location, int damage, boolean falling) {
        return this.spawnDamageIndicator(player, location, false, damage, falling);
    }


    /**
     * Spawn a damage indicator visible only to a player
     *
     * @param player location of the damager
     * @param location location of the damage indicator
     * @param damage point of damage to show in the hologram
     * @return Damage indicator hologram (ArmorStand entity) (Packet entity)
     */

    public Entity spawnDamageIndicator(Player player, Location location, int damage) {
        return this.spawnDamageIndicator(player, location, false, damage);
    }

    /**
     * shows a damage indicator visible to all players who have not disabled damage indicators
     *
     * @param location location of the damage indicator
     * @param damage point of damage to show in the hologram
     * @param falling is hologram falling to the ground (animation configurable)
     * @return Damage indicator hologram (ArmorStand entity)
     */
    public Entity spawnDamageIndicator(Location location, int damage, boolean falling) {
        return this.spawnDamageIndicator(location, false, damage, falling);
    }

    /**
     * shows a damage indicator visible to all players who have not disabled damage indicators
     *
     * @param location location of the damage indicator
     * @param damage point of damage to show in the hologram
     * @return Damage indicator hologram (ArmorStand entity)
     */

    public Entity spawnDamageIndicator(Location location, int damage) {
        return this.spawnDamageIndicator(location, false, damage);
    }

    /**
     * Enable damage indicators for a specific player (they are enabled by default)
     * @param player player
     * @return if the request was successful
     */
    public abstract boolean enableDamageIndicators(Player player);

    /**
     * disable damage indicators for a specific player
     * (it will no longer make holograms only if they are visible only to the damager)
     *
     * @param player player
     * @return if the request was successful
     */
    public abstract boolean disableDamageIndicators(Player player);

    /**
     * Get API Instance
     *
     * @return API
     */
    public static SpectralDamage getInstance() {
        return instance;
    }

}
