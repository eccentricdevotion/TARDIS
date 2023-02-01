/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
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
                                case SLIME_BALL -> // dalek
                                        et = EntityType.LLAMA_SPIT;
                                case IRON_INGOT -> // Cyberman
                                        et = EntityType.AREA_EFFECT_CLOUD;
                                case SUGAR -> // Empty Child
                                        et = EntityType.FALLING_BLOCK;
                                case SNOWBALL -> // Ice Warrior
                                        et = EntityType.ARROW;
                                case FEATHER -> // Silurian
                                        et = EntityType.BOAT;
                                case POTATO -> // Sontaran
                                        et = EntityType.FIREWORK;
                                case BAKED_POTATO -> // Strax
                                        et = EntityType.EGG;
                                case BOOK -> // Vashta Nerada
                                        et = EntityType.ENDER_CRYSTAL;
                                case PAINTING -> // Zygon
                                        et = EntityType.FISHING_HOOK;
                                case STONE_BUTTON -> // weeping angel
                                        et = EntityType.DRAGON_FIREBALL;
                                default -> {
                                }
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
                                case YELLOW_DYE -> // Judoon
                                        et = EntityType.SHULKER_BULLET;
                                case BONE -> // K9
                                        et = EntityType.EVOKER_FANGS;
                                case ROTTEN_FLESH -> // Ood
                                        et = EntityType.ITEM_FRAME;
                                case GUNPOWDER -> // Toclafane
                                        et = EntityType.DROPPED_ITEM;
                                default -> {
                                }
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
        if (!plugin.getPlanetsConfig().getBoolean("planets." + scan_loc.getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = TARDISAliasResolver.getWorldAlias(scan_loc.getWorld());
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
        String biome = scan_loc.getBlock().getBiome().toString();
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "BIOME_TYPE", biome), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather = switch (biome) {
            case "BADLANDS", "BADLANDS_PLATEAU", "DESERT", "DESERT_HILLS", "DESERT_LAKES", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> plugin.getLanguage().getString("WEATHER_DRY");
            case "FROZEN_OCEAN", "FROZEN_RIVER", "ICE_SPIKES", "SNOWY_BEACH", "SNOWY_MOUNTAINS", "SNOWY_TAIGA", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS", "SNOWY_TUNDRA" -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
            default -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
        };
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_ENTS");
            if (scannedentities.size() > 0) {
                scannedentities.forEach((ent, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (ent.equals(EntityType.PLAYER) && playernames.size() > 0) {
                        playernames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.substring(2) + ")";
                    }
                    switch (ent) {
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
                            if (ent != EntityType.ARMOR_STAND) {
                                player.sendMessage("    " + ent + ": " + value + message);
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
