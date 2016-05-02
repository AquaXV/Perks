package com.jimune.perks.command;

import com.jimune.perks.manager.Manager;
import com.jimune.perks.manager.Perk;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PerkCommand extends BukkitCommand {

    Perk perk;

    public PerkCommand(Perk perk) {
        super("p" + perk.name());

        this.perk = perk;
        List<String> aliases = new ArrayList<String>();

        for (String s : perk.alias()) {
            aliases.add("p" + s);
        }

        super.setAliases(aliases);
        super.setPermission(perk.permission());
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Perks are for players only!");
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission(perk.permission())) {
            Manager.execute(this.perk.name(), player);
        } else {
            player.sendMessage(ChatColor.RED + "You are not allowed to use this perk!");
        }

        return true;
    }

}
