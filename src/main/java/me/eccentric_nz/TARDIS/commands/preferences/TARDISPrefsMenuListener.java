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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminMenuInventory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetJunk;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
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
public class TARDISPrefsMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, String> lookup = new HashMap<String, String>();

    public TARDISPrefsMenuListener(TARDIS plugin) {
        this.plugin = plugin;
        lookup.put("Auto Power Up", "auto_powerup_on");
        lookup.put("Autonomous", "auto_on");
        lookup.put("Autonomous Siege", "auto_siege_on");
        lookup.put("Beacon", "beacon_on");
        lookup.put("Do Not Disturb", "dnd_on");
        lookup.put("Emergency Programme One", "eps_on");
        lookup.put("Hostile Action Displacement System", "hads_on");
        lookup.put("Minecart Sounds", "minecart_on");
        lookup.put("Who Quotes", "quotes_on");
        lookup.put("Exterior Rendering Room", "renderer_on");
        lookup.put("Interior SFX", "sfx_on");
        lookup.put("Submarine Mode", "submarine_on");
        lookup.put("Resource Pack Switching", "texture_on");
        lookup.put("Companion Build", "build_on");
        lookup.put("Wool For Lights Off", "wool_lights_on");
        lookup.put("Connected Textures", "ctm_on");
        lookup.put("Preset Sign", "sign_on");
        lookup.put("Travel Bar", "travelbar_on");
        lookup.put("Police Box Textures", "policebox_textures_on");
        lookup.put("Mob Farming", "farm_on");
        lookup.put("Telepathic Circuit", "telepathy_on");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(final InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Player Prefs Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    final Player p = (Player) event.getWhoClicked();
                    UUID uuid = p.getUniqueId();
                    ItemMeta im = is.getItemMeta();
                    if (slot == 24 && im.getDisplayName().equals("Handbrake")) {
                        // you can only set it to ON!
                        List<String> lore = im.getLore();
                        if (lore.get(0).equals(plugin.getLanguage().getString("SET_OFF"))) {
                            // get this player's TARDIS
                            ResultSetTardisID rs = new ResultSetTardisID(plugin);
                            if (rs.fromUUID(uuid.toString())) {
                                int id = rs.getTardis_id();
                                // must not be in the vortex or materialising
                                if (!plugin.getTrackerKeeper().getMaterialising().contains(id) && !plugin.getTrackerKeeper().getInVortex().contains(id)) {
                                    // set the handbrake to ON
                                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                                    wheret.put("tardis_id", id);
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("handbrake_on", 1);
                                    QueryFactory qf = new QueryFactory(plugin);
                                    qf.doUpdate("tardis", set, wheret);
                                    im.setLore(Arrays.asList(plugin.getLanguage().getString("SET_ON")));
                                    is.setItemMeta(im);
                                    TARDISMessage.send(p, "HANDBRAKE_ON");
                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        int amount = plugin.getTrackerKeeper().getHasDestination().get(id) * -1;
                                        HashMap<String, Object> wheref = new HashMap<String, Object>();
                                        wheref.put("tardis_id", id);
                                        qf.alterEnergyLevel("tardis", amount, wheref, p);
                                    }
                                    plugin.getTrackerKeeper().getHasDestination().remove(id);
                                    if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().remove(Integer.valueOf(id));
                                    }
                                    TARDISCircuitChecker tcc = null;
                                    if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(p, true)) {
                                        tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                    }
                                    // damage the circuit if configured
                                    if (tcc != null && plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                        // decrement uses
                                        int uses_left = tcc.getMaterialisationUses();
                                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.MATERIALISATION, uses_left, id, p).damage();
                                    }
                                } else {
                                    TARDISMessage.send(p, "HANDBRAKE_IN_VORTEX");
                                }
                            } else {
                                TARDISMessage.send(p, "NO_TARDIS");
                            }
                        } else {
                            TARDISMessage.send(p, "SONIC_HANDBRAKE_ON");
                        }
                        return;
                    }
                    if (slot == 25 && im.getDisplayName().equals("TARDIS Map")) {
                        // must be in the TARDIS
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", uuid.toString());
                        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
                        if (rs.resultSet()) {
                            // close this gui and load the TARDIS map
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Inventory new_inv = plugin.getServer().createInventory(p, 54, "§4TARDIS Map");
                                    // close inventory
                                    p.closeInventory();
                                    // open new inventory
                                    new_inv.setContents(new TARDISARSMap(plugin).getMap());
                                    p.openInventory(new_inv);
                                }
                            }, 1L);
                        } else {
                            TARDISMessage.send(p, "NOT_IN_TARDIS");
                        }
                        return;
                    }
                    if (slot == 26 && im.getDisplayName().equals("Admin Menu")) {
                        // close this gui and load the Admin Menu
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Inventory menu = plugin.getServer().createInventory(p, 54, "§4Admin Menu");
                                menu.setContents(new TARDISAdminMenuInventory(plugin).getMenu());
                                p.openInventory(menu);
                            }
                        }, 1L);
                        return;
                    }
                    List<String> lore = im.getLore();
                    boolean bool = (lore.get(0).equals(plugin.getLanguage().getString("SET_ON")));
                    String value = (bool) ? plugin.getLanguage().getString("SET_OFF") : plugin.getLanguage().getString("SET_ON");
                    int b = (bool) ? 0 : 1;
                    if (im.getDisplayName().equals("Junk TARDIS")) {
                        // must be outside of the TARDIS
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("uuid", uuid);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                        if (rst.resultSet()) {
                            TARDISMessage.send(p, "JUNK_PRESET_OUTSIDE");
                            return;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", uuid.toString());
                        ResultSetJunk rsj = new ResultSetJunk(plugin, where);
                        boolean has = rsj.resultSet();
                        // get preset
                        HashMap<String, Object> wherep = new HashMap<String, Object>();
                        wherep.put("uuid", uuid.toString());
                        ResultSetTardis rsp = new ResultSetTardis(plugin, wherep, "", false, 0);
                        if (rsp.resultSet()) {
                            QueryFactory qf = new QueryFactory(plugin);
                            Tardis tardis = rsp.getTardis();
                            String current = tardis.getPreset().toString();
                            int id = tardis.getTardis_id();
                            String chameleon = tardis.getChameleon();
                            String cham_set;
                            HashMap<String, Object> setj = new HashMap<String, Object>();
                            if (has) {
                                // update rcord with current preset
                                HashMap<String, Object> wherej = new HashMap<String, Object>();
                                wherej.put("uuid", uuid.toString());
                                setj.put("preset", current);
                                qf.doSyncUpdate("junk", setj, wherej);
                            } else {
                                // create a junk record
                                setj.put("uuid", uuid.toString());
                                setj.put("tardis_id", id);
                                setj.put("preset", current);
                                qf.doSyncInsert("junk", setj);
                            }
                            HashMap<String, Object> whereu = new HashMap<String, Object>();
                            whereu.put("uuid", uuid.toString());
                            HashMap<String, Object> sett = new HashMap<String, Object>();
                            String message = "JUNK_PRESET_ON";
                            if (bool) {
                                // restore saved preset
                                String preset = (has) ? rsj.getPreset().toString() : current;
                                sett.put("chameleon_preset", preset);
                                sett.put("chameleon_demat", "JUNK_MODE");
                                message = "JUNK_PRESET_OFF";
                                cham_set = preset;
                            } else {
                                // save JUNK_MODE preset
                                sett.put("chameleon_preset", "JUNK_MODE");
                                sett.put("chameleon_demat", current);
                                cham_set = "JUNK_MODE";
                            }
                            qf.doSyncUpdate("tardis", sett, whereu);
                            // set the Chameleon Circuit sign
                            TARDISStaticUtils.setSign(chameleon, 3, cham_set, p);
                            // rebuild
                            TARDISMessage.send(p, message);
                            p.performCommand("tardis rebuild");
                        }
                    } else if (im.getDisplayName().equals("Companion Build")) {
                        String[] args = new String[2];
                        args[0] = "";
                        args[1] = value;
                        new TARDISBuildCommand(plugin).toggleCompanionBuilding(((Player) event.getWhoClicked()), args);
                    } else {
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", uuid.toString());
                        if (im.getDisplayName().equals("HADS Type")) {
                            value = (lore.get(0).equals("DISPLACEMENT")) ? "DISPERSAL" : "DISPLACEMENT";
                            set.put("hads_type", value);
                        } else {
                            set.put(lookup.get(im.getDisplayName()), b);
                        }
                        new QueryFactory(plugin).doUpdate("player_prefs", set, where);
                    }
                    lore.set(0, value);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    if (im.getDisplayName().equals("Beacon")) {
                        // get tardis id
                        ResultSetTardisID rsi = new ResultSetTardisID(plugin);
                        if (rsi.fromUUID(uuid.toString())) {
                            new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardis_id(), !bool);
                        }
                    }
                }
            }
        }
    }
}
