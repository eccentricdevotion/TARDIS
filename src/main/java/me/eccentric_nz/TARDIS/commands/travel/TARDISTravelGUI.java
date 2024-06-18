package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicBiome;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicStructure;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateBiomesForm;
import me.eccentric_nz.TARDIS.floodgate.FloodgateStructuresForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TARDISTravelGUI {

    private final TARDIS plugin;

    public TARDISTravelGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player, int id, String which) {
        // check for telepathic circuit
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true)) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (!tcc.hasTelepathic()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
                return true;
            }
        }
        if (which.equals("biome")) {
            // open biomes GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                new FloodgateBiomesForm(plugin, player.getUniqueId(), id).send();
            } else {
                TARDISTelepathicBiome ttb = new TARDISTelepathicBiome(plugin, id);
                ItemStack[] gui = ttb.getButtons();
                Inventory biome = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Telepathic Biome Finder");
                biome.setContents(gui);
                player.openInventory(biome);
            }
        } else {
            // open Structure GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                new FloodgateStructuresForm(plugin, player.getUniqueId(), id).send();
            } else {
                TARDISTelepathicStructure tts = new TARDISTelepathicStructure(plugin);
                ItemStack[] gui = tts.getButtons();
                Inventory structure = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Telepathic Structure Finder");
                structure.setContents(gui);
                player.openInventory(structure);
            }
        }
        return true;
    }
}
