package com.array64.fastMining.events;

import com.array64.fastMining.util.MineManager;
import com.array64.fastMining.util.Recipes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class PrepareItemCraftListener implements Listener {
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e) {
        if(!(e.getViewers().get(0) instanceof Player p)) return;
        if(!MineManager.getInstance().isInMine(p)) return;
        Recipes.handleRecipe(p, e);
    }
}
