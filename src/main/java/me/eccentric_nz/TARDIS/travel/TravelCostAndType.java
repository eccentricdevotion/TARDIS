package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.enumeration.TravelType;

public class TravelCostAndType {

    private final int cost;
    private final TravelType travelType;

    public TravelCostAndType(int cost, TravelType travelType) {
        this.cost = cost;
        this.travelType = travelType;
    }

    public int getCost() {
        return cost;
    }

    public TravelType getTravelType() {
        return travelType;
    }
}
