package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.ShieldMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemStackSetter {

    public static ItemStack build(JsonObject json) {
        if (!json.isEmpty()) {
            Material material = Material.valueOf(json.get("item").getAsString());
            ItemStack is = ItemStack.of(material);
            ItemMeta im = is.getItemMeta();
            // needed for Time Rotors / Doors
            if (json.has("cmd")) {
                String cmd = json.get("cmd").getAsString();
                if (!cmd.equals("st_johns")) {
                    NamespacedKey key = new NamespacedKey(TARDIS.plugin, cmd);
                    im.setItemModel(key);
                } else {
                    im.displayName(Component.text("St John's Logo"));
                }
            }
            if (json.has("name")) {
                im.displayName(ComponentUtils.fromJson(json.get("name")));
            }
            if (json.has("lore")) {
                List<Component> lore = new ArrayList<>();
                for (JsonElement element : json.get("lore").getAsJsonArray()) {
                    lore.add(Component.text(element.getAsString()));
                }
                im.lore(lore);
            }
            if (json.has("banner")) {
                try {
                    JsonObject banner = json.get("banner").getAsJsonObject();
                    BannerMeta bm = (BannerMeta) im;
                    ShieldMeta sm = null;
                    if (material == Material.SHIELD) {
                        DyeColor baseColour = DyeColor.valueOf(banner.get("base_colour").getAsString());
                        sm = (ShieldMeta) bm;
                        sm.setBaseColor(baseColour);
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
                    if (material == Material.SHIELD) {
                        sm.setPatterns(plist);
                        is.setItemMeta(sm);
                    } else { // banner
                        bm.setPatterns(plist);
                        is.setItemMeta(bm);
                    }
                } catch (IllegalArgumentException e) {
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not create item stack for schematic item frame!");
                }
            } else {
                is.setItemMeta(im);
            }
            return is;
        }
        return ItemStack.empty();
    }
}
