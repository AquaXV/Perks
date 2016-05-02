package com.jimune.perks;

import com.jimune.perks.command.Commands;
import com.jimune.perks.command.PerkCommand;
import com.jimune.perks.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class Perks extends JavaPlugin {

    public static Perks instance;
    private CommandMap commandMap;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("perk").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

        } catch(Exception e) {
            e.printStackTrace();
        }

        Manager.init();
    }

    public void registerCommand(PerkCommand cmd) {
        if (commandMap != null) {
            commandMap.register(cmd.getName(), cmd);
            getLogger().info("Registered perk command " + cmd.getName());
        } else {
            getLogger().severe("Failed to register perk command " + cmd.getName() + " because commandMap == null!");
        }
    }
}
