/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.planets.TARDISAngelsAPI;
import me.eccentric_nz.TARDIS.utility.TARDISDalekDisguiser;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.EntityEquipment;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISMonsterRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<EntityType> monsters = new ArrayList<>();
    private final List<EntityType> random_monsters = new ArrayList<>();

    public TARDISMonsterRunnable(TARDIS plugin) {
        this.plugin = plugin;
        monsters.add(EntityType.CAVE_SPIDER);
        monsters.add(EntityType.CREEPER);
        monsters.add(EntityType.ENDERMAN);
        monsters.add(EntityType.ENDERMITE);
        monsters.add(EntityType.HUSK);
        monsters.add(EntityType.PIG_ZOMBIE);
        monsters.add(EntityType.SILVERFISH);
        monsters.add(EntityType.SKELETON);
        monsters.add(EntityType.SLIME);
        monsters.add(EntityType.SPIDER);
        monsters.add(EntityType.STRAY);
        monsters.add(EntityType.VEX);
        monsters.add(EntityType.VINDICATOR);
        monsters.add(EntityType.WITCH);
        monsters.add(EntityType.WITHER_SKELETON);
        monsters.add(EntityType.ZOMBIE);
        monsters.add(EntityType.ZOMBIE_VILLAGER);
        if (this.plugin.getConfig().getBoolean("allow.guardians")) {
            monsters.add(EntityType.GUARDIAN);
        } else {
            random_monsters.add(EntityType.GUARDIAN);
        }
        random_monsters.addAll(monsters);
    }

    @Override
    public void run() {
        // get open portals
        for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
            // only portals in police box worlds
            if (!map.getKey().getWorld().getName().contains("TARDIS") && !map.getValue().isAbandoned()) {
                // only police boxes that are not hidden
                boolean hidden = new ResultSetHidden(plugin, map.getValue().getTardisId()).isVisible();
                if (!hidden) {
                    Entity ent = map.getKey().getWorld().spawnEntity(map.getKey(), EntityType.EXPERIENCE_ORB);
                    List<Entity> entities = ent.getNearbyEntities(16, 16, 16);
                    ent.remove();
                    boolean found = false;
                    if (entities.size() > 0) {
                        // check if a Time Lord or companion is near
                        boolean take_action = true;
                        for (Entity e : entities) {
                            if (e instanceof Player && isTimelord(map.getValue(), e)) {
                                take_action = false;
                                break;
                            }
                        }
                        // nobody there so continue
                        if (take_action) {
                            boolean twa = plugin.getPM().isPluginEnabled("TARDISWeepingAngels");
                            for (Entity e : entities) {
                                EntityType type = e.getType();
                                TARDISMonster tm = new TARDISMonster();
                                String dn = WordUtils.capitalize(type.toString().toLowerCase(Locale.ENGLISH));
                                if (monsters.contains(type)) {
                                    found = true;
                                    switch (type) {
                                        case CREEPER:
                                            Creeper creeper = (Creeper) e;
                                            tm.setCharged(creeper.isPowered());
                                            dn = (creeper.isPowered()) ? "Charged Creeper" : "Creeper";
                                            break;
                                        case ENDERMAN:
                                            Enderman enderman = (Enderman) e;
                                            tm.setCarried(enderman.getCarriedBlock());
                                            if (twa && e.getPassengers().size() > 0 && e.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
                                                dn = "Silent";
                                            }
                                            break;
                                        case PIG_ZOMBIE:
                                            PigZombie pigzombie = (PigZombie) e;
                                            tm.setAggressive(pigzombie.isAngry());
                                            tm.setAnger(pigzombie.getAnger());
                                            tm.setEquipment(pigzombie.getEquipment());
                                            if (twa && pigzombie.getEquipment().getHelmet() != null && pigzombie.getEquipment().getHelmet().hasItemMeta() && pigzombie.getEquipment().getHelmet().getItemMeta().hasDisplayName()) {
                                                String name = pigzombie.getEquipment().getHelmet().getItemMeta().getDisplayName();
                                                if (name.equals("Ice Warrior Head") || name.equals("Strax Head")) {
                                                    dn = name.substring(0, name.length() - 5);
                                                }
                                            } else {
                                                dn = "Pig Zombie";
                                            }
                                            break;
                                        case SKELETON:
                                        case STRAY:
                                        case WITHER_SKELETON:
                                            Skeleton skeleton = (Skeleton) e;
                                            tm.setEquipment(skeleton.getEquipment());
                                            if (twa && skeleton.getEquipment().getHelmet() != null && skeleton.getEquipment().getHelmet().hasItemMeta() && skeleton.getEquipment().getHelmet().getItemMeta().hasDisplayName()) {
                                                String name = skeleton.getEquipment().getHelmet().getItemMeta().getDisplayName();
                                                if (name.equals("Dalek Head") || name.equals("Silurian Head") || name.equals("Weeping Angel Head")) {
                                                    dn = name.substring(0, name.length() - 5);
                                                }
                                            }
                                            if (type.equals(EntityType.WITHER_SKELETON)) {
                                                dn = "Wither Skeleton";
                                            }
                                            break;
                                        case SLIME:
                                            Slime slime = (Slime) e;
                                            tm.setSize(slime.getSize());
                                            break;
                                        case VINDICATOR:
                                            Vindicator vindicator = (Vindicator) e;
                                            tm.setEquipment(vindicator.getEquipment());
                                            break;
                                        case HUSK:
                                        case ZOMBIE:
                                        case ZOMBIE_VILLAGER:
                                            Zombie zombie = (Zombie) e;
                                            tm.setBaby(zombie.isBaby());
                                            tm.setEquipment(zombie.getEquipment());
                                            if (twa && zombie.getEquipment().getHelmet() != null && zombie.getEquipment().getHelmet().hasItemMeta() && zombie.getEquipment().getHelmet().getItemMeta().hasDisplayName()) {
                                                String name = zombie.getEquipment().getHelmet().getItemMeta().getDisplayName();
                                                if (name.equals("Cyberman Head") || name.equals("Empty Child Head") || name.equals("Sontaran Head") || name.equals("Vashta Nerada Head") || name.equals("Zygon Head")) {
                                                    dn = name.substring(0, name.length() - 5);
                                                }
                                            }
                                            if (type.equals(EntityType.ZOMBIE_VILLAGER)) {
                                                ZombieVillager zombie_villager = (ZombieVillager) e;
                                                Profession prof = zombie_villager.getVillagerProfession();
                                                tm.setProfession(prof);
                                                dn = "Zombie Villager";
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    tm.setDisplayName(dn);
                                    tm.setType(type);
                                    tm.setAge(e.getTicksLived());
                                    tm.setHealth(((LivingEntity) e).getHealth());
                                    tm.setName(e.getCustomName());
                                    if (e.getPassengers().size() > 0) {
                                        tm.setPassenger(e.getPassengers().get(0).getType());
                                    }
                                    moveMonster(map.getValue(), tm, e, type.equals(EntityType.GUARDIAN));
                                }
                            }
                        }
                    }
                    if (!found && plugin.getConfig().getBoolean("preferences.spawn_random_monsters")) {
                        // spawn a random mob inside TARDIS?
                        // 25% chance + must not be peaceful, a Mooshroom biome or WG mob-spawning: deny
                        if (TARDISConstants.RANDOM.nextInt(4) == 0 && canSpawn(map.getKey(), TARDISConstants.RANDOM.nextInt(4))) {
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", map.getValue().getTardisId());
                            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
                            if (rs.resultSet() && rs.getTardis().getMonsters() < plugin.getConfig().getInt("preferences.spawn_limit")) {
                                TARDISMonster rtm = new TARDISMonster();
                                // choose a random monster
                                EntityType type = random_monsters.get(TARDISConstants.RANDOM.nextInt(random_monsters.size()));
                                rtm.setType(type);
                                String dn = WordUtils.capitalize(type.toString().toLowerCase(Locale.ENGLISH));
                                if (type.equals(EntityType.ZOMBIE_VILLAGER)) {
                                    Profession prof = Profession.values()[TARDISConstants.RANDOM.nextInt(7)];
                                    rtm.setProfession(prof);
                                    dn = "Zombie " + WordUtils.capitalize(prof.toString().toLowerCase(Locale.ENGLISH));
                                }
                                rtm.setDisplayName(dn);
                                moveMonster(map.getValue(), rtm, null, type.equals(EntityType.GUARDIAN));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canSpawn(Location l, int r) {
        // get biome
        Biome biome = l.getBlock().getRelative(plugin.getGeneralKeeper().getFaces().get(r), 2).getBiome();
        if (biome.equals(Biome.MUSHROOM_FIELDS) || biome.equals(Biome.MUSHROOM_FIELD_SHORE)) {
            return false;
        }
        // worldguard
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().mobsCanSpawnAtLocation(l)) {
            return false;
        }
        // difficulty
        return !l.getWorld().getDifficulty().equals(Difficulty.PEACEFUL);
    }

    private void moveMonster(TARDISTeleportLocation tpl, TARDISMonster m, Entity e, boolean guardian) {
        Location loc = null;
        if (guardian) {
            // check for pool
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", tpl.getTardisId());
            ResultSetARS rsa = new ResultSetARS(plugin, wherea);
            if (rsa.resultSet()) {
                int l = 0, r = 0, c = 0;
                // check there is a pool
                String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                for (String[][] level : json) {
                    for (String[] row : level) {
                        for (String col : row) {
                            if (col.equals("SNOW_BLOCK")) {
                                // need to get the console location - will be different for non-TIPS TARDISes
                                HashMap<String, Object> wheret = new HashMap<>();
                                wheret.put("tardis_id", tpl.getTardisId());
                                ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
                                if (rs.resultSet()) {
                                    int pos = rs.getTardis().getTIPS();
                                    int tx = 0, tz = 0;
                                    if (pos != -1) {
                                        // tips slot
                                        TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
                                        TARDISTIPSData coords = tips.getTIPSData(pos);
                                        tx = coords.getCentreX();
                                        tz = coords.getCentreZ();
                                    }
                                    int x = tx + ((r - 4) * 16) + 8;
                                    int y = 51 + (l * 16);
                                    int z = tz + ((c - 4) * 16) + 8;
                                    loc = new Location(tpl.getLocation().getWorld(), x, y, z);
                                    break;
                                }
                            }
                            c++;
                        }
                        c = 0;
                        r++;
                    }
                    r = 0;
                    l++;
                }
            }
        } else {
            loc = tpl.getLocation();
        }
        if (loc != null) {
            // remove the entity
            if (e != null) {
                e.remove();
            }
            // if there are players in the TARDIS sound the cloister bell
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", tpl.getTardisId());
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                TARDISSounds.playTARDISSound(loc, "tardis_cloister_bell");
            } else {
                // else message the Time Lord
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", tpl.getTardisId());
                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 2);
                if (rst.resultSet()) {
                    Player p = plugin.getServer().getPlayer(rst.getTardis().getUuid());
                    if (p != null) {
                        TARDISMessage.send(p, "MONSTER", m.getDisplayName());
                    }
                }
                HashMap<String, Object> wherer = new HashMap<>();
                wherer.put("tardis_id", rst.getTardis().getTardis_id());
                wherer.put("type", 5);
                wherer.put("secondary", 0);
                ResultSetControls rsc = new ResultSetControls(plugin, wherer, false);
                if (rsc.resultSet()) {
                    // move the location to the y-repeater
                    loc = TARDISStaticLocationGetters.getLocationFromDB(rsc.getLocation());
                    loc.add(0.5d, 0.125d, 0.5d);
                }
            }
            // load the chunk
            while (!loc.getChunk().isLoaded()) {
                loc.getChunk().load();
            }
            // spawn a monster in the TARDIS
            plugin.setTardisSpawn(true);
            Entity ent = loc.getWorld().spawnEntity(loc, m.getType());
            switch (m.getType()) {
                case CREEPER:
                    Creeper creeper = (Creeper) ent;
                    creeper.setPowered(m.isCharged());
                    break;
                case ENDERMAN:
                    Enderman enderman = (Enderman) ent;
                    if (m.getCarried() != null) {
                        enderman.setCarriedBlock(m.getCarried());
                    }
                    break;
                case PIG_ZOMBIE:
                    PigZombie pigzombie = (PigZombie) ent;
                    pigzombie.setAngry(m.isAggressive());
                    pigzombie.setAnger(m.getAnger());
                    EntityEquipment ep = pigzombie.getEquipment();
                    if (m.getEquipment() != null) {
                        ep.setArmorContents(m.getEquipment().getArmorContents());
                        ep.setItemInMainHand(m.getEquipment().getItemInMainHand());
                    }
                    break;
                case SKELETON:
                case STRAY:
                case WITHER_SKELETON:
                    Skeleton skeleton = (Skeleton) ent;
                    EntityEquipment es = skeleton.getEquipment();
                    if (m.getEquipment() != null) {
                        es.setArmorContents(m.getEquipment().getArmorContents());
                        es.setItemInMainHand(m.getEquipment().getItemInMainHand());
                        if (m.getEquipment().getHelmet().getType().equals(Material.VINE) && plugin.getPM().isPluginEnabled("LibsDisguises")) {
                            TARDISDalekDisguiser.dalekanium(skeleton);
                        }
                    }
                    break;
                case SLIME:
                    Slime slime = (Slime) ent;
                    if (m.getSize() > 0) {
                        slime.setSize(m.getSize());
                    }
                    break;
                case VINDICATOR:
                    Vindicator vindicator = (Vindicator) ent;
                    EntityEquipment ev = vindicator.getEquipment();
                    if (m.getEquipment() != null) {
                        ev.setArmorContents(m.getEquipment().getArmorContents());
                        ev.setItemInMainHand(m.getEquipment().getItemInMainHand());
                    }
                    break;
                case HUSK:
                case ZOMBIE:
                    Zombie zombie = (Zombie) ent;
                    zombie.setBaby(m.isBaby());
                    EntityEquipment ez = zombie.getEquipment();
                    if (m.getEquipment() != null) {
                        ez.setArmorContents(m.getEquipment().getArmorContents());
                        ez.setItemInMainHand(m.getEquipment().getItemInMainHand());
                    }
                    break;
                case ZOMBIE_VILLAGER:
                    ZombieVillager zombie_villager = (ZombieVillager) ent;
                    zombie_villager.setBaby(m.isBaby());
                    if (m.getProfession() != null) {
                        zombie_villager.setVillagerProfession(m.getProfession());
                    }
                    EntityEquipment zv = zombie_villager.getEquipment();
                    if (m.getEquipment() != null) {
                        zv.setArmorContents(m.getEquipment().getArmorContents());
                        zv.setItemInMainHand(m.getEquipment().getItemInMainHand());
                    }
                    break;
                default:
                    break;
            }
            if (m.getAge() > 0) {
                ent.setTicksLived(m.getAge());
            }
            if (m.getHealth() > 0 && m.getHealth() <= 20.0d) {
                ((LivingEntity) ent).setHealth(m.getHealth());
            }
            if (m.getName() != null && !m.getName().isEmpty()) {
                ent.setCustomName(m.getName());
            }
            if (m.getPassenger() != null) {
                if (plugin.getPM().isPluginEnabled("TARDISWeepingAngels") && m.getPassenger().equals(EntityType.GUARDIAN)) {
                    TARDISAngelsAPI.getAPI(plugin).setSilentEquipment((LivingEntity) ent);
                } else {
                    Entity passenger = loc.getWorld().spawnEntity(loc, m.getPassenger());
                    ent.addPassenger(passenger);
                }
            }
        }
    }

    private boolean isTimelord(TARDISTeleportLocation tpl, Entity e) {
        ResultSetCompanions rsc = new ResultSetCompanions(plugin, tpl.getTardisId());
        return (rsc.getCompanions().contains(e.getUniqueId()));
    }
}
