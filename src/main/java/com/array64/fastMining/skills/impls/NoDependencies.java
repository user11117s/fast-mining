package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.DependencyRule;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.entity.Player;

public class NoDependencies extends DependencyRule {
    @Override
    public Status getStatus(long tree, int node) {
        return DependencyRule.hasBit(tree, node) ? Status.UNLOCKED : Status.NEXT;
    }

    @Override
    public String inLore(SkillTree skills, Player p, long tree) {
        return "";
    }
}
