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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.enumeration.CardinalDirection;

import java.util.EnumMap;

/**
 * A chameleon conversion is a repair procedure that technicians perform on tardis chameleon circuits. The Fourth Doctor
 * once said that the reason the tardis' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
class TardisPreset {

    private final EnumMap<CardinalDirection, TardisChameleonColumn> blueprint = new EnumMap<>(CardinalDirection.class);
    private final EnumMap<CardinalDirection, TardisChameleonColumn> stained = new EnumMap<>(CardinalDirection.class);
    private final EnumMap<CardinalDirection, TardisChameleonColumn> glass = new EnumMap<>(CardinalDirection.class);
    private String[][] blueprintData;
    private String[][] stainedData;
    private String[][] glassData;

    TardisPreset() {
    }

    public void makePresets(boolean assymetric, boolean duck) {
        for (CardinalDirection d : CardinalDirection.values()) {
            blueprint.put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, blueprintData, assymetric, duck));
            stained.put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, stainedData, assymetric, duck));
            glass.put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, glassData, assymetric, duck));
        }
    }

    public EnumMap<CardinalDirection, TardisChameleonColumn> getBlueprint() {
        return blueprint;
    }

    public EnumMap<CardinalDirection, TardisChameleonColumn> getStained() {
        return stained;
    }

    public EnumMap<CardinalDirection, TardisChameleonColumn> getGlass() {
        return glass;
    }

    void setBlueprintData(String[][] blueprintData) {
        this.blueprintData = blueprintData;
    }

    void setStainedData(String[][] stainedData) {
        this.stainedData = stainedData;
    }

    void setGlassData(String[][] glassData) {
        this.glassData = glassData;
    }
}
