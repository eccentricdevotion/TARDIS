package me.eccentric_nz.TARDIS.rooms.surgery;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.kitchen.ChestData;
import me.eccentric_nz.TARDIS.rooms.kitchen.ChestUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectTypeCategory;

import java.util.ArrayList;
import java.util.List;

public class SurgeryRunnable implements Runnable {

    private final TARDIS plugin;

    public SurgeryRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void run() {
        // get all players in TARDIS worlds
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getUtils().inTARDISWorld(player)) {
                continue;
            }
            // get the tardis the player is in
            int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
            // if there is a surgery room in the tardis
            ChestData chestData = ChestUtility.getSurgeryData(plugin, id);
            if (chestData.chest()) {
                Inventory inventory = ChestUtility.getChestInventory(chestData.location());
                // is the surgery chest empty - return
                if (inventory.isEmpty()) {
                    continue;
                }
                // do they have a potion effect?
                // is the effect a harmful one that can be reversed by drinking milk?
                List<PotionEffect> harmful = new ArrayList<>();
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().getCategory() == PotionEffectTypeCategory.HARMFUL) {
                        harmful.add(effect);
                    }
                }
                int slot;
                if (!harmful.isEmpty()) {
                    // does the surgery chest have milk in it
                    slot = inventory.first(Material.MILK_BUCKET);
                    if (slot == -1) {
                        continue;
                    }
                    // remove harmful effects from player
                    for (PotionEffect effect : harmful) {
                        player.removePotionEffect(effect.getType());
                    }
                    // remove milk bucket from chest - add one empty bucket
                    ChestUtility.removeItem(Material.MILK_BUCKET, chestData.location());
                } else if (player.getHealth() < 2.0) {
                    // else is the player's health below 2?
                    // does the surgery chest have healing potions in it?
                    slot = inventory.first(Material.POTION);
                    if (slot == -1) {
                        continue;
                    }
                    HealingData healing = ChestUtility.getFirstHealingPotion(inventory);
                    if (healing != null) {
                        // heal the player
                        /*
                        1 => potion of healing I  4HP ❤️❤️
                        2 => potion of healing II 8HP ❤️❤️❤️❤️
                         */
                        player.setHealth(player.getHealth() + (healing.potency() * 4));
                        // remove healing potion
                        ChestUtility.removeItem(healing.type(), chestData.location());
                    }
                }
            }
        }
    }
}
