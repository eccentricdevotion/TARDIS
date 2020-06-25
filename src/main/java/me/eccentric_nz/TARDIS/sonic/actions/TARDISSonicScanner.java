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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.eccentric_nz.TARDIS.control.TARDISScanner.getNearbyEntities;

public class TARDISSonicScanner {

    public static void scan(TARDIS plugin, Location scan_loc, Player player) {
        // record nearby entities
        HashMap<EntityType, Integer> scannedentities = new HashMap<>();
        List<String> playernames = new ArrayList<>();
        getNearbyEntities(scan_loc, 16).forEach((k) -> {
            EntityType et = k.getType();
            if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                boolean visible = true;
                if (et.equals(EntityType.PLAYER)) {
                    Player entPlayer = (Player) k;
                    if (player.canSee(entPlayer)) {
                        playernames.add(entPlayer.getName());
                    } else {
                        visible = false;
                    }
                }
                if (TARDIS.plugin.getPM().isPluginEnabled("TARDISWeepingAngels")) {
                    if (et.equals(EntityType.SKELETON) || et.equals(EntityType.ZOMBIE) || et.equals(EntityType.ZOMBIFIED_PIGLIN)) {
                        EntityEquipment ee = ((LivingEntity) k).getEquipment();
                        if (ee.getHelmet() != null) {
                            switch (ee.getHelmet().getType()) {
                                case SLIME_BALL: // dalek
                                    et = EntityType.LLAMA_SPIT;
                                    break;
                                case IRON_INGOT: // Cyberman
                                    et = EntityType.AREA_EFFECT_CLOUD;
                                    break;
                                case SUGAR: // Empty Child
                                    et = EntityType.FALLING_BLOCK;
                                    break;
                                case SNOWBALL: // Ice Warrior
                                    et = EntityType.ARROW;
                                    break;
                                case FEATHER: // Silurian
                                    et = EntityType.BOAT;
                                    break;
                                case POTATO: // Sontaran
                                    et = EntityType.FIREWORK;
                                    break;
                                case BAKED_POTATO: // Strax
                                    et = EntityType.EGG;
                                    break;
                                case BOOK: // Vashta Nerada
                                    et = EntityType.ENDER_CRYSTAL;
                                    break;
                                case PAINTING: // Zygon
                                    et = EntityType.FISHING_HOOK;
                                    break;
                                case STONE_BUTTON: // weeping angel
                                    et = EntityType.DRAGON_FIREBALL;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (et.equals(EntityType.ENDERMAN) && k.getPassengers().size() > 0 && k.getPassengers().get(0) != null && k.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
                        // silent
                        et = EntityType.SPLASH_POTION;
                    }
                    if (et.equals(EntityType.ARMOR_STAND)) {
                        EntityEquipment ee = ((ArmorStand) k).getEquipment();
                        if (ee.getHelmet() != null) {
                            switch (ee.getHelmet().getType()) {
                                case YELLOW_DYE: // Judoon
                                    et = EntityType.SHULKER_BULLET;
                                    break;
                                case BONE: // K9
                                    et = EntityType.EVOKER_FANGS;
                                    break;
                                case ROTTEN_FLESH: // Ood
                                    et = EntityType.ITEM_FRAME;
                                    break;
                                case GUNPOWDER: // Toclafane
                                    et = EntityType.DROPPED_ITEM;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                Integer entity_count = scannedentities.getOrDefault(et, 0);
                if (visible) {
                    scannedentities.put(et, entity_count + 1);
                }
            }
        });
        long time = scan_loc.getWorld().getTime();
        String daynight = TARDISStaticUtils.getTime(time);
        String worldname;
        if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = scan_loc.getWorld().getName();
        }
        String wn = worldname;
        // message the player
        TARDISMessage.send(player, "SONIC_SCAN");
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_WORLD", wn);
            TARDISMessage.send(player, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        }, 20L);
        // get biome
        Biome biome = scan_loc.getBlock().getBiome();
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "BIOME_TYPE", biome.toString()), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather;
        switch (biome) {
            case BADLANDS:
            case BADLANDS_PLATEAU:
            case DESERT:
            case DESERT_HILLS:
            case DESERT_LAKES:
            case ERODED_BADLANDS:
            case MODIFIED_BADLANDS_PLATEAU:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:
            case SAVANNA:
            case SAVANNA_PLATEAU:
            case SHATTERED_SAVANNA:
            case SHATTERED_SAVANNA_PLATEAU:
            case WOODED_BADLANDS_PLATEAU:
                weather = plugin.getLanguage().getString("WEATHER_DRY");
                break;
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
            case ICE_SPIKES:
            case SNOWY_BEACH:
            case SNOWY_MOUNTAINS:
            case SNOWY_TAIGA:
            case SNOWY_TAIGA_HILLS:
            case SNOWY_TAIGA_MOUNTAINS:
            case SNOWY_TUNDRA:
                weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
                break;
            default:
                weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
                break;
        }
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_ENTS");
            if (scannedentities.size() > 0) {
                scannedentities.forEach((key, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (key.equals(EntityType.PLAYER) && playernames.size() > 0) {
                        playernames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.toString().substring(2) + ")";
                    }
                    switch (key) {
                        case AREA_EFFECT_CLOUD:
                            player.sendMessage("    Cyberman: " + value);
                            break;
                        case LLAMA_SPIT:
                            player.sendMessage("    Dalek: " + value);
                            break;
                        case FALLING_BLOCK:
                            player.sendMessage("    Empty Child: " + value);
                            break;
                        case ARROW:
                            player.sendMessage("    Ice Warrior: " + value);
                            break;
                        case SHULKER_BULLET:
                            player.sendMessage("    Judoon: " + value);
                            break;
                        case EVOKER_FANGS:
                            player.sendMessage("    K9: " + value);
                            break;
                        case ITEM_FRAME:
                            player.sendMessage("    Ood: " + value);
                            break;
                        case SPLASH_POTION:
                            player.sendMessage("    Silent: " + value);
                            break;
                        case BOAT:
                            player.sendMessage("    Silurian: " + value);
                            break;
                        case FIREWORK:
                            player.sendMessage("    Sontaran: " + value);
                            break;
                        case EGG:
                            player.sendMessage("    Strax: " + value);
                            break;
                        case DROPPED_ITEM:
                            player.sendMessage("    Toclafane: " + value);
                            break;
                        case ENDER_CRYSTAL:
                            player.sendMessage("    Vashta Nerada: " + value);
                            break;
                        case DRAGON_FIREBALL:
                            player.sendMessage("    Weeping Angel: " + value);
                            break;
                        case FISHING_HOOK:
                            player.sendMessage("    Zygon: " + value);
                            break;
                        default:
                            if (key != EntityType.ARMOR_STAND) {
                                player.sendMessage("    " + key.toString() + ": " + value + message);
                            }
                            break;
                    }
                });
                scannedentities.clear();
            } else {
                TARDISMessage.send(player, "SCAN_NONE");
            }
        }, 140L);
    }
}
