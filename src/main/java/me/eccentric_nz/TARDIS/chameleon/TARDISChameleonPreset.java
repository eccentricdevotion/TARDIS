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
import me.eccentric_nz.TARDIS.utility.recalculators.TARDISButtonRecalculator;
import me.eccentric_nz.TARDIS.utility.recalculators.TARDISLeverRecalculator;
import me.eccentric_nz.TARDIS.utility.recalculators.TARDISMushroomRecalculator;
import me.eccentric_nz.TARDIS.utility.recalculators.TARDISStairRecalculator;
import me.eccentric_nz.TARDIS.utility.recalculators.TARDISTorchRecalculator;
import me.eccentric_nz.TARDIS.utility.recalculators.TARDISTrapdoorRecalculator;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPreset {

    private final List<Integer> problemBlocks = Arrays.asList(new Integer[]{50, 53, 63, 64, 67, 68, 69, 71, 77, 91, 96, 99, 106, 109, 114, 128, 145, 156});
    public final TARDISAngelDownPreset angeld;
    public final TARDISAngelUpPreset angelu;
    public final TARDISAppertureSciencePreset apperture;
    public final TARDISCakePreset cake;
    public final TARDISCandyCanePreset candy;
    public final TARDISChalicePreset chalice;
    public final TARDISColumnPreset column;
    public final TARDISCreepyPreset creepy;
    public final TARDISDesertPreset desert;
    public final TARDISDoubleHelixPreset helix;
    public final TARDISFactoryPreset factory;
    public final TARDISFencePreset fence;
    public final TARDISFlowerPreset flower;
    public final TARDISGazeboPreset gazebo;
    public final TARDISGravestonePreset gravestone;
    public final TARDISJailPreset jail;
    public final TARDISJunglePreset jungle;
    public final TARDISLampPostPreset lamp;
    public final TARDISLibraryPreset library;
    public final TARDISLighthousePreset lighthouse;
    public final TARDISMineshaftPreset mine;
    public final TARDISMushroomPreset shroom;
    public final TARDISNetherPreset nether;
    public final TARDISPandoricaPreset pandorica;
    public final TARDISPartyPreset party;
    public final TARDISPeanutButterPreset peanut;
    public final TARDISPineTreePreset pine;
    public final TARDISPoliceBoxPreset police;
    public final TARDISPortalPreset portal;
    public final TARDISPunkedPreset punked;
    public final TARDISRobotPreset robot;
    public final TARDISRubberDuckPreset duck;
    public final TARDISSnowmanPreset snowman;
    public final TARDISSubmergedPreset submerged;
    public final TARDISSwampPreset swamp;
    public final TARDISTallerPreset taller;
    public final TARDISTelephoneBoxPreset telephone;
    public final TARDISTheEndPreset theend;
    public final TARDISToiletPreset toilet;
    public final TARDISTopsyTurveyPreset topsyturvey;
    public final TARDISTorchPreset torch;
    public final TARDISVillagePreset village;
    public final TARDISWellPreset well;
    public final TARDISWindmillPreset windmill;
    public final TARDISYellowSubmarinePreset yellow;
    public final TARDISCustomPreset custom;
    int r;

    public TARDISChameleonPreset() {
        this.angeld = new TARDISAngelDownPreset();
        this.angelu = new TARDISAngelUpPreset();
        this.apperture = new TARDISAppertureSciencePreset();
        this.cake = new TARDISCakePreset();
        this.candy = new TARDISCandyCanePreset();
        this.chalice = new TARDISChalicePreset();
        this.column = new TARDISColumnPreset();
        this.creepy = new TARDISCreepyPreset();
        this.desert = new TARDISDesertPreset();
        this.duck = new TARDISRubberDuckPreset();
        this.factory = new TARDISFactoryPreset();
        this.fence = new TARDISFencePreset();
        this.flower = new TARDISFlowerPreset();
        this.gazebo = new TARDISGazeboPreset();
        this.gravestone = new TARDISGravestonePreset();
        this.helix = new TARDISDoubleHelixPreset();
        this.jail = new TARDISJailPreset();
        this.jungle = new TARDISJunglePreset();
        this.lamp = new TARDISLampPostPreset();
        this.library = new TARDISLibraryPreset();
        this.lighthouse = new TARDISLighthousePreset();
        this.mine = new TARDISMineshaftPreset();
        this.nether = new TARDISNetherPreset();
        this.pandorica = new TARDISPandoricaPreset();
        this.party = new TARDISPartyPreset();
        this.peanut = new TARDISPeanutButterPreset();
        this.pine = new TARDISPineTreePreset();
        this.police = new TARDISPoliceBoxPreset();
        this.portal = new TARDISPortalPreset();
        this.punked = new TARDISPunkedPreset();
        this.robot = new TARDISRobotPreset();
        this.shroom = new TARDISMushroomPreset();
        this.snowman = new TARDISSnowmanPreset();
        this.submerged = new TARDISSubmergedPreset();
        this.swamp = new TARDISSwampPreset();
        this.taller = new TARDISTallerPreset();
        this.telephone = new TARDISTelephoneBoxPreset();
        this.theend = new TARDISTheEndPreset();
        this.toilet = new TARDISToiletPreset();
        this.topsyturvey = new TARDISTopsyTurveyPreset();
        this.torch = new TARDISTorchPreset();
        this.village = new TARDISVillagePreset();
        this.well = new TARDISWellPreset();
        this.windmill = new TARDISWindmillPreset();
        this.yellow = new TARDISYellowSubmarinePreset();
        this.custom = new TARDISCustomPreset();
    }

    public void makePresets() {
        angeld.makePresets();
        angelu.makePresets();
        apperture.makePresets();
        cake.makePresets();
        candy.makePresets();
        chalice.makePresets();
        column.makePresets();
        creepy.makePresets();
        desert.makePresets();
        duck.makePresets();
        factory.makePresets();
        fence.makePresets();
        flower.makePresets();
        gazebo.makePresets();
        gravestone.makePresets();
        helix.makePresets();
        jail.makePresets();
        jungle.makePresets();
        lamp.makePresets();
        library.makePresets();
        lighthouse.makePresets();
        mine.makePresets();
        nether.makePresets();
        pandorica.makePresets();
        party.makePresets();
        peanut.makePresets();
        pine.makePresets();
        police.makePresets();
        portal.makePresets();
        punked.makePresets();
        robot.makePresets();
        shroom.makePresets();
        snowman.makePresets();
        submerged.makePresets();
        swamp.makePresets();
        taller.makePresets();
        telephone.makePresets();
        theend.makePresets();
        toilet.makePresets();
        topsyturvey.makePresets();
        torch.makePresets();
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
                    int[] zero_w = id[0];
                    int[] one_w = id[1];
                    int[] two_w = id[2];
                    int[] four_w = id[4];
                    int[] five_w = id[5];
                    int[] six_w = id[6];
                    id[1] = five_w;
                    id[2] = zero_w;
                    id[0] = two_w;
                    id[4] = six_w;
                    id[5] = one_w;
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
                        case 50: // torches
                            data[col][block] = new TARDISTorchRecalculator().recalculate(data[col][block], d);
                            break;
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
                        case 69: // lever
                            data[col][block] = new TARDISLeverRecalculator().recalculate(data[col][block], d);
                            break;
                        case 63: // sign post
                        case 68: // wall sign
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (id[col][block] == 63) ? (byte) 8 : (byte) 2;
                                    break;
                                case WEST:
                                    data[col][block] = (id[col][block] == 63) ? (byte) 12 : (byte) 5;
                                    break;
                                default:
                                    data[col][block] = (id[col][block] == 63) ? (byte) 0 : (byte) 3;
                                    break;
                            }
                            break;
                        case 53: // oak wood stair
                        case 67: // cobble stair
                        case 109: // smooth stair
                        case 114: // nether brick stair
                        case 128: // sandstone stair
                        case 156: // quartz stair
                            data[col][block] = new TARDISStairRecalculator().recalculate(data[col][block], d, col);
                            break;
                        case 77: // button
                            data[col][block] = new TARDISButtonRecalculator().recalculate(data[col][block], d);
                            break;
                        case 96: // trapdoor
                            data[col][block] = new TARDISTrapdoorRecalculator().recalculate(data[col][block], d);
                            break;
                        case 99: // mushroom
                            data[col][block] = new TARDISMushroomRecalculator().recalculate(data[col][block], d, col);
                            break;
                        case 145: //
                            switch (d) {
                                case WEST:
                                    break;
                                default:
                                    switch (data[col][block]) {
                                        case 1:
                                            data[col][block] = (byte) 3;
                                        default:
                                            data[col][block] = (byte) 1;
                                    }
                                    break;
                            }
                        case 91: // Jack-o-lantern
                            switch (d) {
                                case SOUTH:
                                    data[col][block] = (byte) 2;
                                    break;
                                case WEST:
                                    data[col][block] = (byte) 3;
                                    break;
                                default:
                                    data[col][block] = (byte) 0;
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
            case ANGEL:
                if (r == 0) {
                    return angelu.getBlueprint().get(d);
                } else {
                    return angeld.getBlueprint().get(d);
                }
            case APPERTURE:
                return apperture.getBlueprint().get(d);
            case CAKE:
                return cake.getBlueprint().get(d);
            case CANDY:
                return candy.getBlueprint().get(d);
            case CHALICE:
                return chalice.getBlueprint().get(d);
            case CREEPY:
                return creepy.getBlueprint().get(d);
            case DESERT:
                return desert.getBlueprint().get(d);
            case DUCK:
                return duck.getBlueprint().get(d);
            case FACTORY:
                return factory.getBlueprint().get(d);
            case FENCE:
                return fence.getBlueprint().get(d);
            case FLOWER:
                return flower.getBlueprint().get(d);
            case GAZEBO:
                return gazebo.getBlueprint().get(d);
            case GRAVESTONE:
                return gravestone.getBlueprint().get(d);
            case HELIX:
                return helix.getBlueprint().get(d);
            case JAIL:
                return jail.getBlueprint().get(d);
            case JUNGLE:
                return jungle.getBlueprint().get(d);
            case LAMP:
                return lamp.getBlueprint().get(d);
            case LIBRARY:
                return library.getBlueprint().get(d);
            case LIGHTHOUSE:
                return lighthouse.getBlueprint().get(d);
            case MINESHAFT:
                return mine.getBlueprint().get(d);
            case NETHER:
                return nether.getBlueprint().get(d);
            case OLD:
                return police.getBlueprint().get(d);
            case PANDORICA:
                return pandorica.getBlueprint().get(d);
            case PARTY:
                return party.getBlueprint().get(d);
            case PEANUT:
                return peanut.getBlueprint().get(d);
            case PINE:
                return pine.getBlueprint().get(d);
            case PORTAL:
                return portal.getBlueprint().get(d);
            case PUNKED:
                return punked.getBlueprint().get(d);
            case ROBOT:
                return robot.getBlueprint().get(d);
            case SHROOM:
                return shroom.getBlueprint().get(d);
            case SNOWMAN:
                return snowman.getBlueprint().get(d);
            case STONE:
                return column.getBlueprint().get(d);
            case SUBMERGED:
                return submerged.getBlueprint().get(d);
            case SWAMP:
                return swamp.getBlueprint().get(d);
            case TELEPHONE:
                return telephone.getBlueprint().get(d);
            case THEEND:
                return theend.getBlueprint().get(d);
            case TOILET:
                return toilet.getBlueprint().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getBlueprint().get(d);
            case TORCH:
                return torch.getBlueprint().get(d);
            case VILLAGE:
                return village.getBlueprint().get(d);
            case WELL:
                return well.getBlueprint().get(d);
            case WINDMILL:
                return windmill.getBlueprint().get(d);
            case YELLOW:
                return yellow.getBlueprint().get(d);
            case CUSTOM:
                return custom.getBlueprint().get(d);
            default:
                return taller.getBlueprint().get(d);
        }
    }

    public TARDISChameleonColumn getGlass(TARDISConstants.PRESET p, TARDISConstants.COMPASS d) {
        switch (p) {
            case ANGEL:
                if (r == 0) {
                    return angelu.getGlass().get(d);
                } else {
                    return angeld.getGlass().get(d);
                }
            case APPERTURE:
                return apperture.getGlass().get(d);
            case CAKE:
                return cake.getGlass().get(d);
            case CANDY:
                return candy.getGlass().get(d);
            case CHALICE:
                return chalice.getGlass().get(d);
            case CREEPY:
                return creepy.getGlass().get(d);
            case DESERT:
                return desert.getGlass().get(d);
            case DUCK:
                return duck.getGlass().get(d);
            case FACTORY:
                return factory.getGlass().get(d);
            case FENCE:
                return fence.getGlass().get(d);
            case FLOWER:
                return flower.getGlass().get(d);
            case GAZEBO:
                return gazebo.getGlass().get(d);
            case GRAVESTONE:
                return gravestone.getGlass().get(d);
            case HELIX:
                return helix.getGlass().get(d);
            case JAIL:
                return jail.getGlass().get(d);
            case JUNGLE:
                return jungle.getGlass().get(d);
            case LAMP:
                return lamp.getGlass().get(d);
            case LIBRARY:
                return library.getGlass().get(d);
            case LIGHTHOUSE:
                return lighthouse.getGlass().get(d);
            case MINESHAFT:
                return mine.getGlass().get(d);
            case NETHER:
                return nether.getGlass().get(d);
            case OLD:
                return police.getGlass().get(d);
            case PANDORICA:
                return pandorica.getGlass().get(d);
            case PARTY:
                return party.getGlass().get(d);
            case PEANUT:
                return peanut.getGlass().get(d);
            case PINE:
                return pine.getGlass().get(d);
            case PORTAL:
                return portal.getGlass().get(d);
            case PUNKED:
                return punked.getGlass().get(d);
            case ROBOT:
                return robot.getGlass().get(d);
            case SHROOM:
                return shroom.getGlass().get(d);
            case SNOWMAN:
                return snowman.getGlass().get(d);
            case STONE:
                return column.getGlass().get(d);
            case SUBMERGED:
                return submerged.getGlass().get(d);
            case SWAMP:
                return swamp.getGlass().get(d);
            case TELEPHONE:
                return telephone.getGlass().get(d);
            case THEEND:
                return theend.getGlass().get(d);
            case TOILET:
                return toilet.getGlass().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getGlass().get(d);
            case TORCH:
                return torch.getGlass().get(d);
            case VILLAGE:
                return village.getGlass().get(d);
            case WELL:
                return well.getGlass().get(d);
            case WINDMILL:
                return windmill.getGlass().get(d);
            case YELLOW:
                return yellow.getGlass().get(d);
            case CUSTOM:
                return custom.getGlass().get(d);
            default:
                return taller.getGlass().get(d);
        }
    }

    public TARDISChameleonColumn getIce(TARDISConstants.PRESET p, TARDISConstants.COMPASS d) {
        switch (p) {
            case ANGEL:
                if (r == 0) {
                    return angelu.getIce().get(d);
                } else {
                    return angeld.getIce().get(d);
                }
            case APPERTURE:
                return apperture.getIce().get(d);
            case CAKE:
                return cake.getIce().get(d);
            case CANDY:
                return candy.getIce().get(d);
            case CHALICE:
                return chalice.getIce().get(d);
            case CREEPY:
                return creepy.getIce().get(d);
            case DESERT:
                return desert.getIce().get(d);
            case DUCK:
                return duck.getIce().get(d);
            case FACTORY:
                return factory.getIce().get(d);
            case FENCE:
                return fence.getIce().get(d);
            case FLOWER:
                return flower.getIce().get(d);
            case GAZEBO:
                return gazebo.getIce().get(d);
            case GRAVESTONE:
                return gravestone.getIce().get(d);
            case HELIX:
                return helix.getIce().get(d);
            case JAIL:
                return jail.getIce().get(d);
            case JUNGLE:
                return jungle.getIce().get(d);
            case LAMP:
                return lamp.getIce().get(d);
            case LIBRARY:
                return library.getIce().get(d);
            case LIGHTHOUSE:
                return lighthouse.getIce().get(d);
            case MINESHAFT:
                return mine.getIce().get(d);
            case NETHER:
                return nether.getIce().get(d);
            case OLD:
                return police.getIce().get(d);
            case PANDORICA:
                return pandorica.getIce().get(d);
            case PARTY:
                return party.getIce().get(d);
            case PEANUT:
                return peanut.getIce().get(d);
            case PINE:
                return pine.getIce().get(d);
            case PORTAL:
                return portal.getIce().get(d);
            case PUNKED:
                return punked.getIce().get(d);
            case ROBOT:
                return robot.getIce().get(d);
            case SHROOM:
                return shroom.getIce().get(d);
            case SNOWMAN:
                return snowman.getIce().get(d);
            case STONE:
                return column.getIce().get(d);
            case SUBMERGED:
                return submerged.getIce().get(d);
            case SWAMP:
                return swamp.getIce().get(d);
            case TELEPHONE:
                return telephone.getIce().get(d);
            case THEEND:
                return theend.getIce().get(d);
            case TOILET:
                return toilet.getIce().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getIce().get(d);
            case TORCH:
                return torch.getIce().get(d);
            case VILLAGE:
                return village.getIce().get(d);
            case WELL:
                return well.getIce().get(d);
            case WINDMILL:
                return windmill.getIce().get(d);
            case YELLOW:
                return yellow.getIce().get(d);
            case CUSTOM:
                return custom.getIce().get(d);
            default:
                return taller.getIce().get(d);
        }
    }

    public void setR(int r) {
        this.r = r;
    }

}
