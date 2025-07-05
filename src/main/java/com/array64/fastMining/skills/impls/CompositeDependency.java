package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.DependencyRule;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.entity.Player;

public class CompositeDependency extends DependencyRule {
    DependencyRule dep1, dep2;
    public CompositeDependency(DependencyRule dep1, DependencyRule dep2) {
        this.dep1 = dep1;
        this.dep2 = dep2;
    }

    @Override
    public Status getStatus(long tree, int node) {
        if (DependencyRule.hasBit(tree, node)) return Status.UNLOCKED;

        Status s1 = dep1.getStatus(tree, node),
            s2 = dep2.getStatus(tree, node);

        if (s1 == Status.LOCKED && s2 == Status.LOCKED) return Status.LOCKED;
        if (s1 == Status.NEXT && s2 == Status.NEXT) return Status.NEXT;
        return Status.PARTIALLY_NEXT;
    }
    @Override
    public String inLore(SkillTree skills, Player p, long tree) {
        return dep1.inLore(skills, p, tree) + dep2.inLore(skills, p, tree);
    }
}
