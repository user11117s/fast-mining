package com.array64.fastMining.events;

import com.array64.fastMining.util.MineManager;
import com.array64.fastMining.util.MineUpdater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if(MineManager.getInstance().isInMine(e.getPlayer()))
            MineUpdater.updateMine(e.getPlayer());
        else
            e.setCancelled(true);
    }
}
