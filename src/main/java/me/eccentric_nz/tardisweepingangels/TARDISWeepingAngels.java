/*
 * Copyright (C) 2023 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.commands.TARDISWeepingAngelsCommand;
import me.eccentric_nz.tardisweepingangels.commands.TabComplete;
import me.eccentric_nz.tardisweepingangels.death.Death;
import me.eccentric_nz.tardisweepingangels.death.PlayerDeath;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import me.eccentric_nz.tardisweepingangels.equip.PlayerUndisguise;
import me.eccentric_nz.tardisweepingangels.monsters.cybermen.CybermanRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekGlideListener;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.GasMask;
import me.eccentric_nz.tardisweepingangels.monsters.hath.HathRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessProjectileListener;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessTarget;
import me.eccentric_nz.tardisweepingangels.monsters.ice_warriors.IceWarriorRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.*;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Builder;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Listener;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Recipe;
import me.eccentric_nz.tardisweepingangels.monsters.mire.MireRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodListener;
import me.eccentric_nz.tardisweepingangels.monsters.ood.VillagerCuredListener;
import me.eccentric_nz.tardisweepingangels.monsters.ood.VillagerSpawnListener;
import me.eccentric_nz.tardisweepingangels.monsters.racnoss.RacnossRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.sea_devils.SeaDevilRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.silent.CleanGuardians;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.silurians.SilurianRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.silurians.SilurianSpawnerListener;
import me.eccentric_nz.tardisweepingangels.monsters.slitheen.SlitheenRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.sontarans.SontaranRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.BeeSpawnListener;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneListener;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.vashta_nerada.VashtaNeradaListener;
import me.eccentric_nz.tardisweepingangels.monsters.weeping_angels.*;
import me.eccentric_nz.tardisweepingangels.monsters.zygons.ZygonRunnable;
import me.eccentric_nz.tardisweepingangels.utils.*;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TARDISWeepingAngels {

    public static final HashMap<Monster, NamespacedKey> PDC_KEYS = new HashMap<>();
    private static final List<UUID> empty = new ArrayList<>();
    private static final List<UUID> timesUp = new ArrayList<>();
    private static final List<UUID> guards = new ArrayList<>();
    private static final List<UUID> playersWithGuards = new ArrayList<>();
    public static NamespacedKey ANGEL;
    public static NamespacedKey CYBERMAN;
    public static NamespacedKey DALEK;
    public static NamespacedKey DALEK_SEC;
    public static NamespacedKey DAVROS;
    public static NamespacedKey DEVIL;
    public static NamespacedKey EMPTY;
    public static NamespacedKey HATH;
    public static NamespacedKey JUDOON;
    public static NamespacedKey K9;
    public static NamespacedKey MIRE;
    public static NamespacedKey MONK;
    public static NamespacedKey FLAME_TASK;
    public static NamespacedKey HEADLESS_TASK;
    public static NamespacedKey OOD;
    public static NamespacedKey OWNER_UUID;
    public static NamespacedKey FOLLOW;
    public static NamespacedKey RACNOSS;
    public static NamespacedKey SILENT;
    public static NamespacedKey SILURIAN;
    public static NamespacedKey SLITHEEN;
    public static NamespacedKey SONTARAN;
    public static NamespacedKey STRAX;
    public static NamespacedKey TOCLAFANE;
    public static NamespacedKey VASHTA;
    public static NamespacedKey WARRIOR;
    public static NamespacedKey ZYGON;
    public static NamespacedKey MONSTER_HEAD;
    public static PersistentDataType<byte[], UUID> PersistentDataTypeUUID;
    public static UUID UNCLAIMED = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
    public static MonsterEquipment api;
    private static boolean steal;
    private static boolean citizensEnabled = false;
    private final TARDIS plugin;

    public TARDISWeepingAngels(TARDIS plugin) {
        this.plugin = plugin;
        // initialise namespaced keys
        initKeys(this.plugin);
    }

    public static boolean angelsCanSteal() {
        return steal;
    }

    public static List<UUID> getEmpty() {
        return empty;
    }

    public static List<UUID> getTimesUp() {
        return timesUp;
    }

    public static boolean isCitizensEnabled() {
        return citizensEnabled;
    }

    public static List<UUID> getGuards() {
        return guards;
    }

    public static List<UUID> getPlayersWithGuards() {
        return playersWithGuards;
    }

    public void enable() {
        citizensEnabled = plugin.getPM().isPluginEnabled("Citizens");
        // TODO add TWA apin to TARDIS API
        api = new MonsterEquipment();
        // update the config
        new MonstersConfig(plugin).updateConfig();
        // register listeners
        plugin.getPM().registerEvents(new Blink(plugin), plugin);
        if (plugin.getMonstersConfig().getBoolean("angels.can_build")) {
            plugin.getPM().registerEvents(new AngelBuilder(plugin), plugin);
        }
        if (plugin.getMonstersConfig().getBoolean("angels.spawn_from_chat.enabled")) {
            plugin.getPM().registerEvents(new ImageHolder(plugin), plugin);
        }
        if (plugin.getMonstersConfig().getBoolean("judoon.can_build")) {
            plugin.getPM().registerEvents(new JudoonBuilder(plugin), plugin);
        }
        if (plugin.getMonstersConfig().getBoolean("k9.can_build")) {
            plugin.getPM().registerEvents(new K9Builder(plugin), plugin);
        }
        plugin.getPM().registerEvents(new MonsterLoadUnloadListener(plugin), plugin);
        plugin.getPM().registerEvents(new DalekGlideListener(), plugin);
        plugin.getPM().registerEvents(new Damage(plugin), plugin);
        plugin.getPM().registerEvents(new VashtaNeradaListener(plugin), plugin);
        plugin.getPM().registerEvents(new Death(plugin), plugin);
        plugin.getPM().registerEvents(new PlayerDeath(plugin), plugin);
        plugin.getPM().registerEvents(new PlayerUndisguise(), plugin);
        plugin.getPM().registerEvents(new Sounds(plugin), plugin);
        plugin.getPM().registerEvents(new GasMask(plugin), plugin);
        plugin.getPM().registerEvents(new MonsterInteractListener(plugin), plugin);
        plugin.getPM().registerEvents(new HeadlessTarget(plugin), plugin);
        plugin.getPM().registerEvents(new HeadlessProjectileListener(), plugin);
        plugin.getPM().registerEvents(new K9Listener(plugin), plugin);
        plugin.getPM().registerEvents(new ChunkListener(plugin), plugin);
        plugin.getPM().registerEvents(new SilurianSpawnerListener(plugin), plugin);
        plugin.getPM().registerEvents(new OodListener(plugin), plugin);
        plugin.getPM().registerEvents(new JudoonListener(plugin), plugin);
        plugin.getPM().registerEvents(new ToclafaneListener(plugin), plugin);
        plugin.getPM().registerEvents(new ArmourStandListener(), plugin);
        plugin.getPM().registerEvents(new MonsterTranformListener(plugin), plugin);
        plugin.getPM().registerEvents(new MonsterTargetListener(), plugin);
        plugin.getPM().registerEvents(new MonsterHeadEquipListener(plugin), plugin);
        if (plugin.getMonstersConfig().getInt("ood.spawn_from_villager") > 0) {
            plugin.getPM().registerEvents(new VillagerSpawnListener(plugin), plugin);
        }
        if (plugin.getMonstersConfig().getInt("ood.spawn_from_cured") > 0) {
            plugin.getPM().registerEvents(new VillagerCuredListener(plugin), plugin);
        }
        if (plugin.getMonstersConfig().getInt("toclafane.spawn_from_bee") > 0) {
            plugin.getPM().registerEvents(new BeeSpawnListener(plugin), plugin);
        }
        // register command
        plugin.getCommand("twa").setExecutor(new TARDISWeepingAngelsCommand(plugin));
        // set tab completion
        plugin.getCommand("twa").setTabCompleter(new TabComplete(plugin));
        // remove invisible Guardians not riding a Skeleton
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new CleanGuardians(plugin), 100L, 6000L);
        // start repeating spawn tasks
        long delay = plugin.getMonstersConfig().getLong("spawn_rate.how_often");
        List<Runnable> spawners = Arrays.asList(
                new CybermanRunnable(plugin),
                new DalekRunnable(plugin),
                new EmptyChildRunnable(plugin),
                new HathRunnable(plugin),
                new HeadlessMonkRunnable(plugin),
                new IceWarriorRunnable(plugin),
                new JudoonRunnable(plugin),
                new MireRunnable(plugin),
                new RacnossRunnable(plugin),
                new SeaDevilRunnable(plugin),
                new SilentRunnable(plugin),
                new SilurianRunnable(plugin),
                new SlitheenRunnable(plugin),
                new SontaranRunnable(plugin),
                new ToclafaneRunnable(plugin),
                new WeepingAngelsRunnable(plugin),
                new ZygonRunnable(plugin)
        );
        long d = 0;
        for (Runnable r : spawners) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, r, delay + d, delay);
            d += 6;
        }
        steal = (plugin.getMonstersConfig().getBoolean("angels.angels_can_steal"));
        if (plugin.getMonstersConfig().getBoolean("judoon.guards")) {
            // add recipe
            new JudoonAmmoRecipe(plugin).addRecipe();
            // start guarding task
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new JudoonGuardRunnable(plugin), 20L, 20L);
        }
        new K9Recipe(plugin).addRecipe();
        // process worlds
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new WorldProcessor(plugin), 200L);
    }

    // TODO
    public MonsterEquipment getWeepingAngelsAPI() {
        return api;
    }

    private void initKeys(TARDIS plugin) {
        ANGEL = new NamespacedKey(plugin, "angel");
        PDC_KEYS.put(Monster.WEEPING_ANGEL, ANGEL);
        CYBERMAN = new NamespacedKey(plugin, "cyberman");
        PDC_KEYS.put(Monster.CYBERMAN, CYBERMAN);
        DALEK = new NamespacedKey(plugin, "dalek");
        PDC_KEYS.put(Monster.DALEK, DALEK);
        DALEK_SEC = new NamespacedKey(plugin, "dalek_sec");
        PDC_KEYS.put(Monster.DALEK_SEC, DALEK_SEC);
        DAVROS = new NamespacedKey(plugin, "davros");
        PDC_KEYS.put(Monster.DAVROS, DAVROS);
        DEVIL = new NamespacedKey(plugin, "devil");
        PDC_KEYS.put(Monster.SEA_DEVIL, DEVIL);
        EMPTY = new NamespacedKey(plugin, "empty");
        PDC_KEYS.put(Monster.EMPTY_CHILD, EMPTY);
        HATH = new NamespacedKey(plugin, "hath");
        PDC_KEYS.put(Monster.HATH, HATH);
        JUDOON = new NamespacedKey(plugin, "judoon");
        PDC_KEYS.put(Monster.JUDOON, JUDOON);
        K9 = new NamespacedKey(plugin, "k9");
        PDC_KEYS.put(Monster.K9, K9);
        MIRE = new NamespacedKey(plugin, "mire");
        PDC_KEYS.put(Monster.MIRE, MIRE);
        MONK = new NamespacedKey(plugin, "monk");
        PDC_KEYS.put(Monster.HEADLESS_MONK, MONK);
        FLAME_TASK = new NamespacedKey(plugin, "flame_task");
        HEADLESS_TASK = new NamespacedKey(plugin, "headless_task");
        OOD = new NamespacedKey(plugin, "ood");
        PDC_KEYS.put(Monster.OOD, OOD);
        OWNER_UUID = new NamespacedKey(plugin, "owner_uuid");
        FOLLOW = new NamespacedKey(plugin, "follow");
        RACNOSS = new NamespacedKey(plugin, "racnoss");
        PDC_KEYS.put(Monster.RACNOSS, RACNOSS);
        SILENT = new NamespacedKey(plugin, "silent");
        PDC_KEYS.put(Monster.SILENT, SILENT);
        SILURIAN = new NamespacedKey(plugin, "silurian");
        PDC_KEYS.put(Monster.SILURIAN, SILURIAN);
        SLITHEEN = new NamespacedKey(plugin, "slitheen");
        PDC_KEYS.put(Monster.SLITHEEN, SLITHEEN);
        SONTARAN = new NamespacedKey(plugin, "sontaran");
        PDC_KEYS.put(Monster.SONTARAN, SONTARAN);
        STRAX = new NamespacedKey(plugin, "strax");
        PDC_KEYS.put(Monster.STRAX, STRAX);
        TOCLAFANE = new NamespacedKey(plugin, "toclafane");
        PDC_KEYS.put(Monster.TOCLAFANE, TOCLAFANE);
        VASHTA = new NamespacedKey(plugin, "vashta");
        PDC_KEYS.put(Monster.VASHTA_NERADA, VASHTA);
        WARRIOR = new NamespacedKey(plugin, "warrior");
        PDC_KEYS.put(Monster.ICE_WARRIOR, WARRIOR);
        ZYGON = new NamespacedKey(plugin, "zygon");
        PDC_KEYS.put(Monster.ZYGON, ZYGON);
        MONSTER_HEAD = new NamespacedKey(plugin, "monster_head");
        PersistentDataTypeUUID = new UUIDDataType();
    }
}
