package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;

import java.util.NavigableMap;
import java.util.TreeMap;

public class WeightedChoice<E> {

    private final NavigableMap<Double, E> map = new TreeMap<>();
    private double total = 0;

    public WeightedChoice<E> add(double weight, E result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = TARDISWeepingAngels.random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
