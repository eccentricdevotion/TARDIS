package me.eccentric_nz.tardischemistry;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischemistry.compound.CompoundCommand;
import me.eccentric_nz.tardischemistry.constructor.ConstructCommand;
import me.eccentric_nz.tardischemistry.creative.CreativeCommand;
import me.eccentric_nz.tardischemistry.lab.LabCommand;
import me.eccentric_nz.tardischemistry.product.ProductCommand;
import me.eccentric_nz.tardischemistry.reducer.ReduceCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ChemistryUtility {

    public static void open(TARDIS plugin, Player player, String gui) {
        if (!TARDISPermission.hasPermission(player, "tardis.chemistry.command")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_CMD_PERM");
            return;
        }
        switch (gui) {
            case "construct" -> new ConstructCommand(plugin).build(player);
            case "compound" -> new CompoundCommand(plugin).create(player);
            case "reduce" -> new ReduceCommand(plugin).use(player);
            case "product" -> new ProductCommand(plugin).craft(player);
            case "" -> new LabCommand(plugin).combine(player);
            default -> { }
        }
    }

    public static void creative(TARDIS plugin, Player player, String gui) {
        if (!TARDISPermission.hasPermission(player, "tardis.chemistry.command")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_CMD_PERM");
            return;
        }
        new CreativeCommand(plugin).open(player, gui);
    }

    public static void recipe(TARDIS plugin, Player player, String w) {
        if (!TARDISPermission.hasPermission(player, "tardis.help")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        String which = w.toLowerCase(Locale.ROOT);
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        Material surround = switch (which) {
            case "creative" -> Material.DIAMOND;
            case "construct" -> Material.LAPIS_LAZULI;
            case "compound" -> Material.REDSTONE;
            case "reduce" -> Material.GOLD_NUGGET;
            case "product" -> Material.IRON_NUGGET;
            // lab
            default -> Material.COAL;
        };
        player.openInventory(new TARDISChemistryRecipeInventory(plugin, which, surround).getInventory());
    }
}
