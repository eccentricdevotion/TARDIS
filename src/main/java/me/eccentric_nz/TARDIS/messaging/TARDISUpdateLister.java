/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableCategory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISUpdateLister {

    private final Player player;
    private final TableGenerator tg;

    public TARDISUpdateLister(Player player) {
        this.player = player;
        if (TableGenerator.getSenderPrefs(player)) {
            tg = new TableGeneratorCustomFont(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
        } else {
            tg = new TableGeneratorSmallChar(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
        }
    }

    public void list() {
        TARDISMessage.send(player, "UPDATE_INFO");
        for (String line : createUpdateOptions()) {
            player.sendMessage(line);
        }
    }

    private List<String> createUpdateOptions() {
        tg.addRow(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Command argument", ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Description");
        tg.addRow();
        for (TARDISUpdateableCategory category : TARDISUpdateableCategory.values()) {
            tg.addRow(category.getName(), "");
            for (Updateable updateable : Updateable.values()) {
                if (updateable.getCategory() == category) {
                    tg.addRow(category.getKeyColour() + updateable.getName(), category.getValueColour() + updateable.getDescription());
                }
            }
            tg.addRow();
        }
        return tg.generate(TableGeneratorSmallChar.Receiver.CLIENT, true, true);
    }
}
