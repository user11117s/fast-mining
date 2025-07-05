package com.array64.fastMining.util;

import com.array64.fastMining.skills.PlayerSkills;
import com.array64.fastMining.skills.SkillTree;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.CharBuffer;
import java.util.*;

public class Recipes {
    static ShapelessRecipe FOOD_CRAFT_1, FOOD_CRAFT_2;
    static ShapedRecipe HEALTH_CRAFT_1, MINING_CRAFT_1;
    public static void registerRecipes(JavaPlugin plugin) {
        // Foodstuffs Craft 1
        Bukkit.addRecipe(
            FOOD_CRAFT_1 = new ShapelessRecipe(
                new NamespacedKey(plugin, "food_craft_1"),
                new ItemStack(Material.COOKED_BEEF, 2)
            )
                .addIngredient(4, Material.ROTTEN_FLESH)
                .addIngredient(Material.BONE)
        );
        // Foodstuffs Craft 2
        Bukkit.addRecipe(
            FOOD_CRAFT_2 = new ShapelessRecipe(
                new NamespacedKey(plugin, "food_craft_2"),
                new ItemStack(Material.COOKED_BEEF, 6)
            )
                .addIngredient(8, Material.ROTTEN_FLESH)
                .addIngredient(Material.BONE)
        );
        // Health Med Kit
        ItemStack medkit = SpecialItems.createSpecialItem(Material.PALE_OAK_HANGING_SIGN, "health_medkit");
        ItemMeta medkitMeta = medkit.getItemMeta();
        medkitMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Med Kit");
        medkitMeta.setLore(List.of(ChatColor.GRAY + "Right-click to get 10 seconds of Regeneration III."));
        medkit.setItemMeta(medkitMeta);

        Bukkit.addRecipe(
            HEALTH_CRAFT_1 = new ShapedRecipe(
                new NamespacedKey(plugin, "health_craft_1"),
                medkit
            )
                .shape("LIL", "LIL", "LIL")
                .setIngredient('L', Material.OAK_LOG)
                .setIngredient('I', Material.IRON_INGOT)
        );
        ItemStack fortunePick = new ItemStack(Material.DIAMOND_PICKAXE);
        fortunePick.addEnchantment(Enchantment.FORTUNE, 1);
        Bukkit.addRecipe(
            MINING_CRAFT_1 = new ShapedRecipe(
                new NamespacedKey(plugin, "mining_craft_1"),
                fortunePick
            )
                .shape("DDD", "RSR", " S ")
                .setIngredient('R', Material.REDSTONE_BLOCK)
                .setIngredient('S', Material.STICK)
                .setIngredient('D', Material.DIAMOND)
        );
    }
    public static void handleRecipe(Player p, PrepareItemCraftEvent e) {
        Recipe recipe = e.getRecipe();
        boolean lockRecipe = false;

        SkillTree food = PlayerSkills.FOODSTUFFS;
        lockRecipe |= (compareShapelessRecipes(FOOD_CRAFT_1, recipe) && !food.getUpgrade(p, PlayerSkills.FOODSTUFFS_CRAFT_1));
        lockRecipe |= (compareShapelessRecipes(FOOD_CRAFT_2, recipe) && !food.getUpgrade(p, PlayerSkills.FOODSTUFFS_CRAFT_2));
        lockRecipe |= (compareShapedRecipes(HEALTH_CRAFT_1, recipe) && !PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_CRAFT_1));
        lockRecipe |= (compareShapedRecipes(MINING_CRAFT_1, recipe) && !PlayerSkills.MINING.getUpgrade(p, PlayerSkills.MINING_CRAFT_1));

        if(lockRecipe)
            e.getInventory().setResult(null);
    }
    static boolean compareShapelessRecipes(ShapelessRecipe recipe1, Recipe recipe2) {
        if(recipe1 == null && recipe2 == null) return true;
        if(recipe1 == null || recipe2 == null) return false;
        if(!(recipe2 instanceof ShapelessRecipe castRecipe2)) return false;

        if(!itemsSimilar(recipe1.getResult(), recipe2.getResult())) return false;

        List<ItemStack> ingredients1 =     recipe1.getIngredientList(),
                        ingredients2 = castRecipe2.getIngredientList();

        ingredients1.sort(Comparator.comparing(ItemStack::getType));
        ingredients2.sort(Comparator.comparing(ItemStack::getType));

        return ingredients1.equals(ingredients2);
    }
    static boolean compareShapedRecipes(ShapedRecipe recipe1, Recipe recipe2) {
        if(recipe1 == null && recipe2 == null) return true;
        if(recipe1 == null || recipe2 == null) return false;
        if(!(recipe2 instanceof ShapedRecipe castRecipe2)) return false;

        if(!itemsSimilar(recipe1.getResult(), recipe2.getResult())) return false;

        return Arrays.equals(
            getItemGrid(recipe1), getItemGrid(castRecipe2),
            (a1, a2) -> Arrays.equals(
                a1, a2, (i1, i2) -> itemsSimilar(i1, i2) ? 0 : 1
            ) ? 0 : 1
        );
    }
    static boolean itemsSimilar(ItemStack i1, ItemStack i2) {
        if(i1 == null && i2 == null) return true;
        if(i1 == null || i2 == null) return false;
        return i1.isSimilar(i2);
    }
    static ItemStack[][] getItemGrid(ShapedRecipe recipe) {
        return Arrays.stream(recipe.getShape())
            .map(
                (str) -> CharBuffer.wrap(str).chars().mapToObj(
                    (i) -> i == ' ' ? null : recipe.getIngredientMap().get((char) i)
                ).toArray(ItemStack[]::new)
            ).toArray(ItemStack[][]::new);
    }
}
