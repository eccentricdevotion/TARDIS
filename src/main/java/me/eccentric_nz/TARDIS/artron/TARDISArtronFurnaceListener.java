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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.ArtronFurnaceUtils;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
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

/**
 * @author eccentric_nz
 */
public class TARDISArtronFurnaceListener implements Listener {

    private final TARDIS plugin;
    private final double burnFactor;
    private final int cookTime;

    public TARDISArtronFurnaceListener(TARDIS plugin) {
        this.plugin = plugin;
        burnFactor = plugin.getArtronConfig().getInt("artron_furnace.burn_limit") * plugin.getArtronConfig().getDouble("artron_furnace.burn_time");
        cookTime = 200 * this.plugin.getArtronConfig().getInt("artron_furnace.cook_time");
    }

    public static void setLit(Block block, boolean lit) {
        ItemDisplay display = TARDISDisplayItemUtils.get(block);
        if (display != null) {
            ItemStack itemStack = display.getItemStack();
            ItemMeta im = itemStack.getItemMeta();
            im.setItemModel(lit ? Whoniverse.ARTRON_FURNACE_LIT.getKey() : Whoniverse.ARTRON_FURNACE.getKey());
            itemStack.setItemMeta(im);
            display.setItemStack(itemStack);
            display.setBrightness(new Display.Brightness(15, 15));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        String l = furnace.getLocation().toString();
        if (plugin.getTardisHelper().isArtronFurnace(event.getBlock())) {
            ItemStack is = event.getFuel().clone();
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Artron Storage Cell")) {
                    List<Component> lore = im.lore();
                    String one = ComponentUtils.stripColour(lore.get(1));
                    if (!one.equals("0")) {
                        // track furnace
                        plugin.getTrackerKeeper().getArtronFurnaces().add(l);
                        setLit(event.getBlock(), true);
                        TARDISSounds.playTARDISSound(furnace.getLocation(), "artron_furnace");
                        // get charge level
                        int charge_level = TARDISNumberParsers.parseInt(one);
                        double percentage = charge_level / plugin.getArtronConfig().getDouble("full_charge");
                        // determine burn time
                        int burnTime = (int) (percentage * burnFactor);
                        event.setBurnTime(burnTime);
                        furnace.setCookTimeTotal(cookTime);
                        // return an empty cell
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            lore.set(1, Component.text("0"));
                            im.lore(lore);
                            im.setEnchantmentGlintOverride(null);
                            is.setItemMeta(im);
                            is.removeEnchantment(Enchantment.UNBREAKING);
                            furnace.getInventory().setItem(1, is);
                        }, 2L);
                    }
                }
            } else {
                setLit(event.getBlock(), false);
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
        Furnace furnace = (Furnace) event.getInventory().getHolder(false);
        // setting cookTime when the furnace is empty but already burning
        if (plugin.getTardisHelper().isArtronFurnace(furnace.getBlock())
                && plugin.getTrackerKeeper().getArtronFurnaces().contains(furnace.getLocation().toString())
                && (event.getSlot() == 0 || event.getSlot() == 1) // click in one of the two slots
                && event.getCursor().getType() != Material.AIR    // with an item
                && furnace.getCookTime() > cookTime) {            // the furnace is not already burning something
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
        if (!event.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!ComponentUtils.endsWith(event.getItemInHand().getItemMeta().displayName(), "TARDIS Artron Furnace")) {
            return;
        }
        Player player = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(player)) {
            event.setCancelled(true);
            // only in TARDIS
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return;
        }
        if (TARDISPermission.hasPermission(player, "tardis.furnace")) {
            Block b = event.getBlock();
            if (plugin.getArtronConfig().getBoolean("artron_furnace.particles")) {
                plugin.getGeneralKeeper().getArtronFurnaces().add(b);
            }
            plugin.getTardisHelper().nameFurnaceGUI(b, "TARDIS Artron Furnace");
            // set Item Display
            TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ARTRON_FURNACE, b, -1);
        } else {
            event.setCancelled(true);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_FURNACE");
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
            if (plugin.getArtronConfig().getBoolean("artron_furnace.tardis_powered")) {
                ArtronFurnaceUtils.remove(block.getLocation().toString(), event.getPlayer(), plugin);
            }
            ItemStack is = ItemStack.of(Material.FURNACE, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(ComponentUtils.toWhite("TARDIS Artron Furnace"));
            is.setItemMeta(im);
            TARDISDisplayItemUtils.remove(block);
            block.setBlockData(TARDISConstants.AIR);
            block.getWorld().dropItemNaturally(event.getPlayer().getLocation(), is);
        }
    }
}
