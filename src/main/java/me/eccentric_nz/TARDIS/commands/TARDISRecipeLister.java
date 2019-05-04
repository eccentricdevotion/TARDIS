/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISRecipeLister {

    private final TARDIS plugin;
    private final CommandSender sender;
    private final LinkedHashMap<String, List<String>> options;

    TARDISRecipeLister(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
        options = createRecipeOptions();
    }

    public void list() {
        TARDISMessage.send(sender, "RECIPE_VIEW");
        sender.sendMessage(ChatColor.GRAY + "    Command argument" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Recipe Result");
        options.forEach((key, value) -> {
            sender.sendMessage(key);
            value.forEach((s) -> sender.sendMessage("    " + s));
        });
    }

    private LinkedHashMap<String, List<String>> createRecipeOptions() {
        LinkedHashMap<String, List<String>> recipe_options = new LinkedHashMap<>();
        List<String> items = new ArrayList<>();
        items.add(ChatColor.GREEN + "tardis [type]" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Seed Block");
        items.add(ChatColor.GREEN + "key" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Key");
        items.add(ChatColor.GREEN + "locator" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Locator");
        items.add(ChatColor.GREEN + "cell" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Artron Storage Cell");
        items.add(ChatColor.GREEN + "furnace" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Artron Furnace");
        items.add(ChatColor.GREEN + "filter" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Perception Filter");
        items.add(ChatColor.GREEN + "sonic" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Sonic Screwdriver");
        items.add(ChatColor.GREEN + "remote" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Stattenheim Remote");
        items.add(ChatColor.GREEN + "r-key" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Remote Key");
        items.add(ChatColor.GREEN + "reader" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Biome Reader");
        recipe_options.put("TARDIS Items", items);
        List<String> item_circuits = new ArrayList<>();
        item_circuits.add(ChatColor.RED + "l-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Locator Circuit");
        item_circuits.add(ChatColor.RED + "m-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Materialisation Circuit");
        item_circuits.add(ChatColor.RED + "generator" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Sonic Generator");
        item_circuits.add(ChatColor.RED + "oscillator" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Sonic Oscillator");
        item_circuits.add(ChatColor.RED + "p-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Perception Circuit");
        item_circuits.add(ChatColor.RED + "rift-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Rift Circuit");
        item_circuits.add(ChatColor.RED + "s-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Stattenheim Circuit");
        recipe_options.put("Item Circuits", item_circuits);
        List<String> sonic_circuits = new ArrayList<>();
        sonic_circuits.add(ChatColor.BLUE + "a-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Admin Circuit");
        sonic_circuits.add(ChatColor.BLUE + "bio-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Bio-scanner Circuit");
        sonic_circuits.add(ChatColor.BLUE + "d-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Diamond Circuit");
        sonic_circuits.add(ChatColor.BLUE + "e-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Emerald Circuit");
        sonic_circuits.add(ChatColor.BLUE + "r-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Redstone Circuit");
        sonic_circuits.add(ChatColor.BLUE + "painter" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Painter Circuit");
        sonic_circuits.add(ChatColor.BLUE + "ignite-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Ignite Circuit");
        sonic_circuits.add(ChatColor.BLUE + "arrow-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Pickup Arrows Circuit");
        recipe_options.put("Sonic Circuits", sonic_circuits);
        List<String> console_circuits = new ArrayList<>();
        console_circuits.add(ChatColor.LIGHT_PURPLE + "ars-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "ARS Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "c-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Chameleon Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "i-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Input Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "invisible" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Invisibility Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "m-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Materialisation Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "memory-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Memory Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "randomiser-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Randomiser Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "scanner-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Scanner Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "t-circuit" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Temporal Circuit");
        console_circuits.add(ChatColor.LIGHT_PURPLE + "telepathic" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Telepathic Circuit");
        recipe_options.put("Advanced Console Circuits", console_circuits);
        List<String> disks = new ArrayList<>();
        disks.add(ChatColor.AQUA + "blank" + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Blank Storage Disk");
        disks.add(ChatColor.AQUA + "biome-disk" + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Biome Storage Disk");
        disks.add(ChatColor.AQUA + "player-disk" + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Player Storage Disk");
        disks.add(ChatColor.AQUA + "preset-disk" + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Preset Storage Disk");
        disks.add(ChatColor.AQUA + "save-disk" + ChatColor.RESET + " - " + ChatColor.DARK_AQUA + "Save Storage Disk");
        recipe_options.put("Storage Disks", disks);
        List<String> food = new ArrayList<>();
        food.add(ChatColor.GRAY + "custard" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Bowl of Custard");
        food.add(ChatColor.GRAY + "fish-finger" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Fish Finger");
        food.add(ChatColor.GRAY + "jammy-dodger" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Jammy Dodger Biscuit");
        food.add(ChatColor.GRAY + "jelly-baby" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Jelly Baby");
        food.add(ChatColor.GRAY + "paper-bag" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Paper Bag");
        recipe_options.put("Food", food);
        List<String> accessories = new ArrayList<>();
        accessories.add(ChatColor.GREEN + "bow-tie" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Bow Tie");
        accessories.add(ChatColor.GREEN + "communicator" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Communicator");
        accessories.add(ChatColor.GREEN + "handles" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Handles Companion");
        accessories.add(ChatColor.GREEN + "glasses" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "3-D Glasses");
        accessories.add(ChatColor.GREEN + "acid-battery" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Acid Battery");
        accessories.add(ChatColor.GREEN + "rift-manipulator" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Rift Manipulator");
        accessories.add(ChatColor.GREEN + "rust" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Rust Plague Sword");
        if (plugin.getPM().isPluginEnabled("TARDISSonicBlaster")) {
            accessories.add(ChatColor.GREEN + "blaster" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Sonic Blaster");
            accessories.add(ChatColor.GREEN + "battery" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Blaster Battery");
            accessories.add(ChatColor.GREEN + "pad" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Landing Pad");
        }
        if (plugin.getPM().isPluginEnabled("TARDISVortexManipulator")) {
            accessories.add(ChatColor.GREEN + "vortex" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Vortex Manipulator");
        }
        accessories.add(ChatColor.GREEN + "wand" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Schematic Wand");
        accessories.add(ChatColor.GREEN + "watch" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Chameleon Arch Fob Watch");
        recipe_options.put("Accessories", accessories);
        return recipe_options;
    }
}
