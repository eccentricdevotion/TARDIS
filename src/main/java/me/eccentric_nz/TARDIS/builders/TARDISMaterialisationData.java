/*
 * Copyright (C) 2014 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;

/**
 * Data class for building the TARDIS exterior.
 *
 * @author eccentric_nz
 */
public class TARDISMaterialisationData {

    private final TARDIS plugin;
    private final String uuid;
    private COMPASS direction;
    private Location location;
    private OfflinePlayer player;
    private boolean chameleon;
    private boolean dematerialise;
    private boolean hide;
    private boolean malfunction;
    private boolean outside;
    private boolean rebuild;
    private boolean submarine;
    private Material lamp = Material.TORCH;
    private boolean texture = true;
    private boolean minecartSounds = false;
    private boolean CTM = true;
    private boolean addSign = true;
    private int tardisID;
    private Biome biome;
    private int distance;
    private String resetWorld;
    private Location fromLocation;

    public TARDISMaterialisationData(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        // get player preferences
        setPlayerDefaults(this.uuid);
    }

    public COMPASS getDirection() {
        return direction;
    }

    public void setDirection(COMPASS direction) {
        this.direction = direction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public boolean isChameleon() {
        return chameleon;
    }

    public void setChameleon(boolean chameleon) {
        this.chameleon = chameleon;
    }

    public Material getLamp() {
        return lamp;
    }

    public void setLamp(Material lamp) {
        this.lamp = lamp;
    }

    public boolean isDematerialise() {
        return dematerialise;
    }

    public void setDematerialise(boolean dematerialise) {
        this.dematerialise = dematerialise;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isMalfunction() {
        return malfunction;
    }

    public void setMalfunction(boolean malfunction) {
        this.malfunction = malfunction;
    }

    public boolean isOutside() {
        return outside;
    }

    public void setOutside(boolean outside) {
        this.outside = outside;
    }

    public boolean isRebuild() {
        return rebuild;
    }

    public void setRebuild(boolean rebuild) {
        this.rebuild = rebuild;
    }

    public boolean isSubmarine() {
        return submarine;
    }

    public void setSubmarine(boolean submarine) {
        this.submarine = submarine;
    }

    public int getTardisID() {
        return tardisID;
    }

    public void setTardisID(int tardisID) {
        this.tardisID = tardisID;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public boolean useTexture() {
        return texture;
    }

    public void setTexture(boolean texture) {
        this.texture = texture;
    }

    public boolean useMinecartSounds() {
        return minecartSounds;
    }

    public void setMinecartSounds(boolean minecartSounds) {
        this.minecartSounds = minecartSounds;
    }

    public boolean shouldUseCTM() {
        return CTM;
    }

    public void setCTM(boolean CTM) {
        this.CTM = CTM;
    }

    public boolean shouldAddSign() {
        return addSign;
    }

    public void setAddSign(boolean addSign) {
        this.addSign = addSign;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getResetWorld() {
        return resetWorld;
    }

    public void setResetWorld(String resetWorld) {
        this.resetWorld = resetWorld;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    private void setPlayerDefaults(String uuid) {
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", uuid);
        final ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
        if (rsp.resultSet()) {
            lamp = rsp.getLamp();
            texture = rsp.isPoliceboxTexturesOn();
            addSign = rsp.isSignOn();
            CTM = rsp.isCtmOn();
            minecartSounds = rsp.isMinecartOn();
        }
    }
}
