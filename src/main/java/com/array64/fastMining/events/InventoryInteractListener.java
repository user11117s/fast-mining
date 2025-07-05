package com.array64.fastMining.events;

import com.array64.fastMining.FastMining;
import com.array64.fastMining.menus.InventoryMenu;
import com.array64.fastMining.menus.SkillSetMenu;
import com.array64.fastMining.util.MineManager;
import com.array64.fastMining.util.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryInteractListener implements Listener {
    JavaPlugin plugin;

    public InventoryInteractListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p) {
            // For the hub
            if(MineManager.getInstance().isInMine(p)) return;
            if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
                if(SpecialItems.hasSpecialId(e.getCurrentItem(), "skills_icon")) {
                    new SkillSetMenu(FastMining.getMenuHandler(p), plugin).open();
                }
            }
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Action action = e.getAction();
        if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return;

        if(SpecialItems.hasSpecialId(e.getItem(), "skills_icon")) {
            new SkillSetMenu(FastMining.getMenuHandler(e.getPlayer()), plugin).open();
            e.setCancelled(true);
        }
        if(SpecialItems.hasSpecialId(e.getItem(), "health_medkit")) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2, false, false, true));
            e.getPlayer().getInventory().setItem(e.getHand(), null);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
        if(!MineManager.getInstance().isInMine(e.getPlayer()))
            e.setCancelled(true);
    }

}
