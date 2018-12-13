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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISRecipesUpdater {

    private final TARDIS plugin;
    private final FileConfiguration recipes_config;
    private final HashMap<String, String> flavours = new HashMap<>();
    private final HashMap<String, String> colours = new HashMap<>();
    private final HashMap<String, Integer> damage = new HashMap<>();

    public TARDISRecipesUpdater(TARDIS plugin, FileConfiguration recipes_config) {
        this.plugin = plugin;
        this.recipes_config = recipes_config;
        flavours.put("Licorice", "INK_SAC");
        flavours.put("Raspberry", "ROSE_RED");
        flavours.put("Apple", "CACTUS_GREEN");
        flavours.put("Cappuccino", "COCOA_BEANS");
        flavours.put("Blueberry", "LAPIS_LAZULI");
        flavours.put("Grape", "PURPLE_DYE");
        flavours.put("Island Punch", "CYAN_DYE");
        flavours.put("Vodka", "LIGHT_GRAY_DYE");
        flavours.put("Earl Grey", "GRAY_DYE");
        flavours.put("Strawberry", "PINK_DYE");
        flavours.put("Lime", "LIME_DYE");
        flavours.put("Lemon", "DANDELION_YELLOW");
        flavours.put("Bubblegum", "LIGHT_BLUE_DYE");
        flavours.put("Watermelon", "MAGENTA_DYE");
        flavours.put("Orange", "ORANGE_DYE");
        flavours.put("Vanilla", "BONE_MEAL");
        colours.put("White", "WHITE_WOOL");
        colours.put("Orange", "ORANGE_WOOL");
        colours.put("Magenta", "MAGENTA_WOOL");
        colours.put("Light Blue", "LIGHT_BLUE_WOOL");
        colours.put("Yellow", "YELLOW_WOOL");
        colours.put("Lime", "LIME_WOOL");
        colours.put("Pink", "PINK_WOOL");
        colours.put("Grey", "GRAY_WOOL");
        colours.put("Light Grey", "LIGHT_GRAY_WOOL");
        colours.put("Cyan", "CYAN_WOOL");
        colours.put("Purple", "PURPLE_WOOL");
        colours.put("Blue", "BLUE_WOOL");
        colours.put("Brown", "BROWN_WOOL");
        colours.put("Green", "GREEN_WOOL");
        colours.put("Red", "RED_WOOL");
        colours.put("Black", "BLACK_WOOL");
        damage.put("shaped.TARDIS ARS Circuit.lore", 20);
        damage.put("shaped.TARDIS Chameleon Circuit.lore", 25);
        damage.put("shaped.TARDIS Input Circuit.lore", 50);
        damage.put("shaped.TARDIS Materialisation Circuit.lore", 50);
        damage.put("shaped.TARDIS Memory Circuit.lore", 20);
        damage.put("shaped.TARDIS Randomiser Circuit.lore", 50);
        damage.put("shaped.TARDIS Scanner Circuit.lore", 20);
        damage.put("shaped.TARDIS Temporal Circuit.lore", 20);
    }

    public void addRecipes() {
        int i = 0;
        // update for 1.13
        if (recipes_config.getString("shaped.TARDIS Locator.easy_ingredients.C").contains(":")) {
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.C", "CYAN_STAINED_GLASS");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.M", "MAGENTA_STAINED_GLASS");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.C", "CYAN_STAINED_GLASS");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.M", "MAGENTA_STAINED_GLASS");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.R", "COMPARATOR");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.Acid Battery.result", "NETHER_BRICK");
            recipes_config.set("shaped.Artron Storage Cell.easy_ingredients.L", "LIME_STAINED_GLASS");
            recipes_config.set("shaped.Artron Storage Cell.hard_ingredients.L", "LIME_STAINED_GLASS");
            recipes_config.set("shaped.Bio-scanner Circuit.hard_ingredients.R", "REPEATER");
            recipes_config.set("shaped.Bio-scanner Circuit.result", "FILLED_MAP:1969");
            recipes_config.set("shaped.Blank Storage Disk.result", "MUSIC_DISC_STRAD");
            recipes_config.set("shaped.Diamond Disruptor Circuit.result", "FILLED_MAP:1971");
            recipes_config.set("shaped.Emerald Environment Circuit.easy_ingredients.L", "OAK_LEAVES");
            recipes_config.set("shaped.Emerald Environment Circuit.hard_ingredients.L", "OAK_LEAVES");
            recipes_config.set("shaped.Emerald Environment Circuit.result", "FILLED_MAP:1972");
            recipes_config.set("shaped.Fish Finger.easy_ingredients.F", "COD");
            recipes_config.set("shaped.Fish Finger.hard_ingredients.F", "COD");
            recipes_config.set("shaped.Fish Finger.result", "COOKED_COD");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.C", "FILLED_MAP:1966");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.W", "CLOCK");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.C", "FILLED_MAP:1966");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.W", "CLOCK");
            recipes_config.set("shaped.Fob Watch.result", "CLOCK");
            recipes_config.set("shaped.Handles.easy_ingredients.S", "SKELETON_SKULL");
            recipes_config.set("shaped.Handles.hard_ingredients.S", "SKELETON_SKULL");
            recipes_config.set("shaped.Handles.result", "BIRCH_BUTTON");
            recipes_config.set("shaped.Ignite Circuit.result", "FILLED_MAP:1982");
            recipes_config.set("shaped.Painter Circuit.result", "FILLED_MAP:1979");
            recipes_config.set("shaped.Perception Circuit.easy_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.Perception Circuit.easy_ingredients.D", "REPEATER");
            recipes_config.set("shaped.Perception Circuit.easy_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.Perception Circuit.hard_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.Perception Circuit.hard_ingredients.D", "REPEATER");
            recipes_config.set("shaped.Perception Circuit.hard_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.Perception Circuit.result", "FILLED_MAP:1978");
            recipes_config.set("shaped.Perception Filter.easy_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.Perception Filter.hard_ingredients.C", "FILLED_MAP:1978");
            recipes_config.set("shaped.Redstone Activator Circuit.easy_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.Redstone Activator Circuit.hard_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.Redstone Activator Circuit.hard_ingredients.R", "REPEATER");
            recipes_config.set("shaped.Redstone Activator Circuit.result", "FILLED_MAP:1970");
            recipes_config.set("shaped.Rift Circuit.result", "FILLED_MAP:1983");
            recipes_config.set("shaped.Rift Manipulator.easy_ingredients.A", "NETHER_BRICK");
            recipes_config.set("shaped.Rift Manipulator.easy_ingredients.C", "FILLED_MAP:1983");
            recipes_config.set("shaped.Rift Manipulator.hard_ingredients.A", "NETHER_BRICK");
            recipes_config.set("shaped.Rift Manipulator.hard_ingredients.C", "FILLED_MAP:1983");
            recipes_config.set("shaped.Server Admin Circuit.easy_ingredients.O", "FILLED_MAP:1967");
            recipes_config.set("shaped.Server Admin Circuit.hard_ingredients.O", "FILLED_MAP:1967");
            recipes_config.set("shaped.Server Admin Circuit.result", "FILLED_MAP:1968");
            recipes_config.set("shaped.Sonic Generator.easy_ingredients.F", "FLOWER_POT");
            recipes_config.set("shaped.Sonic Generator.hard_ingredients.F", "FLOWER_POT");
            recipes_config.set("shaped.Sonic Generator.result", "FLOWER_POT");
            recipes_config.set("shaped.Sonic Oscillator.result", "FILLED_MAP:1967");
            recipes_config.set("shaped.Sonic Screwdriver.hard_ingredients.O", "FILLED_MAP:1967");
            recipes_config.set("shaped.Stattenheim Remote.hard_ingredients.L", "FILLED_MAP:1963");
            recipes_config.set("shaped.TARDIS ARS Circuit.easy_ingredients.S", "WHEAT_SEEDS");
            recipes_config.set("shaped.TARDIS ARS Circuit.easy_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.TARDIS ARS Circuit.hard_ingredients.D", "REPEATER");
            recipes_config.set("shaped.TARDIS ARS Circuit.hard_ingredients.P", "PISTON");
            recipes_config.set("shaped.TARDIS ARS Circuit.hard_ingredients.S", "WHEAT_SEEDS");
            recipes_config.set("shaped.TARDIS ARS Circuit.hard_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.TARDIS ARS Circuit.result", "FILLED_MAP:1973");
            recipes_config.set("shaped.TARDIS Biome Reader.easy_ingredients.C", "FILLED_MAP:1972");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.C", "FILLED_MAP:1972");
            recipes_config.set("shaped.TARDIS Biome Reader.result", "BRICK");
            recipes_config.set("shaped.TARDIS Chameleon Circuit.hard_ingredients.D", "REPEATER");
            recipes_config.set("shaped.TARDIS Chameleon Circuit.hard_ingredients.M", "FILLED_MAP:1964");
            recipes_config.set("shaped.TARDIS Chameleon Circuit.result", "FILLED_MAP:1966");
            recipes_config.set("shaped.TARDIS Input Circuit.result", "FILLED_MAP:1976");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.P", "FILLED_MAP:1978");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.P", "FILLED_MAP:1978");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.result", "FILLED_MAP:1981");
            recipes_config.set("shaped.TARDIS Key.hard_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.TARDIS Locator Circuit.easy_ingredients.D", "REPEATER");
            recipes_config.set("shaped.TARDIS Locator Circuit.hard_ingredients.D", "REPEATER");
            recipes_config.set("shaped.TARDIS Locator Circuit.result", "FILLED_MAP:1965");
            recipes_config.set("shaped.TARDIS Locator.easy_ingredients.C", "RED_WOOL");
            recipes_config.set("shaped.TARDIS Locator.hard_ingredients.C", "FILLED_MAP:1965");
            recipes_config.set("shaped.TARDIS Materialisation Circuit.hard_ingredients.D", "REPEATER");
            recipes_config.set("shaped.TARDIS Materialisation Circuit.hard_ingredients.I", "ENDER_EYE");
            recipes_config.set("shaped.TARDIS Materialisation Circuit.result", "FILLED_MAP:1964");
            recipes_config.set("shaped.TARDIS Memory Circuit.easy_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.TARDIS Memory Circuit.hard_ingredients.R", "RED_SAND");
            recipes_config.set("shaped.TARDIS Memory Circuit.hard_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.TARDIS Memory Circuit.result", "FILLED_MAP:1975");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.E", "END_STONE");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.E", "END_STONE");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.result", "FILLED_MAP:1980");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.T", "FILLED_MAP:1964");
            recipes_config.set("shaped.TARDIS Scanner Circuit.easy_ingredients.G", "GLASS_PANE");
            recipes_config.set("shaped.TARDIS Scanner Circuit.result", "FILLED_MAP:1977");
            recipes_config.set("shaped.TARDIS Stattenheim Circuit.easy_ingredients.L", "FILLED_MAP:1965");
            recipes_config.set("shaped.TARDIS Stattenheim Circuit.easy_ingredients.M", "FILLED_MAP:1964");
            recipes_config.set("shaped.TARDIS Stattenheim Circuit.hard_ingredients.L", "FILLED_MAP:1965");
            recipes_config.set("shaped.TARDIS Stattenheim Circuit.hard_ingredients.M", "FILLED_MAP:1964");
            recipes_config.set("shaped.TARDIS Stattenheim Circuit.result", "FILLED_MAP:1963");
            recipes_config.set("shaped.TARDIS Temporal Circuit.easy_ingredients.W", "CLOCK");
            recipes_config.set("shaped.TARDIS Temporal Circuit.hard_ingredients.W", "CLOCK");
            recipes_config.set("shaped.TARDIS Temporal Circuit.result", "FILLED_MAP:1974");
            recipes_config.set("shapeless.Admin Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1968");
            recipes_config.set("shapeless.Bio-scanner Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1969");
            recipes_config.set("shapeless.Biome Storage Disk.recipe", "MUSIC_DISC_STRAD,EMERALD");
            recipes_config.set("shapeless.Biome Storage Disk.result", "MUSIC_DISC_CAT");
            recipes_config.set("shapeless.Diamond Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1971");
            recipes_config.set("shapeless.Emerald Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1972");
            recipes_config.set("shapeless.Ignite Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1982");
            recipes_config.set("shapeless.Painter Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1979");
            recipes_config.set("shapeless.Player Storage Disk.recipe", "MUSIC_DISC_STRAD,LAPIS_BLOCK");
            recipes_config.set("shapeless.Player Storage Disk.result", "MUSIC_DISC_WAIT");
            recipes_config.set("shapeless.Preset Storage Disk.recipe", "MUSIC_DISC_STRAD,FILLED_MAP:1966");
            recipes_config.set("shapeless.Preset Storage Disk.result", "MUSIC_DISC_MALL");
            recipes_config.set("shapeless.Redstone Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1970");
            recipes_config.set("shapeless.Save Storage Disk.recipe", "MUSIC_DISC_STRAD,REDSTONE");
            recipes_config.set("shapeless.Save Storage Disk.result", "MUSIC_DISC_CHIRP");
            if (recipes_config.getString("shapeless.Island Punch Jelly Baby.lore").equals("Orange")) {
                recipes_config.set("shapeless.Island Punch Jelly Baby.lore", "");
            }
            if (recipes_config.getString("shapeless.Raspberry Jelly Baby.recipe").contains("RED_ROSE")) {
                recipes_config.set("shapeless.Raspberry Jelly Baby.recipe", "SUGAR,SLIME_BALL,ROSE_RED");
            }
            recipes_config.set("furnace.Nuclear Wool.recipe", "WHITE_WOOL");
            recipes_config.set("furnace.Nuclear Wool.result", "BLACK_WOOL");
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Updated recipes for Minecraft 1.13+");
        }
        if (recipes_config.getString("shaped.Stattenheim Remote.easy_ingredients.L").contains(":")) {
            recipes_config.set("shaped.Stattenheim Remote.easy_ingredients.L", "LAPIS_LAZULI");
            recipes_config.set("shaped.TARDIS Locator Circuit.easy_ingredients.L", "LAPIS_LAZULI");
            recipes_config.set("shaped.TARDIS Locator Circuit.hard_ingredients.L", "LAPIS_LAZULI");
            recipes_config.set("shaped.TARDIS Materialisation Circuit.easy_ingredients.L", "LAPIS_LAZULI");
            recipes_config.set("shaped.TARDIS Materialisation Circuit.hard_ingredients.L", "LAPIS_LAZULI");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.I", "INK_SAC");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.I", "INK_SAC");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.D", "PURPLE_DYE");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.D", "PURPLE_DYE");
            recipes_config.set("shaped.Jammy Dodger.easy_ingredients.R", "ROSE_RED");
            recipes_config.set("shaped.Jammy Dodger.hard_ingredients.R", "ROSE_RED");
            recipes_config.set("shaped.Stattenheim Remote.lore", "Right-click block~to call TARDIS");
            recipes_config.set("shaped.Artron Storage Cell.lore", "Charge Level~0");
            i++;
        }
        if (!recipes_config.contains("shaped.Paper Bag") || !recipes_config.contains("shaped.Paper Bag.hard_ingredients.C")) {
            recipes_config.set("shaped.Paper Bag.easy_shape", "---,PLP,-P-");
            recipes_config.set("shaped.Paper Bag.easy_ingredients.P", "PAPER");
            recipes_config.set("shaped.Paper Bag.easy_ingredients.L", "LAPIS_BLOCK");
            recipes_config.set("shaped.Paper Bag.hard_shape", "-LC,PSP,-P-");
            recipes_config.set("shaped.Paper Bag.hard_ingredients.L", "LAPIS_BLOCK");
            recipes_config.set("shaped.Paper Bag.hard_ingredients.C", "COMPARATOR");
            recipes_config.set("shaped.Paper Bag.hard_ingredients.P", "PAPER");
            recipes_config.set("shaped.Paper Bag.hard_ingredients.S", "SHULKER_SHELL");
            recipes_config.set("shaped.Paper Bag.result", "PAPER");
            recipes_config.set("shaped.Paper Bag.amount", 1);
            recipes_config.set("shaped.Paper Bag.lore", "Smaller on the outside");
            i++;
        }
        if (!recipes_config.contains("shaped.Handles")) {
            recipes_config.set("shaped.Handles.easy_shape", "III,ISI,IRI");
            recipes_config.set("shaped.Handles.easy_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.Handles.easy_ingredients.S", "SKULL_ITEM");
            recipes_config.set("shaped.Handles.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Handles.hard_shape", "IDI,ISI,IRI");
            recipes_config.set("shaped.Handles.hard_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.Handles.hard_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.Handles.hard_ingredients.S", "SKULL_ITEM");
            recipes_config.set("shaped.Handles.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Handles.result", "BIRCH_BUTTON");
            recipes_config.set("shaped.Handles.amount", 1);
            recipes_config.set("shaped.Handles.lore", "Cyberhead from the~Maldovar Market");
            i++;
        }
        if (!recipes_config.contains("shaped.TARDIS Communicator")) {
            recipes_config.set("shaped.TARDIS Communicator.easy_shape", "N--,IIH,--I");
            recipes_config.set("shaped.TARDIS Communicator.easy_ingredients.N", "NOTE_BLOCK");
            recipes_config.set("shaped.TARDIS Communicator.easy_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.TARDIS Communicator.easy_ingredients.H", "HOPPER");
            recipes_config.set("shaped.TARDIS Communicator.hard_shape", "N--,IIH,--D");
            recipes_config.set("shaped.TARDIS Communicator.hard_ingredients.N", "NOTE_BLOCK");
            recipes_config.set("shaped.TARDIS Communicator.hard_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.TARDIS Communicator.hard_ingredients.H", "HOPPER");
            recipes_config.set("shaped.TARDIS Communicator.hard_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.TARDIS Communicator.result", "LEATHER_HELMET");
            recipes_config.set("shaped.TARDIS Communicator.amount", 1);
            recipes_config.set("shaped.TARDIS Communicator.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Rust Plague Sword")) {
            recipes_config.set("shaped.Rust Plague Sword.easy_shape", "RIR,RIR,-S-");
            recipes_config.set("shaped.Rust Plague Sword.easy_ingredients.R", "LAVA_BUCKET");
            recipes_config.set("shaped.Rust Plague Sword.easy_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.Rust Plague Sword.easy_ingredients.S", "STICK");
            recipes_config.set("shaped.Rust Plague Sword.hard_shape", "RIR,RIR,DSD");
            recipes_config.set("shaped.Rust Plague Sword.hard_ingredients.R", "LAVA_BUCKET");
            recipes_config.set("shaped.Rust Plague Sword.hard_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.Rust Plague Sword.hard_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.Rust Plague Sword.hard_ingredients.S", "STICK");
            recipes_config.set("shaped.Rust Plague Sword.result", "IRON_SWORD");
            recipes_config.set("shaped.Rust Plague Sword.amount", 1);
            recipes_config.set("shaped.Rust Plague Sword.lore", "Dalek Virus Dispenser");
            i++;
        }
        if (!recipes_config.contains("shaped.Acid Battery")) {
            recipes_config.set("shaped.Acid Battery.easy_shape", "-A-,ARA,-A-");
            recipes_config.set("shaped.Acid Battery.easy_ingredients.A", "WATER_BUCKET");
            recipes_config.set("shaped.Acid Battery.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Acid Battery.hard_shape", "-A-,ARA,-A-");
            recipes_config.set("shaped.Acid Battery.hard_ingredients.A", "WATER_BUCKET");
            recipes_config.set("shaped.Acid Battery.hard_ingredients.R", "REDSTONE_BLOCK");
            recipes_config.set("shaped.Acid Battery.result", "NETHER_BRICK");
            recipes_config.set("shaped.Acid Battery.amount", 1);
            recipes_config.set("shaped.Acid Battery.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Rift Circuit.lore")) {
            recipes_config.set("shaped.Rift Circuit.easy_shape", "-D-,DND,-D-");
            recipes_config.set("shaped.Rift Circuit.easy_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.Rift Circuit.easy_ingredients.N", "NETHER_STAR");
            recipes_config.set("shaped.Rift Circuit.hard_shape", "-D-,DND,-D-");
            recipes_config.set("shaped.Rift Circuit.hard_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.Rift Circuit.hard_ingredients.N", "NETHER_STAR");
            recipes_config.set("shaped.Rift Circuit.result", "FILLED_MAP:1983");
            recipes_config.set("shaped.Rift Circuit.amount", 1);
            recipes_config.set("shaped.Rift Circuit.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Rift Manipulator.lore")) {
            recipes_config.set("shaped.Rift Manipulator.easy_shape", "-A-,ACA,RAR");
            recipes_config.set("shaped.Rift Manipulator.easy_ingredients.A", "NETHER_BRICK");
            recipes_config.set("shaped.Rift Manipulator.easy_ingredients.C", "FILLED_MAP:1983");
            recipes_config.set("shaped.Rift Manipulator.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Rift Manipulator.hard_shape", "-A-,ACA,NAN");
            recipes_config.set("shaped.Rift Manipulator.hard_ingredients.A", "NETHER_BRICK");
            recipes_config.set("shaped.Rift Manipulator.hard_ingredients.C", "FILLED_MAP:1983");
            recipes_config.set("shaped.Rift Manipulator.hard_ingredients.N", "NETHER_STAR");
            recipes_config.set("shaped.Rift Manipulator.result", "BEACON");
            recipes_config.set("shaped.Rift Manipulator.amount", 1);
            recipes_config.set("shaped.Rift Manipulator.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Sonic Generator")) {
            recipes_config.set("shaped.Sonic Generator.easy_shape", "-S-,-F-,GRG");
            recipes_config.set("shaped.Sonic Generator.easy_ingredients.G", "GOLD_NUGGET");
            recipes_config.set("shaped.Sonic Generator.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Sonic Generator.easy_ingredients.F", "FLOWER_POT_ITEM");
            recipes_config.set("shaped.Sonic Generator.easy_ingredients.S", "BLAZE_ROD");
            recipes_config.set("shaped.Sonic Generator.hard_shape", "-S-,-F-,GRG");
            recipes_config.set("shaped.Sonic Generator.hard_ingredients.G", "GOLD_INGOT");
            recipes_config.set("shaped.Sonic Generator.hard_ingredients.R", "REDSTONE_BLOCK");
            recipes_config.set("shaped.Sonic Generator.hard_ingredients.F", "FLOWER_POT_ITEM");
            recipes_config.set("shaped.Sonic Generator.hard_ingredients.S", "BLAZE_ROD");
            recipes_config.set("shaped.Sonic Generator.result", "FLOWER_POT_ITEM");
            recipes_config.set("shaped.Sonic Generator.amount", 1);
            recipes_config.set("shaped.Sonic Generator.lore", "");
            i++;
        } else {
            recipes_config.set("shaped.Sonic Generator.easy_shape", "-S-,-F-,GRG");
            recipes_config.set("shaped.Sonic Generator.hard_shape", "-S-,-F-,GRG");
        }
        if (!recipes_config.contains("shaped.TARDIS Remote Key")) {
            recipes_config.set("shaped.TARDIS Remote Key.easy_shape", "RCR,-K-,-T-");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.C", "REDSTONE_COMPARATOR");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.K", "GOLD_NUGGET");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.TARDIS Remote Key.hard_shape", "RCR,-K-,-T-");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.C", "REDSTONE_COMPARATOR");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.K", "GOLD_NUGGET");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.T", "FILLED_MAP:1964");
            recipes_config.set("shaped.TARDIS Remote Key.result", "GOLD_NUGGET");
            recipes_config.set("shaped.TARDIS Remote Key.amount", 1);
            recipes_config.set("shaped.TARDIS Remote Key.lore", "Deadlock & unlock~Hide & rebuild");
            i++;
        } else if (recipes_config.getString("shaped.TARDIS Remote Key.easy_ingredients.T").equals("REDSTONE_TORCH")) {
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.T", "REDSTONE_TORCH");
        }
        if (!recipes_config.contains("shaped.White Bow Tie") || recipes_config.getString("shaped.White Bow Tie.easy_ingredients.W").contains(":")) {
            for (Map.Entry<String, String> map : colours.entrySet()) {
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.easy_shape", "---,SWS,---");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.easy_ingredients.S", "STRING");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.easy_ingredients.W", map.getValue());
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_shape", "STS,L-L,WWW");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.S", "STRING");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.T", "TRIPWIRE_HOOK");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.L", "LEATHER");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.W", map.getValue());
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.result", "LEATHER_CHESTPLATE");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.amount", 1);
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.lore", "Bow ties are cool!");
                i++;
            }
        }
        if (!recipes_config.contains("shaped.3-D Glasses")) {
            recipes_config.set("shaped.3-D Glasses.easy_shape", "---,P-P,CPM");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.P", "PAPER");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.C", "CYAN_STAINED_GLASS_PANE");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.M", "MAGENTA_STAINED_GLASS_PANE");
            recipes_config.set("shaped.3-D Glasses.hard_shape", "R-T,P-P,CPM");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.R", "REDSTONE_COMPARATOR");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.T", "REDSTONE_TORCH");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.P", "PAPER");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.C", "CYAN_STAINED_GLASS_PANE");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.M", "MAGENTA_STAINED_GLASS_PANE");
            recipes_config.set("shaped.3-D Glasses.result", "LEATHER_HELMET");
            recipes_config.set("shaped.3-D Glasses.amount", 1);
            recipes_config.set("shaped.3-D Glasses.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Fob Watch")) {
            recipes_config.set("shaped.Fob Watch.easy_shape", "-C-,-W-,R-R");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.C", "FILLED_MAP:1966");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.W", "CLOCK");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Fob Watch.hard_shape", "-C-,IWI,R-R");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.C", "FILLED_MAP:1966");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.W", "CLOCK");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Fob Watch.result", "CLOCK");
            recipes_config.set("shaped.Fob Watch.amount", 1);
            recipes_config.set("shaped.Fob Watch.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.TARDIS Biome Reader") || recipes_config.getString("shaped.TARDIS Biome Reader.result").equals("CLAY_BRICK")) {
            recipes_config.set("shaped.TARDIS Biome Reader.easy_shape", "---,-C-,SDT");
            recipes_config.set("shaped.TARDIS Biome Reader.easy_ingredients.C", "FILLED_MAP:1972");
            recipes_config.set("shaped.TARDIS Biome Reader.easy_ingredients.S", "SAND");
            recipes_config.set("shaped.TARDIS Biome Reader.easy_ingredients.D", "DIRT");
            recipes_config.set("shaped.TARDIS Biome Reader.easy_ingredients.T", "STONE");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_shape", "-C-,SDT,LWN");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.C", "FILLED_MAP:1972");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.S", "SAND");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.D", "DIRT");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.T", "STONE");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.L", "CLAY");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.W", "SNOW_BLOCK");
            recipes_config.set("shaped.TARDIS Biome Reader.hard_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.TARDIS Biome Reader.result", "BRICK");
            recipes_config.set("shaped.TARDIS Biome Reader.amount", 1);
            recipes_config.set("shaped.TARDIS Biome Reader.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Jammy Dodger")) {
            recipes_config.set("shaped.Jammy Dodger.easy_shape", "---,WRW,---");
            recipes_config.set("shaped.Jammy Dodger.easy_ingredients.W", "WHEAT");
            recipes_config.set("shaped.Jammy Dodger.easy_ingredients.R", "ROSE_RED");
            recipes_config.set("shaped.Jammy Dodger.hard_shape", "---,WRW,---");
            recipes_config.set("shaped.Jammy Dodger.hard_ingredients.W", "WHEAT");
            recipes_config.set("shaped.Jammy Dodger.hard_ingredients.R", "ROSE_RED");
            recipes_config.set("shaped.Jammy Dodger.result", "COOKIE");
            recipes_config.set("shaped.Jammy Dodger.amount", 8);
            recipes_config.set("shaped.Jammy Dodger.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Fish Finger")) {
            recipes_config.set("shaped.Fish Finger.easy_shape", "-B-,-F-,-B-");
            recipes_config.set("shaped.Fish Finger.easy_ingredients.B", "BREAD");
            recipes_config.set("shaped.Fish Finger.easy_ingredients.F", "RAW_FISH");
            recipes_config.set("shaped.Fish Finger.hard_shape", "-B-,-F-,-B-");
            recipes_config.set("shaped.Fish Finger.hard_ingredients.B", "BREAD");
            recipes_config.set("shaped.Fish Finger.hard_ingredients.F", "RAW_FISH");
            recipes_config.set("shaped.Fish Finger.result", "COOKED_FISH");
            recipes_config.set("shaped.Fish Finger.amount", 3);
            recipes_config.set("shaped.Fish Finger.lore", "Best eaten with custard!");
            i++;
        }
        if (!recipes_config.contains("shapeless.TARDIS Schematic Wand")) {
            recipes_config.set("shapeless.TARDIS Schematic Wand.recipe", "BONE,REDSTONE");
            recipes_config.set("shapeless.TARDIS Schematic Wand.result", "BONE");
            recipes_config.set("shapeless.TARDIS Schematic Wand.amount", 1);
            recipes_config.set("shapeless.TARDIS Schematic Wand.lore", "Right-click start~Left-click end");
            i++;
        } else {
            recipes_config.set("shapeless.TARDIS Schematic Wand.lore", "Right-click start~Left-click end");
        }
        if (!recipes_config.contains("shapeless.Bowl of Custard") || recipes_config.getString("shapeless.Bowl of Custard.result").contains("SOUP")) {
            recipes_config.set("shapeless.Bowl of Custard.recipe", "BOWL,MILK_BUCKET,EGG");
            recipes_config.set("shapeless.Bowl of Custard.result", "MUSHROOM_STEW");
            recipes_config.set("shapeless.Bowl of Custard.amount", 1);
            recipes_config.set("shapeless.Bowl of Custard.lore", "");
            i++;
        }
        if (!recipes_config.contains("shapeless.Vanilla Jelly Baby") || recipes_config.getString("shapeless.Vanilla Jelly Baby.recipe").contains(":") || recipes_config.getString("shapeless.Vanilla Jelly Baby.result").equals("MELON")) {
            for (Map.Entry<String, String> map : flavours.entrySet()) {
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.recipe", "SUGAR,SLIME_BALL," + map.getValue());
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.result", "MELON_SLICE");
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.amount", 4);
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.lore", "");
                i++;
            }
        }
        if (!recipes_config.contains("shaped.TARDIS Randomiser Circuit")) {
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_shape", "-D-,NCE,-W-");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.D", "DIRT");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.C", "COMPASS");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.E", "END_STONE");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.W", "WATER_BUCKET");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_shape", "-D-,NCE,-W-");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.D", "DIRT");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.C", "COMPASS");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.E", "END_STONE");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.W", "WATER_BUCKET");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.result", "FILLED_MAP:1980");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.amount", 1);
            recipes_config.set("shaped.TARDIS Randomiser Circuit.lore", "Uses left~50");
            i++;
        }
        if (!recipes_config.contains("shaped.TARDIS Invisibility Circuit")) {
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_shape", "-D-,P-E,-W-");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.P", "FILLED_MAP:1978");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.E", "EMERALD");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.W", "POTION");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_shape", "-D-,P-E,-W-");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.P", "FILLED_MAP:1978");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.E", "EMERALD");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.W", "POTION");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.result", "FILLED_MAP:1981");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.amount", 1);
            recipes_config.set("shaped.TARDIS Invisibility Circuit.lore", "Uses left~5");
            i++;
        } else {
            if (recipes_config.getString("shaped.TARDIS Invisibility Circuit.easy_ingredients.W").equals("POTION:8206")) {
                recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.W", "POTION");
            }
            if (recipes_config.getString("shaped.TARDIS Invisibility Circuit.hard_ingredients.W").equals("POTION:8270")) {
                recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.W", "POTION");
            }
        }
        if (!recipes_config.contains("shaped.TARDIS Telepathic Circuit")) {
            recipes_config.set("shaped.TARDIS Telepathic Circuit.easy_shape", "-S-,SES,-S-");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.easy_ingredients.S", "SLIME_BALL");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.easy_ingredients.E", "EMERALD");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.hard_shape", "-S-,SPS,ESE");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.hard_ingredients.S", "SLIME_BALL");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.hard_ingredients.P", "POTION");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.hard_ingredients.E", "EMERALD");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.result", "DAYLIGHT_DETECTOR");
            recipes_config.set("shaped.TARDIS Telepathic Circuit.amount", 1);
            recipes_config.set("shaped.TARDIS Telepathic Circuit.lore", "Allow companions to~use TARDIS commands");
            i++;
        } else if (recipes_config.getString("shaped.TARDIS Telepathic Circuit.hard_ingredients.P").equals("POTION:373")) {
            recipes_config.set("shaped.TARDIS Telepathic Circuit.hard_ingredients.P", "POTION");
        }
        if (!recipes_config.contains("shaped.Painter Circuit")) {
            recipes_config.set("shaped.Painter Circuit.easy_shape", "-I-,DGD,-I-");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.I", "INK_SAC");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.D", "PURPLE_DYE");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.G", "GOLD_NUGGET");
            recipes_config.set("shaped.Painter Circuit.hard_shape", "-I-,DGD,-I-");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.I", "INK_SAC");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.D", "PURPLE_DYE");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.G", "GOLD_BLOCK");
            recipes_config.set("shaped.Painter Circuit.result", "FILLED_MAP:1979");
            recipes_config.set("shaped.Painter Circuit.amount", 1);
            recipes_config.set("shaped.Painter Circuit.lore", "");
            i++;
        } else if (recipes_config.get("shaped.Painter Circuit.hard_shape").equals("-B-,-F-,-B-")) {
            // fix the hard recipe if necessary
            recipes_config.set("shaped.Painter Circuit.hard_shape", "-I-,DGD,-I-");
        }
        if (!recipes_config.contains("shapeless.Painter Upgrade")) {
            recipes_config.set("shapeless.Painter Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1979");
            recipes_config.set("shapeless.Painter Upgrade.result", "BLAZE_ROD");
            recipes_config.set("shapeless.Painter Upgrade.amount", 1);
            recipes_config.set("shapeless.Painter Upgrade.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Ignite Circuit")) {
            recipes_config.set("shaped.Ignite Circuit.easy_shape", "-N-,NFN,-N-");
            recipes_config.set("shaped.Ignite Circuit.easy_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.Ignite Circuit.easy_ingredients.F", "FLINT_AND_STEEL");
            recipes_config.set("shaped.Ignite Circuit.hard_shape", "LN-,NFN,-NL");
            recipes_config.set("shaped.Ignite Circuit.hard_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.Ignite Circuit.hard_ingredients.F", "FLINT_AND_STEEL");
            recipes_config.set("shaped.Ignite Circuit.hard_ingredients.L", "LAVA_BUCKET");
            recipes_config.set("shaped.Ignite Circuit.result", "FILLED_MAP:1982");
            recipes_config.set("shaped.Ignite Circuit.amount", 1);
            recipes_config.set("shaped.Ignite Circuit.lore", "");
            i++;
        }
        if (!recipes_config.contains("shapeless.Ignite Upgrade")) {
            recipes_config.set("shapeless.Ignite Upgrade.recipe", "BLAZE_ROD,FILLED_MAP:1982");
            recipes_config.set("shapeless.Ignite Upgrade.result", "BLAZE_ROD");
            recipes_config.set("shapeless.Ignite Upgrade.amount", 1);
            recipes_config.set("shapeless.Ignite Upgrade.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.TARDIS Artron Furnace")) {
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_shape", "---,OFO,RRR");
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_ingredients.O", "OBSIDIAN");
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_ingredients.F", "FURNACE");
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_shape", "---,OFO,RRR");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_ingredients.O", "OBSIDIAN");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_ingredients.F", "FURNACE");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Artron Furnace.result", "FURNACE");
            recipes_config.set("shaped.TARDIS Artron Furnace.amount", 1);
            recipes_config.set("shaped.TARDIS Artron Furnace.lore", "");
            i++;
        }
        if (!recipes_config.contains("furnace.Nuclear Wool.experience")) {
            recipes_config.set("furnace.Nuclear Wool.experience", 0);
            recipes_config.set("furnace.Nuclear Wool.cooktime", 200);
            i++;
        }
        damage.forEach((key, value) -> {
            if (recipes_config.getString(key).isEmpty()) {
                recipes_config.set(key, "Uses left~" + value);
            }
        });
        try {
            recipes_config.save(new File(plugin.getDataFolder(), "recipes.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to recipes.yml");
            }
            String key = recipes_config.getString("shaped.TARDIS Key.result");
            if (!key.equals(plugin.getConfig().getString("preferences.key"))) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "The TARDIS Key recipe result (recipes.yml) does not match the configured key preference (config.yml)");
            }
            String r_key_5 = recipes_config.getString("shaped.TARDIS Remote Key.easy_ingredients.K");
            if (r_key_5 != null && !key.equals(r_key_5)) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "The TARDIS Key ingredient (" + r_key_5 + ") in the 'TARDIS Remote Key' recipe does not match the crafting result of the 'TARDIS Key' recipe (" + key + ") - they should be the same!");
            }
        } catch (IOException io) {
            plugin.debug("Could not save recipes.yml, " + io);
        }
    }
}
