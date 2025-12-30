/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.custommodels.keys.DirectionVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledButton;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.util.Vector;

public enum ConsoleInteraction {

    // section zero
    SONIC_DOCK("Sonic Screwdriver Dock", new Vector(0.5d, 0.75d, 1.625d), 0.5f, 0.65f, 0, 0.0f, Material.FLOWER_POT, SonicItem.SONIC_DOCK.getKey()),
    DIRECTION("Exterior Directional Control", new Vector(0.75d, 0.75d, 2.25d), 0.625f, 0.5f, 0, 0.0f, Material.RAIL, DirectionVariant.DIRECTION_NORTH.getKey()),

    // section one
    LIGHT_SWITCH("Interior Light Switch", new Vector(-1.45d, 0.75d, 0.9d), 0.25f, 0.33f, 0, 60.0f, Material.BAMBOO_BUTTON, ModelledButton.LIGHT_SWITCH_0.getKey()),
    INTERIOR_LIGHT_LEVEL_SWITCH("Interior Light Level", new Vector(-0.85d, 0.75d, 1.05d), 0.25f, 0.5f, 0, 60.0f, Material.LEVER, ModelledControl.MODELLED_LIGHT_0.getKey()),
    EXTERIOR_LAMP_LEVEL_SWITCH("Exterior Lamp Level", new Vector(-0.65d, 0.75d, 1.45d), 0.25f, 0.5f, 0, 60.0f, Material.LEVER, ModelledControl.MODELLED_LAMP_0.getKey()),
    // new Vector(0.125d, 0.75d, 1.8d)
    CONSOLE_LAMP("Console Lamp", new Vector(-0.55d, 0.75d, 1.8d), 0.15f, 0.5f, 0, 60.0f, Material.ARMADILLO_SCUTE, ModelledControl.CONSOLE_LAMP.getKey()),
    DOOR_TOGGLE("Toggle Wool Switch", new Vector(-0.775d, 0.75d, 2.0d), 0.25f, 0.33f, 0, 60.0f, Material.BAMBOO_BUTTON, ModelledButton.TOGGLE_WOOL_BUTTON_0.getKey()),

