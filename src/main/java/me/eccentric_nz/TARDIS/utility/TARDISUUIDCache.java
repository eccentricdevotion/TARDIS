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
package me.eccentric_nz.TARDIS.utility;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.apache.commons.lang.Validate;

/**
 * A cache of username->UUID mappings that automatically cleans itself.
 *
 * This cache is meant to be used in plugins such that plugins can look up the
 * UUID of a player by using the name of the player.
 *
 * For the most part, when the plugin asks the cache for the UUID of an online
 * player, it should have it available immediately because the cache registers
 * itself for the player join/quit events and does background fetches.
 *
 * @author James Crasta
 *
 */
public class TARDISUUIDCache {

    private final TARDIS plugin;
    private final UUID ZERO_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final Map<String, UUID> cache = new ConcurrentHashMap<>();
    private final Map<UUID, String> nameCache = new ConcurrentHashMap<>();

    public TARDISUUIDCache(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the UUID from the cache for the player named 'name'.
     *
     * If the id does not exist in our database, then we will queue a fetch to
     * get it, and return null. A fetch at a later point will then be able to
     * return this id.
     *
     * @param name the player name to lookup
     * @return the player's UUID
     */
    public UUID getIdOptimistic(String name) {
        Validate.notEmpty(name);
        UUID uuid = cache.get(name);
        if (uuid == null) {
            ensurePlayerUUID(name);
            return null;
        }
        return uuid;
    }

    /**
     * Get the UUID from the cache for the player named 'name', with blocking
     * get.
     *
     * If the player named is not in the cache, then we will fetch the UUID in a
     * blocking fashion. Note that this will block the thread until the fetch is
     * complete, so only use this in a thread or in special circumstances.
     *
     * @param name The player name.
     * @return a UUID
     */
    public UUID getId(String name) {
        Validate.notEmpty(name);
        UUID uuid = cache.get(name);
        if (uuid == null) {
            syncFetch(nameList(name));
            return cache.get(name);
        } else if (uuid.equals(ZERO_UUID)) {
            uuid = null;
        }
        return uuid;
    }

    /**
     * Asynchronously fetch the name if it's not in our internal map.
     *
     * @param name The player's name
     */
    public void ensurePlayerUUID(String name) {
        if (cache.containsKey(name)) {
            return;
        }
        cache.put(name, ZERO_UUID);
        asyncFetch(nameList(name));
    }

    private void asyncFetch(final ArrayList<String> names) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            syncFetch(names);
        });
    }

    private void syncFetch(ArrayList<String> names) {
        final TARDISUUIDFetcher fetcher = new TARDISUUIDFetcher(names);
        try {
            cache.putAll(fetcher.call());
        } catch (Exception e) {
            plugin.debug("Error fetching UUID: " + e.getMessage());
        }
    }

    private ArrayList<String> nameList(String name) {
        ArrayList<String> names = new ArrayList<>();
        names.add(name);
        return names;
    }

    public Map<String, UUID> getCache() {
        return cache;
    }

    public Map<UUID, String> getNameCache() {
        return nameCache;
    }

    public UUID getZERO_UUID() {
        return ZERO_UUID;
    }
}
