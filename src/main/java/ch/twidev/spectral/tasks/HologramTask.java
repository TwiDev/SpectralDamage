package ch.twidev.spectral.tasks;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.utils.LocationUtils;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramTask extends BukkitRunnable {

    public static HologramTask createHologramTask(Player player, EntityArmorStand entityArmorStand, Location initialLocation) {
        return new HologramTask(player, entityArmorStand, initialLocation);
    }

    private final int DURATION;
    private final double INITIAL_SPEED, ACCELERATION;
    private int tick = 0;

    private final Player player;
    private final EntityArmorStand entityArmorStand;
    private final Location initialLocation;
    private final PlayerConnection tempConnection;


    /**
     * Create a hologram gravity position task
     *
     * @param player Player to send the packet (damager)
     * @param entityArmorStand NMS hologram
     * @param initialLocation Initial location of the NMS hologram
     */
    public HologramTask(Player player, EntityArmorStand entityArmorStand, Location initialLocation) {
        // Load configurable constants values
        this.DURATION = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_LIVING_TIME).asInt();
        this.INITIAL_SPEED = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_INITIAL_SPEED).asDouble();
        this.ACCELERATION = ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_ACCELERATION).asDouble();

        this.player = player;
        this.entityArmorStand = entityArmorStand;
        this.initialLocation = initialLocation;
        this.tempConnection = ((CraftPlayer) player).getHandle().playerConnection;

        this.runTaskTimerAsynchronously(SpectralDamage.get(),0,1);

        // Register the task
        SpectralDamage.TASKS_ID.add(this.getTaskId());
    }

    @Override
    public void run() {
        if (!player.isOnline() || entityArmorStand.getBukkitEntity() == null) {
            this.cancel();
            return;
        }

        double time = tick/20d;
        double dy = INITIAL_SPEED*time - 0.5d*ACCELERATION*Math.pow(time, 2);

        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityArmorStand.getBukkitEntity().getEntityId(),
                LocationUtils.getShortPoint(initialLocation.getX()),
                LocationUtils.getShortPoint(initialLocation.getY(), dy),
                LocationUtils.getShortPoint(initialLocation.getZ()),
                true
        );

        tempConnection.sendPacket(packetPlayOutRelEntityMove);


        if(tick >= DURATION) {
            tempConnection.sendPacket(new PacketPlayOutEntityDestroy(entityArmorStand.getBukkitEntity().getEntityId()));
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
