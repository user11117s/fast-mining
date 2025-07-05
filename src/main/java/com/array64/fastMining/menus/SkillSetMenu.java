package com.array64.fastMining.menus;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.skills.PlayerSkills;
import com.array64.fastMining.util.SpecialItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillSetMenu extends InventoryMenu {
    public SkillSetMenu(MenuHandler handler, JavaPlugin plugin) {
        super(handler, plugin);
    }

    @Override
    public String getMenuName() {
        return "Skills";
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(!(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT)) return;
        if(e.getWhoClicked() instanceof Player p) {
            if(SpecialItems.hasSpecialId(e.getCurrentItem(), "skills_foodstuffs_icon"))
                FastMining.newSkillMenu(p, PlayerSkills.FOODSTUFFS).open();
            if(SpecialItems.hasSpecialId(e.getCurrentItem(), "skills_health_icon"))
                FastMining.newSkillMenu(p, PlayerSkills.HEALTH).open();
            if(SpecialItems.hasSpecialId(e.getCurrentItem(), "skills_mining_icon"))
                FastMining.newSkillMenu(p, PlayerSkills.MINING).open();
        }
    }

    @Override
    public void setContents() {
        ItemStack foodIcon = SpecialItems.createSpecialItem(Material.COOKED_BEEF, "skills_foodstuffs_icon");
        ItemMeta foodMeta = foodIcon.getItemMeta();
        foodMeta.setDisplayName(ChatColor.GOLD + "Foodstuffs");
        foodIcon.setItemMeta(foodMeta);

        ItemStack healthIcon = SpecialItems.createSpecialItem(Material.GOLDEN_APPLE, "skills_health_icon");
        ItemMeta healthMeta = healthIcon.getItemMeta();
        healthMeta.setDisplayName(ChatColor.YELLOW + "Health");
        healthIcon.setItemMeta(healthMeta);

        ItemStack miningIcon = SpecialItems.createSpecialItem(Material.DIAMOND_PICKAXE, "skills_mining_icon");
        ItemMeta miningMeta = miningIcon.getItemMeta();
        miningMeta.setDisplayName(ChatColor.AQUA + "Mining");
        miningIcon.setItemMeta(miningMeta);

        inventory.setItem(12, foodIcon);
        inventory.setItem(13, healthIcon);
        inventory.setItem(14, miningIcon);
    }
}
