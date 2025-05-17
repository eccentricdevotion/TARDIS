/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.BoneDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ClassicDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOuterPortal;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.doors.DoorAnimator;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class InnerDisplayDoorOpener {

    private final TARDIS plugin;

    public InnerDisplayDoorOpener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(Block block, int id, boolean outside) {
        // get and open display door
        ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
        if (display != null) {
            TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
            if (tdi != null) {
                // animate door opening
                if (outside) {
                    ItemStack itemStack = display.getItemStack();
                    ItemMeta im = itemStack.getItemMeta();
                    switch (itemStack.getType()) {
                        case IRON_DOOR -> im.setItemModel(TardisDoorVariant.TARDIS_DOOR_OPEN.getKey());
                        case BIRCH_DOOR -> im.setItemModel(BoneDoorVariant.BONE_DOOR_OPEN.getKey());
                        case CHERRY_DOOR -> im.setItemModel(ClassicDoorVariant.CLASSIC_DOOR_OPEN.getKey());
                        default -> im.setItemModel(Door.getOpenModel(itemStack.getType()));
                    }
                    itemStack.setItemMeta(im);
                    display.setItemStack(itemStack);
                } else {
                    new DoorAnimator(plugin, display).animate(false);
                }
                if (!plugin.getTrackerKeeper().getWoolToggles().contains(id)) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        ChameleonPreset preset = tardis.getPreset();
                        // get portal location
                        Location portal = block.getLocation();
                        // get tp location
                        ResultSetOuterPortal resultSetPortal = new ResultSetOuterPortal(plugin, id);
                        if (resultSetPortal.resultSet()) {
                            Location teleport = resultSetPortal.getLocation().clone();
                            // adjust for teleport
                            if (preset.usesArmourStand() || preset == ChameleonPreset.INVISIBLE) {
                                switch (resultSetPortal.getDirection()) {
                                    case NORTH_EAST -> teleport.add(0, 0, 1);
                                    case NORTH -> teleport.add(0.5d, 0.0d, 1.0d);
                                    case NORTH_WEST -> teleport.add(1, 0, 1);
                                    case WEST -> teleport.add(1.0d, 0.0d, 0.5d);
                                    case SOUTH_WEST -> teleport.add(1, 0, -0.5);
                                    case SOUTH -> teleport.add(0.5d, 0.0d, -1.0d);
                                    case SOUTH_EAST -> teleport.add(-0.5, 0, 0);
                                    default -> teleport.add(-1.0d, 0.0d, 0.5d);
                                }
                            } else {
                                teleport.setX(teleport.getX() + 0.5);
                                teleport.setZ(teleport.getZ() + 0.5);
                            }
                            TARDISTeleportLocation tp_out = new TARDISTeleportLocation();
                            tp_out.setLocation(teleport);
                            tp_out.setTardisId(id);
                            tp_out.setDirection(resultSetPortal.getDirection());
                            tp_out.setAbandoned(tardis.isAbandoned());
                            // create portal
                            plugin.getTrackerKeeper().getPortals().put(portal, tp_out);
                            // add movers
                            if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                                // always add the time lord of this TARDIS - as a companion may be opening the door
                                plugin.getTrackerKeeper().getMovers().add(tardis.getUuid());
                                // others
                                if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                                    // online players
                                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                        plugin.getTrackerKeeper().getMovers().add(p.getUniqueId());
                                    }
                                } else {
                                    //  companion UUIDs
                                    String[] companions = tardis.getCompanions().split(":");
                                    for (String c : companions) {
                                        if (!c.isEmpty()) {
                                            plugin.getTrackerKeeper().getMovers().add(UUID.fromString(c));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
