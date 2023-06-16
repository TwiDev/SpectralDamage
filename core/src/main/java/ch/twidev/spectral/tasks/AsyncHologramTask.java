package ch.twidev.spectral.tasks;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;

import ch.twidev.spectral.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncHologramTask extends BukkitRunnable {

    public static AsyncHologramTask createHologramTask(Player player, int armorStandId, Location initialLocation) {
        return new AsyncHologramTask(player, armorStandId, initialLocation);
    }

    private final int DURATION;
    private final double INITIAL_SPEED, ACCELERATION;
    private int tick = 0;

    private final Player player;
    private final int armorStandId;
    private final Location initialLocation;


    /**
     * Create a hologram gravity position task
     *
     * @param player Player to send the packet (damager)
     * @param armorStandId NMS hologram entuty ID
     * @param initialLocation Initial location of the NMS hologram
     */
    public AsyncHologramTask(Player player, int armorStandId, Location initialLocation) {
        // Load configurable constants values
        this.DURATION = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt();
        this.INITIAL_SPEED = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_INITIAL_SPEED).asDouble();
        this.ACCELERATION = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_ACCELERATION).asDouble();

        this.player = player;
        this.armorStandId = armorStandId;
        this.initialLocation = initialLocation;

        this.runTaskTimerAsynchronously(SpectralDamage.get(),0,1);

        // Register the task
        SpectralDamage.TASKS_ID.add(this.getTaskId());
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        double time = tick/20d;
        double dy = INITIAL_SPEED*time - 0.5d*ACCELERATION*Math.pow(time, 2);

        SpectralDamage.get().getPacketManager().relEntityMove(player, armorStandId,
                initialLocation.getY(),
                dy,
                false);


        if(tick >= DURATION) {
            SpectralDamage.get().getPacketManager().destroyEntity(player, armorStandId);
            this.cancel();

        }
        tick++;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        Integer id = this.getTaskId();
        SpectralDamage.TASKS_ID.remove(id);

        super.cancel();
    }
}
