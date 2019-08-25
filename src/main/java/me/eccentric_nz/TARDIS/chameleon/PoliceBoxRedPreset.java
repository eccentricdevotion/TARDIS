package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxRedEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxRedNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxRedSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxRedWestPreset;

public class PoliceBoxRedPreset extends TARDISColoured {

    public PoliceBoxRedPreset() {
        PoliceBoxRedEastPreset east = new PoliceBoxRedEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxRedNorthPreset north = new PoliceBoxRedNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxRedWestPreset west = new PoliceBoxRedWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxRedSouthPreset south = new PoliceBoxRedSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
