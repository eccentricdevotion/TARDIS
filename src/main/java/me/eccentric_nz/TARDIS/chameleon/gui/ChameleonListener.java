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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.CircuitChecker;
import me.eccentric_nz.TARDIS.advanced.CircuitDamager;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.chameleon.construct.ChameleonConstructorInventory;
import me.eccentric_nz.TARDIS.chameleon.utils.ChameleonFrame;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class ChameleonListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public ChameleonListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onChameleonMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ChameleonInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 26) {
            return;
        }
        InventoryView view = event.getView();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        Adaption adapt = tardis.getAdaption();
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ChameleonFrame tcf = new ChameleonFrame();
        String chameleon = "";
        // set the Chameleon Circuit sign(s)
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        whereh.put("type", Control.CHAMELEON.getId());
        ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
        boolean hasChameleonSign = false;
        if (rsc.resultSet()) {
            hasChameleonSign = true;
            for (HashMap<String, String> map : rsc.getData()) {
                chameleon = map.get("location");
            }
        }
        HashMap<String, Object> wheref = new HashMap<>();
        wheref.put("tardis_id", id);
        wheref.put("type", Control.FRAME.getId());
        ResultSetControls rsf = new ResultSetControls(plugin, wheref, false);
        boolean hasFrame = rsf.resultSet();
        switch (slot) {
            case 0 -> {
                // apply
                player.performCommand("tardis rebuild");
                close(player);
                // damage the circuit if configured
                DamageUtility.run(plugin, DiskCircuit.CHAMELEON, id, player);
            }
            case 11 -> {
                // factory
                set.put("adapti_on", 0);
                ItemStack frb = view.getItem(20);
                ItemMeta fact = frb.getItemMeta();
                Component ory = fact.displayName();
                if (ory != null && ory.equals(Component.text(plugin.getLanguage().getString("SET_ON", "ON"), NamedTextColor.GREEN))) {
                    set.put("chameleon_preset", "FACTORY");
                    toggleOthers(ChameleonOption.FACTORY, view);
                    if (hasChameleonSign) {
                        updateChameleonSign(rsc.getData(), "FACTORY", player);
                    }
                    if (hasFrame) {
                        tcf.updateChameleonFrame(ChameleonPreset.FACTORY, rsf.getLocation());
                    }
                    plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Factory Fresh", plugin);
                } else {
                    set.put("chameleon_preset", "POLICE_BOX_BLUE");
                    toggleOthers(ChameleonOption.PRESET, view);
                    if (hasFrame) {
                        tcf.updateChameleonFrame(ChameleonPreset.POLICE_BOX_BLUE, rsf.getLocation());
                    }
                    setDefault(view, player, chameleon);
                }
            }
            case 12 -> {
                // cycle biome adaption
                int ca = adapt.ordinal() + 1;
                if (ca >= Adaption.values().length) {
                    ca = 0;
                }
                Adaption a = Adaption.values()[ca];
                if (a.equals(Adaption.OFF)) {
                    // default to Blue Police Box
                    set.put("chameleon_preset", "POLICE_BOX_BLUE");
                    toggleOthers(ChameleonOption.PRESET, view);
                    if (hasFrame) {
                        tcf.updateChameleonFrame(ChameleonPreset.POLICE_BOX_BLUE, rsf.getLocation());
                    }
                    setDefault(view, player, chameleon);
                } else {
                    toggleOthers(ChameleonOption.ADAPTIVE, view);
                    ChameleonPreset adaptive = (tardis.getPreset().equals(ChameleonPreset.SUBMERGED)) ? ChameleonPreset.SUBMERGED : ChameleonPreset.FACTORY;
                    if (hasFrame) {
                        tcf.updateChameleonFrame(adaptive, rsf.getLocation());
                    }
                    set.put("chameleon_preset", adaptive.toString());
                }
                set.put("adapti_on", ca);
                ItemStack arb = view.getItem(21);
                ItemMeta bio = arb.getItemMeta();
                bio.displayName(Component.text(a.toString(), a.getColour()));
                arb.setItemMeta(bio);
            }
            case 13 -> {
                // Invisibility
                set.put("adapti_on", 0);
                ItemStack irb = view.getItem(22);
                ItemMeta invis = irb.getItemMeta();
                Component ible = invis.displayName();
                if (ible != null && ible.equals(Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"), NamedTextColor.RED))) {
                    // check they have an Invisibility Circuit
                    CircuitChecker tcc = new CircuitChecker(plugin, id);
                    tcc.getCircuits();
                    if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, false) && !tcc.hasInvisibility()) {
                        close(player);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "INVISIBILITY_MISSING");
                        break;
                    }
                    if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                        // decrement uses
                        int uses_left = tcc.getInvisibilityUses();
                        new CircuitDamager(plugin, DiskCircuit.INVISIBILITY, uses_left, id, player).damage();
                    }
                    toggleOthers(ChameleonOption.INVISIBLE, view);
                    set.put("chameleon_preset", "INVISIBLE");
                    if (hasChameleonSign) {
                        updateChameleonSign(rsc.getData(), "INVISIBLE", player);
                    }
                    if (hasFrame) {
                        tcf.updateChameleonFrame(ChameleonPreset.INVISIBLE, rsf.getLocation());
                    }
                    plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Invisibility", plugin);
                } else {
                    toggleOthers(ChameleonOption.PRESET, view);
                    // default to Blue Police Box
                    set.put("chameleon_preset", "POLICE_BOX_BLUE");
                    if (hasFrame) {
                        tcf.updateChameleonFrame(ChameleonPreset.POLICE_BOX_BLUE, rsf.getLocation());
                    }
                    setDefault(view, player, chameleon);
                }
            }
            // presets
            case 14 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(new BlockPresetInventory(plugin, player).getInventory()), 2L);
            // constructor GUI
            case 15 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(new ChameleonConstructorInventory(plugin).getInventory()), 2L);
            case 3 -> {
                // set the current adaptive preset as shorted out - this
                // will allow locking in a usually unavailable biome preset
                // ONLY if the Chameleon Circuit is set to Adaptive BIOME
                if (isBiomeAdaptive(view)) {
                    toggleOthers(ChameleonOption.PRESET, view);
                    // get current location's biome
                    ResultSetCurrentFromId rsl = new ResultSetCurrentFromId(plugin, id);
                    if (rsl.resultSet()) {
                        Biome biome = rsl.getCurrent().location().getBlock().getBiome();
                        // get which preset
                        ChameleonPreset which = getAdaption(biome);
                        if (which != null) {
                            set.put("adapti_on", 0);
                            set.put("chameleon_preset", which.toString());
                            if (hasFrame) {
                                tcf.updateChameleonFrame(which, rsf.getLocation());
                            }
                            // set preset lore
                            ItemStack p = view.getItem(23);
                            ItemMeta pim = p.getItemMeta();
                            pim.displayName(Component.text(which.toString(), NamedTextColor.GREEN));
                            p.setItemMeta(pim);
                            // remove button
                            view.setItem(3, null);
                            TARDISStaticUtils.setSign(chameleon, 3, which.toString(), player);
                            plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", which.getDisplayName(), plugin);
                        }
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_LOCK");
                }
            }
            case 20, 21, 22, 23, 24 -> { }
            default -> close(player);
        }
        if (!set.isEmpty()) {
            plugin.getQueryFactory().doUpdate("tardis", set, wherec);
        }
    }

    private void setDefault(InventoryView view, Player player, String chameleon) {
        // default to Blue Police Box
        // set preset lore
        ItemStack p = view.getItem(23);
        ItemMeta pim = p.getItemMeta();
        pim.displayName(Component.text("POLICE_BOX_BLUE", NamedTextColor.GREEN));
        p.setItemMeta(pim);
        TARDISStaticUtils.setSign(chameleon, 3, "POLICE_BOX_BLUE", player);
        plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Blue Police Box", plugin);
    }

    private void toggleOthers(ChameleonOption c, InventoryView view) {
        for (ChameleonOption co : ChameleonOption.values()) {
            ItemStack is = view.getItem(co.getSlot());
            ItemMeta im = is.getItemMeta();
            Component onoff;
            Material m;
            if (!co.equals(c)) {
                onoff = Component.text(plugin.getLanguage().getString(co.getOff()), co.getOffColour());
                m = Material.LIGHT_GRAY_CARPET;
            } else {
                onoff = Component.text(plugin.getLanguage().getString(co.getOn()), co.getOnColour());
                m = Material.LIME_WOOL;
            }
            im.displayName(onoff);
            ItemStack sub = ItemStack.of(m);
            sub.setItemMeta(im);
            view.setItem(co.getSlot(), sub);
        }
    }

    private void updateChameleonSign(ArrayList<HashMap<String, String>> map, String preset, Player player) {
        for (HashMap<String, String> entry : map) {
            TARDISStaticUtils.setSign(entry.get("location"), 3, preset, player);
        }
    }

    private boolean isBiomeAdaptive(InventoryView view) {
        ItemStack adaption = view.getItem(21);
        ItemMeta im = adaption.getItemMeta();
        PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();
        String dn = plainSerializer.serialize(im.displayName());
        return dn.equals("BIOME");
    }

    private ChameleonPreset getAdaption(Biome biome) {
        try {
            return ChameleonPreset.valueOf(plugin.getAdaptiveConfig().getString(biome.getKey().getKey().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
