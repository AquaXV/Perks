package com.jimune.perks.command;

import com.jimune.perks.manager.Manager;
import com.jimune.perks.manager.Perk;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Perks are for players only!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            String p = args[0];

            if (p.equalsIgnoreCase("list")) {
                Set<Perk> perks = Manager.perks.keySet();
                Set<String> perkList = new HashSet<String>();

                for (Perk perk : perks) {
                    if (player.hasPermission(perk.permission())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(perk.name());

                        if (perk.alias().length > 0) {
                            sb.append(" (");

                            for (int i = 0; i < perk.alias().length; i++) {
                                sb.append(perk.alias()[i]);
                                if (i != perk.alias().length - 1) sb.append(", ");
                            }

                            sb.append(")");
                        }

                        perkList.add(sb.toString());
                    }
                }

                if (perkList.size() > 0) {
                    player.sendMessage(ChatColor.BLUE + "Your perks:");

                    for (String perk : perkList) {
                        player.sendMessage(ChatColor.AQUA + " - " + perk);
                    }
                } else {
                    player.sendMessage(ChatColor.BLUE + "You don't have any perks!");
                }
            } else {
                Perk perk = Manager.getPerk(p);

                if (perk != null) {
                    if (player.hasPermission(perk.permission())) {
                        Manager.execute(p, player);
                    } else {
                        player.sendMessage(ChatColor.RED + "You are not allowed to use this perk!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Perk " + p + " was not found!");
                }
            }
        }
        return true;
    }
}
