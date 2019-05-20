/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum CONTROL {

    HANDBRAKE(0, "handbrake"),
    RANDOM(1, "button"),
    WORLD(2, "world-repeater"),
    X(3, "x-repeater"),
    Y(4, "z-repeater"),
    MULITPLIER(5, "y-repeater"),
    ARTRON(6, "artron"),
    KEYBOARD(7, "keyboard"),
    BACK(8, "back"),
    TERMINAL(9, "terminal"),
    ARS(10, "ars"),
    TEMPORAL(11, "temporal"),
    LIGHT_SWITCH(12, "light"),
    TARDIS_INFOMATION_SYSTEM(13, "info"),
    DISK_CIRCUIT_STORAGE(14, "storage"),
    ADVANCED_CONSOLE(15, "advanced"),
    ZERO_ENTRY(16, "zero"),
    ZERO_EXIT(17, ""),
    DIRECTION(18, ""),
    LAZARUS_DEVICE(20, ""),
    DOOR_TOGGLE_WOOL(20, "toggle_wool"),
    SIEGE(21, "siege"),
    CONTROLS(22, "control"),
    TELEPATHIC_CIRCUIT(23, "telepathic"),
    SONIC_GENERATOR(24, "generator"),
    SHELL(25, ""),
    HANDLES(26, "handles"),
    FRAME(27, "");

    private final int id;
    private final String update;
    private static final HashMap<String, Integer> UPDATE_CONTROLS = new HashMap<>();

    CONTROL(int id, String update) {
        this.id = id;
        this.update = update;
    }

    static {
        for (CONTROL control : values()) {
            if (!control.update.isEmpty()) {
                UPDATE_CONTROLS.put(control.update, control.id);
            }
        }
    }

    public int getId() {
        return id;
    }

    public static HashMap<String, Integer> getUPDATE_CONTROLS() {
        return UPDATE_CONTROLS;
    }}
