package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;

public class StorageContents {

    private final TARDIS plugin;

    public StorageContents(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(Player player) {
        // get the storage record
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (!rs.resultSet()) {
            return;
        }
        try {
            for (ItemStack stack : TARDISSerializeInventory.itemStacksFromString(rs.getSavesOne())) {
                if (stack != null) {
                    player.getInventory().addItem(stack);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
