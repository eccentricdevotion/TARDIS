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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.commands.TARDISWeepingAngelsCommand;
import me.eccentric_nz.tardisweepingangels.commands.TabComplete;
import me.eccentric_nz.tardisweepingangels.death.Death;
import me.eccentric_nz.tardisweepingangels.death.PlayerDeath;
import me.eccentric_nz.tardisweepingangels.equip.PlayerUndisguise;
import me.eccentric_nz.tardisweepingangels.monsters.angel_of_liberty.AngelOfLibertyRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.clockwork_droids.ClockworkDroidRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.cybermen.CybermanRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekGlideListener;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.GasMask;
import me.eccentric_nz.tardisweepingangels.monsters.hath.HathRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessProjectileListener;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessTarget;
import me.eccentric_nz.tardisweepingangels.monsters.heavenly_host.HeavenlyHostRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.ice_warriors.IceWarriorRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.*;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Builder;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Listener;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Recipe;
import me.eccentric_nz.tardisweepingangels.monsters.mire.MireRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.nimon.NimonRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.omega.OmegaRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodListener;
import me.eccentric_nz.tardisweepingangels.monsters.ood.VillagerCuredListener;
import me.eccentric_nz.tardisweepingangels.monsters.ood.VillagerSpawnListener;
import me.eccentric_nz.tardisweepingangels.monsters.racnoss.RacnossRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.scarecrows.ScarecrowRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.sea_devils.SeaDevilRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.silent.CleanGuardians;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.silurians.SilurianRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.silurians.SilurianSpawnerListener;
import me.eccentric_nz.tardisweepingangels.monsters.slitheen.SlitheenRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.smilers.SmilerRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.sontarans.SontaranRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.sutekh.SutekhRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.sycorax.SycoraxRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.the_beast.TheBeastRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.BeeSpawnListener;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneListener;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.vampires.VampireRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.vashta_nerada.VashtaNeradaListener;
import me.eccentric_nz.tardisweepingangels.monsters.weeping_angels.*;
import me.eccentric_nz.tardisweepingangels.monsters.zygons.ZygonRunnable;
import me.eccentric_nz.tardisweepingangels.spawner.SpawnerListener;
import me.eccentric_nz.tardisweepingangels.utils.*;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISWeepingAngels {

    public static final HashMap<Monster, NamespacedKey> PDC_KEYS = new HashMap<>();
    public static final UUID UNCLAIMED = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
    private static final List<UUID> empty = new ArrayList<>();
    private static final List<UUID> timesUp = new ArrayList<>();
    private static final List<UUID> guards = new ArrayList<>();
    private static final List<UUID> playersWithGuards = new ArrayList<>();
    public static NamespacedKey ANGEL;
    public static NamespacedKey ANGEL_OF_LIBERTY;
    public static NamespacedKey BEAST;
    public static NamespacedKey CLOCKWORK_DROID;
    public static NamespacedKey CYBERMAN;
    public static NamespacedKey CYBERSHADE;
    public static NamespacedKey DALEK;
    public static NamespacedKey DALEK_SEC;
    public static NamespacedKey DAVROS;
    public static NamespacedKey DEVIL;
    public static NamespacedKey EMPTY;
    public static NamespacedKey FLAME_TASK;
    public static NamespacedKey HATH;
    public static NamespacedKey HEADLESS_TASK;
    public static NamespacedKey HEAVENLY_HOST;
    public static NamespacedKey JUDOON;
    public static NamespacedKey K9;
    public static NamespacedKey MIRE;
    public static NamespacedKey MONK;
    public static NamespacedKey NIMON;
    public static NamespacedKey OMEGA;
    public static NamespacedKey OOD;
    public static NamespacedKey OSSIFIED;
    public static NamespacedKey OWNER_UUID;
    public static NamespacedKey RACNOSS;
    public static NamespacedKey SATURNYNIAN;
    public static NamespacedKey SCARECROW;
    public static NamespacedKey SILENT;
    public static NamespacedKey SILURIAN;
    public static NamespacedKey SLITHEEN;
    public static NamespacedKey SMILER;
    public static NamespacedKey SONTARAN;
    public static NamespacedKey STRAX;
    public static NamespacedKey SUTEKH;
    public static NamespacedKey SYCORAX;
    public static NamespacedKey TOCLAFANE;
    public static NamespacedKey VAMPIRE;
    public static NamespacedKey VASHTA;
    public static NamespacedKey WARRIOR;
    public static NamespacedKey ZYGON;
    public static NamespacedKey MONSTER_HEAD;
    public static PersistentDataType<byte[], UUID> PersistentDataTypeUUID;
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
        if (plugin.getMonstersConfig().getBoolean("custom_spawners")) {
            plugin.getPM().registerEvents(new SpawnerListener(plugin), plugin);
        }
        // register command
        plugin.getCommand("twa").setExecutor(new TARDISWeepingAngelsCommand(plugin));
        // set tab completion
        plugin.getCommand("twa").setTabCompleter(new TabComplete(plugin));
        // remove invisible Guardians not riding a Skeleton
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new CleanGuardians(plugin), 100L, 6000L);
        // start repeating spawn tasks
        long delay = plugin.getMonstersConfig().getLong("spawn_rate.how_often");
        List<Runnable> spawners = List.of(
                new AngelOfLibertyRunnable(plugin),
                new ClockworkDroidRunnable(plugin),
                new CybermanRunnable(plugin),
                new DalekRunnable(plugin),
                new EmptyChildRunnable(plugin),
                new HathRunnable(plugin),
                new HeadlessMonkRunnable(plugin),
                new HeavenlyHostRunnable(plugin),
                new IceWarriorRunnable(plugin),
                new JudoonRunnable(plugin),
                new MireRunnable(plugin),
                new NimonRunnable(plugin),
                new OmegaRunnable(plugin),
                new RacnossRunnable(plugin),
                new ScarecrowRunnable(plugin),
                new SeaDevilRunnable(plugin),
                new SilentRunnable(plugin),
                new SilurianRunnable(plugin),
                new SlitheenRunnable(plugin),
                new SmilerRunnable(plugin),
                new SontaranRunnable(plugin),
                new SutekhRunnable(plugin),
                new SycoraxRunnable(plugin),
                new TheBeastRunnable(plugin),
                new ToclafaneRunnable(plugin),
                new VampireRunnable(plugin),
                new WeepingAngelsRunnable(plugin),
                new ZygonRunnable(plugin)
        );
        long d = 0;
        for (Runnable r : spawners) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, r, delay + d, delay);
            d += 6;
        }
        // cleaner every 10 minutes
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Cleaner(plugin), 12000, 12000);
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
        // reset followers
        new FollowerReset(plugin).tame();
    }

    private void initKeys(TARDIS plugin) {
        ANGEL = new NamespacedKey(plugin, "angel");
        PDC_KEYS.put(Monster.WEEPING_ANGEL, ANGEL);
        ANGEL_OF_LIBERTY = new NamespacedKey(plugin, "angel_of_liberty");
        PDC_KEYS.put(Monster.ANGEL_OF_LIBERTY, ANGEL_OF_LIBERTY);
        BEAST = new NamespacedKey(plugin, "beast");
        PDC_KEYS.put(Monster.THE_BEAST, BEAST);
        CLOCKWORK_DROID = new NamespacedKey(plugin, "clockwork_droid");
        PDC_KEYS.put(Monster.CLOCKWORK_DROID, CLOCKWORK_DROID);
        CYBERMAN = new NamespacedKey(plugin, "cyberman");
        PDC_KEYS.put(Monster.CYBERMAN, CYBERMAN);
        CYBERSHADE = new NamespacedKey(plugin, "cybershade");
        PDC_KEYS.put(Monster.CYBERSHADE, CYBERSHADE);
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
        HEAVENLY_HOST = new NamespacedKey(plugin, "heavenly_host");
        PDC_KEYS.put(Monster.HEAVENLY_HOST, HEAVENLY_HOST);
        NIMON = new NamespacedKey(plugin, "nimon");
        PDC_KEYS.put(Monster.NIMON, NIMON);
        OMEGA = new NamespacedKey(plugin, "omega");
        PDC_KEYS.put(Monster.OMEGA, OMEGA);
        OOD = new NamespacedKey(plugin, "ood");
        PDC_KEYS.put(Monster.OOD, OOD);
        OSSIFIED = new NamespacedKey(plugin, "ossified");
        PDC_KEYS.put(Monster.OSSIFIED, OSSIFIED);
        OWNER_UUID = new NamespacedKey(plugin, "owner_uuid");
        RACNOSS = new NamespacedKey(plugin, "racnoss");
        PDC_KEYS.put(Monster.RACNOSS, RACNOSS);
        SATURNYNIAN = new NamespacedKey(plugin, "saturnynian");
        PDC_KEYS.put(Monster.SATURNYNIAN, SATURNYNIAN);
        SCARECROW = new NamespacedKey(plugin, "scarecrow");
        PDC_KEYS.put(Monster.SCARECROW, SCARECROW);
        SILENT = new NamespacedKey(plugin, "silent");
        PDC_KEYS.put(Monster.SILENT, SILENT);
        SILURIAN = new NamespacedKey(plugin, "silurian");
        PDC_KEYS.put(Monster.SILURIAN, SILURIAN);
        SLITHEEN = new NamespacedKey(plugin, "slitheen");
        PDC_KEYS.put(Monster.SLITHEEN, SLITHEEN);
        SMILER = new NamespacedKey(plugin, "smiler");
        PDC_KEYS.put(Monster.SMILER, SMILER);
        SONTARAN = new NamespacedKey(plugin, "sontaran");
        PDC_KEYS.put(Monster.SONTARAN, SONTARAN);
        STRAX = new NamespacedKey(plugin, "strax");
        PDC_KEYS.put(Monster.STRAX, STRAX);
        SUTEKH = new NamespacedKey(plugin, "sutekh");
        PDC_KEYS.put(Monster.SUTEKH, SUTEKH);
        SYCORAX = new NamespacedKey(plugin, "sycorax");
        PDC_KEYS.put(Monster.SYCORAX, SYCORAX);
        TOCLAFANE = new NamespacedKey(plugin, "toclafane");
        PDC_KEYS.put(Monster.TOCLAFANE, TOCLAFANE);
        VAMPIRE = new NamespacedKey(plugin, "vampire");
        PDC_KEYS.put(Monster.VAMPIRE_OF_VENICE, VAMPIRE);
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
