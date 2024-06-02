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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.RabbitType;
import me.libraryaddict.disguise.disguisetypes.watchers.*;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;

public class TARDISLazarusLibs {

    private final Player player;
    private final String disguise;
    private final Object choice;
    private final boolean hasOption;
    private final boolean isBaby;

    public TARDISLazarusLibs(Player player, String disguise, Object choice, boolean hasOption, boolean isBaby) {
        this.player = player;
        this.disguise = disguise;
        this.choice = choice;
        this.hasOption = hasOption;
        this.isBaby = isBaby;
    }

    public static void removeDisguise(Player player) {
        if (DisguiseAPI.isDisguised(player)) {
            DisguiseAPI.undisguiseToAll(player);
        }
    }

    public static void runImmortalityGate(Player player) {
        PlayerDisguise playerDisguise = new PlayerDisguise(player.getName());
        TARDIS.plugin.getServer().getOnlinePlayers().forEach((p) -> {
            if (!p.equals(player)) {
                DisguiseAPI.disguiseToAll(p, playerDisguise);
            }
        });
    }

    public void createDisguise() {
        DisguiseType dt = DisguiseType.valueOf(disguise);
        if (dt.equals(DisguiseType.PLAYER)) {
            PlayerDisguise playerDisguise = new PlayerDisguise("Herobrine");
            DisguiseAPI.disguiseToAll(player, playerDisguise);
        } else {
            MobDisguise mobDisguise = new MobDisguise(dt);
            LivingWatcher livingWatcher;
            try {
                livingWatcher = mobDisguise.getWatcher();
            } catch (NoSuchMethodError e) {
                TARDIS.plugin.getMessenger().message(player, "LIBS");
                return;
            }
            switch (dt) {
                case AXOLOTL -> {
                    AxolotlWatcher axolotlWatcher = (AxolotlWatcher) livingWatcher;
                    axolotlWatcher.setVariant((Axolotl.Variant) choice);
                    axolotlWatcher.setBaby(isBaby);
                }
                case CAT -> {
                    CatWatcher catWatcher = (CatWatcher) livingWatcher;
                    catWatcher.setType((Cat.Type) choice);
                    if (hasOption) {
                        catWatcher.setTamed(true);
                    }
                    catWatcher.setBaby(isBaby);
                }
                case FROG -> {
                    FrogWatcher frogWatcher = (FrogWatcher) livingWatcher;
                    frogWatcher.setVariant((Frog.Variant) choice);
                    frogWatcher.setBaby(isBaby);
                }
                case PANDA -> {
                    PandaWatcher pandaWatcher = (PandaWatcher) livingWatcher;
                    Panda.Gene gene = (Panda.Gene) choice;
                    pandaWatcher.setMainGene(gene);
                    pandaWatcher.setHiddenGene(gene);
                }
                case DONKEY, MULE -> {
                    ChestedHorseWatcher chestedHorseWatcher = (ChestedHorseWatcher) livingWatcher;
                    chestedHorseWatcher.setCarryingChest(hasOption);
                }
                case PILLAGER -> {
                    PillagerWatcher pillagerWatcher = (PillagerWatcher) livingWatcher;
                    pillagerWatcher.setAimingBow(hasOption);
                }
                case SHEEP -> {
                    SheepWatcher sheepWatcher = (SheepWatcher) livingWatcher;
                    sheepWatcher.setColor((DyeColor) choice);
                    sheepWatcher.setBaby(isBaby);
                    if (hasOption) {
                        sheepWatcher.setCustomName("jeb_");
                        sheepWatcher.setCustomNameVisible(true);
                    }
                }
                case HORSE -> {
                    HorseWatcher horseWatcher = (HorseWatcher) livingWatcher;
                    horseWatcher.setColor((Horse.Color) choice);
                    horseWatcher.setBaby(isBaby);
                }
                case LLAMA -> {
                    LlamaWatcher llamaWatcher = (LlamaWatcher) livingWatcher;
                    llamaWatcher.setColor((Llama.Color) choice);
                    if (hasOption) {
                        llamaWatcher.setCarpet(DyeColor.values()[TARDISConstants.RANDOM.nextInt(16)]);
                    }
                }
                case OCELOT -> {
                    OcelotWatcher ocelotWatcher = (OcelotWatcher) livingWatcher;
                    ocelotWatcher.setBaby(isBaby);
                    ocelotWatcher.setTrusting(hasOption);
                }
                case PARROT -> {
                    ParrotWatcher parrotWatcher = (ParrotWatcher) livingWatcher;
                    parrotWatcher.setVariant((Parrot.Variant) choice);
                    parrotWatcher.setBaby(isBaby);
                }
                case PIG -> {
                    PigWatcher pigWatcher = (PigWatcher) livingWatcher;
                    pigWatcher.setSaddled(hasOption);
                    pigWatcher.setBaby(isBaby);
                }
                case RABBIT -> {
                    RabbitWatcher rabbitWatcher = (RabbitWatcher) livingWatcher;
                    RabbitType rabbitType = RabbitType.valueOf(choice.toString());
                    rabbitWatcher.setType(rabbitType);
                    rabbitWatcher.setBaby(isBaby);
                }
                case VILLAGER -> {
                    VillagerWatcher villagerWatcher = (VillagerWatcher) livingWatcher;
                    villagerWatcher.setProfession((Villager.Profession) choice);
                    villagerWatcher.setBaby(isBaby);
                }
                case WOLF -> {
                    WolfWatcher wolfWatcher = (WolfWatcher) livingWatcher;
                    if (hasOption) {
                        wolfWatcher.setTamed(true);
//                        wolfWatcher.setCollarColor((DyeColor) choice);
                    }
//                    wolfWatcher.setVariant((Wolf.Variant) choice);
                    wolfWatcher.setBaby(isBaby);
                }
                case SLIME, MAGMA_CUBE -> {
                    SlimeWatcher slimeWatcher = (SlimeWatcher) livingWatcher;
                    slimeWatcher.setSize((Integer) choice);
                }
                case BAT -> {
                    BatWatcher batWatcher = (BatWatcher) livingWatcher;
                    batWatcher.setHanging(!hasOption);
                }
                case BLAZE -> {
                    BlazeWatcher blazeWatcher = (BlazeWatcher) livingWatcher;
                    blazeWatcher.setBlazing(hasOption);
                }
                case CREEPER -> {
                    CreeperWatcher cw = (CreeperWatcher) livingWatcher;
                    cw.setPowered(hasOption);
                }
                case ENDERMAN -> {
                    EndermanWatcher endermanWatcher = (EndermanWatcher) livingWatcher;
                    endermanWatcher.setAggressive(hasOption);
                }
                case COW -> {
                    AgeableWatcher ageableWatcher = (AgeableWatcher) livingWatcher;
                    ageableWatcher.setBaby(isBaby);
                }
                case ZOMBIE -> {
                    ZombieWatcher zombieWatcher = (ZombieWatcher) livingWatcher;
                    zombieWatcher.setBaby(isBaby);
                }
                case ZOMBIE_VILLAGER -> {
                    ZombieVillagerWatcher zombieVillagerWatcher = (ZombieVillagerWatcher) livingWatcher;
                    zombieVillagerWatcher.setBaby(isBaby);
                    zombieVillagerWatcher.setProfession((Villager.Profession) choice);
                }
                case SNOWMAN -> {
                    SnowmanWatcher snowmanWatcher = (SnowmanWatcher) livingWatcher;
                    snowmanWatcher.setDerp(!(Boolean) choice);
                }
                case TURTLE -> {
                    TurtleWatcher turtleWatcher = (TurtleWatcher) livingWatcher;
                    turtleWatcher.setBaby(isBaby);
                }
                case PUFFERFISH -> {
                    PufferFishWatcher pufferFishWatcher = (PufferFishWatcher) livingWatcher;
                    pufferFishWatcher.setPuffState((Integer) choice);
                }
                case TROPICAL_FISH -> {
                    TropicalFishWatcher tropicalFishWatcher = (TropicalFishWatcher) livingWatcher;
                    tropicalFishWatcher.setPattern((TropicalFish.Pattern) choice);
                    tropicalFishWatcher.setBodyColor(DyeColor.values()[TARDISConstants.RANDOM.nextInt(16)]);
                    tropicalFishWatcher.setPatternColor(DyeColor.values()[TARDISConstants.RANDOM.nextInt(16)]);
                }
                case MUSHROOM_COW -> {
                    MushroomCowWatcher mushroomCowWatcher = (MushroomCowWatcher) livingWatcher;
                    mushroomCowWatcher.setVariant((MushroomCow.Variant) choice);
                }
                case FOX -> {
                    FoxWatcher foxWatcher = (FoxWatcher) livingWatcher;
                    foxWatcher.setType((Fox.Type) choice);
                }
                default -> {
                }
            }
            DisguiseAPI.disguiseToAll(player, mobDisguise);
        }
    }
}
