package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.Price;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.entity.Player;

public class CompositePrice extends Price {
    Price p1, p2;

    public CompositePrice(Price p1, Price p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean pass(Player p) {
        return p1.pass(p) && p2.pass(p);
    }

    @Override
    public void pay(Player p) {
        p1.pay(p);
        p2.pay(p);
    }

    @Override
    public String inLore(SkillTree skills, Player p, long tree) {
        return p1.inLore(skills, p, tree) + p2.inLore(skills, p, tree);
    }
}
