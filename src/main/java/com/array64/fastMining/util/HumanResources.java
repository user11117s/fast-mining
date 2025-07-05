package com.array64.fastMining.util;

import com.array64.fastMining.FastMining;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HumanResources {
    static int countCurrentResource(Player p, Material type) {
        int resource = 0;
        for(ItemStack stack : p.getInventory().getContents()) {
            if(stack == null) continue;
            if(stack.getType() == type)
                resource += stack.getAmount();
        }
        return resource;
    }
    public static int countCurrentDiamonds(Player p) {
        return countCurrentResource(p, Material.DIAMOND);
    }
    public static int countCurrentGold(Player p) {
        return countCurrentResource(p, Material.GOLD_INGOT);
    }
    public static int countCurrentIron(Player p) {
        return countCurrentResource(p, Material.IRON_INGOT);
    }
    public static Document getPlayerData(Player p) {
        return FastMining.getUsersCollection().find(Filters.eq("uuid", p.getUniqueId().toString())).first();
    }
    static int getTotalResource(Player p, String resource) {
        Document playerData = getPlayerData(p);
        if(playerData == null) return 0;
        else return playerData.getInteger(resource);
    }
    public static int getTotalDiamonds(Player p) {
        return getTotalResource(p, "totalDiamonds");
    }
    public static int getTotalIron(Player p) {
        return getTotalResource(p, "totalIron");
    }
    public static int getTotalGold(Player p) {
        return getTotalResource(p, "totalGold");
    }
    public static void ensurePlayerData(Player p) {
        Document playerData = getPlayerData(p);
        if(playerData == null)
            FastMining.getUsersCollection().insertOne(
                new Document("uuid", p.getUniqueId().toString())
                    .append("totalDiamonds", 0)
                    .append("totalIron", 0)
                    .append("totalGold", 0)
                    .append("skills",
                        new Document("mining", 0L)
                            .append("health", 0L)
                    )
            );
    }
    static void setTotalResource(Player p, String resource, int amount) {
        ensurePlayerData(p);
        FastMining.getUsersCollection().updateOne(Filters.eq("uuid", p.getUniqueId().toString()),
                Updates.set(resource, amount)
        );
    }
    public static void setTotalDiamonds(Player p, int amount) {
        setTotalResource(p, "totalDiamonds", amount);
    }
    public static void setTotalIron(Player p, int amount) {
        setTotalResource(p, "totalIron", amount);
    }
    public static void setTotalGold(Player p, int amount) {
        setTotalResource(p, "totalGold", amount);
    }
}
