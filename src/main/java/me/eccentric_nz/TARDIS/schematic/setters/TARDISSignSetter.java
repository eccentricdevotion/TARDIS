/*
 * Copyright (C) 2026 eccentric_nz
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

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

import java.util.HashMap;
import java.util.Map;

public class TARDISSignSetter {

    public static void setSigns(HashMap<Block, JsonObject> postSignBlocks, TARDIS plugin, int id) {
        for (Map.Entry<Block, JsonObject> entry : postSignBlocks.entrySet()) {
            Block psb = entry.getKey();
            JsonObject signObject = entry.getValue();
            BlockData signData = plugin.getServer().createBlockData(signObject.get("data").getAsString());
            psb.setBlockData(signData);
            JsonObject text = signObject.has("sign") ? signObject.get("sign").getAsJsonObject() : null;
            if (id > 0 && text != null) {
                setSign(psb, text, plugin, id);
            }
        }
    }

    public static void setSign(Block block, JsonObject json, TARDIS plugin, int id) {
        Sign signState = (Sign) block.getState();
        SignSide front = signState.getSide(Side.FRONT);
        if (json.has("line1")) {
            String line1 = json.get("line1").getAsString();
            // save the control centre sign
            if (plugin != null && line1.equals("Control")) {
                String controlLocation = block.getLocation().toString();
                plugin.getQueryFactory().insertSyncControl(id, 22, controlLocation, 0);
                signState.setWaxed(true);
                signState.update(true);
            }
            front.line(0, Component.text(json.get("line0").getAsString()));
            front.line(1, Component.text(line1));
            front.line(2, Component.text(json.get("line2").getAsString()));
            front.line(3, Component.text(json.get("line3").getAsString()));
            front.setGlowingText(json.get("glowing").getAsBoolean());
            DyeColor colour = DyeColor.valueOf(json.get("colour").getAsString());
            front.setColor(colour);
            if (json.has("back")) {
                JsonObject side = json.get("back").getAsJsonObject();
                SignSide back = signState.getSide(Side.BACK);
                back.line(0, Component.text(side.get("line0").getAsString()));
                back.line(1, Component.text(side.get("line1").getAsString()));
                back.line(2, Component.text(side.get("line2").getAsString()));
                back.line(3, Component.text(side.get("line3").getAsString()));
                back.setGlowingText(side.get("glowing").getAsBoolean());
                DyeColor bcolour = DyeColor.valueOf(side.get("colour").getAsString());
                back.setColor(bcolour);
            }
            signState.setWaxed(json.get("editable").getAsBoolean());
            signState.update();
        }
    }
}
