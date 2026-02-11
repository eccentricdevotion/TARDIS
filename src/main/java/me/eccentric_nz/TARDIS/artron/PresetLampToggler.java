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
import me.eccentric_nz.TARDIS.customblocks.VariableLight;
import me.eccentric_nz.TARDIS.database.data.Lamp;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class PresetLampToggler {

    private final TARDIS plugin;

    public PresetLampToggler(TARDIS plugin) {
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
                // get variable material
                ResultSetLightPrefs lightPrefs = new ResultSetLightPrefs(plugin);
                Material variable = Material.LIGHT_GRAY_WOOL;
                if (lightPrefs.fromID(id)) {
                    variable = lightPrefs.getMaterial();
                }
                if (on) {
                    if (b.getType().equals(Material.SEA_LANTERN) || (b.getType().equals(Material.REDSTONE_LAMP))) {
                        // convert to light display item
                        if (l.materialOff() == null) {
                            if (light.getOn().isVariable()) {
                                new VariableLight(variable, b.getLocation().add(0.5, 0.5, 0.5)).set(light.getOff().getCustomModel(), 0);
                            } else {
                                TARDISDisplayItemUtils.set(light.getOff(), b, id);
                            }
                        } else { // lamp has an off material override
                            TARDISDisplayItem customLamp = new TARDISCustomLightDisplayItem(l.materialOff(), false);
                            TARDISDisplayItemUtils.set(customLamp, b, id);
                        }
                    } else {
                        // switch the item stack
                        if (display != null) {
                            Material material = light.getOff().getMaterial();
                            // use material defined on lamp if available
                            if (l.materialOff() != null) {
                                material = l.materialOff();
                            }
                            if (light.isVariable()) {
                                new VariableLight(material, b.getLocation().add(0.5, 0.5, 0.5)).change(light.getOff().getCustomModel(), 0);
                            } else {
                                ItemStack is = ItemStack.of(material);
                                ItemMeta im = is.getItemMeta();
                                im.displayName(ComponentUtils.toWhite(light.getOff().getDisplayName()));
                                is.setItemMeta(im);
                                display.setItemStack(is);
                            }
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
                    // switch the item stack
                    if (display != null) {
                        Material material = light.getOn().getMaterial();
                        // use material defined on lamp if available
                        if (l.materialOn() != null) {
                            material = l.materialOn();
                        }
                        if (light.isVariable()) {
                            new VariableLight(variable, b.getLocation().add(0.5, 0.5, 0.5)).change(light.getOn().getCustomModel(), level);
                        } else {
                            ItemStack is = ItemStack.of(material);
                            ItemMeta im = is.getItemMeta();
                            im.displayName(ComponentUtils.toWhite(light.getOn().getDisplayName()));
                            is.setItemMeta(im);
                            display.setItemStack(is);
                        }
                    }
                }
            }
        }
    }
}
