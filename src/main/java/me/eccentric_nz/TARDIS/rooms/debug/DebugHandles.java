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
package me.eccentric_nz.TARDIS.rooms.debug;

import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import org.bukkit.Material;

import java.util.List;

public class DebugHandles {

    public static final List<GuiPreview> ICONS = List.of(
            new GuiPreview(Material.ARROW, ArrowVariant.HANDLES_OPERATOR_ADDITION.getKey(), "handles_operator_addition"),
            new GuiPreview(Material.ARROW, ArrowVariant.HANDLES_OPERATOR_SUBTRACTION.getKey(), "handles_operator_subtraction"),
            new GuiPreview(Material.BIRCH_BUTTON, Whoniverse.HANDLES_OFF.getKey(), "handles_off"),
            new GuiPreview(Material.BIRCH_BUTTON, Whoniverse.HANDLES_ON.getKey(), "handles_on"),
            new GuiPreview(Material.BIRCH_BUTTON, Whoniverse.COMMUNICATOR.getKey(), "communicator"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_COMEHERE.getKey(), "handles_command_comehere"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_HIDE.getKey(), "handles_command_hide"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_REBUILD.getKey(), "handles_command_rebuild"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_SCAN.getKey(), "handles_command_scan"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_TAKE_OFF.getKey(), "handles_command_take_off"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_TRAVEL.getKey(), "handles_command_travel"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_COMMAND_LAND.getKey(), "handles_command_land"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_FOR.getKey(), "handles_control_for"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_TO.getKey(), "handles_control_to"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_DO.getKey(), "handles_control_do"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_END.getKey(), "handles_control_end"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_IF.getKey(), "handles_control_if"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_ELSE.getKey(), "handles_control_else"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_ELSE_IF.getKey(), "handles_control_else_if"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_CONTROL_BREAK.getKey(), "handles_control_break"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_MATERIALISE.getKey(), "handles_event_materialise"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_DEMATERIALISE.getKey(), "handles_event_dematerialise"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_ARTRON.getKey(), "handles_event_artron"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_DEATH.getKey(), "handles_event_death"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_ENTER.getKey(), "handles_event_enter"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_EXIT.getKey(), "handles_event_exit"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_HADS.getKey(), "handles_event_hads"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_SIEGE_ON.getKey(), "handles_event_siege_on"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_SIEGE_OFF.getKey(), "handles_event_siege_off"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_EVENT_LOG_OUT.getKey(), "handles_event_log_out"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_ZERO.getKey(), "handles_number_zero"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_ONE.getKey(), "handles_number_one"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_TWO.getKey(), "handles_number_two"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_THREE.getKey(), "handles_number_three"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_FOUR.getKey(), "handles_number_four"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_FIVE.getKey(), "handles_number_five"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_SIX.getKey(), "handles_number_six"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_SEVEN.getKey(), "handles_number_seven"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_EIGHT.getKey(), "handles_number_eight"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_NINE.getKey(), "handles_number_nine"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_COIN.getKey(), "handles_number_coin"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_NUMBER_RANDOM.getKey(), "handles_number_random"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_ASSIGNMENT.getKey(), "handles_operator_assignment"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_ADDITION.getKey(), "handles_operator_addition"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_SUBTRACTION.getKey(), "handles_operator_subtraction"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_MULTIPLICATION.getKey(), "handles_operator_multiplication"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_DIVISION.getKey(), "handles_operator_division"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_AND.getKey(), "handles_operator_and"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_OR.getKey(), "handles_operator_or"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_EQUALS.getKey(), "handles_operator_equals"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_NOT_EQUAL.getKey(), "handles_operator_not_equal"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_LESS_THAN.getKey(), "handles_operator_less_than"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_LESS_THAN_EQUAL.getKey(), "handles_operator_less_than_equal"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_GREATER_THAN.getKey(), "handles_operator_greater_than"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_GREATER_THAN_EQUAL.getKey(), "handles_operator_greater_than_equal"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_OPERATOR_MODULO.getKey(), "handles_operator_modulo"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_TIME_LORD.getKey(), "handles_selector_time_lord"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_COMPANIONS.getKey(), "handles_selector_companions"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_TARDIS.getKey(), "handles_selector_tardis"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_DOOR.getKey(), "handles_selector_door"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_LIGHTS.getKey(), "handles_selector_lights"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_POWER.getKey(), "handles_selector_power"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_SELECTOR_SIEGE.getKey(), "handles_selector_siege"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_VARIABLE.getKey(), "handles_variable_variable"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_X.getKey(), "handles_variable_x"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_Y.getKey(), "handles_variable_y"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_Z.getKey(), "handles_variable_z"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_OPEN.getKey(), "handles_variable_open"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_CLOSE.getKey(), "handles_variable_close"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_LOCK.getKey(), "handles_variable_lock"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_UNLOCK.getKey(), "handles_variable_unlock"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_ON.getKey(), "handles_variable_on"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_OFF.getKey(), "handles_variable_off"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_SHOW.getKey(), "handles_variable_show"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_REDSTONE.getKey(), "handles_variable_redstone"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_HOME.getKey(), "handles_variable_home"),
            new GuiPreview(Material.PAPER, HandlesVariant.HANDLES_VARIABLE_RECHARGER.getKey(), "handles_variable_recharger")
    );
}
