package net.bghddev.pearls.purgepearls.commands;

import net.bghddev.pearls.purgepearls.PurgePearls;
import net.bghddev.pearls.purgepearls.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PurgePearlsCommand implements CommandExecutor, TabCompleter
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            this.sendUsage(sender);
        }
        else if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);
            if (args[0].equals("resetcooldown") && player != null) {
                if (sender.hasPermission("purgepearls.admin")) {
                    PurgePearls.getInstance().getTimer().cooldown.remove(player.getUniqueId());
                    sender.sendMessage("You have removed the cooldown of " + player.getName());
                    if (PurgePearls.getInstance().getConfig().getBoolean("PearlRefund.PearlRefund-Command")) {
                        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), PurgePearls.getInstance().getConfig().getString("PearlRefund.Command").replace("%player%", player.getName()));
                    }
                }
                else {
                    sender.sendMessage(CC.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("resetcooldown") && player == null) {
                sender.sendMessage(CC.translate("&cInvalid Player"));
            }
        }
        else if (args.length == 1) {
            if (args[0].equals("slabs")) {
                if (sender.hasPermission("purgepearls.admin")) {
                    if (PurgePearls.getInstance().getConfig().getBoolean("PearlMethods.TalibanPearl.Slabs")) {
                        sender.sendMessage(CC.translate("&eTalibanPearl on &lSlabs &eHas been toggled &cOff"));
                        PurgePearls.getInstance().getConfig().set("TalibanPearl.Slabs", (Object)false);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                    else if (!PurgePearls.getInstance().getConfig().getBoolean("PearlMethods.TalibanPearl.Slabs")) {
                        sender.sendMessage(CC.translate("&eTalibanPearl on &lSlabs &eHas been toggled &aOn"));
                        PurgePearls.getInstance().getConfig().set("TalibanPearl.Slabs", (Object)true);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(CC.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("stairs")) {
                if (sender.hasPermission("purgepearls.admin")) {
                    if (PurgePearls.getInstance().getConfig().getBoolean("PearlMethods.TalibanPearl.Stairs")) {
                        sender.sendMessage(CC.translate("&eTalibanPearl on &lStairs &eHas been toggled &cOff"));
                        PurgePearls.getInstance().getConfig().set("TalibanPearl.Stairs", (Object)false);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                    else if (!PurgePearls.getInstance().getConfig().getBoolean("PearlMethods.TalibanPearl.Stairs")) {
                        sender.sendMessage(CC.translate("&eTalibanPearl on &lStairs &eHas been toggled &aOn"));
                        PurgePearls.getInstance().getConfig().set("TalibanPearl.Stairs", (Object)true);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(CC.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("cpearl")) {
                if (sender.hasPermission("purgepearls.admin")) {
                    if (PurgePearls.getInstance().getConfig().getBoolean("PearlMethods.cPearl")) {
                        sender.sendMessage(CC.translate("&e&lcPearl &eHas been toggled &cOff"));
                        PurgePearls.getInstance().getConfig().set("cPearl", (Object)false);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                    else if (!PurgePearls.getInstance().getConfig().getBoolean("PearlMethods.cPearl")) {
                        sender.sendMessage(CC.translate("&e&lcPearl &eHas been toggled &aOn"));
                        PurgePearls.getInstance().getConfig().set("cPearl", (Object)true);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(CC.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("reload")) {
                if (sender.hasPermission("purgepearls.admin")) {
                    PurgePearls.getInstance().reloadConfig();
                    PurgePearls.getInstance().getconfigfile().load();
                    sender.sendMessage(CC.translate("&aPlugin reloaded!"));
                }
                else {
                    sender.sendMessage(CC.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("tp") || args[0].equals("teleport")) {
                if (sender.hasPermission("purgepearls.admin")) {
                    if (PurgePearls.getInstance().getConfig().getBoolean("Settings.TalibanPearlTP")) {
                        sender.sendMessage(CC.translate("&eTalibanPearl Disabled &lTeleport &eHas been toggled &cOff"));
                        PurgePearls.getInstance().getConfig().set("Settings.TalibanPearlTP", (Object)false);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                    else if (!PurgePearls.getInstance().getConfig().getBoolean("Settings.TalibanPearlTP")) {
                        sender.sendMessage(CC.translate("&eTalibanPearl Disabled &lTeleport &eHas been toggled &aOn"));
                        PurgePearls.getInstance().getConfig().set("Settings.TalibanPearlTP", (Object)true);
                        PurgePearls.getInstance().getconfigfile().save();
                        PurgePearls.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(CC.translate("&cNo permission."));
                }
            }
        }
        else {
            this.sendUsage(sender);
        }
        return false;
    }
    
    public void sendUsage(final CommandSender sender) {
    	sender.sendMessage(CC.MENU_BAR);
        sender.sendMessage("");
        sender.sendMessage(CC.translate("&b&lPurge Pearls"));
        sender.sendMessage(CC.translate("&bVersion:&d 0.5"));
        sender.sendMessage(CC.translate("&bAuthor:&d Tinuy"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate("Commands:"));
        sender.sendMessage(CC.translate("&c/PurgePearls - Displays this message."));
        sender.sendMessage(CC.translate("&c/PurgePearls slabs - Toggles through pearling slabs."));
        sender.sendMessage(CC.translate("&c/PurgePearls stairs - Toggles through pearling stairs."));
        sender.sendMessage(CC.translate("&c/PurgePearls tp - Toggles pearling tp method."));
        sender.sendMessage(CC.translate("&c/PurgePearls resetcooldown <player> - Resets a player's enderpearl cooldown."));
        sender.sendMessage(CC.translate("&c/PurgePearls reload - Reloads the config.yml."));
        sender.sendMessage("");
        sender.sendMessage(CC.MENU_BAR);
    }
    
    @SuppressWarnings("deprecation")
	public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
    	if (args.length == 1) {
            return Arrays.asList("slabs", "stairs", "tp", "resetcooldown", "reload", "cpearl");
        }
    	if (args.length == 2) {
    		for (Player player : Bukkit.getOnlinePlayers()) {
    			return Arrays.asList(player.getName());
    		}
        }
        return Collections.emptyList();
    }
}