    // section two
    SCREEN_RIGHT("Coordinates Display", new Vector(-0.85d, 0.75d, 0.1d), 0.5f, 1.1f, 0, 120.0f, Material.MAP, ModelledControl.SCREEN.getKey()),
    SCREEN_LEFT("Information Display", new Vector(-0.6d, 0.75d, -0.4d), 0.5f, 1.1f, 0, 120.0f, Material.MAP, ModelledControl.SCREEN.getKey()),
    SCANNER("Exterior Environment Scanner", new Vector(-0.875d, 0.75d, -0.875d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, ModelledButton.SCANNER_BUTTON_0.getKey()),
    ARTRON("Artron Energy Button", new Vector(-1.15d, 0.75d, -0.4d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, ModelledButton.ARTRON_BUTTON_0.getKey()),
    REBUILD("Chameleon Circuit Re-initialiser", new Vector(-1.45d, 0.75d, 0.05d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, ModelledButton.REBUILD_BUTTON_0.getKey()),

    // section three
    HANDBRAKE("Time Rotor Handbrake", new Vector(0.95d, 0.75d, -1.2d), 0.5f, 0.4f, 1, 180.0f, Material.LEVER, ModelledControl.HANDBRAKE_1.getKey()),
    THROTTLE("Flight Speed", new Vector(-0.0d, 0.75d, -1.35d), 0.5f, 0.33f, 4, 180.0f, Material.REPEATER, ModelledControl.THROTTLE_NORMAL.getKey()),
    RELATIVITY_DIFFERENTIATOR("Flight Mode Selector", new Vector(0.45d, 0.75d, -0.75d), 0.5f, 0.65f, 1, 180.0f, Material.LEVER, ModelledControl.RELATIVITY_DIFFERENTIATOR_0A.getKey()),

    // section four
    RANDOMISER("Random Location Finder", new Vector(2.35d, 0.75d, 0.025d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, ModelledButton.RANDOM_BUTTON_0.getKey()),
    WAYPOINT_SELECTOR("Saves", new Vector(2.1d, 0.75d, -0.45d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, ModelledButton.SAVES_BUTTON_0.getKey()),
    FAST_RETURN("Back Button", new Vector(1.8d, 0.75d, -0.9d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, ModelledButton.BACK_BUTTON_0.getKey()),
    TELEPATHIC_CIRCUIT("Telepathic Circuit", new Vector(1.575d, 0.75d, -0.05d), 0.5f, 0.65f, 0, 240.0f, Material.DAYLIGHT_DETECTOR, ModelledControl.TELEPATHIC_CIRCUIT.getKey()),

    // section five

    WORLD("Environment Selector", new Vector(1.425d, 0.75d, 1.15d), 0.15f, 0.65f, 1, 300.0f, Material.BAMBOO_BUTTON, ModelledButton.WXYZ_0.getKey()),
    MULTIPLIER("Coordinate Increment Modifier", new Vector(1.55d, 0.75d, 0.925d), 0.15f, 0.65f, 1, 300.0f, Material.BAMBOO_BUTTON, ModelledButton.WXYZ_0.getKey()),
    X("X Distance", new Vector(1.625d, 0.75d, 1.275d), 0.15f, 0.4f, 1, 300.0f, Material.BAMBOO_BUTTON, ModelledButton.WXYZ_0.getKey()),
    Z("Z Distance", new Vector(1.75d, 0.75d, 1.05), 0.15f, 0.4f, 1, 300.0f, Material.BAMBOO_BUTTON, ModelledButton.WXYZ_0.getKey()),
    HELMIC_REGULATOR("Helmic Regulator", new Vector(2.25d, 0.75d, 1.075d), 0.5f, 0.6f, 0, 300.0f, Material.ARMADILLO_SCUTE, ModelledControl.HELMIC_REGULATOR_0.getKey()),

    // manual flight (includes helmic regulator above)
    ASTROSEXTANT_RECTIFIER("Astrosextant Rectifier", new Vector(0.55d, 0.75d, -1.35d), 0.15f, 0.35f, 0, 180.0f, Material.ARMADILLO_SCUTE, ModelledControl.ASTROSEXTANT_RECTIFIER.getKey()),
    GRAVITIC_ANOMALISER("Gravitic Anomaliser", new Vector(0.125d, 0.75d, 1.8d), 0.15f, 0.5f, 0, 0.0f, Material.ARMADILLO_SCUTE, ModelledControl.GRAVITIC_ANOMOLISER.getKey()),
    ABSOLUTE_TESSERACTULATOR("Absolute Tesseractulator", new Vector(-1.15d, 0.75d, 0.825d), 0.15f, 0.5f, 0, 60.0f, Material.ARMADILLO_SCUTE, ModelledControl.ABSOLUTE_TESSERACTULATOR.getKey());

    private final String alternateName;
    private final Vector relativePosition;
    private final float width;
    private final float height;
    private final int defaultState;
    private final float yaw;
    private final Material material;
    private final NamespacedKey customModel;

    ConsoleInteraction(String alternateName, Vector relativePosition, float width, float height, int defaultState, float yaw, Material material, NamespacedKey customModel) {
        this.alternateName = alternateName;
        this.relativePosition = relativePosition;
        this.width = width;
        this.height = height;
        this.defaultState = defaultState;
        this.yaw = yaw;
        this.material = material;
        this.customModel = customModel;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public Vector getRelativePosition() {
        return relativePosition;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getDefaultState() {
        return defaultState;
    }

    public float getYaw() {
        return yaw;
    }

    public Material getMaterial() {
        return material;
    }

    public NamespacedKey getCustomModel() {
        return customModel;
    }
}
