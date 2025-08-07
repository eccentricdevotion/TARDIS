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
