package me.eccentric_nz.TARDIS.commands.tardis.update;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLibrary;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.library.EnchantmentShelf;
import me.eccentric_nz.TARDIS.rooms.library.LibraryCatalogue;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

public class UpdateLungeCommand {

    private final TARDIS plugin;

    public UpdateLungeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean addChiseledShelves(Player player) {
        // find the library room and add 1.21.11 lunge enchantment
        // get players tardis_id
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(player.getUniqueId().toString())) {
            int id = rst.getTardisId();
            ResultSetLibrary rsl = new ResultSetLibrary(plugin);
            if (rsl.fromId(id)) {
                Location start = TARDISStaticLocationGetters.getLocationFromBukkitString(rsl.getLocation()).add(-8, -4, -8);
                Block block;
                for (int level = 1; level < 4; level++) {
                    block = start.clone().add(EnchantmentShelf.LUNGE.getPosition()).getBlock().getRelative(BlockFace.UP, level);
                    block.setType(Material.CHISELED_BOOKSHELF);
                    // add text display
                    Location card = block.getLocation().clone().add(0.5, 0.5, -0.1);
                    TextDisplay display = (TextDisplay) start.getWorld().spawnEntity(card, EntityType.TEXT_DISPLAY);
                    display.text(Component.text("Lunge" + LibraryCatalogue.roman(level)));
                    display.setAlignment(TextDisplay.TextAlignment.CENTER);
                    display.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                    display.setBillboard(Display.Billboard.FIXED);
                    display.setRotation(180.0f, 0);
                }
                plugin.getMessenger().message(player, TardisModule.TARDIS, "Lunge enchantment added to Library room!");
            } else {
                plugin.getMessenger().message(player, TardisModule.TARDIS, "You don't have a Library room!");
            }
        }
        return true;
    }
}
