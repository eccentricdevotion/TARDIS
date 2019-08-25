package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPurpleEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPurpleNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPurpleSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPurpleWestPreset;

public class PoliceBoxPurplePreset extends TARDISColoured {

    public PoliceBoxPurplePreset() {
        PoliceBoxPurpleEastPreset east = new PoliceBoxPurpleEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxPurpleNorthPreset north = new PoliceBoxPurpleNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxPurpleWestPreset west = new PoliceBoxPurpleWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxPurpleSouthPreset south = new PoliceBoxPurpleSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
