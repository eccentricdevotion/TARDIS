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

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;

public enum UPDATEABLE {

    ADVANCED(true, true, new RecipeChoice.MaterialChoice(Material.MUSHROOM_STEM, Material.JUKEBOX)),
    ARS(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    ARTRON(true, true),
    BACK(true, true),
    BACKDOOR(false, false, new RecipeChoice.MaterialChoice(Material.IRON_DOOR)),
    BEACON(false, false, new RecipeChoice.MaterialChoice(Material.BEACON)),
    BELL(true, true),
    BUTTON(true, true),
    CHAMELEON(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    CONDENSER(true, true, new RecipeChoice.MaterialChoice(Material.CHEST)),
    CONTROL(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    CREEPER(false, false, true),
    DIRECTION(false, true, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME)),
    DISPENSER(true, true, new RecipeChoice.MaterialChoice(Material.DISPENSER)),
    DOOR(false, true, new RecipeChoice.MaterialChoice(Material.IRON_DOOR)),
    EPS(false, false, true),
    FARM(false, false, true),
    FLIGHT(true, true),
    FORCEFIELD(true, true),
    FRAME(false, true, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME)),
    FUEL(false, false, new RecipeChoice.MaterialChoice(Material.CHEST, Material.TRAPPED_CHEST)),
    GENERATOR(false, true, new RecipeChoice.MaterialChoice(Material.FLOWER_POT)),
    HANDBRAKE(true, true, new RecipeChoice.MaterialChoice(Material.LEVER)),
    HINGE(false, false),
    INFO(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    KEYBOARD(true, false, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    LIGHT(true, true),
    MAP(false, false, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME)),
    RAIL(false, false, new RecipeChoice.MaterialChoice(Tag.FENCES)),
    ROTOR(false, false, new RecipeChoice.MaterialChoice(Material.ITEM_FRAME)),
    SAVE_SIGN(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    SCANNER(true, true),
    SIEGE(true, false),
    SMELT(false, false, new RecipeChoice.MaterialChoice(Material.CHEST, Material.TRAPPED_CHEST)),
    STABLE(false, false, true),
    STALL(false, false, true),
    STORAGE(true, false, new RecipeChoice.MaterialChoice(Material.MUSHROOM_STEM, Material.NOTE_BLOCK)),
    TELEPATHIC(true, true, new RecipeChoice.MaterialChoice(Material.DAYLIGHT_DETECTOR)),
    TEMPORAL(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    TERMINAL(true, true, new RecipeChoice.MaterialChoice(Tag.SIGNS)),
    TOGGLE_WOOL(true, true),
    VAULT(false, false, new RecipeChoice.MaterialChoice(Material.CHEST, Material.TRAPPED_CHEST)),
    VILLAGE(false, false, true),
    WEATHER(true, true),
    WORLD_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER)),
    X_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER)),
    Y_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER)),
    Z_REPEATER(false, true, new RecipeChoice.MaterialChoice(Material.REPEATER)),
    ZERO(true, false);

    private final boolean control;
    private final boolean secondary;
    private final boolean anyBlock;
    private final RecipeChoice.MaterialChoice materialChoice;

    UPDATEABLE(boolean control, boolean secondary) {
        this.control = control;
        this.secondary = secondary;
        anyBlock = false;
        materialChoice = new RecipeChoice.MaterialChoice(Material.COMPARATOR, Material.LEVER, Material.OAK_BUTTON, Material.DARK_OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON, Material.ACACIA_BUTTON, Material.JUNGLE_BUTTON, Material.STONE_BUTTON, Material.OAK_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE, Material.OAK_WALL_SIGN, Material.DARK_OAK_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.BIRCH_WALL_SIGN, Material.ACACIA_WALL_SIGN, Material.JUNGLE_WALL_SIGN);
    }

    UPDATEABLE(boolean control, boolean secondary, boolean anyBlock) {
        this.control = control;
        this.secondary = secondary;
        this.anyBlock = anyBlock;
        materialChoice = new RecipeChoice.MaterialChoice(Material.SPAWNER);
    }

    UPDATEABLE(boolean control, boolean secondary, RecipeChoice.MaterialChoice materialChoice) {
        this.control = control;
        this.secondary = secondary;
        anyBlock = false;
        this.materialChoice = materialChoice;
    }

    public String getName() {
        if (this == UPDATEABLE.TOGGLE_WOOL) {
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
}
