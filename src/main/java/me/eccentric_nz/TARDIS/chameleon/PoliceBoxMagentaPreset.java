package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaWestPreset;

public class PoliceBoxMagentaPreset extends TARDISColoured {

    public PoliceBoxMagentaPreset() {
        PoliceBoxMagentaEastPreset east = new PoliceBoxMagentaEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxMagentaNorthPreset north = new PoliceBoxMagentaNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxMagentaWestPreset west = new PoliceBoxMagentaWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxMagentaSouthPreset south = new PoliceBoxMagentaSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
