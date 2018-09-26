/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.planets;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * @param <String> the filename
 * @author eccentric_nz
 */
public class TARDISRandomCollection<String> {

    private final NavigableMap<Double, String> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    TARDISRandomCollection() {
        this(new Random());
    }

    private TARDISRandomCollection(Random random) {
        this.random = random;
    }

    public TARDISRandomCollection<String> add(double weight, String result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public String next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
