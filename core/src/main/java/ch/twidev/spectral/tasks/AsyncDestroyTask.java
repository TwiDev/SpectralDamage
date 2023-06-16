package ch.twidev.spectral.tasks;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncDestroyTask extends BukkitRunnable {

    public static AsyncDestroyTask createDestroyingTask(Player player, int entityId) {
        return new AsyncDestroyTask(player, entityId);
    }

    private final Player player;
    private final int entityId;

    /**
     * Create an entity destroying task
     *
     * @param player player to send packet
     * @param entityId entity id to remove
     */
    public AsyncDestroyTask(Player player, int entityId) {
        this.player = player;
        this.entityId = entityId;

        this.runTaskLaterAsynchronously(SpectralDamage.get(), ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt());

        // Register the task
        SpectralDamage.TASKS_ID.add(this.getTaskId());
    }

    @Override
    public void run() {
        if(player.isOnline()) {
            SpectralDamage.get().getPacketManager().destroyEntity(player, entityId);
        }

        SpectralDamage.TASKS_ID.remove(this.getTaskId());
    }
}
