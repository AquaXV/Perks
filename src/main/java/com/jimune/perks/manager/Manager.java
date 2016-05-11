package com.jimune.perks.manager;

import com.jimune.perks.Constants;
import com.jimune.perks.Perks;
import com.jimune.perks.PlayerListener;
import com.jimune.perks.command.PerkCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    public static Map<Perk, Method> perks = new HashMap<Perk, Method>();
    public static Map<String, List<Cooldown>> cooldowns = new HashMap<String, List<Cooldown>>();
    private static PerkList instance;

    public static void init() {
        instance = new PerkList();
        register(instance.getClass());
    }

    public static void register(Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            Perk p = m.getAnnotation(Perk.class);

            if (p != null) {
                perks.put(p, m);
                Perks.instance.registerCommand(new PerkCommand(p));
            }
        }
    }

    public static boolean execute(String perk, Player cause) {
        if ("".equals(perk) || perk == null || cause == null) return false;

        List<Cooldown> cds = cooldowns.get(cause.getName()) != null ? cooldowns.get(cause.getName()) : new ArrayList<Cooldown>();

        if (!cause.hasPermission("perkmanager.ignoreCooldown")) {
            Cooldown remove = null;
            boolean done = false;

            for (Cooldown c : cds) {
                if (c.isForPerk(perk)) {
                    if (c.isFinished()) {
                        remove = c;
                        done = true;

                        break;
                    } else {
                        cause.sendMessage(ChatColor.RED + "This perk is still on cooldown for another " + c.secondsRemaining() + " second(s)!");
                        return false;
                    }
                }
            }

            if (done) {
                cds.remove(remove);
            }
        }

        Perk p = getPerk(perk);

        if (p != null) {
            if (PlayerListener.tagged.containsKey(cause.getName()) && !cause.hasPermission("perkmanager.ignorePVPTag")) {
                Constants tagConst = Constants.TAG_DURATION;
                if (System.currentTimeMillis() - PlayerListener.tagged.get(cause.getName()) < tagConst.ordinal() * 1000) {
                    cause.sendMessage(ChatColor.RED + "This perk can not be used while tagged!");
                    return false;
                }
            }

            try {
                invoke(p.name(), cause, false);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }

            cds.add(new Cooldown(p));
        }

        cooldowns.remove(cause.getName());
        cooldowns.put(cause.getName(), cds);
        return true;
    }

    public static boolean invoke(String perk, Player cause, boolean disable) throws InvocationTargetException, IllegalAccessException {
        for (Perk p : perks.keySet()) {
            if (p.name().equalsIgnoreCase(perk)) {
                perks.get(p).invoke(instance, cause, disable);
                return true;
            }
        }

        return false;
    }

    public static Perk getPerk(String perkName) {
        for (Perk p : perks.keySet()) {
            if (p.name().equalsIgnoreCase(perkName)) return p;

            for (String s : p.alias()) {
                if (s.equalsIgnoreCase(perkName)) return p;
            }
        }

        return null;
    }
}
