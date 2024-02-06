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

    public int getTardis_id() {
        return tardis_id;
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
