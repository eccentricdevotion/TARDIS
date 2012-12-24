/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.eccentric_nz.plugins.TARDIS;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "tardis")
public class eBeanTardis implements Serializable {

    @Id
    private int tardis_id;
    @NotNull
    private String owner;
    @NotEmpty
    private String chunk;
    @NotEmpty
    private String direction;
    @NotEmpty
    private String schematic;
    @NotEmpty
    private String home;
    @NotEmpty
    private String save;
    @NotEmpty
    private String current_loc;
    @NotEmpty
    private String replaced;
    @NotEmpty
    private String chest;
    @NotEmpty
    private String button;
    @NotEmpty
    private String repeater0;
    @NotEmpty
    private String repeater1;
    @NotEmpty
    private String repeater2;
    @NotEmpty
    private String repeater3;
    private String companions;
    private String platform;
    private String save_sign;
    private String chameleon;
    private int chameleon_on;

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getChunk() {
        return chunk;
    }

    public void setChunk(String chunk) {
        this.chunk = chunk;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSchematic() {
        return schematic;
    }

    public void setSchematic(String schematic) {
        this.schematic = schematic;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getCurrent_loc() {
        return current_loc;
    }

    public void setCurrent_loc(String current_loc) {
        this.current_loc = current_loc;
    }

    public String getReplaced() {
        return replaced;
    }

    public void setReplaced(String replaced) {
        this.replaced = replaced;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getRepeater0() {
        return repeater0;
    }

    public void setRepeater0(String repeater0) {
        this.repeater0 = repeater0;
    }

    public String getRepeater1() {
        return repeater1;
    }

    public void setRepeater1(String repeater1) {
        this.repeater1 = repeater1;
    }

    public String getRepeater2() {
        return repeater2;
    }

    public void setRepeater2(String repeater2) {
        this.repeater2 = repeater2;
    }

    public String getRepeater3() {
        return repeater3;
    }

    public void setRepeater3(String repeater3) {
        this.repeater3 = repeater3;
    }

    public String getCompanions() {
        return companions;
    }

    public void setCompanions(String companions) {
        this.companions = companions;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSave_sign() {
        return save_sign;
    }

    public void setSave_sign(String save_sign) {
        this.save_sign = save_sign;
    }

    public String getChameleon() {
        return chameleon;
    }

    public void setChameleon(String chameleon) {
        this.chameleon = chameleon;
    }

    public int getChameleon_on() {
        return chameleon_on;
    }

    public void setChameleon_on(int chameleon_on) {
        this.chameleon_on = chameleon_on;
    }
}
