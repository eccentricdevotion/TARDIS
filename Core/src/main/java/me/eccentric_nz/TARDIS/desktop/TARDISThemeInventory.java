/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.keys.SeedBlock;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone
 * through at least twelve redesigns, though the TARDIS revealed that she had
 * archived 30 versions. Once a control room was reconfigured, the TARDIS
 * archived the old design "for neatness". The TARDIS effectively "curated" a
 * museum of control rooms — both those in the Doctor's personal past and future
 *
 * @author eccentric_nz
 */
public class TARDISThemeInventory {

    /**
     * Constructs an item stack for the Desktop Theme GUI.
     *
     * @param plugin an instance of the TARDIS plugin
     * @param schematic the console schematic to get the item stack for
     * @param currentConsole the players current console
     * @param player the player using the GUI
     * @param level the player's Artron level
     * @return an ItemStack with the console information
     */
    public ItemStack getConsoleStack(TARDIS plugin, Schematic schematic, String currentConsole, Player player, int level) {
        ItemStack is = null;
        // get console
        Material m = Material.getMaterial(schematic.getSeed());
        if (!m.equals(Material.COBBLESTONE)) {
            is = new ItemStack(m, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(schematic.getDescription());
            int cost = plugin.getArtronConfig().getInt("upgrades." + schematic.getPermission());
            if (currentConsole.equals(schematic.getPermission())) {
                cost = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * cost);
            }
            List<String> lore = new ArrayList<>();
            lore.add("Cost: " + cost);
            if (!TARDISPermission.hasPermission(player, "tardis." + schematic.getPermission())) {
                lore.add(ChatColor.RED + plugin.getLanguage().getString("NO_PERM_CONSOLE"));
            } else if (level < cost && !currentConsole.equals(schematic.getPermission())) {
                lore.add(plugin.getLanguage().getString("UPGRADE_ABORT_ENERGY"));
            }
            if (currentConsole.equals(schematic.getPermission())) {
                lore.add(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"));
            } else {
                lore.add(ChatColor.GREEN + plugin.getLanguage().getString("RESET"));
                lore.add(ChatColor.GREEN + plugin.getLanguage().getString("REMEMBER"));
            }
            im.setLore(lore);
            switch (m) {
                case BLACK_CONCRETE -> im.setItemModel(SeedBlock.CURSED.getKey()); // cursed schematic designed by airomis (player at thatsnotacreeper.com)
                case BOOKSHELF -> im.setItemModel(SeedBlock.PLANK.getKey()); // plank
                case COAL_BLOCK -> im.setItemModel(SeedBlock.STEAMPUNK.getKey()); // steampunk
                case COPPER_BULB -> im.setItemModel(SeedBlock.RUSTIC.getKey()); // rustic
                case CRYING_OBSIDIAN -> im.setItemModel(SeedBlock.DELTA.getKey()); // delta
                case DIAMOND_BLOCK -> im.setItemModel(SeedBlock.DELUXE.getKey()); // deluxe
                case DRIPSTONE_BLOCK -> im.setItemModel(SeedBlock.CAVE.getKey()); // dripstone cave
                case EMERALD_BLOCK -> im.setItemModel(SeedBlock.ELEVENTH.getKey()); // eleventh
                case GOLD_BLOCK -> im.setItemModel(SeedBlock.BIGGER.getKey()); // bigger
                case HONEYCOMB_BLOCK -> im.setItemModel(SeedBlock.ROTOR.getKey()); // rotor
                case IRON_BLOCK -> im.setItemModel(SeedBlock.BUDGET.getKey()); // budget
                case LAPIS_BLOCK -> im.setItemModel(SeedBlock.TOM.getKey()); // tom baker
                case NETHER_BRICKS -> im.setItemModel(SeedBlock.MASTER.getKey()); // master schematic designed by ShadowAssociate
                case NETHER_WART_BLOCK -> im.setItemModel(SeedBlock.CORAL.getKey()); // coral schematic designed by vistaero
                case OCHRE_FROGLIGHT -> im.setItemModel(SeedBlock.FIFTEENTH.getKey()); // fifteenth schematic designed by airomis (player at thatsnotacreeper.com)
                case ORANGE_CONCRETE -> im.setItemModel(SeedBlock.THIRTEENTH.getKey()); // thirteenth designed by Razihel
                case PACKED_MUD -> im.setItemModel(SeedBlock.ORIGINAL.getKey()); // original
                case POLISHED_ANDESITE -> im.setItemModel(SeedBlock.MECHANICAL.getKey()); // mechanical adapted from design by Plastic Straw https://www.planetminecraft.com/data-pack/new-tardis-mod-mechanical-interior-datapack/
                case POLISHED_DEEPSLATE -> im.setItemModel(SeedBlock.FUGITIVE.getKey()); // fugitive schematic based on design by DT10 - https://www.youtube.com/watch?v=aykwXVemSs8
                case PRISMARINE -> im.setItemModel(SeedBlock.TWELFTH.getKey()); // twelfth
                case PURPUR_BLOCK -> im.setItemModel(SeedBlock.ENDER.getKey()); // ender schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
                case QUARTZ_BLOCK -> im.setItemModel(SeedBlock.ARS.getKey()); // ARS
                case REDSTONE_BLOCK -> im.setItemModel(SeedBlock.REDSTONE.getKey()); // redstone
                case SANDSTONE_STAIRS -> im.setItemModel(SeedBlock.PYRAMID.getKey()); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
                case SCULK -> im.setItemModel(SeedBlock.ANCIENT.getKey()); // ancient city
                case WAXED_OXIDIZED_CUT_COPPER -> im.setItemModel(SeedBlock.BONE.getKey()); // bone loosely based on a console by DT10 - https://www.youtube.com/watch?v=Ux4qt0qYm80
                case WARPED_PLANKS -> im.setItemModel(SeedBlock.COPPER.getKey()); // copper schematic designed by vistaero
                case WEATHERED_COPPER -> im.setItemModel(SeedBlock.WEATHERED.getKey()); // weathered copper
                case WHITE_CONCRETE -> im.setItemModel(SeedBlock.HOSPITAL.getKey()); // hospital
                case WHITE_TERRACOTTA -> im.setItemModel(SeedBlock.WAR.getKey()); // war doctor
                case YELLOW_CONCRETE_POWDER -> im.setItemModel(SeedBlock.FACTORY.getKey()); // factory designed by Razihel
                case CYAN_GLAZED_TERRACOTTA -> im.setItemModel(SeedBlock.LEGACY_ELEVENTH.getKey()); // legacy_eleventh
                case LIME_GLAZED_TERRACOTTA -> im.setItemModel(SeedBlock.LEGACY_DELUXE.getKey()); // legacy_deluxe
                case ORANGE_GLAZED_TERRACOTTA -> im.setItemModel(SeedBlock.LEGACY_BIGGER.getKey()); // legacy_bigger
                case RED_GLAZED_TERRACOTTA -> im.setItemModel(SeedBlock.LEGACY_REDSTONE.getKey()); // legacy_redstone
                default -> im.setItemModel(SeedBlock.CUSTOM.getKey());
            }
            is.setItemMeta(im);
        }
        return is;
    }
}
