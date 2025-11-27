/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus.disguise;

import io.papermc.paper.world.WeatheringCopperState;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.CaseUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.equine.*;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public record TARDISDisguise(EntityType entityType, Object[] options) {

    public static Entity createMobDisguise(TARDISDisguise disguise, World w) {
        String str;
        String packagePath = "net.minecraft.world.entity.";
        switch (disguise.entityType()) {
            case BREEZE -> {
                str = "Breeze";
                packagePath += "monster.breeze.";
            }
            case COPPER_GOLEM, IRON_GOLEM, SNOW_GOLEM -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "animal.golem.";
            }
            case CREAKING -> {
                str = "Creaking";
                packagePath += "monster.creaking.";
            }
            case TADPOLE -> {
                str = TARDISStringUtils.uppercaseFirst(disguise.entityType().toString());
                packagePath += "animal.frog.";
            }
            case WARDEN -> {
                str = "Warden";
                packagePath += "monster.warden.";
            }
            case BAT -> {
                str = "Bat";
                packagePath += "ambient.";
            }
            case CAMEL_HUSK -> {
                str = "CamelHusk";
                packagePath += "animal.camel.";
            }
            case ZOMBIE_NAUTILUS -> {
                str = "ZombieNautilus";
                packagePath += "animal.nautilus.";
            }
            case DONKEY, HORSE, LLAMA, MULE, SKELETON_HORSE, TRADER_LLAMA, ZOMBIE_HORSE -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "animal.equine.";
            }
            case WANDERING_TRADER -> {
                str = "WanderingTrader";
                packagePath += "npc.wanderingtrader.";
            }
            case DROWNED, HUSK, ZOMBIE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "monster.zombie.";
            }
            case BOGGED, PARCHED, SKELETON, STRAY, WITHER_SKELETON -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "monster.skeleton.";
            }
            case OCELOT -> {
                str = "Ocelot";
                packagePath += "animal.feline.";
            }
            case MOOSHROOM -> {
                str = "MushroomCow";
                packagePath += "animal.cow.";
            }
            case COD, PUFFERFISH, SALMON, TROPICAL_FISH -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "animal.fish.";
            }
            case EVOKER, ILLUSIONER, PILLAGER, VINDICATOR -> {
                str = TARDISStringUtils.capitalise(disguise.entityType().toString());
                packagePath += "monster.illager.";
            }
            case VILLAGER -> {
                str = "Villager";
                packagePath += "npc.villager.";
            }
            case ENDERMAN -> { // special case
                str = "EnderMan"; // camel case but no underscore...
                packagePath += "monster.";
            }
            case BLAZE, CREEPER, ELDER_GUARDIAN, ENDERMITE, GHAST, GIANT, GUARDIAN, MAGMA_CUBE,
                 PHANTOM, RAVAGER, SHULKER, SILVERFISH, SLIME, STRIDER, VEX, WITCH, ZOGLIN -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "monster.";
            }
            case CAVE_SPIDER, SPIDER -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "monster.spider.";
            }
            case HOGLIN -> {
                str = "Hoglin";
                packagePath += "monster.hoglin.";
            }
            case ENDER_DRAGON -> {
                str = "EnderDragon";
                packagePath += "boss.enderdragon.";
            }
            case WITHER -> {
                str = "WitherBoss";
                packagePath += "boss.wither.";
            }
            case PIGLIN, PIGLIN_BRUTE -> {
                str = TARDISStringUtils.capitalise(disguise.entityType().toString());
                packagePath += "monster.piglin.";
            }
            case GLOW_SQUID -> {
                str = "GlowSquid";
                packagePath += "animal.squid.";
            }
            default -> {
                str = CaseUtils.toCamelCase(disguise.entityType().toString(), true, '_');
                packagePath += "animal." + disguise.entityType().toString().toLowerCase(Locale.ROOT).replace(" ","") + ".";
            }
        }
        try {
            String entityPackage = packagePath + str;
            Class<?> entityClass = Class.forName(entityPackage);
            Constructor<?> constructor = entityClass.getConstructor(net.minecraft.world.entity.EntityType.class, net.minecraft.world.level.Level.class);
            net.minecraft.world.entity.EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(disguise.entityType().getKey())).get().value();
            net.minecraft.world.level.Level world = ((CraftWorld) w).getHandle();
            Entity entity = (Entity) constructor.newInstance(type, world);
            if (disguise.options() != null) {
                for (Object o : disguise.options()) {
                    if (o instanceof org.bukkit.DyeColor) {
                        // colour a sheep / wolf collar
                        if (disguise.entityType() == EntityType.SHEEP) {
                            Sheep sheep = (Sheep) entity;
                            sheep.setColor(DyeColor.valueOf(o.toString()));
                        }
                    }
                    if (disguise.entityType().equals(EntityType.AXOLOTL) && o instanceof Axolotl.Variant av) {
                        net.minecraft.world.entity.animal.axolotl.Axolotl axolotl = (net.minecraft.world.entity.animal.axolotl.Axolotl) entity;
                        net.minecraft.world.entity.animal.axolotl.Axolotl.Variant variant = net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.values()[av.ordinal()];
                        axolotl.setVariant(variant);
                    }
                    if (disguise.entityType().equals(EntityType.FROG) && o instanceof Frog.Variant fv) {
                        net.minecraft.world.entity.animal.frog.Frog frog = (net.minecraft.world.entity.animal.frog.Frog) entity;
                        frog.setVariant(CraftFrog.CraftVariant.bukkitToMinecraftHolder(fv));
                    }
                    if (disguise.entityType().equals(EntityType.RABBIT) && o instanceof org.bukkit.entity.Rabbit.Type rt) {
                        Rabbit rabbit = (Rabbit) entity;
                        rabbit.setVariant(Rabbit.Variant.byId(rt.ordinal()));
                    }
                    if (disguise.entityType().equals(EntityType.COPPER_GOLEM) && o instanceof WeatheringCopperState wcs) {
                        CopperGolem golem = (CopperGolem) entity;
                        golem.setWeatherState(WeatheringCopper.WeatherState.valueOf(wcs.toString()));
                    }
                    if (disguise.entityType().equals(EntityType.PANDA) && o instanceof GENE g) {
                        Panda panda = (Panda) entity;
                        Panda.Gene gene = g.getNmsGene();
                        panda.setMainGene(gene);
                        panda.setHiddenGene(gene);
                    }
                    if (o instanceof org.bukkit.entity.Villager.Profession profession) {
                        if (disguise.entityType().equals(EntityType.VILLAGER)) {
                            Villager villager = (Villager) entity;
                            villager.setVillagerData(villager.getVillagerData().withProfession(CraftVillager.CraftProfession.bukkitToMinecraftHolder(profession)));
                        } else if (disguise.entityType().equals(EntityType.ZOMBIE_VILLAGER)) {
                            ZombieVillager zombie = (ZombieVillager) entity;
                            zombie.setVillagerData(zombie.getVillagerData().withProfession(CraftVillager.CraftProfession.bukkitToMinecraftHolder(profession)));
                        }
                    }
                    if (disguise.entityType().equals(EntityType.PARROT) && o instanceof org.bukkit.entity.Parrot.Variant pv) {
                        Parrot parrot = (Parrot) entity;
                        parrot.setVariant(Parrot.Variant.byId(pv.ordinal()));
                    }
                    if (disguise.entityType().equals(EntityType.MOOSHROOM) && o instanceof MUSHROOM_COW mc) {
                        MushroomCow cow = (MushroomCow) entity;
                        cow.setVariant(mc.getNmsType());
                    }
                    if (disguise.entityType().equals(EntityType.WOLF) && o instanceof org.bukkit.entity.Wolf.Variant wv) {
                        Wolf wolf = (Wolf) entity;
                        wolf.setVariant(CraftWolf.CraftVariant.bukkitToMinecraftHolder(wv));
                    }
                    if (disguise.entityType().equals(EntityType.CHICKEN) && o instanceof org.bukkit.entity.Chicken.Variant cv) {
                        Chicken chicken = (Chicken) entity;
                        chicken.setVariant(CraftChicken.CraftVariant.bukkitToMinecraftHolder(cv));
                    }
                    if (disguise.entityType().equals(EntityType.COW) && o instanceof org.bukkit.entity.Cow.Variant cv) {
                        Cow cow = (Cow) entity;
                        cow.setVariant(CraftCow.CraftVariant.bukkitToMinecraftHolder(cv));
                    }
                    if (disguise.entityType().equals(EntityType.PIG) && o instanceof org.bukkit.entity.Pig.Variant pv) {
                        Pig pig = (Pig) entity;
                        pig.setVariant(CraftPig.CraftVariant.bukkitToMinecraftHolder(pv));
                    }
                    if (disguise.entityType().equals(EntityType.CAT) && o instanceof org.bukkit.entity.Cat.Type c) {
                        Cat cat = (Cat) entity;
                        cat.setVariant(CraftCat.CraftType.bukkitToMinecraftHolder(c));
                    }
                    if (disguise.entityType().equals(EntityType.FOX) && o instanceof FOX f) {
                        Fox fox = (Fox) entity;
                        fox.setVariant(f.getNmsType());
                    }
                    if (disguise.entityType().equals(EntityType.HORSE) && o instanceof org.bukkit.entity.Horse.Color hc) {
                        Horse horse = (Horse) entity;
                        horse.setVariantAndMarkings(Variant.values()[hc.ordinal()], Markings.values()[TARDISConstants.RANDOM.nextInt(Markings.values().length)]);
                    }
                    if (disguise.entityType().equals(EntityType.LLAMA) && o instanceof org.bukkit.entity.Llama.Color lc) {
                        Llama llama = (Llama) entity;
                        llama.setVariant(Llama.Variant.byId(lc.ordinal()));
                    }
                    if (o instanceof Boolean bool) {
                        // tamed fox, wolf, cat / decorated llama, chest carrying mule or donkey / trusting ocelot
                        // rainbow sheep / saddled pig / block carrying enderman / powered creeper / hanging bat / blazing blaze
                        switch (disguise.entityType()) {
                            case FOX, WOLF, CAT -> {
                                TamableAnimal tameable = (TamableAnimal) entity;
                                tameable.setTame(bool, false);
                            }
                            case DONKEY, MULE -> {
                                AbstractChestedHorse chesty = (AbstractChestedHorse) entity;
                                chesty.setChest(bool);
                            }
                            case SHEEP -> {
                                if (bool) {
                                    entity.setCustomName(Component.literal(("jeb_")));
                                    entity.setCustomNameVisible(true);
                                }
                            }
                            case ENDERMAN -> {
                                if (bool) {
                                    EnderMan enderman = (EnderMan) entity;
                                    BlockState block = Blocks.PURPUR_BLOCK.defaultBlockState();
                                    enderman.setCarriedBlock(block);
                                }
                            }
                            case CREEPER -> {
                                Creeper creeper = (Creeper) entity;
                                creeper.setPowered(bool);
                            }
                            case BAT -> {
                                Bat bat = (Bat) entity;
                                bat.setResting(bool);
                            }
                            case SNOW_GOLEM -> {
                                SnowGolem snowman = (SnowGolem) entity;
                                snowman.setPumpkin(!bool);
                            }
                            case PILLAGER -> {
                                if (bool) {
                                    Pillager pillager = (Pillager) entity;
                                    ItemStack crossbow = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.CROSSBOW));
                                    pillager.setItemSlot(EquipmentSlot.MAINHAND, crossbow);
                                    pillager.performRangedAttack(pillager, 1.0f);
                                }
                            }
                            case LLAMA -> {
                                Llama llama = (Llama) entity;
                                org.bukkit.inventory.ItemStack bukkitItemStack = new org.bukkit.inventory.ItemStack(CARPET.values()[ThreadLocalRandom.current().nextInt(16)].getCarpet());
                                ItemStack nmsItemStack = CraftItemStack.asNMSCopy(bukkitItemStack);
                                llama.inventory.setItem(1, nmsItemStack);
                            }
                            default -> {
                            }
                        }
                    }
                    if (o instanceof Integer i) {
                        // magma cube and slime size / pufferfish state
                        switch (disguise.entityType()) {
                            case MAGMA_CUBE -> {
                                MagmaCube magma = (MagmaCube) entity;
                                magma.setSize(i, false);
                            }
                            case SLIME -> {
                                Slime slime = (Slime) entity;
                                slime.setSize(i, false);
                            }
                            case PUFFERFISH -> {
                                Pufferfish puffer = (Pufferfish) entity;
                                puffer.setPuffState(i);
                            }
                            default -> {
                            }
                        }
                    }
                    if (disguise.entityType().equals(EntityType.TROPICAL_FISH) && o instanceof org.bukkit.entity.TropicalFish.Pattern pattern) {
                        TropicalFish fish = (TropicalFish) entity;
                        int patternType = pattern.ordinal(); // pattern
                        int baseColour = ThreadLocalRandom.current().nextInt(15); // base colour
                        int patternColour = ThreadLocalRandom.current().nextInt(15); // pattern colour
                        TropicalFish.Pattern fishPattern = TropicalFish.Pattern.byId(patternType);
                        fish.setPackedVariant(fishPattern.getPackedId());
                        fish.setPackedVariant(packVariant(fishPattern, DyeColor.byId(baseColour), DyeColor.byId(patternColour)));
                    }
                    if (o instanceof AGE age && AgeableMob.class.isAssignableFrom(entityClass)) {
                        // adult or baby
                        AgeableMob ageable = (AgeableMob) entity;
                        ageable.setAge(age.getAge());
                    }
                }
            }
            return entity;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HELPER_SEVERE, "~TARDISDisguise~ " + e.getMessage());
//            e.printStackTrace();
        }
        return null;
    }

    static int packVariant(TropicalFish.Pattern var0, DyeColor var1, DyeColor var2) {
        return var0.getPackedId() & '\uffff' | (var1.getId() & 255) << 16 | (var2.getId() & 255) << 24;
    }
}
