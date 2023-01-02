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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.eccentric_nz.TARDIS.control.TARDISScanner.getNearbyEntities;

/**
 * @author eccentric_nz
 */
class TARDISHandlesScanCommand {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final boolean inTARDIS;

    TARDISHandlesScanCommand(TARDIS plugin, Player player, int id) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        inTARDIS = plugin.getUtils().inTARDISWorld(this.player);
    }

    boolean sayScan() {
        TARDISSounds.playTARDISSound(player.getLocation(), "handles_scanner");
        Location scan_loc;
        String whereIsIt;
        COMPASS tardisDirection;
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        if (inTARDIS) {
            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, where);
                if (!rsn.resultSet()) {
                    TARDISMessage.handlesSend(player, "NEXT_NOT_FOUND");
                    return true;
                }
                scan_loc = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
                tardisDirection = rsn.getDirection();
                whereIsIt = plugin.getLanguage().getString("SCAN_NEXT");
            } else {
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
                if (!rsc.resultSet()) {
                    TARDISMessage.handlesSend(player, "CURRENT_NOT_FOUND");
                    return true;
                }
                scan_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                tardisDirection = rsc.getDirection();
                whereIsIt = plugin.getLanguage().getString("SCAN_CURRENT");
            }
        } else {
            scan_loc = player.getLocation();
            tardisDirection = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
            whereIsIt = plugin.getLanguage().getString("SCAN_PLAYER");
        }
        // record nearby entities
        HashMap<EntityType, Integer> scannedEntities = new HashMap<>();
        List<String> playerNames = new ArrayList<>();
        for (Entity k : getNearbyEntities(scan_loc, 16)) {
            EntityType et = k.getType();
            if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                boolean visible = true;
                if (et.equals(EntityType.PLAYER)) {
                    Player entPlayer = (Player) k;
                    if (player.canSee(entPlayer)) {
                        playerNames.add(entPlayer.getName());
                    } else {
                        visible = false;
                    }
                }
                if (plugin.getPM().isPluginEnabled("TARDISWeepingAngels")) {
                    if (et.equals(EntityType.SKELETON) || et.equals(EntityType.ZOMBIE) || et.equals(EntityType.ZOMBIFIED_PIGLIN)) {
                        EntityEquipment ee = ((LivingEntity) k).getEquipment();
                        if (ee.getHelmet() != null) {
                            switch (ee.getHelmet().getType()) {
                                case SLIME_BALL -> et = EntityType.LLAMA_SPIT; // dalek
                                case IRON_INGOT -> et = EntityType.AREA_EFFECT_CLOUD; // Cyberman
                                case SUGAR -> et = EntityType.FALLING_BLOCK; // Empty Child
                                case SNOWBALL -> et = EntityType.ARROW; // Ice Warrior
                                case FEATHER -> et = EntityType.BOAT; // Silurian
                                case POTATO -> et = EntityType.FIREWORK; // Sontaran
                                case BAKED_POTATO -> et = EntityType.EGG; // Strax
                                case BOOK -> et = EntityType.ENDER_CRYSTAL; // Vashta Nerada
                                case PAINTING -> et = EntityType.FISHING_HOOK; // Zygon
                                case STONE_BUTTON -> et = EntityType.DRAGON_FIREBALL; // weeping angel
                                default -> { }
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
                                case YELLOW_DYE -> et = EntityType.SHULKER_BULLET; // Judoon
                                case BONE -> et = EntityType.EVOKER_FANGS; // K9
                                case ROTTEN_FLESH -> et = EntityType.ITEM_FRAME; // Ood
                                case GUNPOWDER -> et = EntityType.DROPPED_ITEM; // Toclafane
                                default -> { }
                            }
                        }
                    }
                }
                Integer entity_count = scannedEntities.getOrDefault(et, 0);
                if (visible) {
                    scannedEntities.put(et, entity_count + 1);
                }
            }
        }
        long time = scan_loc.getWorld().getTime();
        String daynight = TARDISStaticUtils.getTime(time);
        // message the player
        if (inTARDIS) {
            TARDISMessage.handlesSend(player, "SCAN_RESULT", whereIsIt);
        } else {
            TARDISMessage.handlesSend(player, "SCAN_PLAYER");
        }
        String worldname;
        if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = TARDISAliasResolver.getWorldAlias(scan_loc.getWorld());
        }
        TARDISMessage.handlesSend(player, "SCAN_WORLD", worldname);
        TARDISMessage.handlesSend(player, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.handlesSend(player, "SCAN_DIRECTION", tardisDirection.toString()), 20L);
        // get biome
        String biome = scan_loc.getBlock().getBiome().toString();
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.handlesSend(player, "BIOME_TYPE", biome), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.handlesSend(player, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather = switch (biome) {
            case "DESERT", "DESERT_HILLS", "DESERT_LAKES", "SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU", "BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> plugin.getLanguage().getString("WEATHER_DRY");
            case "SNOWY_TUNDRA", "ICE_SPIKES", "FROZEN_OCEAN", "FROZEN_RIVER", "SNOWY_BEACH", "SNOWY_TAIGA", "SNOWY_MOUNTAINS", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS" -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
            default -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
        };
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.handlesSend(player, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.handlesSend(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.handlesSend(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            if (scannedEntities.size() > 0) {
                TARDISMessage.handlesSend(player, "SCAN_ENTS");
                for (Map.Entry<EntityType, Integer> entry : scannedEntities.entrySet()) {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (entry.getKey().equals(EntityType.PLAYER) && playerNames.size() > 0) {
                        playerNames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.substring(2) + ")";
                    }
                    // delay
                    String m = message;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        switch (entry.getKey()) {
                            case AREA_EFFECT_CLOUD -> player.sendMessage("    Cyberman: " + entry.getValue());
                            case LLAMA_SPIT -> player.sendMessage("    Dalek: " + entry.getValue());
                            case ARMOR_STAND -> player.sendMessage("    Empty Child: " + entry.getValue());
                            case ARROW -> player.sendMessage("    Ice Warrior: " + entry.getValue());
                            case SPLASH_POTION -> player.sendMessage("    Silent: " + entry.getValue());
                            case BOAT -> player.sendMessage("    Silurian: " + entry.getValue());
                            case FIREWORK -> player.sendMessage("    Sontaran: " + entry.getValue());
                            case EGG -> player.sendMessage("    Strax: " + entry.getValue());
                            case ENDER_CRYSTAL -> player.sendMessage("    Vashta Nerada: " + entry.getValue());
                            case DRAGON_FIREBALL -> player.sendMessage("    Weeping Angel: " + entry.getValue());
                            case FISHING_HOOK -> player.sendMessage("    Zygon: " + entry.getValue());
                            default -> player.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + m);
                        }
                    }, 3L);
                }
                scannedEntities.clear();
            } else {
                TARDISMessage.handlesSend(player, "SCAN_NONE");
            }
            // damage the circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.scanner") > 0) {
                TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
                // decrement uses
                int uses_left = tcc.getScannerUses();
                new TARDISCircuitDamager(plugin, DiskCircuit.SCANNER, uses_left, id, player).damage();
            }
        }, 140L);
        return true;
    }
}
