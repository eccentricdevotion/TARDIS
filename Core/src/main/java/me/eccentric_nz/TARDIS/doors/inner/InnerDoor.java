package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInnerDoorBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;

public class InnerDoor {

    private final TARDIS plugin;
    private final int id;

    public InnerDoor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public Inner get() {
        Block block = null;
        boolean b = false;
        // get inner door block
        ResultSetInnerDoorBlock rs = new ResultSetInnerDoorBlock(plugin, id);
        if (rs.resultSet()) {
            block = rs.getInnerBlock();
            if (block != null) {
                // check for item display
                ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
                b = display != null;
            }
        }
        return new Inner(b, block);
    }
}
