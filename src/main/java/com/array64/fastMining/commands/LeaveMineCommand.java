package com.array64.fastMining.commands;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.util.HumanResources;
import com.array64.fastMining.util.MineManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class LeaveMineCommand implements CommandExecutor {

    static World hub;
    static JavaPlugin plugin;

    public static World getHub() {
        return hub;
    }

    public LeaveMineCommand(World hub, JavaPlugin plugin) {
        LeaveMineCommand.hub = hub;
        LeaveMineCommand.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) {
            if(MineManager.getInstance().isInMine(p)) {
                World mine = p.getWorld();
                destroyMine(mine, FastMining.MineResult.SUCCESS);
            }
            else
                p.sendMessage(ChatColor.RED + "You are not in a mine right now.");
        }
        else
            sender.sendMessage(ChatColor.RED + "Only players can leave mines.");
        return true;
    }
    public static void destroyMine(World mine, FastMining.MineResult result) {
        if(!mine.getPlayers().isEmpty())
            LeaveMineCommand.destroyMine(mine.getPlayers().get(0), mine, result);
    }
    public static void destroyMine(Player p, World mine, FastMining.MineResult result) {
        if(p == null) return;

        int diamonds = HumanResources.countCurrentDiamonds(p);
        int iron = HumanResources.countCurrentIron(p);
        int gold = HumanResources.countCurrentGold(p);

        MineManager.getInstance().removeMine(p);
        p.teleport(hub.getSpawnLocation());
        p.sendMessage(ChatColor.GRAY + "You have left " + ChatColor.AQUA + mine.getName() + ChatColor.GRAY + "!");
        FastMining.resetPlayer(p);
        destroyWorld(mine);

        if(result != FastMining.MineResult.FAILED) { // Because if you die, you don't keep your diamonds
            HumanResources.setTotalDiamonds(p, HumanResources.getTotalDiamonds(p) + diamonds);
            HumanResources.setTotalIron(p, HumanResources.getTotalIron(p) + iron);
            HumanResources.setTotalGold(p, HumanResources.getTotalGold(p) + gold);
        }
        switch(result) {
            case SUCCESS -> {
                p.sendTitle( ChatColor.AQUA + "" + ChatColor.BOLD + "MINE SUCCESSFUL!", "", 10, 50, 10);
                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Total diamonds: " + ChatColor.GREEN + "" + ChatColor.BOLD + "" + HumanResources.getTotalDiamonds(p));
                p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Total gold: " + ChatColor.GREEN + "" + ChatColor.BOLD + "" + HumanResources.getTotalGold(p));
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Total iron: " + ChatColor.GREEN + "" + ChatColor.BOLD + "" + HumanResources.getTotalIron(p));
            }
            case FAILED -> p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "MINE FAILED!", ChatColor.RED + "You died while mining!", 10, 30, 10);
        }
    }
    static void deleteFile(File file) {
        if(file.isDirectory()) {
            for(File content : file.listFiles()) {
                deleteFile(content);
            }
        }
        file.delete();
    }
    public static void destroyWorld(World world) {
        Bukkit.unloadWorld(world, false);
        File worldFolder = new File(Bukkit.getServer().getWorldContainer(), world.getName());
        deleteFile(worldFolder);
    }
}
