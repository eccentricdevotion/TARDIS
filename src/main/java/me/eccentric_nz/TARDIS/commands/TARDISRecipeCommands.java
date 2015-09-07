/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MAP;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It
 * is stored in the Secondary Control Room. All TARDISes have one of these
 * devices, which can be used to remotely control a TARDIS by broadcasting
 * Stattenheim signals that travel along the time contours in the Space/Time
 * Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISRecipeCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();
    private final HashMap<String, Material> t = new HashMap<String, Material>();

    public TARDISRecipeCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("a-circuit"); // Admin Circuit
        firstArgs.add("ars-circuit"); // ARS Circuit
        firstArgs.add("bio-circuit"); // Bio-scanner Circuit
        firstArgs.add("biome-disk"); // Biome Storage Disk
        firstArgs.add("blank"); // Blank Storage Disk
        firstArgs.add("battery"); // Blaster Battery
        firstArgs.add("blaster"); // Sonic Blaster
        firstArgs.add("bow-tie"); // Bow Tie
        firstArgs.add("c-circuit"); // Chameleon Circuit
        firstArgs.add("cell"); // Artron Energy Cell
        firstArgs.add("custard"); // Bowl of custard
        firstArgs.add("d-circuit"); // Diamond Circuit
        firstArgs.add("e-circuit"); // Emerald Circuit
        firstArgs.add("filter"); // Perception Filter
        firstArgs.add("fish-finger"); // Fish Finger
        firstArgs.add("furnace"); // TARDIS Artron Furnace
        firstArgs.add("glasses"); // 3-D Glasses
        firstArgs.add("i-circuit"); // Input Circuit
        firstArgs.add("ignite-circuit"); // Ignite Circuit
        firstArgs.add("invisible"); // Invisibility Circuit
        firstArgs.add("key"); // TARDIS key
        firstArgs.add("jammy-dodger"); // Jammy Dodger Biscuit
        firstArgs.add("jelly-baby"); // Jelly Baby
        firstArgs.add("l-circuit"); // Locator Circuit
        firstArgs.add("locator"); // TARDIS Locator
        firstArgs.add("m-circuit"); // Materialisation Circuit
        firstArgs.add("memory-circuit"); // Memory Circuit
        firstArgs.add("oscillator"); // Sonic Oscillator
        firstArgs.add("pad"); // Landing Pad
        firstArgs.add("painter"); // Painter Circuit
        firstArgs.add("p-circuit"); // Perception Circuit
        firstArgs.add("player-disk"); // Player Storage Disk
        firstArgs.add("preset-disk"); // Preset Storage Disk
        firstArgs.add("r-circuit"); // Redstone Circuit
        firstArgs.add("r-key"); // TARDIS Remote Key
        firstArgs.add("randomiser-circuit"); // Randomiser Circuit
        firstArgs.add("remote"); // Stattenheim Remote
        firstArgs.add("s-circuit"); // Stattenheim Circuit
        firstArgs.add("save-disk"); // Save Storage Disk
        firstArgs.add("scanner-circuit"); // Scanner Circuit
        firstArgs.add("sonic"); // Sonic Screwdriver
        firstArgs.add("t-circuit"); // Temporal Circuit
        firstArgs.add("tardis"); // TARDIS Seed Block
        firstArgs.add("vortex"); // Vortex Manipulator
        firstArgs.add("watch"); // TARDIS Seed Block
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics supplied by Lord_Rahl and killeratnight at mcnovus.net
        t.put("ARS", Material.QUARTZ_BLOCK); // ARS
        t.put("BIGGER", Material.GOLD_BLOCK); // bigger
        t.put("BUDGET", Material.IRON_BLOCK); // budget
        t.put("DELUXE", Material.DIAMOND_BLOCK); // deluxe
        t.put("ELEVENTH", Material.EMERALD_BLOCK); // eleventh
        t.put("PLANK", Material.BOOKSHELF); // plank
        t.put("REDSTONE", Material.REDSTONE_BLOCK); // redstone
        t.put("STEAMPUNK", Material.COAL_BLOCK); // steampunk
        t.put("TOM", Material.LAPIS_BLOCK); // tom baker
        t.put("TWELFTH", Material.PRISMARINE); // twelfth
        t.put("WAR", Material.STAINED_CLAY); // war doctor
        t.put("PYRAMID", Material.SANDSTONE_STAIRS); // pyramid schematic supplied by airomis (player at thatsnotacreeper.com)
        t.put("MASTER", Material.NETHER_BRICK); // master schematic supplied by macdja38 at pvpcraft.ca
        // custom seeds
        for (String console : plugin.getCustomConsolesConfig().getKeys(false)) {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                Material cmat = Material.valueOf(plugin.getCustomConsolesConfig().getString(console + ".seed"));
                t.put(console.toUpperCase(), cmat);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisrecipe")) {
            if (!sender.hasPermission("tardis.help")) {
                TARDISMessage.send(sender, "NO_PERMS");
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                if (args.length == 0 || !firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                    new TARDISRecipeLister(plugin, sender).list();
                } else {
                    TARDISMessage.send(sender, "CMD_PLAYER");
                }
                return true;
            }
            if (args.length < 1) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            if (!firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                new TARDISRecipeLister(plugin, sender).list();
                return true;
            }
            if (args[0].equalsIgnoreCase("tardis") && args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return true;
            }
            if (args[0].equalsIgnoreCase("tardis") && args.length == 2) {
                if (!t.containsKey(args[1].toUpperCase())) {
                    TARDISMessage.send(player, "ARG_NOT_VALID");
                    return true;
                }
                this.showTARDISRecipe(player, args[1]);
                return true;
            }
            if (args[0].equalsIgnoreCase("a-circuit")) {
                this.showShapedRecipe(player, "Server Admin Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("ars-circuit")) {
                this.showShapedRecipe(player, "TARDIS ARS Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("bio-circuit")) {
                this.showShapedRecipe(player, "Bio-scanner Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("biome-disk")) {
                this.showShapelessRecipe(player, "Biome Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("blank")) {
                this.showShapedRecipe(player, "Blank Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("bow-tie")) {
                this.showShapedRecipe(player, "Red Bow Tie");
                return true;
            }
            if (args[0].equalsIgnoreCase("c-circuit")) {
                showShapedRecipe(player, "TARDIS Chameleon Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("cell")) {
                this.showShapedRecipe(player, "Artron Storage Cell");
                return true;
            }
            if (args[0].equalsIgnoreCase("custard")) {
                this.showShapelessRecipe(player, "Bowl of Custard");
                return true;
            }
            if (args[0].equalsIgnoreCase("d-circuit")) {
                this.showShapedRecipe(player, "Diamond Disruptor Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("e-circuit")) {
                this.showShapedRecipe(player, "Emerald Environment Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("filter")) {
                this.showShapedRecipe(player, "Perception Filter");
                return true;
            }
            if (args[0].equalsIgnoreCase("fish-finger")) {
                this.showShapedRecipe(player, "Fish Finger");
                return true;
            }
            if (args[0].equalsIgnoreCase("furnace")) {
                this.showShapedRecipe(player, "TARDIS Artron Furnace");
                return true;
            }
            if (args[0].equalsIgnoreCase("glasses")) {
                this.showShapedRecipe(player, "3-D Glasses");
                return true;
            }
            if (args[0].equalsIgnoreCase("i-circuit")) {
                this.showShapedRecipe(player, "TARDIS Input Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("ignite-circuit")) {
                this.showShapedRecipe(player, "Ignite Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("invisible")) {
                this.showShapedRecipe(player, "TARDIS Invisibility Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("key")) {
                this.showShapedRecipe(player, "TARDIS Key");
                return true;
            }
            if (args[0].equalsIgnoreCase("jammy-dodger")) {
                this.showShapedRecipe(player, "Jammy Dodger");
                return true;
            }
            if (args[0].equalsIgnoreCase("jelly-baby")) {
                this.showShapelessRecipe(player, "Orange Jelly Baby");
                return true;
            }
            if (args[0].equalsIgnoreCase("l-circuit")) {
                this.showShapedRecipe(player, "TARDIS Locator Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("locator")) {
                showShapedRecipe(player, "TARDIS Locator");
                return true;
            }
            if (args[0].equalsIgnoreCase("m-circuit")) {
                this.showShapedRecipe(player, "TARDIS Materialisation Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("memory-circuit")) {
                this.showShapedRecipe(player, "TARDIS Memory Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("oscillator")) {
                this.showShapedRecipe(player, "Sonic Oscillator");
                return true;
            }
            if (args[0].equalsIgnoreCase("p-circuit")) {
                this.showShapedRecipe(player, "Perception Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("painter")) {
                this.showShapedRecipe(player, "Painter Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("player-disk")) {
                this.showShapelessRecipe(player, "Player Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("preset-disk")) {
                this.showShapelessRecipe(player, "Preset Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("r-circuit")) {
                this.showShapedRecipe(player, "Redstone Activator Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("r-key")) {
                this.showShapedRecipe(player, "TARDIS Remote Key");
                return true;
            }
            if (args[0].equalsIgnoreCase("randomiser-circuit")) {
                this.showShapedRecipe(player, "TARDIS Randomiser Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("remote")) {
                showShapedRecipe(player, "Stattenheim Remote");
                return true;
            }
            if (args[0].equalsIgnoreCase("s-circuit")) {
                showShapedRecipe(player, "TARDIS Stattenheim Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("save-disk")) {
                this.showShapelessRecipe(player, "Save Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("scanner-circuit")) {
                this.showShapedRecipe(player, "TARDIS Scanner Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("sonic")) {
                this.showShapedRecipe(player, "Sonic Screwdriver");
                return true;
            }
            if (args[0].equalsIgnoreCase("t-circuit")) {
                this.showShapedRecipe(player, "TARDIS Temporal Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("watch")) {
                this.showShapedRecipe(player, "Fob Watch");
                return true;
            }
            if (args[0].equalsIgnoreCase("vortex")) {
                if (!plugin.getPM().isPluginEnabled("TARDISVortexManipulator")) {
                    TARDISMessage.send(sender, "RECIPE_VORTEX");
                    return true;
                }
                this.showShapedRecipe(player, "Vortex Manipulator");
                return true;
            }
            if ((args[0].equalsIgnoreCase("battery") || args[0].equalsIgnoreCase("blaster") || args[0].equalsIgnoreCase("pad")) && !plugin.getPM().isPluginEnabled("TARDISSonicBlaster")) {
                TARDISMessage.send(sender, "RECIPE_BLASTER");
                return true;
            }
            if (args[0].equalsIgnoreCase("battery")) {
                this.showShapedRecipe(player, "Blaster Battery");
                return true;
            }
            if (args[0].equalsIgnoreCase("blaster")) {
                this.showShapedRecipe(player, "Sonic Blaster");
                return true;
            }
            if (args[0].equalsIgnoreCase("pad")) {
                this.showShapedRecipe(player, "Landing Pad");
                return true;
            }
        }
        return false;
    }

    public void showShapedRecipe(Player p, String str) {
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(str);
        p.closeInventory();
        plugin.getTrackerKeeper().getRecipeView().add(p.getUniqueId());
        final InventoryView view = p.openWorkbench(null, true);
        final String[] recipeShape = recipe.getShape();
        final Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        for (int j = 0; j < recipeShape.length; j++) {
            for (int k = 0; k < recipeShape[j].length(); k++) {
                final ItemStack item = ingredientMap.get(recipeShape[j].toCharArray()[k]);
                if (item == null) {
                    continue;
                }
                if (item.getType().equals(Material.MAP)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName(getDisplayName(item.getData().getData()));
                    item.setItemMeta(im);
                }
                if (str.equals("TARDIS Remote Key") && item.getType().equals(Material.GOLD_NUGGET)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName("TARDIS Key");
                    item.setItemMeta(im);
                }
                if (str.equals("Sonic Blaster") && item.getType().equals(Material.BUCKET)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName("Blaster Battery");
                    item.setItemMeta(im);
                }
                view.getTopInventory().setItem(j * 3 + k + 1, item);
            }
        }
    }

    public void showShapelessRecipe(Player player, String str) {
        ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(str);
        final List<ItemStack> ingredients = recipe.getIngredientList();
        plugin.getTrackerKeeper().getRecipeView().add(player.getUniqueId());
        final InventoryView view = player.openWorkbench(null, true);
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getType().equals(Material.MAP)) {
                ItemMeta im = ingredients.get(i).getItemMeta();
                im.setDisplayName(getDisplayName(ingredients.get(i).getData().getData()));
                ingredients.get(i).setItemMeta(im);
            }
            if (ingredients.get(i).getType().equals(Material.RECORD_9)) {
                ItemMeta im = ingredients.get(i).getItemMeta();
                im.setDisplayName("Blank Storage Disk");
                ingredients.get(i).setItemMeta(im);
            }
            view.setItem(i + 1, ingredients.get(i));
        }
    }

    public void showTARDISRecipe(Player player, String type) {
        plugin.getTrackerKeeper().getRecipeView().add(player.getUniqueId());
        final InventoryView view = player.openWorkbench(null, true);
        // redstone torch
        ItemStack red = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
        // lapis block
        ItemStack lapis = new ItemStack(Material.LAPIS_BLOCK, 1);
        // restone lamp
        ItemStack lamp = new ItemStack(Material.REDSTONE_LAMP_OFF, 1);
        ItemMeta lamp_meta = lamp.getItemMeta();
        lamp_meta.setDisplayName("Police Box lamp");
        lamp_meta.setLore(Arrays.asList("Any valid lamp item:", "Redstone Lamp", "Glowstone", "Torches"));
        lamp.setItemMeta(lamp_meta);
        // police box wall
        ItemStack pb_wall = new ItemStack(Material.WOOL, 1, (byte) 11);
        ItemMeta pb_meta = pb_wall.getItemMeta();
        pb_meta.setDisplayName("Police Box walls");
        pb_meta.setLore(Arrays.asList("Any valid Chameleon block"));
        pb_wall.setItemMeta(pb_meta);
        // interior wall
        ItemStack in_wall = new ItemStack(Material.WOOL, 1, (byte) 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.setDisplayName("Interior walls");
        in_meta.setLore(Arrays.asList("Any valid Wall/Floor block"));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = new ItemStack(Material.WOOL, 1, (byte) 8);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.setDisplayName("Interior floors");
        fl_meta.setLore(Arrays.asList("Any valid Wall/Floor block"));
        in_floor.setItemMeta(fl_meta);
        // tardis type
        ItemStack tardis = new ItemStack(t.get(type.toUpperCase()), 1);
        ItemMeta seed = tardis.getItemMeta();
        seed.setLore(Arrays.asList(type.toUpperCase()));
        tardis.setItemMeta(seed);
        view.setItem(1, red);
        view.setItem(4, lapis);
        view.setItem(5, lamp);
        view.setItem(6, in_wall);
        view.setItem(7, tardis);
        view.setItem(8, pb_wall);
        view.setItem(9, in_floor);
    }

    private String getDisplayName(byte data) {
        MAP map = MAP.getMap(data);
        if (map != null) {
            return map.getDisplayName();
        } else {
            return "Map #" + data;
        }
    }
}
