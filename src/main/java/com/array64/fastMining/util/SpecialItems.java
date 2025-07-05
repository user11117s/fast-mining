package com.array64.fastMining.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SpecialItems {
    public static NamespacedKey specialItemKey;
    public static void initializeKey(String keyId, Plugin plugin) {
        specialItemKey = new NamespacedKey(plugin, keyId);
    }
    public static ItemStack createSpecialItem(Material material, String itemId) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(specialItemKey, PersistentDataType.STRING, itemId);
        item.setItemMeta(meta);
        return item;
    }
    public static boolean isSpecialItem(ItemStack item) {
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(specialItemKey);
    }
    @Nullable
    public static String getSpecialId(ItemStack item) {
        if(isSpecialItem(item))
            return item.getItemMeta().getPersistentDataContainer().get(specialItemKey, PersistentDataType.STRING);
        else
            return null;
    }
    public static boolean hasSpecialId(ItemStack item, String id) {
        if(isSpecialItem(item))
            return Objects.equals(getSpecialId(item), id);
        else
            return false;
    }
}
