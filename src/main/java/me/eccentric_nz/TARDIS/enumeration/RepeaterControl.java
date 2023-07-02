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
package me.eccentric_nz.TARDIS.enumeration;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author eccentric_nz
 */
public enum RepeaterControl {

    WORLD(2, Arrays.asList("Current world", "Normal world", "Nether world", "End world")),
    X(3, Arrays.asList("1x X-distance", "2x X-distance", "3x X-distance", "4x X-distance")),
    Z(4, Arrays.asList("1x Z-distance", "2x Z-distance", "3x Z-distance", "4x Z-distance")),
    MULTIPLIER(5, Arrays.asList("1x multiplier", "2x multiplier", "3x multiplier", "4x multiplier"));

private int control;
    private List<String> descriptions;

    private RepeaterControl(int control, List<String> descriptions) {
        this.control = control;
        this.descriptions = descriptions;
    }

    public int getControl() {
        return control;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public static RepeaterControl getControl(int c) {
        return switch (c) {
            case 2 -> WORLD;
            case 3 -> X;
            case 4 -> Z;
            default -> MULTIPLIER;
        };
    }
}
