package com.array64.fastMining.skills;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.skills.impls.*;
import com.array64.fastMining.util.HumanResources;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerSkills {
    public static final SkillTree FOODSTUFFS, HEALTH, MINING;
    public static final SkillNode FOODSTUFFS_CRAFT_1, FOODSTUFFS_SATURATION_1, FOODSTUFFS_SATURATION_2, FOODSTUFFS_IMMUNITY_1, FOODSTUFFS_IMMUNITY_2, FOODSTUFFS_CRAFT_2,
    HEALTH_CRAFT_1, HEALTH_FEATHER_1, HEALTH_FEATHER_2, HEALTH_FEATHER_3, HEALTH_REGENERATION_1, HEALTH_REGENERATION_2, HEALTH_EXTRA_1,
    MINING_PICK_1, MINING_PICK_2, MINING_AXE_1, MINING_AXE_2, MINING_CRAFT_1;
    static final String LORE_NEWLINE = "\n" + ChatColor.GRAY + "" + ChatColor.ITALIC;
    public static long getTree(Player p, String skill) {
        HumanResources.ensurePlayerData(p);
        Document playerData = HumanResources.getPlayerData(p);
        return playerData.getEmbedded(List.of("skills", skill), Long.class);
    }
    public static void setTree(Player p, String skill, long tree) {
        HumanResources.ensurePlayerData(p);
        FastMining.getUsersCollection().updateOne(
            Filters.eq("uuid", p.getUniqueId().toString()),
            Updates.set("skills." + skill, tree)
        );
    }
    static {
        FOODSTUFFS = new SkillTree("foodstuffs", "Skills: Foodstuffs", 27,
            FOODSTUFFS_CRAFT_1 = new SkillNode(12, "Sous-Chef's Craft", new NoDependencies(), new IronPrice(30), SkillNode.NodeType.MAJOR, "Craft 2 steak with 4 rotten flesh and one bone."),
            FOODSTUFFS_SATURATION_1 = new SkillNode(22, "Saturation I", new OneDependency(0), new IronPrice(20), SkillNode.NodeType.NORMAL, "Adds 1 minute of saturation effect upon creating a mine."),
            FOODSTUFFS_SATURATION_2 = new SkillNode(23, "Saturation II", new OneDependency(1), new IronPrice(30), SkillNode.NodeType.NORMAL,"Adds extra 1 minute to Saturation I."),
            FOODSTUFFS_IMMUNITY_1 = new SkillNode(4, "Immunity I", new OneDependency(0), new IronPrice(20), SkillNode.NodeType.NORMAL, "Gives 12.5% chance to avoid getting Hunger."),
            FOODSTUFFS_IMMUNITY_2 = new SkillNode(5, "Immunity II", new OneDependency(3), new IronPrice(30), SkillNode.NodeType.NORMAL, "Adds extra 12.5% to Immunity I."),
            FOODSTUFFS_CRAFT_2 = new SkillNode(15, "Chef's Craft", new EitherOfTwoDependencies(2, 4), new DiamondPrice(30), SkillNode.NodeType.MAJOR, "Craft 6 steak with 8 rotten flesh and one bone.")
        );
        HEALTH = new SkillTree("health", "Skills: Health", 45,
            HEALTH_CRAFT_1 = new SkillNode(11, "Med Kit", new NoDependencies(), new GoldPrice(10), SkillNode.NodeType.MAJOR, "Craft a medkit with six oak logs and three iron." + LORE_NEWLINE + "This medkit gives you 10 seconds of Regeneration III." + LORE_NEWLINE + "(Check crafting table's recipe book for shape)"),
            HEALTH_FEATHER_1 = new SkillNode(12, "Feather I", new OneDependency(0), new GoldPrice(20), SkillNode.NodeType.NORMAL, "Reduces fall damage by 5%."),
            HEALTH_FEATHER_2 = new SkillNode(13, "Feather II", new OneDependency(1), new GoldPrice(30), SkillNode.NodeType.NORMAL,"Adds extra 5% to Feather I."),
            HEALTH_FEATHER_3 = new SkillNode(14, "Feather III", new OneDependency(2), new GoldPrice(40), SkillNode.NodeType.NORMAL, "Adds extra 5% to Feather II."),
            HEALTH_REGENERATION_1 = new SkillNode(24, "Regeneration I", new OneDependency(3), new DiamondPrice(20), SkillNode.NodeType.NORMAL, "Regeneration without saturation is 25% faster."),
            HEALTH_REGENERATION_2 = new SkillNode(25, "Regeneration II", new OneDependency(4), new DiamondPrice(30), SkillNode.NodeType.NORMAL, "Adds extra 25% to Regeneration I."),
            HEALTH_EXTRA_1 = new SkillNode(6, "School Lunch", new OneDependency(3), new GoldPrice(60), SkillNode.NodeType.MAJOR, "Receive a golden apple when starting a mine.")
        );
        MINING = new MiningSkillTree("mining", "Skills: Mining", 54,
            MINING_PICK_1 = new SkillNode(12, "Starter Pickaxe", new NoDependencies(), new DiamondPrice(5), SkillNode.NodeType.NORMAL, "Receive a wooden pickaxe when starting a mine."),
            MINING_PICK_2 = new SkillNode(13, "Getting an Upgrade", new OneDependency(0), new DiamondPrice(15), SkillNode.NodeType.NORMAL, "Replaces the Starter Pickaxe with a stone pickaxe."),
            MINING_AXE_1 = new SkillNode(30, "Hatchet", new NoDependencies(), new DiamondPrice(10), SkillNode.NodeType.NORMAL,"Receive a wooden axe when starting a mine."),
            MINING_AXE_2 = new SkillNode(31, "Lumberjack", new OneDependency(2), new DiamondPrice(15), SkillNode.NodeType.NORMAL, "Replaces the Hatchet with a stone axe."),
            MINING_CRAFT_1 = new SkillNode(23, "Redstone Pickaxe",
                new CompositeDependency(new OneDependency(1), new OneDependency(3)),
                new CompositePrice(new CompositePrice(new IronPrice(30), new GoldPrice(30)), new DiamondPrice(30)),
                SkillNode.NodeType.MAJOR, "Add Fortune I to a diamond pickaxe by crafting it using redstone blocks." + LORE_NEWLINE + "(Check crafting table's recipe book for shape)")
        );
    }
}
