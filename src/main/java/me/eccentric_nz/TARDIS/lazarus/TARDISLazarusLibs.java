/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
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
				TARDISMessage.message(player, "LIBS");
				return;
			}
			switch (dt) {
				case CAT:
					CatWatcher catWatcher = (CatWatcher) livingWatcher;
					catWatcher.setType((Cat.Type) choice);
					if (hasOption) {
						catWatcher.setTamed(true);
					}
					catWatcher.setBaby(isBaby);
					break;
				case PANDA:
					PandaWatcher pandaWatcher = (PandaWatcher) livingWatcher;
					Panda.Gene gene = (Panda.Gene) choice;
					pandaWatcher.setMainGene(gene);
					pandaWatcher.setHiddenGene(gene);
					break;
				case DONKEY:
				case MULE:
					ChestedHorseWatcher chestedHorseWatcher = (ChestedHorseWatcher) livingWatcher;
					chestedHorseWatcher.setCarryingChest(hasOption);
					break;
				case PILLAGER:
					PillagerWatcher pillagerWatcher = (PillagerWatcher) livingWatcher;
					pillagerWatcher.setAimingBow(hasOption);
					break;
				case SHEEP:
					SheepWatcher sheepWatcher = (SheepWatcher) livingWatcher;
					sheepWatcher.setColor((DyeColor) choice);
					sheepWatcher.setBaby(isBaby);
					if (hasOption) {
						sheepWatcher.setCustomName("jeb_");
						sheepWatcher.setCustomNameVisible(true);
					}
					break;
				case HORSE:
					HorseWatcher horseWatcher = (HorseWatcher) livingWatcher;
					horseWatcher.setColor((Horse.Color) choice);
					horseWatcher.setBaby(isBaby);
					break;
				case LLAMA:
					LlamaWatcher llamaWatcher = (LlamaWatcher) livingWatcher;
					llamaWatcher.setColor((Llama.Color) choice);
					if (hasOption) {
						llamaWatcher.setCarpet(DyeColor.values()[TARDISConstants.RANDOM.nextInt(16)]);
					}
					break;
				case OCELOT:
					OcelotWatcher ocelotWatcher = (OcelotWatcher) livingWatcher;
					ocelotWatcher.setBaby(isBaby);
					ocelotWatcher.setTrusting(hasOption);
					break;
				case PARROT:
					ParrotWatcher parrotWatcher = (ParrotWatcher) livingWatcher;
					parrotWatcher.setVariant((Parrot.Variant) choice);
					parrotWatcher.setBaby(isBaby);
					break;
				case PIG:
					PigWatcher pigWatcher = (PigWatcher) livingWatcher;
					pigWatcher.setSaddled(hasOption);
					pigWatcher.setBaby(isBaby);
					break;
				case RABBIT:
					RabbitWatcher rabbitWatcher = (RabbitWatcher) livingWatcher;
					RabbitType rabbitType = RabbitType.valueOf(choice.toString());
					rabbitWatcher.setType(rabbitType);
					rabbitWatcher.setBaby(isBaby);
					break;
				case VILLAGER:
					VillagerWatcher villagerWatcher = (VillagerWatcher) livingWatcher;
					villagerWatcher.setProfession((Villager.Profession) choice);
					villagerWatcher.setBaby(isBaby);
					break;
				case WOLF:
					WolfWatcher wolfWatcher = (WolfWatcher) livingWatcher;
					if (hasOption) {
						wolfWatcher.setTamed(true);
						wolfWatcher.setCollarColor((DyeColor) choice);
					}
					wolfWatcher.setBaby(isBaby);
					break;
				case SLIME:
				case MAGMA_CUBE:
					SlimeWatcher slimeWatcher = (SlimeWatcher) livingWatcher;
					slimeWatcher.setSize((Integer) choice);
					break;
				case BAT:
					BatWatcher batWatcher = (BatWatcher) livingWatcher;
					batWatcher.setHanging(!hasOption);
					break;
				case BLAZE:
					BlazeWatcher blazeWatcher = (BlazeWatcher) livingWatcher;
					blazeWatcher.setBlazing(hasOption);
					break;
				case CREEPER:
					CreeperWatcher cw = (CreeperWatcher) livingWatcher;
					cw.setPowered(hasOption);
					break;
				case ENDERMAN:
					EndermanWatcher endermanWatcher = (EndermanWatcher) livingWatcher;
					endermanWatcher.setAggressive(hasOption);
					break;
				case COW:
					AgeableWatcher ageableWatcher = (AgeableWatcher) livingWatcher;
					ageableWatcher.setBaby(isBaby);
					break;
				case ZOMBIE:
					ZombieWatcher zombieWatcher = (ZombieWatcher) livingWatcher;
					zombieWatcher.setBaby(isBaby);
					break;
				case ZOMBIE_VILLAGER:
					ZombieVillagerWatcher zombieVillagerWatcher = (ZombieVillagerWatcher) livingWatcher;
					zombieVillagerWatcher.setBaby(isBaby);
					zombieVillagerWatcher.setProfession((Villager.Profession) choice);
					break;
				case SNOWMAN:
					SnowmanWatcher snowmanWatcher = (SnowmanWatcher) livingWatcher;
					snowmanWatcher.setDerp(!(Boolean) choice);
					break;
				case TURTLE:
					TurtleWatcher turtleWatcher = (TurtleWatcher) livingWatcher;
					turtleWatcher.setBaby(isBaby);
					break;
				case PUFFERFISH:
					PufferFishWatcher pufferFishWatcher = (PufferFishWatcher) livingWatcher;
					pufferFishWatcher.setPuffState((Integer) choice);
					break;
				case TROPICAL_FISH:
					TropicalFishWatcher tropicalFishWatcher = (TropicalFishWatcher) livingWatcher;
					tropicalFishWatcher.setPattern((TropicalFish.Pattern) choice);
					tropicalFishWatcher.setBodyColor(DyeColor.values()[TARDISConstants.RANDOM.nextInt(16)]);
					tropicalFishWatcher.setPatternColor(DyeColor.values()[TARDISConstants.RANDOM.nextInt(16)]);
					break;
				case MUSHROOM_COW:
					MushroomCowWatcher mushroomCowWatcher = (MushroomCowWatcher) livingWatcher;
					mushroomCowWatcher.setVariant((MushroomCow.Variant) choice);
					break;
				case FOX:
					FoxWatcher foxWatcher = (FoxWatcher) livingWatcher;
					foxWatcher.setType((Fox.Type) choice);
					break;
				default:
					break;
			}
			DisguiseAPI.disguiseToAll(player, mobDisguise);
		}
	}
}
