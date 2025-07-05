package com.array64.fastMining.events;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.commands.LeaveMineCommand;
import com.array64.fastMining.util.MineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(MineManager.getInstance().isInMine(p))
            LeaveMineCommand.destroyMine(p.getWorld(), FastMining.MineResult.NONE);
    }

}
