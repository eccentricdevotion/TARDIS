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
package me.eccentric_nz.TARDIS.playerprefs;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PreferencesDialog {

    private final TARDIS plugin;
    private final UUID uuid;

    public PreferencesDialog(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public Dialog create() {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        if (rsp.resultSet()) {
            // get TARDIS record
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("uuid", uuid.toString());
            ResultSetTardis rst = new ResultSetTardis(plugin, wherep, "", false);
            if (rst.resultSet()) {
                Tardis tardis = rst.getTardis();
                List<DialogInput> inputs = new ArrayList<>();
                inputs.add(DialogInput.bool("announce_repeaters_on", Component.text(TARDISStringUtils.rightPad("Announce repeaters", 180)), rsp.isAnnounceRepeatersOn(), "1", "0"));
                inputs.add(DialogInput.bool("auto_on", Component.text(TARDISStringUtils.rightPad("Autonomous homing", 180)), rsp.isAutoOn(), "1", "0"));
                inputs.add(DialogInput.bool("auto_powerup_on", Component.text(TARDISStringUtils.rightPad("Auto power up", 180)), rsp.isAutoPowerUp(), "1", "0"));
                inputs.add(DialogInput.bool("auto_rescue_on", Component.text(TARDISStringUtils.rightPad("Auto rescue", 180)), rsp.isAutoRescueOn(), "1", "0"));
                inputs.add(DialogInput.bool("auto_siege_on", Component.text(TARDISStringUtils.rightPad("Auto siege mode", 180)), rsp.isAutoSiegeOn(), "1", "0"));
                inputs.add(DialogInput.bool("beacon_on", Component.text(TARDISStringUtils.rightPad("Beacon", 180)), rsp.isBeaconOn(), "1", "0"));
                if (plugin.isWorldGuardOnServer()) {
                    inputs.add(DialogInput.bool("build", Component.text(TARDISStringUtils.rightPad("Companion build", 180)), rsp.isBuildOn(), "1", "0"));
                }
                inputs.add(DialogInput.bool("close_gui_on", Component.text(TARDISStringUtils.rightPad("Close GUIs", 180)), rsp.isCloseGUIOn(), "1", "0"));
                inputs.add(DialogInput.bool("dialogs_on", Component.text(TARDISStringUtils.rightPad("Use dialogs", 180)), rsp.isDialogsOn(), "1", "0"));
                inputs.add(DialogInput.bool("dnd_on", Component.text(TARDISStringUtils.rightPad("Do not disturb", 180)), rsp.isDND(), "1", "0"));
                inputs.add(DialogInput.bool("dynamic_lamps_on", Component.text(TARDISStringUtils.rightPad("Dynamic lights", 180)), rsp.isDynamicLightsOn(), "1", "0"));
                inputs.add(DialogInput.bool("eps_on", Component.text(TARDISStringUtils.rightPad("Emergency Programme One", 180)), rsp.isEpsOn(), "1", "0"));
                // eps message
                inputs.add(DialogInput.text("eps_message", 200, Component.text("EP1 message"), true, rsp.getEpsMessage(), 256, TextDialogInput.MultilineOptions.create(3, 32)));
                inputs.add(DialogInput.bool("farm_on", Component.text(TARDISStringUtils.rightPad("Farming", 180)), rsp.isFarmOn(), "1", "0"));
                // flight mode
                inputs.add(DialogInput.singleOption(
                        "flight",
                        200,
                        List.of(
                                SingleOptionDialogInput.OptionEntry.create("NORMAL", Component.text("Normal"), rsp.getHum().equals("ALIEN")),
                                SingleOptionDialogInput.OptionEntry.create("REGULATOR", Component.text("Regulator"), rsp.getFlightMode() == 2),
                                SingleOptionDialogInput.OptionEntry.create("MANUAL", Component.text("Manual"), rsp.getFlightMode() == 3),
                                SingleOptionDialogInput.OptionEntry.create("EXTERIOR", Component.text("Exterior"), rsp.getFlightMode() == 4)
                        ),
                        Component.text("Flight mode"),
                        true
                ));
                inputs.add(DialogInput.bool("forcefield", Component.text(TARDISStringUtils.rightPad("Force field", 180)), plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid), "1", "0"));
                inputs.add(DialogInput.bool("hads_on", Component.text(TARDISStringUtils.rightPad("HADS", 180)), rsp.isHadsOn(), "1", "0"));
                // hads type
                inputs.add(DialogInput.singleOption(
                        "hads_type",
                        200,
                        List.of(
                                SingleOptionDialogInput.OptionEntry.create("DISPLACEMENT", Component.text("Displacement"), rsp.getHadsType().equals(HADS.DISPLACEMENT)),
                                SingleOptionDialogInput.OptionEntry.create("DISPERSAL", Component.text("Dispersal"), rsp.getHadsType().equals(HADS.DISPERSAL))
                        ),
                        Component.text("HADS type"),
                        true
                ));
                // hum
                inputs.add(DialogInput.singleOption(
                        "hum",
                        200,
                        List.of(
                                SingleOptionDialogInput.OptionEntry.create("ALIEN", Component.text("Alien"), rsp.getHum().equals("alien")),
                                SingleOptionDialogInput.OptionEntry.create("ATMOSPHERE", Component.text("Atmosphere"), rsp.getHum().equals("atmosphere")),
                                SingleOptionDialogInput.OptionEntry.create("COMPUTER", Component.text("Computer"), rsp.getHum().equals("computer")),
                                SingleOptionDialogInput.OptionEntry.create("COPPER", Component.text("Copper"), rsp.getHum().equals("copper")),
                                SingleOptionDialogInput.OptionEntry.create("CORAL", Component.text("Coral"), rsp.getHum().equals("coral")),
                                SingleOptionDialogInput.OptionEntry.create("GALAXY", Component.text("Galaxy"), rsp.getHum().equals("galaxy")),
                                SingleOptionDialogInput.OptionEntry.create("LEARNING", Component.text("Learning"), rsp.getHum().equals("learning")),
                                SingleOptionDialogInput.OptionEntry.create("MIND", Component.text("Mind"), rsp.getHum().equals("mind")),
                                SingleOptionDialogInput.OptionEntry.create("NEON", Component.text("Neon"), rsp.getHum().equals("neon")),
                                SingleOptionDialogInput.OptionEntry.create("SLEEPING", Component.text("Sleeping"), rsp.getHum().equals("sleeping")),
                                SingleOptionDialogInput.OptionEntry.create("VOID", Component.text("Void"), rsp.getHum().equals("void")),
                                SingleOptionDialogInput.OptionEntry.create("RANDOM", Component.text("Random"), rsp.getHum().equals("random"))
                        ),
                        Component.text("Interior hum"),
                        true
                ));
                inputs.add(DialogInput.bool("info_on", Component.text(TARDISStringUtils.rightPad("Info", 180)), rsp.isInfoOn(), "1", "0"));
                inputs.add(DialogInput.bool("isomorphic", Component.text(TARDISStringUtils.rightPad("Isomorphic controls", 180)), tardis.isIsomorphicOn(), "1", "0"));
                inputs.add(DialogInput.bool("junk_mode", Component.text(TARDISStringUtils.rightPad("Junk mode", 180)), tardis.getPreset().equals(ChameleonPreset.JUNK_MODE), "1", "0")); // junk mode
                // key item
                inputs.add(DialogInput.text("key", 200, Component.text("Key"), true, rsp.getKey(), 32, null));
                if (plugin.isWorldGuardOnServer()) {
                    String chunk = tardis.getChunk();
                    String[] split = chunk.split(":");
                    World world = plugin.getServer().getWorld(split[0]);
                    Player player = plugin.getServer().getPlayer(uuid);
                    inputs.add(DialogInput.bool("lock_containers", Component.text(TARDISStringUtils.rightPad("Lock containers", 180)), !plugin.getWorldGuardUtils().queryContainers(world, player.getName()), "1", "0")); // lock containers
                }
                inputs.add(DialogInput.bool("minecart_on", Component.text(TARDISStringUtils.rightPad("Minecart SFX", 180)), rsp.isMinecartOn(), "1", "0"));
                inputs.add(DialogInput.bool("open_display_door_on", Component.text(TARDISStringUtils.rightPad("Open display door", 180)), rsp.isOpenDisplayDoorOn(), "1", "0"));
                inputs.add(DialogInput.bool("quotes_on", Component.text(TARDISStringUtils.rightPad("Who quotes", 180)), rsp.isQuotesOn(), "1", "0"));
                inputs.add(DialogInput.bool("renderer_on", Component.text(TARDISStringUtils.rightPad("Renderer room", 180)), rsp.isRendererOn(), "1", "0"));
                inputs.add(DialogInput.bool("sfx_on", Component.text(TARDISStringUtils.rightPad("Sound effects", 180)), rsp.isSfxOn(), "1", "0"));
                inputs.add(DialogInput.bool("submarine_on", Component.text(TARDISStringUtils.rightPad("Submarine mode", 180)), rsp.isSubmarineOn(), "1", "0"));
                inputs.add(DialogInput.bool("sign_on", Component.text(TARDISStringUtils.rightPad("Exterior sign", 180)), rsp.isSignOn(), "1", "0"));
                inputs.add(DialogInput.bool("telepathy_on", Component.text(TARDISStringUtils.rightPad("Telepathic circuit", 180)), rsp.isTelepathyOn(), "1", "0"));
                inputs.add(DialogInput.bool("travelbar_on", Component.text(TARDISStringUtils.rightPad("Show travel bar", 180)), rsp.isTravelbarOn(), "1", "0"));
                DialogAction action = DialogAction.customClick((response, audience) -> {
                            Player player = audience instanceof Player ? (Player) audience : null;
                            new PreferencesProcessor(TARDIS.plugin, player).process(response);
                        },
                        ClickCallback.Options.builder().build()
                );
                DialogBase dialogData = DialogBase.create(Component.text("Player Preferences"), null, true, true, DialogBase.DialogAfterAction.CLOSE, List.of(), inputs);
                ActionButton button = ActionButton.create(Component.text("Save"), null, 150, action);
                return Dialog.create(builder -> builder.empty()
                        .base(dialogData)
                        .type(DialogType.notice(button))
                );

            }
        }
        return null;
    }
}
