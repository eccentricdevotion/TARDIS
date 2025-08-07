package me.eccentric_nz.TARDIS.info.processors;

import io.papermc.paper.dialog.Dialog;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.info.dialog.SectionDialog;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public class CategoryProcessor {

    private final TARDIS plugin;
    private final Player player;

    public CategoryProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void showDialog(String c) {
        TISCategory category = TISCategory.valueOf(c);
        plugin.getTrackerKeeper().getInfoGUI().put(player.getUniqueId(), category);
        Dialog dialog = new SectionDialog().create(category);
        Audience.audience(player).showDialog(dialog);
    }
}
