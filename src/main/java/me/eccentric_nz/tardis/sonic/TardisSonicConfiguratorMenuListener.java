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
package me.eccentric_nz.tardis.sonic;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.ConfiguredSonic;
import me.eccentric_nz.tardis.database.resultset.ResultSetConfiguredSonic;
import me.eccentric_nz.tardis.enumeration.SonicConfig;
import me.eccentric_nz.tardis.listeners.TardisMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TardisSonicConfiguratorMenuListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;
    private final HashMap<UUID, ConfiguredSonic> sonics = new HashMap<>();

    public TardisSonicConfiguratorMenuListener(TardisPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicConfiguratorMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Sonic Configurator")) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                event.setCancelled(slot != 18);
                switch (slot) {
                    case 18:
                        // load configured sonic
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            loadSonic(view.getItem(18), player, view);
                            setOptions(player, view);
                        }, 1L);
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        // toggle option enabled / disabled
                        toggleOption(Objects.requireNonNull(view.getItem(slot)));
                    case 25:
                        // save selected options
                        saveConfiguredSonic(player, view);
                        break;
                    case 26:
                        close(player);
                        break;
                    default:
                        event.setCancelled(true);
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void loadSonic(ItemStack sonic, Player player, InventoryView view) {
        if (!isSonic(sonic)) {
            return;
        }
        ItemMeta im = sonic.getItemMeta();
        ConfiguredSonic configuredSonic;
        assert im != null;
        if (im.getPersistentDataContainer().has(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUuid())) {
            configuredSonic = getConfiguredSonic(im.getPersistentDataContainer().get(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUuid()), im);
        } else {
            configuredSonic = createConfiguredSonic(player, view);
        }
        if (configuredSonic != null) {
            sonics.put(player.getUniqueId(), configuredSonic);
        }
    }

    private void setOptions(Player player, InventoryView view) {
        ConfiguredSonic configuredSonic = sonics.get(player.getUniqueId());
        if (configuredSonic != null) {
            ItemStack bio = view.getItem(9);
            assert bio != null;
            bio.setType(configuredSonic.getBio().getMaterial());
            ItemMeta bim = bio.getItemMeta();
            assert bim != null;
            bim.setDisplayName(configuredSonic.getBio().getName());
            bim.setCustomModelData(configuredSonic.getBio().getCustomModelData());
            bio.setItemMeta(bim);
            ItemStack dia = view.getItem(10);
            assert dia != null;
            dia.setType(configuredSonic.getDiamond().getMaterial());
            ItemMeta dim = dia.getItemMeta();
            assert dim != null;
            dim.setDisplayName(configuredSonic.getDiamond().getName());
            dim.setCustomModelData(configuredSonic.getDiamond().getCustomModelData());
            dia.setItemMeta(dim);
            ItemStack eme = view.getItem(11);
            assert eme != null;
            eme.setType(configuredSonic.getEmerald().getMaterial());
            ItemMeta eim = eme.getItemMeta();
            assert eim != null;
            eim.setDisplayName(configuredSonic.getEmerald().getName());
            eim.setCustomModelData(configuredSonic.getEmerald().getCustomModelData());
            eme.setItemMeta(eim);
            ItemStack red = view.getItem(12);
            assert red != null;
            red.setType(configuredSonic.getRedstone().getMaterial());
            ItemMeta rim = red.getItemMeta();
            assert rim != null;
            rim.setDisplayName(configuredSonic.getRedstone().getName());
            rim.setCustomModelData(configuredSonic.getRedstone().getCustomModelData());
            red.setItemMeta(rim);
            ItemStack pai = view.getItem(13);
            assert pai != null;
            pai.setType(configuredSonic.getPainter().getMaterial());
            ItemMeta pim = pai.getItemMeta();
            assert pim != null;
            pim.setDisplayName(configuredSonic.getPainter().getName());
            pim.setCustomModelData(configuredSonic.getPainter().getCustomModelData());
            pai.setItemMeta(pim);
            ItemStack ign = view.getItem(14);
            assert ign != null;
            ign.setType(configuredSonic.getIgnite().getMaterial());
            ItemMeta iim = ign.getItemMeta();
            assert iim != null;
            iim.setDisplayName(configuredSonic.getIgnite().getName());
            iim.setCustomModelData(configuredSonic.getIgnite().getCustomModelData());
            ign.setItemMeta(iim);
            ItemStack arr = view.getItem(15);
            assert arr != null;
            arr.setType(configuredSonic.getArrow().getMaterial());
            ItemMeta aim = arr.getItemMeta();
            assert aim != null;
            aim.setDisplayName(configuredSonic.getArrow().getName());
            aim.setCustomModelData(configuredSonic.getArrow().getCustomModelData());
            arr.setItemMeta(aim);
            ItemStack kno = view.getItem(16);
            assert kno != null;
            kno.setType(configuredSonic.getKnockback().getMaterial());
            ItemMeta kim = kno.getItemMeta();
            assert kim != null;
            kim.setDisplayName(configuredSonic.getKnockback().getName());
            kim.setCustomModelData(configuredSonic.getKnockback().getCustomModelData());
            kno.setItemMeta(kim);
        }
    }

    private void toggleOption(ItemStack option) {
        ItemMeta im = option.getItemMeta();
        Material cmd = option.getType();
        switch (cmd) {
            case LIME_WOOL:
                // disable
                assert im != null;
                im.setDisplayName("Disabled");
                option.setType(Material.RED_WOOL);
                break;
            case RED_WOOL:
                // enable
                assert im != null;
                im.setDisplayName("Enabled");
                option.setType(Material.LIME_WOOL);
                break;
            default:
                // not upgraded, do nothing
                break;
        }
        option.setItemMeta(im);
    }

    private ConfiguredSonic getConfiguredSonic(UUID sonicUuid, ItemMeta im) {
        ResultSetConfiguredSonic rscs = new ResultSetConfiguredSonic(plugin, sonicUuid);
        if (rscs.resultSet()) {
            ConfiguredSonic configuredSonic = rscs.getConfiguredSonic();
            // check if there are any new upgrades
            List<String> lore = im.getLore();
            if (lore != null) {
                for (int i = 1; i < lore.size(); i++) {
                    String upgrade = lore.get(i);
                    switch (upgrade) {
                        case "Bio-scanner Upgrade":
                            configuredSonic.setBio(SonicConfig.ENABLED);
                            break;
                        case "Diamond Upgrade":
                            configuredSonic.setDiamond(SonicConfig.ENABLED);
                            break;
                        case "Emerald Upgrade":
                            configuredSonic.setEmerald(SonicConfig.ENABLED);
                            break;
                        case "Redstone Upgrade":
                            configuredSonic.setRedstone(SonicConfig.ENABLED);
                            break;
                        case "Painter Upgrade":
                            configuredSonic.setPainter(SonicConfig.ENABLED);
                            break;
                        case "Ignite Upgrade":
                            configuredSonic.setIgnite(SonicConfig.ENABLED);
                            break;
                        case "Pickup Arrows Upgrade":
                            configuredSonic.setArrow(SonicConfig.ENABLED);
                            break;
                        case "Knockback Upgrade":
                            configuredSonic.setKnockback(SonicConfig.ENABLED);
                            break;
                        default:
                            break;
                    }
                }
            }
            return configuredSonic;
        }
        return null;
    }

    private void saveConfiguredSonic(Player player, InventoryView view) {
        List<String> upgrades = new ArrayList<>();
        upgrades.add("Upgrades:");
        //update configured sonic
        ConfiguredSonic configuredSonic = sonics.get(player.getUniqueId());
        if (configuredSonic != null) {
            int bio = getSonicConfig(9, view);
            if (bio == 1) {
                upgrades.add("Bio-scanner Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[bio]);
            int dia = getSonicConfig(10, view);
            if (dia == 1) {
                upgrades.add("Diamond Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[dia]);
            int eme = getSonicConfig(11, view);
            if (eme == 1) {
                upgrades.add("Emerald Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[eme]);
            int red = getSonicConfig(12, view);
            if (red == 1) {
                upgrades.add("Redstone Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[red]);
            int pai = getSonicConfig(13, view);
            if (pai == 1) {
                upgrades.add("Painter Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[pai]);
            int ign = getSonicConfig(14, view);
            if (ign == 1) {
                upgrades.add("Ignite Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[ign]);
            int arr = getSonicConfig(15, view);
            if (arr == 1) {
                upgrades.add("Pickup Arrows Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[arr]);
            int kno = getSonicConfig(16, view);
            if (kno == 1) {
                upgrades.add("Knockback Upgrade");
            }
            configuredSonic.setBio(SonicConfig.values()[kno]);
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
            HashMap<String, Object> where = new HashMap<>();
            where.put("sonicId", configuredSonic.getSonicId());
            plugin.getQueryFactory().doUpdate("sonic", set, where);
            // set sonic lore
            ItemStack sonic = view.getItem(18);
            if (isSonic(sonic)) {
                ItemMeta im = sonic.getItemMeta();
                assert im != null;
                if (upgrades.size() > 1) {
                    im.setLore(upgrades);
                } else {
                    im.setLore(null);
                }
                sonic.setItemMeta(im);
            }
        }
    }

    private int getSonicConfig(int slot, InventoryView view) {
        ItemStack option = view.getItem(slot);
        assert option != null;
        return switch (option.getType()) {
            case LIME_WOOL -> 1;
            case RED_WOOL -> 2;
            default -> 0;
        };
    }

    private ConfiguredSonic createConfiguredSonic(Player player, InventoryView view) {
        // get sonic in slot 18
        ItemStack is = view.getItem(18);
        if (isSonic(is)) {
            ItemMeta im = is.getItemMeta();
            // get the upgrades from the lore
            assert im != null;
            List<String> lore = im.getLore();
            UUID uuid = player.getUniqueId();
            int bio = lore != null && lore.contains("Bio-scanner Upgrade") ? 1 : 0;
            int diamond = lore != null && lore.contains("Diamond Upgrade") ? 1 : 0;
            int emerald = lore != null && lore.contains("Emerald Upgrade") ? 1 : 0;
            int redstone = lore != null && lore.contains("Redstone Upgrade") ? 1 : 0;
            int painter = lore != null && lore.contains("Painter Upgrade") ? 1 : 0;
            int ignite = lore != null && lore.contains("Ignite Upgrade") ? 1 : 0;
            int arrow = lore != null && lore.contains("Pickup Arrows Upgrade") ? 1 : 0;
            int knockback = lore != null && lore.contains("Knockback Upgrade") ? 1 : 0;
            // create a new UUID
            UUID sonicUuid = UUID.randomUUID();
            // set the UUID to the sonic
            im.getPersistentDataContainer().set(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUuid(), sonicUuid);
            is.setItemMeta(im);
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
            set.put("sonic_uuid", sonicUuid.toString());
            // insert into database
            int id = plugin.getQueryFactory().doSyncInsert("sonic", set);
            // create  the configured sonic
            return new ConfiguredSonic(id, uuid, bio, diamond, emerald, redstone, painter, ignite, arrow, knockback, sonicUuid);
        }
        return null;
    }

    private boolean isSonic(ItemStack is) {
        if (is != null) {
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                assert im != null;
                if (im.hasDisplayName()) {
                    return (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver"));
                }
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onKeyMenuClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!title.equals(ChatColor.DARK_RED + "Sonic Configurator")) {
            return;
        }
        ItemStack sonic = view.getItem(18);
        if (sonic != null) {
            Player p = (Player) event.getPlayer();
            Location loc = p.getLocation();
            Objects.requireNonNull(loc.getWorld()).dropItemNaturally(loc, sonic);
            view.setItem(18, new ItemStack(Material.AIR));
        }
    }
}
