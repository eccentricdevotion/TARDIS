package me.eccentric_nz.tardisshop;

import org.bukkit.Location;

public class TARDISShopItem {

    private final int id;
    private final String item;
    private final Location location;
    private final double cost;

    public TARDISShopItem(int id, String item, Location location, double cost) {
        this.id = id;
        this.item = item;
        this.location = location;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public Location getLocation() {
        return location;
    }

    public double getCost() {
        return cost;
    }
}
