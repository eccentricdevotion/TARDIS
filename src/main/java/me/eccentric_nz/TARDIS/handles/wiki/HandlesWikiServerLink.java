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
package me.eccentric_nz.TARDIS.handles.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.net.URI;

public class HandlesWikiServerLink {

    private final TARDIS plugin;

    public HandlesWikiServerLink(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addServerLink() {
        plugin.getServer().getServerLinks().addLink(Component.text("TARDIS Wiki", NamedTextColor.GOLD), URI.create("https://tardis.pages.dev/"));
    }
}
