package com.array64.fastMining.util;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.commands.LeaveMineCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class MineUpdater {
    static MineUpdater instance;
    MineManager mines;

    public MineUpdater(MineManager mines) {
        this.mines = mines;
        instance = this;
    }
    public static void updateMine(Player p) {
        instance.updateMine(p, 0);
    }
    public void updateMine(Player p, int ignored) {
        int mineTimeLeft = mines.getMine(p).timeLeft;
        ScoreboardManager sbManager = Bukkit.getScoreboardManager();
        Scoreboard sb = sbManager.getNewScoreboard();
        Objective objective = sb.registerNewObjective("mine_info", Criteria.DUMMY, ChatColor.AQUA + "" + ChatColor.BOLD + "MINE");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score1 = objective.getScore("");
        Score score2 = objective.getScore("Time left: " + ChatColor.GREEN + ""
                + (mineTimeLeft / 600) + ""
                + ((mineTimeLeft % 600) / 60) + ":"
                + ((mineTimeLeft % 60) / 10) + ""
                + (mineTimeLeft % 10)
        );
        Score score3 = objective.getScore(ChatColor.AQUA + "Diamonds: " + ChatColor.GREEN + "" + HumanResources.countCurrentDiamonds(p));
        Score score4 = objective.getScore(ChatColor.GOLD + "Gold: " + ChatColor.GREEN + "" + HumanResources.countCurrentGold(p));
        Score score5 = objective.getScore(ChatColor.GRAY + "Iron: " + ChatColor.GREEN + "" + HumanResources.countCurrentIron(p));
        Score score6 = objective.getScore("");
        Score score7 = objective.getScore("You are currently in " + ChatColor.AQUA + mines.getMine(p).world.getName());

        score1.setScore(7);
        score2.setScore(6);
        score3.setScore(5);
        score4.setScore(4);
        score5.setScore(3);
        score6.setScore(2);
        score7.setScore(1);

        p.setScoreboard(sb);
    }
    public void updateMines() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(mines.isInMine(p)) {
                mines.getMine(p).timeLeft--;
                updateMine(p);
                if(mines.getMine(p).timeLeft < 1)
                    LeaveMineCommand.destroyMine(mines.getMine(p).world, FastMining.MineResult.SUCCESS);
            }
        }
    }
}
