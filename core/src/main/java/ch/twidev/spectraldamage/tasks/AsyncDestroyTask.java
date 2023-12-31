package ch.twidev.spectraldamage.tasks;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

public class AsyncDestroyTask extends BukkitRunnable {

    public static AsyncDestroyTask createDestroyingTask(@Nullable Player player, Entity entity) {
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
    public AsyncDestroyTask(@Nullable Player player, Entity entity) {
        this.player = player;
        this.entity = entity;
        this.taskType = TaskType.check();

        this.runTaskLaterAsynchronously(SpectralDamagePlugin.get(), ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt());

        // Register the task
        SpectralDamagePlugin.TASKS_ID.add(this.getTaskId());
    }

    @Override
    public void run() {
        if(taskType == TaskType.PACKET) {
            if (player!= null && player.isOnline()) {
                SpectralDamagePlugin.get().getPacketManager().destroyEntity(player, entity.getEntityId());
            }
        }else{
            Bukkit.getScheduler().runTask(SpectralDamagePlugin.get(), () -> {
                SpectralDamagePlugin.get().getPacketManager().destroyEntity(entity);
                entity.remove();
            });
        }

        Integer id = getTaskId();
        SpectralDamagePlugin.TASKS_ID.remove(id);
    }
}
