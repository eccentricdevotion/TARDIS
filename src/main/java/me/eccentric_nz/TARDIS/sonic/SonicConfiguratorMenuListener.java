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
package me.eccentric_nz.TARDIS.sonic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ConfiguredSonic;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConfiguredSonic;
import me.eccentric_nz.TARDIS.enumeration.SonicConfig;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SonicConfiguratorMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, ConfiguredSonic> sonics = new HashMap<>();

    public SonicConfiguratorMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicConfiguratorMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!(event.getInventory().getHolder(false) instanceof SonicConfiguratorInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISSonicConfiguratorMenuListener");
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(slot != 45);
        switch (slot) {
            // load configured sonic
            case 45 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                loadSonic(view.getItem(45), player, view);
                setOptions(player, view);
            }, 1L);
            case 9, 10, 11, 12, 13, 14, 15, 16, 17, 27 -> toggleOption(view, slot); // toggle option enabled / disabled
            case 52 -> saveConfiguredSonic(player, view); // save selected options
            case 53 -> close(player);
            default -> event.setCancelled(true);
        }
    }

    private void loadSonic(ItemStack sonic, Player player, InventoryView view) {
        if (!TARDISStaticUtils.isSonic(sonic)) {
            return;
        }
        ConfiguredSonic configuredSonic;
        if (sonic.getPersistentDataContainer().has(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID())) {
            configuredSonic = getConfiguredSonic(sonic.getPersistentDataContainer().get(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID()), sonic.getData(DataComponentTypes.LORE));
        } else {
            configuredSonic = createConfiguredSonic(player, view);
        }
        if (configuredSonic != null) {
            sonics.put(player.getUniqueId(), configuredSonic);
        }
    }

    private void setOptions(Player player, InventoryView view) {
        ConfiguredSonic configuredSonic = sonics.get(player.getUniqueId());
        if (configuredSonic == null) {
            return;
        }
        ItemStack bio = view.getItem(9);
        bio.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getBio().getName()));
        ItemStack biosub = ItemStack.of(configuredSonic.getBio().getMaterial());
        view.setItem(9, biosub);
        ItemStack diamond = view.getItem(10);
        diamond.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getDiamond().getName()));
        ItemStack diasub = ItemStack.of(configuredSonic.getDiamond().getMaterial());
        view.setItem(10, diasub);
        ItemStack emerald = view.getItem(11);
        emerald.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getEmerald().getName()));
        ItemStack emesub = ItemStack.of(configuredSonic.getEmerald().getMaterial());
        view.setItem(11, emesub);
        ItemStack redstone = view.getItem(12);
        redstone.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getRedstone().getName()));
        ItemStack redsub = ItemStack.of(configuredSonic.getRedstone().getMaterial());
        view.setItem(12, redsub);
        ItemStack painter = view.getItem(13);
        painter.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getPainter().getName()));
        ItemStack paisub = ItemStack.of(configuredSonic.getPainter().getMaterial());
        view.setItem(13, paisub);
        ItemStack ignite = view.getItem(14);
        ignite.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getIgnite().getName()));
        ItemStack ignsub = ItemStack.of(configuredSonic.getIgnite().getMaterial());
        view.setItem(14, ignsub);
        ItemStack arr = view.getItem(15);
        arr.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getArrow().getName()));
        ItemStack arrsub = ItemStack.of(configuredSonic.getArrow().getMaterial());
        view.setItem(15, arrsub);
        ItemStack knockback = view.getItem(16);
        knockback.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getKnockback().getName()));
        ItemStack knocksub = ItemStack.of(configuredSonic.getKnockback().getMaterial());
        view.setItem(16, knocksub);
        ItemStack brush = view.getItem(17);
        brush.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getBrush().getName()));
        ItemStack brusub = ItemStack.of(configuredSonic.getBrush().getMaterial());
        view.setItem(17, brusub);
        ItemStack conversion = view.getItem(27);
        conversion.setData(DataComponentTypes.CUSTOM_NAME, Component.text(configuredSonic.getConversion().getName()));
        ItemStack consub = ItemStack.of(configuredSonic.getConversion().getMaterial());
        view.setItem(27, consub);
    }

    private void toggleOption(InventoryView view, int slot) {
        ItemStack option = view.getItem(slot);
        Material cmd = option.getType();
        Material m = Material.LIME_WOOL;
        switch (cmd) {
            case LIME_WOOL -> {
                // disable
                option.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Disabled"));
                m = Material.RED_WOOL;
            }
            case RED_WOOL -> {
                // enable
                option.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Enabled"));
            }
            default -> m = Material.RED_WOOL; // not upgraded, do nothing
        }
        ItemStack sub = ItemStack.of(m);
        sub.copyDataFrom(option, dataComponentType -> true);
        view.setItem(slot, sub);
    }

    private ConfiguredSonic getConfiguredSonic(UUID sonic_uuid, ItemLore im) {
        ResultSetConfiguredSonic rscs = new ResultSetConfiguredSonic(plugin, sonic_uuid);
        if (rscs.resultSet()) {
            ConfiguredSonic configuredSonic = rscs.getConfiguredSonic();
            // check if there are any new upgrades
            if (im != null) {
                List<Component> lore = im.lines();
                for (int i = 1; i < lore.size(); i++) {
                    String upgrade = ComponentUtils.stripColour(lore.get(i));
                    switch (upgrade) {
                        case "Bio-scanner Upgrade" -> configuredSonic.setBio(SonicConfig.ENABLED);
                        case "Diamond Upgrade" -> configuredSonic.setDiamond(SonicConfig.ENABLED);
                        case "Emerald Upgrade" -> configuredSonic.setEmerald(SonicConfig.ENABLED);
                        case "Redstone Upgrade" -> configuredSonic.setRedstone(SonicConfig.ENABLED);
                        case "Painter Upgrade" -> configuredSonic.setPainter(SonicConfig.ENABLED);
                        case "Ignite Upgrade" -> configuredSonic.setIgnite(SonicConfig.ENABLED);
                        case "Pickup Arrows Upgrade" -> configuredSonic.setArrow(SonicConfig.ENABLED);
                        case "Knockback Upgrade" -> configuredSonic.setKnockback(SonicConfig.ENABLED);
                        case "Brush Upgrade" -> configuredSonic.setBrush(SonicConfig.ENABLED);
                        case "Conversion Upgrade" -> configuredSonic.setConversion(SonicConfig.ENABLED);
                        default -> {
                        }
                    }
                }
            }
            return configuredSonic;
        }
        return null;
    }

    private void saveConfiguredSonic(Player player, InventoryView view) {
        List<Component> upgrades = new ArrayList<>();
        upgrades.add(Component.text("Upgrades:"));
        //update configured sonic
        ConfiguredSonic configuredSonic = sonics.get(player.getUniqueId());
        if (configuredSonic == null) {
            return;
        }
        int bio = getSonicConfig(9, view);
        if (bio == 1) {
            upgrades.add(Component.text("Bio-scanner Upgrade"));
        }
        configuredSonic.setBio(SonicConfig.values()[bio]);
        int dia = getSonicConfig(10, view);
        if (dia == 1) {
            upgrades.add(Component.text("Diamond Upgrade"));
        }
        configuredSonic.setDiamond(SonicConfig.values()[dia]);
        int eme = getSonicConfig(11, view);
        if (eme == 1) {
            upgrades.add(Component.text("Emerald Upgrade"));
        }
        configuredSonic.setEmerald(SonicConfig.values()[eme]);
        int red = getSonicConfig(12, view);
        if (red == 1) {
            upgrades.add(Component.text("Redstone Upgrade"));
        }
        configuredSonic.setRedstone(SonicConfig.values()[red]);
        int pai = getSonicConfig(13, view);
        if (pai == 1) {
            upgrades.add(Component.text("Painter Upgrade"));
        }
        configuredSonic.setPainter(SonicConfig.values()[pai]);
        int ign = getSonicConfig(14, view);
        if (ign == 1) {
            upgrades.add(Component.text("Ignite Upgrade"));
        }
        configuredSonic.setIgnite(SonicConfig.values()[ign]);
        int arr = getSonicConfig(15, view);
        if (arr == 1) {
            upgrades.add(Component.text("Pickup Arrows Upgrade"));
        }
        configuredSonic.setArrow(SonicConfig.values()[arr]);
        int kno = getSonicConfig(16, view);
        if (kno == 1) {
            upgrades.add(Component.text("Knockback Upgrade"));
        }
        configuredSonic.setKnockback(SonicConfig.values()[kno]);
        int bru = getSonicConfig(17, view);
        if (bru == 1) {
            upgrades.add(Component.text("Brush Upgrade"));
        }
        configuredSonic.setBrush(SonicConfig.values()[bru]);
        int con = getSonicConfig(27, view);
        if (con == 1) {
            upgrades.add(Component.text("Conversion Upgrade"));
        }
        configuredSonic.setBrush(SonicConfig.values()[bru]);
        sonics.put(player.getUniqueId(), configuredSonic);
        // prepare data for database insertion
        HashMap<String, Object> set = new HashMap<>();
        set.put("bio", bio);
        set.put("diamond", dia);
        set.put("emerald", eme);
        set.put("redstone", red);
        set.put("painter", pai);
        set.put("ignite", ign);
        set.put("arrow", arr);
        set.put("knockback", kno);
        set.put("brush", bru);
        HashMap<String, Object> where = new HashMap<>();
        where.put("sonic_id", configuredSonic.getSonic_id());
        plugin.getQueryFactory().doUpdate("sonic", set, where);
        // set sonic lore
        ItemStack sonic = view.getItem(45);
        if (TARDISStaticUtils.isSonic(sonic)) {
            if (upgrades.size() > 1) {
                sonic.setData(DataComponentTypes.LORE, ItemLore.lore(upgrades));
            } else {
                sonic.resetData(DataComponentTypes.LORE);
            }
        }
    }

    private int getSonicConfig(int slot, InventoryView view) {
        ItemStack option = view.getItem(slot);
        return switch (option.getType()) {
            case LIME_WOOL -> 1;
            case RED_WOOL -> 2;
            default -> 0;
        };
    }

    private ConfiguredSonic createConfiguredSonic(Player player, InventoryView view) {
        // get sonic in slot 45
        ItemStack is = view.getItem(45);
        if (TARDISStaticUtils.isSonic(is)) {
            // get the upgrades from the lore
            ItemLore lore = is.getData(DataComponentTypes.LORE);
            UUID uuid = player.getUniqueId();
            int bio = lore != null && lore.lines().contains(Component.text("Bio-scanner Upgrade")) ? 1 : 0;
            int diamond = lore != null && lore.lines().contains(Component.text("Diamond Upgrade")) ? 1 : 0;
            int emerald = lore != null && lore.lines().contains(Component.text("Emerald Upgrade")) ? 1 : 0;
            int redstone = lore != null && lore.lines().contains(Component.text("Redstone Upgrade")) ? 1 : 0;
            int painter = lore != null && lore.lines().contains(Component.text("Painter Upgrade")) ? 1 : 0;
            int ignite = lore != null && lore.lines().contains(Component.text("Ignite Upgrade")) ? 1 : 0;
            int arrow = lore != null && lore.lines().contains(Component.text("Pickup Arrows Upgrade")) ? 1 : 0;
            int knockback = lore != null && lore.lines().contains(Component.text("Knockback Upgrade")) ? 1 : 0;
            int brush = lore != null && lore.lines().contains(Component.text("Brush Upgrade")) ? 1 : 0;
            int conversion = lore != null && lore.lines().contains(Component.text("Conversion Upgrade")) ? 1 : 0;
            // create a new UUID
            UUID sonic_uuid = UUID.randomUUID();
            // set the UUID to the sonic
            is.editPersistentDataContainer(pdc ->pdc.set(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID(), sonic_uuid));
            // prepare data for database insertion
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            set.put("bio", bio);
            set.put("diamond", diamond);
            set.put("emerald", emerald);
            set.put("redstone", redstone);
            set.put("painter", painter);
            set.put("ignite", ignite);
            set.put("arrow", arrow);
            set.put("knockback", knockback);
            set.put("brush", brush);
            set.put("conversion", conversion);
            set.put("sonic_uuid", sonic_uuid.toString());
            // insert into database
            int id = plugin.getQueryFactory().doSyncInsert("sonic", set);
            // create the configured sonic
            return new ConfiguredSonic(id, uuid, bio, diamond, emerald, redstone, painter, ignite, arrow, knockback, brush, conversion, sonic_uuid);
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicConfiguratorMenuClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof SonicConfiguratorInventory)) {
            return;
        }
        InventoryView view = event.getView();
        ItemStack sonic = view.getItem(45);
        if (sonic != null) {
            Player p = (Player) event.getPlayer();
            Location loc = p.getLocation();
            loc.getWorld().dropItemNaturally(loc, sonic);
            view.setItem(45, ItemStack.of(Material.AIR));
        }
    }
}
