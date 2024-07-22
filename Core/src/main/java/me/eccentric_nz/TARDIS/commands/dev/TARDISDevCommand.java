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
package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.bStats.ARSRoomCounts;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.regeneration.Regenerator;
import me.eccentric_nz.TARDIS.skins.ArchSkins;
import me.eccentric_nz.TARDIS.skins.DoctorSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.tv.TVInventory;
import me.eccentric_nz.TARDIS.utility.Pluraliser;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrushableBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Command /tardisadmin [arguments].
 * <p>
 * The Lord President was the most powerful member of the Time Lord Council and had near absolute authority, and used a
 * link to the Matrix, a vast computer network containing the knowledge and experiences of all past generations of Time
 * Lords, to set Time Lord policy and remain alert to potential threats from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TARDISDevCommand implements CommandExecutor {

    private final Set<String> firstsStr = Sets.newHashSet(
            "add_regions", "advancements",
            "biome", "box", "brushable",
            "chunks", "chunky", "circuit",
            "dismount", "displayitem",
            "effect",
            "frame", "furnace",
            "gravity",
            "interaction",
            "label", "list",
            "nms",
            "plurals",
            "recipe", "regen",
            "skin", "snapshot", "stats", "systree",
            "tis", "tree"
    );
    private final TARDIS plugin;

    public TARDISDevCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisdev then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisdev")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisadmin", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.contains(first)) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1) {
                    switch (first) {
                        case "add_regions" -> {
                            return new TARDISAddRegionsCommand(plugin).doCheck(sender);
                        }
                        case "biome" -> {
                            return new TARDISBiomeCommand().reset(sender);
                        }
                        case "furnace" -> {
                            return new TARDISFurnaceCommand(plugin).list(sender);
                        }
                        case "gravity" -> {
                            if (sender instanceof Player player) {
                                player.setGravity(!player.hasGravity());
                            }
                            return true;
                        }
                        case "interaction" -> {
                            if (sender instanceof Player player) {
                                return new TARDISInteractionCommand(plugin).process(player.getUniqueId());
                            }
                            return false;
                        }
                        case "regen" -> {
                            if (sender instanceof Player player) {
                                new Regenerator().dev(plugin, player, args);
                            }
                            return true;
                        }
                        case "skin" -> {
                            if (sender instanceof Player player) {
                                plugin.getSkinChanger().remove(player);
                            }
                            return true;
                        }
                        case "stats" -> {
                            ARSRoomCounts arsRoomCounts = new ARSRoomCounts(plugin);
                            for (Map.Entry<String, Integer> entry : arsRoomCounts.getRoomCounts().entrySet()) {
                                plugin.debug(entry.getKey() + ": " + entry.getValue());
                            }
                            plugin.debug("Median per TARDIS: " + arsRoomCounts.getMedian());
                            return true;
                        }
                        case "systree" -> {
                            if (sender instanceof Player player) {
                                return new SystemTreeCommand(plugin).open(player);
                            }
                            return false;
                        }
                        default -> {
                        }
                    }
                }
                switch (first) {
                    case "advancements" -> {
                        TARDISAchievementFactory.checkAdvancement(args[1]);
                        return true;
                    }
                    case "biome" -> {
                        return new TARDISBiomeCommand().getName(sender);
                    }
                    case "box" -> {
                        return new TARDISDevBoxCommand(plugin).setPreset(sender, args);
                    }
                    case "label" -> {
                        return new TARDISDevLabelCommand(plugin).catalog(sender);
                    }
                    case "nms" -> {
                        return new TARDISDevNMSCommand(plugin).spawn(sender, args);
                    }
                    case "circuit" -> {
                        return new TARDISDevCircuitCommand(plugin).give(sender);
                    }
                    case "tis" -> {
                        return new TARDISDevInfoCommand(plugin).test(sender);
                    }
                    case "list" -> {
                        return new TARDISDevListCommand(plugin).listStuff(sender, args);
                    }
                    case "tree" -> {
                        return new TARDISTreeCommand(plugin).grow(sender, args);
                    }
                    case "recipe" -> {
                        return new TARDISWikiRecipeCommand(plugin).write(sender, args);
                    }
                    case "plurals" -> {
                        for (Material m : Material.values()) {
                            String str = m.toString().toLowerCase(Locale.ROOT).replace("_", " ");
                            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, str + " --> " + Pluraliser.pluralise(str));
                        }
                        return true;
                    }
                    case "chunks" -> {
                        return new TARDISChunksCommand(plugin).list(sender);
                    }
                    case "chunky" -> {
                        if (!plugin.getPM().isPluginEnabled("Chunky")) {
                            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, "Chunky plugin is not enabled!");
                            return true;
                        }
                        String radius = args.length > 2 ? args[2] : "250";
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky world " + args[1]);
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky radius " + radius);
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky spawn");
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky start");
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky confirm");
                        return true;
                    }
                    case "skin" -> {
                        if (sender instanceof Player player) {
                            if (TARDISNumberParsers.isSimpleNumber(args[1])) {
                                Skin which;
                                switch (args[1]) {
                                    case "1" -> which = DoctorSkins.FIRST;
                                    case "2" -> which = DoctorSkins.SECOND;
                                    case "3" -> which = DoctorSkins.THIRD;
                                    case "4" -> which = DoctorSkins.FOURTH;
                                    case "5" -> which = DoctorSkins.FIFTH;
                                    case "6" -> which = DoctorSkins.SIXTH;
                                    case "7" -> which = DoctorSkins.SEVENTH;
                                    case "8" -> which = DoctorSkins.EIGHTH;
                                    case "9" -> which = DoctorSkins.NINTH;
                                    case "10" -> which = DoctorSkins.TENTH;
                                    case "11" -> which = DoctorSkins.ELEVENTH;
                                    case "12" -> which = DoctorSkins.TWELFTH;
                                    case "13" -> which = DoctorSkins.THIRTEENTH;
                                    case "14" -> which = DoctorSkins.FOURTEENTH;
                                    case "15" -> which = DoctorSkins.FIFTEENTH;
                                    case "16" -> which = DoctorSkins.WAR;
                                    default -> which = ArchSkins.HEROBRINE;
                                }
                                plugin.getSkinChanger().set(player, which);
                            } else {
//                                SkinUtils.debug(player);
//                                SkinUtils.setSkinModel(player, true);
                                ItemStack[] items = new TVInventory().getMenu();
                                Inventory tvinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "TARDIS Television");
                                tvinv.setContents(items);
                                player.openInventory(tvinv);
                            }
                        }
                        return true;
                    }
                    case "snapshot" -> {
                        if (sender instanceof Player player) {
                            if (args[1].equals("c")) {
                                player.performCommand("minecraft:clear @s minecraft:filled_map");
                            } else {
                                new MonitorSnapshot(plugin).get(args[1].equals("in"), player);
                            }
                        }
                        return true;
                    }
                    case "displayitem" -> {
                        if (sender instanceof Player player) {
                            return new TARDISDisplayItemCommand(plugin).display(player, args);
                        } else {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                            return true;
                        }
                    }
                    case "frame" -> {
                        if (sender instanceof Player player) {
                            return new TARDISFrameCommand(plugin).toggle(player, args[1].equalsIgnoreCase("lock"), args.length == 3);
                        } else {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                            return true;
                        }
                    }
                    case "brushable" -> {
                        if (sender instanceof Player player) {
                            if (args.length == 2) {
                                // get target block
                                Block block = player.getTargetBlock(null, 8);
                                sender.sendMessage(block.getState().toString());
                            } else {
                                ItemStack sand = new ItemStack(Material.SUSPICIOUS_SAND);
                                BlockStateMeta sandMeta = (BlockStateMeta) sand.getItemMeta();
                                BrushableBlock blockState = (BrushableBlock) sandMeta.getBlockState();
                                blockState.setItem(player.getInventory().getItemInMainHand());
                                sandMeta.setBlockState(blockState);
                                sand.setItemMeta(sandMeta);
                                player.getInventory().addItem(sand);
                            }
                        }
                        return true;
                    }
                    case "dismount" -> {
                        if (sender instanceof Player player) {
                            if (player.getVehicle() != null) {
                                player.getVehicle().eject();
                            }
                        }
                        return true;
                    }
                    case "effect" -> {
                        if (sender instanceof Player player) {
                            return new TARDISDevEffectCommand(plugin).show(player, args);
                        }
                    }
                    default -> {
                        return false;
                    }
                }
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}
