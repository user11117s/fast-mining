package com.array64.fastMining.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MineManager {
    static MineManager instance;

    public MineManager() {
        instance = this;
    }

    public static MineManager getInstance() {
        return instance;
    }
    HashMap<Player, Mine> mines = new HashMap<>();
    public void addMine(Player p, World world, int timeLeft, Location woodBlock) {
        mines.put(p, new Mine(world, timeLeft, woodBlock));
    }
    public Mine getMine(Player p) {
        return mines.get(p);
    }
    public void removeMine(Player p) {
        mines.remove(p);
    }
    public boolean isInMine(Player p) {
        return mines.containsKey(p);
    }
}
