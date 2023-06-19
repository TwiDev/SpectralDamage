package ch.twidev.spectral.tasks;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncDestroyTask extends BukkitRunnable {

    public static AsyncDestroyTask createDestroyingTask(Player player, Entity entity) {
        return new AsyncDestroyTask(player, entity);
    }

    private final Player player;
    private final Entity entity;

    private final TaskType taskType;

    /**
     * Create an entity destroying task
     *
     * @param player player to send packet
     * @param entity entity to remove
     */
    public AsyncDestroyTask(Player player, Entity entity) {
        this.player = player;
        this.entity = entity;
        this.taskType = TaskType.check();

        this.runTaskLaterAsynchronously(SpectralDamage.get(), ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt());

        // Register the task
        SpectralDamage.TASKS_ID.add(this.getTaskId());
    }

    @Override
    public void run() {
        if(taskType == TaskType.PACKET) {
            if (player.isOnline()) {
                SpectralDamage.get().getPacketManager().destroyEntity(player, entity.getEntityId());
            }
        }else{
            SpectralDamage.get().getPacketManager().destroyEntity(entity);
            entity.remove();
        }

        SpectralDamage.TASKS_ID.remove(this.getTaskId());
    }
}
