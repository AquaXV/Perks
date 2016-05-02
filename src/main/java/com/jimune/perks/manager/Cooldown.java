package com.jimune.perks.manager;

public class Cooldown {

    public Perk perk;
    public long activated;

    public Cooldown(Perk perk) {
        this.perk = perk;
        this.activated = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - activated > perk.cooldown() * 1000;
    }

    public long secondsRemaining() {
        return (perk.cooldown() - ((System.currentTimeMillis() - activated) / 1000));
    }

    public boolean isForPerk(Perk perk) {
        return isForPerk(perk.name());
    }

    public boolean isForPerk(String perkName) {
        if (perk.name().equalsIgnoreCase(perkName)) {
            return true;
        } else {
            for (String s : perk.alias()) {
                if (s.equalsIgnoreCase(perkName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
