/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
