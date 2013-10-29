/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.commands.TARDISAdminTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISAreaTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISBindTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISGravityTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISPrefsTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISTextureTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISTravelTabComplete;

/**
 * Load the tab completion classes.
 *
 * @author eccentric_nz
 */
public class TARDISLoader_TabComplete implements TARDISTabCompleteAPI {

    private final TARDIS plugin = TARDIS.plugin;

    @Override
    public void loadTabCompletion() {
        plugin.getCommand("tardistexture").setTabCompleter(new TARDISTextureTabComplete());
        plugin.getCommand("tardisadmin").setTabCompleter(new TARDISAdminTabComplete(plugin));
        plugin.getCommand("tardis").setTabCompleter(new TARDISTabComplete(plugin));
        plugin.getCommand("tardisarea").setTabCompleter(new TARDISAreaTabComplete());
        plugin.getCommand("tardisbind").setTabCompleter(new TARDISBindTabComplete());
        plugin.getCommand("tardisprefs").setTabCompleter(new TARDISPrefsTabComplete());
        plugin.getCommand("tardistravel").setTabCompleter(new TARDISTravelTabComplete(plugin));
        plugin.getCommand("tardisgravity").setTabCompleter(new TARDISGravityTabComplete());
        plugin.getCommand("tardisrecipe").setTabCompleter(new TARDISRecipeTabComplete());
    }
}
