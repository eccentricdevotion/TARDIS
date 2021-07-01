package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.enumeration.TravelType;

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