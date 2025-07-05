package com.array64.fastMining.events;

import com.array64.fastMining.menus.InventoryMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof InventoryMenu menu) {
            menu.handleMenu(e);
            e.setCancelled(true);
        }
    }
}
