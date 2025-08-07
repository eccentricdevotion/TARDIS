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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlesWiki {

    private final TARDIS plugin;

    public HandlesWiki(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getLinks(String query, Player player) {
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Set<WikiLink> results = new HashSet<>();
        // <a title="Dev commands" href="/commands/dev">Dev commands</a>
        try {
            Document doc = Jsoup.connect("https://tardis.pages.dev/site-map").get();
            Elements links = doc.select("a");
            for (Element e : links) {
                String linkHref = e.attr("href"); // "/commands/dev"
                String linkText = e.text(); // "Dev commands"
                Matcher mat = pattern.matcher(linkText);
                if (mat.find()) {
                    results.add(new WikiLink(linkText, linkHref));
                }
            }
        } catch (IOException ignored) {
        }
        plugin.getMessenger().send(player, TardisModule.HANDLES, "HANDLES_WIKI");
        if (!results.isEmpty()) {
            for (WikiLink w : results) {
                plugin.getMessenger().sendWikiLink(player, w);
            }
        } else {
            plugin.getMessenger().message(player, "No results");
        }
    }
}
