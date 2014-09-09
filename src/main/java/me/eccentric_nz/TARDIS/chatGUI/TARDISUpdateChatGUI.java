package me.eccentric_nz.TARDIS.chatGUI;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import java.lang.reflect.InvocationTargetException;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

public class TARDISUpdateChatGUI {

    private final TARDIS plugin;

    public TARDISUpdateChatGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean showInterface(Player player, String[] args) {
        if (args.length == 1) {
            TARDISMessage.send(player, "UPDATE_SECTION");
            player.sendMessage("------");
            for (String s : plugin.getJsonKeeper().getSections()) {
                sendJSON(s, player);
            }
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("controls")) {
            TARDISMessage.send(player, "UPDATE_SECTION");
            player.sendMessage("------");
            for (String c : plugin.getJsonKeeper().getControls()) {
                sendJSON(c, player);
            }
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("interfaces")) {
            TARDISMessage.send(player, "UPDATE_INTERFACE");
            player.sendMessage("------");
            for (String i : plugin.getJsonKeeper().getInterfaces()) {
                sendJSON(i, player);
            }
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("locations")) {
            TARDISMessage.send(player, "UPDATE_LOCATION");
            player.sendMessage("------");
            for (String l : plugin.getJsonKeeper().getLocations()) {
                sendJSON(l, player);
            }
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("others")) {
            TARDISMessage.send(player, "UPDATE_OTHER");
            player.sendMessage("------");
            for (String o : plugin.getJsonKeeper().getOthers()) {
                sendJSON(o, player);
            }
            player.sendMessage("------");
            return true;
        }
        return false;
    }

    public static void sendJSON(String json, Player p) {
        PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
        chat.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, chat);
        } catch (InvocationTargetException e) {
            TARDIS.plugin.debug("Error sending JSON chat: " + e.getMessage());
        }
    }
}
