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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.control.TARDISScanner;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitScheduler;

public class TARDISSonicScanner {

    public static void scan(TARDIS plugin, Location scan_loc, Player player) {
        // record nearby entities
        HashMap<EntityType, Integer> scannedEntities = new HashMap<>();
        List<String> playernames = new ArrayList<>();
        TARDISScanner.getNearbyEntities(scan_loc, 16).forEach((k) -> {
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
                if (TARDIS.plugin.getConfig().getBoolean("modules.weeping_angels")) {
                    if (et.equals(EntityType.SKELETON) || et.equals(EntityType.ZOMBIE) || et.equals(EntityType.ZOMBIFIED_PIGLIN)) {
                        EntityEquipment ee = ((LivingEntity) k).getEquipment();
                        if (ee.getHelmet() != null) {
                            switch (ee.getHelmet().getType()) {
                                case BAKED_POTATO -> et = EntityType.EGG; // Strax
                                case BOOK -> et = EntityType.ENDER_CRYSTAL; // Vashta Nerada
                                case CRIMSON_BUTTON -> et = EntityType.BLOCK_DISPLAY; // davros
                                case FEATHER -> et = EntityType.BOAT; // Silurian
                                case IRON_INGOT -> et = EntityType.AREA_EFFECT_CLOUD; // Cyberman
                                case KELP -> et = EntityType.THROWN_EXP_BOTTLE; // sea devil
                                case MANGROVE_PROPAGULE -> et = EntityType.SMALL_FIREBALL; // dalek sec
                                case NETHERITE_SCRAP -> et = EntityType.GLOW_ITEM_FRAME; // mire
                                case PAINTING -> et = EntityType.FISHING_HOOK; // Zygon
                                case POTATO -> et = EntityType.FIREWORK; // Sontaran
                                case PUFFERFISH -> et = EntityType.INTERACTION; // hath
                                case RED_CANDLE -> et = EntityType.TEXT_DISPLAY; // headless monk
                                case SLIME_BALL -> et = EntityType.LLAMA_SPIT; // dalek
                                case SNOWBALL -> et = EntityType.SNOWBALL; // Ice Warrior
                                case SPIDER_EYE -> et = EntityType.ITEM_DISPLAY; // racnoss
                                case STONE_BUTTON -> et = EntityType.DRAGON_FIREBALL; // weeping angel
                                case SUGAR -> et = EntityType.FALLING_BLOCK; // Empty Child
                                case TURTLE_EGG -> et = EntityType.ARROW; // slitheen
                                default -> {
                                }
                            }
                        }
                    }
                    if (et.equals(EntityType.SKELETON) && !k.getPassengers().isEmpty() && k.getPassengers().get(0) != null && k.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
                        // silent
                        et = EntityType.SPLASH_POTION;
                    }
                    if (et.equals(EntityType.ARMOR_STAND)) {
                        EntityEquipment ee = ((ArmorStand) k).getEquipment();
                        if (ee.getHelmet() != null) {
                            switch (ee.getHelmet().getType()) {
                                case YELLOW_DYE -> et = EntityType.SHULKER_BULLET; // Judoon
                                case BONE -> et = EntityType.EVOKER_FANGS; // K9
                                case ROTTEN_FLESH -> et = EntityType.ITEM_FRAME; // Ood
                                case GUNPOWDER -> et = EntityType.DROPPED_ITEM; // Toclafane
                                default -> {
                                }
                            }
                        }
                    }
                }
                Integer entity_count = scannedEntities.getOrDefault(et, 0);
                if (visible) {
                    scannedEntities.put(et, entity_count + 1);
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
        plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_SCAN");
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_WORLD", wn);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        }, 20L);
        // get biome
        String biome = scan_loc.getBlock().getBiome().toString();
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_TYPE", biome), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather = switch (biome) {
            case "BADLANDS", "BADLANDS_PLATEAU", "DESERT", "DESERT_HILLS", "DESERT_LAKES", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> plugin.getLanguage().getString("WEATHER_DRY");
            case "FROZEN_OCEAN", "FROZEN_RIVER", "ICE_SPIKES", "SNOWY_BEACH", "SNOWY_MOUNTAINS", "SNOWY_TAIGA", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS", "SNOWY_TUNDRA" -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
            default -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
        };
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_ENTS");
            if (!scannedEntities.isEmpty()) {
                scannedEntities.forEach((ent, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (ent.equals(EntityType.PLAYER) && !playernames.isEmpty()) {
                        playernames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.substring(2) + ")";
                    }
                    switch (ent) {
                        case AREA_EFFECT_CLOUD -> player.sendMessage("    Cyberman: " + value);
                        case ARROW -> player.sendMessage("    Slitheen: " + value);
                        case BLOCK_DISPLAY -> player.sendMessage("    Davros: " + value);
                        case BOAT -> player.sendMessage("    Silurian: " + value);
                        case DRAGON_FIREBALL -> player.sendMessage("    Weeping Angel: " + value);
                        case DROPPED_ITEM -> player.sendMessage("    Toclafane: " + value);
                        case EGG -> player.sendMessage("    Strax: " + value);
                        case ENDER_CRYSTAL -> player.sendMessage("    Vashta Nerada: " + value);
                        case EVOKER_FANGS -> player.sendMessage("    K9: " + value);
                        case FALLING_BLOCK -> player.sendMessage("    Empty Child: " + value);
                        case FIREWORK -> player.sendMessage("    Sontaran: " + value);
                        case FISHING_HOOK -> player.sendMessage("    Zygon: " + value);
                        case GLOW_ITEM_FRAME -> player.sendMessage("    Mire: " + value);
                        case INTERACTION -> player.sendMessage("    Hath: " + value);
                        case ITEM_DISPLAY -> player.sendMessage("    Racnoss: " + value);
                        case ITEM_FRAME -> player.sendMessage("    Ood: " + value);
                        case LLAMA_SPIT -> player.sendMessage("    Dalek: " + value);
                        case SHULKER_BULLET -> player.sendMessage("    Judoon: " + value);
                        case SMALL_FIREBALL -> player.sendMessage("    Dalek Sec: " + value);
                        case SNOWBALL -> player.sendMessage("    Ice Warrior: " + value);
                        case SPLASH_POTION -> player.sendMessage("    Silent: " + value);
                        case TEXT_DISPLAY -> player.sendMessage("    Headless Monk: " + value);
                        case THROWN_EXP_BOTTLE -> player.sendMessage("    Sea Devil: " + value);
                        default -> {
                            if (ent != EntityType.ARMOR_STAND) {
                                player.sendMessage("    " + ent + ": " + value + message);
                            }
                        }
                    }
                });
                scannedEntities.clear();
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_NONE");
            }
        }, 140L);
    }
}
