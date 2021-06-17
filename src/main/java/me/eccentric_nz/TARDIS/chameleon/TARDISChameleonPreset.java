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
package me.eccentric_nz.TARDIS.chameleon;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.recalculators.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;

import java.util.Arrays;
import java.util.List;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonPreset {

    private static final List<Material> PROBLEM_BLOCKS = Arrays.asList(Material.ACACIA_DOOR, Material.ACACIA_FENCE, Material.ACACIA_SIGN, Material.ACACIA_STAIRS, Material.ACACIA_TRAPDOOR, Material.ACACIA_WALL_SIGN, Material.ANDESITE_STAIRS, Material.ANVIL, Material.BIRCH_DOOR, Material.BIRCH_FENCE, Material.BIRCH_SIGN, Material.BIRCH_STAIRS, Material.BIRCH_TRAPDOOR, Material.BIRCH_WALL_SIGN, Material.BLACK_BED, Material.BLACK_GLAZED_TERRACOTTA, Material.BLACKSTONE_STAIRS, Material.BLUE_BED, Material.BLUE_GLAZED_TERRACOTTA, Material.BRICK_STAIRS, Material.BROWN_BED, Material.BROWN_GLAZED_TERRACOTTA, Material.BROWN_MUSHROOM_BLOCK, Material.CARVED_PUMPKIN, Material.CHIPPED_ANVIL, Material.COBBLESTONE_STAIRS, Material.CRIMSON_BUTTON, Material.CRIMSON_DOOR, Material.CRIMSON_FENCE, Material.CRIMSON_SIGN, Material.CRIMSON_STAIRS, Material.CRIMSON_TRAPDOOR, Material.CRIMSON_WALL_SIGN, Material.CYAN_BED, Material.CYAN_GLAZED_TERRACOTTA, Material.DAMAGED_ANVIL, Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE, Material.DARK_OAK_SIGN, Material.DARK_OAK_STAIRS, Material.DARK_OAK_TRAPDOOR, Material.DARK_OAK_WALL_SIGN, Material.DARK_PRISMARINE_STAIRS, Material.DIORITE_STAIRS, Material.END_STONE_BRICK_STAIRS, Material.GRANITE_STAIRS, Material.GRAY_BED, Material.GRAY_GLAZED_TERRACOTTA, Material.GREEN_BED, Material.GREEN_GLAZED_TERRACOTTA, Material.IRON_DOOR, Material.IRON_TRAPDOOR, Material.JACK_O_LANTERN, Material.JUNGLE_DOOR, Material.JUNGLE_FENCE, Material.JUNGLE_SIGN, Material.JUNGLE_STAIRS, Material.JUNGLE_TRAPDOOR, Material.JUNGLE_WALL_SIGN, Material.LEVER, Material.LIGHT_BLUE_BED, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_BED, Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.LIME_BED, Material.LIME_GLAZED_TERRACOTTA, Material.MAGENTA_BED, Material.MAGENTA_GLAZED_TERRACOTTA, Material.MOSSY_COBBLESTONE_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.NETHER_BRICK_FENCE, Material.NETHER_BRICK_STAIRS, Material.OAK_BUTTON, Material.OAK_DOOR, Material.OAK_FENCE, Material.OAK_SIGN, Material.OAK_STAIRS, Material.OAK_TRAPDOOR, Material.OAK_WALL_SIGN, Material.OBSERVER, Material.ORANGE_BED, Material.ORANGE_GLAZED_TERRACOTTA, Material.PINK_BED, Material.PINK_GLAZED_TERRACOTTA, Material.POLISHED_ANDESITE_STAIRS, Material.POLISHED_BLACKSTONE_BUTTON, Material.POLISHED_BLACKSTONE_BRICK_STAIRS, Material.POLISHED_BLACKSTONE_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_GRANITE_STAIRS, Material.PRISMARINE_BRICK_STAIRS, Material.PRISMARINE_STAIRS, Material.PURPLE_BED, Material.PURPLE_GLAZED_TERRACOTTA, Material.PURPUR_STAIRS, Material.QUARTZ_STAIRS, Material.RAIL, Material.RED_BED, Material.RED_GLAZED_TERRACOTTA, Material.RED_NETHER_BRICK_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS, Material.SPRUCE_DOOR, Material.SPRUCE_FENCE, Material.SPRUCE_SIGN, Material.SPRUCE_STAIRS, Material.SPRUCE_TRAPDOOR, Material.SPRUCE_WALL_SIGN, Material.STONE_BRICK_STAIRS, Material.STONE_BUTTON, Material.STONE_STAIRS, Material.VINE, Material.WALL_TORCH, Material.REDSTONE_WALL_TORCH, Material.SOUL_WALL_TORCH, Material.WARPED_BUTTON, Material.WARPED_DOOR, Material.WARPED_FENCE, Material.WARPED_SIGN, Material.WARPED_STAIRS, Material.WARPED_TRAPDOOR, Material.WARPED_WALL_SIGN, Material.WHITE_BED, Material.WHITE_GLAZED_TERRACOTTA, Material.YELLOW_BED, Material.YELLOW_GLAZED_TERRACOTTA);
    public final TARDISCustomPreset custom;
    private final TARDISAndesitePreset andesite;
    private final TARDISAngelDownPreset angeld;
    private final TARDISAngelUpPreset angelu;
    private final TARDISAppertureSciencePreset apperture;
    private final TARDISCakePreset cake;
    private final TARDISCandyCanePreset candy;
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
        apperture = new TARDISAppertureSciencePreset();
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
    }

    static TARDISChameleonColumn buildTARDISChameleonColumn(COMPASS d, String[][] strings, boolean asymmetric, boolean duck) {
        TARDISChameleonColumn tcc;
        BlockData[][] blockDataArr = getBlockDataFromArray(strings);
        if (d.equals(COMPASS.EAST)) {
            tcc = new TARDISChameleonColumn(blockDataArr);
        } else {
            tcc = new TARDISChameleonColumn(convertData(rotate2DArray(blockDataArr, d, asymmetric), d, duck));
        }
        return tcc;
    }

    static TARDISChameleonColumn buildTARDISChameleonColumn(COMPASS d, String json, boolean asymmetric, boolean duck) {
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
        JsonArray json = new JsonParser().parse(js).getAsJsonArray();
        for (int col = 0; col < 10; col++) {
            JsonArray jsoncol = json.get(col).getAsJsonArray();
            for (int block = 0; block < 4; block++) {
                preset[col][block] = Bukkit.createBlockData(jsoncol.get(block).getAsString());
            }
        }
        return preset;
    }

    private static BlockData[][] rotate2DArray(BlockData[][] arr, COMPASS d, boolean assymetric) {
        switch (d) {
            case NORTH -> {
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
            }
            case WEST -> {
                if (assymetric) {
                    BlockData[] zero_w = arr[0];
                    BlockData[] one_w = arr[1];
                    BlockData[] two_w = arr[2];
                    BlockData[] four_w = arr[4];
                    BlockData[] five_w = arr[5];
                    BlockData[] six_w = arr[6];
                    arr[0] = four_w;
                    arr[1] = five_w;
                    arr[2] = six_w;
                    arr[4] = zero_w;
                    arr[5] = one_w;
                    arr[6] = two_w;
                }
                BlockData[] three_w = arr[3];
                arr[3] = arr[7];
                arr[7] = three_w;
                return arr;
            }
            default -> {
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
    }

    private static BlockData[][] convertData(BlockData[][] data, COMPASS d, boolean duck) {
        for (int col = 0; col < 10; col++) {
            for (int block = 0; block < 4; block++) {
                Material mat = data[col][block].getMaterial();
                if (PROBLEM_BLOCKS.contains(mat)) {
                    switch (mat) {
                        case BLACK_BED, BLUE_BED, BROWN_BED, CYAN_BED, GRAY_BED, GREEN_BED, LIGHT_BLUE_BED, LIGHT_GRAY_BED, LIME_BED, MAGENTA_BED, ORANGE_BED, PINK_BED, PURPLE_BED, RED_BED, WHITE_BED, YELLOW_BED -> data[col][block] = new TARDISBedRecalculator().recalculate(data[col][block], d);
                        case WALL_TORCH, REDSTONE_WALL_TORCH, SOUL_WALL_TORCH -> data[col][block] = new TARDISTorchRecalculator().recalculate(data[col][block], d);
                        case ACACIA_DOOR, BIRCH_DOOR, CRIMSON_DOOR, DARK_OAK_DOOR, IRON_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, WARPED_DOOR -> {
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
                            data[col][block] = door;
                        }
                        case RAIL -> {
                            Rail rail = (Rail) data[col][block];
                            if (d == COMPASS.WEST) {
                                rail.setShape(Rail.Shape.EAST_WEST);
                            } else {
                                rail.setShape(Rail.Shape.NORTH_SOUTH);
                            }
                            data[col][block] = rail;
                        }
                        case LEVER -> data[col][block] = new TARDISLeverRecalculator().recalculate(data[col][block], d);
                        case ACACIA_SIGN, BIRCH_SIGN, CRIMSON_SIGN, DARK_OAK_SIGN, JUNGLE_SIGN, OAK_SIGN, SPRUCE_SIGN, WARPED_SIGN -> {
                            Rotatable sign = (Rotatable) data[col][block];
                            switch (d) {
                                case SOUTH:
                                    sign.setRotation(BlockFace.NORTH);
                                    break;
                                case WEST:
                                    sign.setRotation(BlockFace.EAST);
                                    break;
                                default:
                                    sign.setRotation(BlockFace.SOUTH);
                                    break;
                            }
                            data[col][block] = sign;
                        }
                        case ACACIA_WALL_SIGN, BIRCH_WALL_SIGN, CRIMSON_WALL_SIGN, DARK_OAK_WALL_SIGN, JUNGLE_WALL_SIGN, OAK_WALL_SIGN, SPRUCE_WALL_SIGN, WARPED_WALL_SIGN -> {
                            Directional wall_sign = (Directional) data[col][block];
                            switch (d) {
                                case SOUTH:
                                    wall_sign.setFacing(BlockFace.NORTH);
                                    break;
                                case WEST:
                                    wall_sign.setFacing(BlockFace.EAST);
                                    break;
                                default:
                                    wall_sign.setFacing(BlockFace.SOUTH);
                                    break;
                            }
                            data[col][block] = wall_sign;
                        }
                        case ACACIA_STAIRS, ANDESITE_STAIRS, BIRCH_STAIRS, BLACKSTONE_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS, CRIMSON_STAIRS, DARK_OAK_STAIRS, DARK_PRISMARINE_STAIRS, DIORITE_STAIRS, END_STONE_BRICK_STAIRS, GRANITE_STAIRS, JUNGLE_STAIRS, MOSSY_COBBLESTONE_STAIRS, MOSSY_STONE_BRICK_STAIRS, NETHER_BRICK_STAIRS, OAK_STAIRS, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_STAIRS, POLISHED_DIORITE_STAIRS, POLISHED_GRANITE_STAIRS, PRISMARINE_BRICK_STAIRS, PRISMARINE_STAIRS, PURPUR_STAIRS, QUARTZ_STAIRS, RED_NETHER_BRICK_STAIRS, RED_SANDSTONE_STAIRS, SANDSTONE_STAIRS, SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE_STAIRS, SPRUCE_STAIRS, STONE_BRICK_STAIRS, STONE_STAIRS, WARPED_STAIRS -> data[col][block] = new TARDISStairRecalculator().recalculate(data[col][block], d, col, duck);
                        case ACACIA_BUTTON, BIRCH_BUTTON, CRIMSON_BUTTON, DARK_OAK_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, POLISHED_BLACKSTONE_BUTTON, SPRUCE_BUTTON, STONE_BUTTON, WARPED_BUTTON -> data[col][block] = new TARDISButtonRecalculator().recalculate(data[col][block], d);
                        case ACACIA_FENCE, BIRCH_FENCE, CRIMSON_FENCE, DARK_OAK_FENCE, JUNGLE_FENCE, NETHER_BRICK_FENCE, OAK_FENCE, SPRUCE_FENCE, WARPED_FENCE -> data[col][block] = new TARDISFenceRecalculator().recalculate(data[col][block], d);
                        case ACACIA_TRAPDOOR, BIRCH_TRAPDOOR, CRIMSON_TRAPDOOR, DARK_OAK_TRAPDOOR, IRON_TRAPDOOR, JUNGLE_TRAPDOOR, OAK_TRAPDOOR, SPRUCE_TRAPDOOR, WARPED_TRAPDOOR -> data[col][block] = new TARDISTrapdoorRecalculator().recalculate(data[col][block], d);
                        case BROWN_MUSHROOM_BLOCK -> // mushroom
                                data[col][block] = new TARDISMushroomRecalculator().recalculate(data[col][block], d, col);
                        case ANVIL, CHIPPED_ANVIL, DAMAGED_ANVIL -> {
                            Directional anvil = (Directional) data[col][block];
                            if (d == COMPASS.WEST) {
                            } else {
                                if (anvil.getFacing() == BlockFace.SOUTH) {
                                    anvil.setFacing(BlockFace.WEST);
                                } else {
                                    anvil.setFacing(BlockFace.EAST);
                                }
                            }
                            data[col][block] = anvil;
                        }
                        case JACK_O_LANTERN, CARVED_PUMPKIN, OBSERVER, WHITE_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, MAGENTA_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA, LIME_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA, BLUE_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, RED_GLAZED_TERRACOTTA, BLACK_GLAZED_TERRACOTTA -> {
                            Directional jack = (Directional) data[col][block];
                            switch (d) {
                                case EAST:
                                    jack.setFacing(BlockFace.WEST);
                                    break;
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
                        }
                        default -> { // vine
                            MultipleFacing vine = (MultipleFacing) data[col][block];
                            vine.setFace(BlockFace.EAST, false);
                            switch (d) {
                                case SOUTH:
                                    vine.setFace(BlockFace.SOUTH, true);
                                    break;
                                case WEST:
                                    vine.setFace(BlockFace.WEST, true);
                                    break;
                                default:
                                    vine.setFace(BlockFace.NORTH, true);
                                    break;
                            }
                            data[col][block] = vine;
                        }
                    }
                }
            }
        }
        return data;
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
        portal.makePresets(false, false);
        prismarine.makePresets(false, false);
        punked.makePresets(false, false);
        robot.makePresets(true, false);
        shroom.makePresets(false, false);
        snowman.makePresets(true, false);
        submerged.makePresets(true, false);
        swamp.makePresets(false, false);
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
        adaptive.makePresets(false, false);
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
                return adaptive.getBlueprint().get(d);
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
                return adaptive.getGlass().get(d);
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
                return adaptive.getStained().get(d);
        }
    }

    public void setR(int r) {
        this.r = r;
    }
}
