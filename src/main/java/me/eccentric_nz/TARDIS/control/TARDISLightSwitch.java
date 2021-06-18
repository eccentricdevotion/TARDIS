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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.artron.TardisLampToggler;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisLightSwitch {

    private final TardisPlugin plugin;
    private final int id;
    private final boolean lightsOn;
    private final Player player;
    private final boolean lanterns;

    public TardisLightSwitch(TardisPlugin plugin, int id, boolean lightsOn, Player player, boolean lanterns) {
        this.plugin = plugin;
        this.id = id;
        this.lightsOn = lightsOn;
        this.player = player;
        this.lanterns = lanterns;
    }

    public void flickSwitch() {
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("tardis_id", id);
        HashMap<String, Object> setl = new HashMap<>();
        if (lightsOn) {
            new TardisLampToggler(plugin).flickSwitch(id, player.getUniqueId(), true, lanterns);
            setl.put("lights_on", 0);
        } else {
            new TardisLampToggler(plugin).flickSwitch(id, player.getUniqueId(), false, lanterns);
            setl.put("lights_on", 1);
        }
        plugin.getQueryFactory().doUpdate("tardis", setl, wherel);
    }
}
