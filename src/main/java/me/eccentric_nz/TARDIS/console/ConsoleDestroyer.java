package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.database.ClearInteractions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsoleLabel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteractionsFromId;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class ConsoleDestroyer {

    private final TARDIS plugin;

    public ConsoleDestroyer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack returnStack(String uuids, int id) {
        String colour = "";
        int cmd = 1;
        ResultSetInteractionsFromId rs = new ResultSetInteractionsFromId(plugin, id);
        if (rs.resultSet()) {
            // interactions
            for (UUID u : rs.getUuids()) {
                Entity e = plugin.getServer().getEntity(u);
                if (e != null) {
                    // item displays
                    UUID uuid = e.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
                    if (uuid != null) {
                        ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
                        if (display != null) {
                            display.remove();
                        }
                    }
                    // remove
                    e.remove();
                }
            }
            // remove the centre block
            ResultSetConsoleLabel rsc = new ResultSetConsoleLabel(plugin, id);
            if (rsc.resultSet()) {
                Block centre = rsc.getLocation().getBlock();
                centre.setType(Material.AIR);
                removeTextDisplays(rsc.getLocation());
            }
            String[] split = uuids.split("~");
            for (String u : split) {
                try {
                    UUID uuid = UUID.fromString(u);
                    Entity e = plugin.getServer().getEntity(uuid);
                    if (e instanceof ItemDisplay display) {
                        // get colour
                        ItemStack is = display.getItemStack();
                        if (colour.isEmpty() && is != null && is.hasItemMeta()) {
                            cmd = is.getItemMeta().getCustomModelData() % 1000;
                            colour = ColourType.COLOURS.getOrDefault(cmd, "LIGHT_GRAY");
                        }
                        // remove
                        display.remove();
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
            // remove database records
            new ClearInteractions(plugin).removeRecords(id);
            // build item stack
            Material material = Material.valueOf(colour + "_CONCRETE");
            ItemStack console = new ItemStack(material, 1);
            ItemMeta im = console.getItemMeta();
            String dn = TARDISStringUtils.capitalise(colour) + " Console";
            im.setDisplayName(dn);
            im.setLore(List.of("Integration with interaction"));
            im.setCustomModelData(1001);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
            console.setItemMeta(im);
            return console;
        }
        return null;
    }

    private void removeTextDisplays(Location centre) {
        Location spawn = centre.clone().add(0.5f, 0, 0.5f);
        for (Entity e : spawn.getWorld().getNearbyEntities(spawn, 4, 3, 4, (t) -> t.getType() == EntityType.TEXT_DISPLAY)) {
            if (e instanceof TextDisplay) {
                e.remove();
            }
        }
    }
}
