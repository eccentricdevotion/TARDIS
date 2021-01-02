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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISRecipeLister {

    private final TARDIS plugin;
    private final CommandSender sender;
    private final TableGenerator tg;

    public TARDISRecipeLister(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
        if (TableGenerator.getSenderPrefs(sender)) {
            tg = new TableGeneratorCustomFont(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
        } else {
            tg = new TableGeneratorSmallChar(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
        }
    }

    public void list() {
        TARDISMessage.send(sender, "RECIPE_VIEW");
        for (String line : createRecipeOptions()) {
            sender.sendMessage(line);
        }
    }

    private List<String> createRecipeOptions() {
        tg.addRow(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Command argument", ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Recipe Result");
        tg.addRow();
        tg.addRow("TARDIS Items", "");
        tg.addRow(ChatColor.GREEN + "tardis [type]", ChatColor.DARK_GREEN + "TARDIS Seed Block");
        tg.addRow(ChatColor.GREEN + "key", ChatColor.DARK_GREEN + "TARDIS Key");
        tg.addRow(ChatColor.GREEN + "locator", ChatColor.DARK_GREEN + "TARDIS Locator");
        tg.addRow(ChatColor.GREEN + "cell", ChatColor.DARK_GREEN + "Artron Storage Cell");
        tg.addRow(ChatColor.GREEN + "furnace", ChatColor.DARK_GREEN + "TARDIS Artron Furnace");
        tg.addRow(ChatColor.GREEN + "filter", ChatColor.DARK_GREEN + "Perception Filter");
        tg.addRow(ChatColor.GREEN + "sonic", ChatColor.DARK_GREEN + "Sonic Screwdriver");
        tg.addRow(ChatColor.GREEN + "remote", ChatColor.DARK_GREEN + "Stattenheim Remote");
        tg.addRow(ChatColor.GREEN + "r-key", ChatColor.DARK_GREEN + "TARDIS Remote Key");
        tg.addRow(ChatColor.GREEN + "reader", ChatColor.DARK_GREEN + "TARDIS Biome Reader");
        tg.addRow();
        tg.addRow("Time Rotors", "");
        tg.addRow(ChatColor.GOLD + "rotor_early", ChatColor.YELLOW + "Early Time Rotor");
        tg.addRow(ChatColor.GOLD + "rotor_tenth", ChatColor.YELLOW + "Ninth & Tenth Time Rotor");
        tg.addRow(ChatColor.GOLD + "rotor_eleventh", ChatColor.YELLOW + "Eleventh Time Rotor");
        tg.addRow(ChatColor.GOLD + "rotor_twelfth", ChatColor.YELLOW + "Twelfth Time Rotor");
        tg.addRow();
        tg.addRow("Item Circuits", "");
        tg.addRow(ChatColor.RED + "l-circuit", ChatColor.DARK_RED + "Locator Circuit");
        tg.addRow(ChatColor.RED + "generator", ChatColor.DARK_RED + "Sonic Generator");
        tg.addRow(ChatColor.RED + "oscillator", ChatColor.DARK_RED + "Sonic Oscillator");
        tg.addRow(ChatColor.RED + "p-circuit", ChatColor.DARK_RED + "Perception Circuit");
        tg.addRow(ChatColor.RED + "rift-circuit", ChatColor.DARK_RED + "Rift Circuit");
        tg.addRow(ChatColor.RED + "s-circuit", ChatColor.DARK_RED + "Stattenheim Circuit");
        tg.addRow();
        tg.addRow("Sonic Circuits", "");
        tg.addRow(ChatColor.BLUE + "a-circuit", ChatColor.DARK_BLUE + "Admin Circuit");
        tg.addRow(ChatColor.BLUE + "bio-circuit", ChatColor.DARK_BLUE + "Bio-scanner Circuit");
        tg.addRow(ChatColor.BLUE + "d-circuit", ChatColor.DARK_BLUE + "Diamond Circuit");
        tg.addRow(ChatColor.BLUE + "e-circuit", ChatColor.DARK_BLUE + "Emerald Circuit");
        tg.addRow(ChatColor.BLUE + "r-circuit", ChatColor.DARK_BLUE + "Redstone Circuit");
        tg.addRow(ChatColor.BLUE + "painter", ChatColor.DARK_BLUE + "Painter Circuit");
        tg.addRow(ChatColor.BLUE + "ignite-circuit", ChatColor.DARK_BLUE + "Ignite Circuit");
        tg.addRow(ChatColor.BLUE + "arrow-circuit", ChatColor.DARK_BLUE + "Pickup Arrows Circuit");
        tg.addRow(ChatColor.BLUE + "k-circuit", ChatColor.DARK_BLUE + "Knockback Circuit");
        tg.addRow();
        tg.addRow("Console Circuits", "");
        tg.addRow(ChatColor.LIGHT_PURPLE + "ars-circuit", ChatColor.DARK_PURPLE + "ARS Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "c-circuit", ChatColor.DARK_PURPLE + "Chameleon Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "i-circuit", ChatColor.DARK_PURPLE + "Input Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "invisible", ChatColor.DARK_PURPLE + "Invisibility Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "m-circuit", ChatColor.DARK_PURPLE + "Materialisation Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "memory-circuit", ChatColor.DARK_PURPLE + "Memory Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "randomiser-circuit", ChatColor.DARK_PURPLE + "Randomiser Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "scanner-circuit", ChatColor.DARK_PURPLE + "Scanner Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "t-circuit", ChatColor.DARK_PURPLE + "Temporal Circuit");
        tg.addRow(ChatColor.LIGHT_PURPLE + "telepathic", ChatColor.DARK_PURPLE + "Telepathic Circuit");
        tg.addRow();
        tg.addRow("Storage Disks", "");
        tg.addRow(ChatColor.AQUA + "blank", ChatColor.DARK_AQUA + "Blank Storage Disk");
        tg.addRow(ChatColor.AQUA + "biome-disk", ChatColor.DARK_AQUA + "Biome Storage Disk");
        tg.addRow(ChatColor.AQUA + "player-disk", ChatColor.DARK_AQUA + "Player Storage Disk");
        tg.addRow(ChatColor.AQUA + "preset-disk", ChatColor.DARK_AQUA + "Preset Storage Disk");
        tg.addRow(ChatColor.AQUA + "save-disk", ChatColor.DARK_AQUA + "Save Storage Disk");
        tg.addRow(ChatColor.AQUA + "control", ChatColor.DARK_AQUA + "Authorised Control Disk");
        tg.addRow();
        tg.addRow("Food", "");
        tg.addRow(ChatColor.GRAY + "custard", ChatColor.DARK_GRAY + "Bowl of Custard");
        tg.addRow(ChatColor.GRAY + "fish-finger", ChatColor.DARK_GRAY + "Fish Finger");
        tg.addRow(ChatColor.GRAY + "jammy-dodger", ChatColor.DARK_GRAY + "Jammy Dodger Biscuit");
        tg.addRow(ChatColor.GRAY + "jelly-baby", ChatColor.DARK_GRAY + "Jelly Baby");
        tg.addRow(ChatColor.GRAY + "paper-bag", ChatColor.DARK_GRAY + "Paper Bag");
        tg.addRow();
        tg.addRow("Accessories", "");
        tg.addRow(ChatColor.GREEN + "bow-tie", ChatColor.DARK_GREEN + "Bow Tie");
        tg.addRow(ChatColor.GREEN + "communicator", ChatColor.DARK_GREEN + "TARDIS Communicator");
        tg.addRow(ChatColor.GREEN + "handles", ChatColor.DARK_GREEN + "Handles Companion");
        tg.addRow(ChatColor.GREEN + "glasses", ChatColor.DARK_GREEN + "3-D Glasses");
        tg.addRow(ChatColor.GREEN + "acid-battery", ChatColor.DARK_GREEN + "Acid Battery");
        tg.addRow(ChatColor.GREEN + "rift-manipulator", ChatColor.DARK_GREEN + "Rift Manipulator");
        tg.addRow(ChatColor.GREEN + "rust", ChatColor.DARK_GREEN + "Rust Plague Sword");
        if (plugin.getPM().isPluginEnabled("TARDISSonicBlaster")) {
            tg.addRow(ChatColor.GREEN + "blaster", ChatColor.DARK_GREEN + "Sonic Blaster");
            tg.addRow(ChatColor.GREEN + "battery", ChatColor.DARK_GREEN + "Blaster Battery");
            tg.addRow(ChatColor.GREEN + "pad", ChatColor.DARK_GREEN + "Landing Pad");
        }
        if (plugin.getPM().isPluginEnabled("TARDISVortexManipulator")) {
            tg.addRow(ChatColor.GREEN + "vortex", ChatColor.DARK_GREEN + "Vortex Manipulator");
        }
        tg.addRow(ChatColor.GREEN + "wand", ChatColor.DARK_GREEN + "TARDIS Schematic Wand");
        tg.addRow(ChatColor.GREEN + "watch", ChatColor.DARK_GREEN + "Chameleon Arch Fob Watch");
        return tg.generate(TableGeneratorSmallChar.Receiver.CLIENT, true, true);
    }
}
