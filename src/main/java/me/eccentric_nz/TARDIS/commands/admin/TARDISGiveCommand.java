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
import me.eccentric_nz.TARDIS.custommodeldata.TARDISSeedModel;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.messaging.TARDISGiveLister;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISGiveCommand implements CommandExecutor {

    private final TARDIS plugin;
    private final int full;
    private final HashMap<String, String> items = new HashMap<>();

    public TARDISGiveCommand(TARDIS plugin) {
        this.plugin = plugin;
        full = this.plugin.getArtronConfig().getInt("full_charge");
        items.put("a-circuit", "Server Admin Circuit");
        items.put("acid-battery", "Acid Battery");
        items.put("arrow-circuit", "Pickup Arrows Circuit");
        items.put("ars-circuit", "TARDIS ARS Circuit");
        items.put("artron", "");
        items.put("battery", "Blaster Battery");
        items.put("bio-circuit", "Bio-scanner Circuit");
        items.put("biome-disk", "Biome Storage Disk");
        items.put("blank", "Blank Storage Disk");
        items.put("blaster", "Sonic Blaster");
        items.put("bow-tie", "Red Bow Tie");
        items.put("c-circuit", "TARDIS Chameleon Circuit");
        items.put("cell", "Artron Storage Cell");
        items.put("communicator", "TARDIS Communicator");
        items.put("control", "Authorised Control Disk");
        items.put("custard", "Bowl of Custard");
        items.put("d-circuit", "Diamond Disruptor Circuit");
        items.put("e-circuit", "Emerald Environment Circuit");
        items.put("filter", "Perception Filter");
        items.put("fish-finger", "Fish Finger");
        items.put("furnace", "TARDIS Artron Furnace");
        items.put("generator", "Sonic Generator");
        items.put("glasses", "3-D Glasses");
        items.put("handles", "Handles");
        items.put("i-circuit", "TARDIS Input Circuit");
        items.put("ignite-circuit", "Ignite Circuit");
        items.put("invisible", "TARDIS Invisibility Circuit");
        items.put("jammy-dodger", "Jammy Dodger");
        items.put("jelly-baby", "Orange Jelly Baby");
        items.put("k-circuit", "Knockback Circuit");
        items.put("key", "TARDIS Key");
        items.put("keyboard", "TARDIS Keyboard Editor");
        items.put("kit", "TARDIS Item Kit");
        items.put("l-circuit", "TARDIS Locator Circuit");
        items.put("locator", "TARDIS Locator");
        items.put("m-circuit", "TARDIS Materialisation Circuit");
        items.put("memory-circuit", "TARDIS Memory Circuit");
        items.put("mushroom", "");
        items.put("oscillator", "Sonic Oscillator");
        items.put("p-circuit", "Perception Circuit");
        items.put("pad", "Landing Pad");
        items.put("painter", "Painter Circuit");
        items.put("paper-bag", "Paper Bag");
        items.put("player-disk", "Player Storage Disk");
        items.put("preset-disk", "Preset Storage Disk");
        items.put("r-circuit", "Redstone Activator Circuit");
        items.put("r-key", "TARDIS Remote Key");
        items.put("randomiser-circuit", "TARDIS Randomiser Circuit");
        items.put("reader", "TARDIS Biome Reader");
        items.put("remote", "Stattenheim Remote");
        items.put("rift-circuit", "Rift Circuit");
        items.put("rift-manipulator", "Rift Manipulator");
        items.put("rust", "Rust Plague Sword");
        items.put("rotor_early", "Time Rotor Early");
        items.put("rotor_tenth", "Time Rotor Tenth");
        items.put("rotor_eleventh", "Time Rotor Eleventh");
        items.put("rotor_twelfth", "Time Rotor Twelfth");
        items.put("s-circuit", "TARDIS Stattenheim Circuit");
        items.put("save-disk", "Save Storage Disk");
        items.put("scanner-circuit", "TARDIS Scanner Circuit");
        items.put("seed", "");
        items.put("sonic", "Sonic Screwdriver");
        items.put("t-circuit", "TARDIS Temporal Circuit");
        items.put("tachyon", "");
        items.put("telepathic", "TARDIS Telepathic Circuit");
        items.put("vortex", "Vortex Manipulator");
        items.put("wand", "TARDIS Schematic Wand");
        items.put("watch", "Fob Watch");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisgive then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisgive")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                    new TARDISGiveLister(plugin, sender).list();
                    return true;
                }
                if (args.length < 3) {
                    TARDISMessage.send(sender, "TOO_FEW_ARGS");
                    TARDISMessage.message(sender, "/tardisgive [player] [item] [amount]");
                    return true;
                }
                String item = args[1].toLowerCase(Locale.ENGLISH);
                if (!items.containsKey(item)) {
                    new TARDISGiveLister(plugin, sender).list();
                    return true;
                }
                if (item.equals("kit")) {
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    if (!plugin.getKitsConfig().contains("kits." + args[2])) {
                        TARDISMessage.send(sender, "ARG_KIT");
                        return true;
                    }
                    plugin.getKitsConfig().getStringList("kits." + args[2]).forEach((k) -> giveItem(k, p));
                    TARDISMessage.send(p, "GIVE_KIT", sender.getName(), args[2]);
                    return true;
                }
                if (item.equals("seed")) {
                    String seed = args[2].toUpperCase(Locale.ENGLISH);
                    if (CONSOLES.getBY_NAMES().containsKey(seed) && !seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
                        if (args.length > 3 && args[3].equalsIgnoreCase("knowledge")) {
                            Player sp = plugin.getServer().getPlayer(args[0]);
                            if (sp == null) { // player must be online
                                TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                                return true;
                            }
                            return giveKnowledgeBook(sender, seed.toLowerCase() + "_seed", sp);
                        } else {
                            return giveSeed(sender, args);
                        }
                    } else {
                        TARDISMessage.send(sender, "ARG_SEED");
                        return true;
                    }
                }
                if (item.equals("tachyon")) {
                    return giveTachyon(sender, args[0], args[2]);
                }
                int amount;
                switch (args[2]) {
                    case "full":
                        amount = full;
                        break;
                    case "empty":
                        amount = 0;
                        break;
                    case "knowledge":
                        amount = 1;
                        break;
                    default:
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException nfe) {
                            TARDISMessage.send(sender, "ARG_GIVE");
                            return true;
                        }
                        break;
                }
                if (item.equals("mushroom")) {
                    if (args.length < 4) {
                        TARDISMessage.send(sender, "TOO_FEW_ARGS");
                        TARDISMessage.message(sender, "/tardisgive [player] mushroom [amount] [type]");
                        return true;
                    }
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    ItemStack mushroom = new TARDISMushroomCommand(plugin).getStack(args);
                    p.getInventory().addItem(mushroom);
                    p.updateInventory();
                    TARDISMessage.send(p, "GIVE_ITEM", sender.getName(), amount + " " + args[3]);
                    return true;
                }
                if (item.equals("artron")) {
                    if (plugin.getServer().getOfflinePlayer(args[0]) == null) {
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    return giveArtron(sender, args[0], amount);
                } else {
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("cell") && args.length == 4 && args[3].equalsIgnoreCase("full")) {
                        return giveFullCell(sender, amount, p);
                    } else if (args[2].equals("knowledge")) {
                        return giveKnowledgeBook(sender, item, p);
                    } else if (!args[2].endsWith("_seed")) {
                        return giveItem(sender, item, amount, p);
                    }
                }
            } else {
                TARDISMessage.send(sender, "NO_PERMS");
                return true;
            }
        }
        return false;
    }

    private boolean giveItem(CommandSender sender, String item, int amount, Player player) {
        if (amount > 64) {
            TARDISMessage.send(sender, "ARG_MAX");
            return true;
        }
        if ((item.equals("battery") || item.equals("blaster") || item.equals("pad")) && !plugin.getPM().isPluginEnabled("TARDISSonicBlaster")) {
            TARDISMessage.send(sender, "RECIPE_BLASTER");
            return true;
        }
        if (item.equals("vortex") && !plugin.getPM().isPluginEnabled("TARDISVortexManipulator")) {
            TARDISMessage.send(sender, "RECIPE_VORTEX");
            return true;
        }
        if (item.equals("vortex")) {
            TARDISMessage.send(sender, "VORTEX_CMD");
        }
        String item_to_give = items.get(item);
        ItemStack result;
        if (item.equals("save-disk") || item.equals("preset-disk") || item.equals("biome-disk") || item.equals("player-disk") || item.equals("custard") || item.equals("jelly-baby") || item.equals("wand")) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item_to_give);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item_to_give);
            result = recipe.getResult();
        }
        if (item.equals("invisible")) {
            // set the second line of lore
            ItemMeta im = result.getItemMeta();
            List<String> lore = im.getLore();
            String uses = (plugin.getConfig().getString("circuits.uses.invisibility").equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.invisibility");
            lore.set(1, uses);
            im.setLore(lore);
            result.setItemMeta(im);
        }
        if (item.equals("blank") || item.equals("save-disk") || item.equals("preset-disk") || item.equals("biome-disk") || item.equals("player-disk") || item.equals("blaster") || item.equals("control")) {
            ItemMeta im = result.getItemMeta();
            im.addItemFlags(ItemFlag.values());
            result.setItemMeta(im);
        }
        if (item.equals("key") || item.equals("control")) {
            ItemMeta im = result.getItemMeta();
            im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
            List<String> lore = im.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
            String what = item.equals("key") ? "key" : "disk";
            lore.add(format + "This " + what + " belongs to");
            lore.add(format + player.getName());
            im.setLore(lore);
            result.setItemMeta(im);
        }
        result.setAmount(amount);
        player.getInventory().addItem(result);
        player.updateInventory();
        TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), amount + " " + item_to_give);
        return true;
    }

    private void giveItem(String item, Player player) {
        ItemStack result;
        if (plugin.getIncomposita().getShapelessRecipes().containsKey(item)) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item);
            result = recipe.getResult();
            if (result.hasItemMeta()) {
                ItemMeta im = result.getItemMeta();
                if (im.hasDisplayName() && (im.getDisplayName().contains("Key") || im.getDisplayName().contains("Authorised Control Disk"))) {
                    im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                    if (im.hasLore()) {
                        List<String> lore = im.getLore();
                        String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
                        String what = im.getDisplayName().contains("Key") ? "key" : "disk";
                        lore.add(format + "This " + what + " belongs to");
                        lore.add(format + player.getName());
                        im.setLore(lore);
                    }
                    result.setItemMeta(im);
                }
            }
        }
        result.setAmount(1);
        player.getInventory().addItem(result);
        player.updateInventory();
    }

    private boolean giveArtron(CommandSender sender, String player, int amount) {
        // Look up this player's UUID
        UUID uuid = plugin.getServer().getOfflinePlayer(player).getUniqueId();
        if (uuid != null) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardis_id();
                int level = tardis.getArtron_level();
                int set_level;
                if (amount == 0) {
                    set_level = 0;
                } else {
                    // always fill to full and no more
                    if (level >= full && amount > 0) {
                        TARDISMessage.send(sender, "GIVE_FULL", player);
                        return true;
                    }
                    if ((full - level) < amount) {
                        set_level = full;
                    } else {
                        set_level = level + amount;
                    }
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("artron_level", set_level);
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                sender.sendMessage(plugin.getPluginName() + player + "'s Artron Energy Level was set to " + set_level);
            }
        } else {
            TARDISMessage.send(sender, "UUID_NOT_FOUND", player);
        }
        return true;
    }

    private boolean giveSeed(CommandSender sender, String[] args) {
        Player player;
        if (args[0].equals("@s")) {
            player = (Player) sender;
        } else {
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                return true;
            }
        }
        String type = args[2].toUpperCase(Locale.ENGLISH);
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GRAY_WOOL";
        if (args.length > 4) {
            try {
                wall = Material.valueOf(args[3].toUpperCase()).toString();
                floor = Material.valueOf(args[4].toUpperCase()).toString();
            } catch (IllegalArgumentException e) {
                TARDISMessage.send(sender, "SEED_MAT_NOT_VALID");
                return true;
            }
        }
        ItemStack is;
        if (CONSOLES.getBY_NAMES().containsKey(type)) {
            int model = TARDISSeedModel.modelByString(type);
            if (CONSOLES.getBY_NAMES().get(type).isCustom()) {
                is = new ItemStack(Material.MUSHROOM_STEM, 1);
            } else if (type.equalsIgnoreCase("ROTOR")) {
                is = new ItemStack(Material.MUSHROOM_STEM, 1);
            } else {
                is = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
            }
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000000 + model);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
            // set display name
            im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
            List<String> lore = new ArrayList<>();
            lore.add(type);
            lore.add("Walls: " + wall);
            lore.add("Floors: " + floor);
            lore.add("Chameleon: FACTORY");
            im.setLore(lore);
            is.setItemMeta(im);
            player.getInventory().addItem(is);
            player.updateInventory();
            TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), "a " + type + " seed block");
        }
        return true;
    }

    private boolean giveTachyon(CommandSender sender, String player, String amount) {
        if (!plugin.getPM().isPluginEnabled("TARDISVortexManipulator")) {
            TARDISMessage.send(sender, "RECIPE_VORTEX");
            return true;
        }
        if (plugin.getServer().getOfflinePlayer(player) == null) {
            TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
            return true;
        }
        // Look up this player's UUID
        UUID uuid = plugin.getServer().getOfflinePlayer(player).getUniqueId();
        if (uuid != null) {
            plugin.getServer().dispatchCommand(sender, "vmg " + uuid.toString() + " " + amount);
        } else {
            TARDISMessage.send(sender, "UUID_NOT_FOUND", player);
        }
        return true;
    }

    private boolean giveFullCell(CommandSender sender, int amount, Player player) {
        if (amount > 64) {
            TARDISMessage.send(sender, "ARG_MAX");
            return true;
        }
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell");
        ItemStack result = recipe.getResult();
        result.setAmount(amount);
        // add lore and enchantment
        ItemMeta im = result.getItemMeta();
        List<String> lore = im.getLore();
        int max = plugin.getArtronConfig().getInt("full_charge");
        lore.set(1, "" + max);
        im.setLore(lore);
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.values());
        result.setItemMeta(im);
        player.getInventory().addItem(result);
        player.updateInventory();
        TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), amount + " Full Artron Storage Cell");
        return true;
    }

    private boolean giveKnowledgeBook(CommandSender sender, String item, Player player) {
        String item_to_give = (item.endsWith("_seed")) ? item : items.get(item);
        ItemStack book = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        KnowledgeBookMeta kbm = (KnowledgeBookMeta) book.getItemMeta();
        String message = item_to_give;
        switch (item) {
            case "bow-tie":
                List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                colours.forEach((bt) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                    kbm.addRecipe(nsk);
                });
                message = "Bow Ties";
                break;
            case "jelly-baby":
                List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                flavours.forEach((jelly) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                    kbm.addRecipe(nsk);
                });
                message = "Jelly Babies";
                break;
            default:
                NamespacedKey nsk = new NamespacedKey(plugin, item_to_give.replace(" ", "_").toLowerCase(Locale.ENGLISH));
                kbm.addRecipe(nsk);
        }
        book.setItemMeta(kbm);
        player.getInventory().addItem(book);
        player.updateInventory();
        TARDISMessage.send(player, "GIVE_KNOWLEDGE", sender.getName(), message);
        return true;
    }
}
