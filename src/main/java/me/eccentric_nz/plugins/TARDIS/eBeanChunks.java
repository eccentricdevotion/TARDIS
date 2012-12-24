package me.eccentric_nz.plugins.TARDIS;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "chunks")
public class eBeanChunks implements Serializable {

    @Id
    private int chunk_id;
    @NotNull
    private int tardis_id;
    @NotEmpty
    private String world;
    @NotEmpty
    private int x;
    @NotEmpty
    private int z;

    public int getChunk_id() {
        return chunk_id;
    }

    public void setChunk_id(int chunk_id) {
        this.chunk_id = chunk_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
