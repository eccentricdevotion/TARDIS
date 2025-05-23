package me.eccentric_nz.TARDIS;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

public class TARDISCache {

    public static LoadingCache<UUID, Tardis> BY_UUID;
    public static LoadingCache<Integer, Tardis> BY_ID;
    public static LoadingCache<Integer, Current> CURRENT;

    public void init() {
        BY_UUID = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(15))
                .build(TARDISCache::fromUUID);
        BY_ID = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(15))
                .build(TARDISCache::fromID);
        CURRENT = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(15))
                .build(TARDISCache::currentFromID);
    }

    public static Tardis fromUUID(UUID key) throws Exception {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", key.toString());
        ResultSetTardis rs = new ResultSetTardis(TARDIS.plugin, where, "", false);
        rs.resultSet();
        return rs.getTardis();
    }

    public static Tardis fromID(int key) throws Exception {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", key);
        ResultSetTardis rs = new ResultSetTardis(TARDIS.plugin, where, "", false);
        rs.resultSet();
        return rs.getTardis();
    }

    public static Current currentFromID(int key) throws Exception {
        ResultSetCurrentFromId rs = new ResultSetCurrentFromId(TARDIS.plugin, key);
        rs.resultSet();
        return rs.getCurrent();
    }

    public static void invalidate(UUID key) {
        Tardis tardis = BY_UUID.get(key);
        if (tardis != null) {
            BY_ID.invalidate(tardis.getTardisId());
        }
        BY_UUID.invalidate(key);
    }

    public static void invalidate(Integer key) {
        Tardis tardis = BY_ID.get(key);
        if (tardis != null) {
            try {
                BY_UUID.invalidate(UUID.fromString(tardis.getOwner()));
            } catch (IllegalArgumentException ignored) { }
        }
        BY_ID.invalidate(key);
    }

    public static void invalidateCurrent(Integer id) {
        CURRENT.invalidate(id);
    }
}
