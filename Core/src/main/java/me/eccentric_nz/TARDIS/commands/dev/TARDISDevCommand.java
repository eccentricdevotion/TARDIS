/*
 * Copyright (C) 2025 eccentric_nz
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

import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.bStats.ARSRoomCounts;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.disguise.ArmourTrim;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.skins.ArchSkins;
import me.eccentric_nz.TARDIS.skins.DoctorSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.tv.TVInventory;
import me.eccentric_nz.TARDIS.utility.Pluraliser;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisregeneration.Regenerator;
import me.eccentric_nz.tardisweepingangels.equip.MonsterArmour;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BrushableBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

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
            "add_regions", "advancements", "armour",
            "biome", "box", "brushable",
            "chunks", "chunky", "circuit", "component",
            "dalek", "debug", "dismount", "displayitem",
            "effect",
            "frame", "furnace",
            "gravity", "give",
            "head",
            "interaction",
            "label", "leather", "list",
            "monster",
            "nms",
            "plurals",
            "recipe", "regen", "registry",
            "screen", "skin", "snapshot", "stats", "systree",
            "tis", "tips", "tree", "trim",
            "zero"
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
                String first = args[0].toLowerCase(Locale.ROOT);
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
                        case "dalek" -> {
                            if (sender instanceof Player player) {
                                Location eyeLocation = player.getTargetBlock(null, 16).getLocation();
                                eyeLocation.add(0.5d, 1.25d, 0.5d);
                                eyeLocation.setYaw(player.getLocation().getYaw() - 180.0f);
                                Skeleton skeleton = (Skeleton) eyeLocation.getWorld().spawnEntity(eyeLocation, EntityType.SKELETON);
                                EntityEquipment ee = skeleton.getEquipment();
                                ItemStack head = new ItemStack(Material.SLIME_BALL);
                                ItemMeta him = head.getItemMeta();
                                him.setItemModel(new NamespacedKey(plugin, "dalek_independent_head"));
                                EquippableComponent component = him.getEquippable();
                                component.setSlot(EquipmentSlot.HEAD);
                                component.setAllowedEntities(EntityType.SKELETON);
                                him.setEquippable(component);
                                head.setItemMeta(him);
                                ee.setHelmet(head);
                                ItemStack body = new ItemStack(Material.SLIME_BALL);
                                ItemMeta bim = body.getItemMeta();
                                bim.setItemModel(new NamespacedKey(plugin, "dalek_body"));
                                body.setItemMeta(bim);
                                ee.setItemInMainHand(body);
                                PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
                                skeleton.addPotionEffect(invisibility);
                                PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
                                skeleton.addPotionEffect(resistance);
                            }
                        }
                        case "furnace" -> {
                            return new TARDISFurnaceCommand(plugin).list();
                        }
                        case "gravity" -> {
                            if (sender instanceof Player player) {
                                player.setGravity(!player.hasGravity());
                            }
                            return true;
                        }
                        case "give" -> {
                            if (sender instanceof Player player) {
                                new StorageContents(plugin).give(player);
                            }
                            return true;
                        }
                        case "head" -> {
                            if (sender instanceof Player player) {
                                new HeadCommand(plugin).giveAPIHead(player);
                            }
                            return true;
                        }
                        case "interaction" -> {
                            if (sender instanceof Player player) {
                                return new TARDISInteractionCommand(plugin).process(player.getUniqueId());
                            }
                            return false;
                        }
                        case "leather" -> {
                            if (sender instanceof Player player) {
                                ItemStack is = new ItemStack(Material.LEATHER_HORSE_ARMOR);
                                LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
                                im.setColor(Color.fromRGB(255, 0, 0));
                                im.addItemFlags(ItemFlag.values());
                                im.setAttributeModifiers(Multimaps.forMap(Map.of()));
                                im.setEquippable(null);
                                CustomModelDataComponent cmdc = im.getCustomModelDataComponent();
                                List<String> strings = List.of("chameleon_tint");
                                cmdc.setStrings(strings);
                                im.setCustomModelDataComponent(cmdc);
                                is.setItemMeta(im);
                                player.getInventory().addItem(is);
                                return true;
                            }
                            return false;
                        }
                        case "monster" -> {
                            if (sender instanceof Player player) {
                                // get open portals
                                for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
                                    // only portals in police box worlds
                                    if (map.getKey().getWorld().getName().contains("TARDIS")) {
                                        continue;
                                    }
                                    if (map.getValue().isAbandoned()) {
                                        continue;
                                    }
                                    plugin.getMessenger().message(player, "tardisId => " + map.getValue().getTardisId());
                                }
                                plugin.getMessenger().message(player, "End of open portal list.");
                                return true;
                            }
                            return false;
                        }
                        case "regen" -> {
                            if (sender instanceof Player player) {
                                new Regenerator().dev(plugin, player, args);
                            }
                            return true;
                        }
                        case "registry" -> {
                            for (Art a : Registry.ART) {
                                try {
                                    plugin.debug(a.toString() + " " + plugin.getFromRegistry().getKeysKey(a));
                                } catch (NoSuchElementException | NoSuchMethodError ignored) {
                                }
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
                        case "tips" -> {
                            return new TIPSPreviewSlotInfo(plugin).display();
                        }
                        case "zero" -> {
                            if (sender instanceof Player player) {
                                return new ZeroCommand().spiral(player);
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
                    case "armour" -> {
                        if (sender instanceof Player player) {
                            try {
                                Monster monster = Monster.valueOf(args[1].toUpperCase(Locale.ROOT));
                                EquipmentSlot slot = EquipmentSlot.valueOf(args[2].toUpperCase(Locale.ROOT));
                                if (slot != EquipmentSlot.CHEST && slot != EquipmentSlot.LEGS) {
                                    return false;
                                }
                                ItemStack a = MonsterArmour.makeEquippable(monster, slot);
                                player.getInventory().addItem(a);
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                        return true;
                    }
                    case "biome" -> {
                        return new TARDISBiomeCommand().getName(sender);
                    }
                    case "box" -> {
                        return new TARDISDevBoxCommand(plugin).setPreset(sender, args);
                    }
                    case "component" -> {
                        switch (args[1].toLowerCase(Locale.ROOT)) {
                            case "ars" -> new ComponentCommand(plugin).writeARS();
                            case "repeater" -> new ComponentCommand(plugin).writeRepeater();
                            case "key" -> new ComponentCommand(plugin).writeKey();
                            case "pack" -> new ResourcePackConverterCommand(plugin).process(sender, args);
                            case "extra" -> new ComponentCommand(plugin).writeExtra(sender);
                            default -> {
                            }
                        }
                        return true;
                    }
                    case "debug" -> {
                        return new DebugCommand(plugin).process(sender, args);
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
                    case "screen" -> {
                        if (sender instanceof Player player) {
                            // get the console text display
                            return new ConsoleTextCommand(plugin).move(player, args);
                        }
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
                    case "trim" -> {
                        if (sender instanceof Player player) {
                            new ArmourTrim().giveCustomArmour(player, args);
                        }
                        return true;
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
