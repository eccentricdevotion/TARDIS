package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.schematic.setters.ShelfSetter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ShelfCommand {

    private final TARDIS plugin;

    public ShelfCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean putItems(Player player) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
        Block up = targetBlock.getRelative(BlockFace.UP);
        up.setType(Material.ACACIA_SHELF);
        String items = "{\"items\":[{\"item\":\"WHITE_BANNER\",\"banner\":{\"base_colour\":\"WHITE\",\"patterns\":[{\"pattern\":\"square_top_left\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"stripe_center\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"stripe_bottom\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"border\",\"pattern_colour\":\"WHITE\"}]}},{},{\"item\":\"WHITE_BANNER\",\"banner\":{\"base_colour\":\"WHITE\",\"patterns\":[{\"pattern\":\"stripe_left\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"stripe_top\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"stripe_bottom\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"stripe_right\",\"pattern_colour\":\"BLACK\"},{\"pattern\":\"border\",\"pattern_colour\":\"WHITE\"}]}}]}";
        JsonObject json = JsonParser.parseString(items).getAsJsonObject();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> ShelfSetter.stock(up, json.get("items").getAsJsonArray()), 3L);
        return true;
    }
}
