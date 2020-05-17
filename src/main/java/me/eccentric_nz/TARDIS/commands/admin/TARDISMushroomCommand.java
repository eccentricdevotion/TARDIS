/*
 * Copyright (C) 2020 eccentric_nz
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
    private final List<String> brownBlockNames = Arrays.asList("", "The Moment", "Siege Cube", "Police Box Blue", "Police Box White", "Police Box Orange", "Police Box Magenta", "Police Box Light Blue", "Police Box Yellow", "Police Box Lime", "Police Box Pink", "Police Box Gray", "Police Box Light Gray", "Police Box Cyan", "Police Box Purple", "Police Box Brown", "Police Box Green", "Police Box Red", "Police Box Black", "Police Box Blue South", "Police Box White South", "Police Box Orange South", "Police Box Magenta South", "Police Box Light Blue South", "Police Box Yellow South", "Police Box Lime South", "Police Box Pink South", "Police Box Gray South", "Police Box Light Gray South", "Police Box Cyan South", "Police Box Purple South", "Police Box Brown South", "Police Box Green South", "Police Box Red South", "Police Box Black South", "Police Box Blue West", "Police Box White West", "Police Box Orange West", "Police Box Magenta West", "Police Box Light Blue West", "Police Box Yellow West", "Police Box Lime West", "Police Box Pink West", "Police Box Gray West", "Police Box Light Gray West", "Police Box Cyan West", "Police Box Purple West", "Police Box Brown West", "Police Box Green West", "Police Box Red West", "Police Box Black West", "Police Box Blue North", "Police Box White North", "Police Box Orange North");
    private final List<String> brownSubs = Arrays.asList("", "the_moment", "siege_cube", "tardis_blue", "tardis_white", "tardis_orange", "tardis_magenta", "tardis_light_blue", "tardis_yellow", "tardis_lime", "tardis_pink", "tardis_gray", "tardis_light_gray", "tardis_cyan", "tardis_purple", "tardis_brown", "tardis_green", "tardis_red", "tardis_black", "tardis_blue_south", "tardis_white_south", "tardis_orange_south", "tardis_magenta_south", "tardis_light_blue_south", "tardis_yellow_south", "tardis_lime_south", "tardis_pink_south", "tardis_gray_south", "tardis_light_gray_south", "tardis_cyan_south", "tardis_purple_south", "tardis_brown_south", "tardis_green_south", "tardis_red_south", "tardis_black_south", "tardis_blue_west", "tardis_white_west", "tardis_orange_west", "tardis_magenta_west", "tardis_light_blue_west", "tardis_yellow_west", "tardis_lime_west", "tardis_pink_west", "tardis_gray_west", "tardis_light_gray_west", "tardis_cyan_west", "tardis_purple_west", "tardis_brown_west", "tardis_green_west", "tardis_red_west", "tardis_black_west", "tardis_blue_north", "tardis_white_north", "tardis_orange_north");
    private final List<String> redBlockNames = Arrays.asList("", "Police Box Magenta North", "Police Box Light Blue North", "Police Box Yellow North", "Police Box Lime North", "Police Box Pink North", "Police Box Gray North", "Police Box Light Gray North", "Police Box Cyan North", "Police Box Purple North", "Police Box Brown North", "Police Box Green North", "Police Box Red North", "Police Box Black North", "Ars", "Bigger", "Budget", "Coral", "Deluxe", "Eleventh", "Ender", "Plank", "Pyramid", "Redstone", "Steampunk", "Thirteenth", "Factory", "Tom", "Twelfth", "War", "Small", "Medium", "Tall", "Legacy Bigger", "Legacy Budget", "Legacy Deluxe", "Legacy Eleventh", "Legacy Redstone", "Pandorica", "Master", "Atomic elements", "Chemical compounds", "Material reducer", "Element constructor", "Lab table", "Product crafting");
    private final List<String> redSubs = Arrays.asList("", "tardis_magenta_north", "tardis_light_blue_north", "tardis_yellow_north", "tardis_lime_north", "tardis_pink_north", "tardis_gray_north", "tardis_light_gray_north", "tardis_cyan_north", "tardis_purple_north", "tardis_brown_north", "tardis_green_north", "tardis_red_north", "tardis_black_north", "ars", "bigger", "budget", "coral", "deluxe", "eleventh", "ender", "plank", "pyramid", "redstone", "steampunk", "thirteenth", "factory", "tom", "twelfth", "war", "small", "medium", "tall", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "pandorica", "master", "creative", "compound", "reducer", "constructor", "lab", "product");
    private final List<String> stemBlockNames = Arrays.asList("", "Blue Lamp", "Red Lamp", "Purple Lamp", "Green Lamp", "Blue Lamp On", "Red Lamp On", "Purple Lamp On", "Green Lamp On", "Heat Block", "Custom", "Hexagon", "Roundel", "Roundel Offset", "Cog", "Advanced Console", "Disk Storage", "Lamp Off", "Lantern Off", "Blue Police Box", "Seed Block Grow", "Police Box Blue Open", "Police Box Blue Open South", "Police Box Blue Open West", "Police Box Blue Open North");
    private final List<String> stemSubs = Arrays.asList("", "blue_lamp", "red_lamp", "purple_lamp", "green_lamp", "blue_lamp_on", "red_lamp_on", "purple_lamp_on", "green_lamp_on", "heat_block", "custom", "hexagon", "roundel", "roundel_offset", "cog", "advanced_console", "disk_storage", "lamp_off", "lantern_off", "blue_box", "grow", "tardis_blue_open", "tardis_blue_open_south", "tardis_blue_open_west", "tardis_blue_open_north");

    public TARDISMushroomCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack getStack(String[] args) {
        int which = 0;
        Material mushroom = Material.RED_MUSHROOM_BLOCK;
        String displayName = "";
        if (redSubs.contains(args[3])) {
            which = redSubs.indexOf(args[3]);
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
                which += 35;
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
