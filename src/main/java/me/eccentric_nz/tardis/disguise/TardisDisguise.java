/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.disguise;

import me.eccentric_nz.tardischunkgenerator.TardisHelperPlugin;
import net.minecraft.core.IRegistry;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityTameableAnimal;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;
import org.bukkit.entity.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class TardisDisguise {

    private final EntityType entityType;
    private final Object[] options;

    public TardisDisguise(EntityType entityType, Object[] options) {
        this.entityType = entityType;
        this.options = options;
    }

    public static net.minecraft.world.entity.Entity createMobDisguise(TardisDisguise disguise, World w) {
        String string;
        String packagePath = "net.minecraft.world.entity.";
        boolean hasEntityString = true;
        switch (disguise.getEntityType()) {
            case AXOLOTL -> {
                string = "Axolotl";
                packagePath += "animal.axolotl.";
                hasEntityString = false;
            }
            case BAT -> {
                string = "Bat";
                packagePath += "ambient.";
            }
            case GOAT -> {
                string = "Goat";
                packagePath += "animal.goat.";
                hasEntityString = false;
            }
            case ZOMBIE_HORSE, SKELETON_HORSE, TRADER_LLAMA -> {
                string = switchAndCapitalise(disguise.getEntityType().toString());
                packagePath += "animal.horse.";
            }
            case ELDER_GUARDIAN, WITHER_SKELETON -> {
                string = switchAndCapitalise(disguise.getEntityType().toString());
                packagePath += "monster.";
            }
            case WANDERING_TRADER -> {
                string = "VillagerTrader";
                packagePath += "npc.";
            }
            case HUSK -> {
                string = "ZombieHusk";
                packagePath += "monster.";
            }
            case STRAY -> {
                string = "SkeletonStray";
                packagePath += "monster.";
            }
            case PUFFERFISH -> {
                string = "PufferFish";
                packagePath += "animal.";
            }
            case ILLUSIONER -> {
                string = "IllagerIllusioner";
                packagePath += "monster.";
            }
            case GIANT -> {
                string = "GiantZombie";
                packagePath += "monster.";
            }
            case HORSE, LLAMA -> {
                string = capitalise(disguise.getEntityType().toString());
                packagePath += "animal.horse.";
            }
            case DONKEY, MULE -> {
                string = "Horse" + capitalise(disguise.getEntityType().toString());
                packagePath += "animal.horse.";
            }
            case VILLAGER -> {
                string = "Villager";
                packagePath += "npc.";
            }
            case ZOMBIFIED_PIGLIN -> {
                string = "PigZombie";
                packagePath += "monster.";
            }
            case BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ENDERMAN, ENDERMITE, EVOKER, GHAST, GUARDIAN, MAGMA_CUBE, PHANTOM, PILLAGER, RAVAGER, SHULKER, SILVERFISH, SKELETON, SLIME, SPIDER, STRIDER, VEX, VINDICATOR, WITCH, ZOGLIN, ZOMBIE, ZOMBIE_VILLAGER -> {
                string = capitalise(disguise.getEntityType().toString());
                packagePath += "monster.";
            }
            case HOGLIN -> {
                string = "Hoglin";
                packagePath += "monster.hoglin.";
            }
            case WITHER -> {
                string = "Wither";
                packagePath += "boss.wither.";
            }
            case PIGLIN, PIGLIN_BRUTE -> {
                string = capitalise(disguise.getEntityType().toString());
                packagePath += "monster.piglin.";
            }
            case GLOW_SQUID -> {
                string = "GlowSquid";
                //                packagePath = "";
                hasEntityString = false;
            }
            default -> {
                string = capitalise(disguise.getEntityType().toString());
                packagePath += "animal.";
            }
        }
        try {
            String entityPackage = packagePath + ((hasEntityString) ? "Entity" : "") + string;
            Class<? extends net.minecraft.world.entity.Entity> entityClass = (Class<? extends net.minecraft.world.entity.Entity>) Class.forName(entityPackage);
            Constructor<? extends net.minecraft.world.entity.Entity> constructor = entityClass.getConstructor(EntityTypes.class, net.minecraft.world.level.World.class);
            EntityTypes<? extends net.minecraft.world.entity.Entity> type = IRegistry.Y.get(CraftNamespacedKey.toMinecraft(disguise.getEntityType().getKey()));
            net.minecraft.world.level.World world = ((CraftWorld) w).getHandle();
            net.minecraft.world.entity.Entity entity = constructor.newInstance(type, world);
            if (disguise.getOptions() != null) {
                for (Object object : disguise.getOptions()) {
                    if (object instanceof DyeColor) {
                        // colour a sheep / wolf collar
                        switch (disguise.getEntityType()) {
                            case SHEEP -> {
                                EntitySheep sheep = (EntitySheep) entity;
                                sheep.setColor(EnumColor.valueOf(object.toString()));
                            }
                            case WOLF -> {
                                EntityWolf wolf = (EntityWolf) entity;
                                wolf.setTamed(true);
                                wolf.setCollarColor(EnumColor.valueOf(object.toString()));
                            }
                            default -> {
                            }
                        }
                    }
                    if (object instanceof Axolotl.Variant && disguise.getEntityType().equals(EntityType.AXOLOTL)) {
                        net.minecraft.world.entity.animal.axolotl.Axolotl axolotl = (net.minecraft.world.entity.animal.axolotl.Axolotl) entity;
                        net.minecraft.world.entity.animal.axolotl.Axolotl.Variant variant = net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.values()[((Axolotl.Variant) object).ordinal()];
                        axolotl.setVariant(variant);
                    }
                    if (object instanceof Rabbit.Type && disguise.getEntityType().equals(EntityType.RABBIT)) {
                        EntityRabbit rabbit = (EntityRabbit) entity;
                        rabbit.setRabbitType(((Rabbit.Type) object).ordinal());
                    }
                    if (object instanceof Gene && disguise.getEntityType().equals(EntityType.PANDA)) {
                        EntityPanda panda = (EntityPanda) entity;
                        EntityPanda.Gene gene = ((Gene) object).getNmsGene();
                        panda.setMainGene(gene);
                        panda.setHiddenGene(gene);
                    }
                    if (object instanceof Profession) {
                        if (disguise.getEntityType().equals(EntityType.VILLAGER)) {
                            EntityVillager villager = (EntityVillager) entity;
                            villager.setVillagerData(villager.getVillagerData().withProfession(((Profession) object).getNmsProfession()));
                        } else if (disguise.getEntityType().equals(EntityType.ZOMBIE_VILLAGER)) {
                            EntityZombieVillager zombie = (EntityZombieVillager) entity;
                            zombie.setVillagerData(zombie.getVillagerData().withProfession(((Profession) object).getNmsProfession()));
                        }
                    }
                    if (object instanceof Parrot.Variant && disguise.getEntityType().equals(EntityType.PARROT)) {
                        EntityParrot parrot = (EntityParrot) entity;
                        parrot.setVariant(((Parrot.Variant) object).ordinal());
                    }
                    if (object instanceof Mooshroom && disguise.getEntityType().equals(EntityType.MUSHROOM_COW)) {
                        EntityMushroomCow cow = (EntityMushroomCow) entity;
                        cow.setVariant(((Mooshroom) object).getNmsType());
                    }
                    if (object instanceof Cat.Type && disguise.getEntityType().equals(EntityType.CAT)) {
                        EntityCat cat = (EntityCat) entity;
                        cat.setCatType(((Cat.Type) object).ordinal());
                    }
                    if (object instanceof Fox && disguise.getEntityType().equals(EntityType.FOX)) {
                        EntityFox fox = (EntityFox) entity;
                        fox.setFoxType(((Fox) object).getNmsType());
                    }
                    if (object instanceof Horse.Color && disguise.getEntityType().equals(EntityType.HORSE)) {
                        EntityHorse horse = (EntityHorse) entity;
                        horse.setVariant(((HorseColor) object), HorseStyle.values()[new Random().nextInt(HorseStyle.values().length)]);
                    }
                    if (object instanceof Llama.Color && disguise.getEntityType().equals(EntityType.LLAMA)) {
                        EntityLlama llama = (EntityLlama) entity;
                        llama.setVariant(((Llama.Color) object).ordinal());
                    }
                    if (object instanceof Boolean) {
                        // tamed fox, wolf, cat / decorated llama, chest carrying mule or donkey / trusting ocelot
                        // rainbow sheep / saddled pig / block carrying enderman / powered creeper / hanging bat / blazing blaze
                        switch (disguise.getEntityType()) {
                            case FOX, WOLF, CAT -> {
                                EntityTameableAnimal tameable = (EntityTameableAnimal) entity;
                                tameable.setTamed((Boolean) object);
                            }
                            case DONKEY, MULE -> {
                                EntityHorseChestedAbstract chesty = (EntityHorseChestedAbstract) entity;
                                chesty.setCarryingChest((Boolean) object);
                            }
                            case SHEEP -> {
                                if ((Boolean) object) {
                                    entity.setCustomName(new ChatMessage("jeb_"));
                                    entity.setCustomNameVisible(true);
                                }
                            }
                            case PIG -> {
                                EntityPig pig = (EntityPig) entity;
                                pig.saddle(null);
                            }
                            case ENDERMAN -> {
                                if ((Boolean) object) {
                                    EntityEnderman enderman = (EntityEnderman) entity;
                                    IBlockData block = Blocks.iN.getBlockData(); // iN = PURPUR_BLOCK
                                    enderman.setCarried(block);
                                }
                            }
                            case CREEPER -> {
                                EntityCreeper creeper = (EntityCreeper) entity;
                                creeper.setPowered((Boolean) object);
                            }
                            case BAT -> {
                                EntityBat bat = (EntityBat) entity;
                                bat.setAsleep((Boolean) object);
                            }
                            case SNOWMAN -> {
                                EntitySnowman snowman = (EntitySnowman) entity;
                                snowman.setHasPumpkin(!(Boolean) object);
                            }
                            case PILLAGER -> {
                                if ((Boolean) object) {
                                    EntityPillager pillager = (EntityPillager) entity;
                                    ItemStack crossbow = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.CROSSBOW));
                                    pillager.setSlot(EnumItemSlot.a, crossbow); // a = MAINHAND
                                    pillager.a(pillager, 1.0f);
                                }
                            }
                            case LLAMA -> {
                                EntityLlama llama = (EntityLlama) entity;
                                org.bukkit.inventory.ItemStack bukkitItemStack = new org.bukkit.inventory.ItemStack(Carpet.values()[ThreadLocalRandom.current().nextInt(16)].getCarpet());
                                ItemStack nmsItemStack = CraftItemStack.asNMSCopy(bukkitItemStack);
                                llama.cd.setItem(1, nmsItemStack); // cd = inventoryChest
                            }
                            default -> {
                            }
                        }
                    }
                    if (object instanceof Integer) {
                        // magma cube and slime size / pufferfish state
                        switch (disguise.getEntityType()) {
                            case MAGMA_CUBE -> {
                                EntityMagmaCube magma = (EntityMagmaCube) entity;
                                magma.setSize((Integer) object, false);
                            }
                            case SLIME -> {
                                EntitySlime slime = (EntitySlime) entity;
                                slime.setSize((Integer) object, false);
                            }
                            case PUFFERFISH -> {
                                EntityPufferFish puffer = (EntityPufferFish) entity;
                                puffer.setPuffState((Integer) object);
                            }
                            default -> {
                            }
                        }
                    }
                    if (object instanceof TropicalFish.Pattern && disguise.getEntityType().equals(EntityType.TROPICAL_FISH)) {
                        EntityTropicalFish fish = (EntityTropicalFish) entity;
                        int var5 = ThreadLocalRandom.current().nextInt(2); // shape
                        int var6 = ((TropicalFish.Pattern) object).ordinal(); // pattern
                        int var7 = ThreadLocalRandom.current().nextInt(15); // base colour
                        int var8 = ThreadLocalRandom.current().nextInt(15); // pattern colour
                        fish.setVariant(var5 | var6 << 8 | var7 << 16 | var8 << 24);
                    }
                    if (object instanceof Age && EntityAgeable.class.isAssignableFrom(entityClass)) {
                        // adult or baby
                        EntityAgeable ageable = (EntityAgeable) entity;
                        ageable.setAgeRaw(((Age) object).getAge());
                    }
                }
            }
            return entity;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            Bukkit.getLogger().log(Level.SEVERE, TardisHelperPlugin.MESSAGE_PREFIX + "~TARDISDisguise~ " + e.getMessage());
            e.printStackTrace();
        }
        return null;
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
