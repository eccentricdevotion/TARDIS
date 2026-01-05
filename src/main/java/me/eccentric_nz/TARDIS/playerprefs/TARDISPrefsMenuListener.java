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
package me.eccentric_nz.TARDIS.playerprefs;

import me.eccentric_nz.TARDIS.ARS.ARSMapInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.autonomous.TARDISAutonomousInventory;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.config.ConfigDialog;
import me.eccentric_nz.TARDIS.commands.config.TARDISConfigMenuInventory;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateMapForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmingInventory;
import me.eccentric_nz.TARDIS.particles.TARDISParticleInventory;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicConfiguratorInventory;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISPrefsMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<String, String> lookup = new HashMap<>();

    public TARDISPrefsMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        lookup.put("Announce Repeaters", "announce_repeaters_on");
        lookup.put("Auto Power Up", "auto_powerup_on");
        lookup.put("Auto-rescue", "auto_rescue_on");
        lookup.put("Autonomous Siege", "auto_siege_on");
        lookup.put("Autonomous", "auto_on");
        lookup.put("Beacon", "beacon_on");
        lookup.put("Close GUI", "close_gui_on");
        lookup.put("Companion Build", "build_on");
        lookup.put("Do Not Disturb", "dnd_on");
        lookup.put("Emergency Programme One", "eps_on");
        lookup.put("Exterior Rendering Room", "renderer_on");
        lookup.put("Hostile Action Displacement System", "hads_on");
        lookup.put("Interior SFX", "sfx_on");
        lookup.put("Info GUI", "info_on");
        lookup.put("Minecart Sounds", "minecart_on");
        lookup.put("Mob Farming", "farm_on");
        lookup.put("Preset Sign", "sign_on");
        lookup.put("Open Display Door", "open_display_door_on");
        lookup.put("Submarine Mode", "submarine_on");
        lookup.put("Telepathic Circuit", "telepathy_on");
        lookup.put("Travel Bar", "travelbar_on");
        lookup.put("Who Quotes", "quotes_on");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISPrefsMenuInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        ItemMeta im = is.getItemMeta();
        if (slot == GUIPlayerPreferences.FLIGHT_MODE.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Flight Mode")) {
            List<Component> lore = im.lore();
            // cycle through flight modes
            FlightMode flight = FlightMode.valueOf(ComponentUtils.stripColour(lore.getFirst()));
            int mode = flight.getMode() + 1;
            int limit = (TARDISPermission.hasPermission(player, "tardis.fly")) ? 4 : 3;
            if (mode > limit) {
                mode = 1;
            }
            lore.set(0, Component.text(FlightMode.getByMode().get(mode).toString()));
            im.lore(lore);
            is.setItemMeta(im);
            // set flight mode
            HashMap<String, Object> setf = new HashMap<>();
            setf.put("flying_mode", mode);
            HashMap<String, Object> wheref = new HashMap<>();
            wheref.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", setf, wheref);
            return;
        }
        if (slot == GUIPlayerPreferences.INTERIOR_HUM_SOUND.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Interior Hum Sound")) {
            // close this gui and load the sounds GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                player.closeInventory();
                // open new inventory
                player.openInventory(new TARDISHumInventory(plugin).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.HANDBRAKE.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Handbrake")) {
            // you can only set it to ON!
            List<Component> lore = im.lore();
            if (ComponentUtils.stripColour(lore.getFirst()).equals(plugin.getLanguage().getString("SET_OFF", "OFF"))) {
                // get this player's TARDIS
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (rs.fromUUID(uuid.toString())) {
                    int id = rs.getTardisId();
                    // must not be in the vortex or materialising
                    if (!plugin.getTrackerKeeper().getMaterialising().contains(id) && !plugin.getTrackerKeeper().getInVortex().contains(id)) {
                        // set the handbrake to ON
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("handbrake_on", 1);
                        plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                        im.lore(List.of(Component.text(plugin.getLanguage().getString("SET_ON", "ON"))));
                        is.setItemMeta(im);
                        // Check if it's at a recharge point
                        TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                        tal.recharge(id);
                        // Remove energy from TARDIS and sets database
                        plugin.getMessenger().sendStatus(player, "HANDBRAKE_ON");
                        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            int amount = plugin.getTrackerKeeper().getHasDestination().get(id).cost() * -1;
                            HashMap<String, Object> wheref = new HashMap<>();
                            wheref.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheref, player);
                        }
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                            plugin.getTrackerKeeper().getHasRandomised().removeAll(Collections.singleton(id));
                        }
                        // damage the circuit if configured
                        DamageUtility.run(plugin, DiskCircuit.MATERIALISATION, id, player);
                    } else {
                        plugin.getMessenger().sendStatus(player, "HANDBRAKE_IN_VORTEX");
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_HANDBRAKE_ON");
            }
            return;
        }
        if (slot == GUIPlayerPreferences.TARDIS_MAP.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("TARDIS Map")) {
            // must be in the TARDIS
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                // close this gui and load the TARDIS map
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(uuid)) {
                        new FloodgateMapForm(plugin, uuid, rs.getTardis_id()).send();
                    } else {
                        // close inventory
                        player.closeInventory();
                        // open new inventory
                        player.openInventory(new ARSMapInventory(plugin).getInventory());
                    }
                }, 1L);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            }
            return;
        }
        if (slot == GUIPlayerPreferences.AUTONOMOUS_PREFERENCES.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Autonomous Preferences")) {
            // close this gui and load the TARDIS Autonomous Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                player.closeInventory();
                // open new inventory
                player.openInventory(new TARDISAutonomousInventory(plugin, uuid).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.FARMING_PREFERENCES.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Farming Preferences")) {
            // close this gui and load the TARDIS Farming Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                player.closeInventory();
                // open new inventory
                player.openInventory(new TARDISFarmingInventory(plugin, uuid).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.SONIC_CONFIGURATOR.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Sonic Configurator")) {
            // close this gui and load the Sonic Configurator
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                player.closeInventory();
                // open new inventory
                player.openInventory(new TARDISSonicConfiguratorInventory(plugin).getInventory());
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.PARTICLES.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Materialisation Particles")) {
            // close this gui and load the Particle Preferences
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // close inventory
                player.closeInventory();
                // open new inventory
                player.openInventory(new TARDISParticleInventory(plugin, uuid.toString()).getInventory());
            }, 1L);
            return;
        }
        if (slot == 18) {
            close(player);
        }
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        boolean dialogs = rsp.resultSet() && rsp.isDialogsOn();
        if (slot == GUIPlayerPreferences.GENERAL_PREFERENCES_MENU.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("General Preferences Menu")) {
            // close this gui and load the Admin Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (dialogs) {
                    // close inventory
                    player.closeInventory();
                    Audience.audience(player).showDialog(new PreferencesDialog(plugin, uuid).create());
                } else {
                    player.openInventory(new TARDISGeneralPrefsInventory(plugin, uuid).getInventory());
                }
            }, 1L);
            return;
        }
        if (slot == GUIPlayerPreferences.ADMIN_MENU.getSlot() && ComponentUtils.stripColour(im.displayName()).equals("Admin Config Menu")) {
            // close this gui and load the Admin Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (dialogs) {
                    // close inventory
                    player.closeInventory();
                    Audience.audience(player).showDialog(new ConfigDialog(plugin).create());
                } else {
                    player.openInventory(new TARDISConfigMenuInventory(plugin).getInventory());
                }
            }, 1L);
            return;
        }
    }
}
