/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.planets;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.persistence.PersistentDataContainerView;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.view.MerchantView;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
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
            PersistentDataContainerView check = recipe.getResult().getPersistentDataContainer();
            if (check.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID()) && check.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
                // add a new trade option
                // index 0 = room, index 1 = console
                MerchantRecipe trade = index < 1 ? new GallifreyBlueprintTrade(plugin).getRoom() : new GallifreyBlueprintTrade(plugin).getConsole();
                // add the player's UUID and name
                trade.getResult().editPersistentDataContainer(pdc -> pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId()));
                List<Component> lore = new ArrayList<>(trade.getResult().getData(DataComponentTypes.LORE).lines());
                lore.set(2, Component.text(player.getName()));
                trade.getResult().setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                MerchantRecipe newRecipe = new MerchantRecipe(trade.getResult(), 1);
                newRecipe.addIngredient(trade.getIngredients().getFirst());
                merchant.setRecipe(index, newRecipe);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BLUEPRINT_REFRESH");
            }
        }
    }

    @EventHandler
    public void onVillagerTrade(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Villager villager) {
            if (!villager.getLocation().getWorld().getKey().getKey().equals("gallifrey")) {
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
                List<Component> lines = result.getData(DataComponentTypes.LORE).lines();
                if (lines.isEmpty()) {
                    return;
                }
                PersistentDataContainerView pdcv = result.getPersistentDataContainer();
                // is it a blueprint?
                if (pdcv.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID()) && pdcv.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
                    // get the player
                    result.editPersistentDataContainer(pdc->pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId()));
                    List<Component> lore = new ArrayList<>(lines);
                    lore.set(2, Component.text(player.getName()));
                    result.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                    ItemStack is = result.clone();
                    // TODO check this
                    is.copyDataFrom(result, dataComponentType -> true);
                    MerchantRecipe newRecipe = new MerchantRecipe(is, 1);
                    newRecipe.addIngredient(recipe.getIngredients().getFirst());
                    villager.setRecipe(i, newRecipe);
                }
                i++;
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                MerchantView merchant = getView(villager, player);
                player.openInventory(merchant);
            }, 1L);
        }
    }

    public MerchantView getView(Villager villager, Player player) {
        return MenuType.MERCHANT.builder()
                .merchant(villager)
                .checkReachable(true)
                .title(villager.customName())
                .build(player);
    }
}
