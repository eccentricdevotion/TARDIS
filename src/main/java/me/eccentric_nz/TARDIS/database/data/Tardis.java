/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.data;

import java.util.UUID;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;

/**
 *
 * @author eccentric_nz
 */
public class Tardis {

    private final int tardis_id;
    private final UUID uuid;
    private final String owner;
    private final String lastKnownName;
    private final String chunk;
    private final int tips;
    private final SCHEMATIC schematic;
    private final boolean abandoned;
    private final String replaced;
    private final String companions;
    private final String save_sign;
    private final String chameleon;
    private final boolean chamele_on;
    private final int chameleon_id;
    private final byte chameleon_data;
    private final PRESET preset;
    private final PRESET demat;
    private final boolean adapti_on;
    private final int artron_level;
    private final String creeper;
    private final String condenser;
    private final String beacon;
    private final boolean handbrake_on;
    private final boolean tardis_init;
    private final boolean recharging;
    private final String scanner;
    private final String farm;
    private final String stable;
    private final boolean hidden;
    private final long lastuse;
    private final boolean iso_on;
    private final String eps;
    private final String rail;
    private final String village;
    private final String renderer;
    private final String zero;
    private final String hutch;
    private final String igloo;
    private final boolean powered_on;
    private final boolean lights_on;
    private final boolean siege_on;
    private final int monsters;

    public Tardis(int tardis_id, UUID uuid, String owner, String lastKnownName, String chunk, int tips, SCHEMATIC schematic, boolean abandoned, String replaced, String companions, String save_sign, String chameleon, boolean chamele_on, int chameleon_id, byte chameleon_data, PRESET preset, PRESET demat, boolean adapti_on, int artron_level, String creeper, String condenser, String beacon, boolean handbrake_on, boolean tardis_init, boolean recharging, String scanner, String farm, String stable, boolean hidden, long lastuse, boolean iso_on, String eps, String rail, String village, String renderer, String zero, String hutch, String igloo, boolean powered_on, boolean lights_on, boolean siege_on, int monsters) {
        this.tardis_id = tardis_id;
        this.uuid = uuid;
        this.owner = owner;
        this.lastKnownName = lastKnownName;
        this.chunk = chunk;
        this.tips = tips;
        this.schematic = schematic;
        this.abandoned = abandoned;
        this.replaced = replaced;
        this.companions = companions;
        this.save_sign = save_sign;
        this.chameleon = chameleon;
        this.chamele_on = chamele_on;
        this.chameleon_id = chameleon_id;
        this.chameleon_data = chameleon_data;
        this.preset = preset;
        this.demat = demat;
        this.adapti_on = adapti_on;
        this.artron_level = artron_level;
        this.creeper = creeper;
        this.condenser = condenser;
        this.beacon = beacon;
        this.handbrake_on = handbrake_on;
        this.tardis_init = tardis_init;
        this.recharging = recharging;
        this.scanner = scanner;
        this.farm = farm;
        this.stable = stable;
        this.hidden = hidden;
        this.lastuse = lastuse;
        this.iso_on = iso_on;
        this.eps = eps;
        this.rail = rail;
        this.village = village;
        this.renderer = renderer;
        this.zero = zero;
        this.hutch = hutch;
        this.igloo = igloo;
        this.powered_on = powered_on;
        this.lights_on = lights_on;
        this.siege_on = siege_on;
        this.monsters = monsters;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getOwner() {
        return owner;
    }

    public String getLastKnownName() {
        return lastKnownName;
    }

    public String getChunk() {
        return chunk;
    }

    public int getTIPS() {
        return tips;
    }

    public SCHEMATIC getSchematic() {
        return schematic;
    }

    public boolean isAbandoned() {
        return abandoned;
    }

    public String getReplaced() {
        return replaced;
    }

    public String getCompanions() {
        return companions;
    }

    public String getSave_sign() {
        return save_sign;
    }

    public String getChameleon() {
        return chameleon;
    }

    public boolean isChamele_on() {
        return chamele_on;
    }

    public int getChameleon_id() {
        return chameleon_id;
    }

    public byte getChameleon_data() {
        return chameleon_data;
    }

    public PRESET getPreset() {
        return preset;
    }

    public PRESET getDemat() {
        return demat;
    }

    public boolean isAdapti_on() {
        return adapti_on;
    }

    public int getArtron_level() {
        return artron_level;
    }

    public String getCreeper() {
        return creeper;
    }

    public String getCondenser() {
        return condenser;
    }

    public String getBeacon() {
        return beacon;
    }

    public boolean isHandbrake_on() {
        return handbrake_on;
    }

    public boolean isTardis_init() {
        return tardis_init;
    }

    public boolean isRecharging() {
        return recharging;
    }

    public String getScanner() {
        return scanner;
    }

    public String getFarm() {
        return farm;
    }

    public String getStable() {
        return stable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public long getLastuse() {
        return lastuse;
    }

    public boolean isIso_on() {
        return iso_on;
    }

    public String getEps() {
        return eps;
    }

    public String getRail() {
        return rail;
    }

    public String getVillage() {
        return village;
    }

    public String getRenderer() {
        return renderer;
    }

    public String getZero() {
        return zero;
    }

    public String getHutch() {
        return hutch;
    }

    public String getIgloo() {
        return igloo;
    }

    public boolean isPowered_on() {
        return powered_on;
    }

    public boolean isLights_on() {
        return lights_on;
    }

    public boolean isSiege_on() {
        return siege_on;
    }

    public int getMonsters() {
        return monsters;
    }
}
