/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;

/**
 * @author eccentric_nz
 */
public class TARDISNumberParsers {

    /**
     * Parses a string for an integer.
     *
     * @param i the string to convert to an int.
     * @return a number
     */
    public static int parseInt(String i) {
        int num = 0;
        try {
            num = Integer.parseInt(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to int, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a float.
     *
     * @param i the string to convert to an float.
     * @return a floating point number
     */
    public static float parseFloat(String i) {
        float num = 0.0f;
        try {
            num = Float.parseFloat(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to float, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a double.
     *
     * @param i the string to convert to an double.
     * @return a floating point number
     */
    public static double parseDouble(String i) {
        double num = 0.0d;
        try {
            num = Double.parseDouble(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to double, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a double.
     *
     * @param i the string to convert to an double.
     * @return a floating point number
     */
    public static long parseLong(String i) {
        long num = 0L;
        try {
            num = Long.parseLong(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to double, the string was: " + i);
        }
        return num;
    }

    /**
     * Returns a rounded integer after division.
     *
     * @param num     the number being divided.
     * @param divisor the number to divide by.
     * @return a rounded number.
     */
    public static int roundUp(int num, int divisor) {
        return (num + divisor - 1) / divisor;
    }

    public static boolean isNumber(String str) {
        if (TARDISStringUtils.isEmpty(str)) {
            return false;
        } else {
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] == '-' ? 1 : 0;
            int i;
            if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
                i = start + 2;
                if (i == sz) {
                    return false;
                } else {
                    while (i < chars.length) {
                        if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;
                for (i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        foundDigit = true;
                        allowSigns = false;
                    } else if (chars[i] == '.') {
                        if (hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] != '+' && chars[i] != '-') {
                            return false;
                        }

                        if (!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if (hasExp) {
                            return false;
                        }

                        if (!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }
                if (i < chars.length) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        return true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] == '.') {
                            return !hasDecPoint && !hasExp && foundDigit;
                        } else if (allowSigns || chars[i] != 'd' && chars[i] != 'D' && chars[i] != 'f' && chars[i] != 'F') {
                            if (chars[i] != 'l' && chars[i] != 'L') {
                                return false;
                            } else {
                                return foundDigit && !hasExp;
                            }
                        } else {
                            return foundDigit;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return !allowSigns && foundDigit;
                }
            }
        }
    }

    public static boolean isSimpleNumber(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
