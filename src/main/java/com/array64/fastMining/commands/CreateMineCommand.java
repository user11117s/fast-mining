package com.array64.fastMining.commands;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.util.MineManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CreateMineCommand implements CommandExecutor {
    public JavaPlugin plugin;

    public CreateMineCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p)
            createMine(p);
        else
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
        return true;
    }
    void createMine(Player p) {
        if(MineManager.getInstance().isInMine(p))
            LeaveMineCommand.destroyMine(p.getWorld(), FastMining.MineResult.NONE);

        p.sendMessage(ChatColor.GRAY + "Creating world...");
        Bukkit.getScheduler().runTask(plugin, () -> {
            WorldCreator worldCreator = new WorldCreator("mine_" + FastMining.getMineNumber());
            worldCreator.keepSpawnInMemory(false);
            World world = worldCreator.createWorld();
            if(world != null) {
                FastMining.incrementMineNumber();
                handlePostGeneration(p, world);
            }
        });
    }
    static boolean isAir(Location location) {
        return location.getBlock().getType() == Material.AIR || location.getBlock().getType() == Material.CAVE_AIR;
    }

    static int floodFill(Location location, int requiredAir) {
        // BFS
        Queue<Location> toExplore = new LinkedList<>();
        Set<Location> explored = new HashSet<>();
        toExplore.add(location);
        int numAirBlocks = 0;

        while(!toExplore.isEmpty() && numAirBlocks < requiredAir) {
            Location head = toExplore.remove();
            if(explored.contains(head)) continue;
            explored.add(head);
            if(isAir(head)) {
                numAirBlocks++;
                if(!explored.contains(head.clone().add(1, 0, 0))) toExplore.add(head.clone().add(1, 0, 0));
                if(!explored.contains(head.clone().add(-1, 0, 0))) toExplore.add(head.clone().add(-1, 0, 0));
                if(!explored.contains(head.clone().add(0, 1, 0))) toExplore.add(head.clone().add(0, 1, 0));
                if(!explored.contains(head.clone().add(0, -1, 0))) toExplore.add(head.clone().add(0, -1, 0));
                if(!explored.contains(head.clone().add(0, 0, 1))) toExplore.add(head.clone().add(0, 0, 1));
                if(!explored.contains(head.clone().add(0, 0, -1))) toExplore.add(head.clone().add(0, 0, -1));
            }
        }
        return numAirBlocks;
    }
    void handlePostGeneration(Player p, World mine) {
        p.sendMessage(ChatColor.GRAY + "Finding spawn location...");
        // Find cave
        new BukkitRunnable() {
            boolean validLocationFound = false;
            Location spawn = null;
            int xChunk = 0;
            int zChunk = 0;
            int dxChunk = 1;
            int dzChunk = 0;
            int numSteps = 0;
            int stepsBeforeTurning = 1;
            final Random random = new Random();
            @Override
            public void run() {
                // Spiral pattern of chunks from (0, 0)
                if(validLocationFound) {
                    Location woodBlock = spawn.clone().add(1, 0, 0);
                    Location airBlock = woodBlock.clone().add(0, 1, 0);
                    mine.getBlockAt(woodBlock).setType(Material.OAK_LOG);
                    mine.getBlockAt(airBlock).setType(Material.AIR);

                    ArmorStand hologram = (ArmorStand) mine.spawnEntity(woodBlock.clone().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);
                    hologram.setVisible(false);
                    hologram.setGravity(false);
                    hologram.setCustomNameVisible(true);
                    hologram.setCustomName(ChatColor.AQUA + "This block regenerates when broken.");

                    p.sendMessage(ChatColor.GRAY + "Teleporting you to " + ChatColor.AQUA + mine.getName() + ChatColor.GRAY + "!");
                    p.teleport(spawn);
                    FastMining.preparePlayer(p);
                    MineManager.getInstance().addMine(p, mine, 600, woodBlock);
                    cancel();
                }
                else {
                    // Pick up a random chunk and try 24 times to find a block in a big enough cave
                    for(int a = 0; a < 24 && !validLocationFound; a++) {
                        int x = random.nextInt(0, 16);
                        int y = random.nextInt(-50, 20);
                        int z = random.nextInt(0, 16);
                        spawn = new Location(mine, xChunk * 16 + x, y, zChunk * 16 + z);
                        if (!isAir(spawn)) continue;
                        if (!(isAir(spawn.clone().add(0, 1, 0)) || isAir(spawn.clone().add(0, -1, 0)))) continue;
                        int numAirBlocks = floodFill(spawn, 500);
                        validLocationFound = numAirBlocks >= 500;
                        if(validLocationFound) {
                            while(spawn.getY() >= -64 && isAir(spawn)) {
                                spawn = spawn.add(0, -1, 0);
                            }
                            if(spawn.getY() <= -65 || spawn.getBlock().isLiquid()) validLocationFound = false;
                            spawn = spawn.add(0, 1, 0);
                        }
                    }
                    // Update spiral
                    xChunk += dxChunk;
                    zChunk += dzChunk;
                    numSteps++;
                    if(numSteps == stepsBeforeTurning) {
                        int tempDXChunk = dxChunk;
                        int tempDZChunk = dzChunk;
                        dxChunk = -tempDZChunk;
                        dzChunk = tempDXChunk;
                        if(dzChunk == 0) stepsBeforeTurning++;
                        numSteps = 0;
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
