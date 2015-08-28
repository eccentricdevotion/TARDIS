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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

/**
 *
 * @author eccentric_nz
 */
public class TARDISGiveCommand implements CommandExecutor {

    private final TARDIS plugin;
    private final int full;
    private final HashMap<String, String> items = new HashMap<String, String>();
    private final HashMap<String, Material> seeds = new HashMap<String, Material>();

    public TARDISGiveCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.full = this.plugin.getArtronConfig().getInt("full_charge");
        items.put("a-circuit", "Server Admin Circuit");
        items.put("ars-circuit", "TARDIS ARS Circuit");
        items.put("artron", "");
        items.put("bio-circuit", "Bio-scanner Circuit");
        items.put("biome-disk", "Biome Storage Disk");
        items.put("blank", "Blank Storage Disk");
        items.put("battery", "Blaster Battery");
        items.put("blaster", "Sonic Blaster");
        items.put("bow-tie", "Red Bow Tie");
        items.put("c-circuit", "TARDIS Chameleon Circuit");
        items.put("cell", "Artron Storage Cell");
        items.put("custard", "Bowl of Custard");
        items.put("d-circuit", "Diamond Disruptor Circuit");
        items.put("e-circuit", "Emerald Environment Circuit");
        items.put("filter", "Perception Filter");
        items.put("fish-finger", "Fish Finger");
        items.put("furnace", "TARDIS Artron Furnace");
        items.put("glasses", "3-D Glasses");
        items.put("i-circuit", "TARDIS Input Circuit");
        items.put("ignite-circuit", "Ignite Circuit");
        items.put("invisible", "TARDIS Invisibility Circuit");
        items.put("jammy-dodger", "Jammy Dodger");
        items.put("jelly-baby", "Orange Jelly Baby");
        items.put("key", "TARDIS Key");
        items.put("kit", "TARDIS Item Kit");
        items.put("l-circuit", "TARDIS Locator Circuit");
        items.put("locator", "TARDIS Locator");
        items.put("m-circuit", "TARDIS Materialisation Circuit");
        items.put("memory-circuit", "TARDIS Memory Circuit");
        items.put("oscillator", "Sonic Oscillator");
        items.put("p-circuit", "Perception Circuit");
        items.put("pad", "Landing Pad");
        items.put("painter", "Painter Circuit");
        items.put("player-disk", "Player Storage Disk");
        items.put("preset-disk", "Preset Storage Disk");
        items.put("r-circuit", "Redstone Activator Circuit");
        items.put("r-key", "TARDIS Remote Key");
        items.put("randomiser-circuit", "TARDIS Randomiser Circuit");
        items.put("remote", "Stattenheim Remote");
        items.put("s-circuit", "TARDIS Stattenheim Circuit");
        items.put("save-disk", "Save Storage Disk");
        items.put("scanner-circuit", "TARDIS Scanner Circuit");
        items.put("seed", "");
        items.put("sonic", "Sonic Screwdriver");
        items.put("t-circuit", "TARDIS Temporal Circuit");
        items.put("tachyon", "");
        items.put("vortex", "Vortex Manipulator");
        items.put("watch", "Fob Watch");
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisgive then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisgive")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                    new TARDISGiveLister(plugin, sender).list();
                    return true;
                }
                if (args.length < 3) {
                    TARDISMessage.send(sender, "TOO_FEW_ARGS", " /tardisgive [player] [item] [amount]");
                    return true;
                }
                String item = args[1].toLowerCase();
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
                    for (String k : plugin.getKitsConfig().getStringList("kits." + args[2])) {
                        this.giveItem(k, p);
                    }
                    TARDISMessage.send(p, "GIVE_KIT", sender.getName(), args[2]);
                    return true;
                }
                if (item.equals("seed")) {
                    return this.giveSeed(sender, args[0], args[2].toUpperCase());
                }
                if (item.equals("tachyon")) {
                    return this.giveTachyon(sender, args[0], args[2]);
                }
                int amount;
                if (args[2].equals("full")) {
                    amount = full;
                } else if (args[2].equals("empty")) {
                    amount = 0;
                } else {
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe) {
                        TARDISMessage.send(sender, "ARG_GIVE");
                        return true;
                    }
                }
                if (item.equals("artron")) {
                    if (plugin.getServer().getOfflinePlayer(args[0]) == null) {
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    return this.giveArtron(sender, args[0], amount);
                } else {
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("cell") && args.length == 4 && args[3].equalsIgnoreCase("full")) {
                        return this.giveFullCell(sender, amount, p);
                    } else {
                        return this.giveItem(sender, item, amount, p);
                    }
                }
            } else {
                TARDISMessage.send(sender, "NO_PERMS");
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
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
        if (item.equals("save-disk") || item.equals("preset-disk") || item.equals("biome-disk") || item.equals("player-disk") || item.equals("custard") || item.equals("jelly-baby")) {
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
        result.setAmount(amount);
        player.getInventory().addItem(result);
        player.updateInventory();
        TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), amount + " " + item_to_give);
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean giveItem(String item, Player player) {
        ItemStack result;
        if (plugin.getIncomposita().getShapelessRecipes().containsKey(item)) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item);
            result = recipe.getResult();
        }
        result.setAmount(1);
        player.getInventory().addItem(result);
        player.updateInventory();
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean giveArtron(CommandSender sender, String player, int amount) {
        // Look up this player's UUID
        UUID uuid = plugin.getServer().getOfflinePlayer(player).getUniqueId();
        if (uuid == null) {
            uuid = plugin.getGeneralKeeper().getUUIDCache().getIdOptimistic(player);
            plugin.getGeneralKeeper().getUUIDCache().getId(player);
        }
        if (uuid != null) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                int id = rs.getTardis_id();
                int level = rs.getArtron_level();
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
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("artron_level", set_level);
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
                qf.doUpdate("tardis", set, wheret);
                sender.sendMessage(plugin.getPluginName() + player + "'s Artron Energy Level was set to " + set_level);
            }
            return true;
        } else {
            TARDISMessage.send(sender, "UUID_NOT_FOUND", player);
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    private boolean giveSeed(CommandSender sender, String p, String type) {
        if (plugin.getServer().getPlayer(p) == null) {
            TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
            return true;
        }
        Player player = plugin.getServer().getPlayer(p);
        ItemStack is;
        if (CONSOLES.getByNames().containsKey(type)) {
            SCHEMATIC schm = CONSOLES.getByNames().get(type);
            is = new ItemStack(schm.getSeedMaterial(), 1);
            // set display name
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("§6TARDIS Seed Block");
            List<String> lore = new ArrayList<String>();
            lore.add(type);
            lore.add("Walls: ORANGE_WOOL");
            lore.add("Floors: LIGHT_GRAY_WOOL");
            lore.add("Chameleon block: BLUE WOOL");
            lore.add("Lamp: REDSTONE_LAMP_OFF");
            im.setLore(lore);
            is.setItemMeta(im);
            player.getInventory().addItem(is);
            player.updateInventory();
            TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), "a " + type + " seed block");
        }
        return true;
    }

    @SuppressWarnings("deprecation")
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
        if (uuid == null) {
            uuid = plugin.getGeneralKeeper().getUUIDCache().getIdOptimistic(player);
            plugin.getGeneralKeeper().getUUIDCache().getId(player);
        }
        if (uuid != null) {
            plugin.getServer().dispatchCommand(sender, "vmg " + uuid.toString() + " " + amount);
            return true;
        } else {
            TARDISMessage.send(sender, "UUID_NOT_FOUND", player);
            return true;
        }
    }

    @SuppressWarnings("deprecation")
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
        if (!plugin.getPM().isPluginEnabled("Multiverse-Inventories")) {
            im.addItemFlags(ItemFlag.values());
        }
        result.setItemMeta(im);
        player.getInventory().addItem(result);
        player.updateInventory();
        TARDISMessage.send(player, "GIVE_ITEM", sender.getName(), amount + " Full Artron Storage Cell");
        return true;
    }
}
