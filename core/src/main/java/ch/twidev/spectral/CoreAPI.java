package ch.twidev.spectral;

import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.config.ConfigVars;
import ch.twidev.spectral.tasks.AsyncDestroyTask;
import ch.twidev.spectral.tasks.AsyncHologramTask;
import ch.twidev.spectral.tasks.TaskType;
import ch.twidev.spectral.utils.StringUtils;
import ch.twidev.spectraldamage.api.SpectralDamage;
import ch.twidev.spectraldamage.nms.common.IPackets;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class CoreAPI extends SpectralDamage {

    private final IPackets packetManager;

    public CoreAPI(SpectralDamagePlugin plugin) {
        super(plugin);

        this.packetManager = plugin.getPacketManager();
    }

    @Override
    public void spawnDamageIndicator(Player player, Location location, boolean critical, int damage, boolean falling) {
        Entity armorStand = packetManager.spawnHologram(player, location, damage, StringUtils.getDamageFormat(damage, critical), getPlugin());

        if(falling) {
            AsyncHologramTask.createHologramTask(player, armorStand, location);
        }else{
            AsyncDestroyTask.createDestroyingTask(player, armorStand);
        }
    }

    @Override
    public void spawnDamageIndicator(Player player, Location location, boolean critical, int damage) {
        this.spawnDamageIndicator(player, location, critical, damage, true);
    }

    @Override
    public void spawnDamageIndicator(Location location, boolean critical, int damage, boolean falling) {
        Entity armorStand = packetManager.spawnHologram(location, damage, StringUtils.getDamageFormat(damage, critical), getPlugin(), falling);
        ArmorStand entityArmorStand = (ArmorStand) armorStand;
        entityArmorStand.setGravity(falling);

        if(falling) armorStand.setVelocity(new Vector(0, ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_INITIAL_SPEED).asDouble()/1.8d, 0));

        AsyncDestroyTask.createDestroyingTask(null, armorStand);

    }

    @Override
    public void spawnDamageIndicator(Location location, boolean critical, int damage) {
        this.spawnDamageIndicator(location, critical, damage, true);
    }

    @Override
    public boolean enableDamageIndicators(Player player) {
        if(TaskType.check() == TaskType.WORLD) return false;
        if(SpectralDamagePlugin.PLAYER_VISIBILITY.contains(player)) return false;

        SpectralDamagePlugin.PLAYER_VISIBILITY.remove(player);
        return true;
    }

    @Override
    public boolean disableDamageIndicators(Player player) {
        if(TaskType.check() == TaskType.WORLD) return false;
        if(!SpectralDamagePlugin.PLAYER_VISIBILITY.contains(player)) return false;

        SpectralDamagePlugin.PLAYER_VISIBILITY.add(player);
        return true;
    }
}
