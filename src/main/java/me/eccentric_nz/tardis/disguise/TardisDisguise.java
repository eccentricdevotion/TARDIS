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
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
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

    public static Entity createMobDisguise(TardisDisguise disguise, World world) {
        String string = switch (disguise.getEntityType()) {
            case ZOMBIE_HORSE, SKELETON_HORSE, ELDER_GUARDIAN, WITHER_SKELETON, TRADER_LLAMA -> switchAndCapitalise(disguise.getEntityType().toString());
            case WANDERING_TRADER -> "VillagerTrader";
            case HUSK -> "ZombieHusk";
            case STRAY -> "SkeletonStray";
            case PUFFERFISH -> "PufferFish";
            case ILLUSIONER -> "IllagerIllusioner";
            case GIANT -> "GiantZombie";
            case DONKEY, MULE -> "Horse" + capitalise(disguise.getEntityType().toString());
            default -> capitalise(disguise.getEntityType().toString());
        };
        try {
            Class<? extends Entity> entityClass = (Class<? extends Entity>) Class.forName("net.minecraft.server.v1_16_R3.Entity" + string);
            Constructor<? extends Entity> constructor = entityClass.getConstructor(EntityTypes.class, net.minecraft.server.v1_16_R3.World.class);
            EntityTypes<? extends Entity> type = IRegistry.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(disguise.getEntityType().getKey()));
            net.minecraft.server.v1_16_R3.World world1 = ((CraftWorld) world).getHandle();
            Entity entity = constructor.newInstance(type, world1);
            if (disguise.getOptions() != null) {
                for (Object object : disguise.getOptions()) {
                    if (object instanceof DyeColor) {
                        // colour a sheep / wolf collar
                        switch (disguise.getEntityType()) {
                            case SHEEP:
                                EntitySheep sheep = (EntitySheep) entity;
                                sheep.setColor(EnumColor.valueOf(object.toString()));
                                break;
                            case WOLF:
                                EntityWolf wolf = (EntityWolf) entity;
                                wolf.setTamed(true);
                                wolf.setCollarColor(EnumColor.valueOf(object.toString()));
                                break;
                            default:
                                break;
                        }
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
                    if (object instanceof me.eccentric_nz.tardis.disguise.Fox && disguise.getEntityType().equals(EntityType.FOX)) {
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
                            case FOX:
                            case WOLF:
                            case CAT:
                                EntityTameableAnimal tameable = (EntityTameableAnimal) entity;
                                tameable.setTamed((Boolean) object);
                                break;
                            case DONKEY:
                            case MULE:
                                EntityHorseChestedAbstract chesty = (EntityHorseChestedAbstract) entity;
                                chesty.setCarryingChest((Boolean) object);
                                break;
                            case SHEEP:
                                if ((Boolean) object) {
                                    entity.setCustomName(new ChatMessage("jeb_"));
                                    entity.setCustomNameVisible(true);
                                }
                                break;
                            case PIG:
                                EntityPig pig = (EntityPig) entity;
                                pig.saddle(null);
                                break;
                            case ENDERMAN:
                                if ((Boolean) object) {
                                    EntityEnderman enderman = (EntityEnderman) entity;
                                    IBlockData block = Blocks.PURPUR_BLOCK.getBlockData();
                                    enderman.setCarried(block);
                                }
                                break;
                            case CREEPER:
                                EntityCreeper creeper = (EntityCreeper) entity;
                                creeper.setPowered((Boolean) object);
                                break;
                            case BAT:
                                EntityBat bat = (EntityBat) entity;
                                bat.setAsleep((Boolean) object);
                                break;
                            case SNOWMAN:
                                EntitySnowman snowman = (EntitySnowman) entity;
                                snowman.setHasPumpkin(!(Boolean) object);
                                break;
                            case PILLAGER:
                                if ((Boolean) object) {
                                    EntityPillager pillager = (EntityPillager) entity;
                                    ItemStack crossbow = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.CROSSBOW));
                                    pillager.setSlot(EnumItemSlot.MAINHAND, crossbow);
                                    pillager.a(pillager, 1.0f);
                                }
                                break;
                            case LLAMA:
                                EntityLlama llama = (EntityLlama) entity;
                                org.bukkit.inventory.ItemStack bukkitItemStack = new org.bukkit.inventory.ItemStack(Carpet.values()[ThreadLocalRandom.current().nextInt(16)].getCarpet());
                                ItemStack nmsItemStack = CraftItemStack.asNMSCopy(bukkitItemStack);
                                llama.inventoryChest.setItem(1, nmsItemStack);
                            default:
                                break;
                        }
                    }
                    if (object instanceof Integer) {
                        // magma cube and slime size / pufferfish state
                        switch (disguise.getEntityType()) {
                            case MAGMA_CUBE:
                                EntityMagmaCube magma = (EntityMagmaCube) entity;
                                magma.setSize((Integer) object, false);
                                break;
                            case SLIME:
                                EntitySlime slime = (EntitySlime) entity;
                                slime.setSize((Integer) object, false);
                                break;
                            case PUFFERFISH:
                                EntityPufferFish puffer = (EntityPufferFish) entity;
                                puffer.setPuffState((Integer) object);
                                break;
                            default:
                                break;
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

    private static String switchAndCapitalise(String string) {
        String[] split = string.split("_");
        return uppercaseFirst(split[1]) + uppercaseFirst(split[0]);
    }

    private static String capitalise(String string) {
        String[] split = string.split("_");
        return (split.length > 1) ? uppercaseFirst(split[0]) + uppercaseFirst(split[1]) : uppercaseFirst(split[0]);
    }

    private static String uppercaseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Object[] getOptions() {
        return options;
    }
}
