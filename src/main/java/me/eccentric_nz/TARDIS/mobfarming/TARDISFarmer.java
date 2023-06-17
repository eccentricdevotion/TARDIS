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
package me.eccentric_nz.TARDIS.mobfarming;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.data.Farm;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarming;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMultiverseInventoriesChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
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
     * Also allows players to teleport their pets (tamed wolves and Cats) with them.
     *
     * @param l    The location to check for animals. This will be the current location of the TARDIS Police Box.
     * @param d    the direction the Police Box is facing
     * @param id   The database key of the TARDIS.
     * @param p    the player to award achievements or give spawn eggs to
     * @param to   the world to
     * @param from the world from
     * @return a List of the player's pets (if any are nearby)
     */
    public TARDISPetsAndFollowers farmAnimals(Location l, COMPASS d, int id, Player p, String to, String from) {
        switch (d) {
            case NORTH -> l.setZ(l.getZ() - 1);
            case WEST -> l.setX(l.getX() - 1);
            case SOUTH -> l.setZ(l.getZ() + 1);
            default -> l.setX(l.getX() + 1);
        }
        l.setY(l.getY() + 1);
        // spawn an entity at this location so we can get nearby entities - an egg will do
        World w = l.getWorld();
        Entity egg = w.spawnEntity(l, EntityType.EGG);
        List<Entity> mobs = egg.getNearbyEntities(3.75D, 3.75D, 3.75D);

        List<TARDISPet> pets = new ArrayList<>();
        List<TARDISFollower> followers = new ArrayList<>();
        if (!mobs.isEmpty()) {
            List<TARDISAxolotl> axolotls = new ArrayList<>();
            List<TARDISHorse> horses = new ArrayList<>();
            List<TARDISLlama> llamas = new ArrayList<>();
            List<TARDISMob> chickens = new ArrayList<>();
            List<TARDISMob> cows = new ArrayList<>();
            TARDISFish fish = null;
            List<TARDISMooshroom> mooshrooms = new ArrayList<>();
            List<TARDISMob> sheep = new ArrayList<>();
            List<TARDISPet> parrots = new ArrayList<>();
            List<TARDISPig> pigs = new ArrayList<>();
            List<TARDISMob> polarbears = new ArrayList<>();
            List<TARDISRabbit> rabbits = new ArrayList<>();
            List<TARDISBee> bees = new ArrayList<>();
            List<TARDISVillager> villagers = new ArrayList<>();
            List<TARDISPanda> pandas = new ArrayList<>();
            List<TARDISFrog> frogs = new ArrayList<>();
            // are we doing an achievement?
            TARDISAchievementFactory taf = null;
            if (plugin.getAchievementConfig().getBoolean("farm.enabled")) {
                taf = new TARDISAchievementFactory(plugin, p, Advancement.FARM, 14);
            }
            // count total farm mobs
            int farmtotal = 0;
            // is there a farm room?
            ResultSetFarming rs = new ResultSetFarming(plugin, id);
            if (rs.resultSet()) {
                Farm farming = rs.getFarming();
                String apiary = farming.getApiary();
                String aquarium = farming.getAquarium();
                String bamboo = farming.getBamboo();
                String birdcage = farming.getBirdcage();
                String farm = farming.getFarm();
                String geode = farming.getGeode();
                String hutch = farming.getHutch();
                String igloo = farming.getIgloo();
                String stable = farming.getStable();
                String stall = farming.getStall();
                String village = farming.getVillage();
                String mangrove = farming.getMangrove();
                // collate the mobs
                for (Entity entity : mobs) {
                    switch (entity.getType()) {
                        case ARMOR_STAND -> {
                            if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                                TARDISFollower follower = new TARDISFollower(entity, p.getUniqueId());
                                if (follower.isValid()) {
                                    followers.add(follower);
                                    entity.remove();
                                }
                            }
                        }
                        case AXOLOTL -> {
                            TARDISAxolotl tmaxl = new TARDISAxolotl();
                            tmaxl.setAxolotlVariant(((Axolotl) entity).getVariant());
                            tmaxl.setAge(((Axolotl) entity).getAge());
                            tmaxl.setBaby(!((Axolotl) entity).isAdult());
                            tmaxl.setName(entity.getCustomName());
                            axolotls.add(tmaxl);
                            if (!geode.isEmpty() || (geode.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("AXOLOTL");
                            }
                        }
                        case BEE -> {
                            TARDISBee tmbee = new TARDISBee();
                            tmbee.setHasNectar(((Bee) entity).hasNectar());
                            tmbee.setHasStung(((Bee) entity).hasStung());
                            tmbee.setAnger(((Bee) entity).getAnger());
                            tmbee.setAge(((Bee) entity).getAge());
                            tmbee.setBaby(!((Bee) entity).isAdult());
                            tmbee.setName(entity.getCustomName());
                            bees.add(tmbee);
                            if (!apiary.isEmpty() || (apiary.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("BEE");
                            }
                        }
                        case CHICKEN -> {
                            TARDISMob tmchk = new TARDISMob();
                            tmchk.setAge(((Chicken) entity).getAge());
                            tmchk.setBaby(!((Chicken) entity).isAdult());
                            tmchk.setName(entity.getCustomName());
                            chickens.add(tmchk);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("CHICKEN");
                            }
                            farmtotal++;
                        }
                        case COW -> {
                            TARDISMob tmcow = new TARDISMob();
                            tmcow.setAge(((Cow) entity).getAge());
                            tmcow.setBaby(!((Cow) entity).isAdult());
                            tmcow.setName(entity.getCustomName());
                            cows.add(tmcow);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("COW");
                            }
                            farmtotal++;
                        }
                        case DONKEY, MULE, HORSE -> {
                            Tameable brokenin = (Tameable) entity;
                            AbstractHorse horse = (AbstractHorse) entity;
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
                            if (entity.getType().equals(EntityType.HORSE)) {
                                Horse pony = (Horse) horse;
                                tmhor.setHorseColour(pony.getColor());
                                tmhor.setHorseStyle(pony.getStyle());
                            }
                            tmhor.setHorseVariant(entity.getType());
                            tmhor.setTamed(brokenin.isTamed());
                            if (entity.getType().equals(EntityType.DONKEY) || entity.getType().equals(EntityType.MULE)) {
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
                            horses.add(tmhor);
                            if (!stable.isEmpty() || (stable.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("HORSE");
                            }
                        }
                        case FROG -> {
                            Frog frog = (Frog) entity;
                            TARDISFrog tmfrog = new TARDISFrog();
                            tmfrog.setType(EntityType.FROG);
                            tmfrog.setFrogVariant(frog.getVariant());
                            tmfrog.setAge(frog.getAge());
                            tmfrog.setHealth(frog.getHealth());
                            tmfrog.setName(entity.getCustomName());
                            frogs.add(tmfrog);
                            if (!mangrove.isEmpty() || (mangrove.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("FROG");
                            }
                        }
                        case LLAMA -> {
                            Tameable ll_brokenin = (Tameable) entity;
                            Llama llama = (Llama) entity;
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
                            tmlla.setTamed(ll_brokenin.isTamed());
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
                            llamas.add(tmlla);
                            if (!stall.isEmpty() || (stall.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("LLAMA");
                            }
                        }
                        case PARROT -> {
                            Tameable polly = (Tameable) entity;
                            AnimalTamer tamer = polly.getOwner();
                            boolean timeLordIsOwner = (tamer != null && tamer.getUniqueId().equals(p.getUniqueId()));
                            TARDISPet tmpet = new TARDISPet();
                            if (polly.isTamed()) {
                                if (timeLordIsOwner) {
                                    // only move tamed parrots that the time lord owns!
                                    tmpet.setTamed(true);
                                    tmpet.setOnLeftShoulder(p.getShoulderEntityLeft() != null);
                                    tmpet.setOnRightShoulder(p.getShoulderEntityRight() != null);
                                } else {
                                    break;
                                }
                            }
                            tmpet.setType(EntityType.PARROT);
                            tmpet.setVariant(((Parrot) entity).getVariant());
                            tmpet.setAge(((Parrot) entity).getAge());
                            tmpet.setBaby(!((Parrot) entity).isAdult());
                            tmpet.setName(entity.getCustomName());
                            tmpet.setSitting(((Sittable) entity).isSitting());
                            double phealth = Math.min(((Parrot) entity).getHealth(), 8D);
                            tmpet.setHealth(phealth);
                            if (timeLordIsOwner || polly.isTamed()) {
                                pets.add(tmpet);
                            } else {
                                parrots.add(tmpet);
                            }
                            if (timeLordIsOwner || !birdcage.isEmpty() || (birdcage.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("PARROT");
                            }
                        }
                        case PANDA -> {
                            TARDISPanda tmpanda = new TARDISPanda();
                            tmpanda.setAge(((Panda) entity).getAge());
                            tmpanda.setBaby(!((Panda) entity).isAdult());
                            tmpanda.setName(entity.getCustomName());
                            tmpanda.setMainGene(((Panda) entity).getMainGene());
                            tmpanda.setHiddenGene(((Panda) entity).getHiddenGene());
                            pandas.add(tmpanda);
                            if (!bamboo.isEmpty() || (bamboo.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("PANDA");
                            }
                        }
                        case PIG -> {
                            TARDISPig tmpig = new TARDISPig();
                            tmpig.setAge(((Pig) entity).getAge());
                            tmpig.setBaby(!((Pig) entity).isAdult());
                            tmpig.setName(entity.getCustomName());
                            tmpig.setSaddled(((Pig) entity).hasSaddle());
                            // eject any passengers
                            entity.eject();
                            pigs.add(tmpig);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("PIG");
                            }
                            farmtotal++;
                        }
                        case POLAR_BEAR -> {
                            TARDISMob tmbear = new TARDISMob();
                            tmbear.setAge(((PolarBear) entity).getAge());
                            tmbear.setBaby(!((PolarBear) entity).isAdult());
                            tmbear.setName(entity.getCustomName());
                            polarbears.add(tmbear);
                            if (!igloo.isEmpty() || (igloo.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("POLAR_BEAR");
                            }
                        }
                        case RABBIT -> {
                            Rabbit rabbit = (Rabbit) entity;
                            TARDISRabbit tmrabbit = new TARDISRabbit();
                            tmrabbit.setAge(rabbit.getAge());
                            tmrabbit.setBaby(!rabbit.isAdult());
                            tmrabbit.setName(rabbit.getCustomName());
                            tmrabbit.setBunnyType(rabbit.getRabbitType());
                            rabbits.add(tmrabbit);
                            if (!hutch.isEmpty() || (hutch.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("RABBIT");
                            }
                        }
                        case SHEEP -> {
                            TARDISMob tmshp = new TARDISMob();
                            tmshp.setAge(((Sheep) entity).getAge());
                            tmshp.setBaby(!((Sheep) entity).isAdult());
                            tmshp.setColour(((Sheep) entity).getColor());
                            tmshp.setName(entity.getCustomName());
                            sheep.add(tmshp);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("SHEEP");
                            }
                            farmtotal++;
                        }
                        case MUSHROOM_COW -> {
                            TARDISMooshroom tmshr = new TARDISMooshroom();
                            tmshr.setAge(((MushroomCow) entity).getAge());
                            tmshr.setBaby(!((MushroomCow) entity).isAdult());
                            tmshr.setVariant(((MushroomCow) entity).getVariant());
                            tmshr.setName(entity.getCustomName());
                            mooshrooms.add(tmshr);
                            if (!farm.isEmpty() || (farm.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                            if (taf != null) {
                                taf.doAchievement("MUSHROOM_COW");
                            }
                            farmtotal++;
                        }
                        case VILLAGER -> {
                            TARDISVillager tv = new TARDISVillager();
                            Villager v = (Villager) entity;
                            tv.setProfession(v.getProfession());
                            tv.setAge(v.getAge());
                            tv.setHealth(v.getHealth());
                            tv.setBaby(!v.isAdult());
                            tv.setName(v.getCustomName());
                            tv.setTrades(v.getRecipes());
                            tv.setLevel(v.getVillagerLevel());
                            tv.setVillagerType(v.getVillagerType());
                            tv.setExperience(v.getVillagerExperience());
                            tv.setReputation(plugin.getTardisHelper().getReputation(v, p.getUniqueId()));
                            villagers.add(tv);
                            if (!village.isEmpty() || (village.isEmpty() && plugin.getConfig().getBoolean("allow.spawn_eggs"))) {
                                entity.remove();
                            }
                        }
                        case WOLF, CAT -> {
                            Tameable tamed = (Tameable) entity;
                            if (tamed.isTamed() && tamed.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                TARDISPet pet = new TARDISPet();
                                pet.setType(entity.getType());
                                pet.setName(entity.getCustomName());
                                double health;
                                if (entity.getType().equals(EntityType.WOLF)) {
                                    pet.setAge(((Wolf) entity).getAge());
                                    pet.setSitting(((Sittable) entity).isSitting());
                                    pet.setColour(((Wolf) entity).getCollarColor());
                                    health = Math.min(((Wolf) entity).getHealth(), 8D);
                                    pet.setHealth(health);
                                    pet.setBaby(!((Wolf) entity).isAdult());
                                } else {
                                    pet.setAge(((Cat) entity).getAge());
                                    pet.setSitting(((Sittable) entity).isSitting());
                                    pet.setCatType(((Cat) entity).getCatType());
                                    pet.setColour(((Cat) entity).getCollarColor());
                                    health = Math.min(((Cat) entity).getHealth(), 8D);
                                    pet.setHealth(health);
                                    pet.setBaby(!((Cat) entity).isAdult());
                                }
                                pets.add(pet);
                                entity.remove();
                            }
                        }
                        default -> {
                            // do nothing
                        }
                    }
                }
                ItemStack fishBucket = p.getInventory().getItemInOffHand();
                if (fishBucket != null && TARDISMaterials.fish_buckets.contains(fishBucket.getType())) {
                    fish = new TARDISFish();
                    fish.setType(TARDISMaterials.fishMap.get(fishBucket.getType()));
                    if (fishBucket.getType().equals(Material.TROPICAL_FISH_BUCKET)) {
                        TropicalFishBucketMeta fbim = (TropicalFishBucketMeta) fishBucket.getItemMeta();
                        fish.setColour(fbim.getBodyColor());
                        fish.setPattern(fbim.getPattern());
                        fish.setPatternColour(fbim.getPatternColor());
                    }
                }
                if (!bees.isEmpty() || farmtotal > 0 || !horses.isEmpty() || !villagers.isEmpty() || !pets.isEmpty() || !polarbears.isEmpty() || !llamas.isEmpty() || !parrots.isEmpty() || !pandas.isEmpty() || !rabbits.isEmpty() || fish != null || !followers.isEmpty() || !axolotls.isEmpty() || !frogs.isEmpty()) {
                    boolean canfarm = switch (plugin.getInvManager()) {
                        case MULTIVERSE -> TARDISMultiverseInventoriesChecker.checkWorldsCanShare(from, to);
                        default -> true;
                    };
                    if (!canfarm) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "WORLD_NO_FARM");
                        plugin.getTrackerKeeper().getFarming().remove(p.getUniqueId());
                        return null;
                    }
                }
                if (!apiary.isEmpty()) {
                    // get location of apiary room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(apiary);
                    if (!bees.isEmpty()) {
                        Location beehive = TARDISStaticLocationGetters.getSpawnLocationFromDB(apiary);
                        while (!world.getChunkAt(beehive).isLoaded()) {
                            world.getChunkAt(beehive).load();
                        }
                        bees.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Bee bee = (Bee) world.spawnEntity(beehive, EntityType.BEE);
                            bee.setHasNectar(e.hasNectar());
                            bee.setHasStung(e.hasStung());
                            bee.setAnger(e.getAnger());
                            bee.setAge(e.getAge());
                            if (e.isBaby()) {
                                bee.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                bee.setCustomName(name);
                            }
                            bee.setRemoveWhenFarAway(false);
                        });
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !bees.isEmpty()) {
                    // give spawn eggs
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.BEE_SPAWN_EGG, bees.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!bees.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_APIARY");
                }
                if (!aquarium.isEmpty() && fish != null) {
                    // get location of aquarium room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(aquarium);
                    Location fish_tank = TARDISStaticLocationGetters.getSpawnLocationFromDB(aquarium);
                    switch (fish.getType()) {
                        case COD -> fish_tank.add(3.0d, 1.5d, 3.0d);
                        case PUFFERFISH -> fish_tank.add(-3.0d, 1.5d, 3.0d);
                        case SALMON -> fish_tank.add(3.0d, 1.5d, -3.0d);
                        default -> // TROPICAL_FISH
                                fish_tank.add(-3.0d, 1.5d, -3.0d);
                    }
                    while (!world.getChunkAt(fish_tank).isLoaded()) {
                        world.getChunkAt(fish_tank).load();
                    }
                    plugin.setTardisSpawn(true);
                    Entity tropical = world.spawnEntity(fish_tank, fish.getType());
                    if (fish.getType().equals(EntityType.TROPICAL_FISH)) {
                        TropicalFish tf = (TropicalFish) tropical;
                        tf.setBodyColor(fish.getColour());
                        tf.setPattern(fish.getPattern());
                        tf.setPatternColor(fish.getPatternColour());
                    }
                    // change fish bucket to empty bucket
                    p.getInventory().getItemInMainHand().setType(Material.BUCKET);
                    p.updateInventory();
                }
                if (!bamboo.isEmpty()) {
                    // get location of bamboo room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(bamboo);
                    if (!pandas.isEmpty()) {
                        Location forest = TARDISStaticLocationGetters.getSpawnLocationFromDB(bamboo);
                        while (!world.getChunkAt(forest).isLoaded()) {
                            world.getChunkAt(forest).load();
                        }
                        pandas.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Panda panda = (Panda) world.spawnEntity(forest, EntityType.PANDA);
                            panda.setMainGene(e.getMainGene());
                            panda.setHiddenGene(e.getHiddenGene());
                            panda.setAge(e.getAge());
                            if (e.isBaby()) {
                                panda.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                panda.setCustomName(name);
                            }
                            panda.setRemoveWhenFarAway(false);
                        });
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !pandas.isEmpty()) {
                    // give spawn eggs
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.PANDA_SPAWN_EGG, pandas.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!pandas.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_BAMBOO");
                }
                if (!farm.isEmpty()) {
                    // get location of farm room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(farm);
                    if (!chickens.isEmpty()) {
                        Location chicken_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(farm).add(3, 0, -3);
                        while (!world.getChunkAt(chicken_pen).isLoaded()) {
                            world.getChunkAt(chicken_pen).load();
                        }
                        chickens.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Chicken chicken = (Chicken) world.spawnEntity(chicken_pen, EntityType.CHICKEN);
                            chicken.setAge(e.getAge());
                            if (e.isBaby()) {
                                chicken.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                chicken.setCustomName(name);
                            }
                            chicken.setRemoveWhenFarAway(false);
                        });
                    }
                    if (!cows.isEmpty()) {
                        Location cow_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(farm).add(3, 0, 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        cows.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Cow cow = (Cow) world.spawnEntity(cow_pen, EntityType.COW);
                            cow.setAge(e.getAge());
                            if (e.isBaby()) {
                                cow.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                cow.setCustomName(name);
                            }
                            cow.setRemoveWhenFarAway(false);
                        });
                    }
                    if (!pigs.isEmpty()) {
                        Location pig_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(farm).add(-3, 0, -3);
                        while (!world.getChunkAt(pig_pen).isLoaded()) {
                            world.getChunkAt(pig_pen).load();
                        }
                        pigs.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Pig pig = (Pig) world.spawnEntity(pig_pen, EntityType.PIG);
                            pig.setAge(e.getAge());
                            if (e.isBaby()) {
                                pig.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                pig.setCustomName(name);
                            }
                            pig.setSaddle(e.isSaddled());
                            pig.setRemoveWhenFarAway(false);
                        });
                    }
                    if (!sheep.isEmpty()) {
                        Location sheep_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(farm).add(-3, 0, 3);
                        while (!world.getChunkAt(sheep_pen).isLoaded()) {
                            world.getChunkAt(sheep_pen).load();
                        }
                        sheep.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Sheep ewe = (Sheep) world.spawnEntity(sheep_pen, EntityType.SHEEP);
                            ewe.setAge(e.getAge());
                            ewe.setColor(e.getColour());
                            if (e.isBaby()) {
                                ewe.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                ewe.setCustomName(name);
                            }
                            ewe.setRemoveWhenFarAway(false);
                        });
                    }
                    if (!mooshrooms.isEmpty()) {
                        Location cow_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(farm).add(3, 0, 3);
                        while (!world.getChunkAt(cow_pen).isLoaded()) {
                            world.getChunkAt(cow_pen).load();
                        }
                        mooshrooms.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            MushroomCow fungi = (MushroomCow) world.spawnEntity(cow_pen, EntityType.MUSHROOM_COW);
                            fungi.setAge(e.getAge());
                            if (e.isBaby()) {
                                fungi.setBaby();
                            }
                            fungi.setVariant(e.getVariant());
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
                    if (!chickens.isEmpty()) {
                        ItemStack is = new ItemStack(Material.CHICKEN_SPAWN_EGG, chickens.size());
                        inv.addItem(is);
                    }
                    if (!cows.isEmpty()) {
                        ItemStack is = new ItemStack(Material.COW_SPAWN_EGG, cows.size());
                        inv.addItem(is);
                    }
                    if (!pigs.isEmpty()) {
                        ItemStack is = new ItemStack(Material.PIG_SPAWN_EGG, pigs.size());
                        inv.addItem(is);
                    }
                    if (!sheep.isEmpty()) {
                        ItemStack is = new ItemStack(Material.SHEEP_SPAWN_EGG, sheep.size());
                        inv.addItem(is);
                    }
                    if (!mooshrooms.isEmpty()) {
                        ItemStack is = new ItemStack(Material.MOOSHROOM_SPAWN_EGG, mooshrooms.size());
                        inv.addItem(is);
                    }
                    p.updateInventory();
                } else if (farmtotal > 0) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM");
                }
                if (!geode.isEmpty()) {
                    // get location of geode room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(geode);
                    if (!axolotls.isEmpty()) {
                        Location pool = TARDISStaticLocationGetters.getSpawnLocationFromDB(geode);
                        while (!world.getChunkAt(pool).isLoaded()) {
                            world.getChunkAt(pool).load();
                        }
                        axolotls.forEach((x) -> {
                            plugin.setTardisSpawn(true);
                            Axolotl axolotl = (Axolotl) world.spawnEntity(pool, EntityType.AXOLOTL);
                            axolotl.setVariant(x.getAxolotlVariant());
                            axolotl.setAge(x.getAge());
                            if (x.isBaby()) {
                                axolotl.setBaby();
                            }
                            String name = x.getName();
                            if (name != null && !name.isEmpty()) {
                                axolotl.setCustomName(name);
                            }
                            axolotl.setRemoveWhenFarAway(false);
                        });
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !axolotls.isEmpty()) {
                    // give spawn eggs
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.AXOLOTL_SPAWN_EGG, axolotls.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!axolotls.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_GEODE");
                }
                if (!stable.isEmpty() && !horses.isEmpty()) {
                    // get location of stable room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(stable);
                    Location horse_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(stable);
                    while (!world.getChunkAt(horse_pen).isLoaded()) {
                        world.getChunkAt(horse_pen).load();
                    }
                    horses.forEach((e) -> {
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
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !horses.isEmpty()) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.HORSE_SPAWN_EGG, horses.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!horses.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_STABLE");
                }
                if (!stall.isEmpty() && !llamas.isEmpty()) {
                    // get location of stable room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(stall);
                    Location llama_pen = TARDISStaticLocationGetters.getSpawnLocationFromDB(stall);
                    while (!world.getChunkAt(llama_pen).isLoaded()) {
                        world.getChunkAt(llama_pen).load();
                    }
                    llamas.forEach((ll) -> {
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
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !llamas.isEmpty()) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.LLAMA_SPAWN_EGG, llamas.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!llamas.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_STALL");
                }
                if (!hutch.isEmpty() && !rabbits.isEmpty()) {
                    // get location of hutch room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(hutch);
                    Location rabbit_hutch = TARDISStaticLocationGetters.getSpawnLocationFromDB(hutch);
                    while (!world.getChunkAt(rabbit_hutch).isLoaded()) {
                        world.getChunkAt(rabbit_hutch).load();
                    }
                    rabbits.forEach((e) -> {
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
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !rabbits.isEmpty()) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.RABBIT_SPAWN_EGG, rabbits.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!rabbits.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_HUTCH");
                }
                if (!village.isEmpty() && !villagers.isEmpty()) {
                    // get location of village room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(village);
                    Location v_room = TARDISStaticLocationGetters.getSpawnLocationFromDB(village);
                    while (!world.getChunkAt(v_room).isLoaded()) {
                        world.getChunkAt(v_room).load();
                    }
                    villagers.forEach((e) -> {
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
                        npc.setVillagerExperience(e.getExperience());
                        npc.setVillagerLevel(e.getLevel());
                        npc.setVillagerType(e.getVillagerType());
                        plugin.getTardisHelper().setReputation(npc, p.getUniqueId(), e.getReputation());
                        String name = e.getName();
                        if (name != null && !name.isEmpty()) {
                            npc.setCustomName(name);
                        }
                        npc.setRemoveWhenFarAway(false);
                    });
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !villagers.isEmpty()) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.VILLAGER_SPAWN_EGG, villagers.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!villagers.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_VILLAGE");
                }
                if (!igloo.isEmpty() && !polarbears.isEmpty()) {
                    // get location of igloo room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(igloo);
                    Location i_room = TARDISStaticLocationGetters.getSpawnLocationFromDB(igloo);
                    while (!world.getChunkAt(i_room).isLoaded()) {
                        world.getChunkAt(i_room).load();
                    }
                    polarbears.forEach((e) -> {
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
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !polarbears.isEmpty()) {
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG, polarbears.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!polarbears.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_IGLOO");
                }
                if (!birdcage.isEmpty() && !parrots.isEmpty()) {
                    // get location of birdcage room
                    World world = TARDISStaticLocationGetters.getWorldFromSplitString(birdcage);
                    Location b_room = TARDISStaticLocationGetters.getSpawnLocationFromDB(birdcage);
                    while (!world.getChunkAt(b_room).isLoaded()) {
                        world.getChunkAt(b_room).load();
                    }
                    parrots.forEach((e) -> {
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
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !parrots.isEmpty()) {
                    // give spawn eggs
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.PARROT_SPAWN_EGG, parrots.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!parrots.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_BIRDCAGE");
                }
                if (!mangrove.isEmpty()) {
                    if (!frogs.isEmpty()) {
                        // get location of bamboo room
                        Location swamp = TARDISStaticLocationGetters.getSpawnLocationFromDB(mangrove);
                        World world = swamp.getWorld();
                        while (!world.getChunkAt(swamp).isLoaded()) {
                            world.getChunkAt(swamp).load();
                        }
                        frogs.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Frog frog = (Frog) world.spawnEntity(swamp, EntityType.FROG);
                            frog.setVariant(e.getFrogVariant());
                            frog.setAge(e.getAge());
                            frog.setHealth(e.getHealth());
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                frog.setCustomName(name);
                            }
                            frog.setRemoveWhenFarAway(false);
                        });
                    }
                } else if (plugin.getConfig().getBoolean("allow.spawn_eggs") && !frogs.isEmpty()) {
                    // give spawn eggs
                    Inventory inv = p.getInventory();
                    ItemStack is = new ItemStack(Material.FROG_SPAWN_EGG, frogs.size());
                    inv.addItem(is);
                    p.updateInventory();
                } else if (!frogs.isEmpty()) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_MANGROVE");
                }
            }
        }
        egg.remove();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getFarming().remove(p.getUniqueId()), 20L);
        return new TARDISPetsAndFollowers(pets, followers);
    }

    public TARDISPetsAndFollowers exitPets(Player player) {
        List<TARDISPet> pets = new ArrayList<>();
        List<TARDISFollower> followers = new ArrayList<>();
        List<Entity> mobs = player.getNearbyEntities(3.5D, 3.5D, 3.5D);
        for (Entity entity : mobs) {
            if (entity.getType().equals(EntityType.CAT) || entity.getType().equals(EntityType.WOLF) || entity.getType().equals(EntityType.PARROT)) {
                Tameable tamed = (Tameable) entity;
                if (tamed.isTamed() && tamed.getOwner().getUniqueId().equals(player.getUniqueId())) {
                    TARDISPet pet = new TARDISPet();
                    pet.setType(entity.getType());
                    String pet_name = entity.getCustomName();
                    if (pet_name != null) {
                        pet.setName(pet_name);
                    }
                    double health;
                    switch (entity.getType()) {
                        case WOLF -> {
                            pet.setAge(((Wolf) entity).getAge());
                            pet.setSitting(((Wolf) entity).isSitting());
                            pet.setColour(((Wolf) entity).getCollarColor());
                            health = Math.min(((Wolf) entity).getHealth(), 8D);
                            pet.setHealth(health);
                            pet.setBaby(!((Wolf) entity).isAdult());
                        }
                        case CAT -> {
                            pet.setAge(((Cat) entity).getAge());
                            pet.setSitting(((Cat) entity).isSitting());
                            pet.setCatType(((Cat) entity).getCatType());
                            pet.setColour(((Cat) entity).getCollarColor());
                            health = Math.min(((Cat) entity).getHealth(), 8D);
                            pet.setHealth(health);
                            pet.setBaby(!((Cat) entity).isAdult());
                        }
                        case PARROT -> {
                            pet.setAge(((Parrot) entity).getAge());
                            pet.setSitting(((Parrot) entity).isSitting());
                            pet.setVariant(((Parrot) entity).getVariant());
                            health = Math.min(((Parrot) entity).getHealth(), 8D);
                            pet.setHealth(health);
                            pet.setBaby(!((Parrot) entity).isAdult());
                            pet.setOnLeftShoulder(player.getShoulderEntityLeft() != null);
                            pet.setOnRightShoulder(player.getShoulderEntityRight() != null);
                        }
                        default -> {
                        }
                    }
                    pets.add(pet);
                    entity.remove();
                }
            } else if (entity.getType().equals(EntityType.ARMOR_STAND) && plugin.getConfig().getBoolean("modules.weeping_angels")) {
                TARDISFollower follower = new TARDISFollower(entity, player.getUniqueId());
                if (follower.isValid()) {
                    followers.add(follower);
                    entity.remove();
                }
            }
        }
        return new TARDISPetsAndFollowers(pets, followers);
    }
}
