package ch.twidev.spectraldamage.tasks;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncHologramTask extends BukkitRunnable {

    public static AsyncHologramTask createHologramTask(Player player, Entity armorStand, Location initialLocation) {
        return new AsyncHologramTask(player, armorStand, initialLocation);
    }

    private final int DURATION;
    private final double INITIAL_SPEED, ACCELERATION;
    private int tick = 0;

    private final Player player;
    private final Entity armorStand;
    private final Location initialLocation;

    private final TaskType taskType;


    /**
     * Create a hologram gravity position task
     *
     * @param player Player to send the packet (damager)
     * @param armorStand NMS hologram entity
     * @param initialLocation Initial location of the NMS hologram
     */
    public AsyncHologramTask(Player player, Entity armorStand, Location initialLocation) {
        // Load configurable constants values
        this.DURATION = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt();
        this.INITIAL_SPEED = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_INITIAL_SPEED).asDouble();
        this.ACCELERATION = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_ACCELERATION).asDouble();

        this.player = player;
        this.armorStand = armorStand;
        this.initialLocation = initialLocation;
        this.taskType = TaskType.check();

        this.runTaskTimerAsynchronously(SpectralDamagePlugin.get(),0,1);

        // Register the task
        SpectralDamagePlugin.TASKS_ID.add(this.getTaskId());
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        double time = tick/20d;
        double dy = INITIAL_SPEED*time - 0.5d*ACCELERATION*Math.pow(time, 2);

        SpectralDamagePlugin.get().getPacketManager().relEntityMove(player, armorStand.getEntityId(),
                initialLocation.getY(),
                dy,
                false);


        if(tick >= DURATION) {
            SpectralDamagePlugin.get().getPacketManager().destroyEntity(player, armorStand.getEntityId());
            this.cancel();

        }
        tick++;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        Integer id = this.getTaskId();
        SpectralDamagePlugin.TASKS_ID.remove(id);

        super.cancel();
    }
}
