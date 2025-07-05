package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.DependencyRule;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EitherOfTwoDependencies extends DependencyRule {
    int dep1, dep2;

    public EitherOfTwoDependencies(int dep1, int dep2) {
        this.dep1 = dep1;
        this.dep2 = dep2;
    }

    public Status getStatus(long tree, int index) {
        return (DependencyRule.hasBit(tree, dep1) || DependencyRule.hasBit(tree, dep2)) ? (DependencyRule.hasBit(tree, index) ? Status.UNLOCKED : Status.NEXT) : Status.LOCKED;
    }

    public String inLore(SkillTree skills, Player p, long tree) {
        return "\n" + ((DependencyRule.hasBit(tree, dep1) || DependencyRule.hasBit(tree, dep2)) ? ChatColor.GREEN : ChatColor.RED) + skills.getNode(dep1).getName() + " or " + skills.getNode(dep2).getName();
    }
}
