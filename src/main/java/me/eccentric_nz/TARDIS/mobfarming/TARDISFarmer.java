/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.data.Farm;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Undefined Storage Holds make up most of a TARDIS's interior volume. Each Hold has an identifying number.
 *
 * @author eccentric_nz
 */
public class TARDISFarmer {

    private final TARDIS plugin;

    public TARDISFarmer(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether there are any animals around the TARDIS Police Box. If mobs are found they are teleported to the
     * 'farm' room (if present), otherwise a spawn egg for the mob type is placed in the player's inventory. Only cows,
     * sheep, pigs, chickens and mooshrooms will be processed.
     * <p>
     * Also allows players to teleport their pets (tamed wolves and ocelots) with them.
     *
     * @param l    The location to check for animals. This will be the current location of the TARDIS Police Box.
     * @param d    the direction the Police Box is facing
     * @param id   The database key of the TARDIS.
     * @param p    the player to award achievements or give spawn eggs to
     * @param to   the world to
     * @param from the world from
     * @return a List of the player's pets (if any are nearby)
     */
    public List<TARDISParrot> farmAnimals(Location l, COMPASS d, int id, Player p, String to, String from) {
        List<TARDISParrot> old_macd_had_a_pet = new ArrayList<>();
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
            List<TARDISHorse> old_macd_had_a_horse = new ArrayList<>();
            List<TARDISLlama> old_macd_had_a_llama = new ArrayList<>();
            List<TARDISMob> old_macd_had_a_chicken = new ArrayList<>();
            List<TARDISMob> old_macd_had_a_cow = new ArrayList<>();
            TARDISFish old_macd_had_a_fish = null;
            List<TARDISMob> old_macd_had_a_mooshroom = new ArrayList<>();
            List<TARDISMob> old_macd_had_a_sheep = new ArrayList<>();
            List<TARDISParrot> old_macd_had_a_parrot = new ArrayList<>();
            List<TARDISPig> old_macd_had_a_pig = new ArrayList<>();
            List<TARDISMob> old_macd_had_a_polarbear = new ArrayList<>();
            List<TARDISRabbit> old_macd_had_a_rabbit = new ArrayList<>();
            List<TARDISVillager> old_macd_had_a_villager = new ArrayList<>();
            // are we doing an achievement?
            TARDISAchievementFactory taf = null;
            if (plugin.getAchievementConfig().getBoolean("farm.enabled")) {
                taf = new TARDISAchievementFactory(plugin, p, ADVANCEMENT.FARM, 5);
            }
            // count total farm mobs
            int farmtotal = 0;
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
            // count total parrots
            int parrottotal = 0;
            // is there a farm room?
            ResultSetFarming rs = new ResultSetFarming(plugin, id);
            if (rs.resultSet()) {
                Farm farming = rs.getFarming();
                String aquarium = farming.getAquarium();
                String birdcage = farming.getBirdcage();
                String farm = farming.getFarm();
                String hutch = farming.getHutch();
                String igloo = farming.getIgloo();
                String stable = farming.getStable();
                String stall = farming.getStall();
                String village = farming.getVillage();
                // collate the mobs
                for (Entity e : mobs) {
                    switch (e.getType()) {
                        case CHICKEN:
                            TARDISMob tmchk = new TARDISMob();
                            tmchk.setAge(((Chicken) e).getAge());
                            tmchk.setBaby(!((Chicken) e).isAdult());
                            tmchk.setName(e.getCustomName());
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
                            tmcow.setName(e.getCustomName());
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
                        case HORSE:
                            Tameable brokenin = (Tameable) e;
                            AbstractHorse horse = (AbstractHorse) e;
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
                            double mh = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            tmhor.setHorseHealth(mh);
                            tmhor.setHealth(horse.getHealth());
                            // get horse colour, style and variant
                            if (e.getType().equals(EntityType.HORSE)) {
                                Horse pony = (Horse) horse;
                                tmhor.setHorseColour(pony.getColor());
                                tmhor.setHorseStyle(pony.getStyle());
                            }
                            tmhor.setHorseVariant(e.getType());
                            if (brokenin.isTamed()) {
                                tmhor.setTamed(true);
                            } else {
                                tmhor.setTamed(false);
                            }
                            if (e.getType().equals(EntityType.DONKEY) || e.getType().equals(EntityType.MULE)) {
                                ChestedHorse ch = (ChestedHorse) horse;
                                if (ch.isCarryingChest()) {
                                    tmhor.setHasChest(true);
                                }
                            }
                            tmhor.setName(horse.getCustomName());
                            tmhor.setHorseInventory(horse.getInventory().getContents());
                            tmhor.setDomesticity(horse.getDomestication());
                            tmhor.setJumpStrength(horse.getJumpStrength());
                            tmhor.setSpeed(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
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
                            double llmh = llama.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            tmlla.setHorseHealth(llmh);
                            tmlla.setHealth(llama.getHealth());
                            // get horse colour, style and variant
                            tmlla.setLlamacolor(llama.getColor());
                            tmlla.setStrength(llama.getStrength());
                            tmlla.setHorseVariant(EntityType.HORSE);
                            if (ll_brokenin.isTamed()) {
                                tmlla.setTamed(true);
                            } else {
                                tmlla.setTamed(false);
                            }
                            if (llama.isCarryingChest()) {
                                tmlla.setHasChest(true);
                            }
                            tmlla.setName(llama.getCustomName());
                            tmlla.setHorseInventory(llama.getInventory().getContents());
                            tmlla.setDomesticity(llama.getDomestication());
                            tmlla.setJumpStrength(llama.getJumpStrength());
                            tmlla.setSpeed(llama.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
                            // check the leash
                            if (llama.isLeashed()) {
                                Entity leash = llama.getLeashHolder();
                                tmlla.setLeashed(true);
                                if (leash instanceof LeashHitch) {
                                    leash.remove();
                                }
                            }
                            LlamaInventory llinv = llama.getInventory();
                            tmlla.setDecor(llinv.getDecor());
                            old_macd_had_a_llama.add(tmlla);
                            if (!stall.isEmpty() || (stall.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("LLAMA");
                            }
                            llamatotal++;
                            break;
                        case PARROT:
                            Tameable polly = (Tameable) e;
                            AnimalTamer tamer = polly.getOwner();
                            boolean timeLordIsOwner = (tamer != null && tamer.getUniqueId().equals(p.getUniqueId()));
                            TARDISParrot tmparrot = new TARDISParrot();
                            if (polly.isTamed()) {
                                if (timeLordIsOwner) {
                                    // only move tamed parrots that the time lord owns!
                                    tmparrot.setTamed(true);
                                    tmparrot.setOnLeftShoulder(p.getShoulderEntityLeft() != null);
                                    tmparrot.setOnRightShoulder(p.getShoulderEntityRight() != null);
                                } else {
                                    break;
                                }
                            }
                            tmparrot.setType(EntityType.PARROT);
                            tmparrot.setVariant(((Parrot) e).getVariant());
                            tmparrot.setAge(((Parrot) e).getAge());
                            tmparrot.setBaby(!((Parrot) e).isAdult());
                            tmparrot.setName(e.getCustomName());
                            tmparrot.setSitting(((Sittable) e).isSitting());
                            double phealth = (((Parrot) e).getHealth() > 8D) ? 8D : ((Parrot) e).getHealth();
                            tmparrot.setHealth(phealth);
                            if (timeLordIsOwner || polly.isTamed()) {
                                old_macd_had_a_pet.add(tmparrot);
                            } else {
                                old_macd_had_a_parrot.add(tmparrot);
                            }
                            if (timeLordIsOwner || !birdcage.isEmpty() || (birdcage.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                e.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("PARROT");
                            }
                            parrottotal++;
                            break;
                        case PIG:
                            TARDISPig tmpig = new TARDISPig();
                            tmpig.setAge(((Pig) e).getAge());
                            tmpig.setBaby(!((Pig) e).isAdult());
                            tmpig.setName(e.getCustomName());
                            tmpig.setSaddled(((Pig) e).hasSaddle());
                            // eject any passengers
                            e.eject();
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
                            tmbear.setName(e.getCustomName());
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
                            tmshp.setName(e.getCustomName());
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
                            tmshr.setName(e.getCustomName());
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
                            tv.setName(v.getCustomName());
                            tv.setTrades(v.getRecipes());
                            tv.setRiches(v.getRiches());
                            tv.setCareer(v.getCareer());
                            if (plugin.isHelperOnServer()) {
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
                            if (tamed.isTamed() && tamed.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                TARDISParrot pet = new TARDISParrot();
                                pet.setType(e.getType());
                                pet.setName(e.getCustomName());
                                double health;
                                if (e.getType().equals(EntityType.WOLF)) {
                                    pet.setAge(((Wolf) e).getAge());
                                    pet.setSitting(((Sittable) e).isSitting());
                                    pet.setColour(((Wolf) e).getCollarColor());
                                    health = (((Wolf) e).getHealth() > 8D) ? 8D : ((Wolf) e).getHealth();
                                    pet.setHealth(health);
                                    pet.setBaby(!((Wolf) e).isAdult());
                                } else {
                                    pet.setAge(((Ocelot) e).getAge());
                                    pet.setSitting(((Sittable) e).isSitting());
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
                ItemStack fishBucket = p.getInventory().getItemInMainHand();
                if (fishBucket != null && TARDISMaterials.fish_buckets.contains(fishBucket.getType())) {
                    old_macd_had_a_fish = new TARDISFish();
                    old_macd_had_a_fish.setType(TARDISMaterials.fishMap.get(fishBucket.getType()));
                    if (fishBucket.getType().equals(Material.TROPICAL_FISH_BUCKET)) {
                        TropicalFishBucketMeta fbim = (TropicalFishBucketMeta) fishBucket.getItemMeta();
                        old_macd_had_a_fish.setColour(fbim.getBodyColor());
                        old_macd_had_a_fish.setPattern(fbim.getPattern());
                        old_macd_had_a_fish.setPatternColour(fbim.getPatternColor());
                    }
                }
                if (farmtotal > 0 || horsetotal > 0 || villagertotal > 0 || pettotal > 0 || beartotal > 0 || llamatotal > 0 || parrottotal > 0 || old_macd_had_a_fish != null) {
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
                if (!aquarium.isEmpty() && old_macd_had_a_fish != null) {
                    // get location of farm room
                    String[] data = aquarium.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location fish_tank = new Location(world, x, y, z);
                    switch (old_macd_had_a_fish.getType()) {
                        case COD:
                            fish_tank.add(3.0d, 1.5d, 3.0d);
                            break;
                        case PUFFERFISH:
                            fish_tank.add(-3.0d, 1.5d, 3.0d);
                            break;
                        case SALMON:
                            fish_tank.add(3.0d, 1.5d, -3.0d);
                            break;
                        default: // TROPICAL_FISH
                            fish_tank.add(-3.0d, 1.5d, -3.0d);
                            break;
                    }
                    while (!world.getChunkAt(fish_tank).isLoaded()) {
                        world.getChunkAt(fish_tank).load();
                    }
                    plugin.setTardisSpawn(true);
                    Entity fish = world.spawnEntity(fish_tank, old_macd_had_a_fish.getType());
                    if (old_macd_had_a_fish.getType().equals(EntityType.TROPICAL_FISH)) {
                        TropicalFish tf = (TropicalFish) fish;
                        tf.setBodyColor(old_macd_had_a_fish.getColour());
                        tf.setPattern(old_macd_had_a_fish.getPattern());
                        tf.setPatternColor(old_macd_had_a_fish.getPatternColour());
                    }
                    // change fish bucket to empty bucket
                    p.getInventory().getItemInMainHand().setType(Material.BUCKET);
                    p.updateInventory();
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
                        old_macd_had_a_chicken.forEach((e) -> {
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
                        });
                    }
                    if (old_macd_had_a_cow.size() > 0) {
                        Location cow_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        old_macd_had_a_cow.forEach((e) -> {
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
                        });
                    }
                    if (old_macd_had_a_pig.size() > 0) {
                        Location pig_pen = new Location(world, x - 3, y, z - 3);
                        while (!world.getChunkAt(pig_pen).isLoaded()) {
                            world.getChunkAt(pig_pen).load();
                        }
                        old_macd_had_a_pig.forEach((e) -> {
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
                        });
                    }
                    if (old_macd_had_a_sheep.size() > 0) {
                        Location sheep_pen = new Location(world, x - 3, y, z + 3);
                        while (!world.getChunkAt(sheep_pen).isLoaded()) {
                            world.getChunkAt(sheep_pen).load();
                        }
                        old_macd_had_a_sheep.forEach((e) -> {
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
                        });
                    }
                    if (old_macd_had_a_mooshroom.size() > 0) {
                        Location cow_pen = new Location(world, x + 3, y, z + 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        old_macd_had_a_mooshroom.forEach((e) -> {
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
                        });
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs")) {
                    // no farm, give the player spawn eggs
                    Inventory inv = p.getInventory();
                    if (old_macd_had_a_chicken.size() > 0) {
                        ItemStack is = new ItemStack(Material.CHICKEN_SPAWN_EGG, old_macd_had_a_chicken.size());
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_cow.size() > 0) {
                        ItemStack is = new ItemStack(Material.COW_SPAWN_EGG, old_macd_had_a_cow.size());
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_pig.size() > 0) {
                        ItemStack is = new ItemStack(Material.PIG_SPAWN_EGG, old_macd_had_a_pig.size());
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_sheep.size() > 0) {
                        ItemStack is = new ItemStack(Material.SHEEP_SPAWN_EGG, old_macd_had_a_sheep.size());
                        inv.addItem(is);
                    }
                    if (old_macd_had_a_mooshroom.size() > 0) {
                        ItemStack is = new ItemStack(Material.MOOSHROOM_SPAWN_EGG, old_macd_had_a_mooshroom.size());
                        inv.addItem(is);
                    }
                    p.updateInventory();
                } else if (farmtotal > 0) {
                    TARDISMessage.send(p, "FARM");
                }
                if (!stable.isEmpty() && old_macd_had_a_horse.size() > 0) {
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
                    old_macd_had_a_horse.forEach((e) -> {
                        plugin.setTardisSpawn(true);
                        Entity horse = world.spawnEntity(horse_pen, e.getHorseVariant());
                        AbstractHorse equine = (AbstractHorse) horse;
                        equine.setAge(e.getAge());
                        if (e.isBaby()) {
                            equine.setBaby();
                        }
                        AttributeInstance attribute = equine.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        attribute.setBaseValue(e.getHorseHealth());
                        equine.setHealth(e.getHealth());
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            equine.setCustomName(name);
                        }
                        if (e.isTamed()) {
                            equine.setTamed(true);
                            equine.setOwner(p);
                        }
                        if (e.getHorseVariant().equals(EntityType.HORSE)) {
                            Horse pony = (Horse) equine;
                            pony.setColor(e.getHorseColour());
                            pony.setStyle(e.getHorseStyle());
                        }
                        if ((e.getHorseVariant().equals(EntityType.DONKEY) || e.getHorseVariant().equals(EntityType.MULE)) && e.hasChest()) {
                            ChestedHorse ch = (ChestedHorse) equine;
                            ch.setCarryingChest(true);
                        }
                        equine.setDomestication(e.getDomesticity());
                        equine.setJumpStrength(e.getJumpStrength());
                        Inventory inv = equine.getInventory();
                        inv.setContents(e.getHorseinventory());
                        if (e.isLeashed()) {
                            Inventory pinv = p.getInventory();
                            ItemStack leash = new ItemStack(Material.LEAD, 1);
                            pinv.addItem(leash);
                            p.updateInventory();
                        }
                        equine.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(e.getSpeed());
                        equine.setRemoveWhenFarAway(false);
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_horse.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.HORSE_SPAWN_EGG, old_macd_had_a_horse.size());
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
                    old_macd_had_a_llama.forEach((ll) -> {
                        plugin.setTardisSpawn(true);
                        Entity llama = world.spawnEntity(llama_pen, EntityType.LLAMA);
                        Llama cria = (Llama) llama;
                        cria.setColor(ll.getLlamacolor());
                        cria.setStrength(ll.getStrength());
                        cria.setAge(ll.getAge());
                        if (ll.isBaby()) {
                            cria.setBaby();
                        }
                        AttributeInstance attribute = cria.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        attribute.setBaseValue(ll.getHorseHealth());
                        cria.setHealth(ll.getHealth());
                        String name = ll.getName();
                        if (name != null && !name.isEmpty()) {
                            cria.setCustomName(name);
                        }
                        if (ll.isTamed()) {
                            cria.setTamed(true);
                            cria.setOwner(p);
                        }
                        cria.setDomestication(ll.getDomesticity());
                        cria.setJumpStrength(ll.getJumpStrength());
                        if (ll.hasChest()) {
                            ChestedHorse ch = (ChestedHorse) llama;
                            ch.setCarryingChest(true);
                        }
                        LlamaInventory inv = cria.getInventory();
                        inv.setContents(ll.getHorseinventory());
                        inv.setDecor(ll.getDecor());
                        if (ll.isLeashed()) {
                            Inventory pinv = p.getInventory();
                            ItemStack leash = new ItemStack(Material.LEAD, 1);
                            pinv.addItem(leash);
                            p.updateInventory();
                        }
                        cria.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(ll.getSpeed());
                        cria.setRemoveWhenFarAway(false);
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_llama.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.LLAMA_SPAWN_EGG, old_macd_had_a_llama.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (llamatotal > 0) {
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
                    old_macd_had_a_rabbit.forEach((e) -> {
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
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_rabbit.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.RABBIT_SPAWN_EGG, old_macd_had_a_rabbit.size());
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
                    old_macd_had_a_villager.forEach((e) -> {
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
                        npc.setCareer(e.getCareer());
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            npc.setCustomName(name);
                        }
                        if (plugin.isHelperOnServer()) {
                            plugin.getTardisHelper().setVillagerCareerLevel(npc, e.getCareerLevel());
                            plugin.getTardisHelper().setVillagerWilling(npc, e.isWilling());
                        }
                        npc.setRemoveWhenFarAway(false);
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_villager.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.VILLAGER_SPAWN_EGG, old_macd_had_a_villager.size());
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
                    old_macd_had_a_polarbear.forEach((e) -> {
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
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_polarbear.size() > 0) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG, old_macd_had_a_polarbear.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (beartotal > 0) {
                    TARDISMessage.send(p, "FARM_IGLOO");
                }
                if (!birdcage.isEmpty() && old_macd_had_a_parrot.size() > 0) {
                    // get location of igloo room
                    String[] data = birdcage.split(":");
                    World world = plugin.getServer().getWorld(data[0]);
                    int x = TARDISNumberParsers.parseInt(data[1]);
                    int y = TARDISNumberParsers.parseInt(data[2]) + 1;
                    int z = TARDISNumberParsers.parseInt(data[3]);
                    Location b_room = new Location(world, x + 0.5F, y, z + 0.5F);
                    while (!world.getChunkAt(b_room).isLoaded()) {
                        world.getChunkAt(b_room).load();
                    }
                    old_macd_had_a_parrot.forEach((e) -> {
                        plugin.setTardisSpawn(true);
                        Entity polly = world.spawnEntity(b_room, EntityType.PARROT);
                        Parrot parrot = (Parrot) polly;
                        parrot.setVariant(e.getVariant());
                        parrot.setAge(e.getAge());
                        if (e.isBaby()) {
                            parrot.setBaby();
                        }
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            parrot.setCustomName(name);
                        }
                        parrot.setSitting(false); // let them fly in the cage
                        parrot.setRemoveWhenFarAway(false);
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && old_macd_had_a_parrot.size() > 0) {
                    // give spawn eggs
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.PARROT_SPAWN_EGG, old_macd_had_a_parrot.size());
                    inv.addItem(is);
                    p.updateInventory();
                }
            }
        }
        ent.remove();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getFarming().remove(p.getUniqueId()), 20L);
        return old_macd_had_a_pet;
    }

    public List<TARDISParrot> exitPets(Player p) {
        List<TARDISParrot> old_macd_had_a_pet = new ArrayList<>();
        List<Entity> mobs = p.getNearbyEntities(3.5D, 3.5D, 3.5D);
        for (Entity e : mobs) {
            if (e.getType().equals(EntityType.OCELOT) || e.getType().equals(EntityType.WOLF) || e.getType().equals(EntityType.PARROT)) {
                Tameable tamed = (Tameable) e;
                if (tamed.isTamed() && tamed.getOwner().getUniqueId().equals(p.getUniqueId())) {
                    TARDISParrot pet = new TARDISParrot();
                    pet.setType(e.getType());
                    String pet_name = e.getCustomName();
                    if (pet_name != null) {
                        pet.setName(pet_name);
                    }
                    double health;
                    switch (e.getType()) {
                        case WOLF:
                            pet.setAge(((Wolf) e).getAge());
                            pet.setSitting(((Wolf) e).isSitting());
                            pet.setColour(((Wolf) e).getCollarColor());
                            health = (((Wolf) e).getHealth() > 8D) ? 8D : ((Wolf) e).getHealth();
                            pet.setHealth(health);
                            pet.setBaby(!((Wolf) e).isAdult());
                            break;
                        case OCELOT:
                            pet.setAge(((Ocelot) e).getAge());
                            pet.setSitting(((Ocelot) e).isSitting());
                            pet.setCatType(((Ocelot) e).getCatType());
                            health = (((Ocelot) e).getHealth() > 8D) ? 8D : ((Ocelot) e).getHealth();
                            pet.setHealth(health);
                            pet.setBaby(!((Ocelot) e).isAdult());
                            break;
                        case PARROT:
                            pet.setAge(((Parrot) e).getAge());
                            pet.setSitting(((Parrot) e).isSitting());
                            pet.setVariant(((Parrot) e).getVariant());
                            health = (((Parrot) e).getHealth() > 8D) ? 8D : ((Parrot) e).getHealth();
                            pet.setHealth(health);
                            pet.setBaby(!((Parrot) e).isAdult());
                            pet.setOnLeftShoulder(p.getShoulderEntityLeft() != null);
                            pet.setOnRightShoulder(p.getShoulderEntityRight() != null);
                            break;
                        default:
                            break;
                    }
                    old_macd_had_a_pet.add(pet);
                    e.remove();
                }
            }
        }
        return old_macd_had_a_pet;
    }
}
