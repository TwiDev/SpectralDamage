package ch.twidev.spectral.config;

public enum ConfigVars {
    // The holograms are visible only to the player, if false the hologram will be visible to everyone
    SHOW_TO_DAMAGER_ONLY("showToDamagerOnly", true),

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
    // Defines if the critical damage should be displayed with the custom format
    DETECT_CRITICAL_DAMAGE("damage_critical", true),
    // Critical damage display format
    HOLOGRAM_DAMAGE_CRITICAL_FORMAT("damage_critical_format", "&c- &6&l%damage% &cHP"),
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
    HOLOGRAM_FALLING_ANIMATION("falling_animation", true);

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
