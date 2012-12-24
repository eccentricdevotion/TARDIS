package me.eccentric_nz.plugins.TARDIS;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "areas")
public class eBeanAreas implements Serializable {

    @Id
    private int area_id;
    @NotNull
    private String area_name;
    @NotEmpty
    private String world;
    @NotEmpty
    private String location;
    @NotEmpty
    private int minx;
    @NotEmpty
    private int minz;
    @NotEmpty
    private int maxx;
    @NotEmpty
    private int maxz;

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinx() {
        return minx;
    }

    public void setMinx(int minx) {
        this.minx = minx;
    }

    public int getMinz() {
        return minz;
    }

    public void setMinz(int minz) {
        this.minz = minz;
    }

    public int getMaxx() {
        return maxx;
    }

    public void setMaxx(int maxx) {
        this.maxx = maxx;
    }

    public int getMaxz() {
        return maxz;
    }

    public void setMaxz(int maxz) {
        this.maxz = maxz;
    }
}
