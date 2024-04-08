package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIMap;
import me.eccentric_nz.TARDIS.custommodeldata.GUIWallFloor;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISTelepathicBiome {

    private final TARDIS plugin;
    private final int id;

    public TARDISTelepathicBiome(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public ItemStack[] getButtons() {
        ItemStack[] stack = new ItemStack[54];
        // only show biomes for the environment the TARDIS is currently in
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Environment environment = rsc.getWorld().getEnvironment();
            // biome finder
            List<Biome> biomes;
            switch (environment) {
                case NETHER -> biomes = EnvironmentBiomes.NETHER;
                case THE_END -> biomes = EnvironmentBiomes.END;
                default -> biomes = EnvironmentBiomes.OVERWORLD;
            }
            int i = 0;
            for (Biome biome : biomes) {
                if (i > 52) {
                    break;
                }
                ItemStack is = new ItemStack(EnvironmentBiomes.BIOME_BLOCKS.get(biome));
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(TARDISStringUtils.capitalise(biome.toString()));
                is.setItemMeta(im);
                stack[i] = is;
                if (i % 9 == 7) {
                    i += 2;
                } else {
                    i++;
                }
            }
            if (environment == Environment.NORMAL) {
                // scroll up
                ItemStack scroll_up = new ItemStack(Material.ARROW, 1);
                ItemMeta uim = scroll_up.getItemMeta();
                uim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_U"));
                uim.setCustomModelData(GUIWallFloor.BUTTON_SCROLL_U.getCustomModelData());
                scroll_up.setItemMeta(uim);
                stack[8] = scroll_up;
                // scroll down
                ItemStack scroll_down = new ItemStack(Material.ARROW, 1);
                ItemMeta dim = scroll_down.getItemMeta();
                dim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_D"));
                dim.setCustomModelData(GUIWallFloor.BUTTON_SCROLL_D.getCustomModelData());
                scroll_down.setItemMeta(dim);
                stack[17] = scroll_down;
            }
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setCustomModelData(GUIMap.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(gui);
        stack[53] = close;
        return stack;
    }
}
