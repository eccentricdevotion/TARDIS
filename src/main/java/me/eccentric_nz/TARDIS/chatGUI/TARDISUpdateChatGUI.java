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

    public boolean showGUI(Player player, String[] args) {
        if (args.length == 1) {
            TARDISMessage.send(player, "UPDATE_SECTION");
            for (String s : plugin.getJsonKeeper().getSections()) {
                sendJSON(s, player);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("controls")) {
            TARDISMessage.send(player, "UPDATE_SECTION");
            for (String c : plugin.getJsonKeeper().getControls()) {
                sendJSON(c, player);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("interfaces")) {
            TARDISMessage.send(player, "UPDATE_INTERFACE");
            for (String i : plugin.getJsonKeeper().getInterfaces()) {
                sendJSON(i, player);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("locations")) {
            TARDISMessage.send(player, "UPDATE_LOCATION");
            for (String l : plugin.getJsonKeeper().getLocations()) {
                sendJSON(l, player);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("others")) {
            TARDISMessage.send(player, "UPDATE_OTHER");
            for (String o : plugin.getJsonKeeper().getOthers()) {
                sendJSON(o, player);
            }
            return true;
        }
        return false;
    }

    private void sendJSON(String json, Player p) {
        PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
        chat.getChatComponents().write(0, WrappedChatComponent.fromJson(json));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, chat);
        } catch (InvocationTargetException e) {
            plugin.debug("Error sending JSON chat: " + e.getMessage());
        }
    }
}
