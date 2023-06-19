package ch.twidev.spectral.command;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.config.ConfigManager;
import ch.twidev.spectral.tasks.TaskType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectralDamageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean isOp = commandSender.isOp();

        if(strings.length >= 1) {
            switch (strings[0]) {
                case "reload":
                    if (commandSender.hasPermission("spectraldamage.reload") || isOp) {
                        SpectralDamage.get().reloadConfig();
                        ConfigManager.load();
                        commandSender.sendMessage("§c§lSpectralDamage §8- §aConfig file reloaded successfully!");
                    }

                    break;
                case "toggle":
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;

                        if(TaskType.check() == TaskType.WORLD) {
                            player.sendMessage("§c§lSpectralDamage §8- §cImpossible to change the visibility of the damage indicators because they are by default visible to everyone.");
                            if(isOp) {
                                player.sendMessage("§7§oTo change this modify the plugin configuration value ShowOnlyToDamager to true §8(Message only visible to OPs");
                            }
                            return true;
                        }

                        if (commandSender.hasPermission("spectraldamage.switch") || isOp) {
                            if(SpectralDamage.PLAYER_VISIBILITY.contains(player)) {
                                SpectralDamage.PLAYER_VISIBILITY.remove(player);
                                player.sendMessage("§c§lSpectralDamage §8- §aThe visibility of damage indicators has been reactivated!");
                            }else{
                                SpectralDamage.PLAYER_VISIBILITY.add(player);
                                player.sendMessage("§c§lSpectralDamage §8- §cThe visibility of damage indicators has been disabled!");
                            }
                        }

                    }

                    break;
                default:
                    sendDefaultHelp(commandSender);
            }
        }else{
            sendDefaultHelp(commandSender);
        }


        return true;
    }

    public void sendDefaultHelp(CommandSender commandSender) {
        boolean isOp = commandSender.isOp();

        commandSender.sendMessage("§c§lSpectralDamage §8- §7A damage indicator plugin");
        commandSender.sendMessage("§7Created by §bTwiDev §8(§bhttps://github.com/twidev§8)");

        if (commandSender.hasPermission("spectraldamage.reload") || isOp) {
            commandSender.sendMessage("§8 - §b/spectraldamage reload §8- §cReload configuration");
        }

        if (commandSender instanceof Player && (commandSender.hasPermission("spectraldamage.toggle") || isOp)) {
            commandSender.sendMessage("§8 - §b/spectraldamage toggle §8- §cToggle hologram visibility for the player who execute the command");
        }
    }
}
