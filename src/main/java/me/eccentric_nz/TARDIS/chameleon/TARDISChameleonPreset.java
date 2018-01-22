/*
 * Copyright (C) 2016 eccentric_nz
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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rail;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPreset {

    private static final List<Material> PROBLEM_BLOCKS = Arrays.asList(Material.WALL_TORCH, Material.OAK_STAIRS, Material.SIGN, Material.OAK_DOOR, Material.RAIL, Material.COBBLESTONE_STAIRS, Material.WALL_SIGN, Material.LEVER, Material.IRON_DOOR, Material.IRON_TRAPDOOR, Material.STONE_BUTTON, Material.JACK_O_LANTERN, Material.OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.BROWN_MUSHROOM_BLOCK, Material.VINE, Material.BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.SANDSTONE_STAIRS, Material.SPRUCE_STAIRS, Material.JUNGLE_STAIRS, Material.ACACIA_STAIRS, Material.BIRCH_STAIRS, Material.DARK_OAK_STAIRS, Material.OAK_BUTTON, Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL, Material.QUARTZ_STAIRS, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.ACACIA_DOOR, Material.JUNGLE_DOOR, Material.DARK_OAK_DOOR, Material.PURPUR_STAIRS, Material.RED_SANDSTONE_STAIRS);
    public final TARDISAndesitePreset andesite;
    public final TARDISAngelDownPreset angeld;
    public final TARDISAngelUpPreset angelu;
    public final TARDISAppertureSciencePreset apperture;
    public final TARDISCakePreset cake;
    public final TARDISCandyCanePreset candy;
    public final TARDISChalicePreset chalice;
    public final TARDISChorusPreset chorus;
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
    public final TARDISJunkPreset junk;
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
    // biome adaptive presets
    public final TARDISRenderPreset render;
    public final TARDISExtremeHillsPreset extreme;
    public final TARDISForestPreset forest;
    public final TARDISIcePlainsPreset flats;
    public final TARDISIcePlainsSpikesPreset spikes;
    public final TARDISMesaPreset mesa;
    public final TARDISPlainsPreset plains;
    public final TARDISRoofedForestPreset roofed;
    public final TARDISSavannaPreset savanna;
    public final TARDISTaigaPreset taiga;
    public final TARDISColdTaigaPreset cold;
    public final TARDISBoatPreset boat;
    int r;

    public TARDISChameleonPreset() {
        this.andesite = new TARDISAndesitePreset();
        this.angeld = new TARDISAngelDownPreset();
        this.angelu = new TARDISAngelUpPreset();
        this.apperture = new TARDISAppertureSciencePreset();
        this.cake = new TARDISCakePreset();
        this.candy = new TARDISCandyCanePreset();
        this.chalice = new TARDISChalicePreset();
        this.chorus = new TARDISChorusPreset();
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
        this.junk = new TARDISJunkPreset();
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
        this.extreme = new TARDISExtremeHillsPreset();
        this.forest = new TARDISForestPreset();
        this.flats = new TARDISIcePlainsPreset();
        this.spikes = new TARDISIcePlainsSpikesPreset();
        this.mesa = new TARDISMesaPreset();
        this.plains = new TARDISPlainsPreset();
        this.roofed = new TARDISRoofedForestPreset();
        this.savanna = new TARDISSavannaPreset();
        this.taiga = new TARDISTaigaPreset();
        this.cold = new TARDISColdTaigaPreset();
        this.boat = new TARDISBoatPreset();
    }

    public void makePresets() {
        andesite.makePresets(false, false);
        angeld.makePresets(true, false);
        angelu.makePresets(true, false);
        apperture.makePresets(false, false);
        cake.makePresets(false, false);
        candy.makePresets(true, false);
        chalice.makePresets(false, false);
        chorus.makePresets(false, false);
        column.makePresets(false, false);
        creepy.makePresets(false, false);
        desert.makePresets(false, false);
        diorite.makePresets(false, false);
        duck.makePresets(true, true);
        factory.makePresets(false, false);
        fence.makePresets(true, false);
        flower.makePresets(false, false);
        gazebo.makePresets(false, false);
        granite.makePresets(false, false);
        gravestone.makePresets(true, false);
        helix.makePresets(false, false);
        invisible.makePresets(true, true);
        jail.makePresets(false, false);
        jungle.makePresets(false, false);
        junk.makePresets(true, false);
        lamp.makePresets(true, false);
        library.makePresets(false, false);
        lighthouse.makePresets(false, false);
        mine.makePresets(false, false);
        nether.makePresets(false, false);
        pandorica.makePresets(false, false);
        party.makePresets(false, false);
        peanut.makePresets(false, false);
        pine.makePresets(false, false);
        police.makePresets(false, false);
        portal.makePresets(false, false);
        prismarine.makePresets(false, false);
        punked.makePresets(false, false);
        robot.makePresets(true, false);
        shroom.makePresets(false, false);
        snowman.makePresets(true, false);
        submerged.makePresets(true, false);
        swamp.makePresets(false, false);
        taller.makePresets(false, false);
        telephone.makePresets(false, false);
        theend.makePresets(false, false);
        toilet.makePresets(true, false);
        topsyturvey.makePresets(false, false);
        torch.makePresets(true, false);
        village.makePresets(false, false);
        well.makePresets(false, false);
        windmill.makePresets(true, false);
        yellow.makePresets(false, false);
        custom.makePresets();
        render.makePresets(false, false);
        extreme.makePresets(false, false);
        forest.makePresets(true, false);
        flats.makePresets(true, false);
        spikes.makePresets(false, false);
        mesa.makePresets(true, false);
        plains.makePresets(false, false);
        roofed.makePresets(false, false);
        savanna.makePresets(true, false);
        taiga.makePresets(false, false);
        cold.makePresets(false, false);
        boat.makePresets(true, false);
    }

    public static TARDISChameleonColumn buildTARDISChameleonColumn(COMPASS d, String json, boolean asymmetric, boolean duck) {
        TARDISChameleonColumn tcc;
        BlockData[][] blockDataArr = getStringArrayFromJSON(json);
        if (d.equals(COMPASS.EAST)) {
            tcc = new TARDISChameleonColumn(blockDataArr);
        } else {
            tcc = new TARDISChameleonColumn(convertData(rotate2DArray(blockDataArr, d, asymmetric), d, duck));
        }
        return tcc;
    }

    /**
     * Converts a JSON data string to a 2D array.
     *
     * @param js the JSON string
     * @return a 2D array of strings
     */
    private static BlockData[][] getStringArrayFromJSON(String js) {
        BlockData[][] preset = new BlockData[10][4];
        JSONArray json = new JSONArray(js);
        for (int col = 0; col < 10; col++) {
            JSONArray jsoncol = json.getJSONArray(col);
            for (int block = 0; block < 4; block++) {
                preset[col][block] = Bukkit.createBlockData(jsoncol.getString(block));
            }
        }
        return preset;
    }

    private static BlockData[][] rotate2DArray(BlockData[][] arr, COMPASS d, boolean assymetric) {
        switch (d) {
            case NORTH:
                BlockData[] zero_s = arr[0];
                BlockData[] one_s = arr[1];
                BlockData[] two_s = arr[2];
                BlockData[] three_s = arr[3];
                BlockData[] four_s = arr[4];
                BlockData[] five_s = arr[5];
                BlockData[] six_s = arr[6];
                BlockData[] seven_s = arr[7];
                arr[0] = two_s;
                arr[1] = three_s;
                arr[2] = four_s;
                arr[3] = five_s;
                arr[4] = six_s;
                arr[5] = seven_s;
                arr[6] = zero_s;
                arr[7] = one_s;
                return arr;
            case WEST:
                if (assymetric) {
                    BlockData[] zero_w = arr[0];
                    BlockData[] one_w = arr[1];
                    BlockData[] two_w = arr[2];
                    BlockData[] four_w = arr[4];
                    BlockData[] five_w = arr[5];
                    BlockData[] six_w = arr[6];
                    arr[1] = five_w;
                    arr[2] = zero_w;
                    arr[0] = two_w;
                    arr[4] = six_w;
                    arr[5] = one_w;
                    arr[6] = four_w;
                }
                BlockData[] three_w = arr[3];
                arr[3] = arr[7];
                arr[7] = three_w;
                return arr;
            default:
                BlockData[] zero_n = arr[0];
                BlockData[] one_n = arr[1];
                BlockData[] two_n = arr[2];
                BlockData[] three_n = arr[3];
                BlockData[] four_n = arr[4];
                BlockData[] five_n = arr[5];
                BlockData[] six_n = arr[6];
                BlockData[] seven_n = arr[7];
                arr[0] = six_n;
                arr[1] = seven_n;
                arr[2] = zero_n;
                arr[3] = one_n;
                arr[4] = two_n;
                arr[5] = three_n;
                arr[6] = four_n;
                arr[7] = five_n;
                return arr;
        }
    }

    private static BlockData[][] convertData(BlockData[][] data, COMPASS d, boolean duck) {
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                Material mat = data[col][block].getMaterial();
                if (PROBLEM_BLOCKS.contains(mat)) {
                    switch (mat) {
                        case WALL_TORCH:
                            data[col][block] = new TARDISTorchRecalculator().recalculate(data[col][block], d);
                            break;
                        case IRON_DOOR:
                        case OAK_DOOR:
                        case BIRCH_DOOR:
                        case SPRUCE_DOOR:
                        case JUNGLE_DOOR:
                        case ACACIA_DOOR:
                        case DARK_OAK_DOOR:
                            Bisected bisected = (Bisected) data[col][block];
                            if (bisected.getHalf().equals(Half.BOTTOM)) {
                                // data is either a 0 or a 2
                                Directional door = (Directional) data[col][block];
                                switch (d) {
                                    case SOUTH:
                                        if (door.getFacing().equals(BlockFace.EAST)) {
                                            door.setFacing(BlockFace.SOUTH);
                                        } else {
                                            door.setFacing(BlockFace.NORTH);
                                        }
                                        break;
                                    case WEST:
                                        if (door.getFacing().equals(BlockFace.EAST)) {
                                            door.setFacing(BlockFace.WEST);
                                        } else {
                                            door.setFacing(BlockFace.EAST);
                                        }
                                        break;
                                    default:
                                        if (door.getFacing().equals(BlockFace.EAST)) {
                                            door.setFacing(BlockFace.NORTH);
                                        } else {
                                            door.setFacing(BlockFace.SOUTH);
                                        }
                                        break;
                                }
                            }
                            break;
                        case RAIL:
                            Rail rail = (Rail) data[col][block];
                            switch (d) {
                                case WEST:
                                    rail.setShape(Rail.Shape.EAST_WEST);
                                    break;
                                default:
                                    rail.setShape(Rail.Shape.NORTH_SOUTH);
                                    break;
                            }
                            data[col][block] = rail;
                            break;
                        case LEVER:
                            data[col][block] = new TARDISLeverRecalculator().recalculate(data[col][block], d);
                            break;
                        case SIGN:
                        case WALL_SIGN:
                            Directional sign = (Directional) data[col][block];
                            switch (d) {
                                case SOUTH:
                                    sign.setFacing(BlockFace.NORTH);
                                    break;
                                case WEST:
                                    sign.setFacing(BlockFace.EAST);
                                    break;
                                default:
                                    sign.setFacing(BlockFace.SOUTH);
                                    break;
                            }
                            data[col][block] = sign;
                            break;
                        case OAK_STAIRS:
                        case COBBLESTONE_STAIRS:
                        case BRICK_STAIRS:
                        case STONE_BRICK_STAIRS:
                        case NETHER_BRICK_STAIRS:
                        case SANDSTONE_STAIRS:
                        case SPRUCE_STAIRS:
                        case BIRCH_STAIRS:
                        case JUNGLE_STAIRS:
                        case QUARTZ_STAIRS:
                        case ACACIA_STAIRS:
                        case DARK_OAK_STAIRS:
                        case PURPUR_STAIRS:
                        case RED_SANDSTONE_STAIRS:
                            data[col][block] = new TARDISStairRecalculator().recalculate(data[col][block], d, col, duck);
                            break;
                        case STONE_BUTTON:
                        case OAK_BUTTON:
                        case SPRUCE_BUTTON:
                        case BIRCH_BUTTON:
                        case JUNGLE_BUTTON:
                        case ACACIA_BUTTON:
                        case DARK_OAK_BUTTON:
                            data[col][block] = new TARDISButtonRecalculator().recalculate(data[col][block], d);
                            break;
                        case OAK_TRAPDOOR:
                        case BIRCH_TRAPDOOR:
                        case SPRUCE_TRAPDOOR:
                        case JUNGLE_TRAPDOOR:
                        case ACACIA_TRAPDOOR:
                        case DARK_OAK_TRAPDOOR:
                        case IRON_TRAPDOOR:
                            data[col][block] = new TARDISTrapdoorRecalculator().recalculate(data[col][block], d);
                            break;
                        case BROWN_MUSHROOM_BLOCK: // mushroom
                            data[col][block] = new TARDISMushroomRecalculator().recalculate(data[col][block], d, col);
                            break;
                        case ANVIL:
                        case CHIPPED_ANVIL:
                        case DAMAGED_ANVIL:
                            Directional anvil = (Directional) data[col][block];
                            switch (d) {
                                case WEST:
                                    break;
                                default:
                                    switch (anvil.getFacing()) {
                                        case SOUTH:
                                            anvil.setFacing(BlockFace.WEST);
                                            break;
                                        default:
                                            anvil.setFacing(BlockFace.EAST);
                                    }
                            }
                            data[col][block] = anvil;
                            break;
                        case JACK_O_LANTERN:
                            Directional jack = (Directional) data[col][block];
                            switch (d) {
                                case SOUTH:
                                    jack.setFacing(BlockFace.NORTH);
                                    break;
                                case WEST:
                                    jack.setFacing(BlockFace.EAST);
                                    break;
                                default:
                                    jack.setFacing(BlockFace.SOUTH);
                                    break;
                            }
                            data[col][block] = jack;
                            break;
                        default: // vine
                            Directional vine = (Directional) data[col][block];
                            switch (d) {
                                case SOUTH:
                                    vine.setFacing(BlockFace.SOUTH);
                                    break;
                                case WEST:
                                    vine.setFacing(BlockFace.WEST);
                                    break;
                                default:
                                    vine.setFacing(BlockFace.NORTH);
                                    break;
                            }
                            data[col][block] = vine;
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
            case CHORUS:
                return chorus.getBlueprint().get(d);
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
            case JUNK_MODE:
                return junk.getBlueprint().get(d);
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
            case EXTREME_HILLS:
                return extreme.getBlueprint().get(d);
            case FOREST:
                return forest.getBlueprint().get(d);
            case ICE_FLATS:
                return flats.getBlueprint().get(d);
            case ICE_SPIKES:
                return spikes.getBlueprint().get(d);
            case MESA:
                return mesa.getBlueprint().get(d);
            case PLAINS:
                return plains.getBlueprint().get(d);
            case ROOFED_FOREST:
                return roofed.getBlueprint().get(d);
            case SAVANNA:
                return savanna.getBlueprint().get(d);
            case TAIGA:
                return taiga.getBlueprint().get(d);
            case COLD_TAIGA:
                return cold.getBlueprint().get(d);
            case BOAT:
                return boat.getBlueprint().get(d);
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
            case CHORUS:
                return chorus.getGlass().get(d);
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
            case JUNK_MODE:
                return junk.getGlass().get(d);
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
            case EXTREME_HILLS:
                return extreme.getGlass().get(d);
            case FOREST:
                return forest.getGlass().get(d);
            case ICE_FLATS:
                return flats.getGlass().get(d);
            case ICE_SPIKES:
                return spikes.getGlass().get(d);
            case MESA:
                return mesa.getGlass().get(d);
            case PLAINS:
                return plains.getGlass().get(d);
            case ROOFED_FOREST:
                return roofed.getGlass().get(d);
            case SAVANNA:
                return savanna.getGlass().get(d);
            case TAIGA:
                return taiga.getGlass().get(d);
            case COLD_TAIGA:
                return cold.getGlass().get(d);
            case BOAT:
                return boat.getGlass().get(d);
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
            case CHORUS:
                return chorus.getStained().get(d);
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
            case JUNK_MODE:
                return junk.getStained().get(d);
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
            case EXTREME_HILLS:
                return extreme.getStained().get(d);
            case FOREST:
                return forest.getStained().get(d);
            case ICE_FLATS:
                return flats.getStained().get(d);
            case ICE_SPIKES:
                return spikes.getStained().get(d);
            case MESA:
                return mesa.getStained().get(d);
            case PLAINS:
                return plains.getStained().get(d);
            case ROOFED_FOREST:
                return roofed.getStained().get(d);
            case SAVANNA:
                return savanna.getStained().get(d);
            case TAIGA:
                return taiga.getStained().get(d);
            case COLD_TAIGA:
                return cold.getStained().get(d);
            case BOAT:
                return boat.getStained().get(d);
            default:
                return taller.getStained().get(d);
        }
    }

    public void setR(int r) {
        this.r = r;
    }
}
