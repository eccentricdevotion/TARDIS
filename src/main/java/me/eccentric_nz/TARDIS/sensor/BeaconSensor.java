package me.eccentric_nz.TARDIS.sensor;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.block.Block;

public class BeaconSensor {

    public void toggle(String str, boolean on) {
        Block b = TARDISStaticLocationGetters.getLocationFromDB(str).getBlock();
        b.setBlockData((on) ? TARDISConstants.GLASS : TARDISConstants.POWER);
    }
}
