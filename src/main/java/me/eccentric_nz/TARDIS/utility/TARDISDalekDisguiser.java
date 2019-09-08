package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.planets.TARDISAngelsAPI;
import org.bukkit.entity.Skeleton;

public class TARDISDalekDisguiser {

    public static void dalekanium(Skeleton skeleton) {
        TARDISAngelsAPI.getAPI(TARDIS.plugin).setDalekEquipment(skeleton);
    }
}
