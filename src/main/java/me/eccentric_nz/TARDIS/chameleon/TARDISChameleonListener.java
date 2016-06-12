/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.Arrays;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChameleonListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onChameleonTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Chameleon Circuit")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            String last_line = TARDISStaticUtils.getLastLine(tardis.getChameleon());
                            final boolean bool = tardis.isChamele_on();
                            final boolean adapt = tardis.isAdapti_on();
                            String preset = tardis.getPreset().toString();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            HashMap<String, Object> set_oo = new HashMap<String, Object>();
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("tardis_id", id);
                            switch (slot) {
                                case 0:
                                    // toggle chameleon circuit
                                    String onoff;
                                    String engage;
                                    int oo;
                                    if (bool) {
                                        onoff = ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
                                        engage = plugin.getLanguage().getString("SET_ON");
                                        oo = 0;
                                    } else {
                                        onoff = ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
                                        engage = plugin.getLanguage().getString("SET_OFF");
                                        oo = 1;
                                    }
                                    ItemMeta im = is.getItemMeta();
                                    im.setLore(Arrays.asList(onoff, String.format(plugin.getLanguage().getString("CHAM_CLICK"), engage)));
                                    is.setItemMeta(im);
                                    // set sign text
                                    TARDISStaticUtils.setSign(tardis.getChameleon(), 2, onoff, player);
                                    set_oo.put("chamele_on", oo);
                                    break;
                                case 2:
                                    player.performCommand("tardis rebuild");
                                    close(player);
                                    // damage the circuit if configured
                                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        // decrement uses
                                        int uses_left = tcc.getChameleonUses();
                                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.CHAMELEON, uses_left, id, player).damage();
                                    }
                                    break;
                                case 4:
                                    // toggle biome adaption
                                    String biome;
                                    String to_turn;
                                    int ba;
                                    if (adapt) {
                                        biome = ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
                                        to_turn = plugin.getLanguage().getString("SET_ON");
                                        ba = 0;
                                    } else {
                                        biome = ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
                                        to_turn = plugin.getLanguage().getString("SET_OFF");
                                        ba = 1;
                                    }
                                    ItemMeta bio = is.getItemMeta();
                                    bio.setLore(Arrays.asList(biome, String.format(plugin.getLanguage().getString("CHAM_CLICK"), to_turn)));
                                    is.setItemMeta(bio);
                                    set_oo.put("adapti_on", ba);
                                    break;
                                case 8:
                                    // page two
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            TARDISPresetInventory tpi = new TARDISPresetInventory(plugin, bool, adapt);
                                            ItemStack[] items = tpi.getTerminal();
                                            Inventory presetinv = plugin.getServer().createInventory(player, 54, "§4More Presets");
                                            presetinv.setContents(items);
                                            player.openInventory(presetinv);
                                        }
                                    }, 2L);
                                    break;
                                case 18:
                                    // new Police Box
                                    if (!last_line.equals("NEW")) {
                                        set.put("chameleon_preset", "NEW");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "NEW", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "New Police Box");
                                    break;
                                case 20:
                                    // factory
                                    if (!last_line.equals("FACTORY")) {
                                        set.put("chameleon_preset", "FACTORY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "FACTORY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Factory Fresh");
                                    break;
                                case 22:
                                    // jungle temple
                                    if (!last_line.equals("JUNGLE")) {
                                        set.put("chameleon_preset", "JUNGLE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "JUNGLE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Jungle Temple");
                                    break;
                                case 24:
                                    // nether fortress
                                    if (!last_line.equals("NETHER")) {
                                        set.put("chameleon_preset", "NETHER");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "NETHER", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Nether Fortress");
                                    break;
                                case 26:
                                    // old police box
                                    if (!last_line.equals("OLD")) {
                                        set.put("chameleon_preset", "OLD");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "OLD", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Old Police Box");
                                    break;
                                case 28:
                                    // swamp
                                    if (!last_line.equals("SWAMP")) {
                                        set.put("chameleon_preset", "SWAMP");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "SWAMP", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Swamp Hut");
                                    break;
                                case 30:
                                    // party tent
                                    if (!last_line.equals("PARTY")) {
                                        set.put("chameleon_preset", "PARTY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PARTY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Party Tent");
                                    break;
                                case 32:
                                    // village house
                                    if (!last_line.equals("VILLAGE")) {
                                        set.put("chameleon_preset", "VILLAGE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "VILLAGE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Village House");
                                    break;
                                case 34:
                                    // yellow submarine
                                    if (!last_line.equals("YELLOW")) {
                                        set.put("chameleon_preset", "YELLOW");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "YELLOW", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Yellow Submarine");
                                    break;
                                case 36:
                                    // telephone
                                    if (!last_line.equals("TELEPHONE")) {
                                        set.put("chameleon_preset", "TELEPHONE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "TELEPHONE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Red Telephone Box");
                                    break;
                                case 38:
                                    // weeping angel
                                    if (!last_line.equals("ANGEL")) {
                                        set.put("chameleon_preset", "ANGEL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "ANGEL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Weeping Angel");
                                    break;
                                case 40:
                                    // submerged
                                    if (!last_line.equals("SUBMERGED")) {
                                        set.put("chameleon_preset", "SUBMERGED");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "SUBMERGED", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Submerged");
                                    break;
                                case 42:
                                    // flower
                                    if (!last_line.equals("FLOWER")) {
                                        set.put("chameleon_preset", "FLOWER");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "FLOWER", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Daisy Flower");
                                    break;
                                case 44:
                                    // stone brick column
                                    if (!last_line.equals("STONE")) {
                                        set.put("chameleon_preset", "STONE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "STONE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Stone Brick Column");
                                    break;
                                case 46:
                                    // chalice
                                    if (!last_line.equals("CHALICE")) {
                                        set.put("chameleon_preset", "CHALICE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CHALICE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Quartz Chalice");
                                    break;
                                case 48:
                                    // desert temple
                                    if (!last_line.equals("DESERT")) {
                                        set.put("chameleon_preset", "DESERT");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "DESERT", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Desert Temple");
                                    break;
                                case 50:
                                    // mossy well
                                    if (!last_line.equals("WELL")) {
                                        set.put("chameleon_preset", "WELL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "WELL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mossy Well");
                                    break;
                                case 52:
                                    // windmill
                                    if (!last_line.equals("WINDMILL")) {
                                        set.put("chameleon_preset", "WINDMILL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "WINDMILL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Windmill");
                                    break;
                                default:
                                    close(player);
                            }
                            if (set.size() > 0) {
                                set.put("chameleon_demat", preset);
                                qf.doUpdate("tardis", set, wherec);
                            }
                            if (set_oo.size() > 0) {
                                qf.doUpdate("tardis", set_oo, wherec);
                            }
                        }
                    }
                }
            }
        }
    }
}
