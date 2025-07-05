package com.array64.fastMining.skills;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.menus.SkillSetMenu;
import com.array64.fastMining.util.SpecialItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillTree {
    SkillNode[] nodes;
    String name;
    String skillId;
    int size;

    public SkillTree(String skillId, String name, int size, SkillNode... nodes) {
        this.nodes = nodes;
        this.name = name;
        this.size = size;
        this.skillId = skillId;
    }

    public void setContents(Player p, Inventory inventory, long tree) {
        for(int i = 0; i < nodes.length; i++) {
            SkillNode node = nodes[i];
            inventory.setItem(node.getSlot(), node.createItem(this, p, i, tree, skillId));
        }
        ItemStack back = SpecialItems.createSpecialItem(Material.BARRIER, "skills_back_icon");
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.GRAY + "Back");
        back.setItemMeta(backMeta);
        inventory.setItem(size - 1, back);
    }

    // The only reason we don't use a long tree parameter here is that I will only use this method to put
    // upgrades into effect. But I think providing a long tree parameter is not too much of
    // a burden for a SkillMenu.
    // Let's just pretend this method doesn't exist for the rest of com.array64.fastMining.skills.

    public boolean getUpgrade(Player p, SkillNode node) {
        long tree = PlayerSkills.getTree(p, skillId);
        for(int i = 0; i < nodes.length; i++) {
            SkillNode element = nodes[i];
            if(element == node)
                return (tree & (1L << i)) > 0;
        }
        return false;
    }

    public String getSkillId() {
        return skillId;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
    public void handleMenu(InventoryClickEvent e, long tree, JavaPlugin plugin) {
        if(e.getWhoClicked() instanceof Player p) {
            if(!SpecialItems.isSpecialItem(e.getCurrentItem())) return;

            if(SpecialItems.hasSpecialId(e.getCurrentItem(), "skills_back_icon")) {
                new SkillSetMenu(FastMining.getMenuHandler(p), plugin).open();
                return;
            }

            String specialItemId = SpecialItems.getSpecialId(e.getCurrentItem());
            int indexInTree = Integer.parseInt(specialItemId.split("_")[2]);
            SkillNode node = nodes[indexInTree];
            node.tryBuy(this, p, indexInTree, tree);
        }
    }
    public SkillNode getNode(int index) {
        return nodes[index];
    }
}
