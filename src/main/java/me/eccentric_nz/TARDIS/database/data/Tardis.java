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

import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
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
    private final String companions;
    private final PRESET preset;
    private final PRESET demat;
    private final ADAPTION adaption;
    private final int artron_level;
    private final String creeper;
    private final String condenser;
    private final String beacon;
    private final boolean handbrake_on;
    private final boolean tardis_init;
    private final boolean recharging;
    private final boolean hidden;
    private final long lastuse;
    private final boolean iso_on;
    private final String eps;
    private final String rail;
    private final String renderer;
    private final String zero;
    private final boolean powered_on;
    private final boolean lights_on;
    private final boolean siege_on;
    private final int monsters;

    public Tardis(int tardis_id, UUID uuid, String owner, String lastKnownName, String chunk, int tips, SCHEMATIC schematic, boolean abandoned, String companions, PRESET preset, PRESET demat, int adapt, int artron_level, String creeper, String condenser, String beacon, boolean handbrake_on, boolean tardis_init, boolean recharging, boolean hidden, long lastuse, boolean iso_on, String eps, String rail, String renderer, String zero, boolean powered_on, boolean lights_on, boolean siege_on, int monsters) {
        this.tardis_id = tardis_id;
        this.uuid = uuid;
        this.owner = owner;
        this.lastKnownName = lastKnownName;
        this.chunk = chunk;
        this.tips = tips;
        this.schematic = schematic;
        this.abandoned = abandoned;
        this.companions = companions;
        this.preset = preset;
        this.demat = demat;
        adaption = ADAPTION.values()[adapt];
        this.artron_level = artron_level;
        this.creeper = creeper;
        this.condenser = condenser;
        this.beacon = beacon;
        this.handbrake_on = handbrake_on;
        this.tardis_init = tardis_init;
        this.recharging = recharging;
        this.hidden = hidden;
        this.lastuse = lastuse;
        this.iso_on = iso_on;
        this.eps = eps;
        this.rail = rail;
        this.renderer = renderer;
        this.zero = zero;
        this.powered_on = powered_on;
        this.lights_on = lights_on;
        this.siege_on = siege_on;
        this.monsters = monsters;
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
     * Returns the UUID of the player who owns this TARDIS.
     *
     * @return the owners UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the name of the player who owns this TARDIS.
     *
     * @return the owners name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Returns the last known name of the player who owns this TARDIS.
     *
     * @return the owners last known name
     */
    public String getLastKnownName() {
        return lastKnownName;
    }

    /**
     * Returns a String representation of the Chunk coordinates that this TARDIS interior occupies. It is in the form of
     * {@code World name:chunkX:chunkZ}
     *
     * @return the Chunk string
     */
    public String getChunk() {
        return chunk;
    }

    /**
     * Returns the TARDIS Interior Positioning slot number of this TARDIS.
     *
     * @return the TIPS slot number, or -1 if the server is not using TIPS
     */
    public int getTIPS() {
        return tips;
    }

    /**
     * Returns the SCHEMATIC (desktop theme) this TARDIS is currently using.
     *
     * @return the SCHEMATIC
     */
    public SCHEMATIC getSchematic() {
        return schematic;
    }

    /**
     * Returns the abandoned status this TARDIS.
     *
     * @return true if abandoned, false if in use
     */
    public boolean isAbandoned() {
        return abandoned;
    }

    /**
     * Returns a colon (:) separated list of companion UUID strings for this TARDIS.
     *
     * @return the companion UUIDs, or an empty string if there are none
     */
    public String getCompanions() {
        if (companions.equalsIgnoreCase("everyone")) {
            StringBuilder sb = new StringBuilder();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                sb.append(p.getUniqueId().toString()).append(":");
            }
            return sb.substring(0, sb.length() - 1);
        } else {
            return companions;
        }
    }

    /**
     * Returns the exterior PRESET the Chameleon Circuit will use when next materialising.
     *
     * @return the exterior PRESET
     */
    public PRESET getPreset() {
        return preset;
    }

    /**
     * Returns the exterior PRESET the Chameleon Circuit will use when next dematerialising.
     *
     * @return the exterior PRESET
     */
    public PRESET getDemat() {
        return demat;
    }

    /**
     * Returns the Chameleon Circuit adaptive setting for this TARDIS. It will be one of BIOME, BLOCK or OFF.
     *
     * @return the Chameleon Circuit adaptive setting
     */
    public ADAPTION getAdaption() {
        return adaption;
    }

    /**
     * Returns the amount of Artron Energy this TARDIS has in its Artron Capacitor.
     *
     * @return the Artron Energy level
     */
    public int getArtron_level() {
        return artron_level;
    }

    /**
     * Returns a String representation of the Creeper spawn location in this TARDIS. It is in the form of {@code World
     * name:x:y:z}
     *
     * @return the Creeper spawn location string
     */
    public String getCreeper() {
        return creeper;
    }

    /**
     * Returns a String representation of the Artron Condenser chest location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Artron Condenser chest location string
     */
    public String getCondenser() {
        return condenser;
    }

    /**
     * Returns a String representation of the Beacon block-off block location in this TARDIS (used to turn off the
     * beacon). It is in the form of {@code World name:x:y:z}
     *
     * @return the Beacon block-off block location string
     */
    public String getBeacon() {
        return beacon;
    }

    /**
     * Returns whether the TARDIS handbrake is on or off.
     *
     * @return true if on, false if off
     */
    public boolean isHandbrake_on() {
        return handbrake_on;
    }

    /**
     * Returns whether the TARDIS has been initialized.
     *
     * @return true if initialized, false if not
     */
    public boolean isTardis_init() {
        return tardis_init;
    }

    /**
     * Returns whether the TARDIS is currently recharging.
     *
     * @return true if recharging, false if not
     */
    public boolean isRecharging() {
        return recharging;
    }

    /**
     * Returns whether the TARDIS is hidden.
     *
     * @return true if hidden, false if not
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Returns the time in milliseconds that the TARDIS was last used.
     *
     * @return the time in milliseconds, or if the player has the 'tardis.prune.bypass' permission Long.MAX_VALUE
     */
    public long getLastuse() {
        return lastuse;
    }

    /**
     * Returns whether the TARDIS Isomorphic circuit is on or off.
     *
     * @return true if on, false if off
     */
    public boolean isIso_on() {
        return iso_on;
    }

    /**
     * Returns a String representation of the Emergency Programme One hologram spawn location in this TARDIS. It is in
     * the form of {@code World name:x:y:z}
     *
     * @return the Emergency Programme One hologram spawn location string, or an empty string if the location does not
     * exist
     */
    public String getEps() {
        return eps;
    }

    /**
     * Returns a String representation of the Rail room minecart spawn location in this TARDIS. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Rail room minecart spawn location string, or an empty string if the room does not exist
     */
    public String getRail() {
        return rail;
    }

    /**
     * Returns a String representation of the Renderer room spawn location in this TARDIS. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Renderer room spawn location string, or an empty string if the room does not exist
     */
    public String getRenderer() {
        return renderer;
    }

    /**
     * Returns a String representation of the Zero room transmat location in this TARDIS. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Zero room transmat spawn location string, or an empty string if the room does not exist
     */
    public String getZero() {
        return zero;
    }

    /**
     * Returns whether the TARDIS is powered on.
     *
     * @return true if powered on, false if off
     */
    public boolean isPowered_on() {
        return powered_on;
    }

    /**
     * Returns whether the TARDIS lights are on or off.
     *
     * @return true if on, false if off
     */
    public boolean isLights_on() {
        return lights_on;
    }

    /**
     * Returns whether the TARDIS Siege Mode is on or off.
     *
     * @return true if Siege Mode is on, false if off
     */
    public boolean isSiege_on() {
        return siege_on;
    }

    /**
     * Returns the number of monsters that have spawned in this TARDIS.
     *
     * @return the number of monsters
     */
    public int getMonsters() {
        return monsters;
    }
}
