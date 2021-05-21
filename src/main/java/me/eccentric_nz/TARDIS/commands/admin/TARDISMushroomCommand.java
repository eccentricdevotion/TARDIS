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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class TARDISMushroomCommand {

    private final TARDIS plugin;
    private final List<String> brownBlockNames = Arrays.asList("", "The Moment", "Siege Cube");
    private final List<String> brownSubs = Arrays.asList("", "the_moment", "siege_cube");
    private final List<String> redBlockNames = Arrays.asList("", "Ars", "Bigger", "Budget", "Coral", "Deluxe", "Eleventh", "Ender", "Plank", "Pyramid", "Redstone", "Steampunk", "Thirteenth", "Factory", "Tom", "Twelfth", "War", "Small", "Medium", "Tall", "Legacy Bigger", "Legacy Budget", "Legacy Deluxe", "Legacy Eleventh", "Legacy Redstone", "Pandorica", "Master", "Atomic elements", "Chemical compounds", "Material reducer", "Element constructor", "Lab table", "Product crafting");
    private final List<String> redSubs = Arrays.asList("", "ars", "bigger", "budget", "coral", "deluxe", "eleventh", "ender", "plank", "pyramid", "redstone", "steampunk", "thirteenth", "factory", "tom", "twelfth", "war", "small", "medium", "tall", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "pandorica", "master", "creative", "compound", "reducer", "constructor", "lab", "product");
    private final List<String> stemBlockNames = Arrays.asList("", "Blue Lamp", "Red Lamp", "Purple Lamp", "Green Lamp", "Blue Lamp On", "Red Lamp On", "Purple Lamp On", "Green Lamp On", "Heat Block", "Custom", "Hexagon", "Roundel", "Roundel Offset", "Cog", "Advanced Console", "Disk Storage", "Lamp Off", "Lantern Off", "Blue Police Box", "Seed Block Grow");
    private final List<String> stemSubs = Arrays.asList("", "blue_lamp", "red_lamp", "purple_lamp", "green_lamp", "blue_lamp_on", "red_lamp_on", "purple_lamp_on", "green_lamp_on", "heat_block", "rotor", "custom", "hexagon", "roundel", "roundel_offset", "cog", "advanced_console", "disk_storage", "lamp_off", "lantern_off", "blue_box", "grow");

    public TARDISMushroomCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack getStack(String[] args) {
        int which = 0;
        Material mushroom = Material.RED_MUSHROOM_BLOCK;
        String displayName = "";
        if (redSubs.contains(args[3])) {
            which = redSubs.indexOf(args[3]) + 13;
            mushroom = Material.RED_MUSHROOM_BLOCK;
            displayName = redBlockNames.get(which);
        } else if (brownSubs.contains(args[3])) {
            which = brownSubs.indexOf(args[3]);
            mushroom = Material.BROWN_MUSHROOM_BLOCK;
            displayName = brownBlockNames.get(which);
        } else if (stemSubs.contains(args[3])) {
            which = stemSubs.indexOf(args[3]);
            mushroom = Material.MUSHROOM_STEM;
            displayName = stemBlockNames.get(which);
            if (which > 4 && which < 9) {
                which += 9999996;
            } else if (which == 9) {
                which = 5;
            } else if (which > 9) {
                which += 34;
            }
        }
        if (which != 0) {
            ItemStack is = new ItemStack(mushroom, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(displayName);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which);
            im.setCustomModelData(10000000 + which);
            is.setItemMeta(im);
            return is;
        }
        return null;
    }
}
