/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.enumeration.SONIC_CONFIG;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class ConfiguredSonic {

    private final int sonic_id;
    private final UUID uuid;
    private SONIC_CONFIG bio;
    private SONIC_CONFIG diamond;
    private SONIC_CONFIG emerald;
    private SONIC_CONFIG redstone;
    private SONIC_CONFIG painter;
    private SONIC_CONFIG ignite;
    private SONIC_CONFIG arrow;
    private SONIC_CONFIG knockback;
    private final UUID sonic_uuid;

    public ConfiguredSonic(int sonic_id, UUID uuid, int bio, int diamond, int emerald, int redstone, int painter, int ignite, int arrow, int knockback, UUID sonic_uuid) {
        this.sonic_id = sonic_id;
        this.uuid = uuid;
        this.bio = SONIC_CONFIG.values()[bio];
        this.diamond = SONIC_CONFIG.values()[diamond];
        this.emerald = SONIC_CONFIG.values()[emerald];
        this.redstone = SONIC_CONFIG.values()[redstone];
        this.painter = SONIC_CONFIG.values()[painter];
        this.ignite = SONIC_CONFIG.values()[ignite];
        this.arrow = SONIC_CONFIG.values()[arrow];
        this.knockback = SONIC_CONFIG.values()[knockback];
        this.sonic_uuid = sonic_uuid;
    }

    public int getSonic_id() {
        return sonic_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public SONIC_CONFIG getBio() {
        return bio;
    }

    public void setBio(SONIC_CONFIG bio) {
        this.bio = bio;
    }

    public SONIC_CONFIG getDiamond() {
        return diamond;
    }

    public void setDiamond(SONIC_CONFIG diamond) {
        this.diamond = diamond;
    }

    public SONIC_CONFIG getEmerald() {
        return emerald;
    }

    public void setEmerald(SONIC_CONFIG emerald) {
        this.emerald = emerald;
    }

    public SONIC_CONFIG getRedstone() {
        return redstone;
    }

    public void setRedstone(SONIC_CONFIG redstone) {
        this.redstone = redstone;
    }

    public SONIC_CONFIG getPainter() {
        return painter;
    }

    public void setPainter(SONIC_CONFIG painter) {
        this.painter = painter;
    }

    public SONIC_CONFIG getIgnite() {
        return ignite;
    }

    public void setIgnite(SONIC_CONFIG ignite) {
        this.ignite = ignite;
    }

    public SONIC_CONFIG getArrow() {
        return arrow;
    }

    public void setArrow(SONIC_CONFIG arrow) {
        this.arrow = arrow;
    }

    public SONIC_CONFIG getKnockback() {
        return knockback;
    }

    public void setKnockback(SONIC_CONFIG knockback) {
        this.knockback = knockback;
    }

    public UUID getSonic_uuid() {
        return sonic_uuid;
    }
}
