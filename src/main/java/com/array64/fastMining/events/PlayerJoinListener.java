package com.array64.fastMining.events;

import com.array64.fastMining.FastMining;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        FastMining.resetPlayer(e.getPlayer());
    }
}
