package com.array64.fastMining.skills.impls;

import com.array64.fastMining.skills.SkillNode;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

// We will just add pickaxes to the skill menu. That's all.
public class MiningSkillTree extends SkillTree {
    public MiningSkillTree(String skillId, String name, int size, SkillNode... nodes) {
        super(skillId, name, size, nodes);
    }

    @Override
    public void setContents(Player p, Inventory inventory, long tree) {
        super.setContents(p, inventory, tree);
        inventory.setItem(3, new ItemStack(Material.DIAMOND_PICKAXE));
        inventory.setItem(21, new ItemStack(Material.DIAMOND_AXE));
    }
}
