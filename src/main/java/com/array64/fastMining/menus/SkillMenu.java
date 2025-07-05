package com.array64.fastMining.menus;

import com.array64.fastMining.skills.PlayerSkills;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillMenu extends InventoryMenu {
    SkillTree skills;

    public SkillMenu(MenuHandler handler, JavaPlugin plugin, SkillTree skills) {
        super(handler, plugin);
        this.skills = skills;
    }

    @Override
    public String getMenuName() {
        return skills.getName();
    }

    @Override
    public int size() {
        return skills.getSize();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(!(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT)) return;
        skills.handleMenu(e, PlayerSkills.getTree(handler.getOwner(), skills.getSkillId()), plugin);
    }

    @Override
    public void setContents() {
        skills.setContents(handler.getOwner(), inventory, PlayerSkills.getTree(handler.getOwner(), skills.getSkillId()));
    }
}
