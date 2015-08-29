/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
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

    private static final List<Integer> problemBlocks = Arrays.asList(50, 53, 63, 64, 66, 67, 68, 69, 71, 77, 91, 96, 99, 106, 108, 109, 114, 128, 134, 135, 136, 143, 145, 156, 163, 164, 193, 194, 195, 196, 197);
    public final TARDISAndesitePreset andesite;
    public final TARDISAngelDownPreset angeld;
    public final TARDISAngelUpPreset angelu;
    public final TARDISAppertureSciencePreset apperture;
    public final TARDISCakePreset cake;
    public final TARDISCandyCanePreset candy;
    public final TARDISChalicePreset chalice;
    public final TARDISColumnPreset column;
    public final TARDISCreepyPreset creepy;
    public final TARDISDesertPreset desert;
    public final TARDISDioritePreset diorite;
    public final TARDISDoubleHelixPreset helix;
    public final TARDISFactoryPreset factory;
    public final TARDISFencePreset fence;
    public final TARDISFlowerPreset flower;
    public final TARDISGazeboPreset gazebo;
    public final TARDISGranitePreset granite;
    public final TARDISGravestonePreset gravestone;
    public final TARDISInvisiblePreset invisible;
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
    public final TARDISPrismarinePreset prismarine;
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
    public final TARDISRenderPreset render;
    int r;

    public TARDISChameleonPreset() {
        this.andesite = new TARDISAndesitePreset();
        this.angeld = new TARDISAngelDownPreset();
        this.angelu = new TARDISAngelUpPreset();
        this.apperture = new TARDISAppertureSciencePreset();
        this.cake = new TARDISCakePreset();
        this.candy = new TARDISCandyCanePreset();
        this.chalice = new TARDISChalicePreset();
        this.column = new TARDISColumnPreset();
        this.creepy = new TARDISCreepyPreset();
        this.desert = new TARDISDesertPreset();
        this.diorite = new TARDISDioritePreset();
        this.duck = new TARDISRubberDuckPreset();
        this.factory = new TARDISFactoryPreset();
        this.fence = new TARDISFencePreset();
        this.flower = new TARDISFlowerPreset();
        this.gazebo = new TARDISGazeboPreset();
        this.granite = new TARDISGranitePreset();
        this.gravestone = new TARDISGravestonePreset();
        this.helix = new TARDISDoubleHelixPreset();
        this.invisible = new TARDISInvisiblePreset();
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
        this.prismarine = new TARDISPrismarinePreset();
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
        this.render = new TARDISRenderPreset();
    }

    public void makePresets() {
        andesite.makePresets(false, false, false);
        angeld.makePresets(true, false, false);
        angelu.makePresets(true, false, false);
        apperture.makePresets(false, false, false);
        cake.makePresets(false, false, false);
        candy.makePresets(true, false, true);
        chalice.makePresets(false, false, false);
        column.makePresets(false, false, false);
        creepy.makePresets(false, false, false);
        desert.makePresets(false, false, false);
        diorite.makePresets(false, false, false);
        duck.makePresets(true, true, false);
        factory.makePresets(false, false, false);
        fence.makePresets(true, false, false);
        flower.makePresets(false, false, false);
        gazebo.makePresets(false, false, false);
        granite.makePresets(false, false, false);
        gravestone.makePresets(true, false, false);
        helix.makePresets(false, false, false);
        invisible.makePresets(true, true, true);
        jail.makePresets(false, false, false);
        jungle.makePresets(false, false, false);
        lamp.makePresets(true, false, true);
        library.makePresets(false, false, false);
        lighthouse.makePresets(false, false, false);
        mine.makePresets(false, false, false);
        nether.makePresets(false, false, false);
        pandorica.makePresets(false, false, false);
        party.makePresets(false, false, false);
        peanut.makePresets(false, false, false);
        pine.makePresets(false, false, false);
        police.makePresets(false, false, false);
        portal.makePresets(false, false, false);
        prismarine.makePresets(false, false, false);
        punked.makePresets(false, false, false);
        robot.makePresets(true, false, false);
        shroom.makePresets(false, false, false);
        snowman.makePresets(true, false, false);
        submerged.makePresets(true, false, false);
        swamp.makePresets(false, false, false);
        taller.makePresets(false, false, false);
        telephone.makePresets(false, false, false);
        theend.makePresets(false, false, false);
        toilet.makePresets(true, false, false);
        topsyturvey.makePresets(false, false, false);
        torch.makePresets(true, false, true);
        village.makePresets(false, false, false);
        well.makePresets(false, false, false);
        windmill.makePresets(true, false, false);
        yellow.makePresets(false, false, false);
        custom.makePresets();
        render.makePresets(false, false, false);
    }

    public static TARDISChameleonColumn buildTARDISChameleonColumn(COMPASS d, String id, String data, boolean asymmetric, boolean duck, boolean istorch) {
        TARDISChameleonColumn tcc = new TARDISChameleonColumn();
        if (d.equals(COMPASS.EAST)) {
            tcc.setId(getIntArrayFromJSON(id));
            tcc.setData(getByteArrayFromJSON(data));
        } else {
            int[][] id_arr = rotate2DIntArray(getIntArrayFromJSON(id), d, asymmetric);
            byte[][] data_arr = rotate2DByteArray(getByteArrayFromJSON(data), d, asymmetric);
            tcc.setId(id_arr);
            tcc.setData(convertData(id_arr, data_arr, d, duck, istorch));
        }
        return tcc;
    }

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of ints
     */
    private static int[][] getIntArrayFromJSON(String js) {
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
    private static byte[][] getByteArrayFromJSON(String js) {
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

    private static int[][] rotate2DIntArray(int[][] id, COMPASS d, boolean assymetric) {
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

    private static byte[][] rotate2DByteArray(byte[][] id, COMPASS d, boolean assymetric) {
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

    private static byte[][] convertData(int[][] id, byte[][] data, COMPASS d, boolean duck, boolean istorch) {
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                if (problemBlocks.contains(id[col][block])) {
                    switch (id[col][block]) {
                        case 50: // torches
                            data[col][block] = new TARDISTorchRecalculator().recalculate(data[col][block], d);
                            break;
                        case 64: // oak door
                        case 71: // iron door
                        case 193: // spruce door
                        case 194: // birch door
                        case 195: // jungle door
                        case 196: // acacia door
                        case 197: // dark oak door
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
                        case 66: // rails
                            switch (d) {
                                case WEST:
                                    data[col][block] = (byte) 1;
                                    break;
                                default:
                                    data[col][block] = (byte) 0;
                                    break;
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
                        case 108: // brick stair
                        case 109: // smooth stair
                        case 114: // nether brick stair
                        case 128: // sandstone stair
                        case 134: // spruce stair
                        case 135: // birch stair
                        case 136: // jungle stair
                        case 156: // quartz stair
                        case 163: // acacia stair
                        case 164: // dark oak stair
                            data[col][block] = new TARDISStairRecalculator().recalculate(data[col][block], d, col, duck);
                            break;
                        case 77: // stone button
                        case 143: // wood button
                            data[col][block] = new TARDISButtonRecalculator().recalculate(data[col][block], d);
                            break;
                        case 96: // trapdoor
                            data[col][block] = new TARDISTrapdoorRecalculator().recalculate(data[col][block], d, istorch);
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
                                            break;
                                        default:
                                            data[col][block] = (byte) 1;
                                    }
                            }
                            break;
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

    public TARDISChameleonColumn getColumn(PRESET p, COMPASS d) {
        switch (p) {
            case ANDESITE:
                return andesite.getBlueprint().get(d);
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
            case DIORITE:
                return diorite.getBlueprint().get(d);
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
            case GRANITE:
                return granite.getBlueprint().get(d);
            case GRAVESTONE:
                return gravestone.getBlueprint().get(d);
            case HELIX:
                return helix.getBlueprint().get(d);
            case INVISIBLE:
                return invisible.getBlueprint().get(d);
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
            case PRISMARINE:
                return prismarine.getBlueprint().get(d);
            case PUNKED:
                return punked.getBlueprint().get(d);
            case RENDER:
                return render.getBlueprint().get(d);
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

    public TARDISChameleonColumn getGlass(PRESET p, COMPASS d) {
        switch (p) {
            case ANDESITE:
                return andesite.getGlass().get(d);
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
            case DIORITE:
                return diorite.getGlass().get(d);
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
            case GRANITE:
                return granite.getGlass().get(d);
            case GRAVESTONE:
                return gravestone.getGlass().get(d);
            case HELIX:
                return helix.getGlass().get(d);
            case INVISIBLE:
                return invisible.getGlass().get(d);
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
            case PRISMARINE:
                return prismarine.getGlass().get(d);
            case PUNKED:
                return punked.getGlass().get(d);
            case RENDER:
                return render.getGlass().get(d);
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

    public TARDISChameleonColumn getStained(PRESET p, COMPASS d) {
        switch (p) {
            case ANDESITE:
                return andesite.getStained().get(d);
            case ANGEL:
                if (r == 0) {
                    return angelu.getStained().get(d);
                } else {
                    return angeld.getStained().get(d);
                }
            case APPERTURE:
                return apperture.getStained().get(d);
            case CAKE:
                return cake.getStained().get(d);
            case CANDY:
                return candy.getStained().get(d);
            case CHALICE:
                return chalice.getStained().get(d);
            case CREEPY:
                return creepy.getStained().get(d);
            case DESERT:
                return desert.getStained().get(d);
            case DIORITE:
                return diorite.getStained().get(d);
            case DUCK:
                return duck.getStained().get(d);
            case FACTORY:
                return factory.getStained().get(d);
            case FENCE:
                return fence.getStained().get(d);
            case FLOWER:
                return flower.getStained().get(d);
            case GAZEBO:
                return gazebo.getStained().get(d);
            case GRANITE:
                return granite.getStained().get(d);
            case GRAVESTONE:
                return gravestone.getStained().get(d);
            case HELIX:
                return helix.getStained().get(d);
            case INVISIBLE:
                return invisible.getStained().get(d);
            case JAIL:
                return jail.getStained().get(d);
            case JUNGLE:
                return jungle.getStained().get(d);
            case LAMP:
                return lamp.getStained().get(d);
            case LIBRARY:
                return library.getStained().get(d);
            case LIGHTHOUSE:
                return lighthouse.getStained().get(d);
            case MINESHAFT:
                return mine.getStained().get(d);
            case NETHER:
                return nether.getStained().get(d);
            case OLD:
                return police.getStained().get(d);
            case PANDORICA:
                return pandorica.getStained().get(d);
            case PARTY:
                return party.getStained().get(d);
            case PEANUT:
                return peanut.getStained().get(d);
            case PINE:
                return pine.getStained().get(d);
            case PORTAL:
                return portal.getStained().get(d);
            case PRISMARINE:
                return prismarine.getStained().get(d);
            case PUNKED:
                return punked.getStained().get(d);
            case RENDER:
                return render.getStained().get(d);
            case ROBOT:
                return robot.getStained().get(d);
            case SHROOM:
                return shroom.getStained().get(d);
            case SNOWMAN:
                return snowman.getStained().get(d);
            case STONE:
                return column.getStained().get(d);
            case SUBMERGED:
                return submerged.getStained().get(d);
            case SWAMP:
                return swamp.getStained().get(d);
            case TELEPHONE:
                return telephone.getStained().get(d);
            case THEEND:
                return theend.getStained().get(d);
            case TOILET:
                return toilet.getStained().get(d);
            case TOPSYTURVEY:
                return topsyturvey.getStained().get(d);
            case TORCH:
                return torch.getStained().get(d);
            case VILLAGE:
                return village.getStained().get(d);
            case WELL:
                return well.getStained().get(d);
            case WINDMILL:
                return windmill.getStained().get(d);
            case YELLOW:
                return yellow.getStained().get(d);
            case CUSTOM:
                return custom.getStained().get(d);
            default:
                return taller.getStained().get(d);
        }
    }

    public void setR(int r) {
        this.r = r;
    }
}
