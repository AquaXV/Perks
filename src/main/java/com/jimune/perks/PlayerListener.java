package com.jimune.perks;

import com.jimune.perks.manager.Cooldown;
import com.jimune.perks.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerListener implements Listener {

    public static Map<String, Long> tagged = new HashMap<String, Long>();
    public static List<String> gliding = new ArrayList<String>();

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamage() > 0)) return;

        if (e.getEntity() instanceof Player) {
            Player damager;
            if (e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            } else if (e.getDamager() instanceof Arrow) {
                Arrow a = (Arrow) e.getDamager();

                if (a.hasMetadata("shooter")) {
                    damager = Bukkit.getPlayer(a.getMetadata("shooter").get(0).asString());

                    if (damager == null) {
                        Throwable t = new Throwable();
                        t.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }

            Player damagee = (Player) e.getEntity();

            long time = System.currentTimeMillis();

            tagged.put(damager.getName(), time);
            tagged.put(damagee.getName(), time);

            try {
                for (String player : Manager.cooldowns.keySet()) {
                    if (player.equalsIgnoreCase(damager.getName())) {
                        for (Cooldown c : Manager.cooldowns.get(player)) {
                            if (!c.perk.allowpvp()) {
                                Manager.invoke(c.perk.name(), damager, true);
                            }
                        }
                    } else if (player.equalsIgnoreCase(damagee.getName())) {
                        for (Cooldown c : Manager.cooldowns.get(player)) {
                            if (!c.perk.allowpvp()) {
                                Manager.invoke(c.perk.name(), damagee, true);
                            }
                        }
                    }
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        try {
            for (String player : Manager.cooldowns.keySet()) {
                if (player.equalsIgnoreCase(e.getPlayer().getName())) {
                    for (Cooldown c : Manager.cooldowns.get(player)) {
                        if (!c.perk.crossWorld()) {
                            Manager.invoke(c.perk.name(), e.getPlayer(), true);
                        }
                    }
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void arrowShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow) {
            Player shooter = (Player) e.getEntity();
            Arrow projectile = (Arrow) e.getProjectile();

            projectile.setMetadata("shooter", new FixedMetadataValue(Perks.instance, shooter.getName()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerGlide(EntityToggleGlideEvent e) {
        if (e.getEntity() instanceof Player) {
            Player glider = (Player) e.getEntity();

            if (gliding.contains(glider.getName()) && glider.isGliding()) {
                if (glider.isOnGround()) {
                    gliding.remove(glider.getName());
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
