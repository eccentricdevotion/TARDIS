package me.eccentric_nz.TARDIS.utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class TARDISSign {

    public static void sendSignLink(Player player) {
        TextComponent start = new TextComponent("Click the link to view the TARDIS wiki: ");
        TextComponent link = new TextComponent("https://eccentricdevotion.github.io/TARDIS/site-map.html");
        link.setColor(ChatColor.GREEN);
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://eccentricdevotion.github.io/TARDIS/site-map.html"));
        start.addExtra(link);
        player.spigot().sendMessage(start);
    }
}
