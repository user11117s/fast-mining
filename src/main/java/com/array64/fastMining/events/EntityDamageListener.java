package com.array64.fastMining.events;

import com.array64.fastMining.skills.PlayerSkills;
import com.array64.fastMining.util.MineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player p)) return;
        if(!MineManager.getInstance().isInMine(p)) return;
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            double damageReduction = 0;
            if(PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_FEATHER_1)) damageReduction += 0.05;
            if(PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_FEATHER_2)) damageReduction += 0.05;
            if(PlayerSkills.HEALTH.getUpgrade(p, PlayerSkills.HEALTH_FEATHER_3)) damageReduction += 0.05;

            e.setDamage(e.getDamage() * (1 - damageReduction));
        }
    }
}
