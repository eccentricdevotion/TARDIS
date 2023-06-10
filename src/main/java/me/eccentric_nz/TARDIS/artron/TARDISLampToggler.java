/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisTimeLord;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author eccentric_nz
 */
public class TARDISLampToggler {

    private final TARDIS plugin;

    public TARDISLampToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void flickSwitch(int id, UUID uuid, boolean on, TardisLight light) {
        // get lamp locations
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, true);
        if (rsl.resultSet()) {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                // only use player preference if the tardis id of the timelord/companion is the same as the tardis id they are in
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (rs.fromUUID(uuid.toString()) && rs.getTardis_id() == id) {
                    light = rsp.getLights();
                } else {
                    // also force the use of lanterns if that is the tardis owner's preference
                    ResultSetTardisTimeLord rstl = new ResultSetTardisTimeLord(plugin);
                    if (rstl.fromID(id) && rstl.getUuid() != uuid) {
                        // get tardis owner's preference
                        ResultSetPlayerPrefs rsptl = new ResultSetPlayerPrefs(plugin, rstl.getUuid().toString());
                        if (rsptl.resultSet()) {
                            light = rsptl.getLights();
                        }
                    }
                }
            }
            for (Block b : rsl.getData()) {
                while (!b.getChunk().isLoaded()) {
                    b.getChunk().load();
                }
                Levelled levelled = TARDISConstants.LIGHT;
                ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(b);
                if (on) {
                    levelled.setLevel(0);
                    b.setBlockData(levelled);
                    if (b.getType().equals(Material.SEA_LANTERN) || (b.getType().equals(Material.REDSTONE_LAMP))) {
                        // convert to light display item
                        TARDISDisplayItemUtils.set(light.getOff(), b);
                    } else {
                        // switch the itemstack
                        if (display != null) {
                            ItemStack is = new ItemStack(light.getOff().getMaterial());
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(light.getOff().getDisplayName());
                            int cmd = light.getOff().getCustomModelData();
                            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
                            if (cmd != -1) {
                                im.setCustomModelData(cmd);
                            }
                            is.setItemMeta(im);
                            display.setItemStack(is);
                        }
                    }
                } else {
                    levelled.setLevel(15);
                    b.setBlockData(levelled);
                    // switch the itemstack
                    if (display != null) {
                        ItemStack is = new ItemStack(light.getOn().getMaterial());
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(light.getOn().getDisplayName());
                        int cmd = light.getOn().getCustomModelData();
                        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
                        if (cmd != -1) {
                            im.setCustomModelData(cmd);
                        }
                        is.setItemMeta(im);
                        display.setItemStack(is);
                    }
                }
            }
        }
    }
}
