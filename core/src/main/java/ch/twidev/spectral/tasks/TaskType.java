package ch.twidev.spectral.tasks;

import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;

public enum TaskType {

    PACKET,
    WORLD;


    public static TaskType check() {
        return ConfigManager.CONFIG_VALUES.get(ConfigVars.SHOW_TO_DAMAGER_ONLY).asBoolean() ? TaskType.PACKET : TaskType.WORLD;
    }

}
