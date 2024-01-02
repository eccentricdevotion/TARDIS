package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
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
            if (text != null) {
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
            front.setLine(0, json.get("line0").getAsString());
            front.setLine(1, line1);
            front.setLine(2, json.get("line2").getAsString());
            front.setLine(3, json.get("line3").getAsString());
            front.setGlowingText(json.get("glowing").getAsBoolean());
            DyeColor colour = DyeColor.valueOf(json.get("colour").getAsString());
            front.setColor(colour);
            if (json.has("back")) {
                JsonObject side = json.get("back").getAsJsonObject();
                SignSide back = signState.getSide(Side.BACK);
                back.setLine(0, side.get("line0").getAsString());
                back.setLine(1, side.get("line1").getAsString());
                back.setLine(2, side.get("line2").getAsString());
                back.setLine(3, side.get("line3").getAsString());
                back.setGlowingText(side.get("glowing").getAsBoolean());
                DyeColor bcolour = DyeColor.valueOf(side.get("colour").getAsString());
                back.setColor(bcolour);
            }
            signState.setEditable(json.get("editable").getAsBoolean());
            signState.update();
        }
    }
}
