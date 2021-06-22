/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * Programming is a process used by Cybermen to control humans. To program a human, the person has to be dead. A control
 * is installed in the person, powered by electricity, turning the person into an agent of the Cybermen. Control over
 * programmed humans can be shorted out by another signal, but that kills whatever might be left of the person.
 *
 * @author eccentric_nz
 */
class TardisHandlesValidator {

    private final ItemStack[] program;
    private final Player player;
    private int endCount = 1;
    private int eventCount = 0;

    TardisHandlesValidator(ItemStack[] program, Player player) {
        this.program = program;
        this.player = player;
    }

    boolean validateDisk() {
        int i = 0;
        for (ItemStack is : program) {
            if (is != null) {
                TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(is.getItemMeta()).getDisplayName());
                switch (thb) {
                    case FOR:
                        if (!validateFor(i + 1)) {
                            TardisMessage.handlesMessage(player, "The FOR loop does not compute!");
                            return false;
                        }
                        break;
                    case IF:
                        if (!validateIF(i + 1)) {
                            TardisMessage.handlesMessage(player, "The IF statement does not compute!");
                            return false;
                        }
                        break;
                    case VARIABLE:
                        if (!validateVar(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Variable assignment does not compute!");
                            return false;
                        }
                        break;
                    case X:
                    case Y:
                    case Z:
                        if (!validateCoordOrMath(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Coordinate assignment does not compute!");
                            return false;
                        }
                        break;
                    case ARTRON:
                    case DEATH:
                    case DEMATERIALISE:
                    case ENTER:
                    case EXIT:
                    case HADS:
                    case LOG_OUT:
                    case MATERIALISE:
                    case SIEGE_OFF:
                    case SIEGE_ON:
                        if (eventCount > 0) {
                            TardisMessage.handlesMessage(player, "You can only have one event per program!");
                            return false;
                        }
                        eventCount++;
                        break;
                    case ADDITION:
                    case SUBTRACTION:
                    case MULTIPLICATION:
                    case DIVISION:
                    case MODULO:
                    case LESS_THAN:
                    case LESS_THAN_EQUAL:
                    case GREATER_THAN:
                    case GREATER_THAN_EQUAL:
                        // must be followed by a number (and maybe a variable)
                        if (!validateCoordOrMath(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Math operation does not compute!");
                            return false;
                        }
                        break;
                    case RANDOM:
                        // must be followed by a number or preceded by travel
                        ItemStack pre = program[i - 1];
                        TardisHandlesBlock cede = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(pre.getItemMeta()).getDisplayName());
                        if (!TardisHandlesBlock.TRAVEL.equals(cede) && !validateCoordOrMath(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Math operation does not compute!");
                            return false;
                        }
                    case DOOR:
                        // must be followed by =, ==, OPEN, CLOSED, LOCK, UNLOCK
                        if (!validateDoor(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Door action does not compute!");
                            return false;
                        }
                        break;
                    case LIGHTS:
                    case SIEGE:
                        // must be followed by =, ==, ON, OFF
                        if (!validateOnOff(i + 1)) {
                            TardisMessage.handlesMessage(player, "The ON / OFF action does not compute!");
                            return false;
                        }
                        break;
                    case POWER:
                        // must be followed by =, ==, ON, OFF, SHOW, REDSTONE
                        if (!validatePower(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Power action does not compute!");
                            return false;
                        }
                        break;
                    case TRAVEL:
                        // must be followed by X, Y, Z, RECHARGER, HOME, RANDOM, Biome disk, Player disk, Save disk, Area disk
                        if (!validateTravel(i + 1)) {
                            TardisMessage.handlesMessage(player, "The Travel destination does not compute!");
                            return false;
                        }
                        break;
                    default:
                        break;
                }
            }
            i++;
        }
        return true;
    }

    private boolean validateVar(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(op.getItemMeta()).getDisplayName());
        if (!thb.getCategory().equals(TardisHandlesCategory.OPERATOR)) {
            return false;
        }
        ItemStack val = program[start];
        if (val == null) {
            return false;
        }
        TardisHandlesBlock thbv = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(val.getItemMeta()).getDisplayName());
        return thbv.getCategory().equals(TardisHandlesCategory.NUMBER);
    }

    private boolean validateCoordOrMath(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(op.getItemMeta()).getDisplayName());
        return thb.getCategory().equals(TardisHandlesCategory.NUMBER) || thb.equals(TardisHandlesBlock.SUBTRACTION);
    }

    private boolean validateDoor(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(op.getItemMeta()).getDisplayName());
        return thb.equals(TardisHandlesBlock.ASSIGNMENT) || thb.equals(TardisHandlesBlock.EQUALS) || thb.equals(TardisHandlesBlock.OPEN) || thb.equals(TardisHandlesBlock.CLOSE) || thb.equals(TardisHandlesBlock.LOCK) || thb.equals(TardisHandlesBlock.UNLOCK);
    }

