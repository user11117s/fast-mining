package com.array64.fastMining.events;

import com.array64.fastMining.util.MineManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBreakListener implements Listener {
    JavaPlugin plugin;

    public BlockBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(locationsEqual(e.getBlock().getLocation(), MineManager.getInstance().getMine(e.getPlayer()).woodBlock)) {
            Location location = e.getBlock().getLocation();
            World world = location.getWorld();
            Bukkit.getScheduler().runTask(plugin, () -> world.getBlockAt(location).setType(Material.OAK_LOG));
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        World world = e.getBlock().getWorld();
        if(!world.getPlayers().isEmpty()) {
            Player p = world.getPlayers().get(0);
            if(locationsEqual(e.getBlock().getLocation(), MineManager.getInstance().getMine(p).woodBlock))
                e.getBlock().getLocation().getBlock().setType(Material.OAK_LOG);
        }
    }
    boolean locationsEqual(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() &&
                loc1.getBlockY() == loc2.getBlockY() &&
                loc1.getBlockZ() == loc2.getBlockZ();
    }
}
