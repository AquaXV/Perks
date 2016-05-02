package com.jimune.perks.manager;

import com.jimune.perks.Perks;
import com.jimune.perks.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PerkList {

    public PerkList() {}

    @Perk(name = "nightvision", permission = "perkmanager.nightvision", alias = {"nv", "nvision"}, crossWorld = false)
    public void nightvision(Player p, boolean disable) {
        if (disable) {
            if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        } else {
            if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                p.sendMessage(ChatColor.BLUE + "Nightvision has been disabled!");
            } else if (!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                p.sendMessage(ChatColor.BLUE + "Nightvision has been enabled!");
            }
        }
    }

    @Perk(name = "jumpboost", permission = "perkmanager.jumpboost", alias = {"j", "jump"}, cooldown = 25, allowpvp = false, crossWorld = false)
    public void jumpboost(Player p, boolean disable) {
        if (disable) {
            if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                p.removePotionEffect(PotionEffectType.JUMP);
            }
        } else {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 2));
            p.sendMessage(ChatColor.BLUE + "Bunny Hop Hop Hop!");
        }
    }

    @Perk(name = "superman", permission = "perkmanager.superman", alias = {"s", "super"}, cooldown = 20, allowpvp = false, crossWorld = false)
    public void elytra(Player p, boolean disable) {
        if (disable) {
            PlayerListener.gliding.remove(p.getName());
            p.setGliding(false);
        } else {
            if (!p.isGliding()) {
                p.sendMessage(ChatColor.BLUE + "You are now gliding the winds!");
                p.setFlying(false);
                p.setGliding(true);
                PlayerListener.gliding.add(p.getName());
            } else {
                p.setGliding(false);
            }
        }
    }

    @Perk(name = "supermanjump", permission = "perkmanager.superman", alias = {"sj", "superjump"}, cooldown = 30, allowpvp = false, crossWorld = false)
    public void elytrajump(final Player p, boolean disable) {
        if (disable) {
            p.setGliding(false);
            elytra(p, true);
        } else {
            double vy = 30.0D;
            double y = p.getLocation().getY();
            p.sendMessage(ChatColor.BLUE + "Woooooohoooooooooooo!");

            if (y < p.getWorld().getMaxHeight()) {
                p.setVelocity(new Vector(0.0D, vy, 0.0D));

                new BukkitRunnable() {
                    public void run() {
                        elytra(p, true);
                        elytra(p, false);
                    }
                }.runTaskLater(Perks.instance, 20L);
            }
        }
    }
}
