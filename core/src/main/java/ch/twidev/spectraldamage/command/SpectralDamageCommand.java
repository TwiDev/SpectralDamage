package ch.twidev.spectraldamage.command;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.tasks.TaskType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectralDamageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean isOp = commandSender.isOp();

        final String prefix = ConfigManager.parseString(ConfigVars.MESSAGE_PREFIX);

        if(strings.length >= 1) {
            switch (strings[0]) {
                case "reload":
                    if (commandSender.hasPermission("spectraldamage.reload") || isOp) {
                        SpectralDamagePlugin.get().reloadConfig();
                        ConfigManager.load();
                            commandSender.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_CONFIG_RELOADED));
                    }

                    break;
                case "toggle":
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;

                        if(TaskType.check() == TaskType.WORLD) {
                            player.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_CANNOT_TOGGLE_INDICATOR));
                            if(isOp) {
                                player.sendMessage("§7§oTo change this modify the plugin configuration value ShowOnlyToDamager to true §8(Message only visible to OPs");
                            }
                            return true;
                        }

                        if (commandSender.hasPermission("spectraldamage.toggle") || isOp) {
                            if(SpectralDamagePlugin.PLAYER_VISIBILITY.contains(player)) {
                                SpectralDamagePlugin.PLAYER_VISIBILITY.remove(player);
                                player.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_INDICATOR_TOGGLE_ON));
                            }else{
                                SpectralDamagePlugin.PLAYER_VISIBILITY.add(player);
                                player.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_INDICATOR_TOGGLE_OFF));
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

        commandSender.sendMessage("§c§lSpectralDamage §8- §7A damage indicator plugin §8(v " + SpectralDamagePlugin.get().getDescription().getVersion() + ")");
        commandSender.sendMessage("§7Created by §bTwiDev §8(§bhttps://github.com/twidev§8)");

        if (commandSender.hasPermission("spectraldamage.reload") || isOp) {
            commandSender.sendMessage("§8 - §b/spectraldamage reload §8- §cReload configuration");
        }

        if (commandSender instanceof Player && (commandSender.hasPermission("spectraldamage.toggle") || isOp)) {
            commandSender.sendMessage("§8 - §b/spectraldamage toggle §8- §cToggle hologram visibility for the player who execute the command");
        }
    }
}
