/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISBannerSetter {

    public static void setBanners(HashMap<Block, TARDISBannerData> banners) {
        banners.forEach((key, tbd) -> {
            JsonObject state = tbd.getState();
            if (state != null) {
                key.setBlockData(tbd.getData(), true);
                Banner banner = (Banner) key.getState();
                List<Pattern> plist = new ArrayList<>();
                JsonArray patterns = state.get("patterns").getAsJsonArray();
                for (int j = 0; j < patterns.size(); j++) {
                    JsonObject jo = patterns.get(j).getAsJsonObject();
                    Registry<PatternType> patternTypeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN);
                    PatternType pt = patternTypeRegistry.get(NamespacedKey.minecraft(jo.get("pattern").getAsString().toLowerCase(Locale.ROOT)));
                    DyeColor dc = DyeColor.valueOf(jo.get("pattern_colour").getAsString());
                    if (pt != null) {
                        Pattern p = new Pattern(dc, pt);
                        plist.add(p);
                    }
                }
                banner.setPatterns(plist);
                banner.update();
                if (TARDIS.plugin.getBlockLogger().isLogging()) {
                    TARDIS.plugin.getBlockLogger().logPlacement(key);
                }
            }
        });
    }
}
