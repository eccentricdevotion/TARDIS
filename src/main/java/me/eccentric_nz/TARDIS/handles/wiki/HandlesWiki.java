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
        // <dd><a href="dev-commands.html" title="Dev commands">/tardisdev commands</a></dd>
        Document doc = null;
        try {
            doc = Jsoup.connect("https://eccentricdevotion.github.io/TARDIS/site-map.html").get();
            Elements links = doc.select("dd a");
            for (Element e : links) {
                String linkHref = e.attr("href"); // "dev-commands.html"
                String linkText = e.text(); // "/tardisdev commands"
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
            plugin.getMessenger().message(player,"No results");
        }
    }
}
