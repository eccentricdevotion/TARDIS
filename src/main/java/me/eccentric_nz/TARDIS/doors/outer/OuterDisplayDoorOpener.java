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
package me.eccentric_nz.TARDIS.doors.outer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.utils.PandoricaOpens;
import me.eccentric_nz.TARDIS.chameleon.utils.SidratOpens;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInnerDoorLocations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class OuterDisplayDoorOpener {

    private final TARDIS plugin;

    public OuterDisplayDoorOpener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(ArmorStand stand, int id) {
        if (stand == null) {
            return;
        }
        EntityEquipment ee = stand.getEquipment();
        ItemStack dye = ee.getHelmet();
        if (dye == null) {
            return;
        }
        if ((TARDISConstants.DYES.contains(dye.getType()) || plugin.getUtils().isCustomModel(dye)) && dye.hasItemMeta()) {
            ItemMeta dim = dye.getItemMeta();
            if (!dim.hasItemModel()) {
                return;
            }
            // exterior portal
            Location portal = new Location(stand.getWorld(), stand.getLocation().getBlockX(), stand.getLocation().getBlockY(), stand.getLocation().getBlockZ());
            // open door
            if (dye.getType() == Material.ENDER_PEARL) {
                // animate pandorica opening
                new PandoricaOpens(plugin).animate(stand, true);
            } else if (dye.getType() == Material.GREEN_STAINED_GLASS_PANE) {
                // animate SIDRAT opening
                new SidratOpens(plugin).animate(stand, true);
            } else {
                switch (dye.getType()) {
                    case CYAN_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.TENNANT_OPEN.getKey());
                    case GRAY_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.WEEPING_ANGEL_OPEN.getKey());
                    case RED_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.BATTLE_OPEN.getKey());
                    case WHITE_DYE -> dim.setItemModel(ChameleonVariant.WHITE_OPEN.getKey());
                    case ORANGE_DYE -> dim.setItemModel(ChameleonVariant.ORANGE_OPEN.getKey());
                    case MAGENTA_DYE -> dim.setItemModel(ChameleonVariant.MAGENTA_OPEN.getKey());
                    case LIGHT_BLUE_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_BLUE_OPEN.getKey());
                    case YELLOW_DYE -> dim.setItemModel(ChameleonVariant.YELLOW_OPEN.getKey());
                    case LIME_DYE -> dim.setItemModel(ChameleonVariant.LIME_OPEN.getKey());
                    case PINK_DYE -> dim.setItemModel(ChameleonVariant.PINK_OPEN.getKey());
                    case GRAY_DYE -> dim.setItemModel(ChameleonVariant.GRAY_OPEN.getKey());
                    case LIGHT_GRAY_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_GRAY_OPEN.getKey());
                    case CYAN_DYE -> dim.setItemModel(ChameleonVariant.CYAN_OPEN.getKey());
                    case PURPLE_DYE -> dim.setItemModel(ChameleonVariant.PURPLE_OPEN.getKey());
                    case BLUE_DYE -> dim.setItemModel(ChameleonVariant.BLUE_OPEN.getKey());
                    case BROWN_DYE -> dim.setItemModel(ChameleonVariant.BROWN_OPEN.getKey());
                    case GREEN_DYE -> dim.setItemModel(ChameleonVariant.GREEN_OPEN.getKey());
                    case RED_DYE -> dim.setItemModel(ChameleonVariant.RED_OPEN.getKey());
                    case BLACK_DYE -> dim.setItemModel(ChameleonVariant.BLACK_OPEN.getKey());
                    case LEATHER_HORSE_ARMOR -> dim.setItemModel(ColouredVariant.TINTED_OPEN.getKey());
                    default -> {
                        // get the custom model config
                        NamespacedKey c = plugin.getUtils().getCustomModel(dye.getType(), "_open");
                        if (c != null) {
                            dim.setItemModel(c);
                        }
                    }
                }
                dye.setItemMeta(dim);
                ee.setHelmet(dye, true);
                TARDISSounds.playDoorSound(true, portal);
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                // get interior teleport location
                ResultSetInnerDoorLocations resultSetPortal = new ResultSetInnerDoorLocations(plugin, id);
                if (resultSetPortal.resultSet()) {
                    Location teleport = resultSetPortal.getTeleportLocation();
                    TARDISTeleportLocation tp_in = new TARDISTeleportLocation();
                    tp_in.setLocation(teleport);
                    tp_in.setTardisId(id);
                    tp_in.setDirection(resultSetPortal.getDirection());
                    tp_in.setAbandoned(tardis.isAbandoned());
                    // track portal
                    plugin.getTrackerKeeper().getPortals().put(portal, tp_in);
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
