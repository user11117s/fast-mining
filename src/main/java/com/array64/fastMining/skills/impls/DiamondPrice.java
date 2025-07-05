package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.Price;
import com.array64.fastMining.skills.SkillTree;
import com.array64.fastMining.util.HumanResources;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DiamondPrice extends Price {
    int numDiamonds;

    public DiamondPrice(int numDiamonds) {
        this.numDiamonds = numDiamonds;
    }

    @Override
    public boolean pass(Player p) {
        return HumanResources.getTotalDiamonds(p) >= numDiamonds;
    }

    @Override
    public void pay(Player p) {
        HumanResources.setTotalDiamonds(p, HumanResources.getTotalDiamonds(p) - numDiamonds);
    }

    @Override
    public String inLore(SkillTree skills, Player p, long tree) {
        return "\n" + (pass(p) ? ChatColor.GREEN : ChatColor.RED) + numDiamonds + " Diamonds (You have " + HumanResources.getTotalDiamonds(p) + ")";
    }
}
