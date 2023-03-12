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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels;

import java.util.*;
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
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonAmmoRecipe;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonBuilder;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonGuardRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonListener;
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
import me.eccentric_nz.tardisweepingangels.monsters.sontarans.Butler;
import me.eccentric_nz.tardisweepingangels.monsters.sontarans.SontaranRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.BeeSpawnListener;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneListener;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.vashta_nerada.VashtaNeradaListener;
import me.eccentric_nz.tardisweepingangels.monsters.weeping_angels.*;
import me.eccentric_nz.tardisweepingangels.monsters.zygons.ZygonRunnable;
import me.eccentric_nz.tardisweepingangels.move.MonsterMoveListener;
import me.eccentric_nz.tardisweepingangels.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDISWeepingAngels extends JavaPlugin {

    public static TARDISWeepingAngels plugin;
    public static Random random = new Random();
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
    private final List<UUID> empty = new ArrayList<>();
    private final List<UUID> timesUp = new ArrayList<>();
    private final List<UUID> guards = new ArrayList<>();
    private final List<UUID> playersWithGuards = new ArrayList<>();
    private final HashMap<UUID, Integer> followTasks = new HashMap<>();
    public String pluginName;
    private boolean steal;
    private PluginManager pm;
    private boolean citizensEnabled = false;

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        plugin = this;
        pm = getServer().getPluginManager();
        PluginDescriptionFile pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        citizensEnabled = pm.isPluginEnabled("Citizens");
        saveDefaultConfig();
        api = new MonsterEquipment();
        // update the config
        new Config(this).updateConfig();
        // initialise namespaced keys
        initKeys(this);
        // register listeners
        pm.registerEvents(new Blink(this), this);
        if (getConfig().getBoolean("angels.can_build")) {
            pm.registerEvents(new AngelBuilder(this), this);
        }
        if (getConfig().getBoolean("angels.spawn_from_chat.enabled")) {
            pm.registerEvents(new ImageHolder(this), this);
        }
        if (getConfig().getBoolean("judoon.can_build")) {
            pm.registerEvents(new JudoonBuilder(this), this);
        }
        if (getConfig().getBoolean("k9.can_build")) {
            pm.registerEvents(new K9Builder(this), this);
        }
        pm.registerEvents(new MonsterMoveListener(), this);
        pm.registerEvents(new DalekGlideListener(this), this);
        pm.registerEvents(new Damage(this), this);
        pm.registerEvents(new VashtaNeradaListener(this), this);
        pm.registerEvents(new Death(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerUndisguise(this), this);
        pm.registerEvents(new Sounds(this), this);
        pm.registerEvents(new GasMask(this), this);
        pm.registerEvents(new Butler(this), this);
        pm.registerEvents(new HelmetChecker(), this);
        pm.registerEvents(new HeadlessTarget(this), this);
        pm.registerEvents(new HeadlessProjectileListener(), this);
        pm.registerEvents(new K9Listener(this), this);
        pm.registerEvents(new ChunkListener(this), this);
        pm.registerEvents(new SilurianSpawnerListener(this), this);
        pm.registerEvents(new OodListener(), this);
        pm.registerEvents(new JudoonListener(this), this);
        pm.registerEvents(new ToclafaneListener(this), this);
        pm.registerEvents(new ArmourStandListener(), this);
        pm.registerEvents(new MonsterTranformListener(this), this);
        pm.registerEvents(new MonsterTargetListener(), this);
        pm.registerEvents(new MonsterHeadEquipListener(this), this);
        if (plugin.getConfig().getInt("ood.spawn_from_villager") > 0) {
            pm.registerEvents(new VillagerSpawnListener(this), this);
        }
        if (plugin.getConfig().getInt("ood.spawn_from_cured") > 0) {
            pm.registerEvents(new VillagerCuredListener(this), this);
        }
        if (plugin.getConfig().getInt("toclafane.spawn_from_bee") > 0) {
            pm.registerEvents(new BeeSpawnListener(this), this);
        }
        // register command
        getCommand("twa").setExecutor(new TARDISWeepingAngelsCommand(this));
        // set tab completion
        getCommand("twa").setTabCompleter(new TabComplete(this));
        // remove invisible Guardians not riding a Skeleton
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CleanGuardians(this), 100L, 6000L);
        // start repeating spawn tasks
        long delay = getConfig().getLong("spawn_rate.how_often");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CybermanRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DalekRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new EmptyChildRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HathRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HeadlessMonkRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new IceWarriorRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new MireRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new RacnossRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SeaDevilRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SilentRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SilurianRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SlitheenRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SontaranRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new ToclafaneRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new WeepingAngelsRunnable(this), delay, delay);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new ZygonRunnable(this), delay, delay);
        steal = (getConfig().getBoolean("angels.angels_can_steal"));
        if (getConfig().getBoolean("judoon.guards")) {
            // add recipe
            new JudoonAmmoRecipe(this).addRecipe();
            // start guarding task
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new JudoonGuardRunnable(this), 20L, 20L);
        }
        new K9Recipe(this).addRecipe();
        // process worlds
        getServer().getScheduler().scheduleSyncDelayedTask(this, new WorldProcessor(this), 200L);
    }

    public boolean angelsCanSteal() {
        return steal;
    }

    public List<UUID> getEmpty() {
        return empty;
    }

    public List<UUID> getTimesUp() {
        return timesUp;
    }

    public boolean isCitizensEnabled() {
        return citizensEnabled;
    }

    /**
     * Outputs a message to the console.
     *
     * @param o the Object to print to the console
     */
    public void debug(Object o) {
        getServer().getConsoleSender().sendMessage(pluginName + "Debug: " + o);
    }

    public MonsterEquipment getWeepingAngelsAPI() {
        return api;
    }

    public List<UUID> getGuards() {
        return guards;
    }

    public List<UUID> getPlayersWithGuards() {
        return playersWithGuards;
    }

    public HashMap<UUID, Integer> getFollowTasks() {
        return followTasks;
    }

    private void initKeys(TARDISWeepingAngels plugin) {
        ANGEL = new NamespacedKey(plugin, "angel");
        CYBERMAN = new NamespacedKey(plugin, "cyberman");
        DALEK = new NamespacedKey(plugin, "dalek");
        DALEK_SEC = new NamespacedKey(plugin, "dalek_sec");
        DAVROS = new NamespacedKey(plugin, "davros");
        DEVIL = new NamespacedKey(plugin, "devil");
        EMPTY = new NamespacedKey(plugin, "empty");
        HATH = new NamespacedKey(plugin, "hath");
        JUDOON = new NamespacedKey(plugin, "judoon");
        K9 = new NamespacedKey(plugin, "k9");
        MIRE = new NamespacedKey(plugin, "mire");
        MONK = new NamespacedKey(plugin, "monk");
        FLAME_TASK = new NamespacedKey(plugin, "flame_task");
        HEADLESS_TASK = new NamespacedKey(plugin, "headless_task");
        OOD = new NamespacedKey(plugin, "ood");
        OWNER_UUID = new NamespacedKey(plugin, "owner_uuid");
        RACNOSS = new NamespacedKey(plugin, "racnoss");
        SILENT = new NamespacedKey(plugin, "silent");
        SILURIAN = new NamespacedKey(plugin, "silurian");
        SLITHEEN = new NamespacedKey(plugin, "slitheen");
        SONTARAN = new NamespacedKey(plugin, "sontaran");
        STRAX = new NamespacedKey(plugin, "strax");
        TOCLAFANE = new NamespacedKey(plugin, "toclafane");
        VASHTA = new NamespacedKey(plugin, "vashta");
        WARRIOR = new NamespacedKey(plugin, "warrior");
        ZYGON = new NamespacedKey(plugin, "zygon");
        MONSTER_HEAD = new NamespacedKey(plugin, "monster_head");
        PersistentDataTypeUUID = new UUIDDataType();
    }
}
