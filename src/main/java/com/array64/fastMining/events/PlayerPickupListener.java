package com.array64.fastMining.events;

import com.array64.fastMining.util.MineManager;
import com.array64.fastMining.util.MineUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PlayerPickupListener implements Listener {
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player p) {
            if(MineManager.getInstance().isInMine(p))
                MineUpdater.updateMine(p);
        }
    }

}
