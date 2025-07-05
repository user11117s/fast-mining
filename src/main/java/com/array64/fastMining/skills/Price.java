package com.array64.fastMining.skills;

import org.bukkit.entity.Player;

public abstract class Price implements Requirement {
    public abstract boolean pass(Player p);
    public final boolean pass(Player p, long tree, int index) {
        return pass(p); // Price shouldn't depend on skill tree or index in tree.
    }
    public abstract void pay(Player p);
}
