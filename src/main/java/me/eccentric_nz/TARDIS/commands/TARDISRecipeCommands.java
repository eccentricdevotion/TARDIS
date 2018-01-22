/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.INVENTORY_MANAGER;
import me.eccentric_nz.TARDIS.enumeration.MAP;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
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
    private final List<String> firstArgs = new ArrayList<>();
    private final HashMap<String, Material> t = new HashMap<>();

    public TARDISRecipeCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("a-circuit"); // Admin Circuit
        firstArgs.add("acid-battery"); // Admin Circuit
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
        firstArgs.add("generator"); // Sonic Generator
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
        firstArgs.add("reader"); // TARDIS Biome Reader
        firstArgs.add("remote"); // Stattenheim Remote
        firstArgs.add("rift-circuit"); // Rift Circuit
        firstArgs.add("rift-manipulator"); // Rift Manipulator
        firstArgs.add("rust"); // Rust Plague Sword
        firstArgs.add("s-circuit"); // Stattenheim Circuit
        firstArgs.add("save-disk"); // Save Storage Disk
        firstArgs.add("scanner-circuit"); // Scanner Circuit
        firstArgs.add("sonic"); // Sonic Screwdriver
        firstArgs.add("t-circuit"); // Temporal Circuit
        firstArgs.add("tardis"); // TARDIS Seed Block
        firstArgs.add("telepathic"); // TARDIS Telepathic Circuit
        firstArgs.add("vortex"); // Vortex Manipulator
        firstArgs.add("watch"); // TARDIS Seed Block
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        t.put("ARS", Material.QUARTZ_BLOCK); // ARS
        t.put("BIGGER", Material.GOLD_BLOCK); // bigger
        t.put("BUDGET", Material.IRON_BLOCK); // budget
        t.put("CORAL", Material.NETHER_WART_BLOCK); // coral schematic designed by vistaero
        t.put("DELUXE", Material.DIAMOND_BLOCK); // deluxe
        t.put("ELEVENTH", Material.EMERALD_BLOCK); // eleventh
        t.put("ENDER", Material.PURPUR_BLOCK); // ender schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        t.put("PLANK", Material.BOOKSHELF); // plank
        t.put("REDSTONE", Material.REDSTONE_BLOCK); // redstone
        t.put("STEAMPUNK", Material.COAL_BLOCK); // steampunk
        t.put("TOM", Material.LAPIS_BLOCK); // tom baker
        t.put("TWELFTH", Material.PRISMARINE); // twelfth
        t.put("WAR", Material.WHITE_TERRACOTTA); // war doctor
        t.put("PYRAMID", Material.SANDSTONE_STAIRS); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
        t.put("MASTER", Material.NETHER_BRICKS); // master schematic designed by ShadowAssociate
        t.put("LEGACY_BIGGER", Material.ORANGE_GLAZED_TERRACOTTA);
        t.put("LEGACY_BUDGET", Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        t.put("LEGACY_DELUXE", Material.LIME_GLAZED_TERRACOTTA);
        t.put("LEGACY_ELEVENTH", Material.CYAN_GLAZED_TERRACOTTA);
        t.put("LEGACY_REDSTONE", Material.RED_GLAZED_TERRACOTTA);
        // custom seeds
        plugin.getCustomConsolesConfig().getKeys(false).forEach((console) -> {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                Material cmat = Material.valueOf(plugin.getCustomConsolesConfig().getString(console + ".seed"));
                t.put(console.toUpperCase(Locale.ENGLISH), cmat);
            }
        });
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
                if (!t.containsKey(args[1].toUpperCase(Locale.ENGLISH))) {
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
            if (args[0].equalsIgnoreCase("acid-battery")) {
                this.showShapedRecipe(player, "Acid Battery");
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
            if (args[0].equalsIgnoreCase("generator")) {
                this.showShapedRecipe(player, "Sonic Generator");
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
            if (args[0].equalsIgnoreCase("reader")) {
                showShapedRecipe(player, "TARDIS Biome Reader");
                return true;
            }
            if (args[0].equalsIgnoreCase("remote")) {
                showShapedRecipe(player, "Stattenheim Remote");
                return true;
            }
            if (args[0].equalsIgnoreCase("rift-circuit")) {
                showShapedRecipe(player, "Rift Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("rift-manipulator")) {
                showShapedRecipe(player, "Rift Manipulator");
                return true;
            }
            if (args[0].equalsIgnoreCase("rust")) {
                showShapedRecipe(player, "Rust Plague Sword");
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
            if (args[0].equalsIgnoreCase("telepathic")) {
                this.showShapedRecipe(player, "TARDIS Telepathic Circuit");
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
        Inventory inv = plugin.getServer().createInventory(p, 27, "§4" + str + " recipe");
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
                if (str.equals("Acid Battery") && item.getType().equals(Material.WATER_BUCKET)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName("Acid Bucket");
                    item.setItemMeta(im);
                }
                if (str.equals("Rift Manipulator") && item.getType().equals(Material.NETHER_BRICK)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName("Acid Battery");
                    item.setItemMeta(im);
                }
                if (str.equals("Rift Manipulator") && item.getType().equals(Material.MAP)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName("Rift Circuit");
                    item.setItemMeta(im);
                }
                inv.setItem(j * 9 + k, item);
            }
        }
        ItemStack result = recipe.getResult();
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(str);
        if (str.equals("TARDIS Invisibility Circuit")) {
            // set the second line of lore
            List<String> lore = im.getLore();
            String uses = (plugin.getConfig().getString("circuits.uses.invisibility").equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.invisibility");
            lore.set(1, uses);
            im.setLore(lore);
        }
        if ((str.equals("Save Storage Disk") || str.equals("Preset Storage Disk") || str.equals("Biome Storage Disk") || str.equals("Player Storage Disk") || str.equals("Sonic Blaster")) && !plugin.getInvManager().equals(INVENTORY_MANAGER.MULTIVERSE)) {
            im.addItemFlags(ItemFlag.values());
        }
        result.setAmount(1);
        result.setItemMeta(im);
        inv.setItem(17, result);
        p.openInventory(inv);
    }

    public void showShapelessRecipe(Player player, String str) {
        ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(str);
        final List<ItemStack> ingredients = recipe.getIngredientList();
        plugin.getTrackerKeeper().getRecipeView().add(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(player, 27, "§4" + str + " recipe");
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getType().equals(Material.MAP)) {
                ItemMeta im = ingredients.get(i).getItemMeta();
                im.setDisplayName(getDisplayName(ingredients.get(i).getData().getData()));
                ingredients.get(i).setItemMeta(im);
            }
            if (ingredients.get(i).getType().equals(Material.MUSIC_DISC_STRAD)) {
                ItemMeta im = ingredients.get(i).getItemMeta();
                im.setDisplayName("Blank Storage Disk");
                ingredients.get(i).setItemMeta(im);
            }
            inv.setItem(i * 9, ingredients.get(i));
        }
        ItemStack result = recipe.getResult();
        ItemMeta im = result.getItemMeta();
        im.setDisplayName(str);
        if ((str.equals("Save Storage Disk") || str.equals("Preset Storage Disk") || str.equals("Biome Storage Disk") || str.equals("Player Storage Disk") || str.equals("Sonic Blaster")) && !plugin.getInvManager().equals(INVENTORY_MANAGER.MULTIVERSE)) {
            im.addItemFlags(ItemFlag.values());
        }
        result.setAmount(1);
        result.setItemMeta(im);
        inv.setItem(17, result);
        player.openInventory(inv);
    }

    public void showTARDISRecipe(Player player, String type) {
        plugin.getTrackerKeeper().getRecipeView().add(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(player, 27, "§4TARDIS " + type.toUpperCase(Locale.ENGLISH) + " seed recipe");
        // redstone torch
        ItemStack red = new ItemStack(Material.REDSTONE_TORCH, 1);
        // lapis block
        ItemStack lapis = new ItemStack(Material.LAPIS_BLOCK, 1);
        // interior wall
        ItemStack in_wall = new ItemStack(Material.ORANGE_WOOL, 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.setDisplayName("Interior walls");
        in_meta.setLore(Arrays.asList("Any valid Wall/Floor block"));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.setDisplayName("Interior floors");
        fl_meta.setLore(Arrays.asList("Any valid Wall/Floor block"));
        in_floor.setItemMeta(fl_meta);
        // seed block
        ItemStack block = new ItemStack(t.get(type.toUpperCase(Locale.ENGLISH)), 1);
        // tardis type
        ItemStack tardis = new ItemStack(t.get(type.toUpperCase(Locale.ENGLISH)), 1);
        ItemMeta seed = tardis.getItemMeta();
        // set display name
        seed.setDisplayName("§6TARDIS Seed Block");
        List<String> lore = new ArrayList<>();
        lore.add(type);
        lore.add("Walls: ORANGE_WOOL");
        lore.add("Floors: LIGHT_GRAY_WOOL");
        lore.add("Chameleon: FACTORY");
        seed.setLore(lore);
        tardis.setItemMeta(seed);
        inv.setItem(0, red);
        inv.setItem(9, lapis);
        inv.setItem(11, in_wall);
        inv.setItem(17, tardis);
        inv.setItem(18, block);
        inv.setItem(20, in_floor);
        player.openInventory(inv);
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
