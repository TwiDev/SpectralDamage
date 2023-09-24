package ch.twidev.spectraldamage.command;

import ch.twidev.spectraldamage.SpectralDamagePlugin;
import ch.twidev.spectraldamage.api.SpectralDamage;
import ch.twidev.spectraldamage.config.ConfigManager;
import ch.twidev.spectraldamage.config.ConfigVars;
import ch.twidev.spectraldamage.tasks.TaskType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectralDamageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean isOp = commandSender.isOp();

        final String prefix = ConfigManager.parseString(ConfigVars.MESSAGE_PREFIX);

        if(ConfigManager.CONFIG_VALUES.get(ConfigVars.RESTRICT_BY_PERMISSION).asBoolean() && !commandSender.hasPermission("spectraldamage.show")) {
            commandSender.sendMessage(ChatColor.RED + ConfigManager.parseString(ConfigVars.MESSAGE_NOT_PERMISSION));
            return false;
        }

        if(strings.length >= 1) {
            switch (strings[0]) {
                case "enable":
                    if(commandSender.hasPermission("spectraldamage.admin") || isOp) {
                        if(strings.length == 2) {
                            Player player = Bukkit.getPlayer(strings[1]);

                            if(player != null && player.isOnline()) {
                                SpectralDamage.getInstance().enableDamageIndicators(player);

                                commandSender.sendMessage(prefix + " §aDamage Indicators enabled for player " + player.getName());
                            }else{
                                commandSender.sendMessage(prefix + " §cthis player does not exists.");
                            }
                        }else{
                            commandSender.sendMessage(prefix + " §cInvalid command syntax: /spectraldamage enable <player>");
                        }
                    }else{
                        commandSender.sendMessage(ChatColor.RED + ConfigManager.parseString(ConfigVars.MESSAGE_NOT_PERMISSION));
                    }
                    break;
                case "disable":
                    if(commandSender.hasPermission("spectraldamage.admin") || isOp) {
                        if(strings.length == 2) {
                            Player player = Bukkit.getPlayer(strings[1]);

                            if(player != null && player.isOnline()) {
                                SpectralDamage.getInstance().disableDamageIndicators(player);

                                commandSender.sendMessage(prefix + " §aDamage Indicators disabled for player " + player.getName());
                            }else{
                                commandSender.sendMessage(prefix + " §cthis player does not exists.");
                            }
                        }else{
                            commandSender.sendMessage(prefix + " §cInvalid command syntax: /spectraldamage disable <player>");
                        }
                    }else{
                        commandSender.sendMessage(ChatColor.RED + ConfigManager.parseString(ConfigVars.MESSAGE_NOT_PERMISSION));
                    }
                    break;
                case "reload":
                    if (commandSender.hasPermission("spectraldamage.reload") || isOp) {
                        SpectralDamagePlugin.get().reloadConfig();
                        ConfigManager.load();
                            commandSender.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_CONFIG_RELOADED));
                    }else{
                        commandSender.sendMessage(ChatColor.RED + ConfigManager.parseString(ConfigVars.MESSAGE_NOT_PERMISSION));
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
                            if(SpectralDamagePlugin.PLAYER_VISIBILITY.getOrDefault(player, true)) {
                                SpectralDamagePlugin.PLAYER_VISIBILITY.put(player,false);
                                player.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_INDICATOR_TOGGLE_ON));
                            }else{
                                SpectralDamagePlugin.PLAYER_VISIBILITY.put(player,false);
                                player.sendMessage(prefix + " §a" + ConfigManager.parseString(ConfigVars.MESSAGE_INDICATOR_TOGGLE_OFF));
                            }
                        }else{
                            commandSender.sendMessage(ChatColor.RED + ConfigManager.parseString(ConfigVars.MESSAGE_NOT_PERMISSION));
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

        commandSender.sendMessage("§c§lSpectralDamage §8- §7A damage indicator plugin §8(v" + SpectralDamagePlugin.get().getDescription().getVersion() + ")");
        commandSender.sendMessage("§7Created by §bTwiDev §8(§bhttps://github.com/twidev§8)");

        if (commandSender.hasPermission("spectraldamage.reload") || isOp) {
            commandSender.sendMessage("§8 - §b/spectraldamage reload §8- §cReload configuration");
        }

        if (commandSender instanceof Player && (commandSender.hasPermission("spectraldamage.toggle") || isOp)) {
            commandSender.sendMessage("§8 - §b/spectraldamage toggle §8- §cToggle hologram visibility for the player who execute the command");
        }
    }
}
