package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemStackSetter {

    public static ItemStack build(JsonObject json) {
        if (!json.isEmpty()) {
            Material material = Material.valueOf(json.get("item").getAsString());
            ItemStack is = ItemStack.of(material);
            // needed for Time Rotors / Doors
            if (json.has("cmd")) {
                String cmd = json.get("cmd").getAsString();
                if (!cmd.equals("st_johns")) {
                    NamespacedKey key = new NamespacedKey(TARDIS.plugin, cmd);
                    is.setData(DataComponentTypes.ITEM_MODEL, key);
                } else {
                    is.setData(DataComponentTypes.CUSTOM_NAME, Component.text("St John's Logo"));
                }
            }
            if (json.has("name")) {
                is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.fromJson(json.get("name")));
            }
            if (json.has("lore")) {
                ItemLore.Builder lore = ItemLore.lore();
                for (JsonElement element : json.get("lore").getAsJsonArray()) {
                    lore.addLine(Component.text(element.getAsString()));
                }
                is.setData(DataComponentTypes.LORE, lore.build());
            }
            if (json.has("banner")) {
                try {
                    JsonObject banner = json.get("banner").getAsJsonObject();
                    if (material == Material.SHIELD) {
                        is.setData(DataComponentTypes.BASE_COLOR, DyeColor.valueOf(banner.get("base_colour").getAsString()));
                    }
                    JsonArray patterns = banner.get("patterns").getAsJsonArray();
                    List<Pattern> plist = new ArrayList<>();
                    for (int j = 0; j < patterns.size(); j++) {
                        JsonObject jo = patterns.get(j).getAsJsonObject();
                        PatternType pt = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(
                                new NamespacedKey("minecraft", jo.get("pattern").getAsString().toLowerCase(Locale.ROOT))
                        );
                        if (pt != null) {
                            DyeColor dc = DyeColor.valueOf(jo.get("pattern_colour").getAsString());
                            Pattern p = new Pattern(dc, pt);
                            plist.add(p);
                        }
                    }
                    is.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                            .addAll(plist)
                            .build());
                } catch (IllegalArgumentException e) {
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not create item stack for schematic item frame!");
                }
            }
            return is;
        }
        return ItemStack.empty();
    }
}
