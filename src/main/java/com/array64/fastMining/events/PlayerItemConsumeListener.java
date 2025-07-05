package com.array64.fastMining.events;

import com.array64.fastMining.skills.PlayerSkills;
import com.array64.fastMining.util.MineManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlayerItemConsumeListener implements Listener {
    JavaPlugin plugin;

    public PlayerItemConsumeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityEffect(EntityPotionEffectEvent e) {
        if(!(e.getEntity() instanceof Player p)) return;
        if(!MineManager.getInstance().isInMine(p)) return;
        if(e.getAction() == EntityPotionEffectEvent.Action.CHANGED
            || e.getAction() == EntityPotionEffectEvent.Action.ADDED) { // Makes sure an effect is being added/changed instead of removed

            if(e.getModifiedType() == PotionEffectType.HUNGER) {
                Random random = new Random();
                double hungerRetention = 1.0;
                if(PlayerSkills.FOODSTUFFS.getUpgrade(p, PlayerSkills.FOODSTUFFS_IMMUNITY_1))
                    hungerRetention -= 1.0 / 8;
                if(PlayerSkills.FOODSTUFFS.getUpgrade(p, PlayerSkills.FOODSTUFFS_IMMUNITY_2))
                    hungerRetention -= 1.0 / 8;
                double randomDouble = random.nextDouble();
                if(randomDouble > hungerRetention)
                    e.setCancelled(true);
            }
        }
    }

}
