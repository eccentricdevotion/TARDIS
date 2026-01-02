/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.util.List;

public enum SwitchVariant {

    ABANDON_OFF(new NamespacedKey(TARDIS.plugin, "switch_abandon_off"), List.of(138f)),
    ABANDON_ON(new NamespacedKey(TARDIS.plugin, "switch_abandon_on"), List.of(238f)),
    ALLOW_OFF(new NamespacedKey(TARDIS.plugin, "switch_allow_off"), List.of(127f)),
    ALLOW_ON(new NamespacedKey(TARDIS.plugin, "switch_allow_on"), List.of(227f)),
    ANNOUNCE_REPEATERS_OFF(new NamespacedKey(TARDIS.plugin, "switch_announce_repeaters_off"), List.of(126f)),
    ANNOUNCE_REPEATERS_ON(new NamespacedKey(TARDIS.plugin, "switch_announce_repeaters_on"), List.of(226f)),
    ARCHIVE_OFF(new NamespacedKey(TARDIS.plugin, "switch_archive_off"), List.of(129f)),
    ARCHIVE_ON(new NamespacedKey(TARDIS.plugin, "switch_archive_on"), List.of(229f)),
    ARCH_OFF(new NamespacedKey(TARDIS.plugin, "switch_arch_off"), List.of(128f)),
    ARCH_ON(new NamespacedKey(TARDIS.plugin, "switch_arch_on"), List.of(228f)),
    AUTONOMOUS_OFF(new NamespacedKey(TARDIS.plugin, "switch_autonomous_off"), List.of(101f)),
    AUTONOMOUS_ON(new NamespacedKey(TARDIS.plugin, "switch_autonomous_on"), List.of(201f)),
    AUTONOMOUS_SIEGE_OFF(new NamespacedKey(TARDIS.plugin, "switch_autonomous_siege_off"), List.of(102f)),
    AUTONOMOUS_SIEGE_ON(new NamespacedKey(TARDIS.plugin, "switch_autonomous_siege_on"), List.of(202f)),
    AUTO_DEFAULT(new NamespacedKey(TARDIS.plugin, "switch_auto_default"), List.of(246f)),
    AUTO_POWER_UP_OFF(new NamespacedKey(TARDIS.plugin, "switch_auto_power_up_off"), List.of(121f)),
    AUTO_POWER_UP_ON(new NamespacedKey(TARDIS.plugin, "switch_auto_power_up_on"), List.of(221f)),
    AUTO_RESCUE_OFF(new NamespacedKey(TARDIS.plugin, "switch_auto_rescue_off"), List.of(103f)),
    AUTO_RESCUE_ON(new NamespacedKey(TARDIS.plugin, "switch_auto_rescue_on"), List.of(203f)),
    AUTO_TYPE(new NamespacedKey(TARDIS.plugin, "switch_auto_type"), List.of(146f)),
    BEACON_OFF(new NamespacedKey(TARDIS.plugin, "switch_beacon_off"), List.of(104f)),
    BEACON_ON(new NamespacedKey(TARDIS.plugin, "switch_beacon_on"), List.of(204f)),
    BLUEPRINTS_OFF(new NamespacedKey(TARDIS.plugin, "switch_blueprints_off"), List.of(130f)),
    BLUEPRINTS_ON(new NamespacedKey(TARDIS.plugin, "switch_blueprints_on"), List.of(230f)),
    BUTTON_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "switch_display_door_closed"), List.of(154f)),
    BUTTON_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "switch_display_door_open"), List.of(254f)),
    BUTTON_LIGHTS_OFF(new NamespacedKey(TARDIS.plugin, "switch_button_lights_off"), List.of(147f)),
    BUTTON_LIGHTS_ON(new NamespacedKey(TARDIS.plugin, "switch_button_lights_on"), List.of(247f)),
    BUTTON_POWER_OFF(new NamespacedKey(TARDIS.plugin, "switch_button_power_off"), List.of(148f)),
    BUTTON_POWER_ON(new NamespacedKey(TARDIS.plugin, "switch_button_power_on"), List.of(248f)),
    BUTTON_SIEGE_OFF(new NamespacedKey(TARDIS.plugin, "switch_button_siege_off"), List.of(149f)),
    BUTTON_SIEGE_ON(new NamespacedKey(TARDIS.plugin, "switch_button_siege_on"), List.of(249f)),
    BUTTON_TOGGLE_OFF(new NamespacedKey(TARDIS.plugin, "switch_button_toggle_off"), List.of(150f)),
    BUTTON_TOGGLE_ON(new NamespacedKey(TARDIS.plugin, "switch_button_toggle_on"), List.of(250f)),
    CIRCUIT_OFF(new NamespacedKey(TARDIS.plugin, "switch_circuit_off"), List.of(139f)),
    CIRCUIT_ON(new NamespacedKey(TARDIS.plugin, "switch_circuit_on"), List.of(239f)),
    CLOSE_GUI_OFF(new NamespacedKey(TARDIS.plugin, "switch_close_gui_off"), List.of(105f)),
    CLOSE_GUI_ON(new NamespacedKey(TARDIS.plugin, "switch_close_gui_on"), List.of(205f)),
    COMPANION_BUILD_OFF(new NamespacedKey(TARDIS.plugin, "switch_companion_build_off"), List.of(115f)),
    COMPANION_BUILD_ON(new NamespacedKey(TARDIS.plugin, "switch_companion_build_on"), List.of(215f)),
    CONSOLE_LABELS_OFF(new NamespacedKey(TARDIS.plugin, "switch_console_labels_off"), List.of(156f)),
    CONSOLE_LABELS_ON(new NamespacedKey(TARDIS.plugin, "switch_console_labels_on"), List.of(256f)),
    CREATION_OFF(new NamespacedKey(TARDIS.plugin, "switch_creation_off"), List.of(131f)),
    CREATION_ON(new NamespacedKey(TARDIS.plugin, "switch_creation_on"), List.of(231f)),
    DEBUG_OFF(new NamespacedKey(TARDIS.plugin, "switch_debug_off"), List.of(140f)),
    DEBUG_ON(new NamespacedKey(TARDIS.plugin, "switch_debug_on"), List.of(240f)),
    DIALOGS_OFF(new NamespacedKey(TARDIS.plugin, "switch_dialogs_off"), List.of(155f)),
    DIALOGS_ON(new NamespacedKey(TARDIS.plugin, "switch_dialogs_on"), List.of(255f)),
    DIFFICULTY_OFF(new NamespacedKey(TARDIS.plugin, "switch_difficulty_off"), List.of(132f)),
    DIFFICULTY_ON(new NamespacedKey(TARDIS.plugin, "switch_difficulty_on"), List.of(232f)),
    DISPLAY_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "switch_display_door_off"), List.of(154f)),
    DISPLAY_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "switch_display_door_on"), List.of(254f)),
    DOWNLOAD_OFF(new NamespacedKey(TARDIS.plugin, "switch_download_off"), List.of(151f)),
    DOWNLOAD_ON(new NamespacedKey(TARDIS.plugin, "switch_download_on"), List.of(251f)),
    DO_NOT_DISTURB_OFF(new NamespacedKey(TARDIS.plugin, "switch_do_not_disturb_off"), List.of(106f)),
    DO_NOT_DISTURB_ON(new NamespacedKey(TARDIS.plugin, "switch_do_not_disturb_on"), List.of(206f)),
    DYNAMIC_LAMPS_OFF(new NamespacedKey(TARDIS.plugin, "switch_dynamic_lamps_off"), List.of(107f)),
    DYNAMIC_LAMPS_ON(new NamespacedKey(TARDIS.plugin, "switch_dynamic_lamps_on"), List.of(207f)),
    EMERGENCY_PROGRAM_ONE_OFF(new NamespacedKey(TARDIS.plugin, "switch_emergency_program_one_off"), List.of(108f)),
    EMERGENCY_PROGRAM_ONE_ON(new NamespacedKey(TARDIS.plugin, "switch_emergency_program_one_on"), List.of(208f)),
    EXTERIOR_RENDERING_ROOM_OFF(new NamespacedKey(TARDIS.plugin, "switch_exterior_rendering_room_off"), List.of(112f)),
    EXTERIOR_RENDERING_ROOM_ON(new NamespacedKey(TARDIS.plugin, "switch_exterior_rendering_room_on"), List.of(212f)),
    EYE_OFF(new NamespacedKey(TARDIS.plugin, "switch_eye_off"), List.of(133f)),
    EYE_ON(new NamespacedKey(TARDIS.plugin, "switch_eye_on"), List.of(233f)),
    FORCE_FIELD_OFF(new NamespacedKey(TARDIS.plugin, "switch_force_field_off"), List.of(122f)),
    FORCE_FIELD_ON(new NamespacedKey(TARDIS.plugin, "switch_force_field_on"), List.of(222f)),
    GROWTH_OFF(new NamespacedKey(TARDIS.plugin, "switch_growth_off"), List.of(134f)),
    GROWTH_ON(new NamespacedKey(TARDIS.plugin, "switch_growth_on"), List.of(234f)),
    HADS_TYPE_DISPERSAL(new NamespacedKey(TARDIS.plugin, "switch_hads_type_dispersal"), List.of(110f)),
    HADS_TYPE_DISPLACEMENT(new NamespacedKey(TARDIS.plugin, "switch_hads_type_displacement"), List.of(210f)),
    HANDLES_OFF(new NamespacedKey(TARDIS.plugin, "switch_handles_off"), List.of(135f)),
    HANDLES_ON(new NamespacedKey(TARDIS.plugin, "switch_handles_on"), List.of(235f)),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM_OFF(new NamespacedKey(TARDIS.plugin, "switch_hostile_action_displacement_system_off"), List.of(109f)),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM_ON(new NamespacedKey(TARDIS.plugin, "switch_hostile_action_displacement_system_on"), List.of(209f)),
    INFO_OFF(new NamespacedKey(TARDIS.plugin, "switch_info_off"), List.of(125f)),
    INFO_ON(new NamespacedKey(TARDIS.plugin, "switch_info_on"), List.of(225f)),
    ISOMORPHIC_OFF(new NamespacedKey(TARDIS.plugin, "switch_isomorphic_off"), List.of(157f)),
    ISOMORPHIC_ON(new NamespacedKey(TARDIS.plugin, "switch_isomorphic_on"), List.of(257f)),
    INTERIOR_SFX_OFF(new NamespacedKey(TARDIS.plugin, "switch_interior_sfx_off"), List.of(113f)),
    INTERIOR_SFX_ON(new NamespacedKey(TARDIS.plugin, "switch_interior_sfx_on"), List.of(213f)),
    JUNK_OFF(new NamespacedKey(TARDIS.plugin, "switch_junk_off"), List.of(142f)),
    JUNK_ON(new NamespacedKey(TARDIS.plugin, "switch_junk_on"), List.of(242f)),
    JUNK_TARDIS_OFF(new NamespacedKey(TARDIS.plugin, "switch_junk_tardis_off"), List.of(120f)),
    JUNK_TARDIS_ON(new NamespacedKey(TARDIS.plugin, "switch_junk_tardis_on"), List.of(220f)),
    LOCK_CONTAINERS_OFF(new NamespacedKey(TARDIS.plugin, "switch_lock_containers_off"), List.of(124f)),
    LOCK_CONTAINERS_ON(new NamespacedKey(TARDIS.plugin, "switch_lock_containers_on"), List.of(224f)),
    MINECART_OFF(new NamespacedKey(TARDIS.plugin, "switch_minecart_off"), List.of(123f)),
    MINECART_ON(new NamespacedKey(TARDIS.plugin, "switch_minecart_on"), List.of(223f)),
    MOB_FARMING_OFF(new NamespacedKey(TARDIS.plugin, "switch_mob_farming_off"), List.of(118f)),
    MOB_FARMING_ON(new NamespacedKey(TARDIS.plugin, "switch_mob_farming_on"), List.of(218f)),
    OPEN_DISPLAY_DOOR_OFF(new NamespacedKey(TARDIS.plugin, "switch_display_door_off"), List.of(153f)),
    OPEN_DISPLAY_DOOR_ON(new NamespacedKey(TARDIS.plugin, "switch_display_door_on"), List.of(253f)),
    POLICE_BOX_OFF(new NamespacedKey(TARDIS.plugin, "switch_police_box_off"), List.of(136f)),
    POLICE_BOX_ON(new NamespacedKey(TARDIS.plugin, "switch_police_box_on"), List.of(236f)),
    PREFERENCES_OFF(new NamespacedKey(TARDIS.plugin, "switch_preferences_off"), List.of(137f)),
    PREFERENCES_ON(new NamespacedKey(TARDIS.plugin, "switch_preferences_on"), List.of(237f)),
    PRESET_SIGN_OFF(new NamespacedKey(TARDIS.plugin, "switch_preset_sign_off"), List.of(116f)),
    PRESET_SIGN_ON(new NamespacedKey(TARDIS.plugin, "switch_preset_sign_on"), List.of(216f)),
    SIEGE_OFF(new NamespacedKey(TARDIS.plugin, "switch_siege_off"), List.of(143f)),
    SIEGE_ON(new NamespacedKey(TARDIS.plugin, "switch_siege_on"), List.of(243f)),
    SONIC_OFF(new NamespacedKey(TARDIS.plugin, "switch_sonic_off"), List.of(144f)),
    SONIC_ON(new NamespacedKey(TARDIS.plugin, "switch_sonic_on"), List.of(244f)),
    SUBMARINE_MODE_OFF(new NamespacedKey(TARDIS.plugin, "switch_submarine_mode_off"), List.of(114f)),
    SUBMARINE_MODE_ON(new NamespacedKey(TARDIS.plugin, "switch_submarine_mode_on"), List.of(214f)),
    TELEPATHIC_CIRCUIT_OFF(new NamespacedKey(TARDIS.plugin, "switch_telepathic_circuit_off"), List.of(119f)),
    TELEPATHIC_CIRCUIT_ON(new NamespacedKey(TARDIS.plugin, "switch_telepathic_circuit_on"), List.of(219f)),
    THEME_OFF(new NamespacedKey(TARDIS.plugin, "switch_theme_off"), List.of(141f)),
    THEME_ON(new NamespacedKey(TARDIS.plugin, "switch_theme_on"), List.of(241f)),
    TRAVEL_BAR_OFF(new NamespacedKey(TARDIS.plugin, "switch_travel_bar_off"), List.of(117f)),
    TRAVEL_BAR_ON(new NamespacedKey(TARDIS.plugin, "switch_travel_bar_on"), List.of(217f)),
    TRAVEL_OFF(new NamespacedKey(TARDIS.plugin, "switch_travel_off"), List.of(145f)),
    TRAVEL_ON(new NamespacedKey(TARDIS.plugin, "switch_travel_on"), List.of(245f)),
    WHO_QUOTES_OFF(new NamespacedKey(TARDIS.plugin, "switch_who_quotes_off"), List.of(111f)),
    WHO_QUOTES_ON(new NamespacedKey(TARDIS.plugin, "switch_who_quotes_on"), List.of(211f));

    private final NamespacedKey key;
    private final List<Float> floats;

    SwitchVariant(NamespacedKey key, List<Float> floats) {
        this.key = key;
        this.floats = floats;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public List<Float> getFloats() {
        return floats;
    }
}
