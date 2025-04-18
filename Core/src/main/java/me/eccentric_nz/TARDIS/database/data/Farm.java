/*
 * Copyright (C) 2025 eccentric_nz
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

/**
 * @author eccentric_nz
 */
public class Farm {

    private final int tardis_id;
    private final String allay;
    private final String apiary;
    private final String aquarium;
    private final String bamboo;
    private final String birdcage;
    private final String farm;
    private final String geode;
    private final String hutch;
    private final String igloo;
    private final String iistubil;
    private final String mangrove;
    private final String lava;
    private final String pen;
    private final String stable;
    private final String stall;
    private final String village;

    public Farm(int tardis_id, String allay, String apiary, String aquarium, String bamboo, String birdcage, String farm, String geode, String hutch, String igloo, String iistubil, String lava, String mangrove, String pen, String stable, String stall, String village) {
        this.tardis_id = tardis_id;
        this.apiary = apiary;
        this.allay = allay;
        this.aquarium = aquarium;
        this.bamboo = bamboo;
        this.birdcage = birdcage;
        this.farm = farm;
        this.geode = geode;
        this.hutch = hutch;
        this.igloo = igloo;
        this.iistubil = iistubil;
        this.lava = lava;
        this.mangrove = mangrove;
        this.pen = pen;
        this.stable = stable;
        this.stall = stall;
        this.village = village;
    }

    /**
     * Returns the numerical id of this TARDIS.
     *
     * @return the TARDIS id
     */
    public int getTardis_id() {
        return tardis_id;
    }

    /**
     * Returns a String representation of the Allay room spawn location in this TARDIS. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Allay room spawn location string, or an empty string if the room does not exist
     */
    public String getAllay() {
        return allay;
    }

    /**
     * Returns a String representation of the Apiary room bee spawn location in this TARDIS. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Apiary room bee spawn location string, or an empty string if the room does not exist
     */
    public String getApiary() {
        return apiary;
    }

    /**
     * Returns a String representation of the Aquarium room fish spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Aquarium room fish spawn location string, or an empty string if the room does not exist
     */
    public String getAquarium() {
        return aquarium;
    }

    /**
     * Returns a String representation of the Bamboo room panda spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Bamboo room panda spawn location string, or an empty string if the room does not exist
     */
    public String getBamboo() {
        return bamboo;
    }

    /**
     * Returns a String representation of the Birdcage room parrot spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Birdcage room parrot spawn location string, or an empty string if the room does not exist
     */
    public String getBirdcage() {
        return birdcage;
    }

    /**
     * Returns a String representation of the Farm room mob spawn location in this TARDIS. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Farm room mob spawn location string, or an empty string if the room does not exist
     */
    public String getFarm() {
        return farm;
    }

    /**
     * Returns a String representation of the Geode room mob spawn location in this TARDIS. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Geode room mob spawn location string, or an empty string if the room does not exist
     */
    public String getGeode() {
        return geode;
    }

    /**
     * Returns a String representation of the Hutch room rabbit spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Hutch room rabbit spawn location string, or an empty string if the room does not exist
     */
    public String getHutch() {
        return hutch;
    }

    /**
     * Returns a String representation of the Igloo room polar bear spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Igloo room polar bear spawn location string, or an empty string if the room does not exist
     */
    public String getIgloo() {
        return igloo;
    }

    /**
     * Returns a String representation of the Iistubil room camel spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Iistubil room camel spawn location string, or an empty string if the room does not exist
     */
    public String getIistubil() {
        return iistubil;
    }

    /**
     * Returns a String representation of the Lava room srtrider spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Lava room strider spawn location string, or an empty string if the room does not exist
     */
    public String getLava() {
        return lava;
    }

    /**
     * Returns a String representation of the Mangrove room frog spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Mangrove room frog spawn location string, or an empty string if the room does not exist
     */
    public String getMangrove() {
        return mangrove;
    }

    /**
     * Returns a String representation of the Pen room sniffer spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Pen room sniffer spawn location string, or an empty string if the room does not exist
     */
    public String getPen() {
        return pen;
    }

    /**
     * Returns a String representation of the Stable room horse spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Stable room horse spawn location string, or an empty string if the room does not exist
     */
    public String getStable() {
        return stable;
    }

    /**
     * Returns a String representation of the Stall room llama spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Stall room llama spawn location string, or an empty string if the room does not exist
     */
    public String getStall() {
        return stall;
    }

    /**
     * Returns a String representation of the Village room villager spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Village room villager spawn location string, or an empty string if the room does not exist
     */
    public String getVillage() {
        return village;
    }
}
