/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

/**
 * Data storage class for Chameleon Circuit presets. Each letter corresponds to
 * a column of blocks of a 3x3x4 area.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonColumn {

    private int[] a_id = new int[4];
    private int[] b_id = new int[4];
    private int[] c_id = new int[4];
    private int[] d_id = new int[4];
    private int[] e_id = new int[4];
    private int[] f_id = new int[4];
    private int[] g_id = new int[4];
    private int[] h_id = new int[4];
    private int[] i_id = new int[4];
    private byte[] a_data = new byte[4];
    private byte[] b_data = new byte[4];
    private byte[] c_data = new byte[4];
    private byte[] d_data = new byte[4];
    private byte[] e_data = new byte[4];
    private byte[] f_data = new byte[4];
    private byte[] g_data = new byte[4];
    private byte[] h_data = new byte[4];
    private byte[] i_data = new byte[4];
    private int sign_id;
    private int sign_data;

    public TARDISChameleonColumn() {
    }

    public int[] getA_id() {
        return a_id;
    }

    public void setA_id(int[] a_id) {
        this.a_id = a_id;
    }

    public int[] getB_id() {
        return b_id;
    }

    public void setB_id(int[] b_id) {
        this.b_id = b_id;
    }

    public int[] getC_id() {
        return c_id;
    }

    public void setC_id(int[] c_id) {
        this.c_id = c_id;
    }

    public int[] getD_id() {
        return d_id;
    }

    public void setD_id(int[] d_id) {
        this.d_id = d_id;
    }

    public int[] getE_id() {
        return e_id;
    }

    public void setE_id(int[] e_id) {
        this.e_id = e_id;
    }

    public int[] getF_id() {
        return f_id;
    }

    public void setF_id(int[] f_id) {
        this.f_id = f_id;
    }

    public int[] getG_id() {
        return g_id;
    }

    public void setG_id(int[] g_id) {
        this.g_id = g_id;
    }

    public int[] getH_id() {
        return h_id;
    }

    public void setH_id(int[] h_id) {
        this.h_id = h_id;
    }

    public int[] getI_id() {
        return i_id;
    }

    public void setI_id(int[] i_id) {
        this.i_id = i_id;
    }

    public byte[] getA_data() {
        return a_data;
    }

    public void setA_data(byte[] a_data) {
        this.a_data = a_data;
    }

    public byte[] getB_data() {
        return b_data;
    }

    public void setB_data(byte[] b_data) {
        this.b_data = b_data;
    }

    public byte[] getC_data() {
        return c_data;
    }

    public void setC_data(byte[] c_data) {
        this.c_data = c_data;
    }

    public byte[] getD_data() {
        return d_data;
    }

    public void setD_data(byte[] d_data) {
        this.d_data = d_data;
    }

    public byte[] getE_data() {
        return e_data;
    }

    public void setE_data(byte[] e_data) {
        this.e_data = e_data;
    }

    public byte[] getF_data() {
        return f_data;
    }

    public void setF_data(byte[] f_data) {
        this.f_data = f_data;
    }

    public byte[] getG_data() {
        return g_data;
    }

    public void setG_data(byte[] g_data) {
        this.g_data = g_data;
    }

    public byte[] getH_data() {
        return h_data;
    }

    public void setH_data(byte[] h_data) {
        this.h_data = h_data;
    }

    public byte[] getI_data() {
        return i_data;
    }

    public void setI_data(byte[] i_data) {
        this.i_data = i_data;
    }

    public int getSign_id() {
        return sign_id;
    }

    public void setSign_id(int sign_id) {
        this.sign_id = sign_id;
    }

    public int getSign_data() {
        return sign_data;
    }

    public void setSign_data(int sign_data) {
        this.sign_data = sign_data;
    }
}
