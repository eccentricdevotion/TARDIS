package me.eccentric_nz.TARDIS.schematic.getters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ShieldMeta;

public class BannerGetter {

    public static JsonObject getJson(BlockState b) {
        JsonObject state = new JsonObject();
        Banner banner = (Banner) b;
        state.addProperty("base_colour", banner.getBaseColor().toString());
        JsonArray patterns = new JsonArray();
        if (banner.numberOfPatterns() > 0) {
            banner.getPatterns().forEach((p) -> {
                JsonObject pattern = new JsonObject();
                pattern.addProperty("pattern", RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getKey(p.getPattern()).getKey());
                pattern.addProperty("pattern_colour", p.getColor().toString());
                patterns.add(pattern);
            });
        }
        state.add("patterns", patterns);
        return state;
    }

    public static JsonObject getJson(ShieldMeta banner) {
        JsonObject state = new JsonObject();
        state.addProperty("base_colour", banner.getBaseColor().toString());
        JsonArray patterns = new JsonArray();
        if (banner.numberOfPatterns() > 0) {
            banner.getPatterns().forEach((p) -> {
                JsonObject pattern = new JsonObject();
                pattern.addProperty("pattern", RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getKey(p.getPattern()).getKey());
                pattern.addProperty("pattern_colour", p.getColor().toString());
                patterns.add(pattern);
            });
        }
        state.add("patterns", patterns);
        return state;
    }

    public static JsonObject getJson(BannerMeta banner, Material material) {
        JsonObject state = new JsonObject();
        state.addProperty("base_colour", material.toString().replace("_BANNER", ""));
        JsonArray patterns = new JsonArray();
        if (banner.numberOfPatterns() > 0) {
            banner.getPatterns().forEach((p) -> {
                JsonObject pattern = new JsonObject();
                pattern.addProperty("pattern", RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getKey(p.getPattern()).getKey());
                pattern.addProperty("pattern_colour", p.getColor().toString());
                patterns.add(pattern);
            });
        }
        state.add("patterns", patterns);
        return state;
    }
}
