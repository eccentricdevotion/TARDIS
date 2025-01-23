package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
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

    @EventHandler
    public void onTradeSelect(TradeSelectEvent event) {
        // get the merchant
        Merchant merchant = event.getMerchant();
        // get the recipe
        int index = event.getIndex();
        MerchantRecipe recipe = merchant.getRecipe(index);
        if (recipe.getUses() == recipe.getMaxUses() && event.getWhoClicked() instanceof Player player) {
            // is it a blueprint?
            ItemMeta im = recipe.getResult().getItemMeta();
            PersistentDataContainer check = im.getPersistentDataContainer();
            if (check.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID()) && check.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
                // add a new trade option
                // index 0 = room, index 1 = console
                MerchantRecipe trade = index < 1 ? new GallifreyBlueprintTrade(plugin).getRoom() : new GallifreyBlueprintTrade(plugin).getConsole();
                // add the player's UUID and name
                ItemMeta meta = trade.getResult().getItemMeta();
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                List<String> lore = im.getLore();
                lore.set(2, player.getName());
                im.lore(lore);
                trade.getResult().setItemMeta(im);
                MerchantRecipe newRecipe = new MerchantRecipe(trade.getResult(), 1);
                newRecipe.addIngredient(trade.getIngredients().getFirst());
                merchant.setRecipe(index, newRecipe);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BLUEPRINT_REFRESH");
            }
        }
    }

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
                    im.lore(lore);
                    ItemStack is = result.clone();
                    is.setItemMeta(im);
                    MerchantRecipe newRecipe = new MerchantRecipe(is, 1);
                    newRecipe.addIngredient(recipe.getIngredients().getFirst());
                    villager.setRecipe(i, newRecipe);
                }
                i++;
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openMerchant(villager, false), 1L);
        }
    }
}
