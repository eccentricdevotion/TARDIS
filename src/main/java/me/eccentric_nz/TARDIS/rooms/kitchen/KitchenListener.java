package me.eccentric_nz.TARDIS.rooms.kitchen;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitchenListener implements Listener {

    private final TARDIS plugin;

    public KitchenListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerStarve(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.STARVATION) {
            return;
        }
        // get the tardis the player is in
        int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
        // if there is a kitchen room in the tardis
        ChestData chestData = ChestUtility.getKitchenData(plugin, id);
        if (chestData.chest()) {
            Inventory inventory = ChestUtility.getChestInventory(chestData.location());
            // is the kitchen chest empty - return
            if (inventory == null || inventory.isEmpty()) {
                return;
            }
            // does the kitchen chest have edible items in it
            for (ItemStack item : inventory.getContents()) {
                if (item == null || item.isEmpty()) {
                    continue;
                }
                Material material = item.getType();
                if (material.isEdible() && (material != Material.ROTTEN_FLESH && material != Material.POISONOUS_POTATO)) {
                    // feed the player
                    player.setFoodLevel(player.getFoodLevel() + EdibleLookup.EDIBLE.get(material));
                    // remove edible item from chest
                    ChestUtility.removeItem(material, chestData.location());
                }
            }
        }
    }
}
