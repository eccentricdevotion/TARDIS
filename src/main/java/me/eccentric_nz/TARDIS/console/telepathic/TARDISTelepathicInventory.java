package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIMap;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISTelepathicInventory {

    private final TARDIS plugin;
    private final Player player;

    public TARDISTelepathicInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public ItemStack[] getButtons() {
        // build buttons
        ItemStack[] stack = new ItemStack[9];
        // get current telepathic status
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        String onOff = (rsp.resultSet() && rsp.isTelepathyOn()) ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF";
        // toggling telepathic circuit on/off
        ItemStack toggle = new ItemStack(Material.REPEATER);
        ItemMeta tim = toggle.getItemMeta();
        tim.setDisplayName("Telepathic Circuit");
        tim.setLore(List.of(onOff));
        tim.setCustomModelData(40);
        toggle.setItemMeta(tim);
        stack[0] = toggle;
        // cave finder
        if (player.hasPermission("tardis.timetravel.cave")) {
            ItemStack cave = new ItemStack(Material.DRIPSTONE_BLOCK);
            ItemMeta cim = cave.getItemMeta();
            cim.setDisplayName("Cave Finder");
            cim.setLore(List.of("Search for a cave", "to travel to."));
            cave.setItemMeta(cim);
            stack[2] = cave;
        }
        // structure finder
        if (player.hasPermission("tardis.timetravel.village")) {
            ItemStack structure = new ItemStack(Material.HAY_BLOCK);
            ItemMeta sim = structure.getItemMeta();
            sim.setDisplayName("Structure Finder");
            sim.setLore(List.of("Search for a structure", "to travel to."));
            structure.setItemMeta(sim);
            stack[4] = structure;
        }
        // biome finder
        if (player.hasPermission("tardis.timetravel.biome")) {
            ItemStack biome = new ItemStack(Material.BAMBOO_MOSAIC);
            ItemMeta bim = biome.getItemMeta();
            bim.setDisplayName("Biome Finder");
            bim.setLore(List.of("Search for a biome", "to travel to."));
            biome.setItemMeta(bim);
            stack[6] = biome;
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setCustomModelData(GUIMap.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(gui);
        stack[8] = close;
        return stack;
    }
}
