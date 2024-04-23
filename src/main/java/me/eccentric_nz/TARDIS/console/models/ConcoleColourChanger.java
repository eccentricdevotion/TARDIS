package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ConcoleColourChanger {

    private final TARDIS plugin;
    private final Location location;
    private final String uuids;
    private final int type;

    public ConcoleColourChanger(TARDIS plugin, Location location, String uuids, int type) {
        this.plugin = plugin;
        this.location = location;
        this.uuids = uuids;
        this.type = type;
    }

    public boolean paint() {
        String[] split = uuids.split("~");
        for (String s : split) {
            try {
                UUID uuid = UUID.fromString(s);
                for (Entity e : location.getWorld().getNearbyEntities(location, 5, 5, 5, (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                    UUID p = e.getPersistentDataContainer().get(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID());
                    if (uuid.equals(p) && e instanceof ItemDisplay display) {
                        // get the item stack
                        ItemStack is = display.getItemStack();
                        if (is != null) {
                            ItemMeta im = is.getItemMeta();
                            int cmd = im.getCustomModelData() > 2000 ? 2000 : 1000;
                            im.setCustomModelData(cmd + type);
                            is.setItemMeta(im);
                            display.setItemStack(is);
                        }
                    }
                }
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        }
        return true;
    }
}
