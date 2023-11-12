package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GallifreyTradeSelectListener implements Listener {

    private final TARDIS plugin;

    public GallifreyTradeSelectListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /*
    @EventHandler
    public void onTradeSelect(TradeSelectEvent event) {
        // get the merchant
        Merchant merchant = event.getMerchant();
        // get the recipe
        int index = event.getIndex();
        MerchantRecipe recipe = merchant.getRecipe(index);
        ItemStack result = recipe.getResult();
        if (!result.hasItemMeta()) {
            return;
        }
        ItemMeta im = result.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        // is it a blueprint?
        if (pdc.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID()) && pdc.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
            // get the player
            if (event.getWhoClicked() instanceof Player player) {
                pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                List<String> lore = im.getLore();
                lore.set(2, player.getName());
                im.setLore(lore);
                ItemStack is = result.clone();
                is.setItemMeta(im);
                MerchantRecipe newRecipe = new MerchantRecipe(is, 1);
                newRecipe.addIngredient(recipe.getIngredients().get(0));
                merchant.setRecipe(index, newRecipe);
            }
        }
    }
    */

    @EventHandler
    public void onVillagerTrade(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager villager) {
            if (!villager.getLocation().getWorld().getName().equals("gallifrey")) {
                return;
            }
            if (!villager.getPersistentDataContainer().has(plugin.getBlueprintKey(), PersistentDataType.BOOLEAN)) {
                return;
            }
            event.setCancelled(true);
            Player player = event.getPlayer();
            int i = 0;
            for (MerchantRecipe recipe : villager.getRecipes()) {
                ItemStack result = recipe.getResult();
                if (!result.hasItemMeta()) {
                    return;
                }
                ItemMeta im = result.getItemMeta();
                PersistentDataContainer pdc = im.getPersistentDataContainer();
                // is it a blueprint?
                if (pdc.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID()) && pdc.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
                    // get the player
                    pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                    List<String> lore = im.getLore();
                    lore.set(2, player.getName());
                    im.setLore(lore);
                    ItemStack is = result.clone();
                    is.setItemMeta(im);
                    MerchantRecipe newRecipe = new MerchantRecipe(is, 1);
                    newRecipe.addIngredient(recipe.getIngredients().get(0));
                    villager.setRecipe(i, newRecipe);
                }
                i++;
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openMerchant(villager, false), 1L);
        }
    }
}
