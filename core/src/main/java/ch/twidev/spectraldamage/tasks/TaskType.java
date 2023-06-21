package ch.twidev.spectraldamage.tasks;

import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;

public enum TaskType {

    PACKET,
    WORLD;


    public static TaskType check() {
        return ConfigManager.CONFIG_VALUES.get(ConfigVars.SHOW_TO_DAMAGER_ONLY).asBoolean() ? TaskType.PACKET : TaskType.WORLD;
    }

}
