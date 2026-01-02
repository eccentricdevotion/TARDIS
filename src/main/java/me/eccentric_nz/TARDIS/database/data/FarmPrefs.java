/*
 * Copyright (C) 2026 eccentric_nz
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

/**
 * @author eccentric_nz
 */
public class FarmPrefs {

    private final UUID uuid;
    private final boolean allay;
    private final boolean apiary;
    private final boolean aquarium;
    private final boolean bamboo;
    private final boolean birdcage;
    private final boolean farm;
    private final boolean geode;
    private final boolean happy;
    private final boolean hutch;
    private final boolean igloo;
    private final boolean iistubil;
    private final boolean mangrove;
    private final boolean nautilis;
    private final boolean lava;
    private final boolean pen;
    private final boolean stable;
    private final boolean stall;
    private final boolean village;

    public FarmPrefs(UUID uuid, boolean allay, boolean apiary, boolean aquarium, boolean bamboo, boolean birdcage, boolean farm, boolean geode, boolean happy, boolean hutch, boolean igloo, boolean iistubil, boolean lava, boolean mangrove, boolean nautilus, boolean pen, boolean stable, boolean stall, boolean village) {
        this.uuid = uuid;
        this.apiary = apiary;
        this.allay = allay;
        this.aquarium = aquarium;
        this.bamboo = bamboo;
        this.birdcage = birdcage;
        this.farm = farm;
        this.geode = geode;
        this.happy = happy;
        this.hutch = hutch;
        this.igloo = igloo;
        this.iistubil = iistubil;
        this.lava = lava;
        this.mangrove = mangrove;
        this.nautilis = nautilus;
        this.pen = pen;
        this.stable = stable;
        this.stall = stall;
        this.village = village;
    }

    /**
     * Returns the unique id of this player.
     *
     * @return a unique id
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns if the player wants to farm into the Allay room.
     *
     * @return true or false
     */
    public boolean shouldFarmAllay() {
        return allay;
    }

    /**
     * Returns if the player wants to farm into the Apiary room.
     *
     * @return true or false
     */
    public boolean shouldFarmBees() {
        return apiary;
    }

    /**
     * Returns if the player wants to farm into the Aquarium room.
     *
     * @return true or false
     */
    public boolean shouldFarmFish() {
        return aquarium;
    }

    /**
     * Returns if the player wants to farm into the Bamboo room.
     *
     * @return true or false
     */
    public boolean shouldFarmPandas() {
        return bamboo;
    }

    /**
     * Returns if the player wants to farm into the Birdcage room.
     *
     * @return true or false
     */
    public boolean shouldFarmParrots() {
        return birdcage;
    }

    /**
     * Returns if the player wants to farm into the Farm room.
     *
     * @return true or false
     */
    public boolean shouldFarmLivestock() {
        return farm;
    }

    /**
     * Returns if the player wants to farm into the Geode room.
     *
     * @return true or false
     */
    public boolean shouldFarmAxolotls() {
        return geode;
    }

    /**
     * Returns if the player wants to farm into the Happy room.
     *
     * @return true or false
     */
    public boolean shouldFarmHappyGhasts() {
        return happy;
    }

    /**
     * Returns if the player wants to farm into the Hutch room.
     *
     * @return true or false
     */
    public boolean shouldFarmRabbits() {
        return hutch;
    }

    /**
     * Returns if the player wants to farm into the Igloo room.
     *
     * @return true or false
     */
    public boolean shouldFarmPolarBears() {
        return igloo;
    }

    /**
     * Returns if the player wants to farm into the Iistubil room.
     *
     * @return true or false
     */
    public boolean shouldFarmCamels() {
        return iistubil;
    }

    /**
     * Returns if the player wants to farm into the Lava room.
     *
     * @return true or false
     */
    public boolean shouldFarmStriders() {
        return lava;
    }

    /**
     * Returns if the player wants to farm into the Mangrove room.
     *
     * @return true or false
     */
    public boolean shouldFarmFrogs() {
        return mangrove;
    }

    /**
     * Returns if the player wants to farm into the Nautilus room.
     *
     * @return true or false
     */
    public boolean shouldFarmNautili() {
        return nautilis;
    }

    /**
     * Returns if the player wants to farm into the Pen room.
     *
     * @return true or false
     */
    public boolean shouldFarmSniffers() {
        return pen;
    }

    /**
     * Returns if the player wants to farm into the Stable room.
     *
     * @return true or false
     */
    public boolean shouldFarmHorses() {
        return stable;
    }

    /**
     * Returns if the player wants to farm into the Stall room.
     *
     * @return true or false
     */
    public boolean shouldFarmLlamas() {
        return stall;
    }

    /**
     * Returns if the player wants to farm into the Village room.
     *
     * @return true or false
     */
    public boolean shouldFarmVillagers() {
        return village;
    }
}
