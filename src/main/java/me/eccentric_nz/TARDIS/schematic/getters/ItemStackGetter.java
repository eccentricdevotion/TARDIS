package me.eccentric_nz.TARDIS.schematic.getters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

public class ItemStackGetter {

    public static JsonObject getJson(ItemStack item) {
        JsonObject object = new JsonObject();
        if (item != null) {
            Material type = item.getType();
            object.addProperty("item", type.toString());
            if (item.hasData(DataComponentTypes.ITEM_MODEL)) {
                object.addProperty("cmd", item.getData(DataComponentTypes.ITEM_MODEL).value());
            }
            if (item.hasData(DataComponentTypes.CUSTOM_NAME)) {
                object.addProperty("name", ComponentUtils.stripColour(item.getData(DataComponentTypes.CUSTOM_NAME)));
            }
            if (item.hasData(DataComponentTypes.LORE)) {
                JsonArray lore = new JsonArray();
                for (Component component : item.getData(DataComponentTypes.LORE).lines()) {
                    lore.add(ComponentUtils.stripColour(component));
                }
                object.add("lore", lore);
            }
            if (Tag.ITEMS_BANNERS.isTagged(type) && item.hasData(DataComponentTypes.BANNER_PATTERNS)) {
                JsonObject state = BannerGetter.getJson(item.getData(DataComponentTypes.BANNER_PATTERNS), null);
                object.add("banner", state);
            }
            if (type == Material.SHIELD && item.hasData(DataComponentTypes.BASE_COLOR)) {
                JsonObject state = BannerGetter.getJson(item.getData(DataComponentTypes.BANNER_PATTERNS), item.getData(DataComponentTypes.BASE_COLOR));
                object.add("banner", state);
            }
        }
        return object;
    }
}
