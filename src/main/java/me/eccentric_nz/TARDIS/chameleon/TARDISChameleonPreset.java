/*
 * Copyright (C) 2023 eccentric_nz
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

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.chameleon.preset.*;
import me.eccentric_nz.TARDIS.chameleon.preset.biome.*;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISBlockDataRotator;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISCustomPreset;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISRenderPreset;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Bukkit;
import org.bukkit.block.data.*;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPreset {

    public final TARDISCustomPreset custom;
    private final TARDISAndesitePreset andesite;
    private final TARDISAngelDownPreset angeld;
    private final TARDISAngelUpPreset angelu;
    private final TARDISApertureSciencePreset aperture;
    private final TARDISCakePreset cake;
    private final TARDISCandyCanePreset candy;
    private final TARDISCavePreset cave;
    private final TARDISChalicePreset chalice;
    private final TARDISChorusPreset chorus;
    private final TARDISColumnPreset column;
    private final TARDISCreepyPreset creepy;
    private final TARDISDesertPreset desert;
    private final TARDISDioritePreset diorite;
    private final TARDISDoubleHelixPreset helix;
    private final TARDISFactoryPreset factory;
    private final TARDISFencePreset fence;
    private final TARDISFlowerPreset flower;
    private final TARDISGazeboPreset gazebo;
    private final TARDISGranitePreset granite;
    private final TARDISGravestonePreset gravestone;
    private final TARDISInvisiblePreset invisible;
    private final TARDISJailPreset jail;
    private final TARDISJunglePreset jungle;
    private final TARDISJunkPreset junk;
    private final TARDISLampPostPreset lamp;
    private final TARDISLibraryPreset library;
    private final TARDISLighthousePreset lighthouse;
    private final TARDISMineshaftPreset mine;
    private final TARDISMushroomPreset shroom;
    private final TARDISNetherPreset nether;
    private final TARDISPandoricaPreset pandorica;
    private final TARDISPartyPreset party;
    private final TARDISPeanutButterPreset peanut;
    private final TARDISPineTreePreset pine;
    private final TARDISPortalPreset portal;
    private final TARDISPrismarinePreset prismarine;
    private final TARDISPunkedPreset punked;
    private final TARDISRobotPreset robot;
    private final TARDISRubberDuckPreset duck;
    private final TARDISSnowmanPreset snowman;
    private final TARDISSubmergedPreset submerged;
    private final TARDISSwampPreset swamp;
    private final TARDISTelephoneBoxPreset telephone;
    private final TARDISTheEndPreset theend;
    private final TARDISToiletPreset toilet;
    private final TARDISTopsyTurveyPreset topsyturvey;
    private final TARDISTorchPreset torch;
    private final TARDISVillagePreset village;
    private final TARDISWellPreset well;
    private final TARDISWindmillPreset windmill;
    private final TARDISYellowSubmarinePreset yellow;
    // biome adaptive presets
    private final TARDISAdaptivePreset adaptive;
    private final TARDISRenderPreset render;
    private final TARDISExtremeHillsPreset extreme;
    private final TARDISForestPreset forest;
    private final TARDISIcePlainsPreset flats;
    private final TARDISIcePlainsSpikesPreset spikes;
    private final TARDISMesaPreset mesa;
    private final TARDISPlainsPreset plains;
    private final TARDISRoofedForestPreset roofed;
    private final TARDISSavannaPreset savanna;
    private final TARDISTaigaPreset taiga;
    private final TARDISColdTaigaPreset cold;
    private final TARDISBoatPreset boat;
    private int r;

    public TARDISChameleonPreset() {
        andesite = new TARDISAndesitePreset();
        angeld = new TARDISAngelDownPreset();
        angelu = new TARDISAngelUpPreset();
        aperture = new TARDISApertureSciencePreset();
        cake = new TARDISCakePreset();
        candy = new TARDISCandyCanePreset();
        chalice = new TARDISChalicePreset();
        chorus = new TARDISChorusPreset();
        column = new TARDISColumnPreset();
        creepy = new TARDISCreepyPreset();
        desert = new TARDISDesertPreset();
        diorite = new TARDISDioritePreset();
        duck = new TARDISRubberDuckPreset();
        factory = new TARDISFactoryPreset();
        fence = new TARDISFencePreset();
        flower = new TARDISFlowerPreset();
        gazebo = new TARDISGazeboPreset();
        granite = new TARDISGranitePreset();
        gravestone = new TARDISGravestonePreset();
        helix = new TARDISDoubleHelixPreset();
        invisible = new TARDISInvisiblePreset();
        jail = new TARDISJailPreset();
        jungle = new TARDISJunglePreset();
        junk = new TARDISJunkPreset();
        lamp = new TARDISLampPostPreset();
        library = new TARDISLibraryPreset();
        lighthouse = new TARDISLighthousePreset();
        mine = new TARDISMineshaftPreset();
        nether = new TARDISNetherPreset();
        pandorica = new TARDISPandoricaPreset();
        party = new TARDISPartyPreset();
        peanut = new TARDISPeanutButterPreset();
        pine = new TARDISPineTreePreset();
        portal = new TARDISPortalPreset();
        prismarine = new TARDISPrismarinePreset();
        punked = new TARDISPunkedPreset();
        robot = new TARDISRobotPreset();
        shroom = new TARDISMushroomPreset();
        snowman = new TARDISSnowmanPreset();
        submerged = new TARDISSubmergedPreset();
        swamp = new TARDISSwampPreset();
        telephone = new TARDISTelephoneBoxPreset();
        theend = new TARDISTheEndPreset();
        toilet = new TARDISToiletPreset();
        topsyturvey = new TARDISTopsyTurveyPreset();
        torch = new TARDISTorchPreset();
        village = new TARDISVillagePreset();
        well = new TARDISWellPreset();
        windmill = new TARDISWindmillPreset();
        yellow = new TARDISYellowSubmarinePreset();
        custom = new TARDISCustomPreset();
        adaptive = new TARDISAdaptivePreset();
        render = new TARDISRenderPreset();
        extreme = new TARDISExtremeHillsPreset();
        forest = new TARDISForestPreset();
        flats = new TARDISIcePlainsPreset();
        spikes = new TARDISIcePlainsSpikesPreset();
        mesa = new TARDISMesaPreset();
        plains = new TARDISPlainsPreset();
        roofed = new TARDISRoofedForestPreset();
        savanna = new TARDISSavannaPreset();
        taiga = new TARDISTaigaPreset();
        cold = new TARDISColdTaigaPreset();
        boat = new TARDISBoatPreset();
        cave = new TARDISCavePreset();
    }

    public static TARDISChameleonColumn buildTARDISChameleonColumn(COMPASS d, String[][] strings) {
        TARDISChameleonColumn tcc;
        BlockData[][] blockDataArr = getBlockDataFromArray(strings);
        if (d.equals(COMPASS.EAST)) {
            tcc = new TARDISChameleonColumn(blockDataArr);
        } else {
            tcc = new TARDISChameleonColumn(convertData(rotate2DArray(blockDataArr, d), d));
        }
        return tcc;
    }

    public static TARDISChameleonColumn buildTARDISChameleonColumn(COMPASS d, String json) {
        TARDISChameleonColumn tcc;
        BlockData[][] blockDataArr = getStringArrayFromJSON(json);
        if (d.equals(COMPASS.EAST)) {
            tcc = new TARDISChameleonColumn(blockDataArr);
        } else {
            tcc = new TARDISChameleonColumn(convertData(rotate2DArray(blockDataArr, d), d));
        }
        return tcc;
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

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of strings
     */
    private static BlockData[][] getStringArrayFromJSON(String js) {
        BlockData[][] preset = new BlockData[10][4];
        JsonArray json = JsonParser.parseString(js).getAsJsonArray();
        for (int col = 0; col < 10; col++) {
            JsonArray jsoncol = json.get(col).getAsJsonArray();
            for (int block = 0; block < 4; block++) {
                preset[col][block] = Bukkit.createBlockData(jsoncol.get(block).getAsString());
            }
        }
        return preset;
    }

    private static BlockData[][] rotate2DArray(BlockData[][] arr, COMPASS d) {
        BlockData[] zero = arr[0];
        BlockData[] one = arr[1];
        BlockData[] two = arr[2];
        BlockData[] three = arr[3];
        BlockData[] four = arr[4];
        BlockData[] five = arr[5];
        BlockData[] six = arr[6];
        BlockData[] seven = arr[7];
        switch (d) {
            case NORTH -> {
                arr[0] = two;
                arr[1] = three;
                arr[2] = four;
                arr[3] = five;
                arr[4] = six;
                arr[5] = seven;
                arr[6] = zero;
                arr[7] = one;
                return arr;
            }
            case WEST -> {
                arr[0] = four;
                arr[1] = five;
                arr[2] = six;
                arr[3] = seven;
                arr[4] = zero;
                arr[5] = one;
                arr[6] = two;
                arr[7] = three;
                return arr;
            }
            // SOUTH
            default -> {
                arr[0] = six;
                arr[1] = seven;
                arr[2] = zero;
                arr[3] = one;
                arr[4] = two;
                arr[5] = three;
                arr[6] = four;
                arr[7] = five;
                return arr;
            }
        }
    }

    private static BlockData[][] convertData(BlockData[][] data, COMPASS d) {
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                data[col][block] = TARDISBlockDataRotator.rotate(data[col][block], d);
//                if (data[col][block] instanceof Directional) {
//                    data[col][block] = new TARDISDirectionalRecalculator().recalculate(data[col][block], d);
//                } else if (data[col][block] instanceof Rail) {
//                    data[col][block] = new TARDISRailRecalculator().recalculate(data[col][block], d);
//                } else if (data[col][block] instanceof Rotatable) {
//                    data[col][block] = new TARDISRotatableRecalculator().recalculate(data[col][block], d);
//                } else if (data[col][block] instanceof MultipleFacing) {
//                    data[col][block] = new TARDISMultipleFacingRecalculator().recalculate(data[col][block], d);
//                }
            }
        }
        return data;
    }

    public void makePresets() {
        andesite.makePresets();
        angeld.makePresets();
        angelu.makePresets();
        aperture.makePresets();
        cake.makePresets();
        candy.makePresets();
        chalice.makePresets();
        chorus.makePresets();
        column.makePresets();
        creepy.makePresets();
        desert.makePresets();
        diorite.makePresets();
        duck.makePresets();
        factory.makePresets();
        fence.makePresets();
        flower.makePresets();
        gazebo.makePresets();
        granite.makePresets();
        gravestone.makePresets();
        helix.makePresets();
        invisible.makePresets();
        jail.makePresets();
        jungle.makePresets();
        junk.makePresets();
        lamp.makePresets();
        library.makePresets();
        lighthouse.makePresets();
        mine.makePresets();
        nether.makePresets();
        pandorica.makePresets();
        party.makePresets();
        peanut.makePresets();
        pine.makePresets();
        portal.makePresets();
        prismarine.makePresets();
        punked.makePresets();
        robot.makePresets();
        shroom.makePresets();
        snowman.makePresets();
        submerged.makePresets();
        swamp.makePresets();
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
        adaptive.makePresets();
        render.makePresets();
        extreme.makePresets();
        forest.makePresets();
        flats.makePresets();
        spikes.makePresets();
        mesa.makePresets();
        plains.makePresets();
        roofed.makePresets();
        savanna.makePresets();
        taiga.makePresets();
        cold.makePresets();
        boat.makePresets();
        cave.makePresets();
    }

    public TARDISChameleonColumn getColumn(ChameleonPreset p, COMPASS d) {
        switch (p) {
            case ANDESITE -> {
                return andesite.getBlueprint().get(d);
            }
            case ANGEL -> {
                if (r == 0) {
                    return angelu.getBlueprint().get(d);
                } else {
                    return angeld.getBlueprint().get(d);
                }
            }
            case APPERTURE -> {
                return aperture.getBlueprint().get(d);
            }
            case CAKE -> {
                return cake.getBlueprint().get(d);
            }
            case CANDY -> {
                return candy.getBlueprint().get(d);
            }
            case CHALICE -> {
                return chalice.getBlueprint().get(d);
            }
            case CHORUS -> {
                return chorus.getBlueprint().get(d);
            }
            case CREEPY -> {
                return creepy.getBlueprint().get(d);
            }
            case DESERT -> {
                return desert.getBlueprint().get(d);
            }
            case DIORITE -> {
                return diorite.getBlueprint().get(d);
            }
            case DUCK -> {
                return duck.getBlueprint().get(d);
            }
            case FACTORY -> {
                return factory.getBlueprint().get(d);
            }
            case FENCE -> {
                return fence.getBlueprint().get(d);
            }
            case FLOWER -> {
                return flower.getBlueprint().get(d);
            }
            case GAZEBO -> {
                return gazebo.getBlueprint().get(d);
            }
            case GRANITE -> {
                return granite.getBlueprint().get(d);
            }
            case GRAVESTONE -> {
                return gravestone.getBlueprint().get(d);
            }
            case HELIX -> {
                return helix.getBlueprint().get(d);
            }
            case INVISIBLE -> {
                return invisible.getBlueprint().get(d);
            }
            case JAIL -> {
                return jail.getBlueprint().get(d);
            }
            case JUNGLE -> {
                return jungle.getBlueprint().get(d);
            }
            case JUNK_MODE -> {
                return junk.getBlueprint().get(d);
            }
            case LAMP -> {
                return lamp.getBlueprint().get(d);
            }
            case LIBRARY -> {
                return library.getBlueprint().get(d);
            }
            case LIGHTHOUSE -> {
                return lighthouse.getBlueprint().get(d);
            }
            case MINESHAFT -> {
                return mine.getBlueprint().get(d);
            }
            case NETHER -> {
                return nether.getBlueprint().get(d);
            }
            case PANDORICA -> {
                return pandorica.getBlueprint().get(d);
            }
            case PARTY -> {
                return party.getBlueprint().get(d);
            }
            case PEANUT -> {
                return peanut.getBlueprint().get(d);
            }
            case PINE -> {
                return pine.getBlueprint().get(d);
            }
            case PORTAL -> {
                return portal.getBlueprint().get(d);
            }
            case PRISMARINE -> {
                return prismarine.getBlueprint().get(d);
            }
            case PUNKED -> {
                return punked.getBlueprint().get(d);
            }
            case RENDER -> {
                return render.getBlueprint().get(d);
            }
            case ROBOT -> {
                return robot.getBlueprint().get(d);
            }
            case SHROOM -> {
                return shroom.getBlueprint().get(d);
            }
            case SNOWMAN -> {
                return snowman.getBlueprint().get(d);
            }
            case STONE -> {
                return column.getBlueprint().get(d);
            }
            case SUBMERGED -> {
                return submerged.getBlueprint().get(d);
            }
            case SWAMP -> {
                return swamp.getBlueprint().get(d);
            }
            case TELEPHONE -> {
                return telephone.getBlueprint().get(d);
            }
            case THEEND -> {
                return theend.getBlueprint().get(d);
            }
            case TOILET -> {
                return toilet.getBlueprint().get(d);
            }
            case TOPSYTURVEY -> {
                return topsyturvey.getBlueprint().get(d);
            }
            case TORCH -> {
                return torch.getBlueprint().get(d);
            }
            case VILLAGE -> {
                return village.getBlueprint().get(d);
            }
            case WELL -> {
                return well.getBlueprint().get(d);
            }
            case WINDMILL -> {
                return windmill.getBlueprint().get(d);
            }
            case YELLOW -> {
                return yellow.getBlueprint().get(d);
            }
            case CUSTOM -> {
                return custom.getBlueprint().get(d);
            }
            case EXTREME_HILLS -> {
                return extreme.getBlueprint().get(d);
            }
            case FOREST -> {
                return forest.getBlueprint().get(d);
            }
            case ICE_FLATS -> {
                return flats.getBlueprint().get(d);
            }
            case ICE_SPIKES -> {
                return spikes.getBlueprint().get(d);
            }
            case MESA -> {
                return mesa.getBlueprint().get(d);
            }
            case PLAINS -> {
                return plains.getBlueprint().get(d);
            }
            case ROOFED_FOREST -> {
                return roofed.getBlueprint().get(d);
            }
            case SAVANNA -> {
                return savanna.getBlueprint().get(d);
            }
            case TAIGA -> {
                return taiga.getBlueprint().get(d);
            }
            case COLD_TAIGA -> {
                return cold.getBlueprint().get(d);
            }
            case BOAT -> {
                return boat.getBlueprint().get(d);
            }
            case CAVE -> {
                return cave.getBlueprint().get(d);
            }
            default -> {
                return adaptive.getBlueprint().get(d);
            }
        }
    }

    public TARDISChameleonColumn getGlass(ChameleonPreset p, COMPASS d) {
        switch (p) {
            case ANDESITE -> {
                return andesite.getGlass().get(d);
            }
            case ANGEL -> {
                if (r == 0) {
                    return angelu.getGlass().get(d);
                } else {
                    return angeld.getGlass().get(d);
                }
            }
            case APPERTURE -> {
                return aperture.getGlass().get(d);
            }
            case CAKE -> {
                return cake.getGlass().get(d);
            }
            case CANDY -> {
                return candy.getGlass().get(d);
            }
            case CHALICE -> {
                return chalice.getGlass().get(d);
            }
            case CHORUS -> {
                return chorus.getGlass().get(d);
            }
            case CREEPY -> {
                return creepy.getGlass().get(d);
            }
            case DESERT -> {
                return desert.getGlass().get(d);
            }
            case DIORITE -> {
                return diorite.getGlass().get(d);
            }
            case DUCK -> {
                return duck.getGlass().get(d);
            }
            case FACTORY -> {
                return factory.getGlass().get(d);
            }
            case FENCE -> {
                return fence.getGlass().get(d);
            }
            case FLOWER -> {
                return flower.getGlass().get(d);
            }
            case GAZEBO -> {
                return gazebo.getGlass().get(d);
            }
            case GRANITE -> {
                return granite.getGlass().get(d);
            }
            case GRAVESTONE -> {
                return gravestone.getGlass().get(d);
            }
            case HELIX -> {
                return helix.getGlass().get(d);
            }
            case INVISIBLE -> {
                return invisible.getGlass().get(d);
            }
            case JAIL -> {
                return jail.getGlass().get(d);
            }
            case JUNGLE -> {
                return jungle.getGlass().get(d);
            }
            case JUNK_MODE -> {
                return junk.getGlass().get(d);
            }
            case LAMP -> {
                return lamp.getGlass().get(d);
            }
            case LIBRARY -> {
                return library.getGlass().get(d);
            }
            case LIGHTHOUSE -> {
                return lighthouse.getGlass().get(d);
            }
            case MINESHAFT -> {
                return mine.getGlass().get(d);
            }
            case NETHER -> {
                return nether.getGlass().get(d);
            }
            case PANDORICA -> {
                return pandorica.getGlass().get(d);
            }
            case PARTY -> {
                return party.getGlass().get(d);
            }
            case PEANUT -> {
                return peanut.getGlass().get(d);
            }
            case PINE -> {
                return pine.getGlass().get(d);
            }
            case PORTAL -> {
                return portal.getGlass().get(d);
            }
            case PRISMARINE -> {
                return prismarine.getGlass().get(d);
            }
            case PUNKED -> {
                return punked.getGlass().get(d);
            }
            case RENDER -> {
                return render.getGlass().get(d);
            }
            case ROBOT -> {
                return robot.getGlass().get(d);
            }
            case SHROOM -> {
                return shroom.getGlass().get(d);
            }
            case SNOWMAN -> {
                return snowman.getGlass().get(d);
            }
            case STONE -> {
                return column.getGlass().get(d);
            }
            case SUBMERGED -> {
                return submerged.getGlass().get(d);
            }
            case SWAMP -> {
                return swamp.getGlass().get(d);
            }
            case TELEPHONE -> {
                return telephone.getGlass().get(d);
            }
            case THEEND -> {
                return theend.getGlass().get(d);
            }
            case TOILET -> {
                return toilet.getGlass().get(d);
            }
            case TOPSYTURVEY -> {
                return topsyturvey.getGlass().get(d);
            }
            case TORCH -> {
                return torch.getGlass().get(d);
            }
            case VILLAGE -> {
                return village.getGlass().get(d);
            }
            case WELL -> {
                return well.getGlass().get(d);
            }
            case WINDMILL -> {
                return windmill.getGlass().get(d);
            }
            case YELLOW -> {
                return yellow.getGlass().get(d);
            }
            case CUSTOM -> {
                return custom.getGlass().get(d);
            }
            case EXTREME_HILLS -> {
                return extreme.getGlass().get(d);
            }
            case FOREST -> {
                return forest.getGlass().get(d);
            }
            case ICE_FLATS -> {
                return flats.getGlass().get(d);
            }
            case ICE_SPIKES -> {
                return spikes.getGlass().get(d);
            }
            case MESA -> {
                return mesa.getGlass().get(d);
            }
            case PLAINS -> {
                return plains.getGlass().get(d);
            }
            case ROOFED_FOREST -> {
                return roofed.getGlass().get(d);
            }
            case SAVANNA -> {
                return savanna.getGlass().get(d);
            }
            case TAIGA -> {
                return taiga.getGlass().get(d);
            }
            case COLD_TAIGA -> {
                return cold.getGlass().get(d);
            }
            case BOAT -> {
                return boat.getGlass().get(d);
            }
            case CAVE -> {
                return cave.getGlass().get(d);
            }
            default -> {
                return adaptive.getGlass().get(d);
            }
        }
    }

    public TARDISChameleonColumn getStained(ChameleonPreset p, COMPASS d) {
        switch (p) {
            case ANDESITE -> {
                return andesite.getStained().get(d);
            }
            case ANGEL -> {
                if (r == 0) {
                    return angelu.getStained().get(d);
                } else {
                    return angeld.getStained().get(d);
                }
            }
            case APPERTURE -> {
                return aperture.getStained().get(d);
            }
            case CAKE -> {
                return cake.getStained().get(d);
            }
            case CANDY -> {
                return candy.getStained().get(d);
            }
            case CHALICE -> {
                return chalice.getStained().get(d);
            }
            case CHORUS -> {
                return chorus.getStained().get(d);
            }
            case CREEPY -> {
                return creepy.getStained().get(d);
            }
            case DESERT -> {
                return desert.getStained().get(d);
            }
            case DIORITE -> {
                return diorite.getStained().get(d);
            }
            case DUCK -> {
                return duck.getStained().get(d);
            }
            case FACTORY -> {
                return factory.getStained().get(d);
            }
            case FENCE -> {
                return fence.getStained().get(d);
            }
            case FLOWER -> {
                return flower.getStained().get(d);
            }
            case GAZEBO -> {
                return gazebo.getStained().get(d);
            }
            case GRANITE -> {
                return granite.getStained().get(d);
            }
            case GRAVESTONE -> {
                return gravestone.getStained().get(d);
            }
            case HELIX -> {
                return helix.getStained().get(d);
            }
            case INVISIBLE -> {
                return invisible.getStained().get(d);
            }
            case JAIL -> {
                return jail.getStained().get(d);
            }
            case JUNGLE -> {
                return jungle.getStained().get(d);
            }
            case JUNK_MODE -> {
                return junk.getStained().get(d);
            }
            case LAMP -> {
                return lamp.getStained().get(d);
            }
            case LIBRARY -> {
                return library.getStained().get(d);
            }
            case LIGHTHOUSE -> {
                return lighthouse.getStained().get(d);
            }
            case MINESHAFT -> {
                return mine.getStained().get(d);
            }
            case NETHER -> {
                return nether.getStained().get(d);
            }
            case PANDORICA -> {
                return pandorica.getStained().get(d);
            }
            case PARTY -> {
                return party.getStained().get(d);
            }
            case PEANUT -> {
                return peanut.getStained().get(d);
            }
            case PINE -> {
                return pine.getStained().get(d);
            }
            case PORTAL -> {
                return portal.getStained().get(d);
            }
            case PRISMARINE -> {
                return prismarine.getStained().get(d);
            }
            case PUNKED -> {
                return punked.getStained().get(d);
            }
            case RENDER -> {
                return render.getStained().get(d);
            }
            case ROBOT -> {
                return robot.getStained().get(d);
            }
            case SHROOM -> {
                return shroom.getStained().get(d);
            }
            case SNOWMAN -> {
                return snowman.getStained().get(d);
            }
            case STONE -> {
                return column.getStained().get(d);
            }
            case SUBMERGED -> {
                return submerged.getStained().get(d);
            }
            case SWAMP -> {
                return swamp.getStained().get(d);
            }
            case TELEPHONE -> {
                return telephone.getStained().get(d);
            }
            case THEEND -> {
                return theend.getStained().get(d);
            }
            case TOILET -> {
                return toilet.getStained().get(d);
            }
            case TOPSYTURVEY -> {
                return topsyturvey.getStained().get(d);
            }
            case TORCH -> {
                return torch.getStained().get(d);
            }
            case VILLAGE -> {
                return village.getStained().get(d);
            }
            case WELL -> {
                return well.getStained().get(d);
            }
            case WINDMILL -> {
                return windmill.getStained().get(d);
            }
            case YELLOW -> {
                return yellow.getStained().get(d);
            }
            case CUSTOM -> {
                return custom.getStained().get(d);
            }
            case EXTREME_HILLS -> {
                return extreme.getStained().get(d);
            }
            case FOREST -> {
                return forest.getStained().get(d);
            }
            case ICE_FLATS -> {
                return flats.getStained().get(d);
            }
            case ICE_SPIKES -> {
                return spikes.getStained().get(d);
            }
            case MESA -> {
                return mesa.getStained().get(d);
            }
            case PLAINS -> {
                return plains.getStained().get(d);
            }
            case ROOFED_FOREST -> {
                return roofed.getStained().get(d);
            }
            case SAVANNA -> {
                return savanna.getStained().get(d);
            }
            case TAIGA -> {
                return taiga.getStained().get(d);
            }
            case COLD_TAIGA -> {
                return cold.getStained().get(d);
            }
            case BOAT -> {
                return boat.getStained().get(d);
            }
            case CAVE -> {
                return cave.getStained().get(d);
            }
            default -> {
                return adaptive.getStained().get(d);
            }
        }
    }

    public void setR(int r) {
        this.r = r;
    }
}
