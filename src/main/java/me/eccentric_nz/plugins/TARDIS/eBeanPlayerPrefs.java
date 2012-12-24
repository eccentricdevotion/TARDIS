package me.eccentric_nz.plugins.TARDIS;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "player_prefs")
public class eBeanPlayerPrefs implements Serializable {

    @Id
    private int pp_id;
    @NotNull
    private String player;
    @NotEmpty
    private int sfx_on;
    @NotEmpty
    private int platform_on;
    @NotEmpty
    private int quotes_on;

    public int getPp_id() {
        return pp_id;
    }

    public void setPp_id(int pp_id) {
        this.pp_id = pp_id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getSfx_on() {
        return sfx_on;
    }

    public void setSfx_on(int sfx_on) {
        this.sfx_on = sfx_on;
    }

    public int getPlatform_on() {
        return platform_on;
    }

    public void setPlatform_on(int platform_on) {
        this.platform_on = platform_on;
    }

    public int getQuotes_on() {
        return quotes_on;
    }

    public void setQuotes_on(int quotes_on) {
        this.quotes_on = quotes_on;
    }
}