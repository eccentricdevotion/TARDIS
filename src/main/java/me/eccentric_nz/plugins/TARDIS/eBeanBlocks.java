package me.eccentric_nz.plugins.TARDIS;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "block")
public class eBeanBlocks implements Serializable {

    @Id
    private int block_id;
    @NotNull
    private int tardis_id;
    @NotEmpty
    private String location;
    @NotEmpty
    private int block;
    @NotEmpty
    private int data;

    public int getBlock_id() {
        return block_id;
    }

    public void setBlock_id(int block_id) {
        this.block_id = block_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
