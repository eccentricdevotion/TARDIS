package me.eccentric_nz.plugins.TARDIS;
/**
 * ImprovedOfflinePlayer, a library for Bukkit. Copyright (C) 2012
 * one4me@github.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.server.InventoryEnderChest;
import net.minecraft.server.NBTCompressedStreamTools;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * @name ImprovedOfflinePlayer
 * @version 1.5.0
 * @author one4me
 */
public class ImprovedOfflinePlayer {

    private String player;
    private File file;
    private NBTTagCompound compound;
    private boolean exists = false;
    private boolean autosave = true;

    public ImprovedOfflinePlayer(String playername) {
        this.exists = loadPlayerData(playername);
    }

    public ImprovedOfflinePlayer(OfflinePlayer offlineplayer) {
        this.exists = loadPlayerData(offlineplayer.getName());
    }

    private boolean loadPlayerData(String name) {
        try {
            this.player = name;
            for (World w : Bukkit.getWorlds()) {
                this.file = new File(w.getWorldFolder(), "players" + File.separator + this.player + ".dat");
                if (this.file.exists()) {
                    this.compound = NBTCompressedStreamTools.a(new FileInputStream(this.file));
                    this.player = this.file.getCanonicalFile().getName().replace(".dat", "");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void savePlayerData() {
        if (this.exists) {
            try {
                NBTCompressedStreamTools.a(this.compound, new FileOutputStream(this.file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean exists() {
        return this.exists;
    }

    public boolean getAutoSave() {
        return this.autosave;
    }

    public void setAutoSave(boolean autosave) {
        this.autosave = autosave;
    }

    /**
     * @param Incomplete*
     */
    public void copyDataTo(String playername) {
        try {
            if (!playername.equalsIgnoreCase(this.player)) {
                Player to = Bukkit.getPlayerExact(playername);
                Player from = Bukkit.getPlayerExact(this.player);
                if (from != null) {
                    from.saveData();
                }
                Files.copy(this.file, new File(this.file.getParentFile(), playername + ".dat"));
                if (to != null) {
                    to.teleport(from == null ? getLocation() : from.getLocation());
                    to.loadData();
                }
            } else {
                Player player = Bukkit.getPlayerExact(this.player);
                if (player != null) {
                    player.saveData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//  public PlayerAbilities getAbilities() {
//    PlayerAbilities pa = new PlayerAbilities();
//    pa.a(this.compound);
//    return pa;
//  }
//  public void setAbilities(PlayerAbilities abilities) {
//    abilities.a(this.compound);
//    savePlayerData();
//  }

    public Location getBedSpawnLocation() {
        return new Location(
                Bukkit.getWorld(this.compound.getString("SpawnWorld")),
                this.compound.getInt("SpawnX"),
                this.compound.getInt("SpawnY"),
                this.compound.getInt("SpawnZ"));
    }

    public void setBedSpawnLocation(Location location, Boolean override) {
        this.compound.setInt("SpawnX", (int) location.getX());
        this.compound.setInt("SpawnY", (int) location.getY());
        this.compound.setInt("SpawnZ", (int) location.getZ());
        this.compound.setString("SpawnWorld", location.getWorld().getName());
        this.compound.setBoolean("SpawnForced", override == null ? false : override);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public Inventory getEnderChest() {
        InventoryEnderChest endchest = new InventoryEnderChest();
        endchest.a(this.compound.getList("EnderItems"));
        return new CraftInventory(endchest);
    }

    public void setEnderChest(Inventory inventory) {
        this.compound.set("EnderItems", ((InventoryEnderChest) ((CraftInventory) inventory).getInventory()).g());
        if (this.autosave) {
            savePlayerData();
        }
    }

    public float getExhaustion() {
        return this.compound.getFloat("foodExhaustionLevel");
    }

    public void setExhaustion(float input) {
        this.compound.setFloat("foodExhaustionLevel", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public float getExp() {
        return this.compound.getFloat("XpP");
    }

    public void setExp(float input) {
        this.compound.setFloat("XpP", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public float getFallDistance() {
        return this.compound.getFloat("FallDistance");
    }

    public void setFallDistance(float input) {
        this.compound.setFloat("FallDistance", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getFireTicks() {
        return this.compound.getShort("Fire");
    }

    public void setFireTicks(int input) {
        this.compound.setShort("Fire", (short) input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public float getFlySpeed() {
        return this.compound.getCompound("abilities").getFloat("flySpeed");
    }

    public void setFlySpeed(float speed) {
        this.compound.getCompound("abilities").setFloat("flySpeed", speed);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getFoodLevel() {
        return this.compound.getInt("foodLevel");
    }

    public void setFoodLevel(int input) {
        this.compound.setInt("foodLevel", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getFoodTickTimer() {
        return this.compound.getInt("foodTickTimer");
    }

    public void setFoodTickTimer(int input) {
        this.compound.setInt("foodTickTimer", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public GameMode getGameMode() {
        return GameMode.getByValue(this.compound.getInt("playerGameType"));
    }

    public void setGameMode(GameMode input) {
        this.compound.setInt("playerGameType", input.getValue());
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getHealth() {
        return this.compound.getShort("Health");
    }

    public void setHealth(int input) {
        this.compound.setShort("Health", (short) input);
        if (this.autosave) {
            savePlayerData();
        }
    }
    /* <3 md_5 */

    public org.bukkit.inventory.PlayerInventory getInventory() {
        PlayerInventory inventory = new PlayerInventory(null);
        inventory.b(this.compound.getList("Inventory"));
        return new CraftInventoryPlayer(inventory);
    }
    /* <3 md_5 */

    public void setInventory(org.bukkit.inventory.PlayerInventory inventory) {
        this.compound.set("Inventory", ((CraftInventoryPlayer) inventory).getInventory().a(new NBTTagList()));
        if (this.autosave) {
            savePlayerData();
        }
    }

    public boolean getIsInvulnerable() {
        return compound.getBoolean("Invulnerable");
    }

    public void setIsInvulnerable(boolean input) {
        this.compound.setBoolean("Invulnerable", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public boolean getIsOnGround() {
        return compound.getBoolean("OnGround");
    }

    public void setIsOnGround(boolean input) {
        this.compound.setBoolean("OnGround", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public boolean getIsSleeping() {
        return this.compound.getBoolean("Sleeping");
    }

    public void setIsSleeping(boolean input) {
        this.compound.setBoolean("Sleeping", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getLevel() {
        return this.compound.getInt("XpLevel");
    }

    public void setLevel(int input) {
        this.compound.setInt("XpLevel", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public Location getLocation() {
        NBTTagList position = this.compound.getList("Pos");
        NBTTagList rotation = this.compound.getList("Rotation");
        return new Location(
                Bukkit.getWorld(new UUID(this.compound.getLong("WorldUUIDMost"), this.compound.getLong("WorldUUIDLeast"))),
                ((NBTTagDouble) position.get(0)).data,
                ((NBTTagDouble) position.get(1)).data,
                ((NBTTagDouble) position.get(2)).data,
                ((NBTTagFloat) rotation.get(0)).data,
                ((NBTTagFloat) rotation.get(1)).data);
    }

    public void setLocation(Location location) {
        World w = location.getWorld();
        UUID uuid = w.getUID();
        this.compound.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
        this.compound.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
        this.compound.setInt("Dimension", w.getEnvironment().getId());
        NBTTagList position = new NBTTagList();
        position.add(new NBTTagDouble(null, location.getX()));
        position.add(new NBTTagDouble(null, location.getY()));
        position.add(new NBTTagDouble(null, location.getZ()));
        this.compound.set("Pos", position);
        NBTTagList rotation = new NBTTagList();
        rotation.add(new NBTTagFloat(null, location.getYaw()));
        rotation.add(new NBTTagFloat(null, location.getPitch()));
        this.compound.set("Rotation", rotation);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public String getName() {
        return this.player;
    }

    public ArrayList<PotionEffect> getPotionEffects() {
        ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
        if (this.compound.hasKey("ActiveEffects")) {
            NBTTagList list = this.compound.getList("ActiveEffects");
            for (int i = 0; i < list.size(); i++) {
                NBTTagCompound effect = (NBTTagCompound) list.get(i);
                byte amp = effect.getByte("Amplifier");
                byte id = effect.getByte("Id");
                int time = effect.getInt("Duration");
                effects.add(new PotionEffect(PotionEffectType.getById(id), time, amp));
            }
        }
        return effects;
    }

    public void setPotionEffects(ArrayList<PotionEffect> effects) {
        if (effects.isEmpty()) {
            this.compound.remove("ActiveEffects");
            if (this.autosave) {
                savePlayerData();
            }
            return;
        }
        NBTTagList activeEffects = new NBTTagList();
        for (PotionEffect pe : effects) {
            NBTTagCompound eCompound = new NBTTagCompound();
            eCompound.setByte("Amplifier", (byte) (pe.getAmplifier()));
            eCompound.setByte("Id", (byte) (pe.getType().getId()));
            eCompound.setInt("Duration", (int) (pe.getDuration()));
            activeEffects.add(eCompound);
        }
        this.compound.set("ActiveEffects", activeEffects);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getRemainingAir() {
        return this.compound.getShort("Air");
    }

    public void setRemainingAir(int input) {
        this.compound.setShort("Air", (short) input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public float getSaturation() {
        return this.compound.getFloat("foodSaturationLevel");
    }

    public void setSaturation(float input) {
        this.compound.setFloat("foodSaturationLevel", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public short getTimeAttack() {
        return this.compound.getShort("AttackTime");
    }

    public void setTimeAttack(short input) {
        this.compound.setShort("AttackTime", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public short getTimeDeath() {
        return this.compound.getShort("DeathTime");
    }

    public void setTimeDeath(short input) {
        this.compound.setShort("DeathTime", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public short getTimeHurt() {
        return this.compound.getShort("HurtTime");
    }

    public void setTimeHurt(short input) {
        this.compound.setShort("HurtTime", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public short getTimeSleep() {
        return this.compound.getShort("SleepTimer");
    }

    public void setTimeSleep(short input) {
        this.compound.setShort("SleepTimer", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public int getTotalExperience() {
        return this.compound.getInt("XpTotal");
    }

    public void setTotalExperience(int input) {
        this.compound.setInt("XpTotal", input);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public Vector getVelocity() {
        NBTTagList list = this.compound.getList("Motion");
        return new Vector(((NBTTagDouble) list.get(0)).data, ((NBTTagDouble) list.get(1)).data, ((NBTTagDouble) list.get(2)).data);
    }

    public void setVelocity(Vector vector) {
        NBTTagList motion = new NBTTagList();
        motion.add(new NBTTagDouble(null, vector.getX()));
        motion.add(new NBTTagDouble(null, vector.getY()));
        motion.add(new NBTTagDouble(null, vector.getZ()));
        this.compound.set("Motion", motion);
        if (this.autosave) {
            savePlayerData();
        }
    }

    public float getWalkSpeed() {
        return this.compound.getCompound("abilities").getFloat("walkSpeed");
    }

    public void setWalkSpeed(float speed) {
        this.compound.getCompound("abilities").setFloat("walkSpeed", speed);
        if (this.autosave) {
            savePlayerData();
        }
    }
}
/*
 * Copyright (C) 2012 one4me@github.com
 */