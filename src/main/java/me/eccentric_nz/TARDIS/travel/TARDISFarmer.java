/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import static org.bukkit.entity.EntityType.OCELOT;
import static org.bukkit.entity.EntityType.WOLF;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Undefined Storage Holds make up most of a TARDIS's interior volume. Each Hold
 * has an identifying number.
 *
 * @author eccentric_nz
 */
public class TARDISFarmer {

    private final TARDIS plugin;

    public TARDISFarmer(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether there are any animals around the TARDIS Police Box. If
     * mobs are found they are teleported to the 'farm' room (if present),
     * otherwise a spawn egg for the mob type is placed in the player's
     * inventory. Only cows, sheep, pigs, chickens and mooshrooms will be
     * processed.
     *
     * Also allows players to teleport their pets (tamed wolves and ocelots)
     * with them.
     *
     * @param l The location to check for animals. This will be the current
     * location of the TARDIS Police Box.
     * @param d the direction the Police Box is facing
     * @param id The database key of the TARDIS.
     * @param p the player to award achievements or give spawn eggs to
     * @return a List of the player's pets (if any are nearby)
     */
    public List<TARDISMob> farmAnimals(Location l, COMPASS d, int id, Player p) {
        List<TARDISMob> old_macd_had_a_pet = new ArrayList<TARDISMob>();
        switch (d) {
            case NORTH:
                l.setZ(l.getZ() - 1);
                break;
            case WEST:
                l.setX(l.getX() - 1);
                break;
            case SOUTH:
                l.setZ(l.getZ() + 1);
                break;
            default:
                l.setX(l.getX() + 1);
                break;
        }
        l.setY(l.getY() + 1);
        // spawn an entity at this location so we can get nearby entities - an egg will do
        World w = l.getWorld();
        Entity ent = w.spawnEntity(l, EntityType.EGG);
        List<Entity> mobs = ent.getNearbyEntities(3.5D, 3.5D, 3.5D);
        if (mobs.size() > 0) {
            List<TARDISMob> old_macd_had_a_chicken = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_cow = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_pig = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_sheep = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_mooshroom = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_horse = new ArrayList<TARDISMob>();
            // are we doing an achievement?
            TARDISAchievementFactory taf = null;
            if (plugin.getAchivementConfig().getBoolean("farm.enabled")) {
                taf = new TARDISAchievementFactory(plugin, p, "farm", 5);
            }
            // count total mobs
            int total = 0;
            for (Entity e : mobs) {
                switch (e.getType()) {
                    case CHICKEN:
                        TARDISMob tmchk = new TARDISMob();
                        tmchk.setAge(e.getTicksLived());
                        tmchk.setBaby(!((Chicken) e).isAdult());
                        old_macd_had_a_chicken.add(tmchk);
                        e.remove();
                        if (taf != null) {
                            taf.doAchievement("CHICKEN");
                        }
                        total++;
                        break;
                    case COW:
                        TARDISMob tmcow = new TARDISMob();
                        tmcow.setAge(e.getTicksLived());
                        tmcow.setBaby(!((Cow) e).isAdult());
                        old_macd_had_a_cow.add(tmcow);
                        e.remove();
                        if (taf != null) {
                            taf.doAchievement("COW");
                        }
                        total++;
                        break;
                    case HORSE:
                        TARDISMob tmhor = new TARDISMob();
                        tmhor.setAge(e.getTicksLived());
                        tmhor.setBaby(!((Horse) e).isAdult());
                        old_macd_had_a_horse.add(tmhor);
                        e.remove();
                        if (taf != null) {
                            taf.doAchievement("HORSE");
                        }
                        total++;
                        break;
                    case PIG:
                        TARDISMob tmpig = new TARDISMob();
                        tmpig.setAge(e.getTicksLived());
                        tmpig.setBaby(!((Pig) e).isAdult());
                        old_macd_had_a_pig.add(tmpig);
                        e.remove();
                        if (taf != null) {
                            taf.doAchievement("PIG");
                        }
                        total++;
                        break;
                    case SHEEP:
                        TARDISMob tmshp = new TARDISMob();
                        tmshp.setAge(e.getTicksLived());
                        tmshp.setBaby(!((Sheep) e).isAdult());
                        tmshp.setColour(((Sheep) e).getColor());
                        old_macd_had_a_sheep.add(tmshp);
                        e.remove();
                        if (taf != null) {
                            taf.doAchievement("SHEEP");
                        }
                        total++;
                        break;
                    case MUSHROOM_COW:
                        TARDISMob tmshr = new TARDISMob();
                        tmshr.setAge(e.getTicksLived());
                        tmshr.setBaby(!((MushroomCow) e).isAdult());
                        old_macd_had_a_mooshroom.add(tmshr);
                        e.remove();
                        if (taf != null) {
                            taf.doAchievement("MUSHROOM_COW");
                        }
                        total++;
                        break;
                    case WOLF:
                    case OCELOT:
                        Tameable tamed = (Tameable) e;
                        if (tamed.isTamed() && tamed.getOwner().getName().equals(p.getName())) {
                            TARDISMob pet = new TARDISMob();
                            pet.setType(e.getType());
                            pet.setAge(e.getTicksLived());
                            String pet_name = ((LivingEntity) e).getCustomName();
                            if (pet_name != null) {
                                pet.setName(pet_name);
                            }
                            int health;
                            if (e.getType().equals(EntityType.WOLF)) {
                                pet.setSitting(((Wolf) e).isSitting());
                                pet.setColour(((Wolf) e).getCollarColor());
                                health = (((Wolf) e).getHealth() > 8) ? 8 : (int) ((Wolf) e).getHealth();
                                pet.setHealth(health);
                                pet.setBaby(!((Wolf) e).isAdult());
                            } else {
                                pet.setSitting(((Ocelot) e).isSitting());
                                pet.setCatType(((Ocelot) e).getCatType());
                                health = (((Ocelot) e).getHealth() > 8) ? 8 : (int) ((Ocelot) e).getHealth();
                                pet.setHealth(health);
                                pet.setBaby(!((Ocelot) e).isAdult());
                            }
                            old_macd_had_a_pet.add(pet);
                            e.remove();
                        }
                        break;
                    default:
                        break;
                }
            }
            // is there a farm room?
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                String farm = rs.getFarm();
                String stable = rs.getStable();
                if (!farm.isEmpty()) {
                    // get location of farm room
                    String[] data = farm.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = plugin.utils.parseNum(data[1]);
                    int y = plugin.utils.parseNum(data[2]) + 1;
                    int z = plugin.utils.parseNum(data[3]);
                    if (old_macd_had_a_chicken.size() > 0) {
                        Location chicken_pen = new Location(world, x + 3, y, z - 3);
                        while (!world.getChunkAt(chicken_pen).isLoaded()) {
                            world.getChunkAt(chicken_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_chicken) {
                            plugin.myspawn = true;
                            Entity chicken = world.spawnEntity(chicken_pen, EntityType.CHICKEN);
                            Chicken pecker = (Chicken) chicken;
                            pecker.setAge(e.getAge());
                            if (e.isBaby()) {
                                pecker.setBaby();
                            }
                        }
                    }
                    if (old_macd_had_a_cow.size() > 0) {
                        Location cow_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_cow) {
                            plugin.myspawn = true;
                            Entity cow = world.spawnEntity(cow_pen, EntityType.COW);
                            Cow moo = (Cow) cow;
                            moo.setAge(e.getAge());
                            if (e.isBaby()) {
                                moo.setBaby();
                            }
                        }
                    }
                    if (old_macd_had_a_pig.size() > 0) {
                        Location pig_pen = new Location(world, x - 3, y, z - 3);
                        while (!world.getChunkAt(pig_pen).isLoaded()) {
                            world.getChunkAt(pig_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_pig) {
                            plugin.myspawn = true;
                            Entity pig = world.spawnEntity(pig_pen, EntityType.PIG);
                            Pig oinker = (Pig) pig;
                            oinker.setAge(e.getAge());
                            if (e.isBaby()) {
                                oinker.setBaby();
                            }
                        }
                    }
                    if (old_macd_had_a_sheep.size() > 0) {
                        Location sheep_pen = new Location(world, x - 3, y, z + 3);
                        while (!world.getChunkAt(sheep_pen).isLoaded()) {
                            world.getChunkAt(sheep_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_sheep) {
                            plugin.myspawn = true;
                            Entity sheep = world.spawnEntity(sheep_pen, EntityType.SHEEP);
                            Sheep baa = (Sheep) sheep;
                            baa.setAge(e.getAge());
                            baa.setColor(e.getColour());
                            if (e.isBaby()) {
                                baa.setBaby();
                            }
                        }
                    }
                    if (old_macd_had_a_mooshroom.size() > 0) {
                        Location cow_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_mooshroom) {
                            plugin.myspawn = true;
                            Entity mooshroom = world.spawnEntity(cow_pen, EntityType.MUSHROOM_COW);
                            MushroomCow fungi = (MushroomCow) mooshroom;
                            fungi.setAge(e.getAge());
                            if (e.isBaby()) {
                                fungi.setBaby();
                            }
                        }
                    }
                } else if (!stable.isEmpty()) {
                    // get location of farm room
                    String[] data = stable.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = plugin.utils.parseNum(data[1]);
                    int y = plugin.utils.parseNum(data[2]) + 1;
                    int z = plugin.utils.parseNum(data[3]);
                    if (old_macd_had_a_horse.size() > 0) {
                        Location horse_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(horse_pen).isLoaded()) {
                            world.getChunkAt(horse_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_horse) {
                            plugin.myspawn = true;
                            Entity horse = world.spawnEntity(horse_pen, EntityType.HORSE);
                            Horse equine = (Horse) horse;
                            equine.setAge(e.getAge());
                            if (e.isBaby()) {
                                equine.setBaby();
                            }
                        }
                    }
                } else {
                    if (plugin.getConfig().getBoolean("spawn_eggs")) {
                        // no farm, give the player spawn eggs
                        Inventory inv = p.getInventory();
                        if (old_macd_had_a_chicken.size() > 0) {
                            ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_chicken.size(), (short) 93);
                            inv.addItem(is);
                        }
                        if (old_macd_had_a_cow.size() > 0) {
                            ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_cow.size(), (short) 92);
                            inv.addItem(is);
                        }
                        if (old_macd_had_a_pig.size() > 0) {
                            ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_pig.size(), (short) 90);
                            inv.addItem(is);
                        }
                        if (old_macd_had_a_sheep.size() > 0) {
                            ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_sheep.size(), (short) 91);
                            inv.addItem(is);
                        }
                        if (old_macd_had_a_mooshroom.size() > 0) {
                            ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_mooshroom.size(), (short) 96);
                            inv.addItem(is);
                        }
                        if (old_macd_had_a_horse.size() > 0) {
                            ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_mooshroom.size(), (short) 100);
                            inv.addItem(is);
                        }
                        p.updateInventory();
                    } else if (total > 0) {
                        p.sendMessage(plugin.pluginName + "You need to grow a farm room before you can farm mobs!");
                    }
                }
            }
        }
        ent.remove();
        return old_macd_had_a_pet;
    }

    public List<TARDISMob> exitPets(Player p) {
        List<TARDISMob> old_macd_had_a_pet = new ArrayList<TARDISMob>();
        Entity ent = (Entity) p;
        List<Entity> mobs = ent.getNearbyEntities(3.5D, 3.5D, 3.5D);
        for (Entity e : mobs) {
            switch (e.getType()) {
                case WOLF:
                case OCELOT:
                    Tameable tamed = (Tameable) e;
                    if (tamed.isTamed() && tamed.getOwner().getName().equals(p.getName())) {
                        TARDISMob pet = new TARDISMob();
                        pet.setType(e.getType());
                        pet.setAge(e.getTicksLived());
                        String pet_name = ((LivingEntity) e).getCustomName();
                        if (pet_name != null) {
                            pet.setName(pet_name);
                        }
                        int health;
                        if (e.getType().equals(EntityType.WOLF)) {
                            pet.setSitting(((Wolf) e).isSitting());
                            pet.setColour(((Wolf) e).getCollarColor());
                            health = (((Wolf) e).getHealth() > 8) ? 8 : (int) ((Wolf) e).getHealth();
                            pet.setHealth(health);
                            pet.setBaby(!((Wolf) e).isAdult());
                        } else {
                            pet.setSitting(((Ocelot) e).isSitting());
                            pet.setCatType(((Ocelot) e).getCatType());
                            health = (((Ocelot) e).getHealth() > 8) ? 8 : (int) ((Ocelot) e).getHealth();
                            pet.setHealth(health);
                            pet.setBaby(!((Ocelot) e).isAdult());
                        }
                        old_macd_had_a_pet.add(pet);
                        e.remove();
                    }
                    break;
                default:
                    break;
            }
        }
        return old_macd_had_a_pet;
    }
}
