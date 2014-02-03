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
package me.eccentric_nz.TARDIS;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.chameleon.TARDISStainedGlassLookup;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private String[][][] ARSSchematic;
    private String[][][] budgetSchematic;
    private String[][][] biggerSchematic;
    private String[][][] deluxeSchematic;
    private String[][][] eleventhSchematic;
    private String[][][] redstoneSchematic;
    private String[][][] steampunkSchematic;
    private String[][][] plankSchematic;
    private String[][][] tomSchematic;
    private String[][][] customSchematic;
    private short[] ARSDimensions = new short[3];
    private short[] budgetDimensions = new short[3];
    private short[] biggerDimensions = new short[3];
    private short[] deluxeDimensions = new short[3];
    private short[] eleventhDimensions = new short[3];
    private short[] redstoneDimensions = new short[3];
    private short[] steampunkDimensions = new short[3];
    private short[] plankDimensions = new short[3];
    private short[] tomDimensions = new short[3];
    private short[] customDimensions = new short[3];
    private HashMap<String, String[][][]> roomSchematics = new HashMap<String, String[][][]>();
    private final HashMap<String, short[]> roomDimensions = new HashMap<String, short[]>();
    private final HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<String, HashMap<String, Integer>>();
    private final TARDISStainedGlassLookup stainedGlassLookup = new TARDISStainedGlassLookup();
    private HashMap<Material, String> seeds;

    public String[][][] getARSSchematic() {
        return ARSSchematic;
    }

    public void setARSSchematic(String[][][] ARSSchematic) {
        this.ARSSchematic = ARSSchematic;
    }

    public String[][][] getBudgetSchematic() {
        return budgetSchematic;
    }

    public void setBudgetSchematic(String[][][] budgetSchematic) {
        this.budgetSchematic = budgetSchematic;
    }

    public String[][][] getBiggerSchematic() {
        return biggerSchematic;
    }

    public void setBiggerSchematic(String[][][] biggerSchematic) {
        this.biggerSchematic = biggerSchematic;
    }

    public String[][][] getDeluxeSchematic() {
        return deluxeSchematic;
    }

    public void setDeluxeSchematic(String[][][] deluxeSchematic) {
        this.deluxeSchematic = deluxeSchematic;
    }

    public String[][][] getEleventhSchematic() {
        return eleventhSchematic;
    }

    public void setEleventhSchematic(String[][][] eleventhSchematic) {
        this.eleventhSchematic = eleventhSchematic;
    }

    public String[][][] getRedstoneSchematic() {
        return redstoneSchematic;
    }

    public void setRedstoneSchematic(String[][][] redstoneSchematic) {
        this.redstoneSchematic = redstoneSchematic;
    }

    public String[][][] getSteampunkSchematic() {
        return steampunkSchematic;
    }

    public void setSteampunkSchematic(String[][][] steampunkSchematic) {
        this.steampunkSchematic = steampunkSchematic;
    }

    public String[][][] getPlankSchematic() {
        return plankSchematic;
    }

    public void setPlankSchematic(String[][][] plankSchematic) {
        this.plankSchematic = plankSchematic;
    }

    public String[][][] getTomSchematic() {
        return tomSchematic;
    }

    public void setTomSchematic(String[][][] tomSchematic) {
        this.tomSchematic = tomSchematic;
    }

    public String[][][] getCustomSchematic() {
        return customSchematic;
    }

    public void setCustomSchematic(String[][][] customSchematic) {
        this.customSchematic = customSchematic;
    }

    public HashMap<String, String[][][]> getRoomSchematics() {
        return roomSchematics;
    }

    public void setRoomSchematics(HashMap<String, String[][][]> roomSchematics) {
        this.roomSchematics = roomSchematics;
    }

    public short[] getARSDimensions() {
        return ARSDimensions;
    }

    public void setARSDimensions(short[] ARSDimensions) {
        this.ARSDimensions = ARSDimensions;
    }

    public short[] getBudgetDimensions() {
        return budgetDimensions;
    }

    public void setBudgetDimensions(short[] budgetDimensions) {
        this.budgetDimensions = budgetDimensions;
    }

    public short[] getBiggerDimensions() {
        return biggerDimensions;
    }

    public void setBiggerDimensions(short[] biggerDimensions) {
        this.biggerDimensions = biggerDimensions;
    }

    public short[] getDeluxeDimensions() {
        return deluxeDimensions;
    }

    public void setDeluxeDimensions(short[] deluxeDimensions) {
        this.deluxeDimensions = deluxeDimensions;
    }

    public short[] getEleventhDimensions() {
        return eleventhDimensions;
    }

    public void setEleventhDimensions(short[] eleventhDimensions) {
        this.eleventhDimensions = eleventhDimensions;
    }

    public short[] getRedstoneDimensions() {
        return redstoneDimensions;
    }

    public void setRedstoneDimensions(short[] redstoneDimensions) {
        this.redstoneDimensions = redstoneDimensions;
    }

    public short[] getSteampunkDimensions() {
        return steampunkDimensions;
    }

    public void setSteampunkDimensions(short[] steampunkDimensions) {
        this.steampunkDimensions = steampunkDimensions;
    }

    public short[] getPlankDimensions() {
        return plankDimensions;
    }

    public void setPlankDimensions(short[] plankDimensions) {
        this.plankDimensions = plankDimensions;
    }

    public short[] getTomDimensions() {
        return tomDimensions;
    }

    public void setTomDimensions(short[] tomDimensions) {
        this.tomDimensions = tomDimensions;
    }

    public short[] getCustomDimensions() {
        return customDimensions;
    }

    public void setCustomDimensions(short[] customDimensions) {
        this.customDimensions = customDimensions;
    }

    public HashMap<String, short[]> getRoomDimensions() {
        return roomDimensions;
    }

    public HashMap<String, HashMap<String, Integer>> getRoomBlockCounts() {
        return roomBlockCounts;
    }

    public TARDISStainedGlassLookup getStainedGlassLookup() {
        return stainedGlassLookup;
    }

    public HashMap<Material, String> getSeeds() {
        return seeds;
    }

    public void setSeeds(HashMap<Material, String> seeds) {
        this.seeds = seeds;
    }
}
