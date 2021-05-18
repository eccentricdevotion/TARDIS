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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.update.TARDISUpdatableCategory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;

public enum Updateable {

	ADVANCED(true, true, new RecipeChoice.MaterialChoice(Material.MUSHROOM_STEM, Material.JUKEBOX), TARDISUpdatableCategory.INTERFACES, "TARDIS Advanced Console"),
	ARS(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.INTERFACES, "Architectural Reconfiguration System"),
	ARTRON(true, true, TARDISUpdatableCategory.CONTROLS, "Artron Energy Capacitor button"),
	BACK(true, true, TARDISUpdatableCategory.CONTROLS, "Previous Location button"),
	BACKDOOR(false, false, new RecipeChoice.MaterialChoice(Material.IRON_DOOR), TARDISUpdatableCategory.OTHERS, "TARDIS back door"),
	BEACON(false, false, true, TARDISUpdatableCategory.LOCATIONS, "The block used to obstruct and turn off the TARDIS beacon"),
	BELL(true, true, TARDISUpdatableCategory.OTHERS, "TARDIS Cloister bell button"),
	BUTTON(true, true, TARDISUpdatableCategory.CONTROLS, "Random Location button"),
	CHAMELEON(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.INTERFACES, "Chameleon Circuit"),
	CONDENSER(true, true, new RecipeChoice.MaterialChoice(Material.CHEST), TARDISUpdatableCategory.OTHERS, "Artron Energy Condenser"),
	CONTROL(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.CONTROLS, "TARDIS Control Centre Menu"),
	CREEPER(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Artron Charged Creeper"),
	DIRECTION(false, true, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME), TARDISUpdatableCategory.INTERFACES, "Direction item frame"),
	DISPENSER(true, true, new RecipeChoice.MaterialChoice(Material.DISPENSER), TARDISUpdatableCategory.OTHERS, "Custard Cream dispenser"),
	DOOR(false, true, new RecipeChoice.MaterialChoice(Material.IRON_DOOR), TARDISUpdatableCategory.OTHERS, "TARDIS Interior Door"),
	EPS(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Emergency Programme One"),
	FARM(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Farm room"),
	FLIGHT(true, true, TARDISUpdatableCategory.CONTROLS, "Flight Mode button"),
	FORCEFIELD(true, true, TARDISUpdatableCategory.OTHERS, "TARDIS Force Field button"),
	FRAME(false, true, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME), TARDISUpdatableCategory.OTHERS, "Chameleon item frame"),
	FUEL(false, false, new RecipeChoice.MaterialChoice(Material.CHEST, Material.TRAPPED_CHEST), TARDISUpdatableCategory.LOCATIONS, "Smelter room fuel chest"),
	GENERATOR(false, true, new RecipeChoice.MaterialChoice(Material.FLOWER_POT), TARDISUpdatableCategory.INTERFACES, "Sonic Generator"),
	HANDBRAKE(true, true, new RecipeChoice.MaterialChoice(Material.LEVER), TARDISUpdatableCategory.CONTROLS, "Handbrake"),
	HINGE(false, false, TARDISUpdatableCategory.OTHERS, "Set the side a door hinge is on"),
	IGLOO(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Igloo room"),
	INFO(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.INTERFACES, "TARDIS Information System"),
	KEYBOARD(true, false, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.OTHERS, "Keyboard Input sign"),
	LIGHT(true, true, TARDISUpdatableCategory.OTHERS, "Console Light switch"),
	MAP(false, false, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME), TARDISUpdatableCategory.OTHERS, "TARDIS Scanner Map"),
	RAIL(false, false, new RecipeChoice.MaterialChoice(Tag.FENCES), TARDISUpdatableCategory.LOCATIONS, "Rail room entry point"),
	ROTOR(false, false, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME), TARDISUpdatableCategory.OTHERS, "Time Rotor item frame"),
	SAVE_SIGN(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.INTERFACES, "Saved locations and TARDIS areas"),
	SCANNER(true, true, TARDISUpdatableCategory.OTHERS, "Exterior Scanner button"),
	SIEGE(true, false, TARDISUpdatableCategory.INTERFACES, "Siege Mode button"),
	SMELT(false, false, new RecipeChoice.MaterialChoice(Material.CHEST, Material.TRAPPED_CHEST), TARDISUpdatableCategory.LOCATIONS, "Smelter room drop chest"),
	STABLE(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Horse Stable room"),
	STALL(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Llama Stall room"),
	STORAGE(true, false, new RecipeChoice.MaterialChoice(Material.MUSHROOM_STEM, Material.NOTE_BLOCK), TARDISUpdatableCategory.INTERFACES, "Disk Storage Container"),
	TELEPATHIC(true, true, new RecipeChoice.MaterialChoice(Material.DAYLIGHT_DETECTOR), TARDISUpdatableCategory.INTERFACES, "Telepathic Circuit"),
	TEMPORAL(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.INTERFACES, "Temporal Relocation"),
	TERMINAL(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS), TARDISUpdatableCategory.INTERFACES, "Destination Terminal"),
	THROTTLE(true, true, new RecipeChoice.MaterialChoice(Material.REPEATER), TARDISUpdatableCategory.CONTROLS, "Space Time Throttle"),
	TOGGLE_WOOL(true, true, TARDISUpdatableCategory.OTHERS, "Toggle Black Wool behind door"),
	VAULT(false, false, new RecipeChoice.MaterialChoice(Material.CHEST, Material.TRAPPED_CHEST), TARDISUpdatableCategory.LOCATIONS, "Vault room drop chest"),
	VILLAGE(false, false, true, TARDISUpdatableCategory.LOCATIONS, "Village room"),
	WEATHER(true, true, TARDISUpdatableCategory.INTERFACES, "TARDIS Weather Menu"),
	WORLD_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER), TARDISUpdatableCategory.CONTROLS, "World Type selector"),
	X_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER), TARDISUpdatableCategory.CONTROLS, "Random x coordinate setter"),
	Y_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER), TARDISUpdatableCategory.CONTROLS, "Distance multiplier"),
	Z_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER), TARDISUpdatableCategory.CONTROLS, "Random z coordinate setter"),
	ZERO(true, false, TARDISUpdatableCategory.OTHERS, "Zero room transmat button");

	private final boolean control;
	private final boolean secondary;
	private final boolean anyBlock;
	private final RecipeChoice.MaterialChoice materialChoice;
	private final TARDISUpdatableCategory category;
	private final String description;

	Updateable(boolean control, boolean secondary, TARDISUpdatableCategory category, String description) {
		this.control = control;
		this.secondary = secondary;
		anyBlock = false;
		materialChoice = new RecipeChoice.MaterialChoice(Material.ACACIA_BUTTON, Material.ACACIA_PRESSURE_PLATE, Material.ACACIA_WALL_SIGN, Material.BIRCH_BUTTON, Material.BIRCH_PRESSURE_PLATE, Material.BIRCH_WALL_SIGN, Material.COMPARATOR, Material.CRIMSON_BUTTON, Material.CRIMSON_PRESSURE_PLATE, Material.DARK_OAK_BUTTON, Material.DARK_OAK_PRESSURE_PLATE, Material.DARK_OAK_WALL_SIGN, Material.JUNGLE_BUTTON, Material.JUNGLE_PRESSURE_PLATE, Material.JUNGLE_WALL_SIGN, Material.LEVER, Material.OAK_BUTTON, Material.OAK_PRESSURE_PLATE, Material.OAK_WALL_SIGN, Material.POLISHED_BLACKSTONE_BUTTON, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.SPRUCE_BUTTON, Material.SPRUCE_PRESSURE_PLATE, Material.SPRUCE_WALL_SIGN, Material.STONE_BUTTON, Material.STONE_PRESSURE_PLATE, Material.WARPED_BUTTON, Material.WARPED_PRESSURE_PLATE);
		this.category = category;
		this.description = description;
	}

	Updateable(boolean control, boolean secondary, boolean anyBlock, TARDISUpdatableCategory category, String description) {
		this.control = control;
		this.secondary = secondary;
		this.anyBlock = anyBlock;
		materialChoice = new RecipeChoice.MaterialChoice(Material.SPAWNER);
		this.category = category;
		this.description = description;
	}

	Updateable(boolean control, boolean secondary, RecipeChoice.MaterialChoice materialChoice, TARDISUpdatableCategory category, String description) {
		this.control = control;
		this.secondary = secondary;
		anyBlock = false;
		this.materialChoice = materialChoice;
		this.category = category;
		this.description = description;
	}

	public String getName() {
		if (this == Updateable.TOGGLE_WOOL) {
			return "toggle_wool";
		} else {
			return TARDISStringUtils.toDashedLowercase(toString());
		}
	}

	public boolean isControl() {
		return control;
	}

	public boolean isSecondary() {
		return secondary;
	}

	public boolean isAnyBlock() {
		return anyBlock;
	}

	public RecipeChoice.MaterialChoice getMaterialChoice() {
		return materialChoice;
	}

	public TARDISUpdatableCategory getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}
}
