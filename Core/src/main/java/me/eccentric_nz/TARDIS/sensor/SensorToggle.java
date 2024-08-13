package me.eccentric_nz.TARDIS.sensor;

import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SensorToggle {

    public Block getBlock(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        Location location = TARDISStaticLocationGetters.getLocationFromDB(str);
        if (location != null) {
            return location.getBlock();
        }
        return null;
    }

    public void setState(Block block) {
        if (block.getType() == Material.REDSTONE_BLOCK) {
            block.setType(Material.STONE);
        } else {
            block.setType(Material.REDSTONE_BLOCK);
        }
    }
}
