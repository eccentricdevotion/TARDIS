package me.eccentric_nz.TARDIS.schematic.getters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackGetter {

    public static JsonObject getJson(ItemStack item) {
        JsonObject object = new JsonObject();
        if (item != null) {
            Material type = item.getType();
            object.addProperty("item", type.toString());
            if (item.hasItemMeta()) {
                ItemMeta im = item.getItemMeta();
                if (im.hasItemModel()) {
                    object.addProperty("cmd", im.getItemModel().getKey());
                }
                if (im.hasDisplayName()) {
                    object.addProperty("name", ComponentUtils.stripColour(im.displayName()));
                }
                if (im.hasLore()) {
                    JsonArray lore = new JsonArray();
                    for (Component component : im.lore()) {
                        lore.add(ComponentUtils.stripColour(component));
                    }
                    object.add("lore", lore);
                }
                if ((Tag.ITEMS_BANNERS.isTagged(type) || type == Material.SHIELD) && im instanceof BlockStateMeta bsm) {
                    JsonObject state = BannerGetter.getJson(bsm.getBlockState());
                    object.add("banner", state);
                }
            }
        }
        return object;
    }
}
