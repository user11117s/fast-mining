package com.array64.fastMining.skills;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.util.SpecialItems;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SkillNode {
    int slot;
    String name;
    String lore;
    DependencyRule dep;
    Price price;
    NodeType type;

    public SkillNode(int slot, String name, DependencyRule dep, Price price, NodeType nodeType, String lore) {
        this.slot = slot;
        this.name = name;
        this.dep = dep;
        this.price = price;
        this.lore = lore;
        this.type = nodeType;
    }
    public int getSlot() {
        return slot;
    }
    public ItemStack createItem(SkillTree skills, Player p, int indexInTree, long tree, String skillId) {
        DependencyRule.Status status = dep.getStatus(tree, indexInTree);
        ItemStack item = SpecialItems.createSpecialItem(switch(type) {
            case NORMAL -> status.material;
            case MAJOR -> status.majorMaterial;
        }, "skill_" + skillId + "_" + indexInTree);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(status.color + name);
        String itemLore = ChatColor.GRAY + "" + ChatColor.ITALIC + lore
                + "\n\n" + ChatColor.AQUA + "Requirements:"
                + dep.inLore(skills, p, tree)
                + price.inLore(skills, p, tree);
        meta.setLore(Arrays.asList(itemLore.split("\n")));

        item.setItemMeta(meta);
        return item;
    }
    public void tryBuy(SkillTree skills, Player p, int indexInTree, long tree) {
        if(dep.pass(p, tree, indexInTree) && price.pass(p, tree, indexInTree)){
            if(dep.getStatus(tree, indexInTree) == DependencyRule.Status.UNLOCKED) {
                p.sendMessage(ChatColor.GREEN + "You already have bought this upgrade, lil bro.");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.5f);
            }
            else {
                price.pay(p);
                PlayerSkills.setTree(p, skills.getSkillId(), tree | (1L << indexInTree));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                FastMining.newSkillMenu(p, skills).open();
            }
        } else {
            p.sendMessage(ChatColor.RED + "You do not have the requirements to unlock this skill.");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        }
    }

    public String getName() {
        return name;
    }
    public enum NodeType {
        NORMAL,
        MAJOR
    }
}
