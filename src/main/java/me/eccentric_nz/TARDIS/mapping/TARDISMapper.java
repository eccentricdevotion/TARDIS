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

    String INFO = """
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
        window = window.replace("%owner%", data.owner());
        window = window.replace("%console%", data.console());
        window = window.replace("%chameleon%", data.chameleon());
        String l = "x: " + data.location().getBlockX() + ", y: " + data.location().getBlockY() + ", z: " + data.location().getBlockZ();
        window = window.replace("%location%", l);
        window = window.replace("%powered%", data.powered());
        window = window.replace("%door%", data.door());
        window = window.replace("%siege%", data.siege());
        StringBuilder travellers = new StringBuilder();
        if (!data.occupants().isEmpty()) {
            for (String o : data.occupants()) {
                travellers.append(o).append("<br />");
            }
        } else {
            travellers = new StringBuilder("Empty");
        }
        window = window.replace("%occupants%", travellers.toString());
        return window;
    }
}
