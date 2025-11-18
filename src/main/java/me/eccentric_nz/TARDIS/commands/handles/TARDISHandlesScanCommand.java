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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.control.TARDISScanner;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
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
import java.util.Locale;

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
        if (inTARDIS) {
            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, id);
                if (!rsn.resultSet()) {
                    plugin.getMessenger().handlesSend(player, "NEXT_NOT_FOUND");
                    return true;
                }
                scan_loc = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
                tardisDirection = rsn.getDirection();
                whereIsIt = plugin.getLanguage().getString("SCAN_NEXT");
            } else {
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                if (!rsc.resultSet()) {
                    plugin.getMessenger().handlesSend(player, "CURRENT_NOT_FOUND");
                    return true;
                }
                Current current = rsc.getCurrent();
                scan_loc = current.location();
                tardisDirection = current.direction();
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
        for (Entity k : TARDISScanner.getNearbyEntities(scan_loc, 16)) {
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
                if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                    if (et.equals(EntityType.SKELETON) || et.equals(EntityType.ZOMBIE) || et.equals(EntityType.ZOMBIFIED_PIGLIN)) {
                        EntityEquipment ee = ((LivingEntity) k).getEquipment();
                        if (ee.getHelmet() != null) {
                            switch (ee.getHelmet().getType()) {
                                case BAKED_POTATO -> et = EntityType.EGG; // Strax
                                case BOOK -> et = EntityType.END_CRYSTAL; // Vashta Nerada
                                case CRIMSON_BUTTON -> et = EntityType.BLOCK_DISPLAY; // davros
                                case FEATHER -> et = EntityType.JUNGLE_BOAT; // Silurian
                                case IRON_INGOT -> et = EntityType.AREA_EFFECT_CLOUD; // Cyberman
                                case KELP -> et = EntityType.EXPERIENCE_BOTTLE; // sea devil
                                case MANGROVE_PROPAGULE -> et = EntityType.SMALL_FIREBALL; // dalek sec
                                case NETHERITE_SCRAP -> et = EntityType.GLOW_ITEM_FRAME; // mire
                                case PAINTING -> et = EntityType.FISHING_BOBBER; // Zygon
                                case POTATO -> et = EntityType.FIREWORK_ROCKET; // Sontaran
                                case PUFFERFISH -> et = EntityType.INTERACTION; // hath
                                case RED_CANDLE -> et = EntityType.TEXT_DISPLAY; // headless monk
                                case SLIME_BALL -> et = EntityType.LLAMA_SPIT; // dalek
                                case SNOWBALL -> et = EntityType.SNOWBALL; // Ice Warrior
                                case SPIDER_EYE -> et = EntityType.ITEM_DISPLAY; // racnoss
                                case STONE_BUTTON -> et = EntityType.DRAGON_FIREBALL; // weeping angel
                                case SUGAR -> et = EntityType.FALLING_BLOCK; // Empty Child
                                case TURTLE_EGG -> et = EntityType.ARROW; // slitheen
                                default -> { }
                            }
                        }
                    }
                    if (et.equals(EntityType.ENDERMAN) && !k.getPassengers().isEmpty() && k.getPassengers().getFirst() != null && k.getPassengers().getFirst().getType().equals(EntityType.GUARDIAN)) {
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
                                case GUNPOWDER -> et = EntityType.ITEM; // Toclafane
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
            plugin.getMessenger().handlesSend(player, "SCAN_RESULT", whereIsIt);
        } else {
            plugin.getMessenger().handlesSend(player, "SCAN_PLAYER");
        }
        String worldname;
        if (!plugin.getPlanetsConfig().getBoolean("planets." + scan_loc.getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = TARDISAliasResolver.getWorldAlias(scan_loc.getWorld());
        }
        plugin.getMessenger().handlesSend(player, "SCAN_WORLD", worldname);
        plugin.getMessenger().handlesSend(player, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().handlesSend(player, "SCAN_DIRECTION", tardisDirection.toString()), 20L);
        // get biome
        String biome = scan_loc.getBlock().getBiome().getKey().value().toUpperCase(Locale.ROOT);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().handlesSend(player, "BIOME_TYPE", biome), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().handlesSend(player, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather = switch (biome) {
            case "DESERT", "DESERT_HILLS", "DESERT_LAKES", "SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU", "BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> plugin.getLanguage().getString("WEATHER_DRY");
            case "SNOWY_TUNDRA", "ICE_SPIKES", "FROZEN_OCEAN", "FROZEN_RIVER", "SNOWY_BEACH", "SNOWY_TAIGA", "SNOWY_MOUNTAINS", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS" -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
            default -> (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
        };
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().handlesSend(player, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().handlesSend(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().handlesSend(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            if (!scannedEntities.isEmpty()) {
                plugin.getMessenger().handlesSend(player, "SCAN_ENTS");
                scannedEntities.forEach((ent, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (ent.equals(EntityType.PLAYER) && !playerNames.isEmpty()) {
                        playerNames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.substring(2) + ")";
                    }
                    // delay
                    String m = message;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        switch (ent) {
                            case AREA_EFFECT_CLOUD -> player.sendMessage("    Cyberman: " + value);
                            case ARROW -> player.sendMessage("    Slitheen: " + value);
                            case BLOCK_DISPLAY -> player.sendMessage("    Davros: " + value);
                            case JUNGLE_BOAT -> player.sendMessage("    Silurian: " + value);
                            case DRAGON_FIREBALL -> player.sendMessage("    Weeping Angel: " + value);
                            case ITEM -> player.sendMessage("    Toclafane: " + value);
                            case EGG -> player.sendMessage("    Strax: " + value);
                            case END_CRYSTAL -> player.sendMessage("    Vashta Nerada: " + value);
                            case EVOKER_FANGS -> player.sendMessage("    K9: " + value);
                            case FALLING_BLOCK -> player.sendMessage("    Empty Child: " + value);
                            case FIREWORK_ROCKET -> player.sendMessage("    Sontaran: " + value);
                            case FISHING_BOBBER -> player.sendMessage("    Zygon: " + value);
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
                            case EXPERIENCE_BOTTLE -> player.sendMessage("    Sea Devil: " + value);
                            default -> player.sendMessage("    " + ent + ": " + value + m);
                        }
                    }, 3L);
                });
                scannedEntities.clear();
            } else {
                plugin.getMessenger().handlesSend(player, "SCAN_NONE");
            }
            // damage the circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.scanner") > 0) {
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
