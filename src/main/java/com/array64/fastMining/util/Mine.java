package com.array64.fastMining.util;

import org.bukkit.Location;
import org.bukkit.World;

public class Mine {
    public World world;
    public int timeLeft;
    public Location woodBlock;

    public Mine(World world, int timeLeft, Location woodBlock) {
        this.world = world;
        this.timeLeft = timeLeft;
        this.woodBlock = woodBlock;
    }
}
