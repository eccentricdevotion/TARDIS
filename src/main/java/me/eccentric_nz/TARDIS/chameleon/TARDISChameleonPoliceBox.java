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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPoliceBox {

    private final PoliceBoxBlackPreset black;
    private final PoliceBoxBluePreset blue;
    private final PoliceBoxBlueOpenPreset blue_open;
    private final PoliceBoxBrownPreset brown;
    private final PoliceBoxCyanPreset cyan;
    private final PoliceBoxGrayPreset gray;
    private final PoliceBoxGreenPreset green;
    private final PoliceBoxLightBluePreset lightBlue;
    private final PoliceBoxLightGrayPreset lightGray;
    private final PoliceBoxLimePreset lime;
    private final PoliceBoxMagentaPreset magenta;
    private final PoliceBoxOrangePreset orange;
    private final PoliceBoxPinkPreset pink;
    private final PoliceBoxPurplePreset purple;
    private final PoliceBoxRedPreset red;
    private final PoliceBoxWhitePreset white;
    private final PoliceBoxYellowPreset yellow;

    public TARDISChameleonPoliceBox() {
        black = new PoliceBoxBlackPreset();
        blue = new PoliceBoxBluePreset();
        blue_open = new PoliceBoxBlueOpenPreset();
        brown = new PoliceBoxBrownPreset();
        cyan = new PoliceBoxCyanPreset();
        gray = new PoliceBoxGrayPreset();
        green = new PoliceBoxGreenPreset();
        lightBlue = new PoliceBoxLightBluePreset();
        lightGray = new PoliceBoxLightGrayPreset();
        lime = new PoliceBoxLimePreset();
        magenta = new PoliceBoxMagentaPreset();
        orange = new PoliceBoxOrangePreset();
        pink = new PoliceBoxPinkPreset();
        purple = new PoliceBoxPurplePreset();
        red = new PoliceBoxRedPreset();
        white = new PoliceBoxWhitePreset();
        yellow = new PoliceBoxYellowPreset();
    }

    public void makePresets() {
        black.makePresets();
        blue.makePresets();
        blue_open.makePresets();
        brown.makePresets();
        cyan.makePresets();
        gray.makePresets();
        green.makePresets();
        lightBlue.makePresets();
        lightGray.makePresets();
        lime.makePresets();
        magenta.makePresets();
        orange.makePresets();
        pink.makePresets();
        purple.makePresets();
        red.makePresets();
        white.makePresets();
        yellow.makePresets();
    }

    public static TARDISChameleonColumn buildTARDISChameleonColumn(String[][] strings) {
        BlockData[][] blockDataArr = getBlockDataFromArray(strings);
        return new TARDISChameleonColumn(blockDataArr);
    }

    /**
     * Converts a 2D String array to a 2D BlockData array.
     *
     * @param arr the String array
     * @return a 2D array of BlockData
     */
    private static BlockData[][] getBlockDataFromArray(String[][] arr) {
        BlockData[][] preset = new BlockData[10][4];
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                preset[col][block] = Bukkit.createBlockData(arr[col][block]);
            }
        }
        return preset;
    }

    public TARDISChameleonColumn getColumn(PRESET p, COMPASS d) {
        switch (p) {
            case POLICE_BOX_WHITE:
                return white.getBlueprint().get(d);
            case POLICE_BOX_ORANGE:
                return orange.getBlueprint().get(d);
            case POLICE_BOX_MAGENTA:
                return magenta.getBlueprint().get(d);
            case POLICE_BOX_LIGHT_BLUE:
                return lightBlue.getBlueprint().get(d);
            case POLICE_BOX_YELLOW:
                return yellow.getBlueprint().get(d);
            case POLICE_BOX_LIME:
                return lime.getBlueprint().get(d);
            case POLICE_BOX_PINK:
                return pink.getBlueprint().get(d);
            case POLICE_BOX_GRAY:
                return gray.getBlueprint().get(d);
            case POLICE_BOX_LIGHT_GRAY:
                return lightGray.getBlueprint().get(d);
            case POLICE_BOX_CYAN:
                return cyan.getBlueprint().get(d);
            case POLICE_BOX_PURPLE:
                return purple.getBlueprint().get(d);
            case POLICE_BOX_BROWN:
                return brown.getBlueprint().get(d);
            case POLICE_BOX_GREEN:
                return green.getBlueprint().get(d);
            case POLICE_BOX_RED:
                return red.getBlueprint().get(d);
            case POLICE_BOX_BLACK:
                return black.getBlueprint().get(d);
            case POLICE_BOX_BLUE_OPEN:
                return blue_open.getBlueprint().get(d);
            default: // POLICE_BOX_BLUE
                return blue.getBlueprint().get(d);
        }
    }

    public TARDISChameleonColumn getGlass(PRESET p, COMPASS d) {
        switch (p) {
            case POLICE_BOX_WHITE:
                return white.getGlass().get(d);
            case POLICE_BOX_ORANGE:
                return orange.getGlass().get(d);
            case POLICE_BOX_MAGENTA:
                return magenta.getGlass().get(d);
            case POLICE_BOX_LIGHT_BLUE:
                return lightBlue.getGlass().get(d);
            case POLICE_BOX_YELLOW:
                return yellow.getGlass().get(d);
            case POLICE_BOX_LIME:
                return lime.getGlass().get(d);
            case POLICE_BOX_PINK:
                return pink.getGlass().get(d);
            case POLICE_BOX_GRAY:
                return gray.getGlass().get(d);
            case POLICE_BOX_LIGHT_GRAY:
                return lightGray.getGlass().get(d);
            case POLICE_BOX_CYAN:
                return cyan.getGlass().get(d);
            case POLICE_BOX_PURPLE:
                return purple.getGlass().get(d);
            case POLICE_BOX_BROWN:
                return brown.getGlass().get(d);
            case POLICE_BOX_GREEN:
                return green.getGlass().get(d);
            case POLICE_BOX_RED:
                return red.getGlass().get(d);
            case POLICE_BOX_BLACK:
                return black.getGlass().get(d);
            default: // POLICE_BOX_BLUE
                return blue.getGlass().get(d);
        }
    }

    public TARDISChameleonColumn getStained(PRESET p, COMPASS d) {
        switch (p) {
            case POLICE_BOX_WHITE:
                return white.getStained().get(d);
            case POLICE_BOX_ORANGE:
                return orange.getStained().get(d);
            case POLICE_BOX_MAGENTA:
                return magenta.getStained().get(d);
            case POLICE_BOX_LIGHT_BLUE:
                return lightBlue.getStained().get(d);
            case POLICE_BOX_YELLOW:
                return yellow.getStained().get(d);
            case POLICE_BOX_LIME:
                return lime.getStained().get(d);
            case POLICE_BOX_PINK:
                return pink.getStained().get(d);
            case POLICE_BOX_GRAY:
                return gray.getStained().get(d);
            case POLICE_BOX_LIGHT_GRAY:
                return lightGray.getStained().get(d);
            case POLICE_BOX_CYAN:
                return cyan.getStained().get(d);
            case POLICE_BOX_PURPLE:
                return purple.getStained().get(d);
            case POLICE_BOX_BROWN:
                return brown.getStained().get(d);
            case POLICE_BOX_GREEN:
                return green.getStained().get(d);
            case POLICE_BOX_RED:
                return red.getStained().get(d);
            case POLICE_BOX_BLACK:
                return black.getStained().get(d);
            default: // POLICE_BOX_BLUE
                return blue.getStained().get(d);
        }
    }
}
