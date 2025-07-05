package com.array64.fastMining.skills;

import org.bukkit.entity.Player;

public interface Requirement {
    boolean pass(Player p, long tree, int index);
    String inLore(SkillTree skills, Player p, long tree);
}
