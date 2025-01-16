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
        Document doc = null;
        try {
            doc = Jsoup.connect("https://tardis.pages.dev/site-map").get();
            Elements links = doc.select("a");
            for (Element e : links) {
                String linkHref = e.attr("href"); // "/commands/dev"
                String linkText = e.text(); // "Dev commands"
                Matcher mat = pattern.matcher(linkText);
                if (mat.find()) {
                    results.add(new WikiLink(linkText, linkHref));
                }
            }
        } catch (IOException e) {
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
