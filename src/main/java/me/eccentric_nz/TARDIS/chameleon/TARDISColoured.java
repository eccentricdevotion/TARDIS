/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;

import java.util.EnumMap;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
class TARDISColoured {

    private String[][] blueprintDataEast;
    private String[][] stainedDataEast;
    private String[][] glassDataEast;
    private String[][] blueprintDataNorth;
    private String[][] stainedDataNorth;
    private String[][] glassDataNorth;
    private String[][] blueprintDataWest;
    private String[][] stainedDataWest;
    private String[][] glassDataWest;
    private String[][] blueprintDataSouth;
    private String[][] stainedDataSouth;
    private String[][] glassDataSouth;

    private final EnumMap<COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> stained = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> glass = new EnumMap<>(COMPASS.class);

    public void makePresets() {
        blueprint.put(COMPASS.EAST, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(blueprintDataEast));
        stained.put(COMPASS.EAST, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(stainedDataEast));
        glass.put(COMPASS.EAST, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(glassDataEast));
        blueprint.put(COMPASS.NORTH, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(blueprintDataNorth));
        stained.put(COMPASS.NORTH, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(stainedDataNorth));
        glass.put(COMPASS.NORTH, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(glassDataNorth));
        blueprint.put(COMPASS.WEST, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(blueprintDataWest));
        stained.put(COMPASS.WEST, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(stainedDataWest));
        glass.put(COMPASS.WEST, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(glassDataWest));
        blueprint.put(COMPASS.SOUTH, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(blueprintDataSouth));
        stained.put(COMPASS.SOUTH, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(stainedDataSouth));
        glass.put(COMPASS.SOUTH, TARDISChameleonPoliceBox.buildTARDISChameleonColumn(glassDataSouth));
    }

    public EnumMap<COMPASS, TARDISChameleonColumn> getBlueprint() {
        return blueprint;
    }

    public EnumMap<COMPASS, TARDISChameleonColumn> getStained() {
        return stained;
    }

    public EnumMap<COMPASS, TARDISChameleonColumn> getGlass() {
        return glass;
    }

    public void setBlueprintDataEast(String[][] blueprintDataEast) {
        this.blueprintDataEast = blueprintDataEast;
    }

    public void setStainedDataEast(String[][] stainedDataEast) {
        this.stainedDataEast = stainedDataEast;
    }

    public void setGlassDataEast(String[][] glassDataEast) {
        this.glassDataEast = glassDataEast;
    }

    public void setBlueprintDataNorth(String[][] blueprintDataNorth) {
        this.blueprintDataNorth = blueprintDataNorth;
    }

    public void setStainedDataNorth(String[][] stainedDataNorth) {
        this.stainedDataNorth = stainedDataNorth;
    }

    public void setGlassDataNorth(String[][] glassDataNorth) {
        this.glassDataNorth = glassDataNorth;
    }

    public void setBlueprintDataWest(String[][] blueprintDataWest) {
        this.blueprintDataWest = blueprintDataWest;
    }

    public void setStainedDataWest(String[][] stainedDataWest) {
        this.stainedDataWest = stainedDataWest;
    }

    public void setGlassDataWest(String[][] glassDataWest) {
        this.glassDataWest = glassDataWest;
    }

    public void setBlueprintDataSouth(String[][] blueprintDataSouth) {
        this.blueprintDataSouth = blueprintDataSouth;
    }

    public void setStainedDataSouth(String[][] stainedDataSouth) {
        this.stainedDataSouth = stainedDataSouth;
    }

    public void setGlassDataSouth(String[][] glassDataSouth) {
        this.glassDataSouth = glassDataSouth;
    }
}
