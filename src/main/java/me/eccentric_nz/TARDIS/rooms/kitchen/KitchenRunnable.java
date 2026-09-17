package me.eccentric_nz.TARDIS.rooms.kitchen;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitchenRunnable implements Runnable {

    private final TARDIS plugin;

    public KitchenRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void run() {
        // get all players in TARDIS worlds
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL) {
                continue;
            }
            // must be in a TARDIS
            if (!plugin.getUtils().inTARDISWorld(player)) {
                continue;
            }
            // get the tardis the player is in
            int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
            // if there is a kitchen room in the tardis
            ChestData chestData = ChestUtility.getKitchenData(plugin, id);
            if (chestData.chest()) {
                Inventory inventory = ChestUtility.getChestInventory(chestData.location());
                // is the kitchen chest empty - return
                if (inventory == null || inventory.isEmpty()) {
                    continue;
                }
                if (player.getFoodLevel() < 2.0) { // else is the player's food level below 2?
                    // does the kitchen chest have edible items in it?
                    for (ItemStack item : inventory.getContents()) {
                        if (player.getFoodLevel() > 19.0) {
                            return;
                        }
                        if (item == null || item.isEmpty()) {
                            continue;
                        }
                        Material material = item.getType();
                        if (material.isEdible()
                                // potential negative effects given
                                && material != Material.ROTTEN_FLESH && material != Material.POISONOUS_POTATO
                                && material != Material.SUSPICIOUS_STEW && material != Material.SPIDER_EYE
                                && material != Material.CHICKEN
                        ) {
                            Pair<Integer, Float> food = EdibleLookup.getFoodValues(material);
                            // feed the player
                            player.setFoodLevel(player.getFoodLevel() + food.getFirst());
                            player.setSaturation(player.getSaturation() + food.getSecond());
                            // remove edible item from chest
                            ChestUtility.removeItem(material, chestData.location());
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "FEED", TARDISStringUtils.words(material.toString()));
                        }
                    }
                }
            }
        }
    }
}
