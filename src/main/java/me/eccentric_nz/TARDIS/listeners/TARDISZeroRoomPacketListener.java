/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.Locale;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix a light bulb. After saying the fixture
 * may be the problem, the sound of the TARDIS materialisation is heard. The TARDIS materialises around them, shocking
 * Brian in place.
 *
 * @author eccentric_nz
 */
public final class TARDISZeroRoomPacketListener implements Listener {

    /**
     * Prevents the occupants of zero rooms from sending or receiving chat.
     *
     * @param instance An instance of the TARDIS plugin
     */
    public TARDISZeroRoomPacketListener(TARDIS instance) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, Collections.singletonList(PacketType.Play.Server.CHAT), ListenerOptions.ASYNC) {
            @Override
            public void onPacketSending(PacketEvent event) {
                boolean send = false;
                WrappedChatComponent chat = event.getPacket().getChatComponents().read(0);
                if (chat != null) {
                    String json = chat.getJson();
                    if (json != null && !json.isEmpty() && !json.equals("\"\"")) {
                        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
                        if (data.has("extra")) {
                            JsonArray extra = data.get("extra").getAsJsonArray();
                            for (int i = 0; i < extra.size(); i++) {
                                JsonObject tmp = extra.get(i).getAsJsonObject();
                                if (tmp.has("text")) {
                                    String text = tmp.get("text").getAsString();
                                    if (text.toLowerCase(Locale.ENGLISH).contains("broadcast")) {
                                        send = true;
                                        break;
                                    }
                                }
                            }
                            if (!send && instance.getTrackerKeeper().getZeroRoomOccupants().contains(event.getPlayer().getUniqueId())) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        });
    }
}
