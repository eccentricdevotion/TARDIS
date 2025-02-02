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
package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.info.TISInfo;
import me.eccentric_nz.TARDIS.info.TISRoomInfo;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateIndexSectionForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TISCategory category;

    public FloodgateIndexSectionForm(TARDIS plugin, UUID uuid, TISCategory category) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.category = category;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Info: " + category.getName());
        for (TARDISInfoMenu tim : TARDISInfoMenu.values()) {
            if (category == TISCategory.ITEMS && tim.isItem()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.CONSOLE_BLOCKS && tim.isConsoleBlock()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.ACCESSORIES && tim.isAccessory()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.COMPONENTS && tim.isComponent()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.SONIC_COMPONENTS && tim.isSonicComponent()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.SONIC_UPGRADES && tim.isSonicUpgrade()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.CONSOLES && tim.isConsole()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.DISKS && tim.isDisk()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.ROOMS && tim.isRoom()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.PLANETS && tim.isPlanet()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.TIME_TRAVEL && tim.isTimeTravel()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.FOOD && tim.isFood()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.UPDATEABLE_BLOCKS && tim.isUpdateable()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            } else if (category == TISCategory.MONSTERS && tim.isMonster()) {
                builder.button(TARDISStringUtils.capitalise(tim.toString()), FormImage.Type.PATH, "textures/items/written_book.png");
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String name = TARDISStringUtils.toEnumUppercase(response.clickedButton().text());
        try {
            TARDISInfoMenu tim = TARDISInfoMenu.valueOf(name);
            if (category == TISCategory.ROOMS) {
                new TISRoomInfo(plugin).show(player, tim);
            } else if ((category.isFirstLevel() && !hasRecipe(tim)) || (category == TISCategory.MONSTERS && tim != TARDISInfoMenu.K9)) {
                new TISInfo(plugin).show(player, tim);
            } else {
                new FloodgateIndexEntryForm(plugin, uuid, tim).send();
                plugin.getTrackerKeeper().getFlight().put(uuid, tim.toString());
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private boolean hasRecipe(TARDISInfoMenu tim) {
        switch (tim) {
            case EXTERIOR_LAMP_LEVEL_SWITCH, INTERIOR_LIGHT_LEVEL_SWITCH, TARDIS_MONITOR, MONITOR_FRAME -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
