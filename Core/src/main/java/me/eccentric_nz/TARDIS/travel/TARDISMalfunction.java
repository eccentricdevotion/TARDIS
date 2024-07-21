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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.builders.LightLevel;
import me.eccentric_nz.TARDIS.control.SensorToggle;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The Dalek Asylum was a snowy and mountainous planet used by the Daleks as a prison and "dumping ground" for those
 * among them who had malfunctioned, gone insane and/or become mentally scarred by battles. The sane Daleks left their
 * insane fellows in the Asylum rather than kill them because they epitomised the Dalek concept of beauty: pure hatred.
 *
 * @author eccentric_nz
 */
public class TARDISMalfunction {

    private final TARDIS plugin;

    public TARDISMalfunction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static void handleSensor(int id) {
        ResultSetSensors rss = new ResultSetSensors(TARDIS.plugin, id);
        if (rss.resultSet()) {
            SensorToggle toggle = new SensorToggle();
            Block block = toggle.getBlock(rss.getSensors().getMalfunction());
            if (block != null) {
                toggle.setState(block);
            }
        }
    }

    public boolean isMalfunction() {
        boolean mal = false;
        if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
            int chance = 100 - plugin.getConfig().getInt("preferences.malfunction");
            if (TARDISConstants.RANDOM.nextInt(100) > chance) {
                mal = true;
            }
        }
        return mal;
    }

    public Location getMalfunction(int id, Player p, COMPASS dir, Location handbrake_loc, String eps, String creeper) {
        Location l;
        // get current TARDIS preset location
        ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, id);
        if (rscl.resultSet()) {
            Location cl = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
            int end = 100 - plugin.getConfig().getInt("preferences.malfunction_end");
            int nether = end - plugin.getConfig().getInt("preferences.malfunction_nether");
            int r = TARDISConstants.RANDOM.nextInt(100);
            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
            int x = TARDISConstants.RANDOM.nextInt(4) + 1;
            int z = TARDISConstants.RANDOM.nextInt(4) + 1;
            int y = TARDISConstants.RANDOM.nextInt(4) + 1;
            if (r > end) {
                // get random the_end location
                l = tt.randomDestination(p, x, z, y, dir, "THE_END", null, true, cl);
            } else if (r > nether) {
                // get random nether location
                l = tt.randomDestination(p, x, z, y, dir, "NETHER", null, true, cl);
            } else {
                // get random normal location
                l = tt.randomDestination(p, x, z, y, dir, "NORMAL", null, false, cl);
            }
        } else {
            l = null;
        }
        if (l != null) {
            doMalfunction(id, p, eps, creeper, handbrake_loc);
        }
        return l;
    }

    private void doMalfunction(int id, Player p, String eps, String creeper, Location handbrake) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, where, true);
        if (rsl.resultSet()) {
            // should we damage circuits?
            int malfunctionDamage = plugin.getConfig().getInt("circuits.malfunction_damage");
            if (plugin.getConfig().getBoolean("circuits.damage") && malfunctionDamage > 0 && !plugin.getConfig().getBoolean("difficulty.circuits")) {
                // choose a random circuit
                DiskCircuit circuit = DiskCircuit.getTardisCircuits().get(TARDISConstants.RANDOM.nextInt(DiskCircuit.getTardisCircuits().size()));
                // is the circuit in the advanced console?
                TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
                int damage;
                int usesLeft;
                double percent = malfunctionDamage / 100.0d;
                switch (circuit) {
                    case ARS -> {
                        if (tcc.hasARS()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.ars") * percent);
                            usesLeft = tcc.getArsUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case CHAMELEON -> {
                        if (tcc.hasChameleon()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.chameleon") * percent);
                            usesLeft = tcc.getChameleonUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case INPUT -> {
                        if (tcc.hasInput()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.input") * percent);
                            usesLeft = tcc.getInputUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case INVISIBILITY -> {
                        if (tcc.hasInvisibility()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.invisibility") * percent);
                            usesLeft = tcc.getInvisibilityUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case MATERIALISATION -> {
                        if (tcc.hasMaterialisation()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.materialisation") * percent);
                            usesLeft = tcc.getMaterialisationUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case MEMORY -> {
                        if (tcc.hasMemory()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.memory") * percent);
                            usesLeft = tcc.getMemoryUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case RANDOMISER -> {
                        if (tcc.hasRandomiser()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.randomiser") * percent);
                            usesLeft = tcc.getRandomiserUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case SCANNER -> {
                        if (tcc.hasScanner()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.scanner") * percent);
                            usesLeft = tcc.getScannerUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    case TEMPORAL -> {
                        if (tcc.hasTemporal()) {
                            damage = (int) (plugin.getConfig().getDouble("circuits.uses.temporal") * percent);
                            usesLeft = tcc.getTemporalUses() - damage;
                            damage(circuit, usesLeft, id, p);
                        }
                    }
                    default -> {
                    }
                }
            }
            // always a chance that a capacitor will be damaged
            ResultSetArtronStorage rsa = new ResultSetArtronStorage(plugin);
            if (rsa.fromID(id)) {
                int c = rsa.getCapacitorCount();
                int d = rsa.getDamageCount();
                if (d < c && TARDISConstants.RANDOM.nextInt(100) < plugin.getArtronConfig().getInt("malfunction_damage")) {
                    d++;
                    HashMap<String, Object> setd = new HashMap<>();
                    setd.put("damaged", d);
                    HashMap<String, Object> whered = new HashMap<>();
                    setd.put("tardis_id", id);
                    plugin.getQueryFactory().doUpdate("eyes", setd, whered);
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "CAPACITOR_DAMAGE");
                }
            }
            // get player prefs
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            if (rsp.resultSet()) {
                if (plugin.getConfig().getBoolean("allow.emergency_npc") && rsp.isEpsOn()) {
                    // schedule the NPC to appear
                    String message = "This is Emergency Programme One. Now listen, this is important. If this message is activated, then it can only mean one thing: we must be in danger, and I mean fatal. You're about to die any second with no chance of escape.";
                    HashMap<String, Object> wherev = new HashMap<>();
                    wherev.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                    List<UUID> playerUUIDs;
                    if (rst.resultSet()) {
                        playerUUIDs = rst.getData();
                    } else {
                        playerUUIDs = new ArrayList<>();
                        playerUUIDs.add(p.getUniqueId());
                    }
                    TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, message, p, playerUUIDs, id, eps, creeper);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 220L);
                }
                TardisLight light = TardisLight.TENTH;
                TARDISDisplayItem tdi = TARDISDisplayItem.LIGHT_TENTH_ON;
                ItemDisplay display = TARDISDisplayItemUtils.get(rsl.getData().get(0));
                if (display != null) {
                    tdi = TARDISDisplayItem.getByItemDisplay(display);
                    light = TardisLight.getFromDisplayItem(tdi);
                }
                ResultSetInteriorLightLevel rs = new ResultSetInteriorLightLevel(plugin, id);
                int level = rs.resultSet() ? LightLevel.interior_level[rs.getLevel()] : 15;
                // flicker lights
                long end = System.currentTimeMillis() + 10000;
                TARDISLampsRunnable runnable = new TARDISLampsRunnable(plugin, rsl.getData(), end, light, light.getOn() == tdi, level);
                runnable.setHandbrake(handbrake);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 10L);
                runnable.setTask(taskID);
                // add fireworks
                ResultSetInteractionCheck rsic = new ResultSetInteractionCheck(plugin);
                TARDISMalfunctionExplosion explodeable = new TARDISMalfunctionExplosion(plugin, id, end, rsic.resultSetFromId(id));
                int taskEx = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, explodeable, 10L, 30L);
                explodeable.setTask(taskEx);
            }
            // toggle the malfunction sensor
            handleSensor(id);
        }
    }

    private void damage(DiskCircuit circuit, int uses_left, int id, Player p) {
        TARDISCircuitDamager tcd = new TARDISCircuitDamager(plugin, circuit, uses_left, id, p);
        tcd.damage();
    }
}
