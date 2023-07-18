package ch.twidev.spectraldamage;

import ch.twidev.spectraldamage.api.DamageType;
import ch.twidev.spectraldamage.api.DamageTypeFactory;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.damage.DamageTypeEnum;
import ch.twidev.spectraldamage.tasks.AsyncDestroyTask;
import ch.twidev.spectraldamage.tasks.AsyncHologramTask;
import ch.twidev.spectraldamage.tasks.TaskType;
import ch.twidev.spectraldamage.api.SpectralDamage;
import ch.twidev.spectraldamage.nms.common.IPackets;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class CoreAPI extends SpectralDamage {

    private final IPackets packetManager;

    public CoreAPI(SpectralDamagePlugin plugin) {
        super(plugin);

        this.packetManager = plugin.getPacketManager();
    }

    @Override
    public Entity spawnDamageIndicator(Player player, Location location, DamageTypeFactory damageType, int damage, boolean falling) {
        Entity armorStand = packetManager.spawnHologram(player, location, damage, damageType.getFormat(damage), getPlugin());
        armorStand.setMetadata("_spectraldamage", new FixedMetadataValue(SpectralDamagePlugin.get(), 1));

        if(falling) {
            AsyncHologramTask.createHologramTask(player, armorStand, location);
        }else{
            AsyncDestroyTask.createDestroyingTask(player, armorStand);
        }

        return armorStand;
    }

    @Override
    public Entity spawnDamageIndicator(Player player, Location location, DamageTypeFactory damageType, int damage) {
        return this.spawnDamageIndicator(player, location, damageType, damage, true);
    }

    @Override
    public Entity spawnDamageIndicator(Location location, DamageTypeFactory damageType, int damage, boolean falling) {
        Entity armorStand = packetManager.spawnHologram(location, damage, damageType.getFormat(damage), getPlugin(), falling);
        ArmorStand entityArmorStand = (ArmorStand) armorStand;
        entityArmorStand.setGravity(falling);
        entityArmorStand.setMetadata("_spectraldamage", new FixedMetadataValue(SpectralDamagePlugin.get(), 1));
        entityArmorStand.setArms(false);

        if(falling) armorStand.setVelocity(new Vector(0, ConfigManager.CONFIG_VALUES.get(ConfigVars.HOLOGRAM_INITIAL_SPEED).asDouble()/1.8d, 0));

        AsyncDestroyTask.createDestroyingTask(null, armorStand);
        return armorStand;
    }

    @Override
    public Entity spawnDamageIndicator(Location location, DamageTypeFactory damageType, int damage) {
        return this.spawnDamageIndicator(location, damageType, damage, true);
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

    @Override
    public DamageTypeFactory getDamageTypeFactory(DamageType damageType) {
        try {
            return DamageTypeEnum.valueOf(damageType.toString());
        } catch (IllegalArgumentException e)  {
            e.printStackTrace();
            return null;
        }
    }
}
