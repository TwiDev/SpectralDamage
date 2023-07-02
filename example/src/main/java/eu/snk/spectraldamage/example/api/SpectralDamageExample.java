package eu.snk.spectraldamage.example.api;

import ch.twidev.spectraldamage.api.DamageType;
import ch.twidev.spectraldamage.api.DamageTypeFactory;
import ch.twidev.spectraldamage.api.SpectralDamage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpectralDamageExample extends JavaPlugin implements CommandExecutor {

    private static final DamageTypeFactory NORMAL_DAMAGE;

    static {
        NORMAL_DAMAGE = SpectralDamage.getInstance().getDamageTypeFactory(DamageType.NORMAL);
    }

    @Override
    public void onEnable() {
        getCommand("damage").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            SpectralDamage.getInstance().spawnDamageIndicator(
                    player.getLocation(), NORMAL_DAMAGE, 10, true);

            player.sendMessage("§aDamage indicator spawned !");
        }

        return true;
    }
}
