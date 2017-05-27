/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBannerSetter {

    @SuppressWarnings("deprecation")
    public static void setBanners(int id, HashMap<Block, JSONObject> banners) {
        for (Map.Entry<Block, JSONObject> entry : banners.entrySet()) {
            JSONObject state = entry.getValue();
            Block pbb = entry.getKey().getLocation().getBlock();
            pbb.setTypeIdAndData(id, state.getByte("bdata"), true);
            Banner banner = (Banner) pbb.getState();
            DyeColor dye = DyeColor.valueOf(state.getString("colour"));
            banner.setBaseColor(dye);
            List<Pattern> plist = new ArrayList<>();
            JSONArray patterns = state.getJSONArray("patterns");
            for (int j = 0; j < patterns.length(); j++) {
                JSONObject jo = patterns.getJSONObject(j);
                PatternType pt = PatternType.valueOf(jo.getString("pattern"));
                DyeColor dc = DyeColor.valueOf(jo.getString("pattern_colour"));
                Pattern p = new Pattern(dc, pt);
                plist.add(p);
            }
            banner.setPatterns(plist);
            banner.update();
        }
    }
}
