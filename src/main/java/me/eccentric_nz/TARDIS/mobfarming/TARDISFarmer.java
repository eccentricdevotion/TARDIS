/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.mobfarming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISMultiInvChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMultiverseInventoriesChecker;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISPerWorldInventoryChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import static org.bukkit.entity.EntityType.OCELOT;
import static org.bukkit.entity.EntityType.WOLF;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

/**
 * Undefined Storage Holds make up most of a TARDIS's interior volume. Each Hold
 * has an identifying number.
 *
 * @author eccentric_nz
 */
public class TARDISFarmer {

    private final TARDIS plugin;
    private final List<Material> barding = new ArrayList<Material>();

    public TARDISFarmer(TARDIS plugin) {
        this.plugin = plugin;
        this.barding.add(Material.IRON_BARDING);
        this.barding.add(Material.GOLD_BARDING);
        this.barding.add(Material.DIAMOND_BARDING);
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
     * @param to the world to
     * @param from the world from
     * @return a List of the player's pets (if any are nearby)
     */
    @SuppressWarnings("deprecation")
    public List<TARDISMob> farmAnimals(Location l, COMPASS d, int id, final Player p, String to, String from) {
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
        List<Entity> mobs = ent.getNearbyEntities(3.75D, 3.75D, 3.75D);
        if (mobs.size() > 0) {
            List<TARDISHorse> old_macd_had_a_horse = new ArrayList<TARDISHorse>();
            List<TARDISHorse> old_macd_had_a_packhorse = new ArrayList<TARDISHorse>();
            List<TARDISLlama> old_macd_had_a_llama = new ArrayList<TARDISLlama>();
            List<TARDISMob> old_macd_had_a_chicken = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_cow = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_mooshroom = new ArrayList<TARDISMob>();
            List<TARDISMob> old_macd_had_a_sheep = new ArrayList<TARDISMob>();
            List<TARDISPig> old_macd_had_a_pig = new ArrayList<TARDISPig>();
            List<TARDISMob> old_macd_had_a_polarbear = new ArrayList<TARDISMob>();
            List<TARDISRabbit> old_macd_had_a_rabbit = new ArrayList<TARDISRabbit>();
            List<TARDISVillager> old_macd_had_a_villager = new ArrayList<TARDISVillager>();
            // are we doing an achievement?
            TARDISAchievementFactory taf = null;
            if (plugin.getAchievementConfig().getBoolean("farm.enabled")) {
                taf = new TARDISAchievementFactory(plugin, p, "farm", 5);
            }
            // count total farm mobs
            int farmtotal = 0;
            // count total horses
            int packhorsetotal = 0;
            // count total horses
            int horsetotal = 0;
            // count total rabbits
            int rabbittotal = 0;
            // count total villagers
            int villagertotal = 0;
            // count total pets
            int pettotal = 0;
            // count total polar bears
            int beartotal = 0;
            // count total llamas
            int llamatotal = 0;
            // is there a farm room?
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                String farm = tardis.getFarm();
                String stable = tardis.getStable();
                String stall = tardis.getStall();
                String hutch = tardis.getHutch();
                String igloo = tardis.getIgloo();
                String village = tardis.getVillage();
                // collate the mobs
                for (Entity e : mobs) {
                    switch (e.getType()) {
                        case CHICKEN:
                            TARDISMob tmchk = new TARDISMob();
                            tmchk.setAge(((Chicken) e).getAge());
                            tmchk.setBaby(!((Chicken) e).isAdult());
                            tmchk.setName(((LivingEntity) e).getCustomName());
                            old_macd_had_a_chicken.add(tmchk);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("CHICKEN");
                            }
                            farmtotal++;
                            break;
                        case COW:
                            TARDISMob tmcow = new TARDISMob();
                            tmcow.setAge(((Cow) e).getAge());
                            tmcow.setBaby(!((Cow) e).isAdult());
                            tmcow.setName(((LivingEntity) e).getCustomName());
                            old_macd_had_a_cow.add(tmcow);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("COW");
                            }
                            farmtotal++;
                            break;
                        case DONKEY:
                        case MULE:
                            Tameable ph_brokenin = (Tameable) e;
                            ChestedHorse packhorse = (ChestedHorse) e;
                            AbstractHorse ah = (AbstractHorse) e;
                            // if horse has a passenger, eject them!
                            packhorse.eject();
                            // don't farm other player's tamed horses
                            if (ph_brokenin.isTamed()) {
                                OfflinePlayer owner = (OfflinePlayer) ph_brokenin.getOwner();
                                if (owner != null && !owner.getUniqueId().equals(p.getUniqueId())) {
                                    break;
                                }
                            }
                            TARDISHorse tmpackhor = new TARDISHorse();
                            tmpackhor.setAge(packhorse.getAge());
                            tmpackhor.setBaby(!packhorse.isAdult());
                            tmpackhor.setHorseHealth(packhorse.getMaxHealth());
                            tmpackhor.setHealth(packhorse.getHealth());
                            tmpackhor.setHorseVariant(e.getType());
                            if (ph_brokenin.isTamed()) {
                                tmpackhor.setTamed(true);
                            } else {
                                tmpackhor.setTamed(false);
                            }
                            if (packhorse.isCarryingChest()) {
                                tmpackhor.setHasChest(true);
                            }
                            tmpackhor.setName(((LivingEntity) packhorse).getCustomName());
                            tmpackhor.setHorseInventory(packhorse.getInventory().getContents());
                            tmpackhor.setDomesticity(packhorse.getDomestication());
                            tmpackhor.setJumpStrength(packhorse.getJumpStrength());
                            if (plugin.isHelperOnServer()) {
                                double speed = plugin.getTardisHelper().getHorseSpeed(ah);
                                tmpackhor.setSpeed(speed);
                            }
                            // check the leash
                            if (packhorse.isLeashed()) {
                                Entity leash = packhorse.getLeashHolder();
                                tmpackhor.setLeashed(true);
                                if (leash instanceof LeashHitch) {
                                    leash.remove();
                                }
                            }
                            old_macd_had_a_packhorse.add(tmpackhor);
                            if (!stable.isEmpty() || (stable.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("HORSE");
                            }
                            packhorsetotal++;
                            break;
                        case HORSE:
                            Tameable brokenin = (Tameable) e;
                            Horse horse = (Horse) e;
                            // if horse has a passenger, eject them!
                            horse.eject();
                            // don't farm other player's tamed horses
                            if (brokenin.isTamed()) {
                                OfflinePlayer owner = (OfflinePlayer) brokenin.getOwner();
                                if (owner != null && !owner.getUniqueId().equals(p.getUniqueId())) {
                                    break;
                                }
                            }
                            TARDISHorse tmhor = new TARDISHorse();
                            tmhor.setAge(horse.getAge());
                            tmhor.setBaby(!horse.isAdult());
                            tmhor.setHorseHealth(horse.getMaxHealth());
                            tmhor.setHealth(horse.getHealth());
                            // get horse colour, style and variant
                            tmhor.setHorseColour(horse.getColor());
                            tmhor.setHorseStyle(horse.getStyle());
                            tmhor.setHorseVariant(EntityType.HORSE);
                            if (brokenin.isTamed()) {
                                tmhor.setTamed(true);
                            } else {
                                tmhor.setTamed(false);
                            }
                            if (horse.isCarryingChest()) {
                                tmhor.setHasChest(true);
                            }
                            tmhor.setName(((LivingEntity) horse).getCustomName());
                            tmhor.setHorseInventory(horse.getInventory().getContents());
                            tmhor.setDomesticity(horse.getDomestication());
                            tmhor.setJumpStrength(horse.getJumpStrength());
                            if (plugin.isHelperOnServer()) {
                                double speed = plugin.getTardisHelper().getHorseSpeed(horse);
                                tmhor.setSpeed(speed);
                            }
                            // check the leash
                            if (horse.isLeashed()) {
                                Entity leash = horse.getLeashHolder();
                                tmhor.setLeashed(true);
                                if (leash instanceof LeashHitch) {
                                    leash.remove();
                                }
                            }
                            old_macd_had_a_horse.add(tmhor);
                            if (!stable.isEmpty() || (stable.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("HORSE");
                            }
                            horsetotal++;
                            break;
                        case LLAMA:
                            // TODO make a Llama room
                            Tameable ll_brokenin = (Tameable) e;
                            Llama llama = (Llama) e;
                            // if horse has a passenger, eject them!
                            llama.eject();
                            // don't farm other player's tamed horses
                            if (ll_brokenin.isTamed()) {
                                OfflinePlayer owner = (OfflinePlayer) ll_brokenin.getOwner();
                                if (owner != null && !owner.getUniqueId().equals(p.getUniqueId())) {
                                    break;
                                }
                            }
                            TARDISLlama tmlla = new TARDISLlama();
                            tmlla.setAge(llama.getAge());
                            tmlla.setBaby(!llama.isAdult());
                            tmlla.setHorseHealth(llama.getMaxHealth());
                            tmlla.setHealth(llama.getHealth());
                            // get horse colour, style and variant
                            tmlla.setLlamacolor(llama.getColor());
                            tmlla.setHorseVariant(EntityType.HORSE);
                            if (ll_brokenin.isTamed()) {
                                tmlla.setTamed(true);
                            } else {
                                tmlla.setTamed(false);
                            }
                            if (llama.isCarryingChest()) {
                                tmlla.setHasChest(true);
                            }
                            tmlla.setName(((LivingEntity) llama).getCustomName());
                            tmlla.setHorseInventory(llama.getInventory().getContents());
                            tmlla.setDomesticity(llama.getDomestication());
                            tmlla.setJumpStrength(llama.getJumpStrength());
                            if (plugin.isHelperOnServer()) {
                                double speed = plugin.getTardisHelper().getHorseSpeed(llama);
                                tmlla.setSpeed(speed);
                            }
                            // check the leash
                            if (llama.isLeashed()) {
                                Entity leash = llama.getLeashHolder();
                                tmlla.setLeashed(true);
                                if (leash instanceof LeashHitch) {
                                    leash.remove();
                                }
                            }
                            old_macd_had_a_llama.add(tmlla);
                            if (!stall.isEmpty() || (stall.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("LLAMA");
                            }
                            llamatotal++;
                            break;
                        case PIG:
                            TARDISPig tmpig = new TARDISPig();
                            tmpig.setAge(((Pig) e).getAge());
                            tmpig.setBaby(!((Pig) e).isAdult());
                            tmpig.setName(((LivingEntity) e).getCustomName());
                            tmpig.setSaddled(((Pig) e).hasSaddle());
                            // eject any passengers
                            ((Pig) e).eject();
                            old_macd_had_a_pig.add(tmpig);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("PIG");
                            }
                            farmtotal++;
                            break;
                        case POLAR_BEAR:
                            TARDISMob tmbear = new TARDISMob();
                            tmbear.setAge(((PolarBear) e).getAge());
                            tmbear.setBaby(!((PolarBear) e).isAdult());
                            tmbear.setName(((LivingEntity) e).getCustomName());
                            old_macd_had_a_polarbear.add(tmbear);
                            if (!igloo.isEmpty() || (igloo.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("POLAR_BEAR");
                            }
                            beartotal++;
                            break;
                        case RABBIT:
                            Rabbit rabbit = (Rabbit) e;
                            TARDISRabbit tmrabbit = new TARDISRabbit();
                            tmrabbit.setAge(rabbit.getAge());
                            tmrabbit.setBaby(!rabbit.isAdult());
                            tmrabbit.setName(rabbit.getCustomName());
                            tmrabbit.setBunnyType(rabbit.getRabbitType());
                            old_macd_had_a_rabbit.add(tmrabbit);
                            if (!hutch.isEmpty() || (hutch.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("RABBIT");
                            }
                            rabbittotal++;
                            break;
                        case SHEEP:
                            TARDISMob tmshp = new TARDISMob();
                            tmshp.setAge(((Sheep) e).getAge());
                            tmshp.setBaby(!((Sheep) e).isAdult());
                            tmshp.setColour(((Sheep) e).getColor());
                            tmshp.setName(((LivingEntity) e).getCustomName());
                            old_macd_had_a_sheep.add(tmshp);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("SHEEP");
                            }
                            farmtotal++;
                            break;
                        case MUSHROOM_COW:
                            TARDISMob tmshr = new TARDISMob();
                            tmshr.setAge(((MushroomCow) e).getAge());
                            tmshr.setBaby(!((MushroomCow) e).isAdult());
                            tmshr.setName(((LivingEntity) e).getCustomName());
                            old_macd_had_a_mooshroom.add(tmshr);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("MUSHROOM_COW");
                            }
                            farmtotal++;
                            break;
                        case VILLAGER:
                            TARDISVillager tv = new TARDISVillager();
                            Villager v = (Villager) e;
                            tv.setProfession(v.getProfession());
                            tv.setAge(v.getAge());
                            tv.setHealth(v.getHealth());
                            tv.setBaby(!v.isAdult());
                            tv.setName(((LivingEntity) v).getCustomName());
                            tv.setTrades(v.getRecipes());
                            tv.setRiches(v.getRiches());
                            if (plugin.isHelperOnServer()) {
                                tv.setCareer(plugin.getTardisHelper().getVillagerCareer(v));
                                tv.setCareerLevel(plugin.getTardisHelper().getVillagerCareerLevel(v));
                                tv.setWilling(plugin.getTardisHelper().getVillagerWilling(v));
                            }
                            old_macd_had_a_villager.add(tv);
                            if (!village.isEmpty() || (village.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            villagertotal++;
                            break;
                        case WOLF:
                        case OCELOT:
                            Tameable tamed = (Tameable) e;
                            if (tamed.isTamed() && ((OfflinePlayer) tamed.getOwner()).getUniqueId().equals(p.getUniqueId())) {
                                TARDISMob pet = new TARDISMob();
                                pet.setType(e.getType());
                                pet.setName(((LivingEntity) e).getCustomName());
                                double health;
                                if (e.getType().equals(EntityType.WOLF)) {
                                    pet.setAge(((Wolf) e).getAge());
                                    pet.setSitting(((Wolf) e).isSitting());
                                    pet.setColour(((Wolf) e).getCollarColor());
                                    health = (((Wolf) e).getHealth() > 8D) ? 8D : ((Wolf) e).getHealth();
                                    pet.setHealth(health);
                                    pet.setBaby(!((Wolf) e).isAdult());
                                } else {
                                    pet.setAge(((Ocelot) e).getAge());
                                    pet.setSitting(((Ocelot) e).isSitting());
                                    pet.setCatType(((Ocelot) e).getCatType());
                                    health = (((Ocelot) e).getHealth() > 8D) ? 8D : ((Ocelot) e).getHealth();
                                    pet.setHealth(health);
                                    pet.setBaby(!((Ocelot) e).isAdult());
                                }
                                old_macd_had_a_pet.add(pet);
                                e.remove();
                                pettotal++;
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (farmtotal > 0 || packhorsetotal > 0 || horsetotal > 0 || villagertotal > 0 || pettotal > 0 || beartotal > 0 || llamatotal > 0) {
                    boolean canfarm;
                    switch (plugin.getInvManager()) {
                        case MULTIVERSE:
                            canfarm = TARDISMultiverseInventoriesChecker.checkWorldsCanShare(from, to);
                            break;
                        case MULTI:
                            canfarm = TARDISMultiInvChecker.checkWorldsCanShare(from, to);
                            break;
                        case PER_WORLD:
                            canfarm = TARDISPerWorldInventoryChecker.checkWorldsCanShare(from, to);
                            break;
                        default:
                            canfarm = true;
                    }
                    if (!canfarm) {
                        TARDISMessage.send(p, "WORLD_NO_FARM");
                        plugin.getTrackerKeeper().getFarming().remove(p.getUniqueId());
                        return null;
                    }
                }
                if (!farm.isEmpty()) {
                    // get location of farm room
                    String[] data = farm.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    if (old_macd_had_a_chicken.size() > 0) {
                        Location chicken_pen = new Location(world, x + 3, y, z - 3);
                        while (!world.getChunkAt(chicken_pen).isLoaded()) {
                            world.getChunkAt(chicken_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_chicken) {
                            plugin.setTardisSpawn(true);
                            Entity chicken = world.spawnEntity(chicken_pen, EntityType.CHICKEN);
                            Chicken pecker = (Chicken) chicken;
                            pecker.setAge(e.getAge());
                            if (e.isBaby()) {
                                pecker.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                pecker.setCustomName(name);
                            }
                            pecker.setRemoveWhenFarAway(false);
                        }
                    }
                    if (old_macd_had_a_cow.size() > 0) {
                        Location cow_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_cow) {
                            plugin.setTardisSpawn(true);
                            Entity cow = world.spawnEntity(cow_pen, EntityType.COW);
                            Cow moo = (Cow) cow;
                            moo.setAge(e.getAge());
                            if (e.isBaby()) {
                                moo.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                moo.setCustomName(name);
                            }
                            moo.setRemoveWhenFarAway(false);
                        }
                    }
                    if (old_macd_had_a_pig.size() > 0) {
                        Location pig_pen = new Location(world, x - 3, y, z - 3);
                        while (!world.getChunkAt(pig_pen).isLoaded()) {
                            world.getChunkAt(pig_pen).load();
                        }
                        for (TARDISPig e : old_macd_had_a_pig) {
                            plugin.setTardisSpawn(true);
                            Entity pig = world.spawnEntity(pig_pen, EntityType.PIG);
                            Pig oinker = (Pig) pig;
                            oinker.setAge(e.getAge());
                            if (e.isBaby()) {
                                oinker.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                oinker.setCustomName(name);
                            }
                            oinker.setSaddle(e.isSaddled());
                            oinker.setRemoveWhenFarAway(false);
                        }
                    }
                    if (old_macd_had_a_sheep.size() > 0) {
                        Location sheep_pen = new Location(world, x - 3, y, z + 3);
                        while (!world.getChunkAt(sheep_pen).isLoaded()) {
                            world.getChunkAt(sheep_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_sheep) {
                            plugin.setTardisSpawn(true);
                            Entity sheep = world.spawnEntity(sheep_pen, EntityType.SHEEP);
                            Sheep baa = (Sheep) sheep;
                            baa.setAge(e.getAge());
                            baa.setColor(e.getColour());
                            if (e.isBaby()) {
                                baa.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                baa.setCustomName(name);
                            }
                            baa.setRemoveWhenFarAway(false);
                        }
                    }
                    if (old_macd_had_a_mooshroom.size() > 0) {
                        Location cow_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        for (TARDISMob e : old_macd_had_a_mooshroom) {
                            plugin.setTardisSpawn(true);
                            Entity mooshroom = world.spawnEntity(cow_pen, EntityType.MUSHROOM_COW);
                            MushroomCow fungi = (MushroomCow) mooshroom;
                            fungi.setAge(e.getAge());
                            if (e.isBaby()) {
                                fungi.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                fungi.setCustomName(name);
                            }
                            fungi.setRemoveWhenFarAway(false);
                        }
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs")) {
                    // no farm, give the player spawn eggs
                    Inventory inv = p.getInventory();
                    if (old_macd_had_a_chicken.size() > 0) {
                        ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_chicken.size());
                        SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                        im.setSpawnedType(EntityType.CHICKEN);
                        is.setItemMeta(im);
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_cow.size() > 0) {
                        ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_cow.size());
                        SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                        im.setSpawnedType(EntityType.COW);
                        is.setItemMeta(im);
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_pig.size() > 0) {
                        ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_pig.size());
                        SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                        im.setSpawnedType(EntityType.PIG);
                        is.setItemMeta(im);
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_sheep.size() > 0) {
                        ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_sheep.size());
                        SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                        im.setSpawnedType(EntityType.SHEEP);
                        is.setItemMeta(im);
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_mooshroom.size() > 0) {
                        ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_mooshroom.size());
                        SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                        im.setSpawnedType(EntityType.MUSHROOM_COW);
                        is.setItemMeta(im);
                        inv.addItem(is);
                    }
                    p.updateInventory();
                } else if (farmtotal > 0) {
                    TARDISMessage.send(p, "FARM");
                }
                if (!stable.isEmpty() && (old_macd_had_a_horse.size() > 0 || old_macd_had_a_packhorse.size() > 0)) {
                    // get location of stable room
                    String[] data = stable.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location horse_pen = new Location(world, x + 0.5F, y, z + 0.5F);
                    while (!world.getChunkAt(horse_pen).isLoaded()) {
                        world.getChunkAt(horse_pen).load();
                    }
                    for (TARDISHorse e : old_macd_had_a_horse) {
                        plugin.setTardisSpawn(true);
                        Entity horse = world.spawnEntity(horse_pen, EntityType.HORSE);
                        Horse equine = (Horse) horse;
                        equine.setAge(e.getAge());
                        if (e.isBaby()) {
                            equine.setBaby();
                        }
                        equine.setMaxHealth(e.getHorseHealth());
                        equine.setHealth(e.getHealth());
                        equine.setColor(e.getHorseColour());
                        equine.setStyle(e.getHorseStyle());
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            equine.setCustomName(name);
                        }
                        Tameable tamed = (Tameable) equine;
                        if (e.isTamed()) {
                            tamed.setTamed(true);
                            tamed.setOwner(p);
                        }
                        equine.setDomestication(e.getDomesticity());
                        equine.setJumpStrength(e.getJumpStrength());
                        Inventory inv = equine.getInventory();
                        inv.setContents(e.getHorseinventory());
                        if (inv.contains(Material.SADDLE)) {
                            int saddle_slot = inv.first(Material.SADDLE);
                            ItemStack saddle = inv.getItem(saddle_slot);
                            equine.getInventory().setSaddle(saddle);
                        }
                        for (Material m : barding) {
                            if (inv.contains(m)) {
                                int armour_slot = inv.first(m);
                                ItemStack bard = inv.getItem(armour_slot);
                                equine.getInventory().setArmor(bard);
                            }
                        }
                        if (e.isLeashed()) {
                            Inventory pinv = p.getInventory();
                            ItemStack leash = new ItemStack(Material.LEASH, 1);
                            pinv.addItem(leash);
                            p.updateInventory();
                        }
                        if (plugin.isHelperOnServer()) {
                            plugin.getTardisHelper().setHorseSpeed(equine, e.getSpeed());
                        }
                        equine.setRemoveWhenFarAway(false);
                    }
                    for (TARDISHorse ph : old_macd_had_a_packhorse) {
                        plugin.setTardisSpawn(true);
                        Entity pack_horse = world.spawnEntity(horse_pen, ph.getHorseVariant());
                        Horse equine = (Horse) pack_horse;
                        equine.setAge(ph.getAge());
                        if (ph.isBaby()) {
                            equine.setBaby();
                        }
                        equine.setMaxHealth(ph.getHorseHealth());
                        equine.setHealth(ph.getHealth());
                        String name = ph.getName();
                        if (name != null && !name.isEmpty()) {
                            equine.setCustomName(name);
                        }
                        Tameable tamed = (Tameable) equine;
                        if (ph.isTamed()) {
                            tamed.setTamed(true);
                            tamed.setOwner(p);
                        }
                        equine.setDomestication(ph.getDomesticity());
                        equine.setJumpStrength(ph.getJumpStrength());
                        if (ph.hasChest()) {
                            ChestedHorse ch = (ChestedHorse) pack_horse;
                            ch.setCarryingChest(true);
                        }
                        Inventory inv = equine.getInventory();
                        inv.setContents(ph.getHorseinventory());
                        if (inv.contains(Material.SADDLE)) {
                            int saddle_slot = inv.first(Material.SADDLE);
                            ItemStack saddle = inv.getItem(saddle_slot);
                            equine.getInventory().setSaddle(saddle);
                        }
                        if (ph.isLeashed()) {
                            Inventory pinv = p.getInventory();
                            ItemStack leash = new ItemStack(Material.LEASH, 1);
                            pinv.addItem(leash);
                            p.updateInventory();
                        }
                        if (plugin.isHelperOnServer()) {
                            plugin.getTardisHelper().setHorseSpeed(equine, ph.getSpeed());
                        }
                        equine.setRemoveWhenFarAway(false);
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_horse.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_horse.size());
                    SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                    im.setSpawnedType(EntityType.HORSE);
                    is.setItemMeta(im);
                    inv.addItem(is);
                    p.updateInventory();
                } else if (horsetotal > 0) {
                    TARDISMessage.send(p, "FARM_STABLE");
                }
                if (!stall.isEmpty() && old_macd_had_a_llama.size() > 0) {
                    // get location of stable room
                    String[] data = stall.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location llama_pen = new Location(world, x + 0.5F, y, z + 0.5F);
                    while (!world.getChunkAt(llama_pen).isLoaded()) {
                        world.getChunkAt(llama_pen).load();
                    }
                    for (TARDISLlama ll : old_macd_had_a_llama) {
                        plugin.setTardisSpawn(true);
                        Entity llama = world.spawnEntity(llama_pen, EntityType.LLAMA);
                        Llama cria = (Llama) llama;
                        cria.setColor(ll.getLlamacolor());
                        cria.setAge(ll.getAge());
                        if (ll.isBaby()) {
                            cria.setBaby();
                        }
                        cria.setMaxHealth(ll.getHorseHealth());
                        cria.setHealth(ll.getHealth());
                        String name = ll.getName();
                        if (name != null && !name.isEmpty()) {
                            cria.setCustomName(name);
                        }
                        Tameable tamed = (Tameable) cria;
                        if (ll.isTamed()) {
                            tamed.setTamed(true);
                            tamed.setOwner(p);
                        }
                        cria.setDomestication(ll.getDomesticity());
                        cria.setJumpStrength(ll.getJumpStrength());
                        if (ll.hasChest()) {
                            ChestedHorse ch = (ChestedHorse) llama;
                            ch.setCarryingChest(true);
                        }
                        Inventory inv = cria.getInventory();
                        inv.setContents(ll.getHorseinventory());
                        if (inv.contains(Material.CARPET)) {
                            int carpet_slot = inv.first(Material.CARPET);
                            ItemStack carpet = inv.getItem(carpet_slot);
                            cria.getInventory().setDecor(carpet);
                        }
                        if (ll.isLeashed()) {
                            Inventory pinv = p.getInventory();
                            ItemStack leash = new ItemStack(Material.LEASH, 1);
                            pinv.addItem(leash);
                            p.updateInventory();
                        }
                        if (plugin.isHelperOnServer()) {
                            plugin.getTardisHelper().setHorseSpeed(cria, ll.getSpeed());
                        }
                        cria.setRemoveWhenFarAway(false);
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_llama.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_llama.size());
                    SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                    im.setSpawnedType(EntityType.LLAMA);
                    is.setItemMeta(im);
                    inv.addItem(is);
                    p.updateInventory();
                } else if (horsetotal > 0) {
                    TARDISMessage.send(p, "FARM_STALL");
                }
                if (!hutch.isEmpty() && old_macd_had_a_rabbit.size() > 0) {
                    // get location of hutch room
                    String[] data = hutch.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location rabbit_hutch = new Location(world, x + 0.5F, y, z + 0.5F);
                    while (!world.getChunkAt(rabbit_hutch).isLoaded()) {
                        world.getChunkAt(rabbit_hutch).load();
                    }
                    for (TARDISRabbit e : old_macd_had_a_rabbit) {
                        plugin.setTardisSpawn(true);
                        Entity rabbit = world.spawnEntity(rabbit_hutch, EntityType.RABBIT);
                        Rabbit bunny = (Rabbit) rabbit;
                        bunny.setAge(e.getAge());
                        if (e.isBaby()) {
                            bunny.setBaby();
                        }
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            bunny.setCustomName(name);
                        }
                        bunny.setRabbitType(e.getBunnyType());
                        bunny.setRemoveWhenFarAway(false);
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_rabbit.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_rabbit.size());
                    SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                    im.setSpawnedType(EntityType.RABBIT);
                    is.setItemMeta(im);
                    inv.addItem(is);
                    p.updateInventory();
                } else if (rabbittotal > 0) {
                    TARDISMessage.send(p, "FARM_HUTCH");
                }
                if (!village.isEmpty() && old_macd_had_a_villager.size() > 0) {
                    // get location of village room
                    String[] data = village.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location v_room = new Location(world, x + 0.5F, y, z + 0.5F);
                    while (!world.getChunkAt(v_room).isLoaded()) {
                        world.getChunkAt(v_room).load();
                    }
                    for (TARDISVillager e : old_macd_had_a_villager) {
                        plugin.setTardisSpawn(true);
                        Entity vill = world.spawnEntity(v_room, EntityType.VILLAGER);
                        Villager npc = (Villager) vill;
                        npc.setProfession(e.getProfession());
                        npc.setAge(e.getAge());
                        if (e.isBaby()) {
                            npc.setBaby();
                        }
                        npc.setHealth(e.getHealth());
                        npc.setRecipes(e.getTrades());
                        npc.setRiches(e.getRiches());
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            npc.setCustomName(name);
                        }
                        if (plugin.isHelperOnServer()) {
                            plugin.getTardisHelper().setVillagerCareer(npc, e.getCareer());
                            plugin.getTardisHelper().setVillagerCareerLevel(npc, e.getCareerLevel());
                            plugin.getTardisHelper().setVillagerWilling(npc, e.isWilling());
                        }
                        npc.setRemoveWhenFarAway(false);
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_villager.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_villager.size());
                    SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                    im.setSpawnedType(EntityType.VILLAGER);
                    is.setItemMeta(im);
                    inv.addItem(is);
                    p.updateInventory();
                } else if (villagertotal > 0) {
                    TARDISMessage.send(p, "FARM_VILLAGE");
                }
                if (!igloo.isEmpty() && old_macd_had_a_polarbear.size() > 0) {
                    // get location of igloo room
                    String[] data = igloo.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location i_room = new Location(world, x + 0.5F, y, z + 0.5F);
                    while (!world.getChunkAt(i_room).isLoaded()) {
                        world.getChunkAt(i_room).load();
                    }
                    for (TARDISMob e : old_macd_had_a_polarbear) {
                        plugin.setTardisSpawn(true);
                        Entity bear = world.spawnEntity(i_room, EntityType.POLAR_BEAR);
                        PolarBear polar = (PolarBear) bear;
                        polar.setAge(e.getAge());
                        if (e.isBaby()) {
                            polar.setBaby();
                        }
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            polar.setCustomName(name);
                        }
                        polar.setRemoveWhenFarAway(false);
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_polarbear.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.MONSTER_EGG, old_macd_had_a_polarbear.size());
                    SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
                    im.setSpawnedType(EntityType.POLAR_BEAR);
                    is.setItemMeta(im);
                    inv.addItem(is);
                    p.updateInventory();
                } else if (beartotal > 0) {
                    TARDISMessage.send(p, "FARM_IGLOO");
                }
            }
        }
        ent.remove();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getTrackerKeeper().getFarming().remove(p.getUniqueId());
            }
        }, 20L);
        return old_macd_had_a_pet;
    }

    public List<TARDISMob> exitPets(Player p) {
        List<TARDISMob> old_macd_had_a_pet = new ArrayList<TARDISMob>();
        Entity ent = (Entity) p;
        List<Entity> mobs = ent.getNearbyEntities(3.5D, 3.5D, 3.5D);
        for (Entity e : mobs) {
            if (e.getType().equals(EntityType.OCELOT) || e.getType().equals(EntityType.WOLF)) {
                Tameable tamed = (Tameable) e;
                if (tamed.isTamed() && ((OfflinePlayer) tamed.getOwner()).getUniqueId().equals(p.getUniqueId())) {
                    TARDISMob pet = new TARDISMob();
                    pet.setType(e.getType());
                    String pet_name = ((LivingEntity) e).getCustomName();
                    if (pet_name != null) {
                        pet.setName(pet_name);
                    }
                    double health;
                    if (e.getType().equals(EntityType.WOLF)) {
                        pet.setAge(((Wolf) e).getAge());
                        pet.setSitting(((Wolf) e).isSitting());
                        pet.setColour(((Wolf) e).getCollarColor());
                        health = (((Wolf) e).getHealth() > 8D) ? 8D : ((Wolf) e).getHealth();
                        pet.setHealth(health);
                        pet.setBaby(!((Wolf) e).isAdult());
                    } else {
                        pet.setAge(((Ocelot) e).getAge());
                        pet.setSitting(((Ocelot) e).isSitting());
                        pet.setCatType(((Ocelot) e).getCatType());
                        health = (((Ocelot) e).getHealth() > 8D) ? 8D : ((Ocelot) e).getHealth();
                        pet.setHealth(health);
                        pet.setBaby(!((Ocelot) e).isAdult());
                    }
                    old_macd_had_a_pet.add(pet);
                    e.remove();
                }
            }
        }
        return old_macd_had_a_pet;
    }
}
