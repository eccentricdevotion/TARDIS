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
package me.eccentric_nz.TARDIS.mapping;

import me.eccentric_nz.TARDIS.api.TARDISData;

public interface TARDISMapper {

    public static final String INFO = """
            <div class="regioninfo">
                <div class="infowindow">
                    <span style="font-weight:bold;">Time Lord:</span> %owner%<br/>
                    <span style="font-weight:bold;">Console type:</span> %console%<br/>
                    <span style="font-weight:bold;">Chameleon circuit:</span> %chameleon%<br/>
                    <span style="font-weight:bold;">Location:</span> %location%<br/>
                    <span style="font-weight:bold;">Door:</span> %door%<br/>
                    <span style="font-weight:bold;">Powered on:</span> %powered%<br/>
                    <span style="font-weight:bold;">Siege mode:</span> %siege%<br/>
                    <span style="font-weight:bold;">Occupants:</span> %occupants%
                </div>
            </div>""";

    /**
     * Disable mapping TARDISes
     */
    void disable();

    /**
     * Enable mapping TARDISes
     */
    void enable();

    /**
     * Activate mapping TARDISes
     */
    void activate();

    /**
     * Update markers with relevant TARDIS data
     *
     * @param period the time in server ticks between updates
     */
    void updateMarkerSet(long period);

    default String formatInfoWindow(TARDISData data) {
        String window = INFO;
        window = window.replace("%owner%", data.getOwner());
        window = window.replace("%console%", data.getConsole());
        window = window.replace("%chameleon%", data.getChameleon());
        String l = "x: " + data.getLocation().getBlockX() + ", y: " + data.getLocation().getBlockY() + ", z: " + data.getLocation().getBlockZ();
        window = window.replace("%location%", l);
        window = window.replace("%powered%", data.getPowered());
        window = window.replace("%door%", data.getDoor());
        window = window.replace("%siege%", data.getSiege());
        StringBuilder travellers = new StringBuilder();
        if (!data.getOccupants().isEmpty()) {
            for (String o : data.getOccupants()) {
                travellers.append(o).append("<br />");
            }
        } else {
            travellers = new StringBuilder("Empty");
        }
        window = window.replace("%occupants%", travellers.toString());
        return window;
    }
}
