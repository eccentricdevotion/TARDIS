package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIMap;
import me.eccentric_nz.TARDIS.travel.TARDISStructureTravel;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISTelepathicStructure {

    private final TARDIS plugin;

    public TARDISTelepathicStructure(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack[] getButtons() {
        // structure finder
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        for (Structure structure : TARDISStructureTravel.overworldStructures) {
            ItemStack is = make(structure, Material.GRASS_BLOCK);
            stack[i] = is;
            i++;
        }
        for (Structure structure : TARDISStructureTravel.netherStructures) {
            ItemStack is = make(structure, Material.CRIMSON_NYLIUM);
            stack[i] = is;
            i++;
        }
        ItemStack end = make(Structure.END_CITY, Material.PURPUR_BLOCK);
        stack[i] = end;
        // close
        ItemStack close = new ItemStack(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setItemModel(GUIMap.BUTTON_CLOSE.key());
        close.setItemMeta(gui);
        stack[53] = close;
        return stack;
    }

    private ItemStack make(Structure structure, Material material) {
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(TARDISStringUtils.capitalise(structure.getKey().getKey()));
        is.setItemMeta(im);
        return is;
    }
}
