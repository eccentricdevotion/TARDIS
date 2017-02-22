/*
 * Copyright (C) 2016 eccentric_nz
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
import java.util.Arrays;
import java.util.Locale;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONException;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.Listener;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix
 * a light bulb. After saying the fixture may be the problem, the sound of the
 * TARDIS materialisation is heard. The TARDIS materialises around them,
 * shocking Brian in place.
 *
 * @author eccentric_nz
 */
public final class TARDISZeroRoomPacketListener implements Listener {

    /**
     * Prevents the occupants of zero rooms from sending or receiving chat.
     *
     * @param instance
     */
    public TARDISZeroRoomPacketListener(final TARDIS instance) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(
                new PacketAdapter(instance, ListenerPriority.NORMAL, Arrays.asList(PacketType.Play.Server.CHAT), ListenerOptions.ASYNC) {
            @Override
            @SuppressWarnings("unchecked")
            public void onPacketSending(PacketEvent event) {
                boolean send = false;
                WrappedChatComponent chat = event.getPacket().getChatComponents().read(0);
                if (chat != null) {
                    String json = chat.getJson();
                    if (json != null && !json.isEmpty() && !json.equals("\"\"")) {
                        try {
                            JSONObject data = new JSONObject(json);
                            if (data.has("extra")) {
                                JSONArray extra = data.getJSONArray("extra");
                                for (int i = 0; i < extra.length(); i++) {
                                    if (extra.get(i) instanceof String) {
                                        return;
                                    }
                                    JSONObject tmp = (JSONObject) extra.get(i);
                                    if (tmp.has("text")) {
                                        String text = (String) tmp.get("text");
                                        if (text.toLowerCase(Locale.ENGLISH).contains("broadcast")) {
                                            send = true;
                                            break;
                                        }
                                    }
                                }
                                if (send == false && instance.getTrackerKeeper().getZeroRoomOccupants().contains(event.getPlayer().getUniqueId())) {
                                    event.setCancelled(true);
                                }
                            }
                        } catch (JSONException e) {
                            instance.debug("Invalid JSON in packet!");
                            instance.debug(json);
                        }
                    }
                }
            }
        }
        );
    }
}
