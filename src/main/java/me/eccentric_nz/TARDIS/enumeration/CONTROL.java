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
package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum CONTROL {

    HANDBRAKE(0, "handbrake", false, true),
    RANDOM(1, "button", false, false),
    WORLD(2, "world-repeater", false, false),
    X(3, "x-repeater", false, false),
    Z(4, "z-repeater", false, false),
    MULITPLIER(5, "y-repeater", false, false),
    ARTRON(6, "artron", false, false),
    KEYBOARD(7, "keyboard", false, false),
    BACK(8, "back", false, false),
    TERMINAL(9, "terminal", false, false),
    ARS(10, "ars", false, true),
    TEMPORAL(11, "temporal", false, false),
    LIGHT_SWITCH(12, "light", false, true),
    TARDIS_INFOMATION_SYSTEM(13, "info", true, false),
    DISK_CIRCUIT_STORAGE(14, "storage", false, false),
    ADVANCED_CONSOLE(15, "advanced", false, false),
    ZERO_ENTRY(16, "zero", false, true),
    ZERO_EXIT(17, "", true, false),
    DIRECTION(18, "direction", false, false),
    LAZARUS_DEVICE(19, "", false, true),
    DOOR_TOGGLE_WOOL(20, "toggle_wool", false, true),
    SIEGE(21, "siege", false, false),
    CONTROLS(22, "control", true, false),
    TELEPATHIC_CIRCUIT(23, "telepathic", false, false),
    SONIC_GENERATOR(24, "generator", false, true),
    SHELL(25, "shell", false, true),
    HANDLES(26, "handles", false, false),
    FRAME(27, "frame", false, false),
    DISPENSER(28, "dispenser", false, false),
    FORCE_FIELD(29, "forcefield", false, true),
    FLIGHT(30, "flight", false, false),
    CHAMELEON(31, "chameleon", false, false),
    SAVES(32, "save-sign", false, false),
    SCANNER(33, "scanner", false, false),
    CONDENSER(34, "condenser", false, false),
    CLOISTER_BELL(35, "bell", true, false);

    private final int id;
    private final String update;
    private final boolean unpowered;
    private final boolean noSiege;
    private static final HashMap<String, Integer> UPDATE_CONTROLS = new HashMap<>();
    private static final HashMap<Integer, CONTROL> BY_ID = new HashMap<>();

    CONTROL(int id, String update, boolean unpowered, boolean noSiege) {
        this.id = id;
        this.update = update;
        this.unpowered = unpowered;
        this.noSiege = noSiege;
    }

    static {
        for (CONTROL control : values()) {
            if (!control.update.isEmpty()) {
                UPDATE_CONTROLS.put(control.update, control.id);
            }
            BY_ID.put(control.id, control);
        }
    }

    public int getId() {
        return id;
    }

    public boolean allowUnpowered() {
        return unpowered;
    }

    public boolean isNoSiege() {
        return noSiege;
    }

    public static HashMap<String, Integer> getUPDATE_CONTROLS() {
        return UPDATE_CONTROLS;
    }

    public static HashMap<Integer, CONTROL> getById() {
        return BY_ID;
    }
}
