/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class SpigotComponents {

    public static TextComponent getModule(TardisModule module) {
        ChatColor colour = ChatColor.of(module.getHex());
        TextComponent textComponent = new TextComponent("[" + module.getName() + "]");
        textComponent.setColor(colour);
        TextComponent r = new TextComponent(" ");
        textComponent.addExtra(r);
        return textComponent;
    }

    public static TextComponent getJenkinsUpdateReady(int current, int latest) {
        TextComponent textComponent = new TextComponent("There is a new TARDIS build! You are using ");
        textComponent.setColor(ChatColor.RED);
        TextComponent c = new TextComponent("#" + current);
        textComponent.setColor(ChatColor.GOLD);
        TextComponent b = new TextComponent(", the latest build is ");
        textComponent.setColor(ChatColor.RED);
        TextComponent l = new TextComponent("#" + latest);
        textComponent.setColor(ChatColor.GOLD);
        TextComponent e = new TextComponent("!");
        textComponent.addExtra(c);
        textComponent.addExtra(b);
        textComponent.addExtra(l);
        textComponent.addExtra(e);
        return textComponent;
    }

    public static TextComponent getUpdateCommand() {
        TextComponent textComponent = new TextComponent("Visit ");
        textComponent.setColor(ChatColor.GOLD);
        TextComponent u = new TextComponent("http://tardisjenkins.duckdns.org:8080/job/TARDIS/");
        u.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://tardisjenkins.duckdns.org:8080/job/TARDIS/"));
        TextComponent o = new TextComponent(" or run the ");
        TextComponent p = new TextComponent("'/tardisadmin update_plugins'");
        u.setColor(ChatColor.AQUA);
        u.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisadmin update_plugins"));
        TextComponent c = new TextComponent(" command");
        textComponent.addExtra(u);
        textComponent.addExtra(o);
        textComponent.addExtra(p);
        textComponent.addExtra(c);
        return textComponent;
    }

    public static TextComponent getBuildsBehind(int behind) {
        TextComponent textComponent = new TextComponent("[TARDIS] ");
        textComponent.setColor(ChatColor.GOLD);
        TextComponent y = new TextComponent("You are %s builds behind! Type ");
        y.setColor(ChatColor.WHITE);
        TextComponent p = new TextComponent("/tadmin update_plugins");
        p.setColor(ChatColor.AQUA);
        TextComponent u = new TextComponent(" to update!");
        u.setColor(ChatColor.WHITE);
        textComponent.addExtra(y);
        textComponent.addExtra(p);
        textComponent.addExtra(u);
        return textComponent;
    }

    public static TextComponent getRequestComehereAccept(String key, String command) {
        TextComponent textComponent = new TextComponent(key);
        textComponent.setColor(ChatColor.AQUA);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return textComponent;
    }

    public static TextComponent getAbandoned(int i, String owner, String location, int id) {
        TextComponent textComponent = new TextComponent(i + ". Abandoned by: " + owner + ", " + location);
        TextComponent tce = new TextComponent(" < Enter > ");
        tce.setColor(ChatColor.GREEN);
        tce.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to enter this TARDIS")));
        tce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisadmin enter " + id));
        textComponent.addExtra(tce);
        return textComponent;
    }

    public static TextComponent getTransmat(Transmat t) {
        TextComponent textComponent = new TextComponent(t.getName());
        textComponent.setColor(ChatColor.GREEN);
        TextComponent tcl = new TextComponent(String.format(" X: %.2f, Y: %.2f, Z: %.2f, Yaw %.2f", t.getX(), t.getY(), t.getZ(), t.getYaw()));
        tcl.setColor(ChatColor.WHITE);
        TextComponent tce = new TextComponent(" <Transmat>");
        tce.setColor(ChatColor.AQUA);
        tce.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Transmat to this location")));
        tce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis transmat tp " + t.getName()));
        textComponent.addExtra(tcl);
        textComponent.addExtra(tce);
        return textComponent;
    }

    public static TextComponent getTARDISForList(Tardis t, String world, int x, int y, int z) {
        TextComponent textComponent = new TextComponent(String.format("%s %s", t.getTardis_id(), t.getOwner()));
        textComponent.setColor(ChatColor.GREEN);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", world, x, y, z))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisadmin enter " + t.getTardis_id()));
        return textComponent;
    }

    public static TextComponent getExterminate(TARDIS plugin) {
        TextComponent textComponent = new TextComponent(plugin.getLanguage().getString("EXTERMINATE_CONFIRM"));
        textComponent.setColor(ChatColor.AQUA);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis exterminate 6z@3=V!Q7*/O_OB^"));
        return textComponent;
    }

    public static TextComponent getRescue(TARDIS plugin) {
        TextComponent textComponent = new TextComponent(plugin.getLanguage().getString("REQUEST_COMEHERE_ACCEPT"));
        textComponent.setColor(ChatColor.AQUA);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "tardis rescue accept"));
        return textComponent;
    }

    public static TextComponent getSuggestCommand(String item, String hover, String colour) {
        TextComponent textComponent = new TextComponent(item);
        textComponent.setColor(ChatColor.of(colour));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisgive [player] " + item + " "));
        return textComponent;
    }

    public static TextComponent getRunCommand(String item, String hover, String colour) {
        TextComponent textComponent = new TextComponent(item);
        textComponent.setColor(ChatColor.of(colour));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisrecipe " + item + " "));
        return textComponent;
    }

    public static TextComponent getShowMore(String command) {
        TextComponent textComponent = new TextComponent("Show more...");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command + " list_more"));
        return textComponent;
    }

    public static TextComponent getRecharger(String recharger, String world, String x, String y, String z, boolean hasPerm) {
        TextComponent textComponent = new TextComponent(recharger);
        textComponent.setColor(ChatColor.GREEN);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", world, x, y, z))));
        if (hasPerm) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tardisteleport %s %s %s not_for_players", world, x, z)));
        }
        return textComponent;
    }

    public static TextComponent getHome(TARDIS plugin, String world, int x, int y, int z) {
        TextComponent textComponent = new TextComponent(plugin.getLanguage().getString("HOME"));
        textComponent.setColor(ChatColor.GREEN);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", world, x, y, z))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardistravel home"));
        return textComponent;
    }

    public static TextComponent getSave(HashMap<String, String> map, String world) {
        TextComponent textComponent = new TextComponent(map.get("dest_name"));
        textComponent.setColor(ChatColor.GREEN);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", world, map.get("x"), map.get("y"), map.get("z")))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardistravel save " + map.get("dest_name")));
        return textComponent;
    }

    public static TextComponent getArea(Area a, int n, boolean hasPerm) {
        TextComponent textComponent = new TextComponent(n + ". [" + a.getAreaName() + "] in world: " + a.getWorld());
        if (hasPerm) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to /tardistravel here")));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tardistravel area %s", a.getAreaName())));
        }
        return textComponent;
    }

    public static TextComponent getRoom(String room, boolean hasPerm) {
        TextComponent textComponent = new TextComponent("    " + room);
        ChatColor colour = (hasPerm) ? ChatColor.GREEN : ChatColor.RED;
        textComponent.setColor(colour);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardis room " + room));
        return textComponent;
    }

    public static TextComponent getRoomGallery() {
        TextComponent textComponent = new TextComponent("https://eccentricdevotion.github.io/TARDIS/room-gallery");
        textComponent.setColor(ChatColor.AQUA);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://eccentricdevotion.github.io/TARDIS/room-gallery"));
        return textComponent;
    }

    public static TextComponent getEyebrows() {
        // TODO ?
        TextComponent textComponent = new TextComponent("[TARDIS] ");
        textComponent.setColor(ChatColor.GOLD);
        TextComponent tcl = new TextComponent("Look at these eyebrows. These are attack eyebrows! They could take off bottle caps!");
        tcl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        tcl.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis egg"));
        textComponent.addExtra(tcl);
        return tcl;
    }

    public static TextComponent getSign() {
        TextComponent textComponent = new TextComponent("Click the link to view the TARDIS wiki: ");
        TextComponent link = new TextComponent("https://eccentricdevotion.github.io/TARDIS/site-map.html");
        link.setColor(ChatColor.GREEN);
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://eccentricdevotion.github.io/TARDIS/site-map.html"));
        textComponent.addExtra(link);
        return textComponent;
    }

    public static TextComponent getUpdate(String first, String value, String split) {
        TextComponent textComponent = new TextComponent(first);
        textComponent.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        TextComponent tck = new TextComponent(value);
        tck.setColor(net.md_5.bungee.api.ChatColor.WHITE);
        textComponent.addExtra(tck);
        TextComponent tcl = new TextComponent(split);
        tcl.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        textComponent.addExtra(tcl);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisinfo " + value));
        return textComponent;
    }

    public static TextComponent getHADS(TARDIS plugin) {
        TextComponent textComponent = new TextComponent("[TARDIS]");
        textComponent.setColor(ChatColor.GOLD);
        TextComponent H = new TextComponent("H");
        H.setColor(ChatColor.RED);
        TextComponent o = new TextComponent("ostile ");
        o.setColor(ChatColor.WHITE);
        TextComponent A = new TextComponent("A");
        A.setColor(ChatColor.RED);
        TextComponent c = new TextComponent("ction ");
        c.setColor(ChatColor.WHITE);
        TextComponent D = new TextComponent("D");
        D.setColor(ChatColor.RED);
        TextComponent i = new TextComponent("isplacement ");
        i.setColor(ChatColor.WHITE);
        TextComponent S = new TextComponent("S");
        S.setColor(ChatColor.RED);
        TextComponent y = new TextComponent("ystem " + plugin.getLanguage().getString("HADS_ENGAGED"));
        y.setColor(ChatColor.WHITE);
        textComponent.addExtra(o);
        textComponent.addExtra(A);
        textComponent.addExtra(c);
        textComponent.addExtra(D);
        textComponent.addExtra(i);
        textComponent.addExtra(S);
        textComponent.addExtra(y);
        return textComponent;
    }

    public static TextComponent getColouredCommand(String which, String command, TARDIS plugin) {
        TextComponent textComponent = getModule(TardisModule.TARDIS);
        String[] split = plugin.getLanguage().getString(which).split("%s");
        TextComponent first = new TextComponent(split[0]);
        first.setColor(ChatColor.WHITE);
        TextComponent c = new TextComponent(command);
        c.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        c.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        c.setColor(ChatColor.GREEN);
        TextComponent last = new TextComponent(split[1]);
        last.setColor(ChatColor.WHITE);
        textComponent.addExtra(first);
        textComponent.addExtra(c);
        textComponent.addExtra(last);
        return textComponent;
    }

    public static TextComponent getInsertColour(String local, String which, TARDIS plugin) {
        TextComponent textComponent = getModule(TardisModule.TARDIS);
        String[] split = plugin.getLanguage().getString(local).split("%s");
        TextComponent first = new TextComponent(split[0]);
        first.setColor(ChatColor.WHITE);
        TextComponent w = new TextComponent(which);
        w.setColor(ChatColor.AQUA);
        TextComponent last = new TextComponent(split[1]);
        last.setColor(ChatColor.WHITE);
        textComponent.addExtra(first);
        textComponent.addExtra(w);
        textComponent.addExtra(last);
        return textComponent;
    }

    public static TextComponent getWithColours(String first, String colour, String last, String hue) {
        TextComponent textComponent = new TextComponent(first);
        textComponent.setColor(ChatColor.of(colour));
        TextComponent l = new TextComponent(last);
        l.setColor(ChatColor.of(hue));
        textComponent.addExtra(l);
        return textComponent;
    }

    public static TextComponent getWithColours(TardisModule module, String first, String colour, String last, String hue) {
        TextComponent textComponent = getModule(module);
        TextComponent f = new TextComponent(first);
        f.setColor(ChatColor.of(colour));
        TextComponent l = new TextComponent(last);
        l.setColor(ChatColor.of(hue));
        textComponent.addExtra(f);
        textComponent.addExtra(l);
        return textComponent;
    }

    public static TextComponent getCommand(String root, String command) {
        TextComponent textComponent = new TextComponent(root);
        textComponent.setColor(ChatColor.GOLD);
        TextComponent c = new TextComponent(" commands - use ");
        c.setColor(ChatColor.WHITE);
        TextComponent t = new TextComponent(command + " ");
        t.setColor(ChatColor.AQUA);
        t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        TextComponent f = new TextComponent("for more info");
        f.setColor(ChatColor.WHITE);
        textComponent.addExtra(c);
        textComponent.addExtra(t);
        textComponent.addExtra(f);
        return textComponent;
    }

    public static TextComponent getWikiLink(WikiLink wikiLink) {
        TextComponent textComponent = new TextComponent(wikiLink.getTitle());
        textComponent.setColor(ChatColor.BLUE);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Open link")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, wikiLink.getURL()));
        return textComponent;
    }
}