    private boolean validateOnOff(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(op.getItemMeta()).getDisplayName());
        return thb.equals(TardisHandlesBlock.ASSIGNMENT) || thb.equals(TardisHandlesBlock.EQUALS) || thb.equals(TardisHandlesBlock.ON) || thb.equals(TardisHandlesBlock.OFF);
    }

    private boolean validatePower(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(op.getItemMeta()).getDisplayName());
        return thb.equals(TardisHandlesBlock.ASSIGNMENT) || thb.equals(TardisHandlesBlock.EQUALS) || thb.equals(TardisHandlesBlock.ON) || thb.equals(TardisHandlesBlock.OFF) || thb.equals(TardisHandlesBlock.SHOW) || thb.equals(TardisHandlesBlock.REDSTONE);
    }

    private boolean validateTravel(int start) {
        ItemStack op = program[start];
        if (op == null) {
            return false;
        }
        TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(op.getItemMeta()).getDisplayName());
        Material record = op.getType();
        return thb.equals(TardisHandlesBlock.HOME) || thb.equals(TardisHandlesBlock.RECHARGER) || thb.equals(TardisHandlesBlock.X) || thb.equals(TardisHandlesBlock.Y) || thb.equals(TardisHandlesBlock.Z) || thb.equals(TardisHandlesBlock.RANDOM) || record.equals(Material.MUSIC_DISC_CHIRP) || record.equals(Material.MUSIC_DISC_WAIT) || record.equals(Material.MUSIC_DISC_CAT) || record.equals(Material.MUSIC_DISC_BLOCKS);
    }

    private boolean validateFor(int start) {
        int found = 0;
        for (int i = start; i < 36; i++) {
            ItemStack is = program[i];
            if (is == null) {
                if (i == 35) {
                    return found == endCount;
                } else {
                    continue;
                }
            }
            TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(is.getItemMeta()).getDisplayName());
            switch (i - start) {
                case 0: // must be a variable
                    if (!thb.getCategory().equals(TardisHandlesCategory.VARIABLE)) {
                        return false;
                    }
                    break;
                case 1: // must be assignment
                    if (!thb.getDisplayName().equals("Assignment operator")) {
                        return false;
                    }
                    break;
                case 2:
                case 4: // must be a number
                    if (!thb.getCategory().equals(TardisHandlesCategory.NUMBER)) {
                        return false;
                    }
                    break;
                case 3: // must be a TO
                    if (!thb.equals(TardisHandlesBlock.TO)) {
                        return false;
                    }
                    break;
                case 5: // must be a DO
                    if (!thb.equals(TardisHandlesBlock.DO)) {
                        return false;
                    }
                    break;
                default: // search for end
                    if (thb.equals(TardisHandlesBlock.IF)) {
                        endCount++;
                    }
                    if (thb.equals(TardisHandlesBlock.END)) {
                        if (found++ == endCount) {
                            return true;
                        }
                    }
            }
        }
        return true;
    }

    private boolean validateIF(int start) {
        boolean twoConditions = false;
        int found = 0;
        for (int i = start; i < 36; i++) {
            ItemStack is = program[i];
            if (is == null) {
                if (i == 35) {
                    return found == endCount;
                } else {
                    continue;
                }
            }
            TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(is.getItemMeta()).getDisplayName());
            switch (i - start) {
                case 0: // must be an event, variable or selector
                    if (!thb.getCategory().equals(TardisHandlesCategory.VARIABLE) && !thb.getCategory().equals(TardisHandlesCategory.SELECTOR) && !thb.getCategory().equals(TardisHandlesCategory.EVENT)) {
                        return false;
                    }
                    break;
                case 1: // must be an operator
                    if (!thb.getCategory().equals(TardisHandlesCategory.OPERATOR)) {
                        return false;
                    }
                    break;
                case 2: // must be a number or event or variable
                    if (!thb.getCategory().equals(TardisHandlesCategory.NUMBER) && !thb.getCategory().equals(TardisHandlesCategory.EVENT) && !thb.getCategory().equals(TardisHandlesCategory.VARIABLE)) {
                        return false;
                    }
                    break;
                case 3: // check for second condition
                    if (thb.equals(TardisHandlesBlock.AND) || thb.equals(TardisHandlesBlock.OR)) {
                        twoConditions = true;
                    } else {
                        // must be a DO
                        if (!thb.equals(TardisHandlesBlock.DO)) {
                            return false;
                        }
                    }
                    break;
                case 4: // must be an event, variable or selector if twoConditions
                    if (twoConditions && (!thb.getCategory().equals(TardisHandlesCategory.VARIABLE) && !thb.getCategory().equals(TardisHandlesCategory.SELECTOR) && !thb.getCategory().equals(TardisHandlesCategory.EVENT))) {
                        return false;
                    } else {
                        if (thb.equals(TardisHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TardisHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                    break;
                case 5: // must be an operator if twoConditions
                    if (twoConditions && (!thb.getCategory().equals(TardisHandlesCategory.OPERATOR))) {
                        return false;
                    } else {
                        if (thb.equals(TardisHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TardisHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                    break;
                case 6: // must be a number or event or variable if twoConditions
                    if (twoConditions && (!thb.getCategory().equals(TardisHandlesCategory.NUMBER) && !thb.getCategory().equals(TardisHandlesCategory.EVENT) && !thb.getCategory().equals(TardisHandlesCategory.VARIABLE))) {
                        return false;
                    } else {
                        if (thb.equals(TardisHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TardisHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                    break;
                case 7: // must be a DO
                    if (twoConditions && !thb.equals(TardisHandlesBlock.DO)) {
                        return false;
                    } else {
                        if (thb.equals(TardisHandlesBlock.IF)) {
                            endCount++;
                        }
                        if (thb.equals(TardisHandlesBlock.END)) {
                            if (found++ == endCount) {
                                return true;
                            }
                        }
                    }
                    break;
                default: // search for end
                    if (thb.equals(TardisHandlesBlock.IF)) {
                        endCount++;
                    }
                    if (thb.equals(TardisHandlesBlock.END)) {
                        if (found++ == endCount) {
                            return true;
                        }
                    }
            }
        }
        return true;
    }
}
