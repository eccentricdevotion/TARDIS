/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.artron;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisArtronFurnaceListener implements Listener {

    private final TardisPlugin plugin;
    private final double burnFactor;
    private final short cookTime;

    public TardisArtronFurnaceListener(TardisPlugin plugin) {
        this.plugin = plugin;
        burnFactor = plugin.getArtronConfig().getInt("artron_furnace.burn_limit") * plugin.getArtronConfig().getDouble("artron_furnace.burn_time");
        cookTime = (short) (200 * this.plugin.getArtronConfig().getDouble("artron_furnace.cook_time"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        String l = furnace.getLocation().toString();
        if (plugin.getTardisHelper().isArtronFurnace(event.getBlock())) {
            ItemStack is = event.getFuel().clone();
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                assert im != null;
                if (im.hasDisplayName() && im.getDisplayName().equals("Artron Storage Cell")) {
                    List<String> lore = im.getLore();
                    assert lore != null;
                    if (!lore.get(1).equals("0")) {
                        // track furnace
                        plugin.getTrackerKeeper().getArtronFurnaces().add(l);
                        TardisSounds.playTardisSound(furnace.getLocation(), "artron_furnace");
                        // get charge level
                        int charge_level = TardisNumberParsers.parseInt(lore.get(1));
                        double percentage = charge_level / plugin.getArtronConfig().getDouble("full_charge");
                        // determine burn time
                        int burnTime = (int) (percentage * burnFactor);
                        event.setBurnTime(burnTime);
                        furnace.setCookTimeTotal(cookTime);
                        // return an empty cell
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            lore.set(1, "0");
                            im.setLore(lore);
                            is.setItemMeta(im);
                            is.removeEnchantment(Enchantment.DURABILITY);
                            furnace.getInventory().setItem(1, is);
                        }, 2L);
                    }
                }
            } else {
                plugin.getTrackerKeeper().getArtronFurnaces().remove(l);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void furnaceSmeltEvent(FurnaceSmeltEvent event) {
        // Setting cookTime after cooking an item (and the fuel is still burning)
        Furnace furnace = (Furnace) event.getBlock().getState();
        if (plugin.getTardisHelper().isArtronFurnace(event.getBlock()) && plugin.getTrackerKeeper().getArtronFurnaces().contains(furnace.getLocation().toString())) {
            furnace.setCookTimeTotal(cookTime);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof FurnaceInventory)) {
            return;
        }
        Furnace furnace = (Furnace) event.getInventory().getHolder();
        // Setting cookTime when the furnace is empty but already burning
        assert furnace != null;
        if (plugin.getTardisHelper().isArtronFurnace(furnace.getBlock()) && plugin.getTrackerKeeper().getArtronFurnaces().contains(furnace.getLocation().toString()) && (event.getSlot() == 0 || event.getSlot() == 1) // Click in one of the two slots
                && Objects.requireNonNull(event.getCursor()).getType() != Material.AIR // With an item
                && furnace.getCookTime() > cookTime) {         // The furnace is not already burning something
            furnace.setCookTimeTotal(cookTime);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnacePlace(BlockPlaceEvent event) {
        if (!event.getBlock().getType().equals(Material.FURNACE)) {
            return;
        }
        if (!event.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!Objects.requireNonNull(event.getItemInHand().getItemMeta()).hasDisplayName()) {
            return;
        }
        if (!event.getItemInHand().getItemMeta().getDisplayName().equals("TARDIS Artron Furnace")) {
            return;
        }
        Player player = event.getPlayer();
        if (plugin.getUtils().inTardisWorld(player)) {
            event.setCancelled(true);
            // only in TARDIS
            TardisMessage.send(player, "NOT_IN_TARDIS");
        }
        if (TardisPermission.hasPermission(player, "tardis.furnace")) {
            Block b = event.getBlock();
            if (plugin.getArtronConfig().getBoolean("artron_furnace.particles")) {
                plugin.getGeneralKeeper().getArtronFurnaces().add(b);
            }
            plugin.getTardisHelper().nameFurnaceGui(b, "TARDIS Artron Furnace");
        } else {
            event.setCancelled(true);
            TardisMessage.send(player, "NO_PERM_FURNACE");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnaceBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.getType().equals(Material.FURNACE)) {
            return;
        }
        if (plugin.getTardisHelper().isArtronFurnace(event.getBlock())) {
            event.setCancelled(true);
            if (plugin.getArtronConfig().getBoolean("artron_furnace.particles")) {
                plugin.getGeneralKeeper().getArtronFurnaces().remove(block);
            }
            ItemStack is = new ItemStack(Material.FURNACE, 1);
            ItemMeta im = is.getItemMeta();
            assert im != null;
            im.setDisplayName("TARDIS Artron Furnace");
            im.setCustomModelData(RecipeItem.TARDIS_ARTRON_FURNACE.getCustomModelData());
            is.setItemMeta(im);
            block.setBlockData(TardisConstants.AIR);
            block.getWorld().dropItemNaturally(event.getPlayer().getLocation(), is);
        }
    }
}
