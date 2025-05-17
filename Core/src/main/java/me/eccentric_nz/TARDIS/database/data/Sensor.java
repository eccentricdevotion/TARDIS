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
package me.eccentric_nz.TARDIS.database.data;

public class Sensor {

    private final int tardis_id;
    private final String charging;
    private final String flight;
    private final String handbrake;
    private final String malfunction;
    private final String power;

    public Sensor(int tardis_id, String charging, String flight, String handbrake, String malfunction, String power) {
        this.tardis_id = tardis_id;
        this.charging = charging;
        this.flight = flight;
        this.handbrake = handbrake;
        this.malfunction = malfunction;
        this.power = power;
    }

    public String getCharging() {
        return charging;
    }

    public String getFlight() {
        return flight;
    }

    public String getHandbrake() {
        return handbrake;
    }

    public String getMalfunction() {
        return malfunction;
    }

    public String getPower() {
        return power;
    }
}
