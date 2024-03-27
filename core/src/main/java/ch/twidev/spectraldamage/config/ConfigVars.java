package ch.twidev.spectraldamage.config;

public enum ConfigVars {

    // Check for an update of the plugin at each start and warn the admins
    UPDATE_CHECKER("update_checker", true),
    // The holograms are visible only to the player, if false the hologram will be visible to everyone
    SHOW_TO_DAMAGER_ONLY("showToDamagerOnly", true),

    // The plugin will be activated by default for all players
    ENABLE_BY_DEFAULT("enable_by_default",true),
    RESTRICT_BY_PERMISSION("restrict_by_permission", false),

    // Maximum random coordinate on the X axis of the hologram location
    HOLOGRAM_OFFSET_X("offset_x", 1.25d),
    // Maximum random coordinate on the Z axis of the hologram location
    HOLOGRAM_OFFSET_Z("offset_z", 1.25d),
    // Maximum random coordinate on the Y axis of the hologram location
    HOLOGRAM_OFFSET_Y("offset_y", 0.65d),
    // Initial speed of the hologram allowing it to accelerate upwards before falling
    HOLOGRAM_INITIAL_SPEED("initial_speed", 1.15d),
    // Acceleration at which the hologram is attracted to the ground
    HOLOGRAM_ACCELERATION("acceleration", 4),
    // Damage display format
    HOLOGRAM_DAMAGE_FORMAT("damage_format", "&c- &4%damage% &cHP"),
    // Minimum number of damage needed to display the hologram
    DETECT_MINIMUM_DAMAGE("minimum_damage", 0),
    // Defines if the critical damage should be displayed with the custom format
    DETECT_CRITICAL_DAMAGE("critical_damage", true),
    // Defines if the fire damage should be displayed with the custom format
    DETECT_FIRE_DAMAGE("fire_damage", false),
    // Defines if the poison damage should be displayed with the custom format
    DETECT_POISON_DAMAGE("poison_damage", false),
    // Defines if the potion damage should be displayed with the custom format
    DETECT_POTION_DAMAGE("potion_damage", false),
    // Defines if the projectile damage should be displayed with the custom format
    DETECT_PROJECTILE_DAMAGE("projectile_damage", false),
    // Defines if the fall damage should be displayed with the custom format
    DETECT_FALL_DAMAGE("fall_damage", false),
    // Critical damage display format
    HOLOGRAM_CRITICAL_DAMAGE_FORMAT("critical_damage_format", "&c- &6&l%damage% &cHP"),
    // Fire damage display format
    HOLOGRAM_FIRE_DAMAGE_FORMAT("fire_damage_format", "&e- &6%damage% &eHP"),
    // Poison damage display format
    HOLOGRAM_POISON_DAMAGE_FORMAT("poison_damage_format", "&a- &2%damage% &aHP"),
    // Magic damage display format
    HOLOGRAM_MAGIC_DAMAGE_FORMAT("potion_damage_format", "&d- &2%damage% &aHP"),
    // Projectile damage display format
    HOLOGRAM_PROJECTILE_DAMAGE_FORMAT("projectile_damage_format", "&6- &2%damage% &aHP"),
    // Fall damage display format
    HOLOGRAM_FALL_DAMAGE_FORMAT("fall_damage_format", "&f- &b%damage% &fHP"),
    /**
     * hologram lifetime (Is fixed so that the hologram disappears after it is in the ground according to
     * at default initial speed and acceleration values)
     * Duration must be in ticks (1 seconds = 20 ticks)
     */
    HOLOGRAM_LIVING_TIME("living_time", 23),
    // Players are affected by damage display
    PLAYER_AFFECTED("player_damage_indicator", true),
    // Entities (not players) are affected by damage display
    ENTITIES_AFFECTED("entities_damage_indicator", true),
    HOLOGRAM_FALLING_ANIMATION("falling_animation", true),
    MESSAGE_PREFIX("message_prefix", "&c&lSpectralDamage &8-"),
    MESSAGE_CONFIG_RELOADED("message_config_reloaded", "Config file reloaded successfully!"),
    MESSAGE_CANNOT_TOGGLE_INDICATOR("message_cannot_toggle_indicator", "Impossible to change the visibility of the damage indicators because they are by default visible to everyone."),
    MESSAGE_INDICATOR_TOGGLE_ON("message_indicator_toggle_on", "The visibility of damage indicators has been reactivated!"),
    MESSAGE_INDICATOR_TOGGLE_OFF("message_indicator_toggle_off", "The visibility of damage indicators has been disabled!"),
    MESSAGE_NOT_PERMISSION("message_not_permission","You do not have permission to perform this action");

    private final String configName;
    private final Object defaultValue;

    ConfigVars(String configName, Object defaultValue) {
        this.configName = configName;
        this.defaultValue = defaultValue;
    }

    @Deprecated
    ConfigVars() {
        this.configName = toString().toLowerCase();
        this.defaultValue = null;
    }

    public String getConfigName() {
        return configName;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
