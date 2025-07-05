package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.Price;
import com.array64.fastMining.skills.SkillTree;
import com.array64.fastMining.util.HumanResources;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IronPrice extends Price {
    int numIron;

    public IronPrice(int numIron) {
        this.numIron = numIron;
    }

    @Override
    public boolean pass(Player p) {
        return HumanResources.getTotalIron(p) >= numIron;
    }

    @Override
    public void pay(Player p) {
        HumanResources.setTotalIron(p, HumanResources.getTotalIron(p) - numIron);
    }

    @Override
    public String inLore(SkillTree skills, Player p, long tree) {
        return "\n" + (pass(p) ? ChatColor.GREEN : ChatColor.RED) + numIron + " Iron (You have " + HumanResources.getTotalIron(p) + ")";
    }
}
