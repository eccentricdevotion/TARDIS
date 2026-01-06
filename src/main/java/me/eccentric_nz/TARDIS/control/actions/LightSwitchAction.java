/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.PresetLampToggler;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class LightSwitchAction {

    private final TARDIS plugin;
    private final int id;
    private final boolean on;
    private final Player player;
    private final TardisLight light;

    public LightSwitchAction(TARDIS plugin, int id, boolean on, Player player, TardisLight light) {
        this.plugin = plugin;
        this.id = id;
        this.on = on;
        this.player = player;
        this.light = light;
    }

    public void flickSwitch() {
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("tardis_id", id);
        HashMap<String, Object> setl = new HashMap<>();
        new PresetLampToggler(plugin).flickSwitch(id, player.getUniqueId(), on, light);
        setl.put("lights_on", (on) ? 0 : 1);
        plugin.getQueryFactory().doUpdate("tardis", setl, wherel);
    }
}
