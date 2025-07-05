package com.array64.fastMining.menus;

import org.bukkit.entity.Player;

public class MenuHandler {
    Player owner;

    public Player getOwner() {
        return owner;
    }

    public MenuHandler(Player owner) {
        this.owner = owner;
    }
}
