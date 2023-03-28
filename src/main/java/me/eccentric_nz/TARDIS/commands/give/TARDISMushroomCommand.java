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
package me.eccentric_nz.TARDIS.commands.give;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TARDISMushroomCommand {

    private final TARDIS plugin;
    private final List<String> brownBlockNames = Arrays.asList("", "The Moment", "Siege Cube");
    private final List<String> brownSubs = Arrays.asList("", "the_moment", "siege_cube");
    private final List<String> redBlockNames = Arrays.asList("", "Mechanical", "Fugitive", "Ancient", "Division", "Ars", "Bigger", "Budget", "Coral", "Deluxe", "Eleventh", "Ender", "Plank", "Pyramid", "Redstone", "Steampunk", "Thirteenth", "Factory", "Tom", "Twelfth", "War", "Small", "Medium", "Tall", "Legacy Bigger", "Legacy Budget", "Legacy Deluxe", "Legacy Eleventh", "Legacy Redstone", "Pandorica", "Master", "Atomic elements", "Chemical compounds", "Material reducer", "Element constructor", "Lab table", "Product crafting");
    private final List<String> redSubs = Arrays.asList("", "mechanical", "fugitive", "ancient", "division", "ars", "bigger", "budget", "coral", "deluxe", "eleventh", "ender", "plank", "pyramid", "redstone", "steampunk", "thirteenth", "factory", "tom", "twelfth", "war", "small", "medium", "tall", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "pandorica", "master", "creative", "compound", "reducer", "constructor", "lab", "product");
    private final List<String> stemBlockNames = Arrays.asList("", "Blue Lamp", "Green Lamp", "Purple Lamp", "Red Lamp", "Blue Lamp On", "Green Lamp On", "Purple Lamp On", "Red Lamp On", "Heat Block", "Copper", "Delta", "Rotor", "Custom", "Hexagon", "Roundel", "Roundel Offset", "Cog", "Advanced Console", "Disk Storage", "Lamp Off", "Lantern Off", "Blue Police Box", "Seed Block Grow", "Cave", "Weathered", "Original");
    private final List<String> stemSubs = Arrays.asList("", "blue_lamp", "green_lamp", "purple_lamp", "red_lamp", "blue lamp on", "green lamp on", "purple lamp on", "red lamp on", "heat_block", "copper", "delta", "rotor", "custom", "hexagon", "roundel", "roundel_offset", "cog", "advanced_console", "disk_storage", "lamp_off", "lantern_off", "blue_box", "grow", "cave", "weathered", "original");

    public TARDISMushroomCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack getStack(String arg) {
        int which = 0;
        Material mushroom = Material.RED_MUSHROOM_BLOCK;
        String displayName = "";
        if (redSubs.contains(arg)) {
            which = redSubs.indexOf(arg) + 9;
            mushroom = Material.RED_MUSHROOM_BLOCK;
            displayName = redBlockNames.get(which);
        } else if (brownSubs.contains(arg)) {
            which = brownSubs.indexOf(arg);
            mushroom = Material.BROWN_MUSHROOM_BLOCK;
            displayName = brownBlockNames.get(which);
        } else if (stemSubs.contains(arg)) {
            which = stemSubs.indexOf(arg);
            mushroom = Material.MUSHROOM_STEM;
            displayName = stemBlockNames.get(which);
            if (which > 4 && which < 9) {
                which += 9999996;
            } else if (which == 9) {
                which = 5;
            } else if (which > 9) {
                which += 32;
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
