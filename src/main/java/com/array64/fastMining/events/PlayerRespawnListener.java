package com.array64.fastMining.events;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.commands.LeaveMineCommand;
import com.array64.fastMining.util.MineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerRespawnListener implements Listener {
    public JavaPlugin plugin;

    public PlayerRespawnListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if(MineManager.getInstance().isInMine(p))
            LeaveMineCommand.destroyMine(p, MineManager.getInstance().getMine(p).world, FastMining.MineResult.FAILED);
    }
}
