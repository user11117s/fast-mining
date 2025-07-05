package com.array64.fastMining.menus;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class InventoryMenu implements InventoryHolder {
    protected Inventory inventory;
    protected MenuHandler handler;
    protected JavaPlugin plugin;

    public InventoryMenu(MenuHandler handler, JavaPlugin plugin) {
        this.handler = handler;
        this.plugin = plugin;
    }
    public abstract String getMenuName();
    public abstract int size();
    public abstract void handleMenu(InventoryClickEvent e);
    public abstract void setContents();
    public void open() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            inventory = Bukkit.createInventory(this, size(), getMenuName());
            setContents();
            handler.getOwner().openInventory(inventory);
        });
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
