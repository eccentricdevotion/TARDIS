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
package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronLeveID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.UUID;

public class SonicConsoleRecharge implements Runnable {

    private final TARDIS plugin;
    private final UUID display_uuid;
    private final Interaction interaction;
    private final int id;
    private final Player player;
    private final int full;
    private final int amount;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(0.33f, 0.33f, 0.33f), TARDISConstants.AXIS_ANGLE_ZERO);
    private TextDisplay text;
    private int task;

    public SonicConsoleRecharge(TARDIS plugin, UUID display_uuid, Interaction interaction, int id, Player player) {
        this.plugin = plugin;
        this.display_uuid = display_uuid;
        this.interaction = interaction;
        this.id = id;
        this.player = player;
        full = this.plugin.getConfig().getInt("sonic.charge_level");
        amount = (int) Math.ceil(this.plugin.getConfig().getDouble("sonic.charge_level") / this.plugin.getConfig().getDouble("sonic.charge_interval"));
    }

    @Override
    public void run() {
        Entity entity = plugin.getServer().getEntity(display_uuid);
        if (entity instanceof ItemDisplay display) {
            ItemStack is = display.getItemStack();
            if (is == null || !is.hasItemMeta()) {
                cancel();
            }
            // check TARDIS has energy to recharge
            ResultSetArtronLeveID rsa = new ResultSetArtronLeveID(plugin, id);
            if (!rsa.resultset() || rsa.getArtronLevel() < amount) {
                TARDISSounds.playTARDISSound(interaction.getLocation(), "charge_fail");
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DOCK_ENERGY");
                cancel();
            } else {
                // take some energy
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, where, null);
            }
            ItemMeta im = is.getItemMeta();
            PersistentDataContainer pdc = im.getPersistentDataContainer();
            if (!pdc.has(plugin.getSonicChargeKey(), PersistentDataType.INTEGER)) {
                pdc.set(plugin.getSonicChargeKey(), PersistentDataType.INTEGER, amount);
                is.setItemMeta(im);
                display.setItemStack(is);
                setTextDisplay(interaction, amount);
            } else {
                int current = pdc.get(plugin.getSonicChargeKey(), PersistentDataType.INTEGER);
                if (current < full - amount) {
                    int charge = current + amount;
                    pdc.set(plugin.getSonicChargeKey(), PersistentDataType.INTEGER, charge);
                    is.setItemMeta(im);
                    display.setItemStack(is);
                    setTextDisplay(interaction, charge);
                } else {
                    pdc.set(plugin.getSonicChargeKey(), PersistentDataType.INTEGER, full);
                    is.setItemMeta(im);
                    display.setItemStack(is);
                    setTextDisplay(interaction, full);
                    // play charge done sound
                    TARDISSounds.playTARDISSound(interaction.getLocation(), "charge_done");
                    cancel();
                }
            }
        } else {
            cancel();
        }
    }

    public void setTask(int task) {
        this.task = task;
    }

    private void cancel() {
        plugin.getServer().getScheduler().cancelTask(task);
        task = 0;
        if (text != null) {
            text.remove();
        }
    }

    private void setTextDisplay(Interaction interaction, int amount) {
        text = TARDISDisplayItemUtils.getText(interaction);
        if (text == null) {
            // spawn a new one
            text = (TextDisplay) interaction.getLocation().getWorld().spawnEntity(interaction.getLocation().clone().add(0, 0.65d, 0), EntityType.TEXT_DISPLAY);
        }
        if (text.isValid()) {
            text.setTransformation(transformation);
            text.setSeeThrough(true);
            text.setBillboard(Display.Billboard.VERTICAL);
            text.text(Component.text("Sonic Dock: " + amount));
        }
    }
}
