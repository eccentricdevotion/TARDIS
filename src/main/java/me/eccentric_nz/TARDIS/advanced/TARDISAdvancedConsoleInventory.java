package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;

public class TARDISAdvancedConsoleInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String uuid;
    private final int id;
    private final Inventory inventory;

    public TARDISAdvancedConsoleInventory(TARDIS plugin, String uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
        this.inventory = plugin.getServer().createInventory(this, 18, Component.text("TARDIS Console", NamedTextColor.DARK_RED));
        this.inventory.setContents(getContents());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getContents() {
        ItemStack[] stacks;
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetDiskStorage storage = new ResultSetDiskStorage(plugin, where);
        if (storage.resultSet()) {
            String console = storage.getConsole();
            if (!console.isEmpty()) {
                String[] versions = storage.getVersions().split(",");
                int version = TARDISNumberParsers.parseInt(versions[3]);
                try {
                    if (version < 2) {
                        stacks = TARDISStorageConverter.updateCircuits(console);
                        versions[3] = version == 0 ? "1" : "2";
                        updateVersions(versions, uuid);
                    } else {
                        stacks = TARDISSerializeInventory.itemStacksFromString(console);
                    }
                } catch (IOException ex) {
                    plugin.debug("Could not read console from database!");
                    stacks = new ItemStack[18];
                }
            } else {
                stacks = new ItemStack[18];
            }
        } else {
            stacks = create(id);
        }
        return stacks;
    }

    private ItemStack[] create(int id) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", uuid);
        set.put("tardis_id", id);
        // a non-empty console record is required for area storage
        set.put("console", "rO0ABXcEAAAAEnBwcHBwcHBwcHBwcHBwcHBwcA==");
        plugin.getQueryFactory().doInsert("storage", set);
        return new ItemStack[18];
    }

    private void updateVersions(String[] versions, String uuid) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("versions", String.join(",", versions));
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        plugin.getQueryFactory().doSyncUpdate("storage", set, where);
    }
}
