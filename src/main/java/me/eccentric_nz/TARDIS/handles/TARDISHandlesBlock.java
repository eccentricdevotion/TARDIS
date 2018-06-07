/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.handles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public enum TARDISHandlesBlock {

    COMEHERE(TARDISHandlesCategory.COMMAND, "/tardis comehere command", Arrays.asList("Make the TARDIS", "travel to you")),
    HIDE(TARDISHandlesCategory.COMMAND, "/tardis hide command", Arrays.asList("Make the TARDIS", "exterior invisible")),
    REBUILD(TARDISHandlesCategory.COMMAND, "/tardis rebuild command", Arrays.asList("Rebuild the", "TARDIS exterior")),
    SCAN(TARDISHandlesCategory.COMMAND, "TARDIS scan command", Arrays.asList("Scan the environment", "outside the TARDIS")),
    TAKE_OFF(TARDISHandlesCategory.COMMAND, "TARDIS take off command", Arrays.asList("Make the TARDIS enter", "the Time Vortex")),
    TRAVEL(TARDISHandlesCategory.COMMAND, "/tardistravel command", Arrays.asList("Make the TARDIS", "travel somewhere")),
    LAND(TARDISHandlesCategory.COMMAND, "TARDIS land command", Arrays.asList("Make the TARDIS exit", "the Time Vortex")),
    FOR(TARDISHandlesCategory.CONTROL, "FOR loop", Arrays.asList("Use to start a loop", "with a counter")),
    TO(TARDISHandlesCategory.CONTROL, "TO", Arrays.asList("Use with a FOR loop", "to specify the number", "of loops")),
    DO(TARDISHandlesCategory.CONTROL, "DO", Arrays.asList("Use after an", "IF or FOR loop")),
    END(TARDISHandlesCategory.CONTROL, "END", Arrays.asList("Use to finish", "a conditional or", "loop statement")),
    IF(TARDISHandlesCategory.CONTROL, "IF", Arrays.asList("Check whether a", "condition is", "true or false")),
    ELSE(TARDISHandlesCategory.CONTROL, "ELSE", Arrays.asList("Use to run actions", "if an IF is false")),
    ELSE_IF(TARDISHandlesCategory.CONTROL, "ELSE IF", Arrays.asList("Check another condition")),
    BREAK(TARDISHandlesCategory.CONTROL, "BREAK", Arrays.asList("Use to break", "out of a loop")),
    MATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS materialisation event", null),
    DEMATERIALISE(TARDISHandlesCategory.EVENT, "TARDIS dematerialisation event", null),
    ARTRON(TARDISHandlesCategory.EVENT, "Artron Level event", Arrays.asList("Use with Operator", "and Number blocks")),
    DEATH(TARDISHandlesCategory.EVENT, "Time Lord Death event", null),
    ENTER(TARDISHandlesCategory.EVENT, "TARDIS Entry event", null),
    EXIT(TARDISHandlesCategory.EVENT, "TARDIS Exit event", null),
    HADS(TARDISHandlesCategory.EVENT, "TARDIS H.A.D.S event", null),
    SIEGE_ON(TARDISHandlesCategory.EVENT, "Siege Mode On event", null),
    SIEGE_OFF(TARDISHandlesCategory.EVENT, "Siege Mode Off event", null),
    LOG_OUT(TARDISHandlesCategory.EVENT, "Player log out event", null),
    ZERO(TARDISHandlesCategory.NUMBER, "0", Arrays.asList("The number zero")),
    ONE(TARDISHandlesCategory.NUMBER, "1", Arrays.asList("The number one")),
    TWO(TARDISHandlesCategory.NUMBER, "2", Arrays.asList("The number two")),
    THREE(TARDISHandlesCategory.NUMBER, "3", Arrays.asList("The number three")),
    FOUR(TARDISHandlesCategory.NUMBER, "4", Arrays.asList("The number four")),
    FIVE(TARDISHandlesCategory.NUMBER, "5", Arrays.asList("The number five")),
    SIX(TARDISHandlesCategory.NUMBER, "6", Arrays.asList("The number six")),
    SEVEN(TARDISHandlesCategory.NUMBER, "7", Arrays.asList("The number seven")),
    EIGHT(TARDISHandlesCategory.NUMBER, "8", Arrays.asList("The number eight")),
    NINE(TARDISHandlesCategory.NUMBER, "9", Arrays.asList("The number nine")),
    COIN(TARDISHandlesCategory.NUMBER, "Flip a coin", Arrays.asList("Returns a true", "or false value")),
    RANDOM(TARDISHandlesCategory.NUMBER, "Random number", Arrays.asList("Generate a random number", "between 0 and the", "number that follows")),
    ASSIGNMENT(TARDISHandlesCategory.OPERATOR, "Assignment operator", Arrays.asList("Used to assign a", "value to a variable")),
    ADDITION(TARDISHandlesCategory.OPERATOR, "Addition operator", Arrays.asList("Use to add two", "numbers together")),
    SUBTRACTION(TARDISHandlesCategory.OPERATOR, "Subtraction operator", Arrays.asList("Use to subtract one", "number from anther")),
    MULTIPLICATION(TARDISHandlesCategory.OPERATOR, "Multiplication operator", Arrays.asList("Use to multiply two", "numbers together")),
    DIVISION(TARDISHandlesCategory.OPERATOR, "Division operator", Arrays.asList("Use to divide one", "number by another")),
    AND(TARDISHandlesCategory.OPERATOR, "AND operator", Arrays.asList("Use to check", "both conditions", "are is true")),
    OR(TARDISHandlesCategory.OPERATOR, "OR operator", Arrays.asList("Use to check", "if either", "condition is true")),
    EQUALS(TARDISHandlesCategory.OPERATOR, "Equality operator", Arrays.asList("Checks if two", "objects are equal")),
    NOT_EQUAL(TARDISHandlesCategory.OPERATOR, "Not equal operator", Arrays.asList("Checks if two objects", "are not the same")),
    LESS_THAN(TARDISHandlesCategory.OPERATOR, "Less than operator", Arrays.asList("Checks if one number", "is smaller than another")),
    LESS_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Less than or equal to operator", Arrays.asList("Checks if one number", "is smaller than or", "equal to another")),
    GREATER_THAN(TARDISHandlesCategory.OPERATOR, "Greater than operator", Arrays.asList("Checks if one number", "is bigger than another")),
    GREATER_THAN_EQUAL(TARDISHandlesCategory.OPERATOR, "Greater than or equal to operator", Arrays.asList("Checks if one number", "is bigger than or", "equal to another")),
    MODULO(TARDISHandlesCategory.OPERATOR, "Modulo operator", Arrays.asList("Gets the remainder", "left over after", "dividing two numbers")),
    TIME_LORD(TARDISHandlesCategory.SELECTOR, "Target Time Lord", null),
    COMPANIONS(TARDISHandlesCategory.SELECTOR, "Target companions", null),
    TARDIS(TARDISHandlesCategory.SELECTOR, "The TARDIS", null),
    DOOR(TARDISHandlesCategory.SELECTOR, "TARDIS Door", Arrays.asList("Use with an", "Open, Close", "Lock or Unlock block")),
    LIGHTS(TARDISHandlesCategory.SELECTOR, "TARDIS Lights", Arrays.asList("Use with an", "On or Off block")),
    POWER(TARDISHandlesCategory.SELECTOR, "TARDIS Power action", Arrays.asList("Use with a Show,", "On or Off block")),
    SIEGE(TARDISHandlesCategory.SELECTOR, "Siege Mode action", Arrays.asList("Use with an", "On or Off block")),
    VARIABLE(TARDISHandlesCategory.VARIABLE, "Variable", Arrays.asList("A generic container", "to hold a value")),
    X(TARDISHandlesCategory.VARIABLE, "X coordinate", Arrays.asList("Use with a number", "to specify a location")),
    Y(TARDISHandlesCategory.VARIABLE, "Y coordinate", Arrays.asList("Use with a number", "to specify a location")),
    Z(TARDISHandlesCategory.VARIABLE, "Z coordinate", Arrays.asList("Use with a number", "to specify a location")),
    OPEN(TARDISHandlesCategory.VARIABLE, "Open Door action", null),
    CLOSE(TARDISHandlesCategory.VARIABLE, "Close Door action", null),
    LOCK(TARDISHandlesCategory.VARIABLE, "Lock Door action", null),
    UNLOCK(TARDISHandlesCategory.VARIABLE, "Unlock Door action", null),
    ON(TARDISHandlesCategory.VARIABLE, "On action", null),
    OFF(TARDISHandlesCategory.VARIABLE, "Off action", null),
    SHOW(TARDISHandlesCategory.VARIABLE, "Show Artron Levels action", null),
    HOME(TARDISHandlesCategory.VARIABLE, "Home Location", null),
    RECHARGER(TARDISHandlesCategory.VARIABLE, "Travel to recharger", null),
    CONTROL(TARDISHandlesCategory.BUTTON, "Show control blocks", null),
    OPERATOR(TARDISHandlesCategory.BUTTON, "Show operator blocks", null),
    VAR(TARDISHandlesCategory.BUTTON, "Show variable blocks", null),
    NUMBER(TARDISHandlesCategory.BUTTON, "Show number blocks", null),
    EVENT(TARDISHandlesCategory.BUTTON, "Show event blocks", null),
    COMMAND(TARDISHandlesCategory.BUTTON, "Show command blocks", null),
    SELECTOR(TARDISHandlesCategory.BUTTON, "Show selector blocks", null),
    PROGRAMS(TARDISHandlesCategory.BUTTON, "Show saved programs", null),
    SAVE(TARDISHandlesCategory.BUTTON, "Save the program to disk", null),
    LEFT(TARDISHandlesCategory.BUTTON, "Scroll left", null),
    RIGHT(TARDISHandlesCategory.BUTTON, "Scroll right", null);

    private final TARDISHandlesCategory category;
    private final String displayName;
    private final List<String> lore;
    private final static List<TARDISHandlesBlock> commands = new ArrayList<>();
    private final static List<TARDISHandlesBlock> controls = new ArrayList<>();
    private final static List<TARDISHandlesBlock> events = new ArrayList<>();
    private final static List<TARDISHandlesBlock> numbers = new ArrayList<>();
    private final static List<TARDISHandlesBlock> operators = new ArrayList<>();
    private final static List<TARDISHandlesBlock> selectors = new ArrayList<>();
    private final static List<TARDISHandlesBlock> variables = new ArrayList<>();
    private final static List<TARDISHandlesBlock> buttons = new ArrayList<>();
    public final static HashMap<String, TARDISHandlesBlock> BY_NAME = new HashMap<>();

    TARDISHandlesBlock(TARDISHandlesCategory category, String displayName, List<String> lore) {
        this.category = category;
        this.displayName = displayName;
        this.lore = lore;
    }

    static {
        for (TARDISHandlesBlock block : values()) {
            switch (block.getCategory()) {
                case BUTTON:
                    buttons.add(block);
                    break;
                case COMMAND:
                    commands.add(block);
                    break;
                case CONTROL:
                    controls.add(block);
                    break;
                case EVENT:
                    events.add(block);
                    break;
                case NUMBER:
                    numbers.add(block);
                    break;
                case OPERATOR:
                    operators.add(block);
                    break;
                case SELECTOR:
                    selectors.add(block);
                    break;
                default:
                    variables.add(block);
                    break;
            }
            BY_NAME.put(block.displayName, block);
        }
    }

    public TARDISHandlesCategory getCategory() {
        return category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public static List<TARDISHandlesBlock> getCommands() {
        return commands;
    }

    public static List<TARDISHandlesBlock> getControls() {
        return controls;
    }

    public static List<TARDISHandlesBlock> getEvents() {
        return events;
    }

    public static List<TARDISHandlesBlock> getNumbers() {
        return numbers;
    }

    public static List<TARDISHandlesBlock> getOperators() {
        return operators;
    }

    public static List<TARDISHandlesBlock> getSelectors() {
        return selectors;
    }

    public static List<TARDISHandlesBlock> getVariables() {
        return variables;
    }

    public static List<TARDISHandlesBlock> getButtons() {
        return buttons;
    }
}
