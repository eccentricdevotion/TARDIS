/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.JSON.JSONArray;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPreset {

    private final List<Integer> problemBlocks = Arrays.asList(new Integer[]{64, 67, 68, 71, 96, 106, 109, 114, 128, 156});
    public final TARDISCakePreset cake;
    public final TARDISChalicePreset chalice;
    public final TARDISColumnPreset column;
    public final TARDISDesertPreset desert;
    public final TARDISFactoryPreset factory;
    public final TARDISFlowerPreset flower;
    public final TARDISGravestonePreset gravestone;
    public final TARDISJunglePreset jungle;
    public final TARDISNetherPreset nether;
    public final TARDISPartyPreset party;
    public final TARDISPoliceBoxPreset police;
    public final TARDISRaisedPreset raised;
    public final TARDISSubmergedPreset submerged;
    public final TARDISSwampPreset swamp;
    public final TARDISTallerPreset taller;
    public final TARDISTelephoneBoxPreset telephone;
    public final TARDISTopsyTurveyPreset topsyturvey;
    public final TARDISVillagePreset village;
    public final TARDISWellPreset well;
    public final TARDISWindmillPreset windmill;
    public final TARDISYellowSubmarinePreset yellow;
    public final TARDISCustomPreset custom;

    public TARDISChameleonPreset() {
        this.cake = new TARDISCakePreset();
        this.chalice = new TARDISChalicePreset();
        this.column = new TARDISColumnPreset();
        this.desert = new TARDISDesertPreset();
        this.factory = new TARDISFactoryPreset();
        this.flower = new TARDISFlowerPreset();
        this.gravestone = new TARDISGravestonePreset();
        this.jungle = new TARDISJunglePreset();
        this.nether = new TARDISNetherPreset();
        this.party = new TARDISPartyPreset();
        this.police = new TARDISPoliceBoxPreset();
        this.raised = new TARDISRaisedPreset();
        this.submerged = new TARDISSubmergedPreset();
        this.swamp = new TARDISSwampPreset();
        this.taller = new TARDISTallerPreset();
        this.telephone = new TARDISTelephoneBoxPreset();
        this.topsyturvey = new TARDISTopsyTurveyPreset();
        this.village = new TARDISVillagePreset();
        this.well = new TARDISWellPreset();
        this.windmill = new TARDISWindmillPreset();
        this.yellow = new TARDISYellowSubmarinePreset();
        this.custom = new TARDISCustomPreset();
    }

    public void makePresets() {
        cake.makePresets();
        chalice.makePresets();
        column.makePresets();
        desert.makePresets();
        factory.makePresets();
        flower.makePresets();
        gravestone.makePresets();
        jungle.makePresets();
        nether.makePresets();
        party.makePresets();
        police.makePresets();
        raised.makePresets();
        submerged.makePresets();
        swamp.makePresets();
        taller.makePresets();
        telephone.makePresets();
        topsyturvey.makePresets();
        village.makePresets();
        well.makePresets();
        windmill.makePresets();
        yellow.makePresets();
        custom.makePresets();
    }

    public TARDISChameleonColumn buildTARDISChameleonColumn(TARDISConstants.COMPASS d, String id, String data, boolean assyemtric) {
        TARDISChameleonColumn tcc = new TARDISChameleonColumn();
        if (d.equals(TARDISConstants.COMPASS.EAST)) {
            tcc.setId(getIntArrayFromJSON(id));
            tcc.setData(getByteArrayFromJSON(data));
        } else {
            int[][] id_arr = rotate2DIntArray(getIntArrayFromJSON(id), d, assyemtric);
            byte[][] data_arr = rotate2DByteArray(getByteArrayFromJSON(data), d, assyemtric);
            tcc.setId(id_arr);
            tcc.setData(convertData(id_arr, data_arr, d));
        }
        return tcc;
    }

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of ints
     */
    private int[][] getIntArrayFromJSON(String js) {
        int[][] preset = new int[10][4];
        JSONArray json = new JSONArray(js);
        for (int col = 0; col < 10; col++) {
            JSONArray jsoncol = json.getJSONArray(col);
            for (int block = 0; block < 4; block++) {
                preset[col][block] = jsoncol.getInt(block);
            }
        }
        return preset;
    }

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of bytes
     */
    private byte[][] getByteArrayFromJSON(String js) {
        byte[][] preset = new byte[10][4];
        JSONArray json = new JSONArray(js);
        for (byte col = 0; col < 10; col++) {
            JSONArray jsoncol = json.getJSONArray(col);
            for (byte block = 0; block < 4; block++) {
                preset[col][block] = jsoncol.getByte(block);
            }
        }
        return preset;
    }

    private int[][] rotate2DIntArray(int[][] id, TARDISConstants.COMPASS d, boolean assymetric) {
        switch (d) {
            case NORTH:
                int[] zero_s = id[0];
                int[] one_s = id[1];
                int[] two_s = id[2];
                int[] three_s = id[3];
                int[] four_s = id[4];
                int[] five_s = id[5];
                int[] six_s = id[6];
                int[] seven_s = id[7];
                id[0] = two_s;
                id[1] = three_s;
                id[2] = four_s;
                id[3] = five_s;
                id[4] = six_s;
                id[5] = seven_s;
                id[6] = zero_s;
                id[7] = one_s;
                return id;
            case WEST:
                if (assymetric) {
                    int[] two_w = id[2];
                    int[] four_w = id[4];
                    id[2] = id[0];
                    id[0] = two_w;
                    id[4] = id[6];
                    id[6] = four_w;
                }
                int[] three_w = id[3];
                id[3] = id[7];
                id[7] = three_w;
                return id;
            default:
                int[] zero_n = id[0];
                int[] one_n = id[1];
                int[] two_n = id[2];
                int[] three_n = id[3];
                int[] four_n = id[4];
                int[] five_n = id[5];
                int[] six_n = id[6];
                int[] seven_n = id[7];
                id[0] = six_n;
                id[1] = seven_n;
                id[2] = zero_n;
                id[3] = one_n;
                id[4] = two_n;
                id[5] = three_n;
                id[6] = four_n;
                id[7] = five_n;
                return id;
        }
    }

    private byte[][] rotate2DByteArray(byte[][] id, TARDISConstants.COMPASS d, boolean assymetric) {
        switch (d) {
            case NORTH:
                byte[] zero_s = id[0];
                byte[] one_s = id[1];
                byte[] two_s = id[2];
                byte[] three_s = id[3];
                byte[] four_s = id[4];
                byte[] five_s = id[5];
                byte[] six_s = id[6];
                byte[] seven_s = id[7];
                id[0] = two_s;
                id[1] = three_s;
                id[2] = four_s;
                id[3] = five_s;
                id[4] = six_s;
                id[5] = seven_s;
                id[6] = zero_s;
                id[7] = one_s;
                return id;
            case WEST:
                if (assymetric) {
                    byte[] two_w = id[2];
                    byte[] four_w = id[4];
                    id[2] = id[0];
                    id[0] = two_w;
                    id[4] = id[6];
                    id[6] = four_w;
                }
                byte[] three_w = id[3];
                id[3] = id[7];
                id[7] = three_w;
                return id;
            default:
                byte[] zero_n = id[0];
                byte[] one_n = id[1];
                byte[] two_n = id[2];
                byte[] three_n = id[3];
                byte[] four_n = id[4];
                byte[] five_n = id[5];
                byte[] six_n = id[6];
                byte[] seven_n = id[7];
                id[0] = six_n;
                id[1] = seven_n;
                id[2] = zero_n;
                id[3] = one_n;
                id[4] = two_n;
                id[5] = three_n;
                id[6] = four_n;
                id[7] = five_n;
                return id;
        }
    }

    private byte[][] convertData(int[][] id, byte[][] data, TARDISConstants.COMPASS d) {
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                if (problemBlocks.contains(id[col][block])) {
                    switch (id[col][block]) {
                        case 64: // wood door
                        case 71: // iron door
                            if (data[col][block] < 8) {
                                // data is either a 0 or a 2
                                switch (d) {
                                    case SOUTH:
                                        if (data[col][block] == (byte) 0) {
                                            data[col][block] = (byte) 1;
                                        } else {
                                            data[col][block] = (byte) 3;
                                        }
                                        break;
                                    case WEST:
                                        if (data[col][block] == (byte) 0) {
                                            data[col][block] = (byte) 2;
                                        } else {
                                            data[col][block] = (byte) 0;
                                        }
                                        break;
                                    default:
                                        if (data[col][block] == (byte) 0) {
                                            data[col][block] = (byte) 3;
                                        } else {
                                            data[col][block] = (byte) 1;
                                        }
                                        break;
                                }
                            }
                            break;
                        case 68: // wall sign
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 2;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 5;
                                    break;
                                default:
                                    data[col][block] = (byte) 3;
                                    break;
                            }
                            break;
                        case 67: // cobble stair
                        case 109: // smooth stair
                        case 114: // nether brick stair
                        case 128: // sandstone stair
                        case 156: // quartz stair
                            switch (d) {
                                case SOUTH:
                                    switch (data[col][block]) {
                                        case 0:
                                            data[col][block] = (byte) 2;
                                            break;
                                        case 1:
                                            data[col][block] = (byte) 3;
                                            break;
                                        case 2:
                                            data[col][block] = (byte) 1;
                                            break;
                                        case 3:
                                            data[col][block] = (byte) 0;
                                            break;
                                        case 4:
                                            data[col][block] = (byte) 6;
                                            break;
                                        case 5:
                                            data[col][block] = (byte) 7;
                                            break;
                                        case 6:
                                            data[col][block] = (byte) 5;
                                            break;
                                        case 7:
                                            data[col][block] = (byte) 4;
                                            break;
                                    }
                                    break;
                                case WEST:
                                    if (col == 3 || col == 7) {
                                        switch (data[col][block]) {
                                            case 0:
                                                data[col][block] = (byte) 1;
                                                break;
                                            case 1:
                                                data[col][block] = (byte) 0;
                                                break;
                                            case 4:
                                                data[col][block] = (byte) 5;
                                                break;
                                            case 5:
                                                data[col][block] = (byte) 4;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                default:
                                    switch (data[col][block]) {
                                        case 0:
                                            data[col][block] = (byte) 3;
                                            break;
                                        case 1:
                                            data[col][block] = (byte) 2;
                                            break;
                                        case 2:
                                            data[col][block] = (byte) 0;
                                            break;
                                        case 3:
                                            data[col][block] = (byte) 1;
                                            break;
                                        case 4:
                                            data[col][block] = (byte) 7;
                                            break;
                                        case 5:
                                            data[col][block] = (byte) 6;
                                            break;
                                        case 6:
                                            data[col][block] = (byte) 4;
                                            break;
                                        case 7:
                                            data[col][block] = (byte) 5;
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case 96: // trapdoor
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 0;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 3;
                                    break;
                                default:
                                    data[col][block] = (byte) 1;
                                    break;
                            }
                            break;
                        default: // vine
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 1;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 2;
                                    break;
                                default:
                                    data[col][block] = (byte) 4;
                                    break;
                            }
                            break;
                    }
                }
            }
        }
        return data;
    }

    public TARDISChameleonColumn getColumn(TARDISConstants.PRESET p, TARDISConstants.COMPASS d) {
        switch (p) {
            case OLD:
                return police.getPoliceBox().get(d);
            case FACTORY:
                return factory.getFactory().get(d);
            case STONE:
                return column.getColumn().get(d);
            case DESERT:
                return desert.getDesert().get(d);
            case JUNGLE:
                return jungle.getJungle().get(d);
            case NETHER:
                return nether.getNether().get(d);
            case SWAMP:
                return swamp.getSwamp().get(d);
            case PARTY:
                return party.getPartyTent().get(d);
            case VILLAGE:
                return village.getVillage().get(d);
            case YELLOW:
                return yellow.getYellowSub().get(d);
            case SUBMERGED:
                return submerged.getSubmerged().get(d);
            case RAISED:
                return raised.getRaised().get(d);
            case FLOWER:
                return flower.getFlower().get(d);
            case CHALICE:
                return chalice.getChalice().get(d);
            case WINDMILL:
                return windmill.getWindmill().get(d);
            case TELEPHONE:
                return telephone.getTelephone().get(d);
            case WELL:
                return well.getWell().get(d);
            case CAKE:
                return cake.getBlueprint().get(d);
            case GRAVESTONE:
                return gravestone.getBlueprint().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getBlueprint().get(d);
            case CUSTOM:
                return custom.getBlueprint().get(d);
            default:
                return taller.getTaller().get(d);
        }
    }

    public TARDISChameleonColumn getGlass(TARDISConstants.PRESET p, TARDISConstants.COMPASS d) {
        switch (p) {
            case OLD:
                return police.getGlass().get(d);
            case FACTORY:
                return factory.getGlass().get(d);
            case STONE:
                return column.getGlass().get(d);
            case DESERT:
                return desert.getGlass().get(d);
            case JUNGLE:
                return jungle.getGlass().get(d);
            case NETHER:
                return nether.getGlass().get(d);
            case SWAMP:
                return swamp.getGlass().get(d);
            case PARTY:
                return party.getGlass().get(d);
            case VILLAGE:
                return village.getGlass().get(d);
            case YELLOW:
                return yellow.getGlass().get(d);
            case SUBMERGED:
                return submerged.getGlass().get(d);
            case RAISED:
                return raised.getGlass().get(d);
            case FLOWER:
                return flower.getGlass().get(d);
            case CHALICE:
                return chalice.getGlass().get(d);
            case WINDMILL:
                return windmill.getGlass().get(d);
            case TELEPHONE:
                return telephone.getGlass().get(d);
            case WELL:
                return well.getGlass().get(d);
            case CAKE:
                return cake.getGlass().get(d);
            case GRAVESTONE:
                return gravestone.getGlass().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getGlass().get(d);
            case CUSTOM:
                return custom.getGlass().get(d);
            default:
                return taller.getGlass().get(d);
        }
    }

    public TARDISChameleonColumn getIce(TARDISConstants.PRESET p, TARDISConstants.COMPASS d) {
        switch (p) {
            case OLD:
                return police.getIce().get(d);
            case FACTORY:
                return factory.getIce().get(d);
            case STONE:
                return column.getIce().get(d);
            case DESERT:
                return desert.getIce().get(d);
            case JUNGLE:
                return jungle.getIce().get(d);
            case NETHER:
                return nether.getIce().get(d);
            case SWAMP:
                return swamp.getIce().get(d);
            case PARTY:
                return party.getIce().get(d);
            case VILLAGE:
                return village.getIce().get(d);
            case YELLOW:
                return yellow.getIce().get(d);
            case SUBMERGED:
                return submerged.getIce().get(d);
            case RAISED:
                return raised.getIce().get(d);
            case FLOWER:
                return flower.getIce().get(d);
            case CHALICE:
                return chalice.getIce().get(d);
            case WINDMILL:
                return windmill.getIce().get(d);
            case TELEPHONE:
                return telephone.getIce().get(d);
            case WELL:
                return well.getIce().get(d);
            case CAKE:
                return cake.getIce().get(d);
            case GRAVESTONE:
                return gravestone.getIce().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getIce().get(d);
            case CUSTOM:
                return custom.getIce().get(d);
            default:
                return taller.getIce().get(d);
        }
    }
}
