package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISDirectionCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.control.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateControlForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateControlForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm form = SimpleForm.builder().title("TARDIS Control Menu")
                .button("Random", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/random_button.png")
                .button("Saves", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/saves_button.png")
                .button("Back", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/back_button.png")
                .button("Areas", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/areas_button.png")
                .button("Terminal", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/dest_terminal.png")
                .button("ARS", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/ars_button.png")
                .button("Desktop Theme", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/theme_button.png")
                .button("Power", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/power_on.png")
                .button("Light switch", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/on_switch.png")
                .button("Door toggle", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/toggle_open.png")
                .button("Map", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/map_button.png")
                .button("Chameleon Circuit", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png")
                .button("Siege Mode", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/siege_on.png")
                .button("Hide", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/hide_button.png")
                .button("Rebuild", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/rebuild_button.png")
                .button("Direction", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/direction_button.png")
                .button("Temporal Locator", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/temporal_button.png")
                .button("Artron Energy Levels", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/artron_button.png")
                .button("Scanner", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/scan_button.png")
                .button("TARDIS Information System", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/info_button.png")
                .button("Transmat", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/transmat_button.png")
                .button("Zero Room", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/zero_button.png")
                .button("Player Preferences", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/prefs_button.png")
                .button("Companions", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/companions_button.png")
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        int buttonId = response.clickedButtonId();
        Player player = Bukkit.getPlayer(uuid);
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                // check they initialised
                if (!tardis.isTardis_init()) {
                    TARDISMessage.send(player, "ENERGY_NO_INIT");
                    return;
                }
                if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on() && buttonId != 7 && buttonId != 12 && buttonId != 17) {
                    TARDISMessage.send(player, "POWER_DOWN");
                    return;
                }
                if (!tardis.isHandbrake_on()) {
                    switch (buttonId) {
                        case 5 -> {
                            TARDISMessage.send(player, "ARS_NO_TRAVEL");
                            return;
                        }
                        case 6, 7, 11, 12, 13, 14, 15, 18 -> {
                            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                return;
                            }
                        }
                        default -> {}
                    }
                }
                boolean lights = tardis.isLights_on();
                int level = tardis.getArtron_level();
                TARDISCircuitChecker tcc = null;
                if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                    tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                }
                switch (buttonId) {
                    case 0 -> { // random button
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "INPUT_MISSING");
                            return;
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISRandomButton(plugin, player, id, level, 0, tardis.getCompanions(), tardis.getUuid()).clickButton(), 2L);
                    }
                    case 1 -> { // saves gui
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasMemory()) {
                            TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                            return;
                        }
                        new FloodgateSavesForm(plugin, uuid, id).send();
                    }
                    case 2 -> { // back / fast return
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "INPUT_MISSING");
                            return;
                        }
                        new TARDISFastReturnButton(plugin, player, id, level).clickButton();
                    }
                    case 3 -> { // areas gui
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (tcc != null && !tcc.hasMemory()) {
                            TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                            return;
                        }
                        new FloodgateAreasForm(plugin, uuid).send();
                    }
                    case 4 -> { // destination terminal
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (level < plugin.getArtronConfig().getInt("travel")) {
                            TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                            return;
                        }
                        if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "INPUT_MISSING");
                            return;
                        }
                        new FloodgateDestinationTerminalForm(plugin, uuid).send();
                    }
                    case 5 -> { // ars
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        // check they're in a compatible world
                        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                            TARDISMessage.send(player, "ROOM_OWN_WORLD");
                            return;
                        }
                        // check they have permission to grow rooms
                        if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
                            TARDISMessage.send(player, "NO_PERM_ROOMS");
                            return;
                        }
                        if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                            TARDISMessage.send(player, "ARS_MISSING");
                            return;
                        }
                        TARDISMessage.send(player, "ARS_USE_CMD");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            ItemStack[] tars = new TARDISARSInventory(plugin, player).getARS();
                            Inventory ars = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
                            ars.setContents(tars);
                            player.openInventory(ars);
                        }, 100);
                    }
                    case 6 -> { // desktop theme
                        // check player is in own TARDIS
                        int p_tid = TARDISThemeButton.getTardisId(uuid.toString());
                        if (p_tid != id) {
                            TARDISMessage.send(player, "UPGRADE_OWN");
                            return;
                        }
                        // check they are not growing rooms
                        if (plugin.getTrackerKeeper().getIsGrowingRooms().contains(id)) {
                            TARDISMessage.send(player, "NO_UPGRADE_WHILE_GROWING");
                            return;
                        }
                        // get player's current console
                        TARDISUpgradeData tud = new TARDISUpgradeData();
                        tud.setPrevious(tardis.getSchematic());
                        tud.setLevel(level);
                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                    }
                    case 7 -> { // power
                        if (plugin.getConfig().getBoolean("allow.power_down")) {
                            new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPowered_on(), tardis.isHidden(), lights, player.getLocation(), level, tardis.getSchematic().hasLanterns()).clickButton();
                        } else {
                            TARDISMessage.send(player, "POWER_DOWN_DISABLED");
                        }
                    }
                    case 8 -> { // light switch
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (!lights && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        new TARDISLightSwitch(plugin, id, lights, player, tardis.getSchematic().hasLanterns()).flickSwitch();
                    }
                    case 9 -> { // door toggle
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
                    }
                    case 10 -> { // map
                        new FloodgateMapForm(plugin, uuid, id).send();
                    }
                    case 11 -> { // chameleon circuit
                        if (tcc != null && !tcc.hasChameleon()) {
                            TARDISMessage.send(player, "CHAM_MISSING");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                            TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
                            return;
                        }
                        new FloodgateChameleonCircuitForm(plugin, uuid, id, tardis.getPreset()).send();
                    }
                    case 12 -> { // siege mode
                        if (tcc != null && !tcc.hasMaterialisation()) {
                            TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                            return;
                        }
                        new TARDISSiegeButton(plugin, player, tardis.isPowered_on(), id).clickButton();
                    }
                    case 13 -> { // hide
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        new TARDISHideCommand(plugin).hide(player);
                    }
                    case 14 -> { // rebuild
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        new TARDISRebuildCommand(plugin).rebuildPreset(player);
                    }
                    case 15 -> { // direction
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, whered);
                        String direction = "EAST";
                        if (rsc.resultSet()) {
                            direction = rsc.getDirection().toString();
                            int ordinal = COMPASS.valueOf(direction).ordinal() + 1;
                            if (ordinal == 4) {
                                ordinal = 0;
                            }
                            direction = COMPASS.values()[ordinal].toString();
                        }
                        String[] args = new String[]{"direction", direction};
                        new TARDISDirectionCommand(plugin).changeDirection(player, args);
                    }
                    case 16 -> { // temporal
                        if (!TARDISPermission.hasPermission(player, "tardis.temporal")) {
                            TARDISMessage.send(player, "NO_PERM_TEMPORAL");
                            return;
                        }
                        if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "TEMP_MISSING");
                            return;
                        }
                        new FloodgateTemporalForm(plugin, uuid).send();
                    }
                    case 17 -> { // artron level
                        new TARDISArtronIndicator(plugin).showArtronLevel(player, id, 0);
                    }
                    case 18 -> { // scanner
                        TARDISScanner.scan(player, id, plugin.getServer().getScheduler());
                    }
                    case 19 -> { // TIS
                        new TARDISInfoMenuButton(plugin, player).clickButton();
                    }
                    case 20 -> { // transmat
                        new FloodgateTransmatForm(plugin, uuid, id).send();
                    }
                    case 21 -> { // zero room
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        int zero_amount = plugin.getArtronConfig().getInt("zero");
                        if (level < zero_amount) {
                            TARDISMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
                            return;
                        }
                        Location zero = TARDISStaticLocationGetters.getLocationFromDB(tardis.getZero());
                        if (zero != null) {
                            TARDISMessage.send(player, "ZERO_READY");
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISExteriorRenderer(plugin).transmat(player, COMPASS.SOUTH, zero), 20L);
                            plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
                            HashMap<String, Object> wherez = new HashMap<>();
                            wherez.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
                        } else {
                            TARDISMessage.send(player, "NO_ZERO");
                        }
                    }
                    case 22 -> { // player prefs
                        new FloodgatePlayerPrefsForm(plugin, uuid).send();
                    }
                    case 23 -> { // companions
                        String comps = tardis.getCompanions();
                        if (comps == null || comps.isEmpty()) {
                            TARDISMessage.send(player, "COMPANIONS_NONE");
                            return;
                        }
                        String[] companionData = comps.split(":");
                        new FloodgateCompanionsForm(plugin, uuid, companionData).send();
                    }
                    default -> { // do nothing
                    }
                }
            }
        }
    }
}
