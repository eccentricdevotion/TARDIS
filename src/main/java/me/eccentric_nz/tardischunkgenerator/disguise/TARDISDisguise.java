/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardischunkgenerator.disguise;

import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftNamespacedKey;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

;

public class TARDISDisguise {

    private final EntityType entityType;
    private final Object[] options;

    public TARDISDisguise(EntityType entityType, Object[] options) {
        this.entityType = entityType;
        this.options = options;
    }

    public static net.minecraft.world.entity.Entity createMobDisguise(TARDISDisguise disguise, World w) {
        String str;
        String packagePath = "net.minecraft.world.entity.";
        boolean hasEntityStr = true;
        switch (disguise.getEntityType()) {
            case AXOLOTL -> {
                str = "Axolotl";
                packagePath += "animal.axolotl.";
                hasEntityStr = false;
            }
            case ALLAY -> {
                str = "Allay";
                packagePath += "animal.allay.";
                hasEntityStr = false;
            }
            case FROG, TADPOLE -> {
                str = uppercaseFirst(disguise.getEntityType().toString());
                packagePath += "animal.frog.";
                hasEntityStr = false;
            }
            case WARDEN -> {
                str = "Warden";
                packagePath += "monster.warden.";
                hasEntityStr = false;
            }
            case BAT -> {
                str = "Bat";
                packagePath += "ambient.";
            }
            case CAMEL -> {
                str = "Camel";
                packagePath += "animal.camel.";
                hasEntityStr = false;
            }
            case GOAT -> {
                str = "Goat";
                packagePath += "animal.goat.";
                hasEntityStr = false;
            }
            case ZOMBIE_HORSE, SKELETON_HORSE, TRADER_LLAMA -> {
                str = switchAndCapitalise(disguise.getEntityType().toString());
                packagePath += "animal.horse.";
            }
            case ELDER_GUARDIAN, WITHER_SKELETON -> {
                str = switchAndCapitalise(disguise.getEntityType().toString());
                packagePath += "monster.";
            }
            case WANDERING_TRADER -> {
                str = "VillagerTrader";
                packagePath += "npc.";
            }
            case HUSK -> {
                str = "ZombieHusk";
                packagePath += "monster.";
            }
            case STRAY -> {
                str = "SkeletonStray";
                packagePath += "monster.";
            }
            case PUFFERFISH -> {
                str = "PufferFish";
                packagePath += "animal.";
            }
            case ILLUSIONER -> {
                str = "IllagerIllusioner";
                packagePath += "monster.";
            }
            case GIANT -> {
                str = "GiantZombie";
                packagePath += "monster.";
            }
            case HORSE, LLAMA -> {
                str = capitalise(disguise.getEntityType().toString());
                packagePath += "animal.horse.";
            }
            case DONKEY, MULE -> {
                str = "Horse" + capitalise(disguise.getEntityType().toString());
                packagePath += "animal.horse.";
            }
            case VILLAGER -> {
                str = "Villager";
                packagePath += "npc.";
            }
            case ZOMBIFIED_PIGLIN -> {
                str = "PigZombie";
                packagePath += "monster.";
            }
            case BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ENDERMAN, ENDERMITE, EVOKER, GHAST, GUARDIAN, MAGMA_CUBE, PHANTOM, PILLAGER, RAVAGER, SHULKER, SILVERFISH, SKELETON, SLIME, SPIDER, STRIDER, VEX, VINDICATOR, WITCH, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER -> {
                str = capitalise(disguise.getEntityType().toString());
                packagePath += "monster.";
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
                str = "Wither";
                packagePath += "boss.wither.";
            }
            case PIGLIN, PIGLIN_BRUTE -> {
                str = capitalise(disguise.getEntityType().toString());
                packagePath += "monster.piglin.";
            }
            case GLOW_SQUID -> {
                str = "GlowSquid";
                hasEntityStr = false;
            }
            default -> {
                str = capitalise(disguise.getEntityType().toString());
                packagePath += "animal.";
            }
        }
        try {
            String entityPackage = packagePath + ((hasEntityStr) ? "Entity" : "") + str;
            Class entityClass = Class.forName(entityPackage);
            Constructor constructor = entityClass.getConstructor(net.minecraft.world.entity.EntityType.class, net.minecraft.world.level.Level.class);
            net.minecraft.world.entity.EntityType type = BuiltInRegistries.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(disguise.getEntityType().getKey()));
            net.minecraft.world.level.Level world = ((CraftWorld) w).getHandle();
            net.minecraft.world.entity.Entity entity = (net.minecraft.world.entity.Entity) constructor.newInstance(type, world);
            if (disguise.getOptions() != null) {
                for (Object o : disguise.getOptions()) {
                    if (o instanceof org.bukkit.DyeColor) {
                        // colour a sheep / wolf collar
                        switch (disguise.getEntityType()) {
                            case SHEEP -> {
                                Sheep sheep = (Sheep) entity;
                                sheep.setColor(DyeColor.valueOf(o.toString()));
                            }
                            case WOLF -> {
                                Wolf wolf = (Wolf) entity;
                                wolf.setTame(true);
                                wolf.setCollarColor(DyeColor.valueOf(o.toString()));
                            }
                            default -> {
                            }
                        }
                    }
                    if (disguise.getEntityType().equals(EntityType.AXOLOTL) && o instanceof Axolotl.Variant av) {
                        net.minecraft.world.entity.animal.axolotl.Axolotl axolotl = (net.minecraft.world.entity.animal.axolotl.Axolotl) entity;
                        net.minecraft.world.entity.animal.axolotl.Axolotl.Variant variant = net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.values()[av.ordinal()];
                        axolotl.setVariant(variant);
                    }
                    if (disguise.getEntityType().equals(EntityType.FROG) && o instanceof Frog.Variant fv) {
                        net.minecraft.world.entity.animal.frog.Frog frog = (net.minecraft.world.entity.animal.frog.Frog) entity;
                        net.minecraft.world.entity.animal.FrogVariant variant = BuiltInRegistries.FROG_VARIANT.byId(fv.ordinal());
                        frog.setVariant(variant);
                    }
                    if (disguise.getEntityType().equals(EntityType.RABBIT) && o instanceof org.bukkit.entity.Rabbit.Type rt) {
                        Rabbit rabbit = (Rabbit) entity;
                        rabbit.setVariant(Rabbit.Variant.byId(rt.ordinal()));
                    }
                    if (disguise.getEntityType().equals(EntityType.PANDA) && o instanceof GENE g) {
                        Panda panda = (Panda) entity;
                        Panda.Gene gene = g.getNmsGene();
                        panda.setMainGene(gene);
                        panda.setHiddenGene(gene);
                    }
                    if (o instanceof PROFESSION profession) {
                        if (disguise.getEntityType().equals(EntityType.VILLAGER)) {
                            Villager villager = (Villager) entity;
                            villager.setVillagerData(villager.getVillagerData().setProfession(profession.getNmsProfession()));
                        } else if (disguise.getEntityType().equals(EntityType.ZOMBIE_VILLAGER)) {
                            ZombieVillager zombie = (ZombieVillager) entity;
                            zombie.setVillagerData(zombie.getVillagerData().setProfession(profession.getNmsProfession()));
                        }
                    }
                    if (disguise.getEntityType().equals(EntityType.PARROT) && o instanceof org.bukkit.entity.Parrot.Variant pv) {
                        Parrot parrot = (Parrot) entity;
                        parrot.setVariant(Parrot.Variant.byId(pv.ordinal()));
                    }
                    if (disguise.getEntityType().equals(EntityType.MUSHROOM_COW) && o instanceof MUSHROOM_COW mc) {
                        MushroomCow cow = (MushroomCow) entity;
                        cow.setVariant(mc.getNmsType());
                    }
                    if (disguise.getEntityType().equals(EntityType.CAT) && o instanceof org.bukkit.entity.Cat.Type c) {
                        Cat cat = (Cat) entity;
                        cat.setVariant(BuiltInRegistries.CAT_VARIANT.byId(c.ordinal()));
                    }
                    if (disguise.getEntityType().equals(EntityType.FOX) && o instanceof FOX f) {
                        Fox fox = (Fox) entity;
                        fox.setVariant(f.getNmsType());
                    }
                    if (disguise.getEntityType().equals(EntityType.HORSE) && o instanceof org.bukkit.entity.Horse.Color hc) {
                        Horse horse = (Horse) entity;
                        horse.setVariantAndMarkings(Variant.values()[hc.ordinal()], Markings.values()[new Random().nextInt(Markings.values().length)]);
                    }
                    if (disguise.getEntityType().equals(EntityType.LLAMA) && o instanceof org.bukkit.entity.Llama.Color lc) {
                        Llama llama = (Llama) entity;
                        llama.setVariant(Llama.Variant.byId(lc.ordinal()));
                    }
                    if (o instanceof Boolean bool) {
                        // tamed fox, wolf, cat / decorated llama, chest carrying mule or donkey / trusting ocelot
                        // rainbow sheep / saddled pig / block carrying enderman / powered creeper / hanging bat / blazing blaze
                        switch (disguise.getEntityType()) {
                            case FOX, WOLF, CAT -> {
                                TamableAnimal tameable = (TamableAnimal) entity;
                                tameable.setTame(bool);
                            }
                            case DONKEY, MULE -> {
                                AbstractChestedHorse chesty = (AbstractChestedHorse) entity;
                                chesty.setChest(bool);
                            }
                            case SHEEP -> {
                                if (bool) {
                                    entity.setCustomName(MutableComponent.create(new LiteralContents(("jeb_"))));
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
                            case SNOWMAN -> {
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
                        switch (disguise.getEntityType()) {
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
                    if (disguise.getEntityType().equals(EntityType.TROPICAL_FISH) && o instanceof org.bukkit.entity.TropicalFish.Pattern pattern) {
                        TropicalFish fish = (TropicalFish) entity;
//                        int var5 = ThreadLocalRandom.current().nextInt(2); // shape
                        int patternType = pattern.ordinal(); // pattern
                        int baseColour = ThreadLocalRandom.current().nextInt(15); // base colour
                        int patternColour = ThreadLocalRandom.current().nextInt(15); // pattern colour
                        TropicalFish.Pattern fishPattern = TropicalFish.Pattern.byId(patternType);
                        fish.setVariant(fishPattern);
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
            Bukkit.getLogger().log(Level.SEVERE, TARDISHelper.messagePrefix + "~TARDISDisguise~ " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    static int packVariant(TropicalFish.Pattern var0, DyeColor var1, DyeColor var2) {
        return var0.getPackedId() & '\uffff' | (var1.getId() & 255) << 16 | (var2.getId() & 255) << 24;
    }

    private static String switchAndCapitalise(String s) {
        String[] split = s.split("_");
        return uppercaseFirst(split[1]) + uppercaseFirst(split[0]);
    }

    private static String capitalise(String s) {
        String[] split = s.split("_");
        return (split.length > 1) ? uppercaseFirst(split[0]) + uppercaseFirst(split[1]) : uppercaseFirst(split[0]);
    }

    private static String uppercaseFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Object[] getOptions() {
        return options;
    }
}
