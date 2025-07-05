package com.array64.fastMining.skills;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class DependencyRule implements Requirement {
    public enum Status {
        LOCKED(Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS, ChatColor.RED),
        PARTIALLY_NEXT(Material.ORANGE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS, ChatColor.GOLD),
        NEXT(Material.YELLOW_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS, ChatColor.YELLOW),
        UNLOCKED(Material.LIME_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS, ChatColor.GREEN);
        public final Material material, majorMaterial;
        public final ChatColor color;

        Status(Material m, Material major, ChatColor c) {
            this.material = m;
            this.majorMaterial = major;
            this.color = c;
        }
    }
    public abstract Status getStatus(long tree, int node);
    public boolean pass(Player p, long tree, int index) {
        Status status = getStatus(tree, index);
        return status == Status.NEXT || status == Status.UNLOCKED;
    }
    public static boolean hasBit(long tree, int index) {
        return (tree & (1L << index)) > 0;
    }
}
