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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Scanner consists of a collection of thousands of instruments designed to
 * gather information about the environment outside a Chief among these is the
 * visual signal, which is displayed on the Scanner Screen found in any of the
 * Control Rooms.
 *
 * @author eccentric_nz
 */
public class TARDISScanner {

    private final TARDIS plugin;

    public TARDISScanner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static List<Entity> getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        List<Entity> radiusEntities = new ArrayList<>();
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities;
    }

    public TARDISScannerData getScannerData(Player player, int id, BukkitScheduler bsched) {
        TARDISScannerData data = new TARDISScannerData();
        TARDISSounds.playTARDISSound(player.getLocation(), "tardis_scanner");
        Location scan_loc;
        String whereIsIt;
        COMPASS tardisDirection;
        HashMap<String, Object> wherenl = new HashMap<>();
        wherenl.put("tardis_id", id);
        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
            if (!rsn.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NEXT_NOT_FOUND");
                return null;
            }
            scan_loc = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
            tardisDirection = rsn.getDirection();
            whereIsIt = plugin.getLanguage().getString("SCAN_NEXT");
        } else {
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherenl);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return null;
            }
            scan_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            tardisDirection = rsc.getDirection();
            whereIsIt = plugin.getLanguage().getString("SCAN_CURRENT");
        }
        data.setScanLocation(scan_loc);
        data.setTardisDirection(tardisDirection);
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
                if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
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
                    if (et.equals(EntityType.ENDERMAN) && !k.getPassengers().isEmpty() && k.getPassengers().get(0) != null && k.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
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
        }
        if (TARDISPermission.hasPermission(player, "tardis.scanner.map")) {
            // is there a scanner map item frame?
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 37);
            ResultSetControls rs = new ResultSetControls(plugin, where, true);
            if (rs.resultSet()) {
                // get the item frame
                Location mapFrame = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
                if (mapFrame != null) {
                    while (!mapFrame.getChunk().isLoaded()) {
                        mapFrame.getChunk().load();
                    }
                }
                ItemFrame itemFrame = null;
                for (Entity e : mapFrame.getWorld().getNearbyEntities(mapFrame, 1.0d, 1.0d, 1.0d)) {
                    if (e instanceof ItemFrame) {
                        itemFrame = (ItemFrame) e;
                        break;
                    }
                }
                if (itemFrame != null) {
                    if (itemFrame.getItem().getType() == Material.FILLED_MAP) {
                        new TARDISScannerMap(plugin, scan_loc, itemFrame).setMap();
                    }
                }
            }
        }
        long time = scan_loc.getWorld().getTime();
        data.setTime(time);
        String dayNight = TARDISStaticUtils.getTime(time);
        // message the player
        plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_RESULT", whereIsIt);
        String worldName = scan_loc.getWorld().getName();
        String alias;
        if (!plugin.getPlanetsConfig().getBoolean("planets." + worldName + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
            alias = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            alias = TARDISAliasResolver.getWorldAlias(scan_loc.getWorld());
        }
        plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_WORLD", alias);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_DIRECTION", tardisDirection.toString()), 20L);
        // get biome
        String biome = scan_loc.getBlock().getBiome().toString();
        data.setScannedBiome(biome);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_TYPE", biome), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_TIME", dayNight + " / " + time), 60L);
        // get weather
        String weather = switch (biome) {
            case "DESERT", "DESERT_HILLS", "DESERT_LAKES", "SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU", "BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU" ->
                plugin.getLanguage().getString("WEATHER_DRY");
            case "SNOWY_TUNDRA", "ICE_SPIKES", "FROZEN_OCEAN", "FROZEN_RIVER", "SNOWY_BEACH", "SNOWY_TAIGA", "SNOWY_MOUNTAINS", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS" ->
                (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
            default ->
                (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
        };
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            if (!scannedEntities.isEmpty()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_ENTS");
                scannedEntities.forEach((ent, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (ent.equals(EntityType.PLAYER) && !playerNames.isEmpty()) {
                        playerNames.forEach((p) -> buf.append(", ").append(p));
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
            // damage the circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.scanner") > 0) {
                TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
                // decrement uses
                int uses_left = tcc.getScannerUses();
                new TARDISCircuitDamager(plugin, DiskCircuit.SCANNER, uses_left, id, player).damage();
            }
        }, 140L);
        return data;
    }

    public void scan(int id, Player player, String renderer, int level) {
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasScanner()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_MISSING");
            return;
        }
        if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCAN_NO_RANDOM");
            return;
        }
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        TARDISScannerData data = getScannerData(player, id, scheduler);
        if (data != null) {
            boolean extrend = true;
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            if (rsp.resultSet()) {
                extrend = rsp.isRendererOn();
            }
            if (!renderer.isEmpty() && extrend) {
                int required = plugin.getArtronConfig().getInt("render");
                if (level > required) {
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        if (player.isOnline() && plugin.getUtils().inTARDISWorld(player)) {
                            TARDISExteriorRenderer ter = new TARDISExteriorRenderer(plugin);
                            ter.render(renderer, data.getScanLocation(), id, player, data.getTardisDirection(), data.getTime(), data.getScannedBiome());
                        }
                    }, 160L);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_RENDER");
                }
            }
        }
    }
}
