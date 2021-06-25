/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.database.data;

import me.eccentric_nz.tardis.enumeration.Adaption;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class Tardis {

    private final int tardisId;
    private final UUID uuid;
    private final String owner;
    private final String lastKnownName;
    private final String chunk;
    private final int tips;
    private final Schematic schematic;
    private final boolean abandoned;
    private final String companions;
    private final Preset preset;
    private final Preset demat;
    private final Adaption adaption;
    private final int artronLevel;
    private final String creeper;
    private final String beacon;
    private final boolean handbrakeOn;
    private final boolean tardisInit;
    private final boolean recharging;
    private final boolean hidden;
    private final long lastUse;
    private final boolean isoOn;
    private final String eps;
    private final String rail;
    private final String renderer;
    private final String zero;
    private final UUID rotor;
    private final boolean powered;
    private final boolean lightsOn;
    private final boolean siegeOn;
    private final int monsters;

    public Tardis(int tardisId, UUID uuid, String owner, String lastKnownName, String chunk, int tips, Schematic schematic, boolean abandoned, String companions, Preset preset, Preset demat, int adapt, int artronLevel, String creeper, String beacon, boolean handbrakeOn, boolean tardisInit, boolean recharging, boolean hidden, long lastUse, boolean isoOn, String eps, String rail, String renderer, String zero, UUID rotor, boolean powered, boolean lightsOn, boolean siegeOn, int monsters) {
        this.tardisId = tardisId;
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
        adaption = Adaption.values()[adapt];
        this.artronLevel = artronLevel;
        this.creeper = creeper;
        this.beacon = beacon;
        this.handbrakeOn = handbrakeOn;
        this.tardisInit = tardisInit;
        this.recharging = recharging;
        this.hidden = hidden;
        this.lastUse = lastUse;
        this.isoOn = isoOn;
        this.eps = eps;
        this.rail = rail;
        this.renderer = renderer;
        this.zero = zero;
        this.rotor = rotor;
        this.powered = powered;
        this.lightsOn = lightsOn;
        this.siegeOn = siegeOn;
        this.monsters = monsters;
    }

    /**
     * Returns the numerical id of this tardis.
     *
     * @return the tardis id
     */
    public int getTardisId() {
        return tardisId;
    }

    /**
     * Returns the UUID of the player who owns this tardis.
     *
     * @return the owners UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the name of the player who owns this tardis.
     *
     * @return the owners name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Returns the last known name of the player who owns this tardis.
     *
     * @return the owners last known name
     */
    public String getLastKnownName() {
        return lastKnownName;
    }

    /**
     * Returns a String representation of the Chunk coordinates that this tardis interior occupies. It is in the form of
     * {@code World name:chunkX:chunkZ}
     *
     * @return the Chunk string
     */
    public String getChunk() {
        return chunk;
    }

    /**
     * Returns the tardis Interior Positioning slot number of this tardis.
     *
     * @return the TIPS slot number, or -1 if the server is not using TIPS
     */
    public int getTips() {
        return tips;
    }

    /**
     * Returns the Schematic (desktop theme) this tardis is currently using.
     *
     * @return the Schematic
     */
    public Schematic getSchematic() {
        return schematic;
    }

    /**
     * Returns the abandoned status this tardis.
     *
     * @return true if abandoned, false if in use
     */
    public boolean isAbandoned() {
        return abandoned;
    }

    /**
     * Returns a colon (:) separated list of companion UUID strings for this tardis.
     *
     * @return the companion UUIDs, or an empty string if there are none
     */
    public String getCompanions() {
        if (companions.equalsIgnoreCase("everyone")) {
            StringBuilder sb = new StringBuilder();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                sb.append(p.getUniqueId()).append(":");
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
    public Preset getPreset() {
        return preset;
    }

    /**
     * Returns the exterior PRESET the Chameleon Circuit will use when next dematerialising.
     *
     * @return the exterior PRESET
     */
    public Preset getDemat() {
        return demat;
    }

    /**
     * Returns the Chameleon Circuit adaptive setting for this tardis. It will be one of BIOME, BLOCK or OFF.
     *
     * @return the Chameleon Circuit adaptive setting
     */
    public Adaption getAdaption() {
        return adaption;
    }

    /**
     * Returns the amount of Artron Energy this tardis has in its Artron Capacitor.
     *
     * @return the Artron Energy level
     */
    public int getArtronLevel() {
        return artronLevel;
    }

    /**
     * Returns a String representation of the Creeper spawn location in this tardis. It is in the form of {@code World
     * name:x:y:z}
     *
     * @return the Creeper spawn location string
     */
    public String getCreeper() {
        return creeper;
    }

    /**
     * Returns a String representation of the Beacon block-off block location in this tardis (used to turn off the
     * beacon). It is in the form of {@code World name:x:y:z}
     *
     * @return the Beacon block-off block location string
     */
    public String getBeacon() {
        return beacon;
    }

    /**
     * Returns whether the tardis handbrake is on or off.
     *
     * @return true if on, false if off
     */
    public boolean isHandbrakeOn() {
        return handbrakeOn;
    }

    /**
     * Returns whether the tardis has been initialized.
     *
     * @return true if initialized, false if not
     */
    public boolean isTardisInit() {
        return tardisInit;
    }

    /**
     * Returns whether the tardis is currently recharging.
     *
     * @return true if recharging, false if not
     */
    public boolean isRecharging() {
        return recharging;
    }

    /**
     * Returns whether the tardis is hidden.
     *
     * @return true if hidden, false if not
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Returns the time in milliseconds that the tardis was last used.
     *
     * @return the time in milliseconds, or if the player has the 'tardis.prune.bypass' permission Long.MAX_VALUE
     */
    public long getLastUse() {
        return lastUse;
    }

    /**
     * Returns whether the tardis Isomorphic circuit is on or off.
     *
     * @return true if on, false if off
     */
    public boolean isIsoOn() {
        return isoOn;
    }

    /**
     * Returns a String representation of the Emergency Programme One hologram spawn location in this tardis. It is in
     * the form of {@code World name:x:y:z}
     *
     * @return the Emergency Programme One hologram spawn location string, or an empty string if the location does not
     * exist
     */
    public String getEps() {
        return eps;
    }

    /**
     * Returns a String representation of the Rail room minecart spawn location in this tardis. It is in the form of
     * {@code World name:x:y:z}
     *
     * @return the Rail room minecart spawn location string, or an empty string if the room does not exist
     */
    public String getRail() {
        return rail;
    }

    /**
     * Returns a String representation of the Renderer room spawn location in this tardis. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Renderer room spawn location string, or an empty string if the room does not exist
     */
    public String getRenderer() {
        return renderer;
    }

    /**
     * Returns a String representation of the Zero room transmat location in this tardis. It is in the form of {@code
     * World name:x:y:z}
     *
     * @return the Zero room transmat spawn location string, or an empty string if the room does not exist
     */
    public String getZero() {
        return zero;
    }

    /**
     * Returns the UUID of the time rotor item frame in this tardis.
     *
     * @return the item frame UUID
     */
    public UUID getRotor() {
        return rotor;
    }

    /**
     * Returns whether the tardis is powered on.
     *
     * @return true if powered on, false if off
     */
    public boolean isPowered() {
        return powered;
    }

    /**
     * Returns whether the tardis lights are on or off.
     *
     * @return true if on, false if off
     */
    public boolean isLightsOn() {
        return lightsOn;
    }

    /**
     * Returns whether the tardis Siege Mode is on or off.
     *
     * @return true if Siege Mode is on, false if off
     */
    public boolean isSiegeOn() {
        return siegeOn;
    }

    /**
     * Returns the number of monsters that have spawned in this tardis.
     *
     * @return the number of monsters
     */
    public int getMonsters() {
        return monsters;
    }
}
