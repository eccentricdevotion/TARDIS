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
package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import com.mojang.datafixers.util.Pair;

import java.util.Random;

public class IslandSpiral {

    private Pair<Integer, Integer> treePosition;

    public double[][] createMatrix(int rows, int cols, Random random, double increment) {

        // create a matrix and fill it with 0s
        double[][] arr = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                arr[r][c] = 0;
            }
        }
        // define the boundaries of the matrix
        // leaving the outside 0
        int top = 1, bottom = rows - 2, left = 1, right = cols - 2;
        double height = 0;
        int change = random.nextInt(12) + 4;
        int counter = 0;
        // get a random tree position
        int t = random.nextInt((rows - 2) * (cols - 2) - 24) + 24;

        // define the direction in which the array is to be traversed
        Direction dir = Direction.RIGHT;

        while (top <= bottom && left <= right) {
            switch (dir) {
                case RIGHT -> {
                    // moving left->right
                    for (int i = left; i <= right; ++i) {
                        if (counter % change == 0) {
                            height += increment;
                            change = random.nextInt(12) + 4;
                        }
                        arr[top][i] = height;
                        if (counter == t) {
                            treePosition = new Pair<>(top, i);
                        }
                        counter++;
                    }
                    // we have traversed the whole first row, move down to the next row
                    ++top;
                    dir = Direction.DOWN;
                }
                case DOWN -> {
                    // moving top->bottom
                    for (int i = top; i <= bottom; ++i) {
                        if (counter % change == 0) {
                            height += increment;
                            change = random.nextInt(12) + 4;
                        }
                        arr[i][right] = height;
                        if (counter == t) {
                            treePosition = new Pair<>(i, right);
                        }
                        counter++;
                    }
                    // we have traversed the whole last column, move left to the previous column
                    --right;
                    dir = Direction.LEFT;
                }
                case LEFT -> {
                    // moving right->left
                    for (int i = right; i >= left; --i) {
                        if (counter % change == 0) {
                            height += increment;
                            change = random.nextInt(12) + 4;
                        }
                        arr[bottom][i] = height;
                        if (counter == t) {
                            treePosition = new Pair<>(bottom, i);
                        }
                        counter++;
                    }
                    // we have traversed the whole last row, move up to the previous row
                    --bottom;
                    dir = Direction.UP;
                }
                default -> {
                    // UP - moving bottom->up
                    for (int i = bottom; i >= top; --i) {
                        if (counter % change == 0) {
                            height += increment;
                            change = random.nextInt(12) + 4;
                        }
                        arr[i][left] = height;
                        if (counter == t) {
                            treePosition = new Pair<>(i, left);
                        }
                        counter++;
                    }
                    // we have traversed the whole first col, move right to the next column
                    ++left;
                    dir = Direction.RIGHT;
                }
            }
        }
        return arr;
    }

    public Pair<Integer, Integer> getTreePosition() {
        return treePosition;
    }

    public enum Direction {
        RIGHT, DOWN, LEFT, UP
    }
}
