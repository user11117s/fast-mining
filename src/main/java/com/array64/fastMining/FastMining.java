package com.array64.fastMining;

import com.array64.fastMining.commands.CreateMineCommand;
import com.array64.fastMining.commands.LeaveMineCommand;
import com.array64.fastMining.events.*;
import com.array64.fastMining.menus.MenuHandler;
import com.array64.fastMining.menus.SkillMenu;
import com.array64.fastMining.skills.PlayerSkills;
import com.array64.fastMining.skills.SkillTree;
import com.array64.fastMining.util.Recipes;
import com.array64.fastMining.util.SpecialItems;
import com.array64.fastMining.util.MineManager;
import com.array64.fastMining.util.MineUpdater;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public final class FastMining extends JavaPlugin {
    static int MINE_NUMBER = 1;
    static FastMining instance;
    static final HashMap<Player, MenuHandler> menuHandlers = new HashMap<>();
    MineUpdater mineUpdater;
    MineManager mines;

    public static int getMineNumber() {
        return MINE_NUMBER;
    }

    public static void incrementMineNumber() {
        MINE_NUMBER++;
    }

    public static MongoClient getMongoClient() {
        return instance.mongodb;
    }

    public static MongoCollection<Document> getUsersCollection() {
        return instance.usersCollection;
    }

    MongoClient mongodb;
    MongoCollection<Document> usersCollection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        instance = this;
        World hub = Bukkit.getWorld("world");
        mines = new MineManager();
        mineUpdater = new MineUpdater(mines);
        SpecialItems.initializeKey("FastMining_special_item", this);

        // Commands
        getCommand("createmine").setExecutor(new CreateMineCommand(this));
        getCommand("leavemine").setExecutor(new LeaveMineCommand(hub, this));

        registerListeners();
        Recipes.registerRecipes(this);

        Bukkit.getScheduler().runTaskTimer(this, () -> mineUpdater.updateMines(), 20L, 20L);

        mongoDBInit();
    }
    void registerListeners() {// Events
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new PlayerRespawnListener(this), this);
        manager.registerEvents(new PlayerDropListener(), this);
        manager.registerEvents(new PlayerPickupListener(), this);
        manager.registerEvents(new BlockBreakListener(this), this);
        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new InventoryInteractListener(this), this);
        manager.registerEvents(new PlayerLeaveListener(), this);
        manager.registerEvents(new PlayerItemConsumeListener(this), this);
        manager.registerEvents(new PrepareItemCraftListener(), this);
        manager.registerEvents(new EntityDamageListener(), this);
    }

    void mongoDBInit() {
        mongodb = MongoClients.create(getConfig().getString("mongoURI"));
        usersCollection = mongodb.getDatabase(
            getConfig().getString("mongoDatabaseName")
        ).getCollection(
            getConfig().getString("mongoCollectionName")
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(World world : Bukkit.getWorlds()) {
            if(world.getName().startsWith("mine_"))
                LeaveMineCommand.destroyMine(world, MineResult.NONE);
        }
    }
    public static void resetPlayer(Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();

        ItemStack skills = SpecialItems.createSpecialItem(Material.IRON_AXE, "skills_icon");
        ItemMeta skillsMeta = skills.getItemMeta();
        skillsMeta.setDisplayName(ChatColor.AQUA + "Skills");
        skills.setItemMeta(skillsMeta);

        p.getInventory().setItem(0, skills);

        for(PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setInvulnerable(true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, -1, 0, false, false, false));
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
    public static void preparePlayer(Player p) {
        p.setGameMode(GameMode.SURVIVAL);
        p.getInventory().clear();
        for(PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false, false));
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setExhaustion(0);
        p.setFireTicks(0);
        p.setInvulnerable(false);

        handleImmediateSkills(p);
    }
    static void handleImmediateSkills(Player p) {
        if(PlayerSkills.FOODSTUFFS.getUpgrade(p, PlayerSkills.FOODSTUFFS_SATURATION_2))
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2400, 0, false, false, true));
        else if(PlayerSkills.FOODSTUFFS.getUpgrade(p, PlayerSkills.FOODSTUFFS_SATURATION_1))
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1200, 0, false, false, true));

        if(PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_REGENERATION_2))
            p.setUnsaturatedRegenRate(40);
        else if(PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_REGENERATION_1))
            p.setUnsaturatedRegenRate(60);
        else
            p.setUnsaturatedRegenRate(80);

        if(PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_EXTRA_1))
            p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));

        if(PlayerSkills.MINING.getUpgrade(p, PlayerSkills.MINING_PICK_2))
            p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
        else if(PlayerSkills.MINING.getUpgrade(p, PlayerSkills.MINING_PICK_1))
            p.getInventory().addItem(new ItemStack(Material.WOODEN_PICKAXE));

        if(PlayerSkills.MINING.getUpgrade(p, PlayerSkills.MINING_AXE_2))
            p.getInventory().addItem(new ItemStack(Material.STONE_AXE));
        else if(PlayerSkills.MINING.getUpgrade(p, PlayerSkills.MINING_AXE_1))
            p.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
    }
    public enum MineResult {
        SUCCESS,
        FAILED,
        NONE
    }
    public static MenuHandler getMenuHandler(Player p) {
        if(!menuHandlers.containsKey(p))
            menuHandlers.put(p, new MenuHandler(p));
        return menuHandlers.get(p);
    }
    public static SkillMenu newSkillMenu(Player p, SkillTree skills) {
        return new SkillMenu(getMenuHandler(p), instance, skills);
    }
}
