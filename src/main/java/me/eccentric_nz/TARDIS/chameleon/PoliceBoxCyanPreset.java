package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxCyanEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxCyanNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxCyanSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxCyanWestPreset;

public class PoliceBoxCyanPreset extends TARDISColoured {

    public PoliceBoxCyanPreset() {
        PoliceBoxCyanEastPreset east = new PoliceBoxCyanEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxCyanNorthPreset north = new PoliceBoxCyanNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxCyanWestPreset west = new PoliceBoxCyanWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxCyanSouthPreset south = new PoliceBoxCyanSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
