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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISonicGenerator;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISSonicGeneratorMenuListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, Integer> costs;
    private final HashMap<String, String> fields;
    private Location location;

    public TARDISSonicGeneratorMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        costs = getCosts();
        fields = getFields();
    }

    private HashMap<String, Integer> getCosts() {
        double full = plugin.getArtronConfig().getDouble("full_charge") / 100.0d;
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Standard Sonic", (int) (plugin.getArtronConfig().getDouble("sonic_generator.standard") * full));
        map.put("Bio-scanner Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full));
        map.put("Redstone Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.redstone") * full));
        map.put("Diamond Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full));
        map.put("Emerald Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full));
        map.put("Painter Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full));
        map.put("Ignite Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full));
        map.put("Pickup Arrows Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full));
        map.put("Knockback Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full));
        return map;
    }

    private HashMap<String, String> getFields() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Bio-scanner Upgrade", "bio");
        map.put("Redstone Upgrade", "redstone");
        map.put("Diamond Upgrade", "diamond");
        map.put("Emerald Upgrade", "emerald");
        map.put("Painter Upgrade", "painter");
        map.put("Ignite Upgrade", "ignite");
        map.put("Pickup Arrows Upgrade", "arrow");
        map.put("Knockback Upgrade", "knockback");
        return map;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Sonic Generator")) {
            Player p = (Player) event.getWhoClicked();
            location = plugin.getTrackerKeeper().getSonicGenerators().get(p.getUniqueId());
            int slot = event.getRawSlot();
            ItemStack sonic;
            ItemMeta sonic_im;
            boolean slotWasNull = false;
            if (slot >= 0 && slot < 54) {
                event.setCancelled(true);
                switch (slot) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        // set display name of sonic in slot 49
                        sonic = view.getItem(49);
                        if (sonic == null) {
                            sonic = new ItemStack(Material.BLAZE_ROD, 1);
                            slotWasNull = true;
                        }
                        // get Display name of selected sonic
                        ItemStack choice = view.getItem(slot);
                        assert choice != null;
                        ItemMeta choice_im = choice.getItemMeta();
                        assert choice_im != null;
                        String choice_name = choice_im.getDisplayName();
                        sonic_im = sonic.getItemMeta();
                        assert sonic_im != null;
                        sonic_im.setDisplayName(choice_name);
                        sonic_im.setCustomModelData(choice_im.getCustomModelData());
                        sonic.setItemMeta(sonic_im);
                        if (slotWasNull) {
                            view.setItem(49, sonic);
                            setCost(view, costs.get("Standard Sonic"));
                        }

                        break;
                    case 27:
                        // reset to standard
                        sonic = view.getItem(49);
                        if (sonic == null) {
                            sonic = new ItemStack(Material.BLAZE_ROD, 1);
                            slotWasNull = true;
                        }
                        sonic_im = sonic.getItemMeta();
                        assert sonic_im != null;
                        if (slotWasNull) {
                            sonic_im.setDisplayName("Sonic Screwdriver");
                            view.setItem(49, sonic);
                        } else {
                            // remove lore
                            sonic_im.setLore(null);
                        }
                        sonic_im.setCustomModelData(GUISonicGenerator.ELEVENTH_DOCTOR.getCustomModelData());
                        sonic.setItemMeta(sonic_im);
                        setCost(view, costs.get("Standard Sonic"));
                        break;
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                        ItemStack upgrade = view.getItem(slot);
                        assert upgrade != null;
                        ItemMeta upgrade_im = upgrade.getItemMeta();
                        assert upgrade_im != null;
                        String upgrade_name = upgrade_im.getDisplayName();
                        sonic = view.getItem(49);
                        if (sonic == null) {
                            sonic = new ItemStack(Material.BLAZE_ROD, 1);
                            slotWasNull = true;
                        }
                        sonic_im = sonic.getItemMeta();
                        List<String> lore;
                        assert sonic_im != null;
                        if (sonic_im.hasLore()) {
                            // get the current sonic's upgrades
                            lore = sonic_im.getLore();
                        } else {
                            // otherwise this is the first upgrade
                            lore = new ArrayList<>();
                            lore.add("Upgrades:");
                        }
                        // if they don't already have the upgrade
                        assert lore != null;
                        if (!lore.contains(upgrade_name)) {
                            lore.add(upgrade_name);
                            sonic_im.setLore(lore);
                            setCost(view, getCost(view) + costs.get(upgrade_name));
                        }
                        sonic.setItemMeta(sonic_im);
                        if (slotWasNull) {
                            view.setItem(49, sonic);
                        }
                        break;
                    case 43:
                        // save
                        sonic = view.getItem(49);
                        if (sonic != null) {
                            save(p, sonic, true);
                        }
                        break;
                    case 44:
                        // save & generate
                        sonic = view.getItem(49);
                        if (sonic != null) {
                            save(p, sonic, false);
                            generate(p, sonic, getCost(view));
                        }
                        break;
                    case 53:
                        // close
                        close(p);
                        break;
                    default:
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void save(Player p, ItemStack is, boolean close) {
        // process the sonic
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        ItemMeta im = is.getItemMeta();
        assert im != null;
        String dn = im.getDisplayName();
        // get ChatColor from display name
        String colour = "";
        if (!dn.startsWith("Sonic")) {
            colour = Objects.requireNonNull(ChatColor.getByChar(dn.substring(1, 2))).name();
        }
        set.put("sonic_type", colour);
        set.put("model", im.getCustomModelData());
        if (im.hasLore()) {
            List<String> lore = im.getLore();
            fields.forEach((key, value) -> {
                assert lore != null;
                set.put(value, (lore.contains(key)) ? 1 : 0);
            });
        } else {
            // has been reset to standard sonic
            fields.forEach((key, value) -> set.put(value, 0));
        }
        plugin.getQueryFactory().doUpdate("sonic", set, where);
        plugin.getTrackerKeeper().getSonicGenerators().remove(p.getUniqueId());
        if (close) {
            close(p);
        }
    }

    private void generate(Player p, ItemStack sonic, int cost) {
        // check they have enough Artron energy
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (rs.fromUUID(p.getUniqueId().toString())) {
            int level = rs.getArtronLevel();
            if (cost < level) {
                ItemStack is = sonic.clone();
                Location loc = location.clone().add(0.5d, 0.75d, 0.5d);
                Entity drop = Objects.requireNonNull(location.getWorld()).dropItem(loc, is);
                drop.setVelocity(new Vector(0, 0, 0));
                plugin.getTrackerKeeper().getSonicGenerators().remove(p.getUniqueId());
                // remove the Artron energy
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                plugin.getQueryFactory().alterEnergyLevel("tardis", -cost, where, p);
            } else {
                TARDISMessage.send(p, "UPGRADE_ABORT_ENERGY");
            }
        }
        close(p);
    }

    private int getCost(InventoryView view) {
        ItemStack is = view.getItem(45);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        assert im != null;
        String c = Objects.requireNonNull(im.getLore()).get(0);
        return TARDISNumberParsers.parseInt(c);
    }

    private void setCost(InventoryView view, int cost) {
        ItemStack is = view.getItem(45);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        assert im != null;
        List<String> lore = im.getLore();
        assert lore != null;
        lore.set(0, "" + cost);
        im.setLore(lore);
        is.setItemMeta(im);
    }
}
