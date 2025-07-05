package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.DependencyRule;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class OneDependency extends DependencyRule {
    int dep;

    public OneDependency(int dep) {
        this.dep = dep;
    }

    public Status getStatus(long tree, int index) {
        return DependencyRule.hasBit(tree, dep) ? (DependencyRule.hasBit(tree, index) ? Status.UNLOCKED : Status.NEXT) : Status.LOCKED;
    }

    public String inLore(SkillTree skills, Player p, long tree) {
        return "\n" + (DependencyRule.hasBit(tree, dep) ? ChatColor.GREEN : ChatColor.RED) + skills.getNode(dep).getName();
    }
}
