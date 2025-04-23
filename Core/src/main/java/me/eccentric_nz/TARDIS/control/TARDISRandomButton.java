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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.control.actions.ExileAction;
import me.eccentric_nz.TARDIS.control.actions.RandomDestinationAction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRandomInteractions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRepeaters;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISRandomButton {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final int level;
    private final HashMap<String, Object> set = new HashMap<>();
    private final int secondary;
    private final String comps;
    private final UUID ownerUUID;

    public TARDISRandomButton(TARDIS plugin, Player player, int id, int level, int secondary, String comps, UUID ownerUUID) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.level = level;
        this.secondary = secondary;
        this.comps = comps;
        this.ownerUUID = ownerUUID;
    }

    public void clickButton() {
        int cost = plugin.getArtronConfig().getInt("random");
        if (level < cost) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, id);
        if (!rscl.resultSet()) {
            // emergency TARDIS relocation
            new TARDISEmergencyRelocation(plugin).relocate(id, player);
            return;
        }
        COMPASS direction = rscl.getDirection();
        if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
            new ExileAction(plugin).getExile(player, id, direction);
        } else {
            ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, id, secondary);
            if (rsr.resultSet()) {
                int[] repeaters = rsr.getRepeaters();
                if (repeaters[0] == -1) {
                    // try getting modeled console settings
                    // from WORLD, MULTIPLIER, X, Z and HELMIC_REGULATOR interactions
                    ResultSetRandomInteractions rsri = new ResultSetRandomInteractions(plugin, id);
                    if (rsri.resultSet()) {
                        repeaters = rsri.getStates();
                    }
                }
                new RandomDestinationAction(plugin).getRandomDestination(player, id, repeaters, rscl, direction, level, cost, comps, ownerUUID);
            }
        }
    }
}
