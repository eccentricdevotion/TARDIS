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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @name ImprovedOfflinePlayer
 * @version 1.5.0
 * @author one4me
 */
public class ImprovedOfflinePlayer implements ImprovedOfflinePlayer_api {

    private String player;
    private File file;
    private NBTTagCompound compound;
    private boolean exists = false;
    private boolean autosave = true;

//    public ImprovedOfflinePlayer_v1_4_6(String playername) {
//        this.exists = loadPlayerData(playername);
//    }
//
//    public ImprovedOfflinePlayer_v1_4_6(OfflinePlayer offlineplayer) {
//        this.exists = loadPlayerData(offlineplayer.getName());
//    }
    private boolean loadPlayerData(String name) {
        try {
            this.player = name;
            for (World w : Bukkit.getWorlds()) {
                this.file = new File(w.getWorldFolder(), "players" + File.separator + this.player + ".dat");
                if (this.file.exists()) {
                    this.compound = CompressedStreamTools.readCompressed(new FileInputStream(this.file));
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
            	CompressedStreamTools.writeCompressed(this.compound, new FileOutputStream(this.file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean exists() {
        return this.exists;
    }

    @Override
    public void setLocation(String playername, Location location) {
        this.exists = loadPlayerData(playername);
        World w = location.getWorld();
        UUID uuid = w.getUID();
        this.compound.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
        this.compound.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
        this.compound.setInteger("Dimension", w.getEnvironment().getId());
        NBTTagList position = new NBTTagList();
        position.appendTag(new NBTTagDouble(null, location.getX()));
        position.appendTag(new NBTTagDouble(null, location.getY()));
        position.appendTag(new NBTTagDouble(null, location.getZ()));
        this.compound.setTag("Pos", position);
        NBTTagList rotation = new NBTTagList();
        rotation.appendTag(new NBTTagFloat(null, location.getYaw()));
        rotation.appendTag(new NBTTagFloat(null, location.getPitch()));
        this.compound.setTag("Rotation", rotation);
        if (this.autosave) {
            savePlayerData();
        }
    }
}
/*
 * Copyright (C) 2012 one4me@github.com
 */