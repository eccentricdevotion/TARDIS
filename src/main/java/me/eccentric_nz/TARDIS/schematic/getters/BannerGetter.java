package me.eccentric_nz.TARDIS.schematic.getters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ShieldMeta;

public class BannerGetter {

    public static JsonObject getJson(BlockState b) {
        JsonObject state = new JsonObject();
        Banner banner = (Banner) b;
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

    public static JsonObject getJson(BannerPatternLayers b, DyeColor c) {
        JsonObject state = new JsonObject();
        if (c != null) {
            state.addProperty("base_colour", c.toString());
        }
        JsonArray patterns = new JsonArray();
        if (!b.patterns().isEmpty()) {
            b.patterns().forEach((p) -> {
                JsonObject pattern = new JsonObject();
                pattern.addProperty("pattern", RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getKey(p.getPattern()).getKey());
                pattern.addProperty("pattern_colour", p.getColor().toString());
                patterns.add(pattern);
            });
        }
        state.add("patterns", patterns);
        return state;
    }

    public static JsonObject getJson(ShieldMeta shield) {
        JsonObject state = new JsonObject();
        state.addProperty("base_colour", shield.getBaseColor().toString());
        JsonArray patterns = new JsonArray();
        if (shield.numberOfPatterns() > 0) {
            shield.getPatterns().forEach((p) -> {
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
