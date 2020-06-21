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

import me.eccentric_nz.TARDIS.enumeration.SonicConfig;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class ConfiguredSonic {

    private final int sonic_id;
    private final UUID uuid;
    private SonicConfig bio;
    private SonicConfig diamond;
    private SonicConfig emerald;
    private SonicConfig redstone;
    private SonicConfig painter;
    private SonicConfig ignite;
    private SonicConfig arrow;
    private SonicConfig knockback;
    private final UUID sonic_uuid;

    public ConfiguredSonic(int sonic_id, UUID uuid, int bio, int diamond, int emerald, int redstone, int painter, int ignite, int arrow, int knockback, UUID sonic_uuid) {
        this.sonic_id = sonic_id;
        this.uuid = uuid;
        this.bio = SonicConfig.values()[bio];
        this.diamond = SonicConfig.values()[diamond];
        this.emerald = SonicConfig.values()[emerald];
        this.redstone = SonicConfig.values()[redstone];
        this.painter = SonicConfig.values()[painter];
        this.ignite = SonicConfig.values()[ignite];
        this.arrow = SonicConfig.values()[arrow];
        this.knockback = SonicConfig.values()[knockback];
        this.sonic_uuid = sonic_uuid;
    }

    public int getSonic_id() {
        return sonic_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public SonicConfig getBio() {
        return bio;
    }

    public void setBio(SonicConfig bio) {
        this.bio = bio;
    }

    public SonicConfig getDiamond() {
        return diamond;
    }

    public void setDiamond(SonicConfig diamond) {
        this.diamond = diamond;
    }

    public SonicConfig getEmerald() {
        return emerald;
    }

    public void setEmerald(SonicConfig emerald) {
        this.emerald = emerald;
    }

    public SonicConfig getRedstone() {
        return redstone;
    }

    public void setRedstone(SonicConfig redstone) {
        this.redstone = redstone;
    }

    public SonicConfig getPainter() {
        return painter;
    }

    public void setPainter(SonicConfig painter) {
        this.painter = painter;
    }

    public SonicConfig getIgnite() {
        return ignite;
    }

    public void setIgnite(SonicConfig ignite) {
        this.ignite = ignite;
    }

    public SonicConfig getArrow() {
        return arrow;
    }

    public void setArrow(SonicConfig arrow) {
        this.arrow = arrow;
    }

    public SonicConfig getKnockback() {
        return knockback;
    }

    public void setKnockback(SonicConfig knockback) {
        this.knockback = knockback;
    }

    public UUID getSonic_uuid() {
        return sonic_uuid;
    }
}
