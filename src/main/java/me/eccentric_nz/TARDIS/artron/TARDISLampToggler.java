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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.customblocks.TARDISCustomLightDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Lamp;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

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
                if (rs.fromUUID(uuid.toString()) && rs.getTardisId() == id) {
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
            for (Lamp l : rsl.getData()) {
                Block b = l.block();
                while (!b.getChunk().isLoaded()) {
                    b.getChunk().load();
                }
                Levelled levelled = TARDISConstants.LIGHT;
                ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(b);
                if (on) {
                    if (b.getType().equals(Material.SEA_LANTERN) || (b.getType().equals(Material.REDSTONE_LAMP))) {
                        // convert to light display item
                        if (l.materialOff() == null) {
                            TARDISDisplayItemUtils.set(light.getOff(), b, id);
                        } else { // Lamp has an off material override
                            TARDISDisplayItem customLamp = new TARDISCustomLightDisplayItem(l.materialOff(), false);
                            TARDISDisplayItemUtils.set(customLamp, b, id);
                        }
                    } else {
                        // switch the itemstack
                        if (display != null) {
                            Material material = light.getOff().getMaterial();
                            // Use material defined on lamp if available
                            if (l.materialOff() != null) {
                                material = l.materialOff();
                            }
                            ItemStack is = ItemStack.of(material);
                            ItemMeta im = is.getItemMeta();
                            im.displayName(ComponentUtils.toWhite(light.getOff().getDisplayName()));
                            // If the lamp has a material don't use the custom light model.
                            if (l.materialOff() == null) {
                                NamespacedKey model = light.getOff().getCustomModel();
                                if (model != null) {
                                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
                                    if (light.getOff().getDisplayName().contains("Variable")) {
                                        im.setItemModel(model);
                                    }
                                }
                            }
                            is.setItemMeta(im);
                            display.setItemStack(is);
                        }
                    }
                    levelled.setLevel(0);
                    b.setBlockData(levelled);
                } else {
                    // set light level from light level switch preference
                    ResultSetInteriorLightLevel rsill = new ResultSetInteriorLightLevel(plugin, id);
                    int level = (rsill.resultSet()) ? LightLevel.interior_level[rsill.getLevel()] : 15;
                    levelled.setLevel(Math.round(level * l.percentage()));
                    b.setBlockData(levelled);
                    // switch the itemstack
                    if (display != null) {
                        Material material = light.getOn().getMaterial();
                        // Use material defined on lamp if available
                        if (l.materialOn() != null) {
                            material = l.materialOn();
                        }
                        ItemStack is = ItemStack.of(material);
                        ItemMeta im = is.getItemMeta();
                        im.displayName(ComponentUtils.toWhite(light.getOn().getDisplayName()));
                        // If the lamp has a material don't use the custom light model.
                        if (l.materialOn() == null) {
                            NamespacedKey model = light.getOn().getCustomModel();
                            if (model != null) {
                                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
                                if (light.getOn().getDisplayName().contains("Variable")) {
                                    im.setItemModel(model);
                                }
                            }
                        }
                        is.setItemMeta(im);
                        display.setItemStack(is);
                    }
                }
            }
        }
    }
}
