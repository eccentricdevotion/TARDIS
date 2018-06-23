package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.database.data.Area;

public class TARDISAreaCheck {

    private final Area area;
    private final boolean inArea;

    public TARDISAreaCheck(Area area, boolean inArea) {
        this.area = area;
        this.inArea = inArea;
    }

    public Area getArea() {
        return area;
    }

    public boolean isInArea() {
        return inArea;
    }
}
