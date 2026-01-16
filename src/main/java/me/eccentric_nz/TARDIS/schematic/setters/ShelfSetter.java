package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.block.Block;
import org.bukkit.block.Shelf;
import org.bukkit.inventory.ItemStack;

public class ShelfSetter {

    public static void stock(Block block, JsonArray items) {
        Shelf shelf = (Shelf) block.getState();
        for (int i = 0; i < 3; i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            ItemStack is = ItemStackSetter.build(item.get("item").getAsJsonObject());
            shelf.getInventory().getContents()[i] = is;
        }
        shelf.update();
    }
}
