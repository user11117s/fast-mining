package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.Price;
import com.array64.fastMining.skills.SkillTree;
import com.array64.fastMining.util.HumanResources;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GoldPrice extends Price {
    int numGold;

    public GoldPrice(int numGold) {
        this.numGold = numGold;
    }

    @Override
    public boolean pass(Player p) {
        return HumanResources.getTotalGold(p) >= numGold;
    }

    @Override
    public void pay(Player p) {
        HumanResources.setTotalGold(p, HumanResources.getTotalGold(p) - numGold);
    }

    @Override
    public String inLore(SkillTree skills, Player p, long tree) {
        return "\n" + (pass(p) ? ChatColor.GREEN : ChatColor.RED) + numGold + " Gold (You have " + HumanResources.getTotalGold(p) + ")";
    }
}
