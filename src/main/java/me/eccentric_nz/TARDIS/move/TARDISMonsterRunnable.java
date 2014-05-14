/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMonsterRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<EntityType> monsters = new ArrayList<EntityType>();
    private final List<SCHEMATIC> tardii = new ArrayList<SCHEMATIC>();

    public TARDISMonsterRunnable(TARDIS plugin) {
        this.plugin = plugin;
        monsters.add(EntityType.CAVE_SPIDER);
        monsters.add(EntityType.CREEPER);
        monsters.add(EntityType.ENDERMAN);
        monsters.add(EntityType.PIG_ZOMBIE);
        monsters.add(EntityType.SILVERFISH);
        monsters.add(EntityType.SKELETON);
        monsters.add(EntityType.SLIME);
        monsters.add(EntityType.SPIDER);
        monsters.add(EntityType.WITCH);
        monsters.add(EntityType.ZOMBIE);
        tardii.add(SCHEMATIC.BIGGER);
        tardii.add(SCHEMATIC.DELUXE);
        tardii.add(SCHEMATIC.ELEVENTH);
        tardii.add(SCHEMATIC.REDSTONE);
    }

    @Override
    public void run() {
        plugin.debug("Checking for open doors...");
        // get open portals
        for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
            // only portals in police box worlds
            if (!map.getKey().getWorld().getName().contains("TARDIS")) {
                plugin.debug("Outer door open!");
                Entity ent = map.getKey().getWorld().spawnEntity(map.getKey(), EntityType.EXPERIENCE_ORB);
                List<Entity> entities = ent.getNearbyEntities(16, 16, 16);
                ent.remove();
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
                        plugin.debug("Attempting to find nearby monsters...");
                        for (Entity e : entities) {
                            EntityType type = e.getType();
                            TARDISMonster tm = new TARDISMonster();
                            if (monsters.contains(type)) {
                                switch (type) {
                                    case CREEPER:
                                        Creeper creeper = (Creeper) e;
                                        tm.setCharged(creeper.isPowered());
                                        break;
                                    case ENDERMAN:
                                        Enderman enderman = (Enderman) e;
                                        tm.setCarried(enderman.getCarriedMaterial());
                                        break;
                                    case PIG_ZOMBIE:
                                        PigZombie pigzombie = (PigZombie) e;
                                        tm.setAggressive(pigzombie.isAngry());
                                        tm.setAnger(pigzombie.getAnger());
                                        tm.setEquipment(pigzombie.getEquipment());
                                        break;
                                    case SKELETON:
                                        Skeleton skeleton = (Skeleton) e;
                                        tm.setEquipment(skeleton.getEquipment());
                                        break;
                                    case SLIME:
                                        Slime slime = (Slime) e;
                                        tm.setSize(slime.getSize());
                                        break;
                                    case ZOMBIE:
                                        Zombie zombie = (Zombie) e;
                                        tm.setVillager(zombie.isVillager());
                                        tm.setBaby(zombie.isBaby());
                                        tm.setEquipment(zombie.getEquipment());
                                        break;
                                }
                                tm.setType(type);
                                tm.setAge(e.getTicksLived());
                                tm.setHealth(((LivingEntity) e).getHealth());
                                tm.setName(((LivingEntity) e).getCustomName());
                                moveMonster(map.getValue(), tm, e);
                            }
                        }
                    }
                } else {
                    // spawn a random mob inside TARDIS?
                    Random r = new Random();
                    // 25% chance + must not be peaceful, a Mooshroom biome or WG mob-spawning: deny
                    if (r.nextInt(4) == 0 && canSpawn(map.getKey(), r)) {
                        plugin.debug("Spawning a random mob!");
                        TARDISMonster rtm = new TARDISMonster();
                        // choose a random monster
                        rtm.setType(monsters.get(r.nextInt(monsters.size())));
                        moveMonster(map.getValue(), rtm, null);
                    }
                }
            }
        }
    }

    private boolean canSpawn(Location l, Random r) {
        // get biome
        Biome biome = l.getBlock().getRelative(plugin.getGeneralKeeper().getFaces().get(r.nextInt(4)), 2).getBiome();
        if (biome.equals(Biome.MUSHROOM_ISLAND) || biome.equals(Biome.MUSHROOM_SHORE)) {
            return false;
        }
        // worldguard
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().mobsCanSpawnAtLocation(l)) {
            return false;
        }
        // difficulty
        return !l.getWorld().getDifficulty().equals(Difficulty.PEACEFUL);
    }

    private void moveMonster(TARDISTeleportLocation tpl, TARDISMonster m, Entity e) {
        // remove the entity
        if (e != null) {
            plugin.debug("Found a monster :)");
            e.remove();
        }
        Location l = tpl.getLocation();
        // if there are players in the TARDIS sound the cloister bell
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", tpl.getTardisId());
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
        if (rs.resultSet()) {
            plugin.getUtils().playTARDISSoundNearby(l, "tardis_cloister_bell");
        } else {
            // else message the Time Lord
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", tpl.getTardisId());
            ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
            if (rst.resultSet()) {
                Player p = plugin.getServer().getPlayer(rst.getUuid());
                if (p != null) {
                    TARDISMessage.send(p, plugin.getPluginName() + "A " + m.getType().toString() + " entered the TARDIS!");
                }
            }
            HashMap<String, Object> wherer = new HashMap<String, Object>();
            wherer.put("tardis_id", rst.getTardis_id());
            wherer.put("type", 5);
            ResultSetControls rsc = new ResultSetControls(plugin, wherer, false);
            if (rsc.resultSet()) {
                // move the location to the y-repeater
                l = plugin.getUtils().getLocationFromDB(rsc.getLocation(), 0.0f, 0.0f);
                l.setY(l.getY() + 0.125d);
            }
        }
        // load the chunk
        while (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        // spawn a monster in the TARDIS
        plugin.setMySpawn(true);
        Entity ent = l.getWorld().spawnEntity(l, m.getType());
        switch (m.getType()) {
            case CREEPER:
                Creeper creeper = (Creeper) ent;
                creeper.setPowered(m.isCharged());
                break;
            case ENDERMAN:
                Enderman enderman = (Enderman) ent;
                if (m.getCarried() != null) {
                    enderman.setCarriedMaterial(m.getCarried());
                }
                break;
            case PIG_ZOMBIE:
                PigZombie pigzombie = (PigZombie) ent;
                pigzombie.setAngry(m.isAggressive());
                pigzombie.setAnger(m.getAnger());
                EntityEquipment ep = pigzombie.getEquipment();
                ep.setArmorContents(m.getEquipment().getArmorContents());
                ep.setItemInHand(m.getEquipment().getItemInHand());
                break;
            case SKELETON:
                Skeleton skeleton = (Skeleton) ent;
                EntityEquipment es = skeleton.getEquipment();
                es.setArmorContents(m.getEquipment().getArmorContents());
                es.setItemInHand(m.getEquipment().getItemInHand());
                break;
            case SLIME:
                Slime slime = (Slime) ent;
                slime.setSize(m.getSize());
                break;
            case ZOMBIE:
                Zombie zombie = (Zombie) ent;
                zombie.setVillager(m.isVillager());
                zombie.setBaby(m.isBaby());
                EntityEquipment ez = zombie.getEquipment();
                ez.setArmorContents(m.getEquipment().getArmorContents());
                ez.setItemInHand(m.getEquipment().getItemInHand());
                break;
        }
        if (m.getAge() > 0) {
            ((LivingEntity) ent).setTicksLived(m.getAge());
        }
        if (m.getHealth() > 0) {
            ((LivingEntity) ent).setHealth(m.getHealth());
        }
        if (m.getName() != null && !m.getName().isEmpty()) {
            ((LivingEntity) ent).setCustomName(m.getName());
        }
    }

    private boolean isTimelord(TARDISTeleportLocation tpl, Entity e) {
        ResultSetCompanions rsc = new ResultSetCompanions(plugin, tpl.getTardisId());
        return (rsc.getCompanions().contains(((Player) e).getUniqueId()));
    }
}
