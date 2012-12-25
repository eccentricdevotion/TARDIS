package me.eccentric_nz.TARDIS.database;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "doors")
public class Doors implements Serializable {

    @Id
    private int door_id;
    @NotNull
    private int tardis_id;
    @NotEmpty
    private String door_location;
    @NotEmpty
    private String door_direction;

    public int getDoor_id() {
        return door_id;
    }

    public void setDoor_id(int door_id) {
        this.door_id = door_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }

    public String getDoor_location() {
        return door_location;
    }

    public void setDoor_location(String door_location) {
        this.door_location = door_location;
    }

    public String getDoor_direction() {
        return door_direction;
    }

    public void setDoor_direction(String door_direction) {
        this.door_direction = door_direction;
    }
}
