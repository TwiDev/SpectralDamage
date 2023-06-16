package ch.twidev.spectral.command;

import ch.twidev.spectral.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpectralDamageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("§c§lSpectralDamage §8- §7A damage indicator plugin");
        commandSender.sendMessage("§7Created by §bTwiDev §8(§bhttps://github.com/twidev§8)");

        if (commandSender.hasPermission("spectraldamage.reload") || commandSender.isOp()) {
            if (strings.length < 1) {
                commandSender.sendMessage("§8 - §b/spectraldamage reload §8- §cReload configuration");
            }else{
                switch (strings[0]) {
                    case "reload":
                        ConfigManager.load();
                        commandSender.sendMessage("§c§lSpectralDamage §8- §aConfig file reloaded successfully!");
                        break;
                }
            }
        }

        return true;
    }
}
