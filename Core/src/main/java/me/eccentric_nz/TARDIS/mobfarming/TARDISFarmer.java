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
package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.data.Farm;
import me.eccentric_nz.TARDIS.database.data.FarmPrefs;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarmingPrefs;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.InventoryManager;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMultiverseInventoriesChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisweepingangels.utils.FollowerChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        // spawn an entity at this location, so we can get nearby entities - an egg will do
        World w = l.getWorld();
        Entity egg = w.spawnEntity(l, EntityType.EGG);
        List<Entity> mobs = egg.getNearbyEntities(3.75D, 3.75D, 3.75D);
        List<TARDISPet> pets = new ArrayList<>();
        List<Follower> followers = new ArrayList<>();
        UUID uuid = p.getUniqueId();
        if (!mobs.isEmpty()) {
            List<TARDISAllay> allays = new ArrayList<>();
            List<TARDISAxolotl> axolotls = new ArrayList<>();
            List<TARDISHorse> horses = new ArrayList<>();
            List<TARDISLlama> llamas = new ArrayList<>();
            List<TARDISMob> chickens = new ArrayList<>();
            List<TARDISMob> cows = new ArrayList<>();
            TARDISFish fish = null;
            List<TARDISMooshroom> mooshrooms = new ArrayList<>();
            List<TARDISMob> sheep = new ArrayList<>();
            List<TARDISMob> sniffers = new ArrayList<>();
            List<TARDISMob> striders = new ArrayList<>();
            List<TARDISPet> parrots = new ArrayList<>();
            List<TARDISPig> pigs = new ArrayList<>();
            List<TARDISMob> polarbears = new ArrayList<>();
            List<TARDISRabbit> rabbits = new ArrayList<>();
            List<TARDISBee> bees = new ArrayList<>();
            List<TARDISVillager> villagers = new ArrayList<>();
            List<TARDISPanda> pandas = new ArrayList<>();
            List<TARDISFrog> frogs = new ArrayList<>();
            List<TARDISHorse> camels = new ArrayList<>();
            // are we doing an achievement?
            TARDISAchievementFactory taf = null;
            if (plugin.getAchievementConfig().getBoolean("farm.enabled")) {
                taf = new TARDISAchievementFactory(plugin, p, Advancement.FARM, 19);
            }
            boolean spawnEggs = plugin.getConfig().getBoolean("allow.spawn_eggs");
            // count total farm mobs
            int farmtotal = 0;
            // is there a farm room?
            ResultSetFarming rs = new ResultSetFarming(plugin, id);
            if (rs.resultSet()) {
                Farm farming = rs.getFarming();
                String allay = farming.getAllay();
                String apiary = farming.getApiary();
                String aquarium = farming.getAquarium();
                String bamboo = farming.getBamboo();
                String birdcage = farming.getBirdcage();
                String farm = farming.getFarm();
                String geode = farming.getGeode();
                String hutch = farming.getHutch();
                String igloo = farming.getIgloo();
                String iistubil = farming.getIistubil();
                String lava = farming.getLava();
                String mangrove = farming.getMangrove();
                String pen = farming.getPen();
                String stable = farming.getStable();
                String stall = farming.getStall();
                String village = farming.getVillage();
                // get farming prefs
                ResultSetFarmingPrefs rsfp = new ResultSetFarmingPrefs(plugin, uuid.toString());
                FarmPrefs farmPrefs;
                if (rsfp.resultSet()) {
                    farmPrefs = rsfp.getData();
                } else {
                    // if not set then default to true
                    farmPrefs = new FarmPrefs(uuid, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
                }
                // collate the mobs
                for (Entity entity : mobs) {
                    switch (entity.getType()) {
                        case HUSK -> {
                            if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                                FollowerChecker fc = new FollowerChecker();
                                fc.checkEntity(entity, uuid);
                                if (fc.isValid()) {
                                    followers.add(fc.getFollower());
                                    entity.remove();
                                }
                            }
                        }
                        case ALLAY -> {
                            if (farmPrefs.shouldFarmAllay() && (!allay.isEmpty() || spawnEggs)) {
                                Allay a = (Allay) entity;
                                TARDISAllay tma = new TARDISAllay();
                                tma.setCanDuplicate(a.canDuplicate());
                                tma.setDuplicationCooldown(a.getDuplicationCooldown());
                                tma.setLikedPlayer(a.getMemory(MemoryKey.LIKED_PLAYER));
                                tma.setInventory(a.getInventory().getContents());
                                tma.setName(entity.getCustomName());
                                allays.add(tma);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("ALLAY");
                                }
                            }
                        }
                        case AXOLOTL -> {
                            if (farmPrefs.shouldFarmAxolotls() && (!geode.isEmpty() || spawnEggs)) {
                                Axolotl axolotl = (Axolotl) entity;
                                TARDISAxolotl tmaxl = new TARDISAxolotl();
                                tmaxl.setAxolotlVariant(axolotl.getVariant());
                                tmaxl.setAge(axolotl.getAge());
                                tmaxl.setBaby(!axolotl.isAdult());
                                tmaxl.setName(entity.getCustomName());
                                axolotls.add(tmaxl);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("AXOLOTL");
                                }
                            }
                        }
                        case BEE -> {
                            if (farmPrefs.shouldFarmBees() && (!apiary.isEmpty() || spawnEggs)) {
                                Bee bee = (Bee) entity;
                                TARDISBee tmbee = new TARDISBee();
                                tmbee.setHasNectar(bee.hasNectar());
                                tmbee.setHasStung(bee.hasStung());
                                tmbee.setAnger(bee.getAnger());
                                tmbee.setAge(bee.getAge());
                                tmbee.setBaby(!bee.isAdult());
                                tmbee.setName(entity.getCustomName());
                                bees.add(tmbee);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("BEE");
                                }
                            }
                        }
                        case CAMEL -> {
                            if (farmPrefs.shouldFarmCamels() && (!iistubil.isEmpty() || spawnEggs)) {
                                Camel camel = (Camel) entity;
                                TARDISHorse hump = new TARDISHorse();
                                hump.setAge(camel.getAge());
                                hump.setBaby(!camel.isAdult());
                                hump.setHorseInventory(camel.getInventory().getContents());
                                hump.setName(camel.getCustomName());
                                hump.setDomesticity(camel.getDomestication());
                                camels.add(hump);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("CAMEL");
                                }
                            }
                        }
                        case CHICKEN -> {
                            if (farmPrefs.shouldFarmLivestock() && (!farm.isEmpty() || spawnEggs)) {
                                Chicken chicken = (Chicken) entity;
                                TARDISMob tmchk = new TARDISMob();
                                tmchk.setAge(chicken.getAge());
                                tmchk.setBaby(!chicken.isAdult());
                                tmchk.setName(entity.getCustomName());
                                chickens.add(tmchk);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("CHICKEN");
                                }
                                farmtotal++;
                            }
                        }
                        case COW -> {
                            if (farmPrefs.shouldFarmLivestock() && (!farm.isEmpty() || spawnEggs)) {
                                Cow cow = (Cow) entity;
                                TARDISMob tmcow = new TARDISMob();
                                tmcow.setAge(cow.getAge());
                                tmcow.setBaby(!cow.isAdult());
                                tmcow.setName(entity.getCustomName());
                                cows.add(tmcow);
                                    entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("COW");
                                }
                                farmtotal++;
                            }
                        }
                        case DONKEY, MULE, HORSE -> {
                            if (farmPrefs.shouldFarmHorses() && (!stable.isEmpty() || spawnEggs)) {
                                AbstractHorse horse = (AbstractHorse) entity;
                                // if horse has a passenger, eject them!
                                horse.eject();
                                // don't farm other player's tamed horses
                                if (horse.isTamed()) {
                                    OfflinePlayer owner = (OfflinePlayer) horse.getOwner();
                                    if (owner != null && !owner.getUniqueId().equals(uuid)) {
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
                                tmhor.setTamed(horse.isTamed());
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
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("HORSE");
                                }
                            }
                        }
                        case FROG -> {
                            if (farmPrefs.shouldFarmFrogs() && (!mangrove.isEmpty() || spawnEggs)) {
                                Frog frog = (Frog) entity;
                                TARDISFrog tmfrog = new TARDISFrog();
                                tmfrog.setType(EntityType.FROG);
                                tmfrog.setFrogVariant(frog.getVariant());
                                tmfrog.setAge(frog.getAge());
                                tmfrog.setHealth(frog.getHealth());
                                tmfrog.setName(entity.getCustomName());
                                frogs.add(tmfrog);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("FROG");
                                }
                            }
                        }
                        case LLAMA -> {
                            if (farmPrefs.shouldFarmLlamas() && (!stall.isEmpty() || spawnEggs)) {
                                Llama llama = (Llama) entity;
                                // if llama has a passenger, eject them!
                                llama.eject();
                                // don't farm other player's tamed llamas
                                if (llama.isTamed()) {
                                    OfflinePlayer owner = (OfflinePlayer) llama.getOwner();
                                    if (owner != null && !owner.getUniqueId().equals(uuid)) {
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
                                tmlla.setTamed(llama.isTamed());
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
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("LLAMA");
                                }
                            }
                        }
                        case PARROT -> {
                            if (farmPrefs.shouldFarmParrots()) {
                                Parrot parrot = (Parrot) entity;
                                AnimalTamer tamer = parrot.getOwner();
                                boolean timeLordIsOwner = (tamer != null && tamer.getUniqueId().equals(uuid));
                                TARDISPet tmpet = new TARDISPet();
                                if (parrot.isTamed()) {
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
                                tmpet.setVariant(parrot.getVariant());
                                tmpet.setAge(parrot.getAge());
                                tmpet.setBaby(!parrot.isAdult());
                                tmpet.setName(entity.getCustomName());
                                tmpet.setSitting(((Sittable) entity).isSitting());
                                double phealth = Math.min(parrot.getHealth(), 8D);
                                tmpet.setHealth(phealth);
                                if (timeLordIsOwner || parrot.isTamed()) {
                                    pets.add(tmpet);
                                } else {
                                    parrots.add(tmpet);
                                }
                                if (timeLordIsOwner || !birdcage.isEmpty() || (birdcage.isEmpty() && spawnEggs)) {
                                    entity.remove();
                                    if (taf != null) {
                                        taf.doAchievement("PARROT");
                                    }
                                }
                            }
                        }
                        case PANDA -> {
                            if (farmPrefs.shouldFarmPandas() && (!bamboo.isEmpty() || spawnEggs)) {
                                Panda panda = (Panda) entity;
                                TARDISPanda tmpanda = new TARDISPanda();
                                tmpanda.setAge(panda.getAge());
                                tmpanda.setBaby(!panda.isAdult());
                                tmpanda.setName(entity.getCustomName());
                                tmpanda.setMainGene(panda.getMainGene());
                                tmpanda.setHiddenGene(panda.getHiddenGene());
                                pandas.add(tmpanda);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("PANDA");
                                }
                            }
                        }
                        case PIG -> {
                            if (farmPrefs.shouldFarmLivestock() && (!farm.isEmpty() || spawnEggs)) {
                                Pig pig = (Pig) entity;
                                TARDISPig tmpig = new TARDISPig();
                                tmpig.setAge(pig.getAge());
                                tmpig.setBaby(!pig.isAdult());
                                tmpig.setName(entity.getCustomName());
                                tmpig.setSaddled(pig.hasSaddle());
                                // eject any passengers
                                entity.eject();
                                pigs.add(tmpig);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("PIG");
                                }
                                farmtotal++;
                            }
                        }
                        case POLAR_BEAR -> {
                            if (farmPrefs.shouldFarmPolarBears() && (!igloo.isEmpty() || spawnEggs)) {
                                PolarBear polarBear = (PolarBear) entity;
                                TARDISMob tmbear = new TARDISMob();
                                tmbear.setAge(polarBear.getAge());
                                tmbear.setBaby(!polarBear.isAdult());
                                tmbear.setName(entity.getCustomName());
                                polarbears.add(tmbear);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("POLAR_BEAR");
                                }
                            }
                        }
                        case RABBIT -> {
                            if (farmPrefs.shouldFarmRabbits() && (!hutch.isEmpty() || spawnEggs)) {
                                Rabbit rabbit = (Rabbit) entity;
                                TARDISRabbit tmrabbit = new TARDISRabbit();
                                tmrabbit.setAge(rabbit.getAge());
                                tmrabbit.setBaby(!rabbit.isAdult());
                                tmrabbit.setName(rabbit.getCustomName());
                                tmrabbit.setBunnyType(rabbit.getRabbitType());
                                rabbits.add(tmrabbit);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("RABBIT");
                                }
                            }
                        }
                        case SHEEP -> {
                            if (farmPrefs.shouldFarmLivestock() && (!farm.isEmpty() || spawnEggs)) {
                                Sheep lamb = (Sheep) entity;
                                TARDISMob tmshp = new TARDISMob();
                                tmshp.setAge(lamb.getAge());
                                tmshp.setBaby(!lamb.isAdult());
                                tmshp.setColour(lamb.getColor());
                                tmshp.setName(entity.getCustomName());
                                sheep.add(tmshp);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("SHEEP");
                                }
                                farmtotal++;
                            }
                        }
                        case SNIFFER -> {
                            if (farmPrefs.shouldFarmSniffers() && (!pen.isEmpty() || spawnEggs)) {
                                Sniffer sniffer = (Sniffer) entity;
                                TARDISMob tmsniffer = new TARDISMob();
                                tmsniffer.setAge(sniffer.getAge());
                                tmsniffer.setBaby(!sniffer.isAdult());
                                tmsniffer.setName(entity.getCustomName());
                                sniffers.add(tmsniffer);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("SNIFFER");
                                }
                            }
                        }
                        case STRIDER -> {
                            if (farmPrefs.shouldFarmStriders() && (!lava.isEmpty() || spawnEggs)) {
                                Strider strider = (Strider) entity;
                                TARDISMob tmsstrider = new TARDISMob();
                                tmsstrider.setAge(strider.getAge());
                                tmsstrider.setBaby(!strider.isAdult());
                                tmsstrider.setName(entity.getCustomName());
                                striders.add(tmsstrider);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("STRIDER");
                                }
                            }
                        }
                        case MOOSHROOM -> {
                            if (farmPrefs.shouldFarmLivestock() && (!farm.isEmpty() || spawnEggs)) {
                                MushroomCow mushroomCow = (MushroomCow) entity;
                                TARDISMooshroom tmshr = new TARDISMooshroom();
                                tmshr.setAge(mushroomCow.getAge());
                                tmshr.setBaby(!mushroomCow.isAdult());
                                tmshr.setVariant(mushroomCow.getVariant());
                                tmshr.setName(entity.getCustomName());
                                mooshrooms.add(tmshr);
                                entity.remove();
                                if (taf != null) {
                                    taf.doAchievement("MUSHROOM_COW");
                                }
                                farmtotal++;
                            }
                        }
                        case VILLAGER -> {
                            if (farmPrefs.shouldFarmVillagers() && (!village.isEmpty() || spawnEggs)) {
                                Villager v = (Villager) entity;
                                TARDISVillager tv = new TARDISVillager();
                                tv.setProfession(v.getProfession());
                                tv.setAge(v.getAge());
                                tv.setHealth(v.getHealth());
                                tv.setBaby(!v.isAdult());
                                tv.setName(v.getCustomName());
                                tv.setTrades(v.getRecipes());
                                tv.setLevel(v.getVillagerLevel());
                                tv.setVillagerType(v.getVillagerType());
                                tv.setExperience(v.getVillagerExperience());
                                tv.setReputation(plugin.getTardisHelper().getReputation(v, uuid));
                                villagers.add(tv);
                                entity.remove();
                            }
                        }
                        case WOLF, CAT -> {
                            Tameable tamed = (Tameable) entity;
                            if (tamed.isTamed() && tamed.getOwner().getUniqueId().equals(uuid)) {
                                TARDISPet pet = new TARDISPet();
                                pet.setType(entity.getType());
                                pet.setName(entity.getCustomName());
                                double health;
                                if (entity.getType().equals(EntityType.WOLF)) {
                                    Wolf wolf = (Wolf) entity;
                                    pet.setAge(wolf.getAge());
                                    pet.setSitting(wolf.isSitting());
                                    pet.setColour(wolf.getCollarColor());
                                    pet.setWolfType(wolf.getVariant());
                                    health = Math.min(wolf.getHealth(), 8D);
                                    pet.setHealth(health);
                                    pet.setBaby(!wolf.isAdult());
                                } else {
                                    Cat cat = (Cat) entity;
                                    pet.setAge(cat.getAge());
                                    pet.setSitting(cat.isSitting());
                                    pet.setCatType(cat.getCatType());
                                    pet.setColour(cat.getCollarColor());
                                    health = Math.min(cat.getHealth(), 8D);
                                    pet.setHealth(health);
                                    pet.setBaby(!cat.isAdult());
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
                if (farmPrefs.shouldFarmFish()) {
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
                }
                if (!bees.isEmpty() || farmtotal > 0 || !horses.isEmpty() || !villagers.isEmpty() || !pets.isEmpty() || !polarbears.isEmpty() || !llamas.isEmpty() || !parrots.isEmpty() || !pandas.isEmpty() || !rabbits.isEmpty() || fish != null || !followers.isEmpty() || !axolotls.isEmpty() || !frogs.isEmpty() || !allays.isEmpty() || !sniffers.isEmpty() || !striders.isEmpty() || !camels.isEmpty()) {
                    boolean canfarm = plugin.getInvManager() != InventoryManager.MULTIVERSE || TARDISMultiverseInventoriesChecker.checkWorldsCanShare(from, to);
                    if (!canfarm) {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "WORLD_NO_FARM");
                        plugin.getTrackerKeeper().getFarming().remove(uuid);
                        return null;
                    }
                }
                if (farmPrefs.shouldFarmAllay() && !allays.isEmpty()) {
                    if (!allay.isEmpty()) {
                        // get location of allay room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(allay);
                        Location house = TARDISStaticLocationGetters.getSpawnLocationFromDB(allay);
                        while (!world.getChunkAt(house).isLoaded()) {
                            world.getChunkAt(house).load();
                        }
                        allays.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Allay a = (Allay) world.spawnEntity(house, EntityType.ALLAY);
                            a.setCanDuplicate(e.canDuplicate());
                            a.setDuplicationCooldown(e.getDuplicationCooldown());
                            a.getInventory().setContents(e.getInventory());
                            if (e.getLikedPlayer() != null) {
                                a.setMemory(MemoryKey.LIKED_PLAYER, e.getLikedPlayer());
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                a.setCustomName(name);
                            }
                            a.setRemoveWhenFarAway(false);
                        });
                    } else if (spawnEggs) {
                        // give spawn eggs
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.ALLAY_SPAWN_EGG, allays.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_ALLAY");
                    }
                }
                if (farmPrefs.shouldFarmBees() && !bees.isEmpty()) {
                    if (!apiary.isEmpty()) {
                        // get location of apiary room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(apiary);
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
                    } else if (spawnEggs) {
                        // give spawn eggs
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.BEE_SPAWN_EGG, bees.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_APIARY");
                    }
                }
                if (farmPrefs.shouldFarmFish() && !aquarium.isEmpty() && fish != null) {
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
                    p.getInventory().getItemInOffHand().setType(Material.BUCKET);
                    p.updateInventory();
                }
                if (farmPrefs.shouldFarmPandas() && !pandas.isEmpty()) {
                    if (!bamboo.isEmpty()) {
                        // get location of bamboo room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(bamboo);
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
                    } else if (spawnEggs) {
                        // give spawn eggs
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.PANDA_SPAWN_EGG, pandas.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_BAMBOO");
                    }
                }
                if (farmPrefs.shouldFarmLivestock() && farmtotal > 0) {
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
                                MushroomCow fungi = (MushroomCow) world.spawnEntity(cow_pen, EntityType.MOOSHROOM);
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
                    } else if (spawnEggs) {
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
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM");
                    }
                }
                if (farmPrefs.shouldFarmAxolotls() && !axolotls.isEmpty()) {
                    if (!geode.isEmpty()) {
                        // get location of geode room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(geode);
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
                    } else if (spawnEggs) {
                        // give spawn eggs
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.AXOLOTL_SPAWN_EGG, axolotls.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_GEODE");
                    }
                }
                if (farmPrefs.shouldFarmHorses() && !horses.isEmpty()) {
                    if (!stable.isEmpty()) {
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
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.HORSE_SPAWN_EGG, horses.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_STABLE");
                    }
                }
                if (farmPrefs.shouldFarmLlamas() && !llamas.isEmpty()) {
                    if (!stall.isEmpty()) {
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
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.LLAMA_SPAWN_EGG, llamas.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_STALL");
                    }
                }
                if (farmPrefs.shouldFarmRabbits() && !rabbits.isEmpty()) {
                    if (!hutch.isEmpty()) {
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
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.RABBIT_SPAWN_EGG, rabbits.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_HUTCH");
                    }
                }
                if (farmPrefs.shouldFarmVillagers() && !villagers.isEmpty()) {
                    if (!village.isEmpty()) {
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
                            plugin.getTardisHelper().setReputation(npc, uuid, e.getReputation());
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                npc.setCustomName(name);
                            }
                            npc.setRemoveWhenFarAway(false);
                        });
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.VILLAGER_SPAWN_EGG, villagers.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_VILLAGE");
                    }
                }
                if (farmPrefs.shouldFarmPolarBears() && !polarbears.isEmpty()) {
                    if (!igloo.isEmpty()) {
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
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG, polarbears.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_IGLOO");
                    }
                }
                if (farmPrefs.shouldFarmSniffers() && !sniffers.isEmpty()) {
                    if (!pen.isEmpty()) {
                        // get location of sniffer pen room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(pen);
                        Location p_room = TARDISStaticLocationGetters.getSpawnLocationFromDB(pen);
                        while (!world.getChunkAt(p_room).isLoaded()) {
                            world.getChunkAt(p_room).load();
                        }
                        sniffers.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Entity sniff = world.spawnEntity(p_room, EntityType.SNIFFER);
                            Sniffer let = (Sniffer) sniff;
                            let.setAge(e.getAge());
                            if (e.isBaby()) {
                                let.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                let.setCustomName(name);
                            }
                            let.setRemoveWhenFarAway(false);
                        });
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.SNIFFER_EGG, sniffers.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_PEN");
                    }
                }
                if (farmPrefs.shouldFarmStriders() && !striders.isEmpty()) {
                    if (!lava.isEmpty()) {
                        // get location of strider lava room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(lava);
                        Location l_room = TARDISStaticLocationGetters.getSpawnLocationFromDB(lava);
                        while (!world.getChunkAt(l_room).isLoaded()) {
                            world.getChunkAt(l_room).load();
                        }
                        striders.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Entity stri = world.spawnEntity(l_room, EntityType.STRIDER);
                            Strider der = (Strider) stri;
                            der.setAge(e.getAge());
                            if (e.isBaby()) {
                                der.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                der.setCustomName(name);
                            }
                            der.setRemoveWhenFarAway(false);
                        });
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.STRIDER_SPAWN_EGG, striders.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_LAVA");
                    }
                }
                if (farmPrefs.shouldFarmCamels() && !camels.isEmpty()) {
                    if (!iistubil.isEmpty()) {
                        // get location of camel iistubil room
                        World world = TARDISStaticLocationGetters.getWorldFromSplitString(iistubil);
                        Location i_room = TARDISStaticLocationGetters.getSpawnLocationFromDB(iistubil);
                        while (!world.getChunkAt(i_room).isLoaded()) {
                            world.getChunkAt(i_room).load();
                        }
                        camels.forEach((e) -> {
                            plugin.setTardisSpawn(true);
                            Entity camel = world.spawnEntity(i_room, EntityType.CAMEL);
                            Camel hump = (Camel) camel;
                            hump.setAge(e.getAge());
                            if (e.isBaby()) {
                                hump.setBaby();
                            }
                            String name = e.getName();
                            if (name != null && !name.isEmpty()) {
                                hump.setCustomName(name);
                            }
                            hump.setDomestication(e.getDomesticity());
                            hump.getInventory().setContents(e.getHorseinventory());
                            hump.setRemoveWhenFarAway(false);
                        });
                    } else if (spawnEggs) {
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.CAMEL_SPAWN_EGG, camels.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_IISTUBIL");
                    }
                }
                if (farmPrefs.shouldFarmParrots() && !parrots.isEmpty()) {
                    if (!birdcage.isEmpty()) {
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
                    } else if (spawnEggs) {
                        // give spawn eggs
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.PARROT_SPAWN_EGG, parrots.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_BIRDCAGE");
                    }
                }
                if (farmPrefs.shouldFarmFrogs() && !frogs.isEmpty()) {
                    if (!mangrove.isEmpty()) {
                        // get location of mangrove room
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
                    } else if (spawnEggs) {
                        // give spawn eggs
                        Inventory inv = p.getInventory();
                        ItemStack is = new ItemStack(Material.FROG_SPAWN_EGG, frogs.size());
                        inv.addItem(is);
                        p.updateInventory();
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "FARM_MANGROVE");
                    }
                }
            }
        }
        egg.remove();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getFarming().remove(uuid), 20L);
        return new TARDISPetsAndFollowers(pets, followers);
    }

    public TARDISPetsAndFollowers exitPets(Player player) {
        List<TARDISPet> pets = new ArrayList<>();
        List<Follower> followers = new ArrayList<>();
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
                            Wolf wolf = (Wolf) entity;
                            pet.setAge(wolf.getAge());
                            pet.setSitting(wolf.isSitting());
                            pet.setColour(wolf.getCollarColor());
                            pet.setWolfType(wolf.getVariant());
                            health = Math.min(wolf.getHealth(), 8D);
                            pet.setHealth(health);
                            pet.setBaby(!wolf.isAdult());
                        }
                        case CAT -> {
                            Cat cat = (Cat) entity;
                            pet.setAge(cat.getAge());
                            pet.setSitting(cat.isSitting());
                            pet.setCatType(cat.getCatType());
                            pet.setColour(cat.getCollarColor());
                            health = Math.min(cat.getHealth(), 8D);
                            pet.setHealth(health);
                            pet.setBaby(!cat.isAdult());
                        }
                        case PARROT -> {
                            Parrot parrot = (Parrot) entity;
                            pet.setAge(parrot.getAge());
                            pet.setSitting(parrot.isSitting());
                            pet.setVariant(parrot.getVariant());
                            health = Math.min(parrot.getHealth(), 8D);
                            pet.setHealth(health);
                            pet.setBaby(!parrot.isAdult());
                            pet.setOnLeftShoulder(player.getShoulderEntityLeft() != null);
                            pet.setOnRightShoulder(player.getShoulderEntityRight() != null);
                        }
                        default -> {
                        }
                    }
                    pets.add(pet);
                    entity.remove();
                }
            } else if (entity.getType().equals(EntityType.HUSK) && plugin.getConfig().getBoolean("modules.weeping_angels")) {
                FollowerChecker fc = new FollowerChecker();
                fc.checkEntity(entity, player.getUniqueId());
                if (fc.isValid()) {
                    followers.add(fc.getFollower());
                    entity.remove();
                }
            }
        }
        return new TARDISPetsAndFollowers(pets, followers);
    }
}
