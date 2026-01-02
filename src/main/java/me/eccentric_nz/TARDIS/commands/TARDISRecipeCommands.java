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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISRecipeLister;
import me.eccentric_nz.TARDIS.recipes.TARDISRecipeCategoryInventory;
import me.eccentric_nz.TARDIS.recipes.TARDISShowSeedRecipeInventory;
import me.eccentric_nz.TARDIS.recipes.TARDISShowShapedRecipeInventory;
import me.eccentric_nz.TARDIS.recipes.TARDISShowShapelessRecipeInventory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It is stored in the Secondary Control Room.
 * All TARDISes have one of these devices, which can be used to remotely control a TARDIS by broadcasting Stattenheim
 * signals that travel along the time contours in the Space/Time Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISRecipeCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final HashMap<String, String> recipeItems = new HashMap<>();
    private final HashMap<String, Material> t = new HashMap<>();
    private final Set<String> CHEM_SUBS = new HashSet<>();
    private final List<String> CHEM_BLOCKS = List.of("atomic-elements", "chemical-compounds", "lab-table", "product-crafting", "material-reducer", "element-constructor");

    public TARDISRecipeCommands(TARDIS plugin) {
        this.plugin = plugin;
        recipeItems.put("seed", "");
        recipeItems.put("tardis", "");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE && recipeItem.getCategory() != RecipeCategory.UNUSED) {
                recipeItems.put(recipeItem.toTabCompletionString(), recipeItem.toRecipeString());
            }
        }
        // custom time rotors
        for (String r : TARDIS.plugin.getCustomRotorsConfig().getKeys(false)) {
            recipeItems.put("time-rotor-" + r.toLowerCase(Locale.ROOT), "Time Rotor " + TARDISStringUtils.capitalise(r));
        }
        // custom doors
        for (String d : TARDIS.plugin.getCustomDoorsConfig().getKeys(false)) {
            recipeItems.put("door-" + d.toLowerCase(Locale.ROOT), "Door " + TARDISStringUtils.capitalise(d));
        }
        // custom consoles
        for (String c : TARDIS.plugin.getCustomConsolesConfig().getConfigurationSection("consoles").getKeys(false)) {
            recipeItems.put("console-" + c.toLowerCase(Locale.ROOT), TARDISStringUtils.capitalise(c) + " Console");
        }
        // remove recipes form modules that are not enabled
        if (!plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            recipeItems.remove("vortex-manipulator");
        }
        if (!plugin.getConfig().getBoolean("modules.regeneration")) {
            recipeItems.remove("elixir-of-life");
        }
        if (!plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            recipeItems.remove("sonic-blaster");
            recipeItems.remove("blaster-battery");
            recipeItems.remove("landing-pad");
        }
        if (!plugin.getConfig().getBoolean("modules.weeping_angels")) {
            recipeItems.remove("judoon-ammunition");
            recipeItems.remove("k9");
        }
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        t.put("ANCIENT", Material.SCULK); // ancient
        t.put("ARS", Material.QUARTZ_BLOCK); // ARS
        t.put("BIGGER", Material.GOLD_BLOCK); // bigger
        t.put("BONE", Material.WAXED_OXIDIZED_CUT_COPPER); // bone loosely based on a console by DT10 - https://www.youtube.com/watch?v=Ux4qt0qYm80
        t.put("BUDGET", Material.IRON_BLOCK); // budget
        t.put("CAVE", Material.DRIPSTONE_BLOCK); // dripstone cave
        t.put("COPPER", Material.WARPED_PLANKS); // copper schematic designed by vistaero
        t.put("CORAL", Material.NETHER_WART_BLOCK); // coral schematic designed by vistaero
        t.put("CURSED", Material.BLACK_CONCRETE); // designed by airomis (player at thatsnotacreeper.com)
        t.put("DELTA", Material.CRYING_OBSIDIAN); // delta
        t.put("DELUXE", Material.DIAMOND_BLOCK); // deluxe
        t.put("DIVISION", Material.PINK_GLAZED_TERRACOTTA); // division
        t.put("EIGHTH", Material.CHISELED_STONE_BRICKS); // eighth
        t.put("ELEVENTH", Material.EMERALD_BLOCK); // eleventh
        t.put("ENDER", Material.PURPUR_BLOCK); // ender schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        t.put("FACTORY", Material.YELLOW_CONCRETE_POWDER); // factory schematic designed by Razihel
        t.put("FIFTEENTH", Material.OCHRE_FROGLIGHT); // designed by airomis (player at thatsnotacreeper.com)
        t.put("FUGITIVE", Material.POLISHED_DEEPSLATE); // fugitive - based on TARDIS designed by DT10 - https://www.youtube.com/watch?v=aykwXVemSs8
        t.put("HOSPITAL", Material.WHITE_CONCRETE); // hospital
        t.put("MASTER", Material.NETHER_BRICKS); // master schematic designed by ShadowAssociate
        t.put("MECHANICAL", Material.POLISHED_ANDESITE); // mechanical schematic adapted from design by Plastic Straw
        t.put("ORIGINAL", Material.PACKED_MUD); // original
        t.put("PLANK", Material.BOOKSHELF); // plank
        t.put("PYRAMID", Material.SANDSTONE_STAIRS); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
        t.put("REDSTONE", Material.REDSTONE_BLOCK); // redstone
        t.put("ROTOR", Material.HONEYCOMB_BLOCK); // rotor
        t.put("RUSTIC", Material.COPPER_BULB); // rustic
        t.put("SIDRAT", Material.GREEN_CONCRETE); // sidrat
        t.put("STEAMPUNK", Material.COAL_BLOCK); // steampunk
        t.put("THIRTEENTH", Material.ORANGE_CONCRETE); // thirteenth designed by Razihel
        t.put("TOM", Material.LAPIS_BLOCK); // tom baker
        t.put("TWELFTH", Material.PRISMARINE); // twelfth
        t.put("WAR", Material.WHITE_TERRACOTTA); // war doctor
        t.put("WEATHERED", Material.WEATHERED_COPPER); // weathered
        t.put("LEGACY_BIGGER", Material.ORANGE_GLAZED_TERRACOTTA);
        t.put("LEGACY_DELUXE", Material.LIME_GLAZED_TERRACOTTA);
        t.put("LEGACY_ELEVENTH", Material.CYAN_GLAZED_TERRACOTTA);
        t.put("LEGACY_REDSTONE", Material.RED_GLAZED_TERRACOTTA);
        // custom seeds
        plugin.getCustomDesktopsConfig().getKeys(false).forEach((console) -> {
            if (plugin.getCustomDesktopsConfig().getBoolean(console + ".enabled")) {
                Material cmat = Material.valueOf(plugin.getCustomDesktopsConfig().getString(console + ".seed"));
                t.put(console.toUpperCase(Locale.ROOT), cmat);
            }
        });
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() == RecipeCategory.CHEMISTRY) {
                CHEM_SUBS.add(recipeItem.toTabCompletionString());
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisrecipe")) {
            if (!TARDISPermission.hasPermission(sender, "tardis.help")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                if (args.length == 0) {
                    new TARDISRecipeLister(plugin, sender).list();
                } else {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                }
                return true;
            }
            if (args.length == 0) {
                // open recipe GUI
                player.openInventory(new TARDISRecipeCategoryInventory(plugin).getInventory());
                return true;
            }
            String which = args[0].toLowerCase(Locale.ROOT);
            if (CHEM_SUBS.contains(which)) {
                // use `/tchemistry formula command`
                String command;
                if (CHEM_BLOCKS.contains(which)) {
                    command = which.split("-")[0];
                    plugin.getMessenger().sendColouredCommand(player, "USE_FORMULA", "/tchemistry recipe " + command, plugin);
                } else {
                    command = TARDISStringUtils.chemistryCase(which);
                    plugin.getMessenger().sendColouredCommand(player, "USE_FORMULA", "/tchemistry formula " + command, plugin);
                }
                return true;
            }
            if (!recipeItems.containsKey(which)) {
                if (args[0].equalsIgnoreCase("list_more")) {
                    new TARDISRecipeLister(plugin, sender).listMore();
                } else {
                    new TARDISRecipeLister(plugin, sender).list();
                }
                return true;
            }
            if ((args[0].equalsIgnoreCase("seed") || args[0].equalsIgnoreCase("tardis")) && args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return true;
            }
            if ((args[0].equalsIgnoreCase("seed") || args[0].equalsIgnoreCase("tardis")) && args.length == 2) {
                if (!t.containsKey(args[1].toUpperCase(Locale.ROOT))) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_VALID");
                    return true;
                }
                showTARDISRecipe(player, args[1].toUpperCase(Locale.ROOT));
                return true;
            }
            switch (which) {
                case "bowl-of-custard", "jelly-baby", "biome-storage-disk", "player-storage-disk",
                     "preset-storage-disk", "save-storage-disk", "schematic-wand", "admin-upgrade",
                     "bio-scanner-upgrade", "redstone-upgrade", "diamond-upgrade", "emerald-upgrade", "painter-upgrade",
                     "ignite-upgrade", "pickup-arrows-upgrade", "knockback-upgrade", "brush-upgrade",
                     "judoon-ammunition" -> {
                    showShapelessRecipe(player, recipeItems.get(which));
                    return true;
                }
                default -> {
                    showShapedRecipe(player, recipeItems.get(which));
                    return true;
                }
            }
        }
        return false;
    }

    // maps still seem to use numeric values
    private void showShapedRecipe(Player player, String str) {
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(str);
        if (recipe == null) {
            plugin.debug(str);
        } else {
            player.discoverRecipe(recipe.getKey());
            player.closeInventory();
            plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
            player.openInventory(new TARDISShowShapedRecipeInventory(plugin, recipe, str).getInventory());
        }
    }

    private void showShapelessRecipe(Player player, String str) {
        ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(str);
        player.discoverRecipe(recipe.getKey());
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        player.openInventory(new TARDISShowShapelessRecipeInventory(plugin, recipe, str).getInventory());
    }

    private void showTARDISRecipe(Player player, String type) {
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        player.openInventory(new TARDISShowSeedRecipeInventory(plugin, type, t.get(type)).getInventory());
    }
}
